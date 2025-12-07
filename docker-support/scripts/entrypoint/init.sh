#!/bin/bash
set -e

get_host_ip() {
    local host_ip=""

    if command -v ip >/dev/null 2>&1; then
        local primary_interface=$(ip route | awk '/default/ {print $5}' | head -n1)
        if [ -n "$primary_interface" ]; then
            host_ip=$(ip -4 addr show "$primary_interface" | awk '/inet / {print $2}' | cut -d'/' -f1 | head -n1)
        fi
    fi

    if [ -z "$host_ip" ] && command -v ifconfig >/dev/null 2>&1; then
        host_ip=$(ifconfig | awk '/inet / {print $2}' | grep -v '127.0.0.1' | head -n1)
    fi

    if [ -z "$host_ip" ]; then
        host_ip=$(hostname -I 2>/dev/null | awk '{print $1}')
    fi

    if [ -z "$host_ip" ]; then
        host_ip=$(env | grep -E '(HOST_IP|HOSTIP|HOST_ADDR)' | cut -d'=' -f2 | head -n1)
    fi

    echo "$host_ip"
}

HOST_IP=$(get_host_ip)

if [ -z "$HOST_IP" ]; then
    echo "错误: 无法自动获取内网IP，请手动设置 HOST_IP 环境变量"
    exit 1
fi

echo "HOST_IP is:${HOST_IP}"

setup_log_permissions() {
    CURRENT_UID=${CURRENT_UID:-$(id -u)}
    CURRENT_GID=${CURRENT_GID:-$(id -g)}
    LOG_DIRS=(
        "/logs/mysql"
        "/logs/redis"
        "/logs/nginx"
        "/logs/lighthouse-insights"
        "/logs/lighthouse-standalone"
	"/logs/lighthouse-demo"
    )
    for host_dir in "${LOG_DIRS[@]}"; do
        mkdir -p "$host_dir"
	if [ -d "$host_dir" ] && [ "$(ls -A "$host_dir" 2>/dev/null)" ]; then
            rm -rf "$host_dir"/* "$host_dir"/.[!.]* "$host_dir"/..?* 2>/dev/null || true
        fi
	chown -R 1001:1001 "$host_dir" 2>/dev/null
	chmod -R 755 "$host_dir" 2>/dev/null
        if [ -d "$host_dir" ]; then
            echo "  目录创建成功: $host_dir"
        else
            echo "  目录创建失败: $host_dir"
        fi
    done

    for host_dir in "${LOG_DIRS[@]}"; do
        if [ -d "$host_dir" ]; then
            perms=$(stat -c "%A %U:%G" "$host_dir" 2>/dev/null || echo "未知权限")
            echo "  $host_dir : $perms"
        else
            echo "  $host_dir : 目录不存在"
        fi
    done
}

generate_cluster_id() {
    local id=""
    if command -v openssl >/dev/null 2>&1 && command -v md5sum >/dev/null 2>&1; then
        openssl rand -hex 8 | md5sum | cut -c1-8
        return
    fi
    while [ "${#id}" -lt 8 ]; do
        if command -v openssl >/dev/null 2>&1; then
            id="${id}$(openssl rand -base64 32 2>/dev/null | tr -dc '0-9a-z')"
        elif [ -c /dev/urandom ]; then
            id="${id}$(tr -dc '0-9a-z' </dev/urandom | head -c 32)"
        elif command -v uuidgen >/dev/null 2>&1; then
            id="${id}$(uuidgen | tr -dc '0-9a-z')"
        else
            id="${id}$(echo "$RANDOM$(date +%s%N)" | md5sum | tr -dc '0-9a-z')"
        fi
    done

    echo "${id:0:8}"
}

setup_log_permissions;

CLUSTER_ID_FILE="/config/cluster.id"

if [ ! -f "$CLUSTER_ID_FILE" ]; then
    RANDOM_CLUSTER_ID=$(generate_cluster_id)
    echo "$RANDOM_CLUSTER_ID" > "$CLUSTER_ID_FILE"
    echo "[INFO] Cluster ID: $RANDOM_CLUSTER_ID (首次生成)"
else
    RANDOM_CLUSTER_ID=$(cat "$CLUSTER_ID_FILE")
    echo "[INFO] Cluster ID: $RANDOM_CLUSTER_ID (已存在)"
fi
echo "[INFO] Cluster ID: $RANDOM_CLUSTER_ID"

TEMPLATE_DIR="/templates"
OUTPUT_DIR="/generated"
CONFIG_DIR="/config"
LOGS_DIR="/logs"
TARGET_COMPONENTS=("lighthouse" "mysql" "nginx")

declare -A VARS_MAP=(
  ["ldp_lighthouse_cluster_id"]="$RANDOM_CLUSTER_ID"
  ["ldp_mysql_master"]="mysql"
  ["ldp_plugins_dir"]="/plugins"
  ["ldp_redis_home"]="/usr"
  ["ldp_lighthouse_nodeip"]="insights"
  ["ldp_redis_cluster_enabled"]="yes"
  ["ldp_host_ip"]=$HOST_IP
  ["ldp_lighthouse_home"]="/app"
  ["ldp_redis_cluster"]="redis_node_1:7101,redis_node_2:7102,redis_node_3:7103,redis_node_4:7104,redis_node_5:7105,redis_node_6:7106"
  ["ldp_lighthouse_ice_locators"]="standalone:4061"
  ["ldp_lighthouse_ice_nodes_ips"]="standalone"
)

parse_json_without_jq() {
    local json_file="$1"
    local content
    content=$(tr -d ' \t\r\n' < "$json_file")
    local components
    components=$(echo "$content" | grep -o '"[^"]*":{' | sed 's/":{//' | tr -d '"')
    for comp in $components; do
        local block
        block=$(echo "$content" | grep -o "\"$comp\":{[^}]*}" | sed -e "s/\"$comp\":{//" -e 's/}$//')
        while IFS= read -r line; do
            [ -z "$line" ] && continue
            local key
            local val
            key=$(echo "$line" | cut -d':' -f1 | tr -d '"')
            val=$(echo "$line" | cut -d':' -f2- | tr -d '"')
            [ -z "$key" ] && continue
            local var_name
            var_name="ldp_${comp}_${key}"
            var_name=$(echo "$var_name" | tr 'A-Z' 'a-z')

            VARS_MAP["$var_name"]="$val"
            echo "[LOAD] $var_name = $val"
        done <<< "$(echo "$block" | tr ',' '\n')"
    done
}

for file in "$CONFIG_DIR"/*.json; do
    [ -f "$file" ] || continue
    parse_json_without_jq "$file"
done

if [ -d "$OUTPUT_DIR" ]; then
	find "$OUTPUT_DIR" -mindepth 1 -exec rm -rf {} + 2>/dev/null || true
fi
mkdir -p "$OUTPUT_DIR"

# 通用文件处理函数，用于非 Redis 组件
process_directory() {
  local src_dir="$1"
  local dest_dir="$2"
  mkdir -p "$dest_dir"
  for entry in "$src_dir"/*; do
    if [ -d "$entry" ]; then
      process_directory "$entry" "$dest_dir/$(basename "$entry")"

    elif [ -f "$entry" ]; then
      local filename=$(basename "$entry")
      tmp_content=$(cat "$entry")
      # 替换所有普通变量
      for key in "${!VARS_MAP[@]}"; do
          value="${VARS_MAP[$key]}"
          value_escaped=${value//&/\\&}
          tmp_content=$(echo "$tmp_content" | sed "s|\${$key}|$value_escaped|g")
      done
      echo "$tmp_content" > "$dest_dir/$filename"
    fi
  done
}

process_redis_cluster() {
    local src_dir="$TEMPLATE_DIR/redis/conf"
    local dest_dir="$OUTPUT_DIR/redis/conf"
    local template_file="$src_dir/redis.conf"
    if [ ! -f "$template_file" ]; then
        echo "[WARN] Redis 模板文件缺失: $template_file"
        return 1
    fi
    mkdir -p "$dest_dir"
    for i in {1..6}; do
        local port_base=7100
        local port=$((port_base + i))
        local output_filename="redis-$port.conf"
        local tmp_content=$(cat "$template_file")
        for key in "${!VARS_MAP[@]}"; do
            value="${VARS_MAP[$key]}"
            value_escaped=${value//&/\\&}
            tmp_content=$(echo "$tmp_content" | sed "s|\${$key}|$value_escaped|g")
        done
        tmp_content=$(echo "$tmp_content" | sed "s|\${port}|$port|g")
        echo "$tmp_content" > "$dest_dir/$output_filename"
        echo "[GENERATE] 生成 $output_filename (Port: $port)"
    done
}


for comp in "${TARGET_COMPONENTS[@]}"; do
  SRC="$TEMPLATE_DIR/$comp"
  DEST="$OUTPUT_DIR/$comp"
  if [ -d "$SRC" ]; then
    echo "[COMPONENT] 处理 $comp"
    process_directory "$SRC" "$DEST"
  else
    echo "[WARN] 模板缺失: $SRC"
  fi
done


process_redis_cluster

chown -R 1001:1001 $TEMPLATE_DIR
chown -R 1001:1001 $OUTPUT_DIR
chown -R 1001:1001 $LOGS_DIR

mv $OUTPUT_DIR/lighthouse/conf/ldp-site-standalone.xml $OUTPUT_DIR/lighthouse/conf/ldp-site.xml

echo "[INFO] 配置生成完毕。"

#!/bin/bash
set -e

HOST_IP="$1"
if [ -z "$HOST_IP" ]; then
    echo "错误: 未接收到IP参数"
    echo "用法: $0 <host_ip>"
    exit 1
fi

echo "[INFO] 接收到宿主机IP参数: $HOST_IP"

setup_log_permissions() {
    CURRENT_UID=${CURRENT_UID:-$(id -u)}
    CURRENT_GID=${CURRENT_GID:-$(id -g)}
    LOG_DIRS=(
        "../logs/mysql"
        "../logs/redis" 
        "../logs/nginx"
        "../logs/springboot"
        "../logs/lighthouse"
    )
    for host_dir in "${LOG_DIRS[@]}"; do
        mkdir -p "$host_dir"
        chown -R $CURRENT_UID:$CURRENT_GID "$host_dir" 2>/dev/null
        chmod -R 777 "$host_dir" 2>/dev/null
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

RANDOM_CLUSTER_ID=$(generate_cluster_id)
echo "[INFO] Cluster ID: $RANDOM_CLUSTER_ID"

TEMPLATE_DIR="../templates"
OUTPUT_DIR="../generated"
CONFIG_DIR="../config"
TARGET_COMPONENTS=("lighthouse" "mysql" "nginx") 

declare -A VARS_MAP=(
  ["ldp_lighthouse_cluster_id"]="$RANDOM_CLUSTER_ID"
  ["ldp_mysql_master"]="mysql"
  ["ldp_mysql_home"]="/usr"
  ["ldp_data_dir"]="/var/lib"
  ["ldp_plugins_dir"]="/plugins"
  ["ldp_redis_home"]="/usr"  
  ["ldp_redis_cluster_enabled"]="yes"
  ["ldp_host_ip"]=$HOST_IP
  ["ldp_lighthouse_home"]="/app"
  ["ldp_redis_cluster"]="redis-node-1:7101,redis-node-2:7102,redis-node-3:7103,redis-node-4:7104,redis-node-5:7105,redis-node-6:7106"	  
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

mv $OUTPUT_DIR/lighthouse/conf/ldp-site-standalone.xml $OUTPUT_DIR/lighthouse/conf/ldp-site.xml

echo "[INFO] 配置生成完毕。"

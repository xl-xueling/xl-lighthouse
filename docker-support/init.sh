#!/bin/bash
set -e


generate_cluster_id() {
    local id=""

    # 优先 openssl + md5sum
    if command -v openssl >/dev/null 2>&1 && command -v md5sum >/dev/null 2>&1; then
        openssl rand -hex 8 | md5sum | cut -c1-8
        return
    fi

    # 其他备选方案（保证至少 8 字符）
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

RANDOM_CLUSTER_ID=$(generate_cluster_id)
echo "[INFO] Cluster ID: $RANDOM_CLUSTER_ID"

TEMPLATE_DIR="../templates"
OUTPUT_DIR="../generated"
CONFIG_DIR="../config"

TARGET_COMPONENTS=("lighthouse" "redis" "mysql" "nginx")

declare -A VARS_MAP=(
  ["ldp_lighthouse_cluster_id"]="$RANDOM_CLUSTER_ID"
  ["ldp_mysql_operate_user"]="lighthouse"
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

echo
echo "[INFO] 加载 config/*.json 配置..."

for file in "$CONFIG_DIR"/*.json; do
    [ -f "$file" ] || continue
    echo "[INFO] 解析: $file"
    parse_json_without_jq "$file"
done

if [ -d "$OUTPUT_DIR" ]; then
  rm -rf "$OUTPUT_DIR"
fi
mkdir -p "$OUTPUT_DIR"

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
for key in "${!VARS_MAP[@]}"; do
    value="${VARS_MAP[$key]}"
    value_escaped=${value//&/\\&}
    tmp_content=$(echo "$tmp_content" | sed "s|\${$key}|$value_escaped|g")
done
echo "$tmp_content" > "$dest_dir/$filename"	
    fi
  done
}

for comp in "${TARGET_COMPONENTS[@]}"; do
  SRC="$TEMPLATE_DIR/$comp"
  DEST="$OUTPUT_DIR/$comp"
  if [ -d "$SRC" ]; then
    process_directory "$SRC" "$DEST"
  else
    echo "[WARN] 模板缺失: $SRC"
  fi
done

echo
echo "========== 完成 =========="
echo "生成目录：$OUTPUT_DIR"
echo "Cluster ID：$RANDOM_CLUSTER_ID"
echo "==================================="


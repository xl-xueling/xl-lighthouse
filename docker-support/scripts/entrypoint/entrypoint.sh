#!/bin/bash
set -e

CONFIG_FILE="${CONFIG_FILE:-/app/runtime/config.json}"
SERVICE_TYPE="${1:-${SERVICE_TYPE:-insights}}"

read_timezone() {
    local default_tz="Asia/Shanghai"
    local cfg_tz=""
    if [ -f "$CONFIG_FILE" ]; then
        if command -v jq >/dev/null 2>&1; then
            cfg_tz=$(jq -r '.lighthouse.timezone // empty' "$CONFIG_FILE")
        else
            cfg_tz=$(grep '"timezone"' "$CONFIG_FILE" 2>/dev/null | \
                     sed 's/.*"\([^"]*\)".*/\1/')
        fi
        if [ -n "$cfg_tz" ]; then
            echo "$cfg_tz"
            return
        fi
    fi
    if [ -n "$TZ" ]; then
        echo "$TZ"
        return
    fi
    echo "$default_tz"
}

TZ=$(read_timezone)

echo "Service Type: $SERVICE_TYPE"
echo "Config File: $CONFIG_FILE"
echo "Timezone: $TZ"
echo "Current Time: $(date)"

read_memory_config() {
    local service=$1
    local default_xmx=$2
    local default_xms=$3
    if [ -n "$JAVA_XMX" ] && [ -n "$JAVA_XMS" ]; then
        XMX=$JAVA_XMX
        XMS=$JAVA_XMS
        echo "Memory from environment: -Xmx$XMX -Xms$XMS"
    elif [ -f "$CONFIG_FILE" ]; then
        if command -v jq >/dev/null 2>&1; then
            XMX=$(jq -r ".lighthouse.${service}_xmx_memory // \"$default_xmx\"" "$CONFIG_FILE")
            XMS=$(jq -r ".lighthouse.${service}_xms_memory // \"$default_xms\"" "$CONFIG_FILE")
            echo "Memory from config file: -Xmx$XMX -Xms$XMS"
        else
            XMX=$(grep "${service}_xmx_memory" "$CONFIG_FILE" 2>/dev/null | sed 's/.*"\([^"]*\)".*/\1/' || echo "$default_xmx")
            XMS=$(grep "${service}_xms_memory" "$CONFIG_FILE" 2>/dev/null | sed 's/.*"\([^"]*\)".*/\1/' || echo "$default_xms")
            echo "Memory from config file (grep): -Xmx$XMX -Xms$XMS"
        fi
    else
        XMX=$default_xmx
        XMS=$default_xms
        echo "Memory from defaults: -Xmx$XMX -Xms$XMS"
    fi
}

set_fixed_memory() {
    local xmx=$1
    local xms=$2
    XMX=$xmx
    XMS=$xms
    echo "Memory (fixed): -Xmx$XMX -Xms$XMS"
}

build_base_java_opts() {
    local base_opts=""
    base_opts="$base_opts -Xmx${XMX} -Xms${XMS}"
    base_opts="$base_opts -Duser.timezone=${TZ}"
    base_opts="$base_opts -Dfile.encoding=UTF-8"
    if [ -n "$JAVA_OPTS" ]; then
        base_opts="$base_opts $JAVA_OPTS"
    fi
    echo "$base_opts"
}

case "$SERVICE_TYPE" in
    insights)
        echo "Starting Insights Service..."
        read_memory_config "insights" "256M" "256M"
        FINAL_JAVA_OPTS=$(build_base_java_opts)
        LOG_CONFIG="${LOG_CONFIG:-log4j2-insights.xml}"
        SPRING_CONFIG="${SPRING_CONFIG:-lighthouse-insights.yml}"
        echo "Log Config: $LOG_CONFIG"
        echo "Spring Config: $SPRING_CONFIG"
        echo "JAVA_OPTS: $FINAL_JAVA_OPTS"
        echo "Starting application..."
        exec java ${FINAL_JAVA_OPTS} \
            -Dloader.path=/app/lib \
            -Dlog4j.configurationFile=/app/conf/${LOG_CONFIG} \
            -Dspring.config.location=file:/app/conf/${SPRING_CONFIG} \
            -jar /app/app.jar
        ;;

    standalone)
        echo "Starting Standalone Service..."
        read_memory_config "standalone" "500M" "500M"
        FINAL_JAVA_OPTS=$(build_base_java_opts)
        FINAL_JAVA_OPTS="$FINAL_JAVA_OPTS -XX:+UseG1GC"
        LOG_CONFIG="${LOG_CONFIG:-log4j2-standalone.xml}"
        MAIN_CLASS="${MAIN_CLASS:-com.dtstep.lighthouse.standalone.executive.LightStandaloneEntrance}"
        echo "Log Config: $LOG_CONFIG"
        echo "Main Class: $MAIN_CLASS"
        echo "JAVA_OPTS: $FINAL_JAVA_OPTS"
        echo "Starting application..."
        exec java ${FINAL_JAVA_OPTS} \
            -Dlog4j.configurationFile=/app/conf/${LOG_CONFIG} \
            -cp /app/lib/*:/app/app.jar \
            ${MAIN_CLASS}
        ;;

    demo-start)
        echo "Starting Demo Start Service..."
        set_fixed_memory "256M" "256M"
        FINAL_JAVA_OPTS=$(build_base_java_opts)
        LOG_CONFIG="${LOG_CONFIG:-log4j2-demo.xml}"
        MAIN_CLASS="${MAIN_CLASS:-com.dtstep.lighthouse.test.LDPFlowTestInstance}"
        LOG_FILE="${LOG_FILE:-/app/logs/lighthouse-demo/system_console.log}"
        LDP_PARAM="${LDP_PARAM:=100}"
        echo "Log Config: $LOG_CONFIG"
        echo "Main Class: $MAIN_CLASS"
        echo "JAVA_OPTS: $FINAL_JAVA_OPTS"
        echo "LDP_PARAM: $LDP_PARAM"
        echo "Console Log: $LOG_FILE"
        echo "Starting application..."
        mkdir -p "$(dirname "$LOG_FILE")"
        exec java ${FINAL_JAVA_OPTS} \
            -Dlog4j.configurationFile=/app/conf/${LOG_CONFIG} \
            -cp /app/lib/*:/app/app.jar \
            ${MAIN_CLASS} \
            ${LDP_PARAM} \
            2>&1 | tee -a "$LOG_FILE"
        ;;
    demo-init)
        echo "Starting Demo Init Service..."
        set_fixed_memory "256M" "256M"
        FINAL_JAVA_OPTS=$(build_base_java_opts)
        LOG_CONFIG="${LOG_CONFIG:-log4j2-demo.xml}"
        MAIN_CLASS="${MAIN_CLASS:-com.dtstep.lighthouse.test.example.StartExample}"
        echo "Log Config: $LOG_CONFIG"
        echo "Main Class: $MAIN_CLASS"
        echo "JAVA_OPTS: $FINAL_JAVA_OPTS"
        echo "Starting application..."
        exec java ${FINAL_JAVA_OPTS} \
            -Dlog4j.configurationFile=/app/conf/${LOG_CONFIG} \
            -cp /app/lib/*:/app/app.jar \
            ${MAIN_CLASS}
        ;;
    *)
        echo "ERROR: Unknown service type: $SERVICE_TYPE"
        exit 1
        ;;
esac

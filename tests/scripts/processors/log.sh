#!/bin/bash

DEFAULT_LOG_FILE="$KONTINUUM_DIR/.logs/log-file.log"
DEFAULT_LOG_LEVEL="ALL"

LOG_LEVEL_ALL=0
LOG_LEVEL_IO_ONLY=1
LOG_LEVEL_CMD_ONLY=2
LOG_LEVEL_INFO=3

LOG_LEVEL_NAMES="ALL IO_ONLY CMD_ONLY INFO"

init_logging() {
    local log_file="${1:-${LOG_FILE:-$DEFAULT_LOG_FILE}}"
    local log_level_name="${2:-${LOG_LEVEL_NAME:-$DEFAULT_LOG_LEVEL}}"

    local log_level_value
    case "${log_level_name^^}" in
        "ALL") log_level_value=$LOG_LEVEL_ALL ;;
        "IO_ONLY") log_level_value=$LOG_LEVEL_IO_ONLY ;;
        "CMD_ONLY") log_level_value=$LOG_LEVEL_CMD_ONLY ;;
        "INFO") log_level_value=$LOG_LEVEL_INFO ;;
        *)
            echo "Invalid log level: $log_level_name. Valid levels: $LOG_LEVEL_NAMES"
            return 1
            ;;
    esac

    export LOG_FILE="$log_file"
    export LOG_LEVEL="$log_level_value"
    export LOG_LEVEL_NAME="$log_level_name"

    local log_dir=$(dirname "$LOG_FILE")
    mkdir -p "$log_dir"

    touch "$LOG_FILE"

    log_info "Logging initialized - File: $LOG_FILE, Level: $log_level_name"
}

get_timestamp() {
    date '+%Y-%m-%d %H:%M:%S'
}

_get_log_level_value() {
    local level="$1"
    case "${level^^}" in
        "ALL") echo $LOG_LEVEL_ALL ;;
        "IO_ONLY") echo $LOG_LEVEL_IO_ONLY ;;
        "CMD_ONLY") echo $LOG_LEVEL_CMD_ONLY ;;
        "INFO") echo $LOG_LEVEL_INFO ;;
        *) echo -1 ;; # Invalid level
    esac
}

_log() {
    local level="$1"
    local message="$2"
    local timestamp=$(get_timestamp)

    local message_level=$(_get_log_level_value "$level")
    local current_level=${LOG_LEVEL:-$LOG_LEVEL_INFO}

    if [ "$message_level" -ge "$current_level" ]; then
        echo "[$timestamp] [$level] $message" | tee -a "$LOG_FILE" >/dev/null
    fi
}

log_mvn() { _log "ALL" "$1"; }
log_cmd() { _log "CMD_ONLY" "$1"; }

log_info() {
  echo -e "\033[1;32m[INFO]  $1\033[0m"
  _log "INFO" "$1";
}

log_error() {
    echo -e "\033[1;31m[ERROR] $1\033[0m" >&2
    _log "INFO" "$1"
    exit 1
}

log_warn() {
    echo -e "\033[1;33m[WARN]  $1\033[0m" >&2
    _log "INFO" "$1"
}

log_io()  {
  local inputs="$1"
  local output="$2"
  local io_message=""

  if [ -n "$inputs" ]; then
      io_message+=" | Inputs: $inputs"
  fi

  if [ -n "$output" ]; then
      io_message+=" | Outputs: $output"
  fi

  _log "IO_ONLY" "$io_message";
}

set_log_level() {
    local level="${1^^}"
    local level_value=$(_get_log_level_value "$level")

    if [ "$level_value" -ge 0 ]; then
        export LOG_LEVEL="$level_value"
        export LOG_LEVEL_NAME="$level"
        log_info "Log level set to: $level"
    else
        echo "Invalid log level: $level. Valid levels: $LOG_LEVEL_NAMES"
    fi
}

# Export functions for use in other scripts
export -f init_logging log_mvn log_io log_cmd log_info _log _get_log_level_value get_timestamp set_log_level
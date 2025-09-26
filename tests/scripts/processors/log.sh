#!/bin/bash

RED='\033[31m'
GREEN='\033[32m'
CYAN='\033[36m'
RESET='\033[0m'


LOG_LEVEL="ALL"
LOG_FILE=""
LOG_TO_CONSOLE="true"

logger_init() {
    LOG_LEVEL="${1:-INFO}"
    LOG_FILE="$2"
    LOG_TO_CONSOLE="${3:-true}"

    case "$LOG_LEVEL" in
        "ALL"|"MVN"|"CONFIG"|"INFO"|"ERROR")
            ;;
        *)
            echo "Warning: Invalid log level '$LOG_LEVEL'. Defaulting to INFO." >&2
            LOG_LEVEL="INFO"
            ;;
    esac

    if [[ -n "$LOG_FILE" ]]; then
        local dir=$(dirname "$LOG_FILE")
        if [[ ! -d "$dir" ]]; then
            mkdir -p "$dir" || {
                echo "Error: Could not create directory for log file '$LOG_FILE'." >&2
                LOG_FILE=""
            }
        fi
    fi
}

should_log() {
    local log_type="$1"
    case "$LOG_LEVEL" in
        "ALL")
            return 0  # Log everything
            ;;
        "MVN")
            [[ "$log_type" == "MVN" || "$log_type" == "INFO" || "$log_type" == "ERROR" ]]
            return $?
            ;;
        "CONFIG")
            [[ "$log_type" == "CONFIG" || "$log_type" == "INFO" || "$log_type" == "ERROR" ]]
            return $?
            ;;
        "INFO")
            [[ "$log_type" == "INFO" || "$log_type" == "ERROR" ]]
            return $?
            ;;
        "ERROR")
            [[ "$log_type" == "ERROR" ]]
            return $?
            ;;
        *)
            return 1  # Should not log
            ;;
    esac
}

_log_output() {
    local level="$1"
    local message="$2"
    local timestamp=$(date '+%Y-%m-%d %H:%M:%S')

    local log_entry="[$timestamp] $level: $message"

    if [[ -n "$LOG_FILE" ]]; then
        echo "$log_entry" >> "$LOG_FILE" 2>/dev/null || {
            echo "Error: Failed to write to log file '$LOG_FILE'." >&2
        }
    fi

    if [[ "$LOG_TO_CONSOLE" == "true" ]]; then
        local color=""
        case "$level" in
            "ERROR")
                color="$RED"
                ;;
            "INFO")
                color="$GREEN"
                ;;
            "CONFIG")
                color="$CYAN"
                ;;
            *)
                color="$RESET"
                ;;
        esac
        local console_entry="[$timestamp] ${color} $level ${RESET}: $message${RESET}"
        echo -e "$console_entry" >&2
    fi
}

log_mvn() {
    local message="$*"
    if should_log "MVN"; then
        _log_output "MVN" "$message"
    fi
}

log_config() {
    local inputs="$1"
    local outputs="$2"

    if [ -n "$inputs" ]; then
      for input in $inputs; do
          if should_log "CONFIG"; then
            _log_output "CONFIG" "Input - $input"
          fi
      done
    fi

    if [ -n "$outputs" ]; then
      for output in $outputs; do
        if should_log "CONFIG"; then
          _log_output "CONFIG" "Output - $output"
        fi
      done
    fi
}

log_info() {
    local message="$*"
    if should_log "INFO"; then
        _log_output "INFO" "$message"
    fi
}

log_error() {
    local message="$*"
    if should_log "ERROR"; then
        _log_output "ERROR" "$message"
    fi
}

export log_mvn log_config log_info log_error
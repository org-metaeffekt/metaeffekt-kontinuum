#!/bin/bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
LOGGER_PATH="$SCRIPT_DIR/../log.sh"
CASE="mirror/mirror_download-index-02.sh"

CASES_DIR="$SCRIPT_DIR/../../cases"
PROCESSORS_DIR="$SCRIPT_DIR/../../../../processors"

source "$SCRIPT_DIR/../shared.sh"
source "$LOGGER_PATH"

logger_init "$SCRIPT_DIR/../../../../.logs/$(basename "$0" .sh).log"

source_case_file() {
    local case_file="$1"
    if [[ -f "$CASES_DIR/$case_file" ]]; then
        source "$CASES_DIR/$case_file"
    elif [[ -f "$case_file" ]]; then
        source "$case_file"
    else
        log_error "Failed to source case file: $case_file"
        exit 1
    fi
}

TEST_PORT=18234
SERVER_PID=""
TEST_TARGET_DIR="/tmp/mirror-download-index-test"

start_test_server() {
    log_info "Starting local HTTP server with basic auth on port $TEST_PORT..."
    python3 - "$TEST_PORT" <<'PYEOF' &
import sys, base64, io, tarfile
from http.server import HTTPServer, BaseHTTPRequestHandler

PORT = int(sys.argv[1])
VALID_USER = "mirror_test_user"
VALID_PASS = "mirror_test_pass_2024"
REALM = "Mirror Test"

def make_tar_gz():
    buf = io.BytesIO()
    with tarfile.open(fileobj=buf, mode="w:gz") as tf:
        info = tarfile.TarInfo(name="placeholder.txt")
        data = b"placeholder"
        info.size = len(data)
        tf.addfile(info, io.BytesIO(data))
    return buf.getvalue()

TAR_GZ_BYTES = make_tar_gz()

class AuthHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        auth = self.headers.get("Authorization")
        if auth and auth.startswith("Basic "):
            try:
                decoded = base64.b64decode(auth[6:]).decode("utf-8")
                user, password = decoded.split(":", 1)
                if user == VALID_USER and password == VALID_PASS:
                    self.send_response(200)
                    self.send_header("Content-Type", "application/octet-stream")
                    self.send_header("Content-Length", str(len(TAR_GZ_BYTES)))
                    self.end_headers()
                    self.wfile.write(TAR_GZ_BYTES)
                    return
            except Exception:
                pass
        self.send_response(401)
        self.send_header("WWW-Authenticate", f'Basic realm="{REALM}"')
        self.send_header("Content-Length", "13")
        self.end_headers()
        self.wfile.write(b"Unauthorized")

    def log_message(self, format, *args):
        pass

httpd = HTTPServer(("127.0.0.1", PORT), AuthHandler)
httpd.serve_forever()
PYEOF
    SERVER_PID=$!
    log_info "Server PID: $SERVER_PID"
}

validate_auth_enforced() {
    log_info "Validating auth is enforced (request without credentials should fail)..."
    local http_code
    http_code=$(curl -s -o /dev/null -w "%{http_code}" "http://127.0.0.1:${TEST_PORT}/mirror-index.tar.gz" 2>/dev/null || true)
    if [[ "$http_code" != "401" ]]; then
        log_error "Expected 401 from test server without auth, got $http_code"
        return 1
    fi
    log_info "Auth enforced: request without credentials returned 401 as expected"

    log_info "Validating auth works (request with credentials should succeed)..."
    http_code=$(curl -s -o /dev/null -w "%{http_code}" -u "mirror_test_user:mirror_test_pass_2024" "http://127.0.0.1:${TEST_PORT}/mirror-index.tar.gz" 2>/dev/null || true)
    if [[ "$http_code" != "200" ]]; then
        log_error "Expected 200 from test server with auth, got $http_code"
        return 1
    fi
    log_info "Auth works: request with credentials returned 200 as expected"
}

cleanup() {
    log_info "Cleaning up..."
    if [[ -n "$SERVER_PID" ]] && kill -0 "$SERVER_PID" 2>/dev/null; then
        kill "$SERVER_PID" 2>/dev/null || true
        wait "$SERVER_PID" 2>/dev/null || true
    fi
    rm -rf "$TEST_TARGET_DIR"
}

trap cleanup EXIT

main() {
    start_test_server
    sleep 1

    if ! kill -0 "$SERVER_PID" 2>/dev/null; then
        log_error "Failed to start test server"
        exit 1
    fi

    validate_auth_enforced

    export MIRROR_TEST_PORT="$TEST_PORT"
    export MIRROR_TEST_TARGET_DIR="$TEST_TARGET_DIR"

    log_info "Running Maven build with auth..."
    source_case_file "$CASE"

    local cmd=(mvn -f "$PROCESSORS_DIR/mirror/mirror_download-index.xml" compile)
    [ "${DEBUG:-}" = "true" ] && cmd+=("-X")
    cmd+=("-Denv.vulnerability.mirror.dir=$MIRROR_TARGET_DIR")
    cmd+=("-Dparam.mirror.archive.url=$MIRROR_ARCHIVE_URL")
    cmd+=("-Dparam.mirror.archive.username=$MIRROR_ARCHIVE_USERNAME")
    cmd+=("-Dparam.mirror.archive.password=$MIRROR_ARCHIVE_PASSWORD")

    log_info "Maven command: ${cmd[*]}"

    if "${cmd[@]}"; then
        log_info "Maven build completed successfully"
    else
        log_error "Maven build failed"
        exit 1
    fi

    log_info "All tests passed"
}

main "$@"

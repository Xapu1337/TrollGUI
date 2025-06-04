#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(dirname "$SCRIPT_DIR")"
cd "$REPO_ROOT"

if ! command -v mvn >/dev/null 2>&1; then
  "$SCRIPT_DIR/install-maven.sh"
fi

mvn clean package

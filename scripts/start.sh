#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(dirname "$SCRIPT_DIR")"
cd "$REPO_ROOT"

if ! command -v mvn >/dev/null 2>&1; then
  "$SCRIPT_DIR/install-maven.sh"
fi

# Build if jar doesn't exist
declare -a jars=(target/*.jar build_out/*.jar build_out/*.war)
JAR=""
for f in "${jars[@]}"; do
  if [ -f "$f" ]; then
    JAR="$f"
    break
  fi
done

if [ -z "$JAR" ]; then
  "$SCRIPT_DIR/build.sh"
  for f in "${jars[@]}"; do
    if [ -f "$f" ]; then
      JAR="$f"
      break
    fi
  done
fi

if [ -z "$JAR" ]; then
  echo "Unable to find built jar"
  exit 1
fi

java -jar "$JAR" "$@"

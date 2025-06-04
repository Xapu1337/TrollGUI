#!/usr/bin/env bash
set -e

if command -v mvn >/dev/null 2>&1; then
  echo "Maven already installed"
  exit 0
fi

apt-get update
apt-get install -y maven

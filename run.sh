#!/bin/bash
# Startup script for Lecture02 - loads environment variables from .env file

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Load .env file if it exists
if [ -f "$SCRIPT_DIR/.env" ]; then
    echo "Loading environment variables from .env file..."
    set -a
    source "$SCRIPT_DIR/.env"
    set +a
else
    echo "WARNING: .env file not found at $SCRIPT_DIR/.env"
    echo "Copy .env.example to .env and fill in your credentials."
    echo "The application will start but OAuth login will fail without credentials."
fi

# Run the Spring Boot application
exec ./mvnw spring-boot:run -f "$SCRIPT_DIR/pom.xml" "$@"

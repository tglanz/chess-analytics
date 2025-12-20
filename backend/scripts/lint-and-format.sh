#!/bin/bash

set -e

cd "$(dirname "$0")/.."

if [ "$1" = "--validate" ]; then
    echo "Validating code formatting with Spotless..."
    ./gradlew spotlessCheck

    echo "Running Checkstyle linter..."
    ./gradlew checkstyleMain checkstyleTest

    echo "Validation passed"
else
    echo "Formatting code with Spotless..."
    ./gradlew spotlessApply

    echo "Running Checkstyle linter..."
    ./gradlew checkstyleMain checkstyleTest

    echo "Done"
fi

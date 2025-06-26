#!/bin/bash

# SonarQube Analysis Script for News App
# This script runs the necessary build tasks and then performs SonarQube analysis

set -e

echo "🔍 Starting SonarQube Analysis for News App"
echo "============================================"

# Clean the project
echo "🧹 Cleaning project..."
./gradlew clean

# Run linting
echo "🔍 Running Ktlint..."
./gradlew ktlintCheck

# Run unit tests with coverage
echo "🧪 Running unit tests with coverage..."
./gradlew testDebugUnitTest

# Generate JaCoCo coverage report
echo "📊 Generating coverage report..."
./gradlew jacocoTestReport

# Run SonarQube analysis
echo "🚀 Running SonarQube analysis..."
if [ -n "$SONAR_TOKEN" ]; then
    echo "Using SONAR_TOKEN from environment"
    ./gradlew sonar -Dsonar.token="$SONAR_TOKEN"
elif [ -n "$SONAR_LOGIN" ]; then
    echo "Using SONAR_LOGIN from environment"
    ./gradlew sonar -Dsonar.login="$SONAR_LOGIN"
else
    echo "⚠️  No SONAR_TOKEN or SONAR_LOGIN environment variable found"
    echo "You can run SonarQube analysis manually with:"
    echo "./gradlew sonar -Dsonar.token=YOUR_TOKEN"
    echo ""
    echo "Or set up environment variables:"
    echo "export SONAR_TOKEN=your_sonarcloud_token"
    echo "export SONAR_ORGANIZATION=your_organization_key  # for SonarCloud"
    echo ""
    echo "For local SonarQube server:"
    echo "export SONAR_HOST_URL=http://localhost:9000"
    echo "export SONAR_LOGIN=your_local_token"
fi

echo "✅ SonarQube analysis completed!"

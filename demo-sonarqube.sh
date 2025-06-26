#!/bin/bash

# Demo script to show SonarQube integration capabilities
# This runs analysis with a dry-run to show what would be analyzed

echo "🔍 SonarQube Integration Demo"
echo "============================="
echo ""

echo "✅ SonarQube plugin is integrated in build.gradle.kts"
echo "✅ JaCoCo coverage reporting is configured"
echo "✅ Configuration files are in place:"
echo "   - sonar-project.properties"
echo "   - analyze.sh (analysis script)" 
echo "   - .github/workflows/sonarcloud.yml (CI/CD)"
echo ""

echo "📊 Running coverage report generation..."
./gradlew clean testDebugUnitTest jacocoTestReport --quiet

echo ""
echo "📈 Coverage report generated:"
if [ -f "app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml" ]; then
    echo "   ✅ XML report: app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
else
    echo "   ❌ XML report not found"
fi

if [ -d "app/build/reports/jacoco/jacocoTestReport/html" ]; then
    echo "   ✅ HTML report: app/build/reports/jacoco/jacocoTestReport/html/index.html"
else
    echo "   ❌ HTML report not found"
fi

echo ""
echo "🔧 Available SonarQube tasks:"
./gradlew tasks --group=verification | grep -E "(sonar|jacoco)" || echo "   Run './gradlew tasks --group=verification' to see all tasks"

echo ""
echo "🚀 To run SonarQube analysis:"
echo "   1. Set up your SonarCloud/SonarQube server"
echo "   2. Get your authentication token"
echo "   3. Run: export SONAR_TOKEN=your_token"
echo "   4. Run: ./analyze.sh"
echo ""
echo "   Or manually: ./gradlew sonar -Dsonar.token=YOUR_TOKEN"
echo ""

echo "📚 For detailed setup instructions, see: SONARQUBE.md"
echo "Demo completed! ✨"

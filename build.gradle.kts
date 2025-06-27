// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    dependencies {
        classpath("org.bouncycastle:bcutil-jdk18on:1.78.1")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.sonarqube)
}

sonar {
    properties {
        property("sonar.projectKey", "news-app")
        property("sonar.projectName", "News App")
        property("sonar.projectVersion", "1.0")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.language", "kotlin")
        
        // Source directories
        property("sonar.sources", "app/src/main")
        property("sonar.tests", "app/src/test,app/src/androidTest")
        
        // Kotlin-specific properties
        property("sonar.kotlin.source.version", "2.1")
        
        // Android-specific properties
        property("sonar.android.lint.report", "app/build/reports/lint-results.xml")
        
        // Test reports
        property("sonar.junit.reportPaths", "app/build/test-results/testDebugUnitTest")
        property("sonar.coverage.jacoco.xmlReportPaths", "app/build/reports/jacoco/testDebugUnitTestCoverage/testDebugUnitTestCoverage.xml")
        
        // Exclusions
        property("sonar.exclusions", 
            "**/*Test.kt," +
            "**/*Tests.kt," +
            "**/build/**," +
            "**/generated/**," +
            "**/*.xml," +
            "**/*.json"
        )
        
        property("sonar.test.exclusions", 
            "**/build/**," +
            "**/generated/**"
        )
        
        // Host URL (default to SonarCloud, can be overridden via command line or environment)
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

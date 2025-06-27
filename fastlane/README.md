fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## Android

### android setup_environment

```sh
[bundle exec] fastlane android setup_environment
```

Setup development environment and install dependencies

### android clean

```sh
[bundle exec] fastlane android clean
```

Clean the project

### android lint

```sh
[bundle exec] fastlane android lint
```

Run Ktlint code style checks

### android unit_tests

```sh
[bundle exec] fastlane android unit_tests
```

Run unit tests

### android ui_tests

```sh
[bundle exec] fastlane android ui_tests
```

Run instrumented tests on connected device/emulator

### android test_suite

```sh
[bundle exec] fastlane android test_suite
```

Run complete test suite (all tests before building)

### android build_debug

```sh
[bundle exec] fastlane android build_debug
```

Build debug APK (runs complete test suite first)

### android build_release

```sh
[bundle exec] fastlane android build_release
```

Build release APK (runs complete test suite first)

### android build_aab

```sh
[bundle exec] fastlane android build_aab
```

Build release AAB (runs complete test suite first)

### android sonar_analysis

```sh
[bundle exec] fastlane android sonar_analysis
```

Run SonarQube code quality analysis

### android quality_check

```sh
[bundle exec] fastlane android quality_check
```

Run comprehensive quality checks (lint + unit tests)

### android full_test

```sh
[bundle exec] fastlane android full_test
```

Run full test suite (lint + unit tests + UI tests)

### android ci_pipeline

```sh
[bundle exec] fastlane android ci_pipeline
```

Complete build and test pipeline

### android release_pipeline

```sh
[bundle exec] fastlane android release_pipeline
```

Complete release pipeline (quality + build release)

### android deploy_internal

```sh
[bundle exec] fastlane android deploy_internal
```

Deploy to Google Play Internal Testing

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).

# Gemfile for News App Fastlane

source "https://rubygems.org"

gem "fastlane", "~> 2.219"

# Optional but useful gems
gem "dotenv"
gem "json"

plugins_path = File.join(File.dirname(__FILE__), 'fastlane', 'Pluginfile')
eval_gemfile(plugins_path) if File.exist?(plugins_path)

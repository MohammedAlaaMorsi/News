#!/bin/bash

# Fastlane setup and execution script for News App
# This script helps set up and run common Fastlane operations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🚀 News App Fastlane Helper${NC}"
echo "=================================="

# Function to check if fastlane is installed
check_fastlane() {
    echo -e "${BLUE}🔍 Checking Fastlane installation...${NC}"
    
    # Check if fastlane command is available
    if ! command -v fastlane &> /dev/null; then
        echo -e "${RED}❌ Fastlane is not installed${NC}"
        echo -e "${YELLOW}Installing Fastlane...${NC}"
        install_fastlane_system
    else
        echo -e "${GREEN}✅ Fastlane is installed${NC}"
        fastlane --version
        
        # Check if we should use bundle exec
        if [ -f "Gemfile" ] && command -v bundle &> /dev/null; then
            echo -e "${BLUE}📋 Gemfile detected, ensuring bundle dependencies...${NC}"
            bundle install --quiet
            echo -e "${GREEN}✅ Using bundle exec for Fastlane${NC}"
        fi
    fi
}

# Function to install Fastlane system-wide
install_fastlane_system() {
    echo -e "${YELLOW}📦 Installing Fastlane...${NC}"
    
    # Check if bundler is available and Gemfile exists
    if command -v bundle &> /dev/null && [ -f "Gemfile" ]; then
        echo -e "${BLUE}📋 Using Bundler to install dependencies...${NC}"
        bundle install
        
        # Verify installation
        if bundle exec fastlane --version &> /dev/null; then
            echo -e "${GREEN}✅ Fastlane installed successfully via bundle${NC}"
            return 0
        fi
    fi
    
    # Fallback to gem installation
    echo -e "${BLUE}💎 Installing Fastlane globally via gem...${NC}"
    if command -v gem &> /dev/null; then
        # Try user install first
        if gem install --user-install fastlane; then
            echo -e "${GREEN}✅ Fastlane installed successfully via gem${NC}"
            
            # Dynamically get the correct gem binary path
            setup_gem_path
            
            # Verify installation
            if command -v fastlane &> /dev/null; then
                fastlane --version
                return 0
            fi
        fi
        
        # If user install fails, try system install
        echo -e "${YELLOW}⚠️  User install failed, trying system install...${NC}"
        if sudo gem install fastlane; then
            echo -e "${GREEN}✅ Fastlane installed system-wide${NC}"
            setup_gem_path
            return 0
        fi
    fi
    
    # If all else fails
    echo -e "${RED}❌ Failed to install Fastlane automatically${NC}"
    echo -e "${YELLOW}Please install Fastlane manually using one of these methods:${NC}"
    echo -e "${BLUE}  1. gem install --user-install fastlane${NC}"
    echo -e "${BLUE}  2. sudo gem install fastlane${NC}"
    echo -e "${BLUE}  3. bundle install (if you have Gemfile)${NC}"
    echo -e "${BLUE}  4. Follow official guide: https://docs.fastlane.tools${NC}"
    exit 1
}

# Function to check environment setup
check_environment() {
    echo -e "${BLUE}🔍 Checking environment...${NC}"
    
    # Check for API key in various locations
    local api_key=""
    
    # Check environment variable
    if [ -n "$NYTIMES_API_KEY" ]; then
        api_key="$NYTIMES_API_KEY"
        echo -e "${GREEN}✅ API key found in environment variable${NC}"
    fi
    
    # Check local.properties
    if [ -z "$api_key" ] && [ -f "local.properties" ]; then
        local prop_key=$(grep "NYTIMES_API_KEY" local.properties | cut -d'=' -f2)
        if [ -n "$prop_key" ]; then
            api_key="$prop_key"
            echo -e "${GREEN}✅ API key found in local.properties${NC}"
        fi
    fi
    
    # Check BuildConfig if available
    if [ -z "$api_key" ] && [ -f "app/build/generated/source/buildConfig/debug/io/mohammedalaamorsi/nyt/BuildConfig.java" ]; then
        echo -e "${BLUE}� Checking BuildConfig for API key...${NC}"
        local build_key=$(grep "NYTIMES_API_KEY" app/build/generated/source/buildConfig/debug/io/mohammedalaamorsi/nyt/BuildConfig.java | sed 's/.*"\(.*\)".*/\1/')
        if [ -n "$build_key" ] && [ "$build_key" != "null" ]; then
            api_key="$build_key"
            echo -e "${GREEN}✅ API key found in BuildConfig${NC}"
        fi
    fi
    
    # If no API key found, prompt or create template
    if [ -z "$api_key" ]; then
        echo -e "${YELLOW}⚠️  NY Times API key not found${NC}"
        
        if [ ! -f ".env" ] && [ -f ".env.template" ]; then
            echo -e "${YELLOW}📋 Creating .env from template...${NC}"
            cp .env.template .env
            echo -e "${YELLOW}📝 Please edit .env file with your actual values${NC}"
        fi
        
        echo -e "${YELLOW}Please set your API key using one of these methods:${NC}"
        echo -e "${BLUE}  1. Environment variable: export NYTIMES_API_KEY=your_key${NC}"
        echo -e "${BLUE}  2. Add to local.properties: NYTIMES_API_KEY=your_key${NC}"
        echo -e "${BLUE}  3. Edit .env file and source it${NC}"
        echo ""
        echo -e "${YELLOW}Fastlane will prompt for the API key when needed.${NC}"
    fi
}

# Function to show available lanes
show_lanes() {
    echo -e "${BLUE}📋 Available Fastlane lanes:${NC}"
    echo ""
    echo -e "${GREEN}Setup & Environment:${NC}"
    echo "  setup_environment - Setup development environment and install dependencies"
    echo ""
    echo -e "${GREEN}Build Commands:${NC}"
    echo "  build_debug     - Build debug APK (runs complete test suite first)"
    echo "  build_release   - Build release APK (runs complete test suite first)" 
    echo "  build_aab       - Build release AAB (runs complete test suite first)"
    echo ""
    echo -e "${GREEN}Test Commands:${NC}"
    echo "  lint           - Run Ktlint checks (with auto-fix)"
    echo "  unit_tests     - Run unit tests"
    echo "  ui_tests       - Run UI tests (requires device/emulator)"
    echo "  test_suite     - Run complete test suite (all tests)"
    echo ""
    echo -e "${GREEN}Quality Commands:${NC}"
    echo "  quality_check    - Run lint + unit tests"
    echo "  full_test        - Run all tests (lint + unit + UI)"
    echo "  sonar_analysis   - Run SonarQube analysis"
    echo ""
    echo -e "${GREEN}Pipeline Commands:${NC}"
    echo "  ci_pipeline      - Complete CI pipeline (setup + tests + build debug)"
    echo "  release_pipeline - Complete release pipeline (setup + tests + build release)"
    echo ""
    echo -e "${GREEN}Utility Commands:${NC}"
    echo "  clean          - Clean project"
    echo ""
    echo -e "${YELLOW}💡 Tips:${NC}"
    echo "  • All build commands automatically run the complete test suite first!"
    echo "  • Fastlane checks for its own installation and installs dependencies"
    echo "  • API key is automatically detected from BuildConfig, environment, or local.properties"
    echo "  • Use ./fastlane.sh setup_environment to verify your setup"
    echo ""
}

# Function to run a specific lane
run_lane() {
    local lane=$1
    echo -e "${BLUE}🏃 Running lane: ${lane}${NC}"
    
    # Ensure environment is set up
    setup_ruby_environment
    
    # Use bundle exec if Gemfile exists, otherwise use direct fastlane
    if [ -f "Gemfile" ] && command -v bundle &> /dev/null; then
        echo -e "${BLUE}📋 Using bundle exec...${NC}"
        bundle exec fastlane android $lane
    else
        echo -e "${BLUE}💎 Using direct fastlane...${NC}"
        fastlane android $lane
    fi
}

# Function to setup Ruby environment (updated to use dynamic detection)
setup_ruby_environment() {
    echo -e "${BLUE}🔧 Setting up Ruby environment...${NC}"
    
    # First try to setup gem path dynamically
    setup_gem_path
    
    # Also add common fallback paths for different platforms
    local platform=$(uname -s)
    local ruby_version=$(ruby -e 'puts RUBY_VERSION' 2>/dev/null || echo "3.0.0")
    
    local additional_paths=()
    
    case "$platform" in
        "Linux")
            additional_paths=(
                "$HOME/.local/bin"
                "/usr/local/bin"
                "$HOME/.local/share/gem/ruby/$ruby_version/bin"
                "$HOME/.gem/ruby/$ruby_version/bin"
            )
            ;;
        "Darwin")
            additional_paths=(
                "/usr/local/bin"
                "/opt/homebrew/bin"
                "$HOME/.gem/ruby/$ruby_version/bin"
                "/usr/local/lib/ruby/gems/$ruby_version/bin"
                "/opt/homebrew/lib/ruby/gems/$ruby_version/bin"
            )
            ;;
        "MINGW"* | "CYGWIN"* | "MSYS"*)
            additional_paths=(
                "$HOME/.gem/ruby/$ruby_version/bin"
                "/c/Ruby*/bin"
            )
            ;;
    esac
    
    # Add additional paths if they exist
    for gem_path in "${additional_paths[@]}"; do
        if [ -d "$gem_path" ] && [[ ":$PATH:" != *":$gem_path:"* ]]; then
            export PATH="$gem_path:$PATH"
        fi
    done
}

# Function to setup Ruby environment
setup_ruby_environment() {
    # Add common gem bin directories to PATH
    local gem_paths=(
        "$HOME/.local/share/gem/ruby/3.0.0/bin"
        "$HOME/.local/share/gem/ruby/3.1.0/bin"
        "$HOME/.local/share/gem/ruby/3.2.0/bin"
        "$HOME/.gem/ruby/2.7.0/bin"
        "/usr/local/bin"
    )
    
    for gem_path in "${gem_paths[@]}"; do
        if [ -d "$gem_path" ] && [[ ":$PATH:" != *":$gem_path:"* ]]; then
            export PATH="$gem_path:$PATH"
        fi
    done
}

# Function to setup gem binary path dynamically
setup_gem_path() {
    echo -e "${BLUE}🔧 Setting up gem binary path...${NC}"
    
    # Method 1: Get from gem environment
    local gem_bin_dir=""
    if command -v gem &> /dev/null; then
        gem_bin_dir=$(gem environment gemdir 2>/dev/null)
        if [ -n "$gem_bin_dir" ]; then
            gem_bin_dir="$gem_bin_dir/bin"
        fi
        
        # Fallback: try to get executable directory directly  
        if [ -z "$gem_bin_dir" ] || [ ! -d "$gem_bin_dir" ]; then
            gem_bin_dir=$(gem environment | grep -i 'EXECUTABLE DIRECTORY' | cut -d: -f2- | sed 's/^[[:space:]]*//')
        fi
    fi
    
    # Method 2: Common paths based on platform detection
    if [ -z "$gem_bin_dir" ] || [ ! -d "$gem_bin_dir" ]; then
        local platform=$(uname -s)
        local ruby_version=$(ruby -e 'puts RUBY_VERSION' 2>/dev/null || echo "3.0.0")
        
        case "$platform" in
            "Linux")
                # Common Linux paths
                local linux_paths=(
                    "$HOME/.local/share/gem/ruby/$ruby_version/bin"
                    "$HOME/.gem/ruby/$ruby_version/bin"
                    "/usr/local/share/gem/ruby/$ruby_version/bin"
                    "$HOME/.local/bin"
                )
                
                for path in "${linux_paths[@]}"; do
                    if [ -d "$path" ]; then
                        gem_bin_dir="$path"
                        break
                    fi
                done
                ;;
                
            "Darwin")
                # macOS paths
                local macos_paths=(
                    "$HOME/.gem/ruby/$ruby_version/bin"
                    "/usr/local/lib/ruby/gems/$ruby_version/bin"
                    "/opt/homebrew/lib/ruby/gems/$ruby_version/bin"
                    "$HOME/.local/bin"
                )
                
                for path in "${macos_paths[@]}"; do
                    if [ -d "$path" ]; then
                        gem_bin_dir="$path"
                        break
                    fi
                done
                ;;
                
            "MINGW"* | "CYGWIN"* | "MSYS"*)
                # Windows paths (Git Bash, MSYS2, etc.)
                local windows_paths=(
                    "$HOME/.gem/ruby/$ruby_version/bin"
                    "/c/Ruby*/bin"
                    "$USERPROFILE/.gem/ruby/$ruby_version/bin"
                )
                
                for path in "${windows_paths[@]}"; do
                    if [ -d "$path" ]; then
                        gem_bin_dir="$path"
                        break
                    fi
                done
                ;;
        esac
    fi
    
    # Add to PATH if found and not already there
    if [ -n "$gem_bin_dir" ] && [ -d "$gem_bin_dir" ]; then
        if [[ ":$PATH:" != *":$gem_bin_dir:"* ]]; then
            export PATH="$gem_bin_dir:$PATH"
            
            # Determine shell config file
            local shell_config=""
            if [ -n "$BASH_VERSION" ]; then
                shell_config="$HOME/.bashrc"
            elif [ -n "$ZSH_VERSION" ]; then
                shell_config="$HOME/.zshrc"
            else
                shell_config="$HOME/.profile"
            fi
            
            # Add to shell config if not already there
            if [ -f "$shell_config" ] && ! grep -q "$gem_bin_dir" "$shell_config" 2>/dev/null; then
                echo "export PATH=\"$gem_bin_dir:\$PATH\"" >> "$shell_config"
                echo -e "${GREEN}✅ Added gem bin directory to PATH: $gem_bin_dir${NC}"
                echo -e "${BLUE}   Added to: $shell_config${NC}"
            else
                echo -e "${GREEN}✅ Gem bin directory added to current session: $gem_bin_dir${NC}"
            fi
        else
            echo -e "${GREEN}✅ Gem bin directory already in PATH: $gem_bin_dir${NC}"
        fi
    else
        echo -e "${YELLOW}⚠️  Could not determine gem binary path automatically${NC}"
        echo -e "${BLUE}   You may need to add the gem bin directory to your PATH manually${NC}"
        echo -e "${BLUE}   Run: gem environment gemdir to find your gem directory${NC}"
    fi
}

# Main script logic
check_fastlane
check_environment

if [ $# -eq 0 ]; then
    show_lanes
    echo ""
    echo -e "${YELLOW}Usage: $0 <lane_name>${NC}"
    echo "Example: $0 quality_check"
    exit 0
fi

case $1 in
    "help"|"-h"|"--help")
        show_lanes
        ;;
    "setup")
        echo -e "${GREEN}✅ Environment setup completed${NC}"
        ;;
    *)
        run_lane $1
        ;;
esac

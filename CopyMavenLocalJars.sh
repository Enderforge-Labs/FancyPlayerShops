#!/bin/bash
# Script to copy framework libraries from Maven local to server mods directory




# Read versions from gradle.properties
FRAMEWORKLIB_VERSION=$(grep "frameworklib_version" gradle.properties | cut -d'=' -f2 | tr -d ' ')
FRAMEWORKCONFIG_VERSION=$(grep "frameworkconfig_version" gradle.properties | cut -d'=' -f2 | tr -d ' ')

if [ -z "$FRAMEWORKLIB_VERSION" ]; then
    echo "Error: Could not find frameworklib_version in gradle.properties"
    exit 1
fi

if [ -z "$FRAMEWORKCONFIG_VERSION" ]; then
    echo "Error: Could not find frameworkconfig_version in gradle.properties"
    exit 1
fi




# Destination directory
DEST_DIR="./run/mods"
mkdir -p "$DEST_DIR"




# Function to copy a library
copy_lib() {
    local LIB_NAME=$1
    local VERSION=$2
    local SOURCE_JAR="$HOME/.m2/repository/com/snek/$LIB_NAME/$VERSION/$LIB_NAME-$VERSION.jar"

    if [ ! -f "$SOURCE_JAR" ]; then
        echo "✗ Error: $LIB_NAME JAR not found at $SOURCE_JAR"
        echo "  Run './gradlew publishToMavenLocal' in the $LIB_NAME project first"
        return 1
    fi

    # Remove old versions
    rm -f "$DEST_DIR"/$LIB_NAME-*.jar

    # Copy the JAR
    cp "$SOURCE_JAR" "$DEST_DIR/"
    echo "✓ Copied $SOURCE_JAR to $DEST_DIR/"
    return 0
}




# Copy both libraries
copy_lib "frameworklib" "$FRAMEWORKLIB_VERSION"
copy_lib "frameworkconfig" "$FRAMEWORKCONFIG_VERSION"
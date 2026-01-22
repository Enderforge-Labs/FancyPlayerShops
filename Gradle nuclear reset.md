
# Gradle nuke

Gradle always has a hard time updating dependencies.
This deletes all caches, forcing it to recompute them.

1. <pre>
    pkill -kill gradle
    pkill -kill java
    rm -rf ~/.gradle/caches/
    rm -rf ~/.config/VSCodium/User/workspaceStorage/
    rm -rf ~/.config/VSCodium/User/globalStorage/
    </pre>

2. Fully close and reopen all VSCode windows

3. clear; rm -rf ./.gradle ./bin ./build; ./gradlew clean build --refresh-dependencies
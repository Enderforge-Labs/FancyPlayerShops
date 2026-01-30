
# Gradle nuke

Gradle always has a hard time updating dependencies.
This deletes all caches, forcing it to recreate them.

1. <pre>
    pkill -kill gradle
    pkill -kill java
    rm -rf ./.gradle ./bin ./build
    rm -rf ~/.gradle/caches/
    rm -rf ~/.config/VSCodium/User/workspaceStorage/</pre>

2. Fully close and reopen all VSCode windows

3. <pre>
    clear
    ./gradlew clean build genSources --refresh-dependencies</pre>

4. Reload VSCode windows

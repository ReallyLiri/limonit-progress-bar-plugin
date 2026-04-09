# limonit-progress-bar-plugin

![https://img.shields.io/jetbrains/plugin/v/19654-limonit-progressbar](https://img.shields.io/jetbrains/plugin/v/19654-limonit-progressbar)

Install from plugin store: https://plugins.jetbrains.com/plugin/19654-limonit-progressbar

Determinate and indeterminate progress bars:

![gif](https://i.imgur.com/Dm20hS1.gif)

Tray icon:

![tray](https://i.imgur.com/Y7hWQPk.png)

### Build and Publish

Project toolchain

The project requests JDK 25. Gradle can automatically download a matching JDK via the Foojay resolver plugin included in
this branch. If you prefer to install a JDK yourself, on macOS you can:

Build and publish

```shell
./gradlew clean build
# run tests
./gradlew test
# run the IntelliJ Plugin Verifier (requires network)
./gradlew runPluginVerifier

export ORG_GRADLE_PROJECT_intellijPublishToken="..."
./gradlew publishPlugin
```

# limonit-progress-bar-plugin

![https://img.shields.io/jetbrains/plugin/v/19654-limonit-progressbar](https://img.shields.io/jetbrains/plugin/v/19654-limonit-progressbar)

Install from plugin store: https://plugins.jetbrains.com/plugin/19654-limonit-progressbar

Determinate and indeterminate progress bars:

![gif](https://i.imgur.com/Dm20hS1.gif)

Tray icon:

![tray](https://i.imgur.com/Y7hWQPk.png)

### Build and Publish

Make sure you have jdk-17 properly configured.

```shell
./gradlew build
export ORG_GRADLE_PROJECT_intellijPublishToken="..."
./gradlew publishPlugin
```

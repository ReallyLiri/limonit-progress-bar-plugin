import org.jetbrains.intellij.platform.gradle.TestFrameworkType

val jdkVersion = 25

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.intellijPlatform)
}

group = properties("pluginGroup")
version = properties("pluginVersion")

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
        jetbrainsRuntime()
    }
}

dependencies {
    intellijPlatform {
        intellijIdea(properties("platformVersion"))

        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }
}

kotlin {
    jvmToolchain(jdkVersion)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jdkVersion))
    }
}

intellijPlatform {
    pluginConfiguration {
        name = properties("pluginName")
        version = properties("pluginVersion")

        ideaVersion {
            sinceBuild = properties("pluginSinceBuild")
            untilBuild = properties("pluginUntilBuild")
        }
    }

    publishing {
        token = System.getenv("ORG_GRADLE_PROJECT_intellijPublishToken")
        channels = listOf("stable")
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }
}

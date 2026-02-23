plugins {
    java
}

group = "com.toast"
version = "0.0.1-SNAPSHOT"
description = "ddd-example"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

plugins {
    kotlin("jvm") version "1.1.51"
    id("org.junit.platform.gradle.plugin")
}

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    compile(kotlin("stdlib-jre8"))
    testCompile("org.jetbrains.spek:spek-api:1.1.5")
    testRuntime("org.jetbrains.spek:spek-junit-platform-engine:1.1.5")
}

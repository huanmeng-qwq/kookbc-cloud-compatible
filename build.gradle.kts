plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "me.huanmeng"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://repo.panda-lang.org/releases/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("com.github.SNWCreations:KookBC:0.31.0")

    // cloud dep
    implementation("cloud.commandframework:cloud-core:1.8.3")
    implementation("cloud.commandframework:cloud-annotations:1.8.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava {
    options.encoding = "UTF-8"
}
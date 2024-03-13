plugins {
    kotlin("jvm") version "1.8.0"
    id("maven-publish")
    application
}

group = "com.toofan.soft.qsb.api"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("com.google.code.gson:gson:2.8.9")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["kotlin"])
                groupId = "com.github.Toofan-Soft"
                artifactId = "qsb-api"
                version = "1.0-SNAPSHOT"
            }
        }
    }
}
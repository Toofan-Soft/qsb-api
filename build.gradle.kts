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

//    implementation("io.socket:socket.io-client:1.0.0")

    // https://mvnrepository.com/artifact/com.pusher/pusher-java-client
    implementation("com.pusher:pusher-java-client:2.4.4")


//    implementation("com.itextpdf:itextpdf:5.5.13.3")
//    implementation("com.itextpdf:itext7-core:8.0.3")
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
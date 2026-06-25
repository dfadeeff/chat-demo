plugins {
    kotlin("jvm") version "1.9.22"
    java
    application
}

group = "com.chat"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/kotlin")
        java.srcDirs("src/main/java")
    }
}

application {
    mainClass.set("chat.ui.ChatApplication")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "chat.ui.ChatApplication"
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

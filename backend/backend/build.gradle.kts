plugins {
    java
    id("com.diffplug.spotless") version "6.25.0"
    checkstyle
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
}

tasks.register<JavaExec>("runSanity") {
    group = "application"
    description = "Run Sanity example"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.tglanz.chesslysis.backend.examples.Sanity")
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

checkstyle {
    toolVersion = "10.12.5"
    configFile = file("${project.rootDir}/config/checkstyle/checkstyle.xml")
}
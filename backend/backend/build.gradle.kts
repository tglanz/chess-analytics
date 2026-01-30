plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "6.25.0"
    checkstyle
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

// Specify default main class for bootRun
springBoot {
    mainClass.set("io.tglanz.chesslytics.backend.app.ChesslyticsApiApplication")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Database drivers
    runtimeOnly("org.xerial:sqlite-jdbc:3.47.1.0")
    runtimeOnly("org.hibernate.orm:hibernate-community-dialects:6.6.5.Final")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
    toolVersion = "10.21.1"
    configFile = file("${project.rootDir}/config/checkstyle/checkstyle.xml")
}

configurations.checkstyle {
    resolutionStrategy.capabilitiesResolution.withCapability("com.google.collections:google-collections") {
        select("com.google.guava:guava:0")
    }
}

tasks.register<JavaExec>("api") {
    group = "application"
    description = "Run REST API server"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.tglanz.chesslytics.backend.app.ChesslyticsApiApplication")
}

tasks.register<JavaExec>("ingest") {
    group = "application"
    description = "Run Chess.com data ingestion (Usage: -Pusername=<username> [-Porder=asc|desc] [-Plimit=<number>])"

    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.tglanz.chesslytics.backend.app.ChesslyticsIngestApplication")

    val username = project.findProperty("username")?.toString()
    val order = project.findProperty("order")?.toString()
    val limit = project.findProperty("limit")?.toString()

    if (username != null) {
        args("--username=$username")
    }
    if (order != null) {
        args("--order=$order")
    }
    if (limit != null) {
        args("--limit=$limit")
    }

    // Disable web server for batch job
    systemProperty("spring.main.web-application-type", "none")
}


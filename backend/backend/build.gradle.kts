plugins {
    java
    id("com.diffplug.spotless") version "6.25.0"
    checkstyle
}

java {
    toolchain {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")

    implementation("org.hibernate.orm:hibernate-core:6.6.41.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    implementation("org.glassfish:jakarta.el:4.0.2")

    implementation("org.hibernate.orm:hibernate-agroal:6.6.41.Final")
    implementation("io.agroal:agroal-pool:2.1")

    runtimeOnly("org.apache.logging.log4j:log4j-core:2.25.3")

    runtimeOnly("com.h2database:h2:2.3.232")


    implementation("org.reflections:reflections:0.10.2")
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
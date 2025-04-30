group = "rk.cinema"
version = "1.0-SNAPSHOT"

val javaVersion = JavaVersion.VERSION_21

val jacksonVersion: String by project
val logbackVersion: String by project
val junitVersion: String by project

plugins {
    id("com.adarshr.test-logger")
    id("java")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:${logbackVersion}")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.test {
    useJUnitPlatform()
}

testlogger {
    showStackTraces = false
    slowThreshold = 10000
    showSimpleNames = true
}
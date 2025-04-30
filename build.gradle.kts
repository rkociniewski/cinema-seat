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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.test {
    useJUnitPlatform()
}

tasks.javadoc {
    isFailOnError = false
    options.encoding = "UTF-8"
    source = sourceSets["main"].allJava
    (options as StandardJavadocDocletOptions).apply {
        addBooleanOption("Xdoclint:none", true)
        addStringOption("charset", "UTF-8")
        addStringOption("docencoding", "UTF-8")
    }
}

testlogger {
    showStackTraces = false
    slowThreshold = 10000
    showSimpleNames = true
}
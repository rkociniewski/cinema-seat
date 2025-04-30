rootProject.name = "cinema-seat"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
    }

    val testLoggerId: String by settings
    val testLoggerVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                testLoggerId -> useVersion(testLoggerVersion)
            }
        }
    }
}
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://naver.jfrog.io/artifactory/maven/")
        maven { url = uri("https://jitpack.io") }
    }
}
//dependencyResolutionManagement {
//    repositories {
//        google()
//        mavenCentral()
//        maven(url = "https://jitpack.io")
//    }
//}

rootProject.name = "OFFROADER"
include(":app")
 
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    //google-services.json 값에 접근 하려면 Google 서비스 Gradle 플러그인이 필요함
    id("com.google.gms.google-services") version "4.4.1" apply false
}


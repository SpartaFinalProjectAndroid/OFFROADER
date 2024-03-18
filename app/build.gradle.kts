import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")

    //google-services 플러그인 추가
    id("com.google.gms.google-services")
}

//API 키를 담고있는 로컬 프로퍼티를 불러옴
val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

android {
    namespace = "com.ing.offroader"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ing.offroader"
        minSdk = 26
        versionCode = 3
        versionName = "1.2"
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //API 키를 로컬 프로퍼티에서 가져와 name에 부여
        //사용방법 = BuildConfig.[name]
        buildConfigField(
            "String",
            "OPENAI_API_KEY",
            properties.getProperty("OFFROADER_OPENAI_API_KEY")
        )
        buildConfigField(
            "String",
            "NAVERMAPS_API_KEY",
            properties.getProperty("OFFROADER_NAVERMAPS_API_KEY")
        )

        renderscriptTargetApi = 21
        renderscriptSupportModeEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // volley : HTTP library that makes networking for Android apps easier and most importantly, faster.
    implementation("com.android.volley:volley:1.2.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.3")
    implementation("com.google.firebase:firebase-firestore:24.10.3")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("androidx.activity:activity:1.8.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")

    // viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // viewModels (in activity and fragment)
    implementation("androidx.activity:activity-ktx:1.3.1")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // sharedpref 저장
    implementation("com.google.code.gson:gson:2.10.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    androidTestImplementation("androidx.room:room-testing:2.6.1")

    //navi
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    //pager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0") //잘나옴

    //blur 효과 부여
    implementation("com.github.Dimezis:BlurView:version-2.0.3") //테스트 요

    //Firebase BoM 추가
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))

    //Firebase 분석을 위해 추가
    implementation("com.google.firebase:firebase-analytics")

    //lottie 애니메이션 추가
    implementation("com.airbnb.android:lottie:4.1.0")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.0-beta02")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Coordinatorlayout
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    // 네이버 지도 SDK
    implementation("com.naver.maps:map-sdk:3.17.0")

    // 현재 위치 반환
    implementation ("com.google.android.gms:play-services-location:21.1.0")


    // androidx.media3
    implementation ("androidx.media3:media3-session:1.3.0")
    implementation ("androidx.media3:media3-exoplayer:1.3.0")
    implementation("androidx.media3:media3-exoplayer-dash:1.3.0")
    implementation("androidx.media3:media3-exoplayer-smoothstreaming:1.3.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.3.0")
    implementation("androidx.media3:media3-exoplayer-rtsp:1.3.0")
    implementation("androidx.media3:media3-ui:1.3.0")
    implementation("androidx.media3:media3-ui-leanback:1.3.0")
    implementation("androidx.media3:media3-common:1.3.0")

    //구글 로그인 지원용
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("androidx.credentials:credentials:1.3.0-alpha01")
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha01")

}
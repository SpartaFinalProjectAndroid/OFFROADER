import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = "com.mit.offroader"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mit.offroader"
        minSdk = 26
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"




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
    }



}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
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
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:$room_version")
    androidTestImplementation("androidx.room:room-testing:$room_version")

    //navi
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    //pager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")
//    implementation("de.hdodenhof:circleimageview:3.1.0") //잘나옴

    //blur 효과 부여
    implementation("jp.wasabeef:blurry:4.0.1") //리사이클 캐시 null 에러 = 사용불가
//    implementation("com.github.Dimezis:BlurView:version-2.0.3") //xml 뷰 안뜸
    implementation("com.github.furkankaplan:fk-blur-view-android:1.0.1")  //사용 가능하나 코너 값 줄수 없음
//    implementation ("io.alterac.blurkit:blurkit:1.1.0")//xml 뷰 안뜸
//    implementation ("com.fivehundredpx:blurringview:1.0.0")//xml 뷰 안뜸
//    implementation ("com.github.skydoves:cloudy:0.1.2") //로드 안됨
//    implementation ("jp.wasabeef:picasso-transformations:2.2.1")
//    implementation ("jp.co.cyberagent.android.gpuimage:gpuimage-library:1.4.1")
//    implementation ("implementation 'com.github.jgabrielfreitas:BlurImageView:1.0.1'")

//kts 파일에 maven 추가하는 방법 찾아보기

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
}
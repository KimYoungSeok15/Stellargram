import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    //Dependency injection with Hilt
    kotlin("kapt")
    id("com.google.dagger.hilt.android")

    // For use secret api key
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
//    id ("kotlin-kapt")
}

android {
    namespace = "com.ssafy.stellargram"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ssafy.stellargram"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "MAPS_API_KEY", getApiKey("MAPS_API_KEY"))
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("com.google.android.gms:play-services-wallet:19.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // Dependency injection with Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Dependency implementation for navigator
    implementation("androidx.navigation:navigation-compose:2.7.4")

    // Dependency implementation for kakao
    implementation("com.kakao.sdk:v2-all:2.17.0") // 전체 모듈 설치, 2.11.0 버전부터 지원

    // Dependency implementation for maps
    implementation("com.google.maps.android:maps-compose:4.1.1")

    // Optionally, you can include the Compose utils library for Clustering,
    // Street View metadata checks, etc.
    implementation("com.google.maps.android:maps-compose-utils:4.1.1")

    // Optionally, you can include the widgets library for ScaleBar, etc.
    implementation("com.google.maps.android:maps-compose-widgets:4.1.1")

        // location service from google play
        implementation ("com.google.android.gms:play-services-location:21.0.1")

        // Places Android KTX for request geoSearch
        implementation ("com.google.android.libraries.places:places:3.1.0")

    // Dependency implementation for video
    val media3v = "1.0.0-rc01"

        // For media playback using ExoPlayer
        //noinspection GradleDependency
        implementation("androidx.media3:media3-exoplayer:$media3v")
        // For building media playback UIs
        //noinspection GradleDependency
        implementation("androidx.media3:media3-ui:$media3v")
        // Common functionality used across multiple media libraries
        //noinspection GradleDependency
        implementation("androidx.media3:media3-common:$media3v")

    // Dependency implementation for get permissions
    implementation ("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    // Dependency implementation for room

        val room_version = "2.5.0"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")

    // livedata implementation
    implementation("androidx.compose.runtime:runtime-livedata:1.3.3")

    // Other supported types of state
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-beta01")

    // Dependency implementation for get permissions
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    // Gson for parsing JSON
    implementation("com.google.code.gson:gson:2.10.1")

    // Glide
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    // STOMP 사용을 위한 라이브러리
    implementation("com.github.bishoybasily:stomp:2.0.5")

    // STOMP 파싱을 위한 코틀린용 JSON 파싱 라이브러리
    implementation("com.beust:klaxon:5.5")

    // unixTimestamp 포매팅 라이브러리
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.6")
}

// Dependency injection with Hilt
kapt {
    correctErrorTypes = true
}

fun getApiKey(propertyKey: String): String {
    return gradleLocalProperties(rootDir).getProperty(propertyKey)
}
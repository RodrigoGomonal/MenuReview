plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Aplicar el plugin de KSP
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.menureview"
    compileSdk {
        version = release(36)
    }
    buildFeatures {
        buildConfig = true
        compose = true  // Si usas Jetpack Compose
    }
    defaultConfig {
        applicationId = "com.example.menureview"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // 2. Define la constante para el entorno de Desarrollo (tu IP local/EC2 de prueba)
            buildConfigField("String", "BASE_URL", "\"http://TU.IP.EC2:3000/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    // Configuración vital para evitar conflictos de licencias en los Tests
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.foundation)
    val room_version = "2.8.3" // Reemplaza con la versión más reciente

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.compose.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // Room + KSP
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")

    // Añade la dependencia de Google Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    // Si necesitas funcionalidades de ubicación (GPS)
    implementation("com.google.android.gms:play-services-location:21.0.1")
    // Google Maps para Jetpack Compose
    implementation("com.google.maps.android:maps-compose:4.2.0")

    implementation(libs.coil.compose)

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // === TESTING UNITARIO (Lógica) ===
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // === TESTING UI (Instrumentado) ===
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    // Solución para mockear en Android (UI)
    androidTestImplementation("io.mockk:mockk-android:1.13.9")
}

// Habilitar JUnit 5 para Kotest
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
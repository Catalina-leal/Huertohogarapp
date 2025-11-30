plugins {

    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.huertohogar"
    compileSdk = 36


    defaultConfig {
        applicationId = "com.huertohogar"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Configuración de firma (debe ir antes de buildTypes)
    signingConfigs {
        create("release") {
            // Las credenciales se cargan desde local.properties o variables de entorno
            val keystoreFile = System.getenv("KEYSTORE_FILE") ?: "${project.rootDir}/keystore/release.keystore"
            val keystorePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
            val keyAlias = System.getenv("KEY_ALIAS") ?: "huertohogar"
            val keyPassword = System.getenv("KEY_PASSWORD") ?: ""
            
            // Solo configurar si el keystore existe y hay credenciales
            if (file(keystoreFile).exists() && keystorePassword.isNotEmpty()) {
                storeFile = file(keystoreFile)
                storePassword = keystorePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            }
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Configuración de firma (solo si está configurado)
            val keystoreFile = System.getenv("KEYSTORE_FILE") ?: "${project.rootDir}/keystore/release.keystore"
            val keystorePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
            if (file(keystoreFile).exists() && keystorePassword.isNotEmpty()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    
    // Configuración de test
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
    }
    
    // Packaging options
    android {
        // ... otras configuraciones

        packaging {
            resources {
                // Excluye el archivo que está causando el conflicto
                excludes += "META-INF/LICENSE-notice.md"

                // Otras exclusiones comunes
                excludes += "META-INF/LICENSE.md"
                excludes += "META-INF/LICENSE"
                excludes += "META-INF/NOTICE"
                excludes += "META-INF/*.txt"
                excludes += "META-INF/*.DSA"
                excludes += "META-INF/*.SF"
            }
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Dependencias de AndroidX y Compose BOM
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3)
    implementation(libs.androidx.room.ktx)
    implementation(libs.junit.jupiter)
    ksp("androidx.room:room-compiler:2.8.3")

    // Soporte para Navigation Runtime (Original)
    implementation(libs.androidx.navigation.runtime.ktx)

    // 1. Navigation Compose (Resuelve NavHost, composable, rememberNavController)
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // 2. Material Icons Extended (Resuelve Icons.Filled.Home, etc.)
    implementation("androidx.compose.material:material-icons-extended")

    // 3. ViewModel Compose (Resuelve el hook viewModel())
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation(libs.androidx.room.common.jvm)
    // Persistencia de Preferencias (DataStore)
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Google Maps para Compose
    implementation("com.google.maps.android:maps-compose:4.3.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    
    // Retrofit para API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // WorkManager para notificaciones
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    
    // Notificaciones
    implementation("androidx.core:core-ktx:1.17.0")
    
    // Dependencias de Testing
    
    // JUnit 4 para compatibilidad con Android (JUnit 5 tiene problemas con Android Gradle Plugin)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    
    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-property:5.8.0")
    
    // MockK para mocking
    testImplementation("io.mockk:mockk:1.13.8")
    androidTestImplementation("io.mockk:mockk-android:1.13.8")
    
    // Coroutines Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    
    // Turbine para testing de Flows
    testImplementation("app.cash.turbine:turbine:1.0.0")
    
    // AndroidX Test
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.junit.jupiter)
    
    // Compose UI Test
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    
    // Test rules and runners
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    
    // Room testing
    testImplementation("androidx.room:room-testing:2.8.3")
    testImplementation("io.mockk:mockk:1.13.10")


}

tasks.withType<Test> {
    useJUnitPlatform()
}
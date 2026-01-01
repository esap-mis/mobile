import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.io.ByteArrayOutputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlinx-serialization")
}

fun Project.gitCommitCount(): Int {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-list", "--count", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim().toInt()
}

val majorVersion = 1
val minorVersion = 0
val patchVersion = 3

composeCompiler {
    enableStrongSkippingMode = true
    includeSourceInformation = true
}

android {
    namespace = "javavlsu.kb.esap.esapmobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "javavlsu.kb.esap.esapmobile"
        minSdk = 26
        targetSdk = 34
        versionCode = gitCommitCount()
        versionName = "$majorVersion.$minorVersion.$patchVersion"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore/esapmobile.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            resValue(
                "string",
                "app_version",
                "v${defaultConfig.versionName}-${defaultConfig.versionCode}"
            )
            signingConfig = signingConfigs.getByName("release")
            applicationVariants.all {
                val variant = this
                variant.outputs
                    .map { it as BaseVariantOutputImpl }
                    .forEach { output ->
                        val outputFileName =
                            "${
                                rootProject.name.replace(
                                    " ",
                                    ""
                                )
                            }.apk"
                        output.outputFileName = outputFileName
                    }
            }
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
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // LiveData
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")

    // Paging
    val pagingVersion = "3.3.0"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation("androidx.paging:paging-compose:3.3.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Koin
    val kotlinVersion = "3.5.3"
    implementation("io.insert-koin:koin-core:$kotlinVersion")
    implementation("io.insert-koin:koin-compose:1.1.1")
    implementation("io.insert-koin:koin-androidx-compose:$kotlinVersion")

    // Ktor
    val ktorVersion = "3.3.3"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Slf4j
    implementation("org.slf4j:slf4j-android:1.7.36")
}

task("printVersionName") {
    val versionName = android.defaultConfig.versionName!!
    val fullVersion = "${versionName.replace(".", "")}-${gitCommitCount()}"
    project.extensions.extraProperties["fullVersion"] = fullVersion
    println(versionName)
}
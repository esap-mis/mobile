import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.builder.model.v2.dsl.SigningConfig
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
}

val gitCommitCount = providers.exec {
    commandLine("git", "rev-list", "--count", "HEAD")
}.standardOutput.asText.get().trim().toInt()

val majorVersion = 1
val minorVersion = 0
val patchVersion = 3

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.compose.runtime)

//            testImplementation("junit:junit:4.13.2")
//            androidTestImplementation("androidx.test.ext:junit:1.3.0")
//            androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
//            androidTestImplementation(platform("androidx.compose:compose-bom:2025.12.01"))
//            androidTestImplementation("androidx.compose.ui:ui-test-junit4")
//            debugImplementation("androidx.compose.ui:ui-tooling")
//            debugImplementation("androidx.compose.ui:ui-test-manifest")

            // Firebase
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.messaging)
            implementation(libs.firebase.analytics)
            implementation(libs.accompanist.permissions)

            // Koin
            implementation(libs.koin.androidx.compose)

            // Slf4j
            implementation(libs.slf4j.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.paging.compose.common)

            implementation(libs.navigation.compose)

            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.core)
            implementation(libs.compose.material.icons.extended)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // Kotlin Serialization
            implementation(libs.kotlinx.serialization.json)

            // Multiplatform settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)

            implementation(libs.kotlin.logging)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            implementation(libs.logback.classic)
        }
    }
}

android {
    namespace = "javavlsu.kb.esap.esapmobile"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "javavlsu.kb.esap.esapmobile"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = gitCommitCount
        versionName = "$majorVersion.$minorVersion.$patchVersion"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        vectorDrawables {
//            useSupportLibrary = true
//        }
    }

    signingConfigs {
        maybeCreate("release").apply {
            storeFile = rootProject.file("keystore/esapmobile.jks")
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

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "javavlsu.kb.esap.esapmobile.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "javavlsu.kb.esap.esapmobile"
            packageVersion = "$majorVersion.$minorVersion.$patchVersion"
        }
    }
}

tasks.register("printVersionName") {
    val versionName = android.defaultConfig.versionName!!
    val fullVersion = "${versionName.replace(".", "")}-${gitCommitCount}"
    project.extensions.extraProperties["fullVersion"] = fullVersion
    println(versionName)
}

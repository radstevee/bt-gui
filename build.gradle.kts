import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.reload.ComposeHotRun
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeFeatureFlag

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose.hot-reload") version "1.0.0-alpha03"
}

group = "net.radstevee.btgui"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.jewel:jewel-int-ui-standalone-243:0.27.0")
    implementation("org.jetbrains.jewel:jewel-int-ui-decorated-window-243:0.27.0")
    implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
}

compose.desktop {
    application {
        buildTypes.release.proguard {
            obfuscate.set(false)
            optimize.set(false)
            version.set("7.4.0")
        }
        mainClass = "net.radstevee.bt.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "bt-gui"
            packageVersion = "1.0.0"
        }
    }
}

composeCompiler {
    featureFlags.add(ComposeFeatureFlag.OptimizeNonSkippingGroups)
}

kotlin {
    jvmToolchain(21)
    explicitApi()
}

tasks.register<ComposeHotRun>("runHot") {
    mainClass.set("net.radstevee.bt.MainKt")
}

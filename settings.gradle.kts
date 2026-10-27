import org.jetbrains.intellij.platform.gradle.extensions.intellijPlatform

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.intellij.platform.settings") version "2.19.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        intellijPlatform {
            defaultRepositories()
        }
    }
}

rootProject.name = "PaniniVM"

include("core", "ashtadhyayi", "parser", "ganapatha", "sankhya", "katapayadi", "aryabhatiya", "bhutasamkhya", "actions", "dhatupatha", "compiler", "cli", "idea-plugin", "derivation", "unadipatha", "analysis", "execution", "linganushasanam")


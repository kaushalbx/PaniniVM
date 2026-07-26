pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    }
}

rootProject.name = "PaniniVM"

include("core", "ashtadhyayi", "parser", "ganapatha", "sankhya", "katapayadi", "aryabhatiya", "bhutasamkhya", "actions", "dhatupatha", "compiler", "cli", "idea-plugin")


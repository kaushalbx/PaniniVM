package dev.panini.plugin

import com.intellij.lang.Language

object PvmLanguage : Language("PaniniVM") {
    private fun readResolve(): Any = PvmLanguage
}

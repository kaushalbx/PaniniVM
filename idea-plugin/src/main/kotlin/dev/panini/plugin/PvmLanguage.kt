package dev.panini.plugin

import com.intellij.lang.Language

class PvmLanguage private constructor() : Language("PaniniVM") {
    companion object {
        @JvmStatic
        val INSTANCE = PvmLanguage()
    }
}

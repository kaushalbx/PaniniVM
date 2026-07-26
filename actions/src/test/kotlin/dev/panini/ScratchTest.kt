package dev.panini

import dev.panini.derivation.KrdantaDerivationRequest
import dev.panini.derivation.KrdantaEngine
import dev.panini.shiksha.Samjna
import kotlin.test.Test

class ScratchTest {
    @Test
    fun testAnukrtyaTrace() {
        val engine = KrdantaEngine()
        val res = engine.derive(KrdantaDerivationRequest("कृ", Samjna.KTVA, upasarga = "अनु"))
        println("=== ANUKRTYA TRACE ===")
        res.applications.forEachIndexed { i, app ->
            println("${i + 1}. [${app.sutra}] ${app.before.surface} -> ${app.after.surface} (${app.explanation})")
        }
        println("FINAL SURFACE: '${res.final.surface}'")
        println("FINAL TERMS: ${res.final.terms.map { "${it.id}:${it.surface}:${it.upadesha}" }}")
    }
}

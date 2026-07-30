package dev.panini.execution.sutra

import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextDecoding
import dev.panini.sutra.runtime.SutraSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SanskritGranthaSourceCompilerTest {
    @Test
    fun `multi-clause Sanskrit source becomes a portable segmented grantha`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । " +
            "फल + अम् द्वि + औट् च गण + णिच् + लोट् + सिप् । " +
            "फल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।"

        val compiled = assertIs<SanskritGranthaSourceCompilation.Success>(
            SanskritGranthaSourceCompiler.compile(source, GranthaId("generated")),
        )
        val decoded = assertIs<SutraBlueprintGranthaTextDecoding.Success>(
            SutraBlueprintGranthaTextCodec.decode(compiled.source),
        )

        assertEquals(GranthaId("generated"), compiled.grantha.id)
        assertEquals(3, compiled.grantha.sutras.size)
        assertEquals(compiled.grantha, decoded.grantha)
        compiled.grantha.sutras.forEach {
            assertEquals(
                "generated",
                assertIs<SutraSource.Vakya>(it.source).uktiId,
            )
        }
    }

}

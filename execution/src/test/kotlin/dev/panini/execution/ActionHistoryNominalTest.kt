package dev.panini.execution

import dev.panini.execution.binding.KarakaReferenceResolver
import dev.panini.execution.binding.NamedActionResultReferenceResolver
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ActionHistoryNominalTest {
    @Test fun `recognized krt affix is not sufficient to denote a completed action`() {
        for (affix in listOf("क्त", "क्तवतुँ", "तृच्", "ण्वुल्", "तव्यत्")) {
            for (referent in listOf("फल", "कर्मन्")) {
                val padas = PaniniParser().parse(
                    "युज् + $affix + ङस् $referent + अम् मुद्र् + णिच् + लोट् + सिप् ।")
                    .grammaticalVakyas().single().padas
                val error = assertFailsWith<IllegalArgumentException>("$affix $referent") {
                    if (referent == "फल") NamedActionResultReferenceResolver.resolve(padas)
                    else KarakaReferenceResolver.references(padas)
                }
                assertTrue(error.message.orEmpty().contains("supported action nominal"))
            }
        }
    }

    @Test fun `both history relations retain supported action nominal meanings`() {
        for (affix in listOf("ल्युट्", "घञ्")) {
            fun padas(referent: String) = PaniniParser().parse(
                "युज् + $affix + ङस् $referent + अम् मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas
            assertEquals(1, NamedActionResultReferenceResolver.resolve(padas("फल")).size)
            assertEquals(1, KarakaReferenceResolver.references(padas("कर्मन्")).size)
        }
    }

    @Test fun `participant history cannot erase further derivation either`() {
        val padas = PaniniParser().parse(
            "युज् + ल्युट् + मतुप् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas
        assertFailsWith<IllegalArgumentException> { KarakaReferenceResolver.references(padas) }
    }
}

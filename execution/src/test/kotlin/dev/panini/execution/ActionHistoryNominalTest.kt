package dev.panini.execution

import dev.panini.execution.binding.KarakaReferenceResolver
import dev.panini.execution.binding.NamedActionResultReferenceResolver
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ActionHistoryNominalTest {
    @Test fun `unknown typed action morphology cannot disappear during history resolution`() {
        for (head in listOf("फल", "कर्मन्")) {
            val padas = PaniniParser().parse(
                "युज् + ल्युट् + ङस् $head + अम् मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas
            val modifier = padas.first() as dev.panini.vyakaranam.ast.SubantaPada
            val derivation = modifier.pratipadika as dev.panini.vyakaranam.ast.KridantaPratipadika
            val invalid = listOf(
                derivation.copy(upasargas = listOf("unknown-prefix")),
                derivation.copy(dhatu = derivation.dhatu.copy(sanadiPratyayas = listOf("unknown-affix"))),
                derivation.copy(dhatu = derivation.dhatu.copy(mulaDhatu = "unknown-root")),
            )
            for (noun in invalid) {
                val changed = listOf(modifier.copy(pratipadika = noun)) + padas.drop(1)
                assertFailsWith<IllegalArgumentException> {
                    if (head == "फल") NamedActionResultReferenceResolver.resolve(changed)
                    else KarakaReferenceResolver.references(changed)
                }
            }
        }
    }

    @Test fun `single participant ordering follows morphological agreement not word position`() {
        for (phrase in listOf("युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम्",
            "कर्मन् + अम् प्रथम + अम् युज् + ल्युट् + ङस्",
            "युज् + ल्युट् + ङस् कर्मन् + अम् प्रथम + अम्")) {
            val padas = PaniniParser().parse("$phrase मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas
            val reference = KarakaReferenceResolver.references(padas).single()
            assertEquals(1L, reference.ordinalFromOldest)
            assertTrue(reference.orderingValid)
            assertNotNull(reference.qualifier)
        }
    }

    @Test fun `postposed participant ordering cannot ignore disagreement or competing ordinals`() {
        for (ending in listOf("प्रथम + सुँ", "प्रथम + शस्", "प्रथम + अम् द्वि + तीय + अम्")) {
            val padas = PaniniParser().parse(
                "युज् + ल्युट् + ङस् कर्मन् + अम् $ending मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas
            assertFalse(KarakaReferenceResolver.references(padas).single().orderingValid)
        }
    }

    @Test fun `one action modifier scopes over multiple participant heads in either order`() {
        for (phrase in listOf("युज् + ल्युट् + ङस् कर्मन् + अम् करण + अम्",
            "कर्मन् + अम् करण + अम् युज् + ल्युट् + ङस्")) {
            val padas = PaniniParser().parse("$phrase मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas
            val references = KarakaReferenceResolver.references(padas)
            assertEquals(2, references.size)
            assertTrue(references.all { it.dhatuUpadesha == "युजिँर्" })
        }
    }

    @Test fun `single participant history modifier can follow its head`() {
        for (phrase in listOf("युज् + ल्युट् + ङस् कर्मन् + अम्", "कर्मन् + अम् युज् + ल्युट् + ङस्")) {
            val padas = PaniniParser().parse("$phrase मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas
            val reference = KarakaReferenceResolver.references(padas).single()
            assertEquals("युजिँर्", reference.dhatuUpadesha)
        }
    }

    @Test fun `multiple action modifiers cannot silently attach to a single participant`() {
        val padas = PaniniParser().parse(
            "युज् + ल्युट् + ङस् भज् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas
        val error = assertFailsWith<IllegalArgumentException> { KarakaReferenceResolver.references(padas) }
        assertTrue(error.message.orEmpty().contains("Ambiguous participant-history"))
    }

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

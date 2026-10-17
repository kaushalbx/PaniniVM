package dev.panini.derivation

import dev.panini.shiksha.*
import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.SutraStage
import kotlin.test.*

class KukTukSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `kuk supplies stop and retention alternatives before s and ss`() {
        for ((right, augmented, plain) in listOf(
            Triple("साये", "प्राङ्क्साये", "प्राङ्साये"),
            Triple("षष्ठः", "प्राङ्क्षष्ठः", "प्राङ्षष्ठः"))) {
            val results = engine.joinAll("प्राङ्", right)
            assertEquals(setOf(augmented, plain), results.map { engine.render(it) }.toSet())
            val applied = results.single { it.applications.any { step -> step.sutra == "8.3.28" } }
            assertEquals(Vyanjana.KA, applied.final.terms.first().varnas.last())
            assertEquals(right, applied.final.terms.last().surface)
            assertTrue(applied.applications.any { it.sutra == "1.1.46" })
            assertTrue(applied.final.terms.first().itMarkerProvenance.any { it.designationSutra == "1.3.3" })
        }
    }

    @Test fun `tuk remains preceding final and does not retroflex following s`() {
        val results = engine.joinAll("वण्", "साये")
        assertEquals(setOf("वण्ट्साये", "वण्साये"), results.map { engine.render(it) }.toSet())
        val applied = results.single { it.applications.any { step -> step.sutra == "8.3.28" } }
        assertEquals(Vyanjana.TTA, applied.final.terms.first().varnas.last())
        assertEquals(Vyanjana.SA, applied.final.terms.last().varnas.first())
        assertTrue(applied.applications.any { it.sutra == "8.4.42" })
    }

    @Test fun `final kuk licenses optional following cha while unaugmented nasal does not`() {
        val results = engine.joinAll("प्राङ्", "शेते")
        assertEquals(setOf("प्राङ्क्शेते", "प्राङ्क्छेते", "प्राङ्शेते"), results.map { engine.render(it) }.toSet())
        for (result in results.filter { it.applications.any { step -> step.sutra == "8.4.63" } }) {
            assertTrue(result.applications.any { it.sutra == "8.3.28" })
            assertEquals(Vyanjana.KA, result.final.terms.first().varnas.last())
        }
    }

    @Test fun `new augment scope preserves previously pronounced nasal vowel`() {
        val result = engine.join("प्राँङ्", "साये")
        assertEquals("प्राँङ्क्साये", engine.render(result))
        assertTrue(result.final.terms.first().phonologicalText.effectiveVarnas.any { it.varna == Svara.AA && it.nasalized })
        assertNull(result.final.terms.first().itUpadeshaVarnaIndices)
    }

    @Test fun `other nasals and non sibilant followers are excluded`() {
        for ((left, right) in listOf("सन्" to "साये", "कम्" to "षष्ठः", "प्राङ्" to "गच्छति")) {
            assertTrue(engine.joinAll(left, right).all { result -> result.applications.none { it.sutra == "8.3.28" } })
        }
    }

    @Test fun `scoped kit does not reclassify processed affix initial or nasal vowel`() {
        val itEngine = DerivationEngine(Ashtadhyayi.executableSutras.filter { it.stage == SutraStage.IT_PROCESSING })
        for ((surface, kind) in listOf("कँ" to TermKind.PRATYAYA, "टँ" to TermKind.PRATYAYA,
            "षँ" to TermKind.PRATYAYA, "ञिँभज्" to TermKind.DHATU)) {
            val carrier = DerivationTerm("carrier", surface, kind)
            val augment = DerivationTerm("augment", "कुँक्", TermKind.AGAMA,
                augmentTargetId = carrier.id, itProcessingPhase = ItProcessingPhase.RAW_UPADESHA)
            val result = itEngine.derive(DerivationState(listOf(carrier, augment), stage = DerivationStage.PADA_FORMED))
            val survivor = result.final.terms.single()
            assertEquals(carrier.varnas + Vyanjana.KA, survivor.varnas, surface)
            assertTrue(survivor.phonologicalText.effectiveVarnas.any { it.varna is Svara && it.nasalized }, surface)
            assertNull(survivor.itUpadeshaVarnaIndices)
            assertTrue(result.applications.none { it.sutra in setOf("1.3.5", "1.3.6", "1.3.7", "1.3.8") }, surface)
        }
    }

    @Test fun `scoped tit retains nasal vowel of following processed pada`() {
        val result = engine.join("कुर्वन्", "अँस्ते")
        assertEquals("कुर्वन्नँस्ते", engine.render(result))
        assertNull(result.final.terms.last().itUpadeshaVarnaIndices)
    }
}

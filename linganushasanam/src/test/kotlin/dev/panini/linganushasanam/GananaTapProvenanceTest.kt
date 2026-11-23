package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** गणना (√गण + णिच् + युच् 3.3.107 + टाप् 4.1.4) is feminine only through Liṅgānuśāsana 11. */
class GananaTapProvenanceTest {
    @Test fun `ganana receives feminine gender through rule 11 from its declared tap`() {
        val entry = assertNotNull(PaninianPratipadikaLexicon.findPratipadika("गणना"))
        assertEquals(setOf(Linga.STRI), entry.linga)
        assertEquals(setOf("11"), entry.lingaRuleIds)
    }

    @Test fun `declared aap stems are feminine by their Linganushasana rule and keep their aap formation`() {
        // लता is named by Liṅgānuśāsana 18 (भूमिविद्युत्सरिल्लतावनिताभिधानानि), and सीमा by 31 (तृटिसीमासंबध्याः); both precede the general rule 11.
        for ((stem, affix, rule) in listOf(Triple("प्रक्रिया", "टाप्", "11"), Triple("अवस्था", "टाप्", "11"),
            Triple("लता", "टाप्", "18"), Triple("सीमा", "डाप्", "31"))) {
            val entry = assertNotNull(PaninianPratipadikaLexicon.findPratipadika(stem), stem)
            assertEquals(affix, entry.lingaPratyaya, stem)
            assertEquals(setOf(Linga.STRI), entry.linga, stem)
            assertEquals(setOf(rule), entry.lingaRuleIds, stem)
            assertEquals(dev.panini.derivation.NominalStemFormation.AP, entry.stemFormation, stem)
        }
    }

    @Test fun `an undeclared aa final stem is not made feminine by its spelling`() {
        assertNull(LinganushasanamEngine().resolveOrNull(LingaRuleContext("गणना")))
    }

    @Test fun `a bhava lyut stem is neuter through rule 119 even when listed as common gender`() {
        // स्थान is also enumerated as masculine or neuter; the stated ल्युट् provenance selects 119.
        val result = assertNotNull(LinganushasanamEngine().resolveOrNull(LingaRuleContext("स्थान",
            pratyaya = "ल्युट्", semanticClasses = setOf(LingaSemanticClass.BHAVA), applicableSutraNumbers = setOf("119"))))
        assertEquals(Linga.NAPUMSAKA, result.linga)
        assertEquals("119", result.ruleId)
    }
}

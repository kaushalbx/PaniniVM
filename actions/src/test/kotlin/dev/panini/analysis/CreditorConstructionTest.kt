package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class CreditorConstructionTest {
    @Test fun `derived stem spelling cannot substitute for canonical root and causative construction`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.CAUSATIVE)) {
            val context = KarakaRuleContext(DhatuIdentity("धारय"), creditor, listOf(creditor), voice)
            assertFalse(dev.panini.ashtadhyayi.adhyaya1.pada4.DharerUttamarnahSutra.matches(context))
        }
    }

    private val creditor = ParticipantFacts("creditor", AvyayaPada("creditor", "creditor"),
        setOf(Vibhakti.CHATURTHI), semanticRelations = setOf(SemanticRelation.CREDITOR, SemanticRelation.RECIPIENT))

    @Test fun `creditor designation requires causative dhr in both voices`() {
        for (passive in listOf(false, true)) for (causative in listOf(false, true)) {
            val verb = TingantaPada("verb", emptyList(), DhatuPrakriti("धृ", "धृ",
                if (causative) listOf("णिच्") else emptyList()), Lakara.LAT,
                TingPratyaya(if (passive) "त" else "तिप्", if (passive) "त" else "तिप्"),
                vikarana = if (passive) Vikarana.YAK else null)
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("धृ"), creditor,
                listOf(creditor), if (passive) Prayoga.KARMANI else Prayoga.KARTARI, verbNode = verb))
            if (causative) {
                assertEquals(listOf("1.4.35", "2.3.13"), result.evidence.map { it.sutra })
                assertEquals(Karaka.SAMPRADANA, result.resolved)
            } else assertTrue(result.evidence.none { it.sutra == "1.4.35" }, result.toString())
        }
    }

    @Test fun `nominal action owns creditor causation independently of enclosing verb`() {
        val outer = TingantaPada("outer", emptyList(), DhatuPrakriti("धृ", "धृ", listOf("णिच्")),
            Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
        for (causative in listOf(false, true)) {
            val nominal = KridantaPratipadika("nominal", emptyList(), DhatuPrakriti("धृ", "धृ",
                if (causative) listOf("णिच्") else emptyList()), "ल्युट्")
            val context = KarakaRuleContext(DhatuIdentity("धृ"), creditor, listOf(creditor), Prayoga.KARTARI,
                verbNode = outer, governingKrt = nominal)
            assertEquals(causative, dev.panini.ashtadhyayi.adhyaya1.pada4.DharerUttamarnahSutra.matches(context))
        }
        val lookalike = KarakaRuleContext(DhatuIdentity("धारयकल्पित"), creditor, listOf(creditor), Prayoga.KARTARI)
        assertFalse(dev.panini.ashtadhyayi.adhyaya1.pada4.DharerUttamarnahSutra.matches(lookalike))
    }
}

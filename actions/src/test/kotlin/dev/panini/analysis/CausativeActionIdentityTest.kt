package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class CausativeActionIdentityTest {
    private val causee = ParticipantFacts("causee", AvyayaPada("causee", "causee"), setOf(Vibhakti.DVITIYA),
        semanticRelations = setOf(SemanticRelation.CAUSEE_AGENT))
    private fun context(nominalRoot: String, outerRoot: String): KarakaRuleContext {
        val outer = TingantaPada("outer", emptyList(), DhatuPrakriti(outerRoot, outerRoot, listOf("णिच्")),
            Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
        val nominal = KridantaPratipadika("nominal", emptyList(),
            DhatuPrakriti(nominalRoot, nominalRoot, listOf("णिच्")), "ल्युट्")
        return KarakaRuleContext(DhatuIdentity(outerRoot), causee, listOf(causee), Prayoga.KARTARI,
            candidates = setOf(Karaka.KARMAN), verbNode = outer, governingKrt = nominal)
    }

    @Test fun `optional hr kr designation follows the governing nominal root`() {
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.HrKrorAnyatarasyamSutra
        for (root in listOf("हृ", "कृ")) {
            assertTrue(rule.matches(context(root, "गम्")))
            assertFalse(rule.matches(context("गम्", root)))
        }
        for (stem in listOf("हार", "कार", "हर", "कर")) {
            assertFalse(rule.matches(KarakaRuleContext(DhatuIdentity(stem), causee, listOf(causee),
                Prayoga.CAUSATIVE, candidates = setOf(Karaka.KARMAN))), stem)
        }
    }

    @Test fun `general causee rule applies nominal root class and exclusions without borrowing finite pada`() {
        val rule = dev.panini.ashtadhyayi.adhyaya1.pada4.GatiBuddhiAniKartaSaNauSutra
        assertTrue(rule.matches(context("गम्", "कृ")))
        assertFalse(rule.matches(context("नी", "गम्")))
        assertFalse(rule.matches(context("दृश्", "गम्")), "A nominal action cannot borrow the enclosing parasmaipada ending.")
        assertFalse(KarakaDhatuDomains.isAtmanepadaOptionalCauseeExtension(context("दृश्", "दृश्")))
    }

    @Test fun `enclosing causative cannot supply missing nominal causative morphology`() {
        for ((root, number) in listOf("कृ" to "1.4.53", "गम्" to "1.4.52")) {
            val original = context(root, "कृ")
            val nominal = requireNotNull(original.governingKrt)
            val noncausative = original.copy(governingKrt = nominal.copy(
                dhatu = nominal.dhatu.copy(sanadiPratyayas = emptyList())))
            assertFalse(KarakaRuleEngine.karakaRules.single { it.number == number }.matches(noncausative))
        }
    }
}

package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class AngerActionIdentityTest {
    private val target = ParticipantFacts("target", AvyayaPada("target", "target"),
        setOf(Vibhakti.DVITIYA, Vibhakti.CHATURTHI),
        semanticRelations = setOf(SemanticRelation.RECIPIENT, SemanticRelation.ANGER_TARGET))

    @Test fun `nominal anger action owns prefix precedence rather than enclosing finite verb`() {
        val recipient = KarakaRuleEngine.karakaRules.single { it.number == "1.4.37" }
        val objectRule = KarakaRuleEngine.karakaRules.single { it.number == "1.4.38" }
        for (root in listOf("क्रुध्", "द्रुह्")) for (prefix in listOf<String?>(null, "अभि", "unknown")) {
            val prefixes = listOfNotNull(prefix)
            val outer = TingantaPada("outer", if (prefix == null) listOf("अभि") else emptyList(),
                DhatuPrakriti(root, root), Lakara.LAT, TingPratyaya("तिप्", "तिप्"))
            val nominal = KridantaPratipadika("nominal", prefixes, DhatuPrakriti(root, root), "ल्युट्")
            val context = KarakaRuleContext(DhatuIdentity(root), target, listOf(target), Prayoga.KARTARI,
                verbNode = outer, governingKrt = nominal)
            assertEquals(prefix == null, recipient.matches(context), "$root $prefix")
            assertEquals(prefix == "अभि", objectRule.matches(context), "$root $prefix")
            val wrongNominal = context.copy(governingKrt = nominal.copy(dhatu = DhatuPrakriti("पठ्", "पठ्")))
            assertFalse(recipient.matches(wrongNominal))
            assertFalse(objectRule.matches(wrongNominal))
        }
    }

    @Test fun `anger roots cannot be established by embedded substrings or stem prefixes`() {
        for (root in listOf("अन्यक्रुध्", "क्रुध्यकल्पित", "द्रुह्यकल्पित", "ईर्ष्यकल्पित", "असूयकल्पित")) {
            for (prefixes in listOf(emptyList(), listOf("अभि"))) {
                val verb = TingantaPada("verb", prefixes, DhatuPrakriti(root, root), Lakara.LAT,
                    TingPratyaya("तिप्", "तिप्"))
                val context = KarakaRuleContext(DhatuIdentity(root), target, listOf(target), Prayoga.KARTARI,
                    verbNode = verb)
                assertTrue(KarakaRuleEngine.resolve(context).evidence.none { it.sutra in setOf("1.4.37", "1.4.38") }, root)
                assertTrue(KarakaRuleEngine.resolve(context.copy(verbNode = null,
                    dhatu = DhatuIdentity(prefixes.joinToString("") + root))).evidence
                    .none { it.sutra in setOf("1.4.37", "1.4.38") }, root)
            }
        }
        for ((root, rule) in listOf("क्रुध्" to "1.4.37", "अभिक्रुध्" to "1.4.38")) {
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity(root), target,
                listOf(target), Prayoga.KARTARI))
            assertEquals(listOf(rule, if (rule == "1.4.38") "2.3.2" else "2.3.13"), result.evidence.map { it.sutra })
        }
    }
}

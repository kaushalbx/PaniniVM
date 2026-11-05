package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class PassiveDvikarmakaExpressionTest {
    private fun context(root: String, primaryCase: Vibhakti, secondaryCase: Vibhakti, secondarySelected: Boolean): KarakaRuleContext {
        val primary = ParticipantFacts("primary", AvyayaPada("primary", "primary"), setOf(primaryCase),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT))
        val secondary = ParticipantFacts("secondary", AvyayaPada("secondary", "secondary"), setOf(secondaryCase),
            semanticRelations = setOf(SemanticRelation.SECONDARY_OBJECT))
        val verb = TingantaPada("passive", emptyList(), DhatuPrakriti(root, root), Lakara.LAT,
            TingPratyaya("त", "त"), vikarana = Vikarana.YAK)
        return KarakaRuleContext(DhatuIdentity(root), if (secondarySelected) secondary else primary,
            listOf(primary, secondary), Prayoga.KARMANI, verbNode = verb)
    }

    @Test fun `duhadis express the secondary object while guiding roots express the primary`() {
        for (root in listOf("दुह्", "याच्", "ब्रू", "शास्", "भाष्", "वच्", "नी", "हृ", "कृष्", "वह्")) {
            val secondaryPromoted = root !in setOf("नी", "हृ", "कृष्", "वह्")
            for (secondary in listOf(false, true)) for (valid in listOf(false, true)) {
                val primaryCase = if (secondaryPromoted == valid) Vibhakti.DVITIYA else Vibhakti.PRATHAMA
                val secondaryCase = if (secondaryPromoted == valid) Vibhakti.PRATHAMA else Vibhakti.DVITIYA
                val result = KarakaRuleEngine.resolve(context(root, primaryCase, secondaryCase, secondary))
                val designation = if (secondary) "1.4.51" else "1.4.49"
                assertEquals(Karaka.KARMAN, result.resolved, "$root $secondary $valid")
                val expressed = secondary == secondaryPromoted
                assertEquals(listOf(designation) + if (valid) listOf(if (expressed) "2.3.46" else "2.3.2") else emptyList(),
                    result.evidence.map { it.sutra }, "$root $secondary $valid")
                assertEquals(if (valid) { if (expressed) Vibhakti.PRATHAMA else Vibhakti.DVITIYA } else null,
                    result.resolvedVibhakti, "$root $secondary $valid")
            }
        }
    }

    @Test fun `alternative object readings retain their own expression dependent cases`() {
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA),
            semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT, SemanticRelation.SECONDARY_OBJECT,
                SemanticRelation.DVIKARMAKA_ACTION_SENSE))
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("दुह्"), p, listOf(p), Prayoga.KARMANI))
        assertEquals(Karaka.KARMAN, result.resolved)
        assertNull(result.resolvedVibhakti)
        assertEquals(listOf("1.4.49", "2.3.2"), result.evidence.map { it.sutra })
        val branch = result.alternatives.single()
        assertEquals(Vibhakti.PRATHAMA, branch.vibhakti)
        assertEquals(listOf("1.4.51", "2.3.46"), branch.evidence.map { it.sutra })
        assertTrue(branch.matchesObservedMorphology)
    }

    @Test fun `unclassified meaning equivalent does not invent a passive promotion pattern`() {
        for (case in listOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(case), semanticRelations = setOf(
                SemanticRelation.SECONDARY_OBJECT, SemanticRelation.DVIKARMAKA_ACTION_SENSE))
            val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कथ्"), p, listOf(p), Prayoga.KARMANI))
            assertEquals(listOf("1.4.51"), result.evidence.map { it.sutra })
            assertNull(result.resolvedVibhakti)
        }
    }

    @Test fun `a nominal secondary object cannot select expression for the enclosing finite action`() {
        val original = context("दुह्", Vibhakti.PRATHAMA, Vibhakti.PRATHAMA, false)
        val governor = SubantaPada("nominal", KridantaPratipadika("nominal", emptyList(),
            DhatuPrakriti("ब्रू", "ब्रू"), "ल्युट्"), SupPratyaya("अम्", "अम्"))
        val other = original.allParticipants[1].copy(governingNominal = governor)
        val result = KarakaRuleEngine.resolve(original.copy(allParticipants = listOf(original.participant, other)))
        assertEquals(listOf("1.4.49", "2.3.46"), result.evidence.map { it.sutra })
    }
}

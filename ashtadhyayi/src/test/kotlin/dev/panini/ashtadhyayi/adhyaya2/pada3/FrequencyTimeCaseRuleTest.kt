package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class FrequencyTimeCaseRuleTest {
    @Test fun `frequency time genitive observes underlying role and anabhihita without Vedic licensing`() {
        val p = ParticipantFacts("day", AvyayaPada("अह्नः", "अह्नः"), setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(SemanticRelation.FREQUENCY_TIME_LOCUS, SemanticRelation.TIME_REFERENCE,
                SemanticRelation.LOCATION, SemanticRelation.SESHA_RELATION), frequencyGovernor = AvyayaPada("द्विः", "द्विः"))
        val context = VibhaktiRuleContext(Karaka.ADHIKARANA, p.possibleVibhaktis,
            participant = p, register = GrammarRegister.CLASSICAL)
        assertTrue(KrtvorthaPrayogeKaleAdhikaraneSutra.matches(context))
        assertFalse(KrtvorthaPrayogeKaleAdhikaraneSutra.matches(context.copy(abhihita = true)))
        assertFalse(KrtvorthaPrayogeKaleAdhikaraneSutra.matches(context.copy(karaka = Karaka.KARANA)))
    }
}

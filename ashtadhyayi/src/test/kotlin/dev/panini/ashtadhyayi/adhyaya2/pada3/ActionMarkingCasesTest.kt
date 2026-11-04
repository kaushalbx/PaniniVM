package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.*
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.*

class ActionMarkingCasesTest {
    @Test fun `disregard inherits action marking and exposes both cases under anabhihita`() {
        val meanings = setOf(SemanticRelation.ACTION_MARKING, SemanticRelation.DISREGARD_ANADARA)
        for (abhihita in listOf(false, true)) for (case in listOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI)) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(case), semanticRelations = meanings)
            val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, setOf(case), abhihita, p)
            assertEquals(!abhihita, SasthiCanadareSutra.matches(context))
            assertFalse(YasyaCaBhavenaBhavalaksanamSutra.matches(context))
            if (!abhihita) assertEquals(case, (SasthiCanadareSutra.apply(context) as VibhaktiRuleResult.Assigned).vibhakti)
            assertFalse(SasthiCanadareSutra.matches(context.copy(participant = p.copy(
                semanticRelations = setOf(SemanticRelation.DISREGARD_ANADARA)))))
        }
        assertEquals(setOf(Vibhakti.SASTHI, Vibhakti.SAPTAMI), SasthiCanadareSutra.permittedCases)
    }
}

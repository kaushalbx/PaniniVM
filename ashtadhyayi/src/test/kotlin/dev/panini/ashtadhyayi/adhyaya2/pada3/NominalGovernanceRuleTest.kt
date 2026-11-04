package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.ParticipantFacts
import dev.panini.analysis.SemanticRelation
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NominalGovernanceRuleTest {
    @Test fun `2 3 44 observes anabhihita governance rather than applying unconditionally`() {
        val governor = SubantaPada("उत्सुक", MulaPratipadika("उत्सुक", "उत्सुक"), SupPratyaya("सुँ", "सुँ"))
        val p = ParticipantFacts("dependent", AvyayaPada("केशैः", "केशैः"), setOf(Vibhakti.TRTIYA),
            semanticRelations = setOf(SemanticRelation.ENGROSSED_ATTACHMENT), governingNominal = governor)
        val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, p.possibleVibhaktis, participant = p)
        assertTrue(PrasitotsukabhyamTrtiyaCaSutra.matches(context))
        assertFalse(PrasitotsukabhyamTrtiyaCaSutra.matches(context.copy(abhihita = true)))
        assertTrue("2.3.1" in PrasitotsukabhyamTrtiyaCaSutra.adhikara)
    }

    @Test fun `2 3 43 cannot classify a bare locative as the respect construction`() {
        assertFalse(SadhuNipunabhyamSutra.matches(VibhaktiRuleContext(
            Karaka.ANIRDHARITA, setOf(Vibhakti.SAPTAMI))))
        assertFalse(SadhuNipunabhyamSutra.matches(VibhaktiRuleContext(
            Karaka.ADHIKARANA, setOf(Vibhakti.SAPTAMI))))
    }
}

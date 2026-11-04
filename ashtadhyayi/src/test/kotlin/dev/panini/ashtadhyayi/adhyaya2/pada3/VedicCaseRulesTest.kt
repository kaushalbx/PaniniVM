package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.AvyayaPada
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class VedicCaseRulesTest {
    @Test fun `vedic case rules observe role and anabhihita in addition to licence`() {
        val p = ParticipantFacts("licensed", AvyayaPada("test", "test"), setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(SemanticRelation.INSTRUMENT, SemanticRelation.VEDIC_YAJ_INSTRUMENT),
            vedicCaseLicense = VedicCaseLicense("2.3.63", "Kāśikā ad 2.3.63"))
        val context = VibhaktiRuleContext(Karaka.KARANA, p.possibleVibhaktis, participant = p,
            dhatu = DhatuIdentity("यज्"), register = GrammarRegister.VEDIC)
        assertTrue(YajesCaKaraneSutra.matches(context))
        assertFalse(YajesCaKaraneSutra.matches(context.copy(abhihita = true)))
        assertFalse(YajesCaKaraneSutra.matches(context.copy(karaka = Karaka.KARMAN)))
        val dative = context.copy(karaka = Karaka.ANIRDHARITA, participant = p.copy(
            semanticRelations = setOf(SemanticRelation.VEDIC_DATIVE_MEANING),
            vedicCaseLicense = VedicCaseLicense("2.3.62", "Kāśikā ad 2.3.62")))
        assertTrue(CaturthyartheBahulamChandasiSutra.matches(dative))
        assertFalse(CaturthyartheBahulamChandasiSutra.matches(dative.copy(abhihita = true)))
        assertFalse(CaturthyartheBahulamChandasiSutra.matches(dative.copy(karaka = Karaka.SAMBODHANA)))
    }
}

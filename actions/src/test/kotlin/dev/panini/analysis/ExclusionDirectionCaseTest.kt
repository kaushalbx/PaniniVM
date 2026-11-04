package dev.panini.analysis

import dev.panini.ashtadhyayi.adhyaya2.pada3.AnyaraditarartedikshabdaSutra
import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class ExclusionDirectionCaseTest {
    private val governor = SubantaPada("अन्य", MulaPratipadika("अन्य", "अन्य"), SupPratyaya("सुँ", "सुँ"))
    @Test fun `linked exclusion reference requires anabhihita and does not acquire residual genitive`() {
        val p = ParticipantFacts("reference", AvyayaPada("ग्राम", "ग्राम"), setOf(Vibhakti.PANCHAMI),
            semanticRelations = setOf(SemanticRelation.DIRECTIONAL_EXCLUSION), governingNominal = governor)
        for (abhihita in listOf(false, true)) {
            assertEquals(!abhihita, AnyaraditarartedikshabdaSutra.matches(
                VibhaktiRuleContext(Karaka.ANIRDHARITA, setOf(Vibhakti.PANCHAMI), abhihita, p)))
        }
        val wrong = p.copy(possibleVibhaktis = setOf(Vibhakti.SASTHI),
            semanticRelations = p.semanticRelations + SemanticRelation.SESHA_RELATION)
        val result = KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("कृ"), wrong, listOf(wrong), Prayoga.KARTARI))
        assertNull(result.resolvedVibhakti)
        assertTrue(result.evidence.isEmpty())
        val modifiedGovernor = governor.copy(pratipadika = MulaPratipadika("अन्य", "अन्य",
            listOf(TaddhitaVikara("त्व", "त्व"))))
        assertFalse(ExclusionDirectionGovernance.hasConstruction(
            VibhaktiRuleContext(Karaka.ANIRDHARITA, setOf(Vibhakti.PANCHAMI), participant = p.copy(governingNominal = modifiedGovernor))))
    }
}

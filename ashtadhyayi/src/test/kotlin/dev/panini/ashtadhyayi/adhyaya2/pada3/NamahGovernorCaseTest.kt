package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.analysis.*
import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class NamahGovernorCaseTest {
    @Test fun `listed expressions require a link and alam requires sufficiency under anabhihita`() {
        for (form in listOf("नमः", "स्वस्ति", "स्वाहा", "स्वधा", "वषट्", "अलम्")) {
            val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.CHATURTHI),
                semanticRelations = if (form == "अलम्") setOf(SemanticRelation.SUFFICIENCY) else emptySet(),
                governingAvyaya = AvyayaPada(form, form))
            val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, p.possibleVibhaktis, participant = p)
            assertTrue(NamahSvastiSvahaSutra.matches(context), form)
            assertFalse(NamahSvastiSvahaSutra.matches(context.copy(abhihita = true)), form)
            assertFalse(NamahSvastiSvahaSutra.matches(context.copy(participant = p.copy(governingAvyaya = null))), form)
            assertFalse(NamahSvastiSvahaSutra.matches(context.copy(morphologicalCandidates = setOf(Vibhakti.SASTHI))), form)
            if (form == "अलम्") assertFalse(NamahSvastiSvahaSutra.matches(context.copy(
                participant = p.copy(semanticRelations = emptySet()))))
        }
    }

    @Test fun `svasti dative excludes general benediction genitive even with overlapping nominal facts`() {
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.SASTHI),
            semanticRelations = setOf(SemanticRelation.BENEDICTION_WELLBEING, SemanticRelation.SESHA_RELATION),
            governingNominal = SubantaPada("हित", MulaPratipadika("हित", "हित"), SupPratyaya("सुँ", "सुँ")),
            governingAvyaya = AvyayaPada("स्वस्ति", "स्वस्ति"))
        val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, p.possibleVibhaktis, participant = p)
        assertFalse(CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra.matches(context))
        assertFalse(SasthiSeseSutra.matches(context))
        assertTrue(NamahSvastiSvahaSutra.matches(context.copy(morphologicalCandidates = setOf(Vibhakti.CHATURTHI))))
        assertTrue(CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra.matches(context.copy(
            participant = p.copy(governingAvyaya = null))))
    }

    @Test fun `nominal sufficiency needs its selected sense and rejects derived homonyms`() {
        val head = SubantaPada("प्रभु", MulaPratipadika("प्रभु", "प्रभु"), SupPratyaya("सुँ", "सुँ"))
        val p = ParticipantFacts("p", AvyayaPada("p", "p"), setOf(Vibhakti.CHATURTHI),
            semanticRelations = setOf(SemanticRelation.SUFFICIENCY), governingNominal = head)
        val context = VibhaktiRuleContext(Karaka.ANIRDHARITA, p.possibleVibhaktis, participant = p)
        assertTrue(NamahSvastiSvahaSutra.matches(context))
        assertFalse(NamahSvastiSvahaSutra.matches(context.copy(participant = p.copy(semanticRelations = emptySet()))))
        val derived = head.copy(pratipadika = MulaPratipadika("प्रभु", "प्रभु", listOf(TaddhitaVikara("त्व", "त्व"))))
        assertFalse(NamahSvastiSvahaSutra.matches(context.copy(participant = p.copy(governingNominal = derived))))
    }
}

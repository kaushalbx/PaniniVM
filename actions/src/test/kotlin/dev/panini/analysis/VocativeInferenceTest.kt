package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.SupPratyaya
import kotlin.test.Test
import kotlin.test.assertEquals

class VocativeInferenceTest {
    private fun resolve(relations: Set<SemanticRelation>, number: Vacana? = Vacana.EKAVACANA,
        cases: Set<Vibhakti> = setOf(Vibhakti.PRATHAMA), voice: Prayoga = Prayoga.KARTARI): KarakaResolution {
        val expression = SubantaPada("राम", MulaPratipadika("राम", "राम"), SupPratyaya("सुँ", "सुँ"))
        val participant = ParticipantFacts("addressed", expression, cases, semanticRelations = relations, vacana = number)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("पच्", sakarmaka = true),
            participant, listOf(participant), voice))
    }

    @Test fun `address uses 2 3 47 and nominal designations are separate from case assignment`() {
        for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
            for (number in listOf(null) + Vacana.entries) {
                val result = resolve(setOf(SemanticRelation.DIRECT_ADDRESS), number, voice = voice)
                assertEquals(Karaka.SAMBODHANA, result.resolved)
                assertEquals(Vibhakti.PRATHAMA, result.resolvedVibhakti)
                assertEquals(listOf("2.3.47"), result.evidence.map { it.sutra })
                val expected = if (number == Vacana.EKAVACANA)
                    listOf(NominalDesignation.AMANTRITA, NominalDesignation.SAMBUDDHI)
                    else listOf(NominalDesignation.AMANTRITA)
                assertEquals(expected, result.nominalDesignations.map { it.designation })
                assertEquals(if (number == Vacana.EKAVACANA) listOf("2.3.48", "2.3.49") else listOf("2.3.48"),
                    result.nominalDesignations.map { it.evidence.sutra })
            }
        }
    }

    @Test fun `bare nominative is not proof of bare meaning or address`() {
        val unknown = resolve(emptySet())
        assertEquals(null, unknown.resolved)
        assertEquals(null, unknown.resolvedVibhakti)
        assertEquals(emptyList(), unknown.nominalDesignations)
        val bare = resolve(setOf(SemanticRelation.BARE_NOMINAL_MEANING))
        assertEquals(Karaka.ANIRDHARITA, bare.resolved)
        assertEquals(listOf("2.3.46"), bare.evidence.map { it.sutra })
        assertEquals(emptyList(), bare.nominalDesignations)
        val wrongCase = resolve(setOf(SemanticRelation.DIRECT_ADDRESS), cases = setOf(Vibhakti.DVITIYA))
        assertEquals(null, wrongCase.resolvedVibhakti)
        assertEquals(emptyList(), wrongCase.nominalDesignations)
    }

    @Test fun `address interpretation is not converted to verbal agent or bare naming`() {
        val result = resolve(setOf(SemanticRelation.DIRECT_ADDRESS,
            SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.BARE_NOMINAL_MEANING))
        assertEquals(Karaka.SAMBODHANA, result.resolved)
        assertEquals(listOf("2.3.47"), result.evidence.map { it.sutra })
    }
}

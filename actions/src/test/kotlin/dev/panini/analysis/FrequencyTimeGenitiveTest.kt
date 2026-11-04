package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FrequencyTimeGenitiveTest {
    private val premises = setOf(SemanticRelation.FREQUENCY_TIME_LOCUS, SemanticRelation.TIME_REFERENCE,
        SemanticRelation.LOCATION, SemanticRelation.SESHA_RELATION)
    private val twice = AvyayaPada("द्विः", "द्विः")
    private fun resolve(relations: Set<SemanticRelation> = premises, frequency: AvyayaPada? = twice,
        cases: Set<Vibhakti> = setOf(Vibhakti.SASTHI), register: GrammarRegister = GrammarRegister.CLASSICAL,
        voice: Prayoga = Prayoga.KARTARI, extras: List<ParticipantFacts> = emptyList()): KarakaResolution {
        val p = ParticipantFacts("day", AvyayaPada("अह्नः", "अह्नः"), cases,
            semanticRelations = relations, frequencyGovernor = frequency)
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भुज्"), p, listOf(p) + extras,
            voice, register = register))
    }

    @Test fun `explicit counting time relation uses 2 3 64 in any register and either voice`() {
        for (register in GrammarRegister.entries) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                val result = resolve(register = register, voice = voice)
                assertEquals(Karaka.SAMBANDHA, result.resolved)
                assertEquals(listOf("2.3.64"), result.evidence.map { it.sutra })
                assertEquals(Vibhakti.SASTHI, result.resolvedVibhakti)
            }
        }
        val typed = AvyayaPada("पञ्चकृत्वः", "पञ्चकृत्वः", AvyayaTaddhitaDerivation("पञ्चन्", "कृत्वसुच्"))
        assertEquals(listOf("2.3.64"), resolve(frequency = typed).evidence.map { it.sutra })
    }

    @Test fun `implied frequency spatial locus temporal instrument and unlinked word do not establish 2 3 64`() {
        assertTrue(resolve(frequency = null).evidence.none { it.sutra == "2.3.64" })
        for (missing in premises) {
            assertTrue(resolve(relations = premises - missing).evidence.none { it.sutra == "2.3.64" }, missing.name)
        }
        for (word in listOf("पुनः", "सदा", "अज्ञातम्")) {
            assertTrue(resolve(frequency = AvyayaPada(word, word)).evidence.none { it.sutra == "2.3.64" })
        }
        val unrelated = ParticipantFacts("unrelated-frequency", twice, emptySet())
        assertTrue(resolve(frequency = null, extras = listOf(unrelated)).evidence.none { it.sutra == "2.3.64" })
        val instrument = premises - SemanticRelation.LOCATION + SemanticRelation.INSTRUMENT
        assertTrue(resolve(relations = instrument).evidence.none { it.sutra == "2.3.64" })
        val badDerivation = twice.copy(derivation = SankhyaAvyayaDerivation("DHA", listOf("द्वि")))
        assertTrue(resolve(frequency = badDerivation).evidence.none { it.sutra == "2.3.64" })
    }

    @Test fun `ordinary locus construal retains locative instead of forcing relational genitive`() {
        val ordinary = resolve(relations = premises - SemanticRelation.SESHA_RELATION, cases = setOf(Vibhakti.SAPTAMI))
        assertEquals(Karaka.ADHIKARANA, ordinary.resolved)
        assertEquals(listOf("1.4.45", "2.3.36"), ordinary.evidence.map { it.sutra })
        assertEquals(null, resolve(cases = setOf(Vibhakti.DVITIYA)).resolvedVibhakti)
    }
}

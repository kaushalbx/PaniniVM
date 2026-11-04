package dev.panini.analysis

import dev.panini.core.*
import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ComparisonBenedictionCasesTest {
    private fun governor(stem: String, vikaras: List<PratipadikaVikara> = emptyList()) =
        SubantaPada(stem, MulaPratipadika(stem, stem, vikaras), SupPratyaya("सुँ", "सुँ"))
    private fun resolve(stem: String?, cases: Set<Vibhakti>, relations: Set<SemanticRelation>,
        voice: Prayoga = Prayoga.KARTARI, vikaras: List<PratipadikaVikara> = emptyList(),
        extras: List<ParticipantFacts> = emptyList()): KarakaResolution {
        val p = ParticipantFacts("dependent", AvyayaPada("dependent", "dependent"), cases,
            semanticRelations = relations, governingNominal = stem?.let { governor(it, vikaras) })
        return KarakaRuleEngine.resolve(KarakaRuleContext(DhatuIdentity("भू"), p, listOf(p) + extras, voice))
    }
    private val comparison = setOf(SemanticRelation.EQUAL_COMPARISON, SemanticRelation.SESHA_RELATION)
    private val blessing = setOf(SemanticRelation.BENEDICTION_WELLBEING, SemanticRelation.SESHA_RELATION)

    @Test fun `linked equal comparison retains instrumental genitive without inventing a karaka`() {
        for (stem in listOf("तुल्य", "सदृश", "समान", "सम")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                for (case in setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI)) {
                    val result = resolve(stem, setOf(case), comparison, voice)
                    assertEquals(Karaka.SAMBANDHA, result.resolved)
                    assertEquals(case, result.resolvedVibhakti)
                    assertEquals(listOf("2.3.72"), result.evidence.map { it.sutra })
                    assertEquals(setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI) - case,
                        result.alternatives.mapNotNull { it.vibhakti }.toSet())
                    assertTrue(result.alternatives.none { it.matchesObservedMorphology })
                }
            }
        }
        val ambiguous = resolve("तुल्य", setOf(Vibhakti.TRTIYA, Vibhakti.SASTHI), comparison)
        assertEquals(Karaka.SAMBANDHA, ambiguous.resolved)
        assertEquals(null, ambiguous.resolvedVibhakti)
        assertTrue(ambiguous.alternatives.single().matchesObservedMorphology)
    }

    @Test fun `seven benediction governors retain dative genitive and no instrumental branch`() {
        for (stem in listOf("आयुष्य", "मद्र", "भद्र", "कुशल", "सुख", "अर्थ", "हित")) {
            for (voice in listOf(Prayoga.KARTARI, Prayoga.KARMANI)) {
                for (case in setOf(Vibhakti.CHATURTHI, Vibhakti.SASTHI)) {
                    val result = resolve(stem, setOf(case), blessing, voice)
                    assertEquals(Karaka.SAMBANDHA, result.resolved)
                    assertEquals(case, result.resolvedVibhakti)
                    assertEquals(listOf("2.3.73"), result.evidence.map { it.sutra })
                    assertEquals(setOf(Vibhakti.CHATURTHI, Vibhakti.SASTHI) - case,
                        result.alternatives.mapNotNull { it.vibhakti }.toSet())
                }
            }
            assertEquals(null, resolve(stem, setOf(Vibhakti.TRTIYA), blessing).resolvedVibhakti)
        }
        val ambiguous = resolve("हित", setOf(Vibhakti.CHATURTHI, Vibhakti.SASTHI), blessing)
        assertEquals(Karaka.SAMBANDHA, ambiguous.resolved)
        assertEquals(null, ambiguous.resolvedVibhakti)
        assertTrue(ambiguous.alternatives.single().matchesObservedMorphology)
    }

    @Test fun `excluded comparison words missing links senses and sesha cannot license special cases`() {
        for (stem in listOf(null, "तुला", "उपमा", "इव", "यथा", "अज्ञात")) {
            assertEquals(null, resolve(stem, setOf(Vibhakti.TRTIYA), comparison).resolvedVibhakti)
            assertEquals(listOf("2.3.50"), resolve(stem, setOf(Vibhakti.SASTHI), comparison).evidence.map { it.sutra })
        }
        for ((stem, relations, specialCase) in listOf(
            Triple("तुल्य", comparison, Vibhakti.TRTIYA), Triple("हित", blessing, Vibhakti.CHATURTHI))) {
            for (missing in relations) {
                assertEquals(null, resolve(stem, setOf(specialCase), relations - missing).resolvedVibhakti)
            }
            assertEquals(null, resolve(null, setOf(specialCase), relations,
                extras = listOf(ParticipantFacts("unlinked", governor(stem), emptySet()))).resolvedVibhakti)
            for (vikara in listOf(TaddhitaVikara("इनि", "इनि"), StriVikara("unknown", "unknown"))) {
                assertEquals(null, resolve(stem, setOf(specialCase), relations, vikaras = listOf(vikara)).resolvedVibhakti)
            }
        }
        val instrument = resolve("तुल्य", setOf(Vibhakti.TRTIYA),
            setOf(SemanticRelation.EQUAL_COMPARISON, SemanticRelation.INSTRUMENT))
        assertEquals(listOf("1.4.42", "2.3.18"), instrument.evidence.map { it.sutra })
        assertEquals(listOf("2.3.50"), resolve("आयुष्य", setOf(Vibhakti.SASTHI),
            setOf(SemanticRelation.SESHA_RELATION)).evidence.map { it.sutra })
    }
}

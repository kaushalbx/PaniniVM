package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada2.*
import dev.panini.ashtadhyayi.adhyaya8.pada3.*
import dev.panini.ashtadhyayi.adhyaya8.pada4.*
import dev.panini.shiksha.*
import kotlin.test.*

class TripadiAnnotationPreservationTest {
    private fun state(left: String, right: String? = null, affix: Boolean = false): DerivationState {
        val term = DerivationTerm("left", left, if (affix) TermKind.PRATYAYA else TermKind.PRATIPADIKA,
            itProcessingPhase = ItProcessingPhase.PROCESSED, formedPadaRupa = Rupa())
        return DerivationState(
            terms = listOf(term) + listOfNotNull(right?.let { DerivationTerm("right", it, TermKind.PRATIPADIKA) }),
            samjnas = setOf(SamjnaAssignment("left", Samjna.PADA)),
            stage = DerivationStage.PADA_FORMED,
        )
    }

    @Test
    fun `tripadi exact mutations preserve unrelated nasalization and parsed signs`() {
        // Synthetic annotated inputs test representation preservation, not new lexical prescriptions.
        val cases: List<Triple<DerivationSutra, DerivationState, List<Varna>>> = listOf(
            Triple(CohKuhSutra, state("अँच्ऽ"), listOf(Svara.A, Vyanjana.KA)),
            Triple(ShadhohKahSiSutra, state("अँष्ऽ", "स"), listOf(Svara.A, Vyanjana.KA)),
            Triple(NaloPratipadikantasyaSutra, state("अँन्ऽ"), listOf(Svara.A)),
            Triple(SamyogantasyaLopaSutra, state("अँक्त्ऽ"), listOf(Svara.A, Vyanjana.KA)),
            Triple(HaliSarveshamSutra, state("आँयऽ", "ह"), listOf(Svara.AA)),
            Triple(VisarjaniyasyaSahSutra, state("अँःऽ", "त"), listOf(Svara.A, Vyanjana.SA)),
            Triple(BhoBhagoSutra, state("अँर्ऽ", "इ").addVarnaSubstitution("left", Vyanjana.SA, listOf(Vyanjana.RA), "8.2.66"), listOf(Svara.A, Vyanjana.YA)),
            Triple(StosShcunaShcuhSutra, state("अँत्श्ऽ"), listOf(Svara.A, Vyanjana.CA, Vyanjana.SHA)),
            Triple(StunaShtuhSutra, state("अँत्ष्ऽ"), listOf(Svara.A, Vyanjana.TTA, Vyanjana.SSA)),
            Triple(DhoDheLopaSutra, state("अँढ्ऽ", "ढ"), listOf(Svara.A)),
            Triple(MonusvarahSutra, state("अँम्ऽ", "क"), listOf(Svara.A, Ayogavaha.ANUSVARA)),
            Triple(NashcapadantasyaSutra, state("अँन्त्ऽ"), listOf(Svara.A, Ayogavaha.ANUSVARA, Vyanjana.TA)),
            Triple(AdesapratyayayohSutra, state("इँसऽति", affix = true), listOf(Svara.I, Vyanjana.SSA, Svara.A, Vyanjana.TA, Svara.I)),
            Triple(AnusvarasyaYayiParasavarnahSutra, state("अँंऽ", "क").copy(samjnas = emptySet()), listOf(Svara.A, Vyanjana.NGA)),
            Triple(VaPadantasyaSutra, state("अँंऽ", "क"), listOf(Svara.A, Vyanjana.NGA)),
            Triple(JhalamJashJhashiSutra, state("अँत्ऽ", "द"), listOf(Svara.A, Vyanjana.DA)),
            Triple(KhariCaSutra, state("अँद्ऽ", "क"), listOf(Svara.A, Vyanjana.TA)),
            Triple(VavasaneSutra, state("अँद्ऽ"), listOf(Svara.A, Vyanjana.TA)),
            Triple(JharoJhariSavarneSutra, state("अँक्त्त्ऽ", "त"), listOf(Svara.A, Vyanjana.KA, Vyanjana.TA)),
        )
        for ((rule, original, expected) in cases) {
            assertTrue(rule.matches(original), rule.sutra)
            val result = rule.apply(original).state
            val term = result.terms.first()
            assertEquals(expected, term.varnas, rule.sutra)
            assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized, rule.sutra)
            val signBoundary = if (rule == AdesapratyayayohSutra) 3 else expected.size
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, signBoundary)), term.orthographicSigns, rule.sutra)
            assertEquals(original.terms.first().id, term.id)
            assertEquals(original.terms.first().upadesha, term.upadesha)
            assertEquals(original.terms.first().formedPadaRupa, term.formedPadaRupa)
            assertEquals(original.terms.drop(1), result.terms.drop(1))
            assertEquals(rule.sutra, result.substitutions.last().sutra)
        }
    }

    @Test
    fun `natva preserves nasal vowels and signs around the exact target`() {
        for ((rule, input, boundary) in listOf(
            Triple(RasabhyamNoNahSutra, "अँर्नऽ", 4),
            Triple(AtkupvangnumvyavayePiSutra, "अँरनऽ", 5),
        )) {
            val original = state(input)
            assertTrue(rule.matches(original), rule.sutra)
            val result = rule.apply(original).state
            val term = result.terms.first()
            assertTrue(Vyanjana.NNA in term.varnas, rule.sutra)
            assertFalse(Vyanjana.NA in term.varnas, rule.sutra)
            assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, boundary)), term.orthographicSigns)
            assertEquals(original.terms.first().id, term.id)
            assertEquals(original.terms.first().upadesha, term.upadesha)
            assertEquals(original.terms.first().formedPadaRupa, term.formedPadaRupa)
            assertEquals(rule.sutra, result.substitutions.last().sutra)
        }
    }

    @Test
    fun `initial ending substitutions retain following nasal vowels and signs`() {
        val cases = listOf(
            Triple(JhasasTathorDhoAdhahSutra, "ध्", "तँऽ"),
            Triple(InahShidhvamLunglitamDhoAngatSutra, "षी", "धँऽ"),
            Triple(VibhashetahSutra, "इ", "धँऽ"),
        )
        for ((rule, stem, ending) in cases) {
            val original = DerivationState(
                terms = listOf(
                    DerivationTerm("stem", stem, TermKind.DHATU),
                    DerivationTerm("ending", ending, TermKind.PRATYAYA, upadesha = "ध्वम्", formedPadaRupa = Rupa()),
                ),
                droppedTerms = listOf(
                    DerivationTerm("it-agama", "इ", TermKind.PRATYAYA),
                    DerivationTerm("sic", "स्", TermKind.PRATYAYA, upadesha = "सिँच्"),
                ),
                context = DerivationalContext(rupa = Rupa(lakara = if (rule == VibhashetahSutra) dev.panini.core.Lakara.LUNG else dev.panini.core.Lakara.LING)),
            )
            assertTrue(rule.matches(original), rule.sutra)
            val result = rule.apply(original).state
            val term = result.terms.last()
            assertEquals(if (rule == JhasasTathorDhoAdhahSutra) Vyanjana.DHA else Vyanjana.DDHA, term.varnas.first())
            assertTrue(term.phonologicalText.effectiveVarnas.last().nasalized)
            assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
            assertEquals(original.terms.first(), result.terms.first())
            assertEquals(original.terms.last().id, term.id)
            assertEquals(original.terms.last().upadesha, term.upadesha)
            assertEquals(original.terms.last().formedPadaRupa, term.formedPadaRupa)
            assertEquals(original.droppedTerms, result.droppedTerms)
            assertEquals(rule.sutra, result.substitutions.single().sutra)
        }
    }

    @Test
    fun `tas final deletion preserves signs and the following ending`() {
        val initial = state("ताँस्ऽ", "ध", affix = true)
        val original = initial.replaceTerm("left", initial.terms.first().copy(upadesha = "तासि"))
        assertTrue(DhiCaSutra.matches(original))
        val result = DhiCaSutra.apply(original).state
        val term = result.terms.first()
        assertEquals(listOf(Vyanjana.TA, Svara.AA), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
        assertEquals(original.terms.first().id, term.id)
        assertEquals(original.terms.first().upadesha, term.upadesha)
        assertEquals(original.terms.first().formedPadaRupa, term.formedPadaRupa)
        assertEquals(original.terms.last(), result.terms.last())
        assertEquals(1, result.substitutions.size)
    }

    @Test
    fun `active ru replacement preserves annotations on both sides of its range`() {
        val original = state("अँस्ऽ", "इ")
        assertTrue(BhoBhagoAghoApurvasyaYoshiSutra.matches(original))
        val result = BhoBhagoAghoApurvasyaYoshiSutra.apply(original).state
        val term = result.terms.first()
        assertEquals(listOf(Svara.A, Vyanjana.YA), term.varnas)
        assertEquals(listOf(true, false), term.phonologicalText.effectiveVarnas.map { it.nasalized })
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
        assertEquals(1, result.substitutions.size)
        assertEquals(original.terms.last(), result.terms.last())
        assertEquals(original.terms.first().formedPadaRupa, term.formedPadaRupa)
    }

    @Test
    fun `suffixal visarga substitution preserves unrelated annotations`() {
        val initial = state("अँष्ऽ", affix = true)
        val original = initial.replaceTerm("left", initial.terms.single().copy(upadesha = "स्"))
        assertTrue(KharavasanayorVisarjaniyahSutra.matches(original))
        val result = KharavasanayorVisarjaniyahSutra.apply(original).state
        val term = result.terms.single()
        assertEquals(listOf(Svara.A, Ayogavaha.VISARGA), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.first().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
        assertEquals(original.terms.single().upadesha, term.upadesha)
        assertEquals(original.terms.single().formedPadaRupa, term.formedPadaRupa)
        assertEquals(KharavasanayorVisarjaniyahSutra.sutra, result.substitutions.last().sutra)
    }

    @Test
    fun `initial sha substitution preserves nasal vowel and parsed sign`() {
        val original = state("त्", "शँऽ")
        assertTrue(ShashChoAtiSutra.matches(original))
        val result = ShashChoAtiSutra.apply(original).state
        val term = result.terms.last()
        assertEquals(listOf(Vyanjana.CHA, Svara.A), term.varnas)
        assertTrue(term.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), term.orthographicSigns)
        assertEquals(original.terms.first(), result.terms.first())
        assertEquals(original.terms.last().id, term.id)
        assertEquals(original.terms.last().upadesha, term.upadesha)
        assertEquals(ShashChoAtiSutra.sutra, result.substitutions.last().sutra)
    }

    @Test
    fun `initial h substitution preserves following nasal vowel and avagraha`() {
        val original = state("द्", "हँऽ")
        assertTrue(JhayoHonyatarasyamSutra.matches(original))
        val result = JhayoHonyatarasyamSutra.apply(original).state
        val target = result.terms.last()
        assertEquals(listOf(Vyanjana.DHA, Svara.A), target.varnas)
        assertTrue(target.phonologicalText.effectiveVarnas.last().nasalized)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), target.orthographicSigns)
        assertEquals(original.terms.first(), result.terms.first())
        assertEquals(original.terms.last().upadesha, target.upadesha)
    }
}

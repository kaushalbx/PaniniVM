package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya3.pada4.*
import dev.panini.core.*
import kotlin.test.*

class TingEndingVarnaConditionTest {
    private fun state(surface: String, upadesha: String, lakara: Lakara,
                      stage: DerivationStage = DerivationStage.PADA_FORMED) = DerivationState(
        listOf(DerivationTerm("root", "भू", TermKind.DHATU),
            DerivationTerm("ending", surface, TermKind.PRATYAYA, upadesha = upadesha)),
        stage = stage, context = DerivationalContext(rupa = Rupa(lakara = lakara)))

    @Test
    fun `completed endings ignore orthographic signs and vowel annotations`() {
        assertFalse(ThasasseSutra.matches(state("सेँऽ", "थास्", Lakara.LAT)))
        assertTrue(ThasasseSutra.matches(state("सै", "थास्", Lakara.LAT)))
        assertFalse(JhasyaRanSutra.matches(state("रँन्ऽ", "झ", Lakara.LING)))
        assertTrue(JhasyaRanSutra.matches(state("रान्", "झ", Lakara.LING)))
        for (surface in listOf("ुस्", "उस्", "उँस्ऽ")) {
            assertFalse(JherJusSutra.matches(state(surface, "झि", Lakara.LING)))
        }
        assertTrue(JherJusSutra.matches(state("ऊस्", "झि", Lakara.LING)))
    }

    @Test
    fun `let augment readiness depends on varnas and typed ending inventory`() {
        fun ready(surface: String, affix: TingAffix): DerivationState {
            val original = state(surface, affix.upadesha, Lakara.LET)
            return original.replaceTerm("ending", original.terms.last().copy(sourceTingAffix = affix))
        }
        for ((surface, affix) in listOf("तिँप्ऽ" to TingAffix.TIP, "सिँप्ऽ" to TingAffix.SIP,
            "मिँ" to TingAffix.MIP, "आतेँऽ" to TingAffix.ATAM, "आथेँऽ" to TingAffix.ATHAM)) {
            assertFalse(LetodatauSutra.matches(ready(surface, affix)))
        }
        for (affix in TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }) {
            assertFalse(LetodatauSutra.matches(ready(affix.upadesha + "ऽ", affix)))
        }
        for ((surface, affix) in listOf("तिँऽ" to TingAffix.TIP, "सिँऽ" to TingAffix.SIP,
            "निँऽ" to TingAffix.MIP, "ऐतेँऽ" to TingAffix.ATAM, "ऐथेँऽ" to TingAffix.ATHAM)) {
            val original = ready(surface, affix)
            assertTrue(LetodatauSutra.matches(original))
            val result = LetodatauSutra.apply(original).state
            assertEquals(surface, result.terms.last().surface)
            assertEquals(affix, result.terms.last().sourceTingAffix)
            assertEquals(original.terms.first(), result.terms.first())
            assertFalse(LetodatauSutra.matches(result))
            assertFalse(LetodatauSutra.matches(original.copy(stage = DerivationStage.INITIAL)))
            if (affix.pada == PadaType.ATMANEPADA) {
                assertFalse(LetodatauSutra.matches(original.copy(context = original.context.copy(letEOption = LetEOption.AI))))
            }
        }
    }

    @Test
    fun `lit parasmaipada table preserves fresh upadesha processing policies`() {
        val rule = ParasmaipadanamNalatUsusthalathusaNalvamahSutra
        for ((affix, expected) in listOf(TingAffix.TIP to "णल्", TingAffix.TAS to "अतुस्",
            TingAffix.JHI to "उस्", TingAffix.SIP to "थल्", TingAffix.THAS to "अथुस्",
            TingAffix.THA to "अ", TingAffix.MIP to "अ", TingAffix.VAS to "व", TingAffix.MAS to "म")) {
            val original = state(affix.upadesha, affix.upadesha, Lakara.LIT)
            assertTrue(rule.matches(original))
            val result = rule.apply(original).state
            val ending = result.terms.last()
            assertEquals(expected, ending.surface)
            assertEquals(expected, ending.upadesha)
            assertEquals(original.terms.last().id, ending.id)
            assertEquals(original.terms.first(), result.terms.first())
            assertEquals(affix.upadesha, ending.sthaniProps?.upadesha)
            assertEquals(if (affix in setOf(TingAffix.TIP, TingAffix.SIP)) ItProcessingPhase.RAW_UPADESHA
                else ItProcessingPhase.PROCESSED, ending.itProcessingPhase)
            assertFalse(rule.matches(result))
            assertFalse(rule.matches(state(expected + "ऽ", affix.upadesha, Lakara.LIT)))
            assertFalse(rule.matches(original.replaceTerm("ending", original.terms.last().copy(kind = TermKind.DHATU))))
        }
    }

    @Test
    fun `tere table completion checks use canonical varnas`() {
        for ((upadesha, completed) in listOf("त" to "ते", "आताम्" to "आते", "आथाम्" to "आथे",
            "ध्वम्" to "ध्वे", "वहि" to "वहे", "महिङ्" to "महे", "इट्" to "ए")) {
            val original = state(upadesha, upadesha, Lakara.LET)
            assertTrue(TitaAtmanepadanamTereSutra.matches(original))
            assertEquals(completed, TitaAtmanepadanamTereSutra.apply(original).state.terms.last().surface)
            assertFalse(TitaAtmanepadanamTereSutra.matches(state(completed + "ँऽ", upadesha, Lakara.LET)))
        }
    }

    @Test
    fun `tere positional outcomes preserve untouched and substituted annotations`() {
        for ((source, priorSutra, expected) in listOf(
            Triple("अँन्त्ऽ", "7.1.3", "अँन्तेऽ"),
            Triple("अँतँऽ", "7.1.5", "अँतेँऽ"),
        )) {
            val original = state(source, "झ", Lakara.LET).copy(appliedSutras = listOf(priorSutra))
            assertTrue(TitaAtmanepadanamTereSutra.matches(original))
            val result = TitaAtmanepadanamTereSutra.apply(original).state
            assertEquals(expected, result.terms.last().surface)
            assertEquals(original.terms.first(), result.terms.first())
            assertEquals(original.terms.last().id, result.terms.last().id)
            assertEquals(original.terms.last().upadesha, result.terms.last().upadesha)
        }
    }

    @Test
    fun `ametah recognizes completed jhi endings as varnas`() {
        val stem = DerivationTerm("shap", "अ", TermKind.PRATYAYA, upadesha = "शप्")
        for (completed in listOf("न्तुँऽ", "अन्तुँऽ", "अतुँऽ")) {
            val original = state(completed, "झि", Lakara.LOT)
            assertFalse(AmetahSutra.matches(original.copy(terms = original.terms.dropLast(1) + stem + original.terms.last())))
        }
        for ((gana, expected) in listOf(DhatuGana.BHVADI to "न्तु", DhatuGana.ADADI to "अन्तु", DhatuGana.JUHOTYADI to "अतु")) {
            val original = state("झि", "झि", Lakara.LOT)
            val ready = original.copy(terms = listOf(original.terms.first().copy(gana = gana), stem, original.terms.last()))
            assertTrue(AmetahSutra.matches(ready))
            assertEquals(expected, AmetahSutra.apply(ready).state.terms.last().surface)
        }
    }

    @Test
    fun `first person lot augment recognizes annotated endings`() {
        for ((surface, upadesha) in listOf("एँऽ" to "इट्", "वहेँऽ" to "वहि", "महेँऽ" to "महिङ्")) {
            val original = state(surface, upadesha, Lakara.LOT).copy(
                context = DerivationalContext(rupa = Rupa(lakara = Lakara.LOT, purusha = Purusha.UTTAMA)))
            assertTrue(AdUttamasyaPicCaSutra.matches(original))
            val result = AdUttamasyaPicCaSutra.apply(original).state
            assertEquals(surface, result.terms.last().surface)
            assertEquals(original.terms.last().id, result.terms.last().id)
            assertEquals(original.terms.first(), result.terms.first())
            assertFalse(AdUttamasyaPicCaSutra.matches(result))
        }
    }

    @Test
    fun `lot ending tables preserve final vowels and stage gates`() {
        for ((upadesha, completed, rule) in listOf(
            Triple("वस्", "आव", SavabhyamVamauSutra),
            Triple("मस्", "आम", SavabhyamVamauSutra),
            Triple("तस्", "ताम्", LotolangvatSutra),
            Triple("थस्", "तम्", LotolangvatSutra),
            Triple("थ", "त", LotolangvatSutra),
        )) {
            val initial = state(upadesha, upadesha, Lakara.LOT)
            assertTrue(rule.matches(initial))
            val result = rule.apply(initial).state
            assertEquals(completed, result.terms.last().surface)
            assertEquals(initial.terms.first(), result.terms.first())
            assertEquals(initial.terms.last().id, result.terms.last().id)
            assertEquals(initial.terms.last().upadesha, result.terms.last().upadesha)
            val annotated = result.replaceTerm("ending", result.terms.last().copy(surface = completed + "ऽ"))
            assertFalse(rule.matches(annotated))
            assertTrue(rule.matches(annotated.copy(stage = DerivationStage.IT_PROCESSED)))
            assertFalse(rule.matches(annotated.copy(stage = DerivationStage.INITIAL)))
        }
        val middle = state("सेँऽ", "थास्", Lakara.LOT)
        assertTrue(SavabhyamVamauSutra.matches(middle))
        assertEquals("स्व", SavabhyamVamauSutra.apply(middle).state.terms.last().surface)
        assertFalse(SavabhyamVamauSutra.matches(state("सै", "थास्", Lakara.LOT)))
    }

    @Test
    fun `lit completion checks ignore vowel annotations`() {
        for ((upadesha, completed) in listOf("त" to "ए", "झ" to "इरे")) {
            val original = state(upadesha, upadesha, Lakara.LIT)
            assertTrue(LitasTajhayorEshirecSutra.matches(original))
            assertEquals(completed, LitasTajhayorEshirecSutra.apply(original).state.terms.last().surface)
            assertFalse(LitasTajhayorEshirecSutra.matches(state(completed + "ँऽ", upadesha, Lakara.LIT)))
        }
    }

    @Test
    fun `er uh identifies completed tu without changing its stage gates`() {
        val complete = state("तुँऽ", "तिप्", Lakara.LOT)
        assertFalse(ErUhSutra.matches(complete))
        assertTrue(ErUhSutra.matches(complete.copy(stage = DerivationStage.IT_PROCESSED)))
        assertFalse(ErUhSutra.matches(complete.copy(stage = DerivationStage.INITIAL)))
        assertTrue(ErUhSutra.matches(state("तू", "तिप्", Lakara.LOT)))
    }

    @Test
    fun `ata ai preserves vowel annotations and orthographic signs`() {
        for ((source, upadesha, expected) in listOf(
            Triple("आँतेँऽ", TingAffix.ATAM.upadesha, "ऐँतेँऽ"),
            Triple("आँथेँऽ", TingAffix.ATHAM.upadesha, "ऐँथेँऽ"),
        )) {
            val original = state(source, upadesha, Lakara.LET)
            assertTrue(AtaAiSutra.matches(original))
            val result = AtaAiSutra.apply(original).state
            assertEquals(expected, result.terms.last().surface)
            assertEquals(original.terms.last().id, result.terms.last().id)
            assertEquals(original.terms.last().upadesha, result.terms.last().upadesha)
            assertEquals(original.terms.first(), result.terms.first())
            assertFalse(AtaAiSutra.matches(result))
        }
        assertFalse(AtaAiSutra.matches(state("आती", TingAffix.ATAM.upadesha, Lakara.LET)))
    }

    @Test
    fun `jus substitution renders an independent vowel at its term boundary`() {
        val original = state("झि", "झि", Lakara.LING)
        val result = JherJusSutra.apply(original).state
        assertEquals("उस्", result.terms.last().surface)
        assertEquals(original.terms.last().id, result.terms.last().id)
        assertEquals(original.terms.last().upadesha, result.terms.last().upadesha)
        assertEquals(original.terms.first(), result.terms.first())
        assertFalse(JherJusSutra.matches(result))
        val consonantFinal = result.replaceTerm("root", original.terms.first().copy(surface = "य्"))
        assertEquals("युस्", consonantFinal.surface)
    }

    @Test
    fun `mer nih preserves its distinct stage gates`() {
        val complete = state("आनिँऽ", "मिप्", Lakara.LOT)
        assertFalse(MerNihSutra.matches(complete))
        assertTrue(MerNihSutra.matches(complete.copy(stage = DerivationStage.IT_PROCESSED)))
        assertFalse(MerNihSutra.matches(complete.copy(stage = DerivationStage.INITIAL)))
        val initial = state("मि", "मिप्", Lakara.LOT)
        assertTrue(MerNihSutra.matches(initial))
        val result = MerNihSutra.apply(initial).state
        assertEquals("आनि", result.terms.last().surface)
        assertEquals(initial.terms.first(), result.terms.first())
        assertEquals(initial.terms.last().id, result.terms.last().id)
        assertEquals(initial.terms.last().upadesha, result.terms.last().upadesha)
        assertFalse(MerNihSutra.matches(result))
    }
}

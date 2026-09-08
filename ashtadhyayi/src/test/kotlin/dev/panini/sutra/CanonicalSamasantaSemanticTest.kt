package dev.panini.sutra

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.ashtadhyayi.adhyaya5.pada4.AcPratyanvavapurvatSamalomnahSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.BahuvrihauSankhyeyeDajabahuganatSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.NanjastatpurusatSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.PathoVibhasaSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.TatpurusasyangulehSankhyavyayadehSutra
import dev.panini.ashtadhyayi.adhyaya5.pada4.UpasargadAdhvanahSutra
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CanonicalSamasantaSemanticTest {
    private fun context(
        first: SamasaPada,
        last: SamasaPada,
        type: SamasaType,
        semantics: Set<SamasaSemanticRelation> = emptySet(),
    ) = SamasaRuleContext(listOf(first, last), type, semanticRelations = semantics, strictSemantics = true)

    @Test fun `5 4 71 prohibits samasanta only for nan tatpurusa`() {
        assertTrue(NanjastatpurusatSutra.matches(context(SamasaPada("न"), SamasaPada("राजन्"), SamasaType.NAN_TATPURUSA)))
        assertFalse(NanjastatpurusatSutra.matches(context(SamasaPada("न"), SamasaPada("राजन्"), SamasaType.BAHUVRIHI)))
    }

    @Test fun `5 4 72 restores optional pathin branch`() {
        val licensed = context(SamasaPada("न"), SamasaPada("पथिन्"), SamasaType.NAN_TATPURUSA)
        assertTrue(PathoVibhasaSutra.matches(licensed))
        assertEquals("अपथ", (PathoVibhasaSutra.apply(licensed) as SamasaRuleResult.Formed).compoundStem)
        assertFalse(PathoVibhasaSutra.matches(context(SamasaPada("न"), SamasaPada("राजन्"), SamasaType.NAN_TATPURUSA)))
    }

    @Test fun `5 4 73 requires numerical referent semantics`() {
        val plain = context(SamasaPada("त्रि"), SamasaPada("पाद"), SamasaType.BAHUVRIHI)
        assertFalse(BahuvrihauSankhyeyeDajabahuganatSutra.matches(plain))
        assertTrue(BahuvrihauSankhyeyeDajabahuganatSutra.matches(plain.copy(semanticRelations = setOf(SamasaSemanticRelation.NUMERICAL_REFERENT))))
    }

    @Test fun `5 4 75 is lexically bounded`() {
        assertTrue(AcPratyanvavapurvatSamalomnahSutra.matches(context(SamasaPada("प्रति"), SamasaPada("लोमन्"), SamasaType.TATPURUSA)))
        assertFalse(AcPratyanvavapurvatSamalomnahSutra.matches(context(SamasaPada("परि"), SamasaPada("लोमन्"), SamasaType.TATPURUSA)))
    }

    @Test fun `5 4 85 requires explicit upasarga metadata`() {
        val bare = context(SamasaPada("प्र"), SamasaPada("अध्वन्"), SamasaType.TATPURUSA)
        assertFalse(UpasargadAdhvanahSutra.matches(bare))
        assertTrue(UpasargadAdhvanahSutra.matches(bare.copy(padas = listOf(SamasaPada("प्र", samjnas = setOf(Samjna.UPASARGA)), SamasaPada("अध्वन्")))))
    }

    @Test fun `5 4 86 accepts numeral or explicit avyaya and rejects ordinary adjective`() {
        assertTrue(TatpurusasyangulehSankhyavyayadehSutra.matches(context(SamasaPada("द्वि"), SamasaPada("अङ्गुलि"), SamasaType.TATPURUSA)))
        assertTrue(TatpurusasyangulehSankhyavyayadehSutra.matches(context(SamasaPada("निर्", samjnas = setOf(Samjna.AVYAYA)), SamasaPada("अङ्गुलि"), SamasaType.TATPURUSA)))
        assertFalse(TatpurusasyangulehSankhyavyayadehSutra.matches(context(SamasaPada("दीर्घ"), SamasaPada("अङ्गुलि"), SamasaType.TATPURUSA)))
    }
}

package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.SupAffix
import dev.panini.derivation.*
import kotlin.test.*

class SupVowelCompositionTest {
    @Test fun `sup lengthening and e substitution compose annotated terms with one exact trace`() {
        for ((affix, expected, apply) in listOf(
            Triple(SupAffix.BHYAM_3, "रामाँऽभ्याँम्", SupiCaSutra::apply),
            Triple(SupAffix.BHYAS_4, "रामेँऽभ्यँस्", BahuvacaneJhalyetSutra::apply),
        )) {
            val stem = DerivationTerm("stem", "रामँऽ", TermKind.PRATIPADIKA)
            val suffix = affix.term().copy(surface = if (affix == SupAffix.BHYAM_3) "भ्याँम्" else "भ्यँस्")
            val before = DerivationState(listOf(stem, suffix))
            val result = apply(before).state
            assertEquals(expected, result.surface)
            assertEquals(listOf(stem.id), result.terms.map { it.id })
            assertEquals(stem.upadesha, result.terms.single().upadesha)
            assertEquals(stem.varnas.lastIndex, result.substitutions.single().sourceVarnaIndex)
            assertEquals(suffix.id, result.droppedTerms.single().id)
            assertEquals(stem.id, result.droppedTerms.single().mergedIntoTermId)
            assertEquals(0, result.droppedTerms.single().mergedAffixVowelFromEnd)
            assertEquals(setOf(suffix.upadesha), result.terms.single().sourceSuffixUpadeshas)
        }
    }
}

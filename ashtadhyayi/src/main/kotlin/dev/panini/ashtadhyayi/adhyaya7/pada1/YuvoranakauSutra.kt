package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.ashtadhyayi.adhyaya1.pada3.YathasamkhyamSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.derivation.ItProcessingPhase
import dev.panini.core.YuvAffixConvention
import dev.panini.shiksha.*
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.1: yuvoranākau.
 * Replaces the elements 'yu' and 'vu' of an affix with 'ana' and 'aka' respectively.
 * Uses 1.3.10 (Yathāsaṃkhyam) logic.
 */
object YuvoranakauSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.1",
    text = "युवोरनाकौ",
    hindiExplanation = "प्रत्यय के अवयव 'यु' और 'वु' के स्थान पर क्रमशः 'अन' और 'अक' आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710001,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
    dependencies = setOf("1.3.10")
), DerivationSutra {
    private fun target(context: DerivationState): DerivationTerm? = context.terms.firstOrNull {
        it.kind == TermKind.PRATYAYA && it.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            it.itDesignations.isEmpty() && it.deferredItDesignations.isEmpty() &&
            when (it.yuvAffixConvention) {
                YuvAffixConvention.YU -> it.varnas == sources[0]
                YuvAffixConvention.VU -> it.varnas == sources[1]
                null -> false
            }
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val target = requireNotNull(target(context))
        val replacement = requireNotNull(YathasamkhyamSutra.map(target.varnas, sources, targets))

        return DerivationChange(
            state = context.replaceTermVarnaRange(target.id, 0, target.varnas.size, replacement, emptyMap(), sutra),
            explanation = "7.1.1 substitutes ana/aka for the conventionally nasalized yaṇ affix (Yuvoranākau)."
        )
    }

    private val sources: List<List<Varna>> = listOf(listOf(Vyanjana.YA, Svara.U), listOf(Vyanjana.VA, Svara.U))
    private val targets: List<List<Varna>> = listOf(listOf(Svara.A, Vyanjana.NA, Svara.A), listOf(Svara.A, Vyanjana.KA, Svara.A))
}

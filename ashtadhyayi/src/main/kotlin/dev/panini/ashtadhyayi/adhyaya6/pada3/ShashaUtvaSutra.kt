package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Vārttika on 6.3.109 (पृषोदरादीनि यथोपदिष्टम्): षष उत्वं दतृदशधासूत्तरपदादेः ष्टुत्वं च।
 * Before the uttarapada दश, षष् takes उ in place of its final, and the uttarapada's initial द takes
 * ṣṭutva (ड): षष् + दश → षउ + डश, which 6.1.87 joins as षोडश.
 *
 * Only the numeral uttarapada दश is modelled here. The vārttika's दत् and तृ, and धा with its optional
 * alternative (षोढा, षड्धा), are not yet implemented.
 */
object ShashaUtvaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.109-v1",
    text = "षष उत्वं दतृदशधासूत्तरपदादेः ष्टुत्वं च",
    hindiExplanation = "दश उत्तरपद होने पर षष् के अन्त्य को उ और उत्तरपद के आदि दकार को ष्टुत्व (ड) होता है (वार्त्तिक)।",
    type = SutraType.APAVADA,
    chapter = 6,
    pada = 3,
    optional = false,
    kramaValue = 630109,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
), DerivationSutra {
    private val shash = listOf(Vyanjana.SSA, Svara.A, Vyanjana.SSA)
    private val dasha = listOf(Vyanjana.DA, Svara.A, Vyanjana.SHA, Svara.A)

    override fun matches(context: DerivationState): Boolean {
        val purvapada = context.terms.getOrNull(0) ?: return false
        val uttarapada = context.terms.getOrNull(1) ?: return false
        return purvapada.varnas == shash && uttarapada.varnas == dasha &&
            context.samjnas.any { it.targetId == uttarapada.id && it.samjna == Samjna.SANKHYA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val purvapada = context.terms[0]
        val uttarapada = context.terms[1]
        // ड first, then उ: the उ is the latest substitution, which 6.1.87 joins with the preceding अ (षो).
        val withDda = context.replaceTermVarna(uttarapada.id, 0, listOf(Vyanjana.DDA), sutra)
        val withU = withDda.replaceTermVarna(purvapada.id, purvapada.varnas.lastIndex, listOf(Svara.U), sutra)
        return DerivationChange(withU, "$text: षष् + दश → षउ + डश")
    }
}

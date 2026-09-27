package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.4.101: tasasthāmipāṃ tāntantāmāḥ.
 * In a Nit lakāra, the endings tas, thas, tha, and mip are replaced by tām, tam, ta, and am.
 */
object TasasthamipamTantantamahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.101",
    text = "तस्थस्थमिपां तान्तन्तामः",
    hindiExplanation = "ङित् लकार के परस्मैपद प्रत्ययों तस्, थस्, th और मिप् के स्थान पर क्रमशः ताम्, तम्, त और अम् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340101,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("7.2.80"),
), DerivationSutra {
    private val eligibleEndings = setOf(TingAffix.TAS, TingAffix.THAS, TingAffix.THA, TingAffix.MIP)

    override fun matches(context: DerivationState): Boolean {
        if (context.stage == DerivationStage.INITIAL || context.stage == DerivationStage.PRATYAYA_SELECTED) return false
        val lastTerm = context.terms.lastOrNull() ?: return false

        val isNit = context.effectiveContext.rupa.lakara in setOf(
            Lakara.LANG, Lakara.LRNG, Lakara.LUNG, Lakara.LING,
        )
        val target = eligibleEndings.singleOrNull { lastTerm.matchesUpadesha(it.upadesha) }
        val eligible = target != null
        val substitutionRecorded = context.substitutions.any { it.sutra == sutra && it.targetId == lastTerm.id }

        val isAlreadyApplied = target?.let { lastTerm.varnas == replacements[it] } == true

        return isNit && eligible && !isAlreadyApplied && !substitutionRecorded
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val affix = requireNotNull(eligibleEndings.singleOrNull { lastTerm.matchesUpadesha(it.upadesha) })
        val substitute = requireNotNull(replacements[affix])
        return DerivationChange(
            state = context.replaceWholeAffix(lastTerm.id, substitute.toDevanagari(), sutra, dev.panini.derivation.WholeAffixDesignationPolicy.Consume)
                .addSubstitution(VarnaSubstitution(lastTerm.id, lastTerm.varnas.first().devanagari.single(), substitute.toDevanagari(), sutra))
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "3.4.101: Replaced ending ${lastTerm.upadesha} with ${substitute.toDevanagari()}."
        )
    }

    private val replacements: Map<TingAffix, List<Varna>> = mapOf(
        TingAffix.TAS to listOf(Vyanjana.TA, Svara.AA, Vyanjana.MA),
        TingAffix.THAS to listOf(Vyanjana.TA, Svara.A, Vyanjana.MA),
        TingAffix.THA to listOf(Vyanjana.TA, Svara.A),
        TingAffix.MIP to listOf(Svara.A, Vyanjana.MA),
    )
}

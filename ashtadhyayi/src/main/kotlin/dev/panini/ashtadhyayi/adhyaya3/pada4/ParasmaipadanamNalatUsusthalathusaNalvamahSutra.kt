package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.hasCurrentAffix
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 3.4.82: परस्मैपदानां णलतुसुस्थलथुसणल्वमाः. */
object ParasmaipadanamNalatUsusthalathusaNalvamahSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.82", text = "परस्मैपदानां णलतुसुस्थलथुसणल्वमाः",
    hindiExplanation = "लिट् में परस्मैपद के नौ तिङ्-प्रत्ययों के स्थान पर णल् आदि नौ आदेश होते हैं।",
    type = SutraType.NITYA, chapter = 3, pada = 4, optional = false, kramaValue = 340082,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.PRATYAYA,
), DerivationSutra {
    private data class Replacement(
        val upadesha: String,
        val varnas: List<Varna>,
        val policy: WholeAffixDesignationPolicy = WholeAffixDesignationPolicy.Consume,
    )

    private val replacements = mapOf(
        TingAffix.TIP to Replacement("णल्", listOf(Vyanjana.NNA, Svara.A, Vyanjana.LA), WholeAffixDesignationPolicy.FreshUpadesha),
        TingAffix.TAS to Replacement("अतुस्", listOf(Svara.A, Vyanjana.TA, Svara.U, Vyanjana.SA)),
        TingAffix.JHI to Replacement("उस्", listOf(Svara.U, Vyanjana.SA)),
        TingAffix.SIP to Replacement("थल्", listOf(Vyanjana.THA, Svara.A, Vyanjana.LA), WholeAffixDesignationPolicy.FreshUpadesha),
        TingAffix.THAS to Replacement("अथुस्", listOf(Svara.A, Vyanjana.THA, Svara.U, Vyanjana.SA)),
        TingAffix.THA to Replacement("अ", listOf(Svara.A)),
        TingAffix.MIP to Replacement("अ", listOf(Svara.A)),
        TingAffix.VAS to Replacement("व", listOf(Vyanjana.VA, Svara.A)),
        TingAffix.MAS to Replacement("म", listOf(Vyanjana.MA, Svara.A)),
    )

    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.last()
        val replacement = replacements.entries.singleOrNull { ending.hasCurrentAffix(it.key) }?.value ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LIT &&
            ending.varnas != replacement.varnas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        val replacement = replacements.entries.single { ending.hasCurrentAffix(it.key) }.value
        return DerivationChange(context.replaceWholeAffix(ending.id, replacement.varnas, sutra, replacement.policy, upadesha = replacement.upadesha),
            "3.4.82 replaces the Parasmaipada ${ending.upadesha} ending with ${replacement.upadesha} in लिट्.")
    }
}

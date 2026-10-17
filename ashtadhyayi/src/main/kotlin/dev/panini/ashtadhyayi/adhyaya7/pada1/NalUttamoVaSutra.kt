package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.core.ItMarker
import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import dev.panini.derivation.*
import dev.panini.sutra.*

/** Nyāsa: the optionality concerns ṇit-work; the actual ṇ-it designation is not denied. */
object NalUttamoVaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.91", text = "णलुत्तमो वा",
    hindiExplanation = "उत्तमपुरुष के णल् में णित्कार्य विकल्प से होता है; इत् का ऐतिहासिक प्रमाण बना रहता है।",
    type = SutraType.VIBHASHA, chapter = 7, pada = 1, optional = false,
    kramaValue = 710091, role = SutraRole.Apavada, action = SutraAction.ATIDESHA,
    scope = SutraScope.PRATYAYA, nimittaScope = NimittaScope.EXTERNAL,
    blocks = setOf("7.2.115", "7.2.116", "7.3.84"),
), DerivationSutra {
    override val hasAlternatives = true
    private fun target(state: DerivationState) = state.terms.lastOrNull()?.takeIf {
        state.effectiveContext.rupa.lakara == Lakara.LIT &&
            it.sourceTingAffix == TingAffix.MIP && it.upadesha == "णल्" &&
            ItMarker.NIT !in it.itMarkerEffects &&
            (it.itMarkerProvenance.any { proof -> proof.marker == ItMarker.NIT } ||
                (it.itDesignations + it.deferredItDesignations).any { proof -> proof.marker == ItMarker.NIT })
    }
    override fun matches(context: DerivationState) = target(context) != null
    private fun choose(state: DerivationState, enabled: Boolean): DerivationChange {
        val suffix = requireNotNull(target(state))
        return DerivationChange(state.replaceTerm(suffix.id, suffix.copy(
            itMarkerEffects = suffix.itMarkerEffects + (ItMarker.NIT to ItMarkerEffectDecision(enabled, sutra)),
            establishedBySutras = suffix.establishedBySutras + sutra)),
            "7.1.91 ${if (enabled) "enables" else "declines"} nit-work while retaining the actual it designation.")
    }
    override fun apply(context: DerivationState) = choose(context, false)
    override fun applyAll(state: DerivationState) = listOf(choose(state, false), choose(state, true))
}

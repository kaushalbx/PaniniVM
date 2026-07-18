package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TingAffix
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

import dev.panini.derivation.Lakara

object TiptasjhiSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.78",
    text = "तिप्तस्झि",
    hindiExplanation = "लट् के स्थान पर पुरुष-वचनानुसार तिङ् प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340078,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val lastTerm = context.terms.lastOrNull() ?: return false
        val upadeshaOrSurface = lastTerm.upadesha ?: lastTerm.surface
        return lastTerm.kind == dev.panini.derivation.TermKind.PRATYAYA &&
            upadeshaOrSurface in Lakara.entries.map { it.upadesha } &&
            context.effectiveContext.rupa.let { it.purusha != null && it.vacana != null }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val dhatuTerm = context.terms.firstOrNull { it.kind == dev.panini.derivation.TermKind.DHATU }
        val lookupName = dhatuTerm?.upadesha ?: dhatuTerm?.surface ?: ""
        val dhatus = dev.panini.dhatupatha.DhatuPatha.all.filter { it.upadesha == lookupName || it.derivationalSurface == lookupName }
        val pada = dhatus.firstOrNull()?.pada ?: dev.panini.dhatupatha.PadaType.PARASMAIPADA
        val targetPada = context.effectiveContext.rupa.pada ?: when (pada) {
            dev.panini.dhatupatha.PadaType.PARASMAIPADA -> dev.panini.dhatupatha.PadaType.PARASMAIPADA
            dev.panini.dhatupatha.PadaType.ATMANEPADA -> dev.panini.dhatupatha.PadaType.ATMANEPADA
            dev.panini.dhatupatha.PadaType.UBHAYAPADA -> dev.panini.dhatupatha.PadaType.PARASMAIPADA
        }

        val baseTerm = context.effectiveContext.rupa.let { morphology ->
            requireNotNull(TingAffix.select(requireNotNull(morphology.purusha), requireNotNull(morphology.vacana), targetPada)).term()
        }
        val replacementTerm = baseTerm.copy(
            sthaniProps = dev.panini.derivation.SthaniProperties(
                upadesha = lastTerm.upadesha ?: lastTerm.surface,
                itMarkers = lastTerm.itMarkers
            )
        )

        return DerivationChange(
            context.replaceTerm(lastTerm.id, replacementTerm),
            "3.4.78 substitutes the requested tiṅ termination for ${lastTerm.surface}.",
        )
    }
}

package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TingAffix
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

import dev.sanskrit.derivation.Lakara

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
        return lastTerm.kind == dev.sanskrit.derivation.TermKind.PRATYAYA &&
            upadeshaOrSurface in Lakara.entries.map { it.upadesha } &&
            context.effectiveContext.rupa.let { it.purusha != null && it.vacana != null }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val dhatuTerm = context.terms.firstOrNull { it.kind == dev.sanskrit.derivation.TermKind.DHATU }
        val lookupName = dhatuTerm?.upadesha ?: dhatuTerm?.surface ?: ""
        val dhatus = dev.sanskrit.dhatupatha.DhatuPatha.all.filter { it.upadesha == lookupName || it.mula == lookupName }
        val pada = dhatus.firstOrNull()?.pada ?: dev.sanskrit.dhatupatha.PadaType.PARASMAIPADA
        val targetPada = when (pada) {
            dev.sanskrit.dhatupatha.PadaType.PARASMAIPADA -> dev.sanskrit.dhatupatha.PadaType.PARASMAIPADA
            dev.sanskrit.dhatupatha.PadaType.ATMANEPADA -> dev.sanskrit.dhatupatha.PadaType.ATMANEPADA
            dev.sanskrit.dhatupatha.PadaType.UBHAYAPADA -> dev.sanskrit.dhatupatha.PadaType.PARASMAIPADA
        }

        val baseTerm = context.effectiveContext.rupa.let { morphology ->
            requireNotNull(TingAffix.select(requireNotNull(morphology.purusha), requireNotNull(morphology.vacana), targetPada)).term()
        }
        val replacementTerm = baseTerm.copy(
            sthaniProps = dev.sanskrit.derivation.SthaniProperties(
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

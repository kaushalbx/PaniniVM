package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Feminine Ghi stem before singular ṅasi/ṅas: form the yāḥ ending. */
object GherNgasyosStriyamSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.128",
    text = "घेर्ङसिङसोः स्त्रियाम्",
    hindiExplanation = "स्त्रीलिङ्ग घि-अन्त में एकवचन ङसि और ङस् के परे याः/वाः रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730128,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("1.4.7"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.STRI ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA ||
            context.effectiveContext.rupa.vibhakti !in setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI)
        ) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI } &&
            affix.upadesha in setOf("ङसि", "ङस्") &&
            stem.varnas.lastOrNull() in setOf(Svara.I, Svara.E, Svara.U, Svara.O)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val source = stem.varnas.last() as Svara
        val ending: List<Varna> = when (source) {
            Svara.I, Svara.E -> listOf(Vyanjana.YA, Svara.AA, Ayogavaha.VISARGA)
            Svara.U, Svara.O -> listOf(Vyanjana.VA, Svara.AA, Ayogavaha.VISARGA)
            else -> error("GherNgasyosStriyamSutra matched a non-ik stem")
        }
        val surface = (stem.varnas.dropLast(1) + ending).toDevanagari()
        return DerivationChange(
            state = context.mergeTermsByVarnaSubstitution(
                stem.id, affix.id, surface, source, ending, sutra,
            ).copy(stage = DerivationStage.FINAL),
            explanation = "7.3.128: Formed the feminine Ghi singular ङसि/ङस् त्याः ending.",
        )
    }
}

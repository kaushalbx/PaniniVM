package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.core.Lakara
import dev.panini.derivation.TermKind
import dev.panini.core.PadaType
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.2.58: गमेरिट् परस्मैपदेषु. */
object GamerItParasmaipadesuSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.58",
    text = "गमेरिट् परस्मैपदेषु",
    hindiExplanation = "परस्मैपद में गम् धातु के बाद सकारादि आर्धधातुक प्रत्यय से पहले इट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720058,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    private val parasmaipadaEndings = setOf("तिप्", "तस्", "झि", "सिप्", "थस्", "थ", "मिप्", "वस्", "मस्")

    override fun matches(context: DerivationState): Boolean {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.matchesUpadesha("गमॢँ") }
        if (dhatuIndex < 0 || context.allEffectiveTerms.any { it.id == "gam-it-agama" || it.id == "it-agama" }) return false

        val hasFutureSya = context.effectiveContext.rupa.lakara in setOf(Lakara.LRT, Lakara.LRNG) &&
            context.terms.drop(dhatuIndex + 1).any { it.kind == TermKind.PRATYAYA && it.upadesha == "स्य" }
        val isParasmaipada = context.effectiveContext.rupa.pada == PadaType.PARASMAIPADA ||
            context.terms.lastOrNull()?.upadesha in parasmaipadaEndings
        return hasFutureSya && isParasmaipada
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.matchesUpadesha("गमॢँ") }
        val itAgama = DerivationTerm("gam-it-agama", "इ", TermKind.AGAMA, upadesha = "इट्")
        return DerivationChange(
            context.copy(
                terms = context.terms.take(dhatuIndex + 1) + itAgama + context.terms.drop(dhatuIndex + 1),
                stage = DerivationStage.IT_PROCESSED,
            ),
            "7.2.58 inserts इट् after गम् before an s-initial ārddhadhātuka affix in the Parasmaipada.",
        )
    }
}

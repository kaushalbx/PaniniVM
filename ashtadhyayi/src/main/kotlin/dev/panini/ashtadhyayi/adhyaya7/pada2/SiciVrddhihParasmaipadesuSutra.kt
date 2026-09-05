package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.PadaType
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.2.1: सिचि वृद्धिः परस्मैपदेषु. Applies Vṛddhi vowel grade to root vowels before सिच् in Parasmaipada. */
object SiciVrddhihParasmaipadesuSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.1",
    text = "सिचि वृद्धिः परस्मैपदेषु",
    hindiExplanation = "परस्मैपद सिच् परे होने पर इगन्त धातु के स्वर को वृद्धि होती है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720001,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    private val parasmaipadaEndings = setOf("तिप्", "तस्", "झि", "सिप्", "थस्", "थ", "मिप्", "वस्", "मस्")

    override fun matches(context: DerivationState): Boolean {
        val hasSic = context.terms.any { it.upadesha == "सिँच्" }
        if (!hasSic) return false
        val isParasmaipada = context.effectiveContext.rupa.pada == PadaType.PARASMAIPADA ||
            context.terms.lastOrNull()?.upadesha in parasmaipadaEndings
        if (!isParasmaipada) return false
        val stem = context.terms.firstOrNull { it.kind == TermKind.DHATU } ?: return false
        if (stem.surface in setOf("हार्", "नै", "कार", "जै")) return false
        return stem.surface in setOf("हृ", "हर्", "नी", "ने", "कृ", "कर", "जि", "जे") ||
            (stem.upadesha != null && setOf("हृ", "नी", "कृ", "जि").any { root -> stem.upadesha!!.startsWith(root) })
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first { it.kind == TermKind.DHATU }
        val vrddhiSurface = when {
            stem.surface.startsWith("हृ") || stem.surface.startsWith("हर्") || stem.upadesha?.startsWith("हृ") == true -> "हार्"
            stem.surface.startsWith("नी") || stem.surface.startsWith("ने") || stem.upadesha?.startsWith("नी") == true -> "नै"
            stem.surface.startsWith("कृ") || stem.surface.startsWith("कर") || stem.upadesha?.startsWith("कृ") == true -> "कार"
            stem.surface.startsWith("जि") || stem.surface.startsWith("जे") || stem.upadesha?.startsWith("जि") == true -> "जै"
            else -> stem.surface
        }
        val updatedDhatu = stem.copy(surface = vrddhiSurface)
        return DerivationChange(
            context.replaceTerm(stem.id, updatedDhatu).copy(stage = DerivationStage.ANGAKARYA),
            "7.2.1 applies Vṛddhi to root vowel before सिच् in Parasmaipada.",
        )
    }
}

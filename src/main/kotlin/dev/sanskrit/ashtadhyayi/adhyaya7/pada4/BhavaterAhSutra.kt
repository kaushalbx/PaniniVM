package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 7.4.73: भवतेरः. */
object BhavaterAhSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.73", text = "भवतेरः",
    hindiExplanation = "लिट् में भू धातु के अभ्यास के अन्तिम उकार के स्थान पर अकार होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740073,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        val bhu = context.terms.any { it.kind == TermKind.DHATU && it.id != "abhyasa" && it.matchesUpadesha("भू") }
        return context.effectiveContext.rupa.lakara == Lakara.LIT && bhu &&
            context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            (abhyasa.surface.endsWith('उ') || abhyasa.surface.endsWith('ु'))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val substituted = abhyasa.surface
            .removeSuffix("उ").removeSuffix("ू").removeSuffix("ु")
        return DerivationChange(
            context.replaceTerm(abhyasa.id, abhyasa.copy(surface = substituted)),
            "7.4.73 replaces the final उ of the भू abhyāsa ${abhyasa.surface} with inherent अ in लिट्.",
        )
    }
}

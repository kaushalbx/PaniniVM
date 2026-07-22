package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.core.Lakara
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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

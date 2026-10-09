package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.4.60: हलादिः शेषः. */
object HaladisSeshahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.60", text = "हलादिः शेषः",
    hindiExplanation = "अभ्यास में केवल आरम्भ का हल् रहता है; बाद के हलों का लोप होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740060,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            shortenedAbhyasa(abhyasa.varnas) != abhyasa.varnas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val removed = abhyasa.varnas.indices.filter { it > 0 && abhyasa.varnas[it] is Vyanjana }.toSet()
        return DerivationChange(
            context.deleteTermVarnaOccurrences(abhyasa.id, removed, sutra),
            "7.4.60 retains only the initial consonant of the abhyāsa ${abhyasa.surface}.",
        )
    }

    private fun shortenedAbhyasa(varnas: List<Varna>) =
        if (varnas.firstOrNull() is Vyanjana) {
            varnas.filterIndexed { index, varna -> index == 0 || varna !is Vyanjana }
        } else {
            varnas.filterNot { it is Vyanjana }
        }
}

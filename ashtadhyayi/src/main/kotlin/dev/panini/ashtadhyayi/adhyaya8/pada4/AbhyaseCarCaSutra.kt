package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.replaceVarna
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.54: abhyāse car ca.
 * A jhal consonant in an abhyāsa receives its nearest car or jaś substitute.
 */
object AbhyaseCarCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.54",
    text = "अभ्यासे चर्च",
    hindiExplanation = "अभ्यास में झल् वर्णों के स्थान पर चर् और जश् आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840054,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    private val carOrJash = mapOf(
        Vyanjana.KA to Vyanjana.KA, Vyanjana.KHA to Vyanjana.KA, Vyanjana.GA to Vyanjana.GA, Vyanjana.GHA to Vyanjana.GA,
        Vyanjana.CA to Vyanjana.CA, Vyanjana.CHA to Vyanjana.CA, Vyanjana.JA to Vyanjana.JA, Vyanjana.JHA to Vyanjana.JA,
        Vyanjana.TTA to Vyanjana.TTA, Vyanjana.TTHA to Vyanjana.TTA, Vyanjana.DDA to Vyanjana.DDA, Vyanjana.DDHA to Vyanjana.DDA,
        Vyanjana.TA to Vyanjana.TA, Vyanjana.THA to Vyanjana.TA, Vyanjana.DA to Vyanjana.DA, Vyanjana.DHA to Vyanjana.DA,
        Vyanjana.PA to Vyanjana.PA, Vyanjana.PHA to Vyanjana.PA, Vyanjana.BA to Vyanjana.BA, Vyanjana.BHA to Vyanjana.BA,
    )

    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        val initial = abhyasa.varnas.firstOrNull() as? Vyanjana ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            initial in carOrJash && carOrJash.getValue(initial) != initial
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val source = abhyasa.varnas.first() as Vyanjana
        val substitute = carOrJash.getValue(source)
        val newSurface = abhyasa.varnas.replaceVarna(0, listOf(substitute)).toDevanagari()
        return DerivationChange(
            state = context.substituteTermSurface(abhyasa.id, newSurface, source, listOf(substitute), sutra),
            explanation = "8.4.54 changes $source to its nearest $substitute substitute in the abhyāsa.",
        )
    }
}

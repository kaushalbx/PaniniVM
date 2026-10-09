package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.4.62: कुहोश्चुः. */
object KuhohCuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.62", text = "कुहोश्चुः",
    hindiExplanation = "अभ्यास के आरम्भ में कवर्ग और हकार के स्थान पर चवर्ग का आदेश होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740062,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    private val cuhSubstitutions = mapOf(
        Vyanjana.KA to Vyanjana.CA,
        Vyanjana.KHA to Vyanjana.CHA,
        Vyanjana.GA to Vyanjana.JA,
        Vyanjana.GHA to Vyanjana.JHA,
        Vyanjana.NGA to Vyanjana.NYA,
        Vyanjana.HA to Vyanjana.JA,
    )

    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            abhyasa.varnas.firstOrNull() in cuhSubstitutions
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val source = abhyasa.varnas.first() as Vyanjana
        val replacement = cuhSubstitutions.getValue(source)
        return DerivationChange(
            context.replaceTermVarna(abhyasa.id, 0, listOf(replacement), sutra),
            "7.4.62 changes initial $source of the abhyāsa to $replacement.",
        )
    }
}

package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.HasMorphosyntax
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.1.24: ato'm. 
 * In neuter (napuṃsaka), after an a-ending stem, the affixes 'su' and 'am' 
 * are replaced by 'am'.
 */
object AtomSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.24",
    text = "अतोऽम्",
    hindiExplanation = "अकारान्त नपुंसक अङ्ग के बाद 'सु' और 'अम्' के स्थान पर 'अम्' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710024,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (!HasMorphosyntax(linga = Linga.NAPUMSAKA).matches(context)) return false
        if ("6.4.1" !in context.activeAdhikaras) return false

        val stem = context.terms.getOrNull(context.terms.size - 2) ?: return false
        val affix = context.terms.last()

        // A consonant without a virāma carries the inherent a; e.g. फल is a-final.
        val endsInA = dev.sanskrit.shiksha.Varnamala.endsWithA(stem.surface)
        return endsInA &&
            (affix.upadesha == "सुँ" || affix.upadesha == "अम्") &&
            affix.surface != "म्"
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            // The aṅga already supplies अ; the visible remainder of अम् is म्.
            state = context.replaceTerm(
                affix.id,
                affix.copy(surface = "म्", upadesha = "अम्", itMarkers = emptySet()),
            ),
            explanation = "7.1.24: Substituted the visible remainder म् of अम् for neuter सु/अम् after an a-stem."
        )
    }
}

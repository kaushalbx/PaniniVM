package dev.panini.ashtadhyayi.adhyaya7.pada3

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

/** 7.3.77: इषुगमियमां छः. Replaces the final of इष्, गम्, and यम् with छ् before a śit affix. */
object IshugamiyamamChahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.77",
    text = "इषुगमियमां छः",
    hindiExplanation = "शित् प्रत्यय परे होने पर इष्, गम् और यम् के अन्त्य वर्ण के स्थान पर छ् होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730077,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val dhatuIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (dhatuIndex < 0) return false
        val dhatu = context.terms[dhatuIndex]
        val eligible = dhatu.matchesUpadesha("इषुँ") ||
            dhatu.matchesUpadesha("गमॢँ") ||
            dhatu.matchesUpadesha("यमँ")
        val following = context.terms.getOrNull(dhatuIndex + 1)
        return eligible && dhatu.surface.endsWith('्') &&
            following?.upadesha in setOf("शप्", "श") &&
            context.substitutions.none { it.sutra == sutra && it.targetId == dhatu.id }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val dhatu = context.terms.first { it.kind == TermKind.DHATU }
        val source = dhatu.surface[dhatu.surface.lastIndex - 1]
        val replacement = "छ"
        val surface = dhatu.surface.dropLast(2) + replacement + "्"
        return DerivationChange(
            state = context.substituteTermSurface(dhatu.id, surface, source, replacement, sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.3.77 replaces the final of ${dhatu.surface} with छ् before the śit stem-forming affix.",
        )
    }
}

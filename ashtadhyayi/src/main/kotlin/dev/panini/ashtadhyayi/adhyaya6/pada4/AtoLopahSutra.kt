package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.core.SanadiAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationalEnvironment
import dev.panini.derivation.HasDerivationalEnvironment
import dev.panini.derivation.TermKind
import dev.panini.derivation.matchesAffix
import dev.panini.shiksha.Svara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.48 अतो लोपः. The final अ of an aṅga is deleted before an ārdhadhātuka affix: कथ + णिच् → कथ् + इ.
 * By 1.1.57 अचः परस्मिन् पूर्वविधौ the deleted अ stands in its place for a rule on what precedes it, so 7.2.116
 * finds no penultimate अ and the form is कथयति, कथनम्, not काथ-.
 */
object AtoLopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.48",
    text = "अतो लोपः",
    hindiExplanation = "आर्धधातुक प्रत्यय परे होने पर अदन्त अङ्ग के अन्त्य अकार का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640048,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DHATU,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
    dependencies = setOf("6.4.1"),
    // The lopa is nitya: it applies whether or not vṛddhi of the final vowel (7.2.115) has, so it takes precedence.
    blocks = setOf("7.2.115"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        if (stemIndex < 0 || context.terms[stemIndex].varnas.lastOrNull() != Svara.A) return false
        val affix = context.terms.drop(stemIndex + 1).firstOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        // णिच् is ārdhadhātuka by 3.4.114; any affix of an ārdhadhātuka derivation is too.
        return affix.matchesAffix(SanadiAffix.NIC) ||
            HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        return DerivationChange(
            state = context.replaceTermVarna(stem.id, stem.varnas.lastIndex, emptyList(), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.48: Deleted the final अ of ${stem.surface} before an ārdhadhātuka affix.",
        )
    }
}

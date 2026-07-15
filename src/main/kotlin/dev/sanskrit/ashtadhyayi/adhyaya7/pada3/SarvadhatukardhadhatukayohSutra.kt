package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.shiksha.ItStatus
import dev.sanskrit.derivation.DerivationalEnvironment
import dev.sanskrit.derivation.HasDerivationalEnvironment
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 7.3.84: sārvadhātukārdhadhātukayoḥ.
 * Substitutes guna for the final ik vowel of an anga before a sarvadhatuka or ardhadhatuka affix.
 * Now correctly checks for the 'Aṅgasya' jurisdiction (6.4.1).
 */
object SarvadhatukardhadhatukayohSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.84",
    text = "सार्वधातुकार्धधातुकयोः",
    hindiExplanation = "सार्वधातुक या आर्धधातुक प्रत्यय परे होने पर अङ्ग के अन्त्य इक् का गुण होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730084,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Jurisdictional check: Must be in the Aṅga section
        if ("6.4.1" !in context.activeAdhikaras) return false

        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        if (stemIndex < 0) return false
        val stem = context.terms[stemIndex]
        val affix = context.terms.getOrNull(stemIndex + 1) ?: return false
        if (affix.kind != TermKind.PRATYAYA) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LIT && affix.surface == affix.upadesha) return false
        if (context.effectiveContext.rupa.lakara == Lakara.LIT && stem.itStatus == ItStatus.SET) return false

        val isSarvaOrArdha = HasDerivationalEnvironment(DerivationalEnvironment.ARDHADHATUKA).matches(context) ||
            affix.id == "shap" || affix.id.startsWith("ting-")

        if (!isSarvaOrArdha) return false

        // Guna should not apply to the it-augment
        if (context.allEffectiveTerms.any { it.id == "it-agama" }) return false

        val lastChar = stem.surface.lastOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.IK, lastChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        val lastChar = stem.surface.last()
        val replacement = requireNotNull(Varnamala.getGuna(lastChar))
        val newSurface = stem.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA)
                .addSubstitution(VarnaSubstitution(stem.id, lastChar, replacement, sutra)),
            explanation = "7.3.84: Applied guna ($replacement) within Aṅgasya jurisdiction."
        )
    }
}

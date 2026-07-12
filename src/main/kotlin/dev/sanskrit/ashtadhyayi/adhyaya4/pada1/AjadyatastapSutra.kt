package dev.sanskrit.ashtadhyayi.adhyaya4.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.SemanticFeature
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 4.1.3: striyām.
 * Heading rule for rules 4.1.3 to 4.1.end. These suffixes are added in the feminine gender.
 */
object StriyamAdhikaraSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.3",
    text = "स्त्रियाम्",
    hindiExplanation = "यह एक अधिकार सूत्र है। यहाँ से समर्थानां प्रथमाद्वा (4.1.82) से पहले तक स्त्री-प्रत्ययों का अधिकार चलता है।",
    type = SutraType.ADHIKARA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410003,
    role = SutraRole.Adhikara,
    action = SutraAction.ADHIKARA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        SemanticFeature.STRI in context.semanticFeatures && "4.1.3" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.activateAdhikara("4.1.3"),
        explanation = "4.1.3 (Striyām) adhikāra activated."
    )
}

/**
 * 4.1.4: ajādyataṣṭāp.
 * The suffix 'ṭāp' is added to stems in the 'aja' group and to stems ending in short 'a' 
 * to express the feminine gender.
 */
object AjadyatastapSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.4",
    text = "अजाद्यतष्टाप्",
    hindiExplanation = "अज आदि गण में पठित शब्दों और अदन्त (अकारान्त) प्रातिपदिकों से स्त्रीत्व की विवक्षा में 'टाप्' प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410004,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("4.1.3")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("4.1.3" !in context.activeAdhikaras) return false
        if (context.terms.none { it.kind == TermKind.PRATIPADIKA }) return false
        
        val stem = context.terms.first { it.kind == TermKind.PRATIPADIKA }
        val endsInA = stem.surface.endsWith('अ')
        
        // Match if it ends in 'a' and no feminine suffix has been added yet
        return endsInA && context.terms.none { it.upadesha == "टाप्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val tap = DerivationTerm(
            id = "tap-suffix",
            surface = "टाप्",
            kind = TermKind.PRATYAYA,
            upadesha = "टाप्"
        )
        return DerivationChange(
            state = context.addTerm(tap).copy(stage = DerivationStage.PRATYAYA_SELECTED),
            explanation = "4.1.4 adds the feminine suffix 'ṭāp' to the a-stem."
        )
    }
}

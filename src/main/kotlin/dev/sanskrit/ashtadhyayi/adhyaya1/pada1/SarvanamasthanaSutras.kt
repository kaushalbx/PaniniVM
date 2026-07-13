package dev.sanskrit.ashtadhyayi.adhyaya1.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.SamjnaAssignment
import dev.sanskrit.derivation.SemanticFeature
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 1.1.42: śī sarvanāmasthānam.
 * In neuter, the substitute 'śī' (for jas/śas) is called 'sarvanāmasthāna'.
 */
object ShiSarvanamasthanamSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.42",
    text = "शि सर्वनामस्थानम्",
    hindiExplanation = "नपुंसकलिङ्ग में 'शि' (जस्/शस् के स्थान पर) की सर्वनामस्थान संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110042,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (SemanticFeature.NAPUMSAKA !in context.semanticFeatures) return false
        
        return context.terms.any { term ->
            term.upadesha == "शि" && 
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.SARVANAMASTHANA }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.upadesha == "शि" }
            .map { SamjnaAssignment(it.id, Samjna.SARVANAMASTHANA) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.42 assigns सर्वनामस्थान संज्ञा to 'śi' in neuter."
        )
    }
}

/**
 * 1.1.43: sud-anapuṃsakasya.
 * The first five case endings (su, au, jas, am, auṭ) are called 'sarvanāmasthāna' 
 * except in the neuter gender.
 */
object SudAnapumsakasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.43",
    text = "सुडनपुंसकस्य",
    hindiExplanation = "नपुंसकलिङ्ग को छोड़कर प्रथमा के तीन और द्वितीया के दो प्रत्ययों की सर्वनामस्थान संज्ञा होती है।",
    type = SutraType.SAMJNA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110043,
    role = SutraRole.Samjna,
    action = SutraAction.SAMJNA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (SemanticFeature.NAPUMSAKA in context.semanticFeatures) return false
        
        return context.terms.any { term ->
            term.upadesha in firstFive && 
            context.samjnas.none { it.targetId == term.id && it.samjna == Samjna.SARVANAMASTHANA }
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val assignments = context.terms
            .filter { it.upadesha in firstFive }
            .map { SamjnaAssignment(it.id, Samjna.SARVANAMASTHANA) }
            .toSet()

        return DerivationChange(
            state = context.withSamjnas(assignments),
            explanation = "1.1.43 assigns सर्वनामस्थान संज्ञा to the first five case endings."
        )
    }

    private val firstFive = setOf("सुँ", "औ", "जस्", "अम्", "औट्")
}

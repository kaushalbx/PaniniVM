package dev.sanskrit.ashtadhyayi.adhyaya7.pada3

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 7.3.108: hrasvasya guṇaḥ.
 * The short vowel of an aṅga gets guṇa substitution when followed by a sambuddhi (vocative singular) affix.
 */
object HrasvasyaGunaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.108",
    text = "ह्रस्वस्य गुणः",
    hindiExplanation = "सम्बुद्धि (सम्बोधन एकवचन) परे होने पर ह्रस्वान्त अङ्ग को गुण होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730108,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Affix must be Sambuddhi (Vocative Singular Su)
        // In our engine, Sambuddhi is marked by semantic features or upadesha.
        // For now, we check the upadesha "सुँ" and context likely having a feature.
        val isSambuddhi = context.samjnas.any { it.targetId == affix.id && it.samjna == Samjna.SAMBUDDHI }

        // 2. Stem must end in a short vowel
        val lastChar = stem.surface.lastOrNull() ?: return false
        val isShort = lastChar == 'इ' || lastChar == 'ि' || lastChar == 'उ' || lastChar == 'ु' || lastChar == 'ऋ' || lastChar == 'ृ'

        return isSambuddhi && isShort
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val lastChar = stem.surface.last()
        val replacement = requireNotNull(Varnamala.getGuna(lastChar))

        val newSurface = stem.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA)
                .addSubstitution(VarnaSubstitution(stem.id, lastChar, replacement, sutra)),
            explanation = "7.3.108: Applied guna ($replacement) to short final vowel before Sambuddhi."
        )
    }
}

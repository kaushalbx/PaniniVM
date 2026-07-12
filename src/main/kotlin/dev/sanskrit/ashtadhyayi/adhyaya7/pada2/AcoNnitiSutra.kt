package dev.sanskrit.ashtadhyayi.adhyaya7.pada2

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.ItMarker
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 7.2.115: aco ñṇiti.
 * The final vowel (ac) of an aṅga gets vṛddhi substitution when followed by 
 * a ñit (marked with ñ) or ṇit (marked with ṇ) affix.
 */
object AcoNnitiSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.115",
    text = "अचो ञ्णिति",
    hindiExplanation = "ञित् या णित् प्रत्यय परे होने पर अजन्त अङ्ग के अन्त्य स्वर को वृद्धि होती है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720115,
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

        val isNniti = affix.hasEffectiveMarker(ItMarker.NG) || 
                      affix.hasEffectiveMarker(ItMarker.NIT) || 
                      affix.upadesha?.endsWith("ण्") == true
        if (!isNniti) return false

        val lastChar = stem.surface.lastOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AC, lastChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val lastChar = stem.surface.last()
        val replacement = requireNotNull(Varnamala.getVrddhi(lastChar))

        val newSurface = stem.surface.dropLast(1) + replacement

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA)
                .addSubstitution(VarnaSubstitution(stem.id, lastChar, replacement, sutra)),
            explanation = "7.2.115: Applied vṛddhi ($replacement) before ñit/ṇit affix."
        )
    }
}

package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
    dependencies = setOf("6.4.1"),
    blocks = setOf("7.3.84"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU && it.id != "abhyasa" }
        if (stemIndex < 0) return false
        val stem = context.terms[stemIndex]
        val affix = context.terms.getOrNull(stemIndex + 1) ?: return false
        if (affix.kind != TermKind.PRATYAYA) return false
        if (affix.hasEffectiveMarker(ItMarker.KIT) || affix.hasEffectiveMarker(ItMarker.NGIT)) return false

        val isNniti = affix.hasEffectiveMarker(ItMarker.NG) ||
                      affix.hasEffectiveMarker(ItMarker.NIT) ||
                      affix.upadesha?.endsWith("ण्") == true
        if (!isNniti) return false

        val lastChar = stem.surface.lastOrNull() ?: return false
        val replacement = Varnamala.getVrddhi(lastChar) ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.AC, lastChar) && replacement != lastChar.toString()
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first { it.kind == TermKind.DHATU && it.id != "abhyasa" }
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

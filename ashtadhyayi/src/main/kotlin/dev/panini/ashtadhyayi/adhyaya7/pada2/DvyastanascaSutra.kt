package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.100: dvy-aṣṭanaś ca.
 * In feminine, before a case affix (vibhakti), 'dvi' and 'aṣṭan' take 'āp' (ā) augment/substitution (yielding dvā, aṣṭā).
 */
object DvyastanascaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.100",
    text = "द्व्यष्टनश्च",
    hindiExplanation = "स्त्रीलिङ्ग में विभक्तौ परे होने पर द्वि और अष्टन् अङ्गों के स्थान पर आप् (आकार) आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720100,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val linga = context.effectiveContext.rupa.linga
        if (linga != Linga.STRI) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isDviOrAstan = stem.upadesha in setOf("द्वि", "अष्टन्") || stem.surface in setOf("द्वि", "द्व", "अष्टन्", "अष्ट")
        if (!isDviOrAstan) return false
        if (stem.surface.endsWith('ा') || stem.surface.endsWith('आ')) return false

        return affix.id.startsWith("sup-")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val newSurface = when (stem.surface) {
            "द्वि", "द्व" -> "द्वा"
            "अष्टन्", "अष्ट" -> "अष्टा"
            else -> if (stem.surface.endsWith("्")) stem.surface.dropLast(1) + "ा" else stem.surface + "ा"
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.100: Added 'āp' (ā) to feminine stem '${stem.surface}' before vibhakti, yielding '$newSurface'."
        )
    }
}

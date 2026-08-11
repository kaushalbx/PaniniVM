package dev.panini.ashtadhyayi.adhyaya7.pada1

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
 * 7.1.98: catur-anaduhor ām.
 * Adds the augment 'ām' (ā) after the last vowel of 'catur' and 'anaḍuh' before a sarvanāmasthāna affix.
 */
object CaturanuduhorAmSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.98",
    text = "चतुरनडुहोराम्",
    hindiExplanation = "सर्वनामस्थाने विभक्तौ परे चतुर् और अनडुह् अङ्गों को आम् (आ) आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710098,
    role = SutraRole.Apavada,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1", "1.1.47")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isCaturOrAnaduh = stem.upadesha in setOf("चतुर्", "अनडुह्") || stem.surface in setOf("चतुर्", "अनडुह्")
        if (!isCaturOrAnaduh) return false

        if (stem.surface.contains("चत्वा") || stem.surface.contains("अनड्वा")) return false

        // Sarvanāmasthāna affixes for catur: jas (7.1.20 shi in neuter!), su, au, etc.
        val isSarvanamasthana = affix.id in setOf("sup-jas", "sup-su", "sup-au", "sup-aut", "sup-am") ||
            affix.upadesha in setOf("जस्", "सुँ", "औ", "औट्", "अम्", "शी", "शि") ||
            affix.surface in setOf("इ", "शी", "अस्", "जस्")

        return isSarvanamasthana
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val newSurface = when (stem.surface) {
            "चतुर्" -> "चत्वार्"
            "अनडुह्" -> "अनड्वाह्"
            else -> stem.surface.replace("तुर्", "त्वार्").replace("डुह्", "ड्वाह्")
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.1.98: Added 'ām' augment to ${stem.surface} before sarvanāmasthāna (becoming $newSurface)."
        )
    }
}

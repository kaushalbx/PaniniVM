package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.8: sarvanāmasthāne cāsambuddhau.
 */
object SarvanamasthaneCasambuddhauSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.8",
    text = "सर्वनामस्थाने चासम्बुद्धौ",
    hindiExplanation = "नकारान्त अङ्ग की उपधा को दीर्घ होता है सर्वनामस्थान परे होने पर (सम्बुद्धि को छोड़कर)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640008,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must end in 'n' (usually from 'num' augment)
        if (!stem.surface.endsWith("न्") && !stem.surface.endsWith("न")) return false

        // 2. Affix must be Sarvanāmasthāna
        val isSarvanamasthana = affix.upadesha == "शि" || affix.id in setOf("sup-su", "sup-au", "sup-jas", "sup-am", "sup-aut")

        // 3. The upadhā may be an explicit vowel or a consonant carrying an
        // inherent a (फलन्/फलन).
        val surface = stem.surface
        if (surface.length < 2) return false
        val penultimateChar = if (surface.endsWith("्")) {
             if (surface.length >= 3) surface[surface.length - 3] else return false
        } else {
             surface[surface.length - 2]
        }

        return penultimateChar != 'ा' && isSarvanamasthana &&
            (Varnamala.isVowel(penultimateChar) || Varnamala.isConsonant(penultimateChar))
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val surface = stem.surface

        val index = if (surface.endsWith("्")) surface.length - 3 else surface.length - 2
        val charToLengthen = surface[index]
        val lengthened = when (charToLengthen) {
            'अ' -> "ा"
            'इ', 'ि' -> "ी"
            'उ', 'ु' -> "ू"
            else -> "${charToLengthen}ा"
        }

        val newSurface = surface.substring(0, index) + lengthened + surface.substring(index + 1)

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.8: Lengthened the penultimate vowel of the 'n'-ending stem before Sarvanāmasthāna."
        )
    }
}

package dev.panini.ashtadhyayi.adhyaya6.pada4

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
 * 6.4.10: sānta-mahataḥ saṁyogasya.
 * Before non-vocative sarvanāmasthāna affixes, the penultimate vowel of s-ending conjunct stems ('vidvas') and 'mahat' is lengthened.
 */
object SantamahatahSamyogasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.10",
    text = "सान्तमहतः संयोगस्य",
    hindiExplanation = "असम्बुद्धौ सर्वनामस्थाने विभक्तौ परे सान्तसंयोग तथा महत् अङ्गस्य उपधायाः दीर्घः भवति।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640010,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isEligibleStem = stem.upadesha in setOf("महत्", "विद्वस्") || stem.surface in setOf("महत्", "विद्वस्", "महन्त्", "विद्वन्स्")
        if (!isEligibleStem) return false

        val isSarvanamasthana = affix.id in setOf("sup-su", "sup-au", "sup-jas", "sup-am", "sup-aut") ||
            affix.upadesha in setOf("सुँ", "औ", "जस्", "अम्", "औट्")
        return isSarvanamasthana
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val newSurface = when {
            stem.surface.contains("मह") -> if (affix.id == "sup-su" || affix.upadesha == "सुँ") "महान्" else "महान्त्"
            stem.surface.contains("विद्व") -> if (affix.id == "sup-su" || affix.upadesha == "सुँ") "विद्वान्" else "विद्वान्स्"
            else -> stem.surface
        }

        val newTerms = if (affix.id == "sup-su" || affix.upadesha == "सुँ") {
            context.terms.dropLast(1)
        } else {
            context.terms
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(terms = newTerms.dropLast(1) + stem.copy(surface = newSurface), stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.10: Lengthened penultimate vowel of stem '${stem.surface}' before sarvanāmasthāna (becoming $newSurface)."
        )
    }
}

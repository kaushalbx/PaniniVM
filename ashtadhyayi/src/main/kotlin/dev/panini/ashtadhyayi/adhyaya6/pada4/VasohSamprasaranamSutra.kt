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
 * 6.4.131: vasoḥ samprasāraṇam.
 * Before weak (bha) vowel affixes, the 'va' of a 'vasu'-ending stem ('vidvas') undergoes samprasāraṇa ('u'), yielding 'viduṣ-'.
 */
object VasohSamprasaranamSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.131",
    text = "वसोः सम्प्रसारणम्",
    hindiExplanation = "वसु-प्रत्ययान्त अङ्ग के वकार का सम्प्रसारण (उकार) होता है अच्-आदि भ-विभक्ति परे होने पर।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640131,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1", "1.4.18")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        if (stem.surface.contains("दुष्")) return false
        val isVasStem = stem.upadesha == "विद्वस्" || stem.surface.endsWith("वस्") || stem.surface == "विद्वस्"
        if (!isVasStem) return false

        val isBhaVowelAffix = affix.id in setOf(
            "sup-ta", "sup-nge", "sup-ngasi", "sup-ngas", "sup-os_6", "sup-os_7", "sup-am_6", "sup-ngi", "sup-sas"
        ) || affix.upadesha in setOf("शस्", "टा", "ङे", "ङसि", "ङस्", "ओस्", "आम्", "ङि")
        return isBhaVowelAffix
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val newSurface = stem.surface.replace("द्वस्", "दुष्")

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.131 & 8.3.59: Applied samprasāraṇa 'u' to 'vas' stem before weak vowel affix (becoming $newSurface)."
        )
    }
}

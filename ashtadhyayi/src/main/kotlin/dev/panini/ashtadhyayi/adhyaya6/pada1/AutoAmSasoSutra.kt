package dev.panini.ashtadhyayi.adhyaya6.pada1

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
 * 6.1.93: auto 'm-śasoḥ.
 * Before 'am' or 'śas', an o-ending stem ('go') merges with the affix into 'ām' (gām, gāḥ).
 */
object AutoAmSasoSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.93",
    text = "औतोऽम्शसोः",
    hindiExplanation = "ओकारान्त अङ्ग से उत्तर अम् और शस् के परे होने पर पूर्व-पर दोनों के स्थान पर आकार एकादेश होता है (गाम्, गाः)।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610093,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isOStem = stem.upadesha == "गो" || stem.surface.endsWith("ो") || stem.surface in setOf("गो", "गौ")
        if (!isOStem) return false

        val isAmOrSas = affix.id in setOf("sup-am", "sup-sas") || affix.upadesha in setOf("अम्", "शस्")
        return isAmOrSas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val newSurface = if (affix.id == "sup-am" || affix.upadesha == "अम्") "गाम्" else "गाः"
        val newTerms = context.terms.dropLast(2) + stem.copy(surface = newSurface)

        return DerivationChange(
            state = context.copy(terms = newTerms, stage = DerivationStage.ANGAKARYA)
                .copy(droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra)),
            explanation = "6.1.93: Merged o-stem with '${affix.surface}' into '$newSurface'."
        )
    }
}

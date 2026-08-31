package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.21: aṣṭābhya auś.
 * Replaces the plural case affixes 'jas' and 'śas' with 'auś' (au) after 'aṣṭan' / 'aṣṭā'.
 */
object AstabhyahAushSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.21",
    text = "अष्टाभ्य औश्",
    hindiExplanation = "अष्टाभ्यः अष्टन् शब्देभ्यः परयोः जश्शसोः औश् (औ) आदेशो भवति।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710021,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
    blocks = setOf("7.1.22"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (affix.upadesha == "औश्") return false

        val isAsta = stem.upadesha in setOf("अष्टन्", "अष्ट") || stem.surface in setOf("अष्टन्", "अष्टा", "अष्ट")
        if (!isAsta) return false

        val isJasOrSas = affix.id in setOf("sup-jas", "sup-sas") || affix.upadesha in setOf("जस्", "शस्")
        return isJasOrSas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            state = context.replaceWholeAffix(
                id = affix.id,
                replacementId = "aush-adesha",
                surface = "औश्",
                upadesha = "औश्",
                sutra = sutra,
                policy = dev.panini.derivation.WholeAffixDesignationPolicy.FreshUpadesha,
            ),
            explanation = "7.1.21: Replaced '${affix.surface}' (jas/śas) with 'auś' (au) after aṣṭan/aṣṭā."
        )
    }
}

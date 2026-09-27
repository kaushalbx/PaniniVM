package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
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

        // A later feminine substitution retains the lexical upadeśa, but it
        // is no longer the चतुर् aṅga to which this augment applies.
        if (stem.varnas !in eligibleStems) return false

        // Sarvanāmasthāna affixes for catur: jas (7.1.20 shi in neuter!), su, au, etc.
        val isSarvanamasthana = affix.id in setOf("sup-jas", "sup-su", "sup-au", "sup-aut", "sup-am") ||
            affix.upadesha in setOf("जस्", "सुँ", "औ", "औट्", "अम्", "शी", "शि") ||
            affix.varnas in sarvanamasthanaSurfaces

        return isSarvanamasthana
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val result = requireNotNull(replacements[stem.varnas])

        return DerivationChange(
            state = context.substituteTermVarnas(
                stem.id, result, Svara.U, listOf(Vyanjana.VA, Svara.AA), sutra,
            )
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.1.98: Added 'ām' augment before sarvanāmasthāna."
        )
    }

    private val catur: List<Varna> = listOf(Vyanjana.CA, Svara.A, Vyanjana.TA, Svara.U, Vyanjana.RA)
    private val anaduh: List<Varna> = listOf(Svara.A, Vyanjana.NA, Svara.A, Vyanjana.DDA, Svara.U, Vyanjana.HA)
    private val eligibleStems = setOf(catur, anaduh)
    private val replacements: Map<List<Varna>, List<Varna>> = mapOf(
        catur to listOf(Vyanjana.CA, Svara.A, Vyanjana.TA, Vyanjana.VA, Svara.AA, Vyanjana.RA),
        anaduh to listOf(Svara.A, Vyanjana.NA, Svara.A, Vyanjana.DDA, Vyanjana.VA, Svara.AA, Vyanjana.HA),
    )
    private val sarvanamasthanaSurfaces: Set<List<Varna>> = setOf(
        listOf(Svara.I), listOf(Vyanjana.SHA, Svara.II),
        listOf(Svara.A, Vyanjana.SA), listOf(Vyanjana.JA, Svara.A, Vyanjana.SA),
    )
}

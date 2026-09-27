package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
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

        val isEligibleStem = stem.upadesha in setOf("महत्", "विद्वस्") ||
            stem.compoundHeadUpadesha in setOf("महत्", "विद्वस्") ||
            eligibleCurrentForms.any { stem.varnas.takeLast(it.size) == it }
        if (!isEligibleStem) return false

        val isSarvanamasthana = affix.id in setOf("sup-su", "sup-au", "sup-jas", "sup-am", "sup-aut") ||
            affix.upadesha in setOf("सुँ", "औ", "जस्", "अम्", "औट्")
        return isSarvanamasthana
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isSu = affix.id == "sup-su" || affix.upadesha == "सुँ"
        val result = when {
            stem.varnas.takeLast(mahat.size) == mahat -> stem.varnas.dropLast(mahat.size) +
                if (isSu) mahan else mahant
            stem.varnas.takeLast(vidvas.size) == vidvas -> stem.varnas.dropLast(vidvas.size) +
                if (isSu) vidvan else vidvans
            else -> stem.varnas
        }

        var state = context.replaceWholeTermSurface(stem.id, result.toDevanagari(), sutra)
        if (isSu) {
            state = state.removeTerm(affix.id, sutra = sutra)
        }
        return DerivationChange(
            state = state.copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.10: Lengthened the penultimate vowel before sarvanāmasthāna."
        )
    }

    private val mahat: List<Varna> = listOf(Vyanjana.MA, Svara.A, Vyanjana.HA, Svara.A, Vyanjana.TA)
    private val mahan: List<Varna> = listOf(Vyanjana.MA, Svara.A, Vyanjana.HA, Svara.AA, Vyanjana.NA)
    private val mahant: List<Varna> = mahan + Vyanjana.TA
    private val vidvas: List<Varna> = listOf(Vyanjana.VA, Svara.I, Vyanjana.DA, Vyanjana.VA, Svara.A, Vyanjana.SA)
    private val vidvan: List<Varna> = vidvas.dropLast(2) + listOf(Svara.AA, Vyanjana.NA)
    private val vidvans: List<Varna> = vidvan + Vyanjana.SA
    private val eligibleCurrentForms = setOf(mahat, vidvas, mahant, vidvans)
}

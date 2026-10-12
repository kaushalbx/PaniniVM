package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.hasSarvanamasthanaSupIdentity
import dev.panini.derivation.matchesAffix
import dev.panini.core.SupAffix
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

        val isSarvanamasthana = affix.hasSarvanamasthanaSupIdentity()
        return isSarvanamasthana
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isSu = affix.sourceSupAffix == SupAffix.SU || affix.matchesAffix(SupAffix.SU)
        var state = when {
            stem.varnas.takeLast(mahat.size) == mahat -> context.replaceTermVarnaRange(
                stem.id, stem.varnas.size - 2, 2,
                if (isSu) listOf(Svara.AA, Vyanjana.NA) else listOf(Svara.AA, Vyanjana.NA, Vyanjana.TA),
                annotationSources = mapOf(0 to 0, (if (isSu) 1 else 2) to 1), sutra = sutra,
            )
            stem.varnas.takeLast(vidvas.size) == vidvas -> context.replaceTermVarnaRange(
                stem.id, stem.varnas.size - 2, 2,
                if (isSu) listOf(Svara.AA, Vyanjana.NA) else listOf(Svara.AA, Vyanjana.NA, Vyanjana.SA),
                annotationSources = mapOf(0 to 0, (if (isSu) 1 else 2) to 1), sutra = sutra,
            )
            else -> context
        }
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

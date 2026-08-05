package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

import dev.panini.shiksha.Samjna

/**
 * 5.4.18: द्वित्रिचतुर्भ्यः सुच्.
 * Prescribes special frequency suffix 'सुच्' after 'द्वि', 'त्रि', and 'चतुर्' stems yielding 'द्विः', 'त्रिः', 'चतुः' (and 'सकृत्' for 1).
 */
object DvitrichaturbhyahSucSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.4.18",
    text = "द्वित्रिचतुर्भ्यः सुच्",
    hindiExplanation = "द्वि, त्रि तथा चतुर् प्रातिपदिक से सुच् प्रत्यय होकर द्विः, त्रिः, चतुः (तथा सकृत्) रूप सिद्ध होता है।",
    type = SutraType.APAVADA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540018,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
    blocks = setOf("5.4.17"),
), DerivationSutra {

    private val supportedStems = setOf("एक", "द्वि", "त्रि", "चतुर्")

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.any { it.samjna == Samjna.PURANA || it.samjna == Samjna.DHATU }) return false
        if (context.terms.any { it.kind == TermKind.DHATU }) return false
        val hasTaddhitaRequest = context.samjnas.any { it.samjna == Samjna.TADDHITA }
        if (!hasTaddhitaRequest) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isAlreadyApplied = context.terms.any { it.upadesha == "सुच्" || it.surface in setOf("द्विः", "त्रिः", "चतुः", "सकृत्") }
        return !isAlreadyApplied && lastTerm.upadesha in supportedStems
    }

    override fun apply(context: DerivationState): DerivationChange {
        val lastTerm = context.terms.last()
        val surface = when (lastTerm.upadesha) {
            "एक" -> "सकृत्"
            "द्वि" -> "द्विः"
            "त्रि" -> "त्रिः"
            "चतुर्" -> "चतुः"
            else -> "द्विः"
        }
        val sucTerm = DerivationTerm(
            id = "taddhita_suc",
            surface = surface,
            kind = TermKind.PRATYAYA,
            upadesha = "सुच्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(terms = listOf(sucTerm)),
            explanation = "$text: derived frequency form '$surface' via सुच्"
        )
    }
}

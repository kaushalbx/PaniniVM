package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.core.FrequencyAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.derivation.rawTerm
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/** The frequency sense of 5.4.17 (क्रियाभ्यावृत्तिगणन), which 5.4.18 and 5.4.19 continue by anuvṛtti. */
internal fun DerivationState.requestsFrequency(): Boolean {
    if (samjnas.any { it.samjna == Samjna.PURANA || it.samjna == Samjna.DHATU }) return false
    if (terms.any { it.kind == TermKind.DHATU }) return false
    val requested = samjnas.map { it.samjna }.toSet()
    return Samjna.KRTVASUC in requested || Samjna.SUC in requested
}

internal fun DerivationState.hasSuc(): Boolean = terms.any { it.upadesha == FrequencyAffix.SUC.upadesha }

/** Adds the raw सुच्; इत् processing, 8.2.66, 8.2.23 and 8.3.15 then complete द्विः, चतुः, सकृत्. */
internal fun DerivationState.withSuc(sutra: String): DerivationState {
    val suc = FrequencyAffix.SUC.rawTerm("taddhita_suc", sutra)
    return copy(terms = terms + suc, samjnas = samjnas + SamjnaAssignment(suc.id, Samjna.TADDHITA))
}

/**
 * 5.4.18: द्वित्रिचतुर्भ्यः सुच्. An apavāda of 5.4.17 in its frequency sense: द्वि, त्रि and चतुर् take सुच्,
 * never कृत्वसुच्. The forms द्विः, त्रिः, चतुः are derived, not written in.
 */
object DvitrichaturbhyahSucSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.4.18",
    text = "द्वित्रिचतुर्भ्यः सुच्",
    hindiExplanation = "क्रियाभ्यावृत्तिगणन अर्थ में द्वि, त्रि तथा चतुर् से कृत्वसुच् के स्थान पर सुच् होता है (द्विः, त्रिः, चतुः)।",
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
    private val stems = setOf("द्वि", "त्रि", "चतुर्")

    override fun matches(context: DerivationState): Boolean =
        context.requestsFrequency() && !context.hasSuc() && context.terms.lastOrNull()?.upadesha in stems

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        state = context.withSuc(sutra),
        explanation = "$text: ${context.terms.last().upadesha} takes सुच् in place of कृत्वसुच्.",
    )
}

/**
 * 5.4.19: एकस्य सकृच्च. In the same frequency sense, एक is replaced by सकृत् and takes सुच् (च continues it):
 * सकृत् + स् → सकृत् by 8.2.23 संयोगान्तस्य लोपः.
 */
object EkasyaSakrcCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.4.19",
    text = "एकस्य सकृच्च",
    hindiExplanation = "क्रियाभ्यावृत्तिगणन अर्थ में एक के स्थान पर सकृत् आदेश तथा सुच् प्रत्यय होता है।",
    type = SutraType.APAVADA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540019,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
    blocks = setOf("5.4.17"),
), DerivationSutra {
    private val sakrt = "सकृत्".toVarnas()

    override fun matches(context: DerivationState): Boolean =
        context.requestsFrequency() && !context.hasSuc() && context.terms.lastOrNull()?.upadesha == "एक"

    override fun apply(context: DerivationState): DerivationChange {
        val eka = context.terms.last()
        val replaced = context.replaceTermVarnaRange(eka.id, 0, eka.varnas.size, sakrt, mapOf(), sutra)
        return DerivationChange(
            state = replaced.withSuc(sutra),
            explanation = "$text: एक → सकृत्, with सुच्.",
        )
    }
}

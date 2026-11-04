package dev.panini.ashtadhyayi.adhyaya2.pada3

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.analysis.KarakaEvidence
import dev.panini.analysis.VibhaktiRuleContext
import dev.panini.analysis.VibhaktiRuleResult
import dev.panini.analysis.SemanticRelation

/** Sūtra 2.3.50 षष्ठी शेषे. Assigns Ṣaṣṭhī for remaining non-kāraka relations (possession, relationship, etc.). */
object SasthiSeseSutra : Sutra<VibhaktiRuleContext, VibhaktiRuleResult>(
    number = "2.3.50", text = "षष्ठी शेषे",
    hindiExplanation = "कारक और प्रातिपदिकार्थ से अतिरिक्त स्वस्वामिभावादि सम्बन्ध शेष है; उसमें षष्ठी होती है।",
    type = SutraType.NITYA, chapter = 2, pada = 3, optional = false, kramaValue = 230050,
    role = SutraRole.Vidhi, action = SutraAction.VIDHI, scope = SutraScope.VAKYA,
    inputs = setOf(SutraInput.SEMANTIC_FEATURE),
    adhikara = setOf("2.3.1"),
) {
    override fun matches(context: VibhaktiRuleContext): Boolean =
        !context.abhihita &&
            context.participant?.governingAvyaya == null &&
            !dev.panini.analysis.ExclusionDirectionGovernance.hasConstruction(context) &&
            !TumarthacCaBhavavacanatSutra.hasPurposeActionNoun(context) &&
            !NamahSvastiSvahaSutra.hasConstruction(context) &&
            SemanticRelation.SESHA_RELATION in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.DIRECT_ADDRESS !in context.participant?.semanticRelations.orEmpty() &&
            SemanticRelation.ACTION_MARKING !in context.participant?.semanticRelations.orEmpty() &&
            !dev.panini.analysis.DistanceCaseConstructions.isReference(context) &&
            !dev.panini.analysis.DistanceCaseConstructions.isNonSubstanceWord(context) &&
            !TulyarthairAtulopamabhyamTrtiyanatarasyamSutra.hasSeshaComparison(context) &&
            !CaturthyAsisyAyusyamadraBhadraKusalaSukharthaHitaihSutra.hasSeshaBenediction(context) &&
            !JnyoAvidarthasyaKaraneSutra.hasSeshaInstrumentConstruction(context) &&
            !AdhigarthaDayesamKarmaniSutra.hasSeshaObjectConstruction(context) &&
            !KrjahPratiyatneSutra.hasSeshaPratiyatnaObject(context) &&
            !RujarthanamBhavavacananamAjvarehSutra.hasSeshaBhavaAgentObject(context) &&
            !AsisiNathahSutra.hasSeshaWishObject(context) &&
            !JasiniPrahanaNatakaKrathaPisarnHimvisayamSutra.hasSeshaInjuryObject(context) &&
            !VyavahruPanohSamarthayohSutra.hasSeshaTransactionObject(context) &&
            !DivasTadarthasyaSutra.hasSeshaDivObject(context) &&
            !PresyabruvorHavisoDevatasampradaneSutra.hasRitualOblationObject(context) &&
            !CaturthyartheBahulamChandasiSutra.hasLicensedUse(context) &&
            !YajesCaKaraneSutra.hasLicensedUse(context) &&
            !KrtvorthaPrayogeKaleAdhikaraneSutra.hasSeshaTimeFrequency(context) &&
            !PancamiVibhakteSutra.hasDistinctReference(context) &&
            !SadhuNipunabhyamSutra.hasArchaConstruction(context) &&
            !PrasitotsukabhyamTrtiyaCaSutra.hasAttachmentConstruction(context) &&
            (context.karaka == Karaka.ANIRDHARITA) &&
            Vibhakti.SASTHI in context.morphologicalCandidates

    override fun apply(context: VibhaktiRuleContext) = VibhaktiRuleResult.Assigned(
        Vibhakti.SASTHI,
        KarakaEvidence(number, text, "षष्ठी realizes non-kāraka relational connection (2.3.50 षष्ठी शेषे)."),
    )
}

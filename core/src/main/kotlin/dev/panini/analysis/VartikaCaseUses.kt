package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Vibhakti

/**
 * Case uses that vārttikas attach to a canonical rule, without being registered as sūtras.
 * Each needs its own established premise or written governor, so a case ending proves nothing.
 */
object VartikaCaseUses {
    private val accusativeGovernors = setOf("उभयतः", "सर्वतः", "धिक्", "उपर्युपरि", "अध्यधि", "अधोऽधः",
        "अभितः", "परितः", "समया", "निकषा", "हा")
    private val dativeUses = mapOf(
        SemanticRelation.TADARTHYA_PURPOSE to "तादर्थ्ये चतुर्थी",
        SemanticRelation.KLRP_RESULT to "कॢपि सम्पद्यमाने च",
        SemanticRelation.UTPATA_INDICATED to "उत्पातेन ज्ञापिते च",
        SemanticRelation.HITA_BENEFICIARY to "हितयोगे च",
    )

    private fun VibhaktiRuleContext.open(case: Vibhakti) = !abhihita && karaka == Karaka.ANIRDHARITA && case in morphologicalCandidates
    private fun VibhaktiRuleContext.relations() = participant?.semanticRelations.orEmpty()

    fun accusative(context: VibhaktiRuleContext): Boolean = context.open(Vibhakti.DVITIYA) &&
        (context.participant?.governingAvyaya?.takeIf { it.derivation == null }?.form in accusativeGovernors ||
            SemanticRelation.ACCUSATIVE_CONCORD in context.relations())
    fun dative(context: VibhaktiRuleContext): Boolean = context.open(Vibhakti.CHATURTHI) &&
        dativeUses.keys.any { it in context.relations() }
    fun instrumental(context: VibhaktiRuleContext): Boolean = context.open(Vibhakti.TRTIYA) &&
        SemanticRelation.PRAKRTYADI_MANNER in context.relations()
    fun ablative(context: VibhaktiRuleContext): Boolean = context.open(Vibhakti.PANCHAMI) &&
        (SemanticRelation.LYAB_LOPA_REFERENCE in context.relations() || SemanticRelation.MEASURE_ORIGIN in context.relations() ||
            SemanticRelation.QUESTION_ANSWER_SOURCE in context.relations())
    fun locative(context: VibhaktiRuleContext): Boolean = context.open(Vibhakti.SAPTAMI) &&
        (SemanticRelation.KTA_IN_OBJECT_LOCUS in context.relations() || SemanticRelation.SADHU_ASADHU_REFERENCE in context.relations() ||
            SemanticRelation.NIMITTA_OBJECT_LOCUS in context.relations() ||
            SemanticRelation.KARAKA_WORTHINESS_MARK in context.relations())
    /** निमित्तपर्यायप्रयोगे सर्वासां प्रायदर्शनम्: every case is attested, so the written one stands. */
    fun nimitta(context: VibhaktiRuleContext): Boolean = !context.abhihita && context.karaka == Karaka.ANIRDHARITA &&
        SemanticRelation.NIMITTA_SYNONYM_HETU in context.relations() && context.morphologicalCandidates.size == 1

    /** Names the vārttika in a rule's evidence when it, not the sūtra's kāraka, licensed the case. */
    fun note(context: VibhaktiRuleContext): String = when {
        accusative(context) -> " Vārttika उभसर्वतसोः कार्या."
        dative(context) -> " Vārttika " + dativeUses.entries.first { it.key in context.relations() }.value + "."
        instrumental(context) -> " Vārttika प्रकृत्यादिभ्य उपसंख्यानम्."
        ablative(context) -> " Vārttika ल्यब्लोपे कर्मण्यधिकरणे च or यतश्चाध्वकालनिर्माणम्."
        locative(context) -> " Vārttika क्तस्येन्विषयस्य कर्मणि or साध्वसाधुप्रयोगे."
        nimitta(context) -> " Vārttika निमित्तपर्यायप्रयोगे सर्वासां प्रायदर्शनम्."
        else -> ""
    }
}

package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga

/** Expression belongs to a selected object reading, not to the KARMAN name as a whole. */
internal object PassiveObjectExpression {
    fun applies(context: KarakaRuleContext, role: Karaka, designation: String?): Boolean =
        context.prayoga == Prayoga.KARMANI && context.governingKrt == null && role == Karaka.KARMAN &&
            ((designation in setOf("1.4.52", "1.4.53") && hasFreeChoice(context)) ||
                (designation in setOf("1.4.49", "1.4.50", "1.4.51") && (hasPromotedCausee(context) ||
                    (!KarakaDhatuDomains.isCausativeConstruction(context) && KarakaDhatuDomains.isDvikarmaka(context)))))

    private fun hasFreeChoice(context: KarakaRuleContext): Boolean =
        KarakaDhatuDomains.isCausativeConstruction(context) && KarakaDhatuDomains.hasFreeCausativePromotion(context)

    /** ण्यन्ते कर्तुश्च कर्मणः: motion and intransitive causatives and हृ, कृ express the causee (Padamañjarī on 1.4.51). */
    private fun hasPromotedCausee(context: KarakaRuleContext): Boolean =
        KarakaDhatuDomains.isCausativeConstruction(context) && context.allParticipants.any { other ->
            other.id != context.participant.id && other.governingNominal === context.participant.governingNominal &&
                other.purposeGovernor?.id == context.participant.purposeGovernor?.id &&
                other.meaningEvidenceStrength(SemanticRelation.CAUSEE_AGENT) >= 2
        }

    /** Null preserves an unresolved expression prerequisite and must never mean unexpressed. */
    fun isExpressed(context: KarakaRuleContext, designation: String?): Boolean? {
        // Where either object may be expressed, the written nominative shows the selected one.
        if (hasFreeChoice(context)) return dev.panini.core.Vibhakti.PRATHAMA in context.participant.possibleVibhaktis
        if (hasPromotedCausee(context)) return false
        val secondaryPromoted = KarakaDhatuDomains.promotesSecondaryObjectInPassive(context)
        if (designation == "1.4.51") return secondaryPromoted
        val explicitDvikarmaka = KarakaDhatuDomains.hasEstablishedDvikarmakaSense(context)
        val secondaryReadings = context.allParticipants.filter { other ->
            other.id != context.participant.id && other.governingNominal === context.participant.governingNominal &&
                other.purposeGovernor?.id == context.participant.purposeGovernor?.id &&
                other.meaningEvidenceStrength(SemanticRelation.SECONDARY_OBJECT) >= 2 &&
                other.isStrongestOrdinaryReading(SemanticRelation.SECONDARY_OBJECT)
        }
        // A primary-only reading can be an ordinary single-object use of the root.
        if (!explicitDvikarmaka && secondaryReadings.isEmpty()) return true
        if (secondaryPromoted == null) return null
        if (!secondaryPromoted) return true
        if (!explicitDvikarmaka && secondaryReadings.all { other ->
                val strength = other.meaningEvidenceStrength(SemanticRelation.SECONDARY_OBJECT)
                competingMeanings.any { other.meaningEvidenceStrength(it) >= strength }
            }) return null
        return false
    }

    private val competingMeanings = setOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.DESIRED_OBJECT,
        SemanticRelation.INDIFFERENT_OBJECT, SemanticRelation.INSTRUMENT, SemanticRelation.LOCATION,
        SemanticRelation.SOURCE, SemanticRelation.RECIPIENT)
}

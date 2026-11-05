package dev.panini.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga

/** Expression belongs to a selected object reading, not to the KARMAN name as a whole. */
internal object PassiveObjectExpression {
    fun applies(context: KarakaRuleContext, role: Karaka, designation: String?): Boolean =
        context.prayoga == Prayoga.KARMANI && context.governingKrt == null && role == Karaka.KARMAN &&
            !KarakaDhatuDomains.isCausativeConstruction(context) &&
            designation in setOf("1.4.49", "1.4.50", "1.4.51") && KarakaDhatuDomains.isDvikarmaka(context)

    /** Null preserves an unresolved expression prerequisite and must never mean unexpressed. */
    fun isExpressed(context: KarakaRuleContext, designation: String?): Boolean? {
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

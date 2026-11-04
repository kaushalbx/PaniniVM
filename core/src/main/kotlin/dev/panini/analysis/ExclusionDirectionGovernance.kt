package dev.panini.analysis

import dev.panini.vyakaranam.ast.MulaPratipadika

/** Verified lexical slice of 2.3.29; añcu/āc/āhi derivations are not guessed from endings. */
object ExclusionDirectionGovernance {
    private val nominalStems = setOf("अन्य", "भिन्न", "अर्थान्तर", "विलक्षण", "इतर", "पूर्व", "उत्तर")

    fun hasConstruction(context: VibhaktiRuleContext): Boolean {
        val p = context.participant ?: return false
        val stem = (p.governingNominal?.pratipadika as? MulaPratipadika)
            ?.takeIf { it.vikaras.isEmpty() }?.text
        if (stem in nominalStems && SemanticRelation.DIRECTIONAL_EXCLUSION in p.semanticRelations) return true
        val avyaya = p.governingAvyaya?.takeIf { it.derivation == null } ?: return false
        if (avyaya.form !in setOf("आरात्", "ऋते")) return false
        return SemanticRelation.DIRECTIONAL_EXCLUSION in p.semanticRelations ||
            (avyaya.form == "आरात्" && SemanticRelation.DISTANCE_OR_PROXIMITY in p.semanticRelations)
    }
}

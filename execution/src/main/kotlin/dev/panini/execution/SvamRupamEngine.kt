package dev.panini.execution

import dev.panini.shiksha.Samjna

/**
 * Pāṇinian Formal Identity & Value vs Symbol Engine based on Sūtra 1.1.68 (स्वं रूपं शब्दस्याशब्दसंज्ञा).
 *
 * Evaluates word terms to their own self-referential literal form (स्वं रूपम्)
 * unless bound to a technical Saṃjñā in scope (अशब्दसंज्ञा).
 */
object SvamRupamEngine {

    /**
     * Resolves a word term according to Sūtra 1.1.68 (स्वं रूपं शब्दस्याशब्दसंज्ञा).
     * If the term matches a technical Saṃjñā, returns null (allowing technical resolution);
     * otherwise returns its self-referential literal [SanskritValue.Shabda].
     */
    fun evaluateTerm(term: String): SanskritValue {
        val cleanTerm = SamjnaKriyaRegistry.stripSupSuffix(term)
        return if (TechnicalSamjnaIdentity.contains(cleanTerm) || TechnicalSamjnaIdentity.contains(term)) {
            SanskritValue.of(term)
        } else {
            // Sūtra 1.1.68: Formal self-referential identity (स्वं रूपम्)
            SanskritValue.Shabda(cleanTerm, setOf(Samjna.SHABDA))
        }
    }

    /**
     * Checks if a term represents a self-referential literal (स्वं रूपम्).
     */
    fun isSelfReferentialLiteral(term: String): Boolean {
        val cleanTerm = SamjnaKriyaRegistry.stripSupSuffix(term)
        return !TechnicalSamjnaIdentity.contains(cleanTerm) && !TechnicalSamjnaIdentity.contains(term)
    }
}

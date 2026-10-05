package dev.panini.execution

import dev.panini.core.NominalCategory
import dev.panini.shiksha.Samjna
import dev.panini.vyakaranam.ast.Pratipadika
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon

/**
 * Pāṇinian Formal Identity & Value vs Symbol Engine based on Sūtra 1.1.68 (स्वं रूपं शब्दस्याशब्दसंज्ञा).
 *
 * Evaluates word terms to their own self-referential literal form (स्वं रूपम्)
 * unless bound to a technical Saṃjñā in scope (अशब्दसंज्ञा).
 */
object SvamRupamEngine {

    /**
     * Resolves parsed nominal morphology.  Vibhakti is absent by construction because
     * the caller supplies the [Pratipadika], rather than a serialized [SubantaPada].
     */
    fun evaluate(pratipadika: Pratipadika): SanskritValue =
        evaluateIdentity(pratipadika.semanticKey())

    /**
     * Resolves a word term according to Sūtra 1.1.68 (स्वं रूपं शब्दस्याशब्दसंज्ञा).
     * If the term matches a technical Saṃjñā, returns null (allowing technical resolution);
     * otherwise returns its self-referential literal [SanskritValue.Shabda].
     */
    fun evaluateTerm(term: String): SanskritValue = evaluateIdentity(term)

    /** Checks parsed nominal morphology for स्वं रूपम् semantics. */
    fun isSelfReferentialLiteral(pratipadika: Pratipadika): Boolean =
        !isTechnicalSamjna(pratipadika.semanticKey())

    /**
     * Checks if a term represents a self-referential literal (स्वं रूपम्).
     */
    fun isSelfReferentialLiteral(term: String): Boolean = !isTechnicalSamjna(term)

    private fun evaluateIdentity(identity: String): SanskritValue =
        if (isTechnicalSamjna(identity)) {
            SanskritValue.of(identity)
        } else {
            // Sūtra 1.1.68: Formal self-referential identity (स्वं रूपम्)
            SanskritValue.Shabda(identity, setOf(Samjna.SHABDA))
        }

    private fun isTechnicalSamjna(identity: String): Boolean =
        StandardPratipadikaLexicon.findPratipadika(identity)
            ?.categories
            ?.contains(NominalCategory.TECHNICAL_SAMJNA) == true
}

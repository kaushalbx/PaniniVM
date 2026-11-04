package dev.panini.analysis

/** Semantic outcome retained when an optional kāraka-saṃjñā rule is declined. */
interface OptionalKarakaRuleOutcome {
    /** Called after this rule's antecedent has established the underlying relation. */
    fun declinedDesignation(context: KarakaRuleContext): KarakaRuleResult.Assigned
}

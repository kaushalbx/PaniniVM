package dev.panini.execution

/**
 * Pāṇinian Sthānivadbhāva Polymorphic Delegation Engine based on Sūtra 1.1.56 (स्थानिवदादेशोऽनल्विधौ).
 *
 * Enables derived struct/class instances (आदेश) to inherit and fallback-delegate
 * missing method operations to parent classes (स्थानी).
 */
object SthanivadbhavaDelegationEngine {

    /**
     * Resolves the target parent class (स्थानी) when a method operation is called on a child class (आदेश).
     */
    fun resolveParentFallbackDomain(
        childDomain: String,
        inheritanceMap: Map<String, String>,
    ): String? {
        val cleanChild = childDomain.substringBefore("+").trim()
        val parent = inheritanceMap[childDomain] ?: inheritanceMap[cleanChild]
        return parent
    }
}

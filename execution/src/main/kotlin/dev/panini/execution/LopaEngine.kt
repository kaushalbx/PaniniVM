package dev.panini.execution

/**
 * Pāṇinian Lopa Null-Safety Engine based on Sūtra 1.1.60 (अदर्शनं लोपः).
 *
 * Evaluates missing or un-initialized struct properties / attributes
 * to [SanskritValue.Lopa] (लोपः / अदर्शनम्) cleanly without throwing errors.
 */
object LopaEngine {

    /**
     * Safely queries an attribute from a struct instance.
     * Returns [SanskritValue.Lopa] if the attribute is absent or un-initialized (1.1.60 अदर्शनं लोपः).
     */
    fun queryAttributeSafe(
        struct: TaddhitaStruct,
        attributeKey: String,
    ): SanskritValue {
        val strVal = struct.attributes[attributeKey] ?: return SanskritValue.Lopa
        return SanskritValue.of(strVal)
    }

    /**
     * Checks if a value represents Pāṇinian Lopa (elision / null).
     */
    fun isLopa(value: SanskritValue): Boolean = value is SanskritValue.Lopa
}

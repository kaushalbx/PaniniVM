package dev.panini.derivation

/** Canonical identities of sanādi affixes; surface forms are never used for classification. */
object SanadiAffixes {
    val upadeshas: Set<String> = setOf("णिच्", "सन्", "यङ्", "क्यच्", "क्यङ्", "काम्यच्")

    fun contains(upadesha: String): Boolean = upadesha in upadeshas
}

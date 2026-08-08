package dev.panini.execution

/** Canonical lexical identities treated as technical saṃjñās by svam-rūpa evaluation. */
internal object TechnicalSamjnaIdentity {
    private val IDENTITIES = setOf(
        "सङ्ख्या",
        "गुण",
        "वृद्धि",
        "लोप",
        "साधकतमम्",
        "कर्म",
        "करणम्",
    )

    fun contains(identity: String): Boolean = identity in IDENTITIES
}

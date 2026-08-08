package dev.panini.execution

/** Compatibility stems used until their full kr̥danta derivations are executable. */
internal object PvmKridantaLexicon {
    private val STEMS = mapOf(
        ("युज्" to "घञ्") to "योग",
        ("युज्" to "अप्") to "योग",
        ("युज्" to "ल्युट्") to "योजन",
        ("युज्" to "अन") to "योजन",
        ("गण" to "ल्युट्") to "गणन",
        ("गण" to "अन") to "गणन",
        ("धृ" to "ल्युट्") to "धारण",
        ("धृ" to "अन") to "धारण",
        ("स्था" to "ल्युट्") to "स्थान",
        ("स्था" to "अन") to "स्थान",
        ("जन्" to "ल्युट्") to "जनन",
        ("जन्" to "अन") to "जनन",
        ("शिष्" to "घञ्") to "शेष",
        ("शिष्" to "अप्") to "शेष",
        ("मूल्" to "घञ्") to "मूल",
        ("मूल्" to "अप्") to "मूल",
        ("भज्" to "घञ्") to "भाग",
        ("हृ" to "ल्युट्") to "हरण",
        ("हृ" to "घञ्") to "हार",
    )
    private val DEFAULT_STEMS = mapOf("हृ" to "हर")

    val declinableStems: Set<String> = STEMS.values.toSet()

    fun stem(dhatu: String, pratyaya: String): String =
        STEMS[dhatu to pratyaya] ?: DEFAULT_STEMS[dhatu] ?: dhatu
}

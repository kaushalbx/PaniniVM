package dev.panini.execution

import dev.panini.derivation.KrdantaDerivationRequest
import dev.panini.derivation.KrdantaEngine
import dev.panini.shiksha.Samjna

/** Compatibility stems used until their full kr̥danta derivations are executable. */
internal object PvmKridantaLexicon {
    private val krdantaEngine = KrdantaEngine()
    private val supportedGhanDhatus = setOf("युज्", "शिष्", "मूल्", "भज्", "हृ")
    private val supportedApDhatus = setOf("युज्", "शिष्", "मूल्")
    private val STEMS = mapOf(
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
        ("हृ" to "ल्युट्") to "हरण",
    )
    private val DEFAULT_STEMS = mapOf("हृ" to "हर")

    fun isDeclinable(dhatu: String, pratyaya: String): Boolean =
        (pratyaya == "घञ्" && dhatu in supportedGhanDhatus) ||
            (pratyaya == "अप्" && dhatu in supportedApDhatus) ||
            (dhatu to pratyaya) in STEMS

    fun stem(dhatu: String, pratyaya: String): String = when (pratyaya) {
        "घञ्" -> if (dhatu in supportedGhanDhatus) deriveGhan(dhatu) else DEFAULT_STEMS[dhatu] ?: dhatu
        "अप्" -> if (dhatu in supportedApDhatus) deriveGhan(dhatu) else DEFAULT_STEMS[dhatu] ?: dhatu
        else -> STEMS[dhatu to pratyaya] ?: DEFAULT_STEMS[dhatu] ?: dhatu
    }

    private fun deriveGhan(dhatu: String): String =
        krdantaEngine.derive(KrdantaDerivationRequest(dhatu, Samjna.GHAN)).final.surface
}

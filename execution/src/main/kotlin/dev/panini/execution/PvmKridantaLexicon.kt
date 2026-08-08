package dev.panini.execution

import dev.panini.derivation.KrdantaDerivationRequest
import dev.panini.derivation.KrdantaEngine
import dev.panini.shiksha.Samjna

/** Compatibility stems used until their full kr̥danta derivations are executable. */
internal object PvmKridantaLexicon {
    private val krdantaEngine = KrdantaEngine()
    private val supportedGhanDhatus = setOf("युज्", "शिष्", "मूल्", "भज्", "हृ")
    private val supportedApDhatus = setOf("युज्", "शिष्", "मूल्")
    private val supportedLyutDhatus = setOf("युज्", "गण", "धृ", "स्था", "जन्", "हृ")
    private val DEFAULT_STEMS = mapOf("हृ" to "हर")

    fun isDeclinable(dhatu: String, pratyaya: String): Boolean =
        (pratyaya == "घञ्" && dhatu in supportedGhanDhatus) ||
            (pratyaya == "अप्" && dhatu in supportedApDhatus) ||
            ((pratyaya == "ल्युट्" || pratyaya == "अन") && dhatu in supportedLyutDhatus)

    fun stem(dhatu: String, pratyaya: String): String = when (pratyaya) {
        "घञ्" -> if (dhatu in supportedGhanDhatus) deriveGhan(dhatu) else DEFAULT_STEMS[dhatu] ?: dhatu
        "अप्" -> if (dhatu in supportedApDhatus) deriveGhan(dhatu) else DEFAULT_STEMS[dhatu] ?: dhatu
        "ल्युट्", "अन" -> if (dhatu in supportedLyutDhatus) deriveLyut(dhatu) else DEFAULT_STEMS[dhatu] ?: dhatu
        else -> DEFAULT_STEMS[dhatu] ?: dhatu
    }

    private fun deriveGhan(dhatu: String): String =
        krdantaEngine.derive(KrdantaDerivationRequest(dhatu, Samjna.GHAN)).final.surface

    private fun deriveLyut(dhatu: String): String =
        krdantaEngine.derive(KrdantaDerivationRequest(dhatu, Samjna.LYUT)).final.surface
}

package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: अच् vowels and हल् consonants are not mutually सवर्ण.
 * हिन्दी: अच् और हल् एक-दूसरे के सवर्ण नहीं माने जाते।
 * Code reference: dev.sanskrit.pratyahara.Pratyahara.AC, dev.sanskrit.pratyahara.Pratyahara.HAL, and dev.sanskrit.samjna.VarnaSamjna.isSavarna.
 */
object NajjhalauSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.10",
        sutraText = "नाज्झलौ",
        hindiVyakhya = "अच् और हल् परस्पर सवर्ण नहीं माने जाते।",
        type = SutraType.NISHEDHA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110010,
        avastha = SutraAvastha.KRIYAVAT,
    )
}

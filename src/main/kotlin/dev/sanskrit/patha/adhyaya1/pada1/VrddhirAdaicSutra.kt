package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: Defines the वृद्धि vowels as आ, ऐ, and औ.
 * हिन्दी: आ, ऐ और औ स्वर वृद्धि संज्ञा वाले माने जाते हैं।
 * Code reference: dev.sanskrit.samjna.SvaraSamjna.vrddhi.
 */
object VrddhirAdaicSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.1",
        sutraText = "वृद्धिरादैच्",
        hindiVyakhya = "आ, ऐ और औ को वृद्धि संज्ञा दी जाती है।",
        type = SutraType.SAMJNA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110001,
        avastha = SutraAvastha.KRIYAVAT,
    )
}

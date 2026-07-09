package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: Defines the गुण vowels as अ, ए, and ओ.
 * हिन्दी: अ, ए और ओ स्वर गुण संज्ञा वाले माने जाते हैं।
 * Code reference: dev.sanskrit.samjna.SvaraSamjna.guna.
 */
object AdengGunaSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.2",
        sutraText = "अदेङ् गुणः",
        hindiVyakhya = "अ, ए और ओ को गुण संज्ञा दी जाती है।",
        type = SutraType.SAMJNA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110002,
        avastha = SutraAvastha.KRIYAVAT,
    )
}

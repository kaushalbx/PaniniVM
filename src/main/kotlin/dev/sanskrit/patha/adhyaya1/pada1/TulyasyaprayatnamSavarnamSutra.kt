package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: Sounds with the same place of articulation and effort are called सवर्ण.
 * हिन्दी: समान आस्य और समान प्रयत्न वाले वर्ण सवर्ण कहलाते हैं।
 * Code reference: dev.sanskrit.samjna.VarnaSamjna.isSavarna.
 */
object TulyasyaprayatnamSavarnamSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.9",
        sutraText = "तुल्यास्यप्रयत्नं सवर्णम्",
        hindiVyakhya = "जिन वर्णों का आस्य और प्रयत्न समान हो, उन्हें सवर्ण संज्ञा दी जाती है।",
        type = SutraType.SAMJNA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110009,
        avastha = SutraAvastha.KRIYAVAT,
    )
}

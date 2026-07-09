package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: When गुण or वृद्धि is prescribed for an इक् vowel, this rule determines the corresponding substitute.
 * हिन्दी: इक् स्वर के लिए गुण या वृद्धि कही जाए तो इसी सूत्र से उसका यथोचित आदेश समझा जाता है।
 * Code reference: dev.sanskrit.pratyahara.Pratyahara.IK and dev.sanskrit.samjna.SvaraSamjna.
 */
object IkoGunaVrddhiSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.3",
        sutraText = "इको गुणवृद्धी",
        hindiVyakhya = "जहाँ इक् स्वर के स्थान पर गुण या वृद्धि कही जाए, वहाँ उसके अनुरूप गुण अथवा वृद्धि आदेश समझा जाता है।",
        type = SutraType.PARIBHASHA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110003,
        avastha = SutraAvastha.KRIYAVAT,
    )
}

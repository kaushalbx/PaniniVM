package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: A sound pronounced through both mouth and nose is called अनुनासिक.
 * हिन्दी: मुख और नासिका दोनों से उच्चरित वर्ण अनुनासिक कहलाता है।
 * Code reference: dev.sanskrit.samjna.VarnaSamjna.isAnunasika.
 */
object MukhanasikavacanoAnunasikahSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.8",
        sutraText = "मुखनासिकावचनोऽनुनासिकः",
        hindiVyakhya = "जिस वर्ण का उच्चारण मुख और नासिका दोनों से हो, उसे अनुनासिक संज्ञा दी जाती है।",
        type = SutraType.SAMJNA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110008,
        avastha = SutraAvastha.KRIYAVAT,
    )
}

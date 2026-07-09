package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: Extends the relevant गुण-वृद्धि prohibition to कित् and ङित् affix environments.
 * हिन्दी: कित् और ङित् प्रत्यय के प्रसंग में भी संबंधित गुण-वृद्धि निषिद्ध रहती है।
 * Code reference: गुण-वृद्धि mapping is in dev.sanskrit.samjna.SvaraSamjna; कित्/ङित् affix markers are not implemented yet.
 */
object KngitiCaSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.5",
        sutraText = "क्ङिति च",
        hindiVyakhya = "कित् या ङित् प्रत्यय के प्रसंग में भी संबंधित गुण-वृद्धि का निषेध माना जाता है।",
        type = SutraType.NISHEDHA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110005,
        avastha = SutraAvastha.PATHITA,
    )
}

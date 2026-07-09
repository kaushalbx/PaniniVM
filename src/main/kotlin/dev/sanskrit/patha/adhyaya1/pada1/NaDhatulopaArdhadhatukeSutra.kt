package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: Blocks गुण-वृद्धि effects caused merely by loss of a dhatu before आर्धधातुक affixes.
 * हिन्दी: आर्धधातुक प्रत्यय के प्रसंग में केवल धातु-लोप के कारण गुण-वृद्धि का प्रवर्तन नहीं होता।
 * Code reference: गुण-वृद्धि mapping is in dev.sanskrit.samjna.SvaraSamjna; धातु/pratyaya logic is not implemented yet.
 */
object NaDhatulopaArdhadhatukeSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.4",
        sutraText = "न धातुलोप आर्धधातुके",
        hindiVyakhya = "आर्धधातुक प्रत्यय के प्रसंग में धातु-लोप होने पर गुण-वृद्धि आदि का प्रवर्तन निषिद्ध होता है।",
        type = SutraType.NISHEDHA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110004,
        avastha = SutraAvastha.PATHITA,
    )
}

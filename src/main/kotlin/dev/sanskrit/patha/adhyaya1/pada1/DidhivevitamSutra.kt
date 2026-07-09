package dev.sanskrit.patha.adhyaya1.pada1

import dev.sanskrit.sutra.SutraAvastha
import dev.sanskrit.sutra.SutraMetadata
import dev.sanskrit.sutra.SutraType

/**
 * English: Applies the preceding prohibition framework to the specifically listed दीधी and वेवी forms.
 * हिन्दी: दीधी और वेवी आदि निर्दिष्ट रूपों पर पूर्व निषेध की व्यवस्था लागू होती है।
 * Code reference: this is patha metadata only for now; धातु-specific handling is not implemented yet.
 */
object DidhivevitamSutra {
    val metadata = SutraMetadata(
        sutraNumber = "1.1.6",
        sutraText = "दीधीवेवीटाम्",
        hindiVyakhya = "दीधी, वेवी आदि निर्देशित धातुओं के प्रसंग में पूर्व निषेध की व्यवस्था लागू होती है।",
        type = SutraType.NISHEDHA,
        adhyaya = 1,
        pada = 1,
        vaikalpika = false,
        krama = 110006,
        avastha = SutraAvastha.PATHITA,
    )
}

package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

abstract class EnumeratedMasculineSutra(
    number: String,
    text: String,
    private val stems: Set<String>,
) : LinganushasanaSutra(
    number = number,
    text = text,
    hindiExplanation = "सूत्र में निर्दिष्ट शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    final override fun matches(context: LingaRuleContext): Boolean = context.pratipadika in stems
}

object RsiRasiDrtiGranthiKrimiDhvaniSutra : EnumeratedMasculineSutra(
    "109",
    "ऋषिराशिदृतिग्रन्थिक्रिमिध्वनिबालिकौलिमौलिरविकविकपिमुनयः",
    setOf("ऋषि", "राशि", "दृति", "ग्रन्थि", "क्रिमि", "ध्वनि", "बालि", "कौलि", "मौलि", "रवि", "कवि", "कपि", "मुनि"),
)

object DhvajaGajaMunjaPunjaSutra : EnumeratedMasculineSutra(
    "110", "ध्वजगजमुञ्जपुञ्जाः", setOf("ध्वज", "गज", "मुञ्ज", "पुञ्ज"),
)

object HastaKuntaAntaVataVrataDutaSutra : EnumeratedMasculineSutra(
    "111",
    "हस्तकुन्तान्तवातव्रातदूतधूर्तसूतचूतमुहूर्ताः",
    setOf("हस्त", "कुन्त", "अन्त", "वात", "व्रात", "दूत", "धूर्त", "सूत", "चूत", "मुहूर्त"),
)

object HradaKandaKundaBudbudaSabdaSutra : EnumeratedMasculineSutra(
    "114", "ह्रदकन्दकुन्दबुद्बुदशब्दाः", setOf("ह्रद", "कन्द", "कुन्द", "बुद्बुद", "शब्द"),
)

object PallavaPalvalaKapharephaSutra : EnumeratedMasculineSutra(
    "116",
    "पल्लवपल्वलकफरेफकटाहनिर्व्यूहमठमणितरङ्गतुरङ्गगन्धस्कन्धमृदङ्गसङ्गसमुद्गपुङ्खाः",
    setOf("पल्लव", "पल्वल", "कफ", "रेफ", "कटाह", "निर्व्यूह", "मठ", "मणि", "तरङ्ग", "तुरङ्ग", "गन्ध", "स्कन्ध", "मृदङ्ग", "सङ्ग", "समुद्ग", "पुङ्ख"),
)

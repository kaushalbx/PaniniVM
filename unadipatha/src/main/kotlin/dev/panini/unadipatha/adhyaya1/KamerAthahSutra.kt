package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.100: कमेरठः
object KamerAthahSutra : UnadiSutra(
    number = "1.100",
    text = "कमेरठः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0511" }),
    pratyaya = "अठ",
    pratyayaSurface = "अठ",
    rootSamjnaMap = mapOf("कमुँ" to Samjna.Rudhi("कमठ")),
    rootMeaningMap = mapOf("कमुँ" to Artha.Explanation("पुल्लिङ्ग में कच्छप और नपुंसकलिङ्ग में भाण्डभेद")),
    rootMorphology = mapOf("कमुँ" to UnadiRootMorphology(conditionedStem = "कम्".toVarnas())),
    derivedVariants = listOf(
        UnadiDerivedVariant(output = "जरठ".toVarnas(), meaning = Artha.Explanation("बाहुलक से प्राप्त रूप; वृत्ति में अर्थ अनिर्दिष्ट")),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कमठ".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कच्छप"), "1.100"),
        UnadiLexicalAttestation("कमठ".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("भाण्डभेद"), "1.100"),
        UnadiLexicalAttestation("जरठ".toVarnas(), attributedSutra = "1.100"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कमठ अठ-प्रत्ययान्त प्रातिपदिक है; बाहुलक से जरठ रूप भी प्रमाणित है।"),
    hindiExplanation = "कमुँ धातु के कम् आधार के बाद अठ प्रत्यय होता है और कम् + अठ से कमठ प्रातिपदिक बनता है। मेदिनी के उद्धरण के अनुसार कमठ पुल्लिङ्ग में कच्छप तथा नपुंसकलिङ्ग में एक प्रकार के भाण्ड का नाम है; दोनों अर्थ-लिङ्ग पृथक् attestations में रखे गए हैं। वृत्ति बाहुलक से जरठ रूप भी देती है, किन्तु न उसका मूल धातु खोलती है और न यहाँ कोई अर्थ बताती है। इसलिए जरठ को स्रोतसिद्ध derived variant बनाया गया है, पर उसके लिए काल्पनिक धातु, वर्णादेश, अर्थ या लिङ्ग नहीं जोड़ा गया।",
)

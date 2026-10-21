package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.divadi.JanDhatu
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.72: कमिमनिजनिगाभायाहिभ्यश्च
object KamiManiJaniGaBhaYaHibhyasCaSutra : UnadiSutra(
    number = "1.72",
    text = "कमिमनिजनिगाभायाहिभ्यश्च",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0511" },
        DivadiDhatus.all.single { it.id == "04.0073" },
        JanDhatu(),
        JuhotyadiDhatus.all.single { it.id == "03.0026" },
        AdadiDhatus.all.single { it.id == "02.0046" },
        AdadiDhatus.all.single { it.id == "02.0044" },
        SvadiDhatus.all.single { it.id == "05.0012" },
    ),
    pratyaya = "तु",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.KIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf(
        "कमुँ" to Samjna.Rudhi("कन्तु"),
        "मनँ" to Samjna.Rudhi("मन्तु"),
        "जनीँ" to Samjna.Rudhi("जन्तु"),
        "गा" to Samjna.Rudhi("गातु"),
        "भा" to Samjna.Rudhi("भानु"),
        "या" to Samjna.Rudhi("यातु"),
        "हि" to Samjna.Rudhi("हेतु"),
    ),
    rootMeaningMap = mapOf(
        "कमुँ" to Artha.Explanation("कन्दर्प अथवा चित्त"),
        "मनँ" to Artha.Explanation("अपराध"),
        "जनीँ" to Artha.Explanation("प्राणी"),
        "गा" to Artha.Explanation("कोकिल, भृङ्ग, गन्धर्व अथवा गायन"),
        "भा" to Artha.Explanation("आदित्य"),
        "या" to Artha.Explanation("अध्वग, काल अथवा रक्षस्"),
        "हि" to Artha.Explanation("कारण"),
    ),
    rootMorphology = mapOf(
        "कमुँ" to UnadiRootMorphology(
            conditionedStem = "कम्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.MA, Vyanjana.NA)),
        ),
        "भा" to UnadiRootMorphology(
            conditionedStem = "भा".toVarnas(),
            pratyayaVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.TA, Vyanjana.NA)),
        ),
        "हि" to UnadiRootMorphology(
            conditionedStem = "हि".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कन्तु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कन्दर्प अथवा चित्त"), "1.72"),
        UnadiLexicalAttestation("मन्तु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("अपराध"), "1.72"),
        UnadiLexicalAttestation("जन्तु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("प्राणी"), "1.72"),
        UnadiLexicalAttestation("गातु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कोकिल, भृङ्ग, गन्धर्व अथवा गायन"), "1.72"),
        UnadiLexicalAttestation("भानु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("आदित्य"), "1.72"),
        UnadiLexicalAttestation("यातु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("अध्वग अथवा काल"), "1.72"),
        UnadiLexicalAttestation("यातु".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("रक्षस्"), "1.72"),
        UnadiLexicalAttestation("हेतु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कारण"), "1.72"),
    ),
    relatedSutraNumbers = setOf("1.71", "7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कन्तु आदि सात कित् तु-प्रत्ययान्त प्रातिपदिक वृत्ति में दिए अर्थों में प्रयुक्त होते हैं।"),
    hindiExplanation = "पिछले सूत्र से कित् तु प्रत्यय की अनुवृत्ति लेकर यह सूत्र कमि, मनि, जनि, गा, भा, या और हि—इन सात धातुओं पर उसका विधान करता है; 7.2.9 के अनुसार इट्-आगम नहीं होता। कमुँ के कम् आधार में त् के सामने म् का न् होकर कन्तु बनता है, जिसका अर्थ कन्दर्प अथवा चित्त है। मनँ के मन् आधार से मन्तु ‘अपराध’ और जनीँ के जन् आधार से जन्तु ‘प्राणी’ बनता है। गा + तु से गातु बनता है, जिसे वृत्ति कोकिल, भृङ्ग, गन्धर्व और गायन का वाचक बताती है। भा वाले अपवाद में प्रत्यय के त् का न्-आदेश होकर भा + नु से भानु ‘आदित्य’ सिद्ध होता है। या + तु से यातु बनता है; यह पुल्लिङ्ग में अध्वग अथवा काल और नपुंसकलिङ्ग में रक्षस् का वाचक है, इसलिए दोनों लिङ्गार्थ अलग रखे गए हैं। हि में इ का गुण ए होकर हे + तु से हेतु ‘कारण’ बनता है; कित्त्व के बावजूद यह स्रोतसिद्ध औणादिक रूप अलग वर्णादेश के रूप में सुरक्षित है।",
)

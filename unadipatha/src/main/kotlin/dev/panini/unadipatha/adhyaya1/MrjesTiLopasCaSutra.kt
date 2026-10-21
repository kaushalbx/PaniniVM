package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSequenceLopa

// 1.107: मृजेष्टिलोपश्च
object MrjesTiLopasCaSutra : UnadiSutra(
    number = "1.107",
    text = "मृजेष्टिलोपश्च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0061" }),
    pratyaya = "कल",
    pratyayaSurface = "अल",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("मृजूँ" to Samjna.Rudhi("मल")),
    rootMorphology = mapOf(
        "मृजूँ" to UnadiRootMorphology(
            conditionedStem = "मृज्".toVarnas(),
            varnaSequenceLopas = listOf(UnadiVarnaSequenceLopa(listOf(Svara.R, Vyanjana.JA))),
        ),
    ),
    lexicalAttestations = listOf(UnadiLexicalAttestation("मल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.107")),
    relatedSutraNumbers = setOf("1.104"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मल कित् कल-प्रत्ययान्त नपुंसकलिङ्ग प्रातिपदिक है।"),
    hindiExplanation = "मृजूँ धातु के मृज् आधार के बाद पिछले कल-प्रत्यय का विधान जारी रहता है। कल का क् इत्संज्ञक होकर लुप्त होता है और अल शेष रहता है। सूत्र का ‘टिलोप’ मृज् के अन्तिम स्वर से आरम्भ होने वाले ऋज्-अंश का लोप करता है; इसे ऋ् और ज् की संयुक्त Varna-श्रृंखला के लोप के रूप में रखा गया है। फलतः मृज् से म् शेष रहता है और म् + अल से मल बनता है। वृत्ति ‘मलम्’ देती है, इसलिए नपुंसकलिङ्ग सुरक्षित है; कोई अतिरिक्त कोशार्थ नहीं गढ़ा गया।",
)

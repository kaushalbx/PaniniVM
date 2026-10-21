package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.67: मीनातेरूरन्
object MinaterUranSutra : UnadiSutra(
    number = "1.67",
    text = "मीनातेरूरन्",
    roots = setOf(KryadiDhatus.all.single { it.id == "09.0004" }),
    pratyaya = "ऊरन्",
    pratyayaSurface = "ऊर",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("मीञ्" to Samjna.Rudhi("मयूर")),
    rootMorphology = mapOf(
        "मीञ्" to UnadiRootMorphology(
            conditionedStem = "मी".toVarnas(),
            varnaExpansions = listOf(
                UnadiVarnaExpansion(Svara.II, listOf(Svara.A, Vyanjana.YA)),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("मयूर".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.67"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मयूर ऊरन्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है; वृत्ति यहाँ उसका पृथक् अर्थ नहीं बताती।"),
    hindiExplanation = "हिंसार्थक क्र्यादिगणीय मीञ् धातु के मी आधार के बाद ऊरन् प्रत्यय होता है। ऊरन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और ऊर शेष रहता है। मी के ई-वर्ण को पहले गुण से ए और स्वरादि प्रत्यय के सामने ए को अय् प्राप्त होने की संयुक्त नियमित प्रक्रिया को model में ई के अय्-वर्ण-विस्तार के रूप में सुरक्षित किया गया है। इस प्रकार मी से मय् आधार बनता है और मय् + ऊर से मयूर प्रातिपदिक सिद्ध होता है। वृत्ति केवल ‘मयूरः’ रूप देती है, इसलिए पुल्लिङ्ग रखा गया है; यहाँ न दिया गया परिचित पक्षी-अर्थ अनुमान से नहीं जोड़ा गया।",
)

package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

// 1.43: मसेश्च
object MaseshCaSutra : UnadiSutra(
    number = "1.43",
    text = "मसेश्च",
    roots = setOf(DivadiDhatus.all.single { it.id == "04.0131" }),
    pratyaya = "उरन्",
    pratyayaSurface = "उर",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("मसीँ" to Samjna.Rudhi("मसुर")),
    rootMeaningMap = mapOf("मसीँ" to Artha.Explanation("वेश्या अथवा व्रीहिभेद")),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "मसुर".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("वेश्या अथवा व्रीहिभेद"),
            attributedSutra = "1.43",
        ),
        UnadiLexicalAttestation(
            form = "मसुरा".toVarnas(),
            lingas = setOf(Linga.STRI),
            meaning = Artha.Explanation("वेश्या अथवा व्रीहिभेद"),
            attributedSutra = "1.43",
        ),
        UnadiLexicalAttestation(
            form = "मसूर".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("वेश्या, व्रीहिभेद अथवा उपधान"),
            attributedSutra = "5.3",
        ),
        UnadiLexicalAttestation(
            form = "मसूरा".toVarnas(),
            lingas = setOf(Linga.STRI),
            meaning = Artha.Explanation("वेश्या अथवा व्रीहिभेद"),
            attributedSutra = "5.3",
        ),
        UnadiLexicalAttestation(
            form = "मसूरी".toVarnas(),
            lingas = setOf(Linga.STRI),
            meaning = Artha.Explanation("पादरोग"),
            attributedSutra = "5.3",
        ),
    ),
    relatedSutraNumbers = setOf("5.3"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मसुर शब्द वेश्या अथवा व्रीहिभेद के अर्थ में प्रयुक्त होता है।"),
    hindiExplanation = "परिणाम अर्थ वाले दिवादिगणीय मसीँ धातु के बाद पूर्वसूत्र से अनुवृत्त उरन् प्रत्यय होता है। प्रत्यय का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और उर शेष रहता है। मस् + उर से मसुर प्रातिपदिक सिद्ध होता है; वृत्ति इसे वेश्या और व्रीहिभेद अर्थों में, पुल्लिङ्ग तथा मसुरा स्त्रीलिङ्ग रूप में, प्रमाणित करती है। " +
        "वृत्ति साथ ही पञ्चमपाद के ५.३ ‘मसेरूरन्’ का स्पष्ट निर्देश करती है। उस पृथक् ऊरन् प्रत्यय से मसूर बनता है; उसके मसूर, मसूरा और मसूरी रूपों के अर्थ क्रमशः वेश्या अथवा व्रीहिभेद, उपधान, तथा पादरोग के रूप में उद्धृत हैं। इन दीर्घ-ऊ रूपों को १.४३ का प्रत्यक्ष उरन्-फल नहीं बनाया गया, बल्कि ५.३ से attributed lexical attestations के रूप में रखा गया है।",
)

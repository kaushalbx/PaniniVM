package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.59: कुम्बेर्नलोपश्च
object KumberNaLopashCaSutra : UnadiSutra(
    number = "1.59",
    text = "कुम्बेर्नलोपश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0492" }),
    pratyaya = "एरक्",
    pratyayaSurface = "एर",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf("कुबिँ" to Samjna.Rudhi("कुबेर")),
    rootMorphology = mapOf(
        "कुबिँ" to UnadiRootMorphology(
            conditionedStem = "कुम्ब्".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.MA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "कुबेर".toVarnas(),
            lingas = setOf(Linga.PUMS),
            attributedSutra = "1.59",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कुबेर इस सूत्र से सिद्ध एरक्-प्रत्ययान्त प्रातिपदिक है; इस प्रविष्टि में उसका पृथक् अर्थ नहीं बताया गया।"),
    hindiExplanation = "छादनार्थक भ्वादिगणीय कुबिँ धातु का धातुपाठीय आधार कुम्ब् है। उसके बाद पिछले सूत्र से अनुवृत्त एरक् प्रत्यय होता है; अन्तिम क् इत्संज्ञक होकर लुप्त होता है और एर शेष रहता है। सूत्र का ‘नलोपश्च’ इदित् धातु से प्राप्त नुम् के नासिक्य प्रभाव का लोप करता है; लिखित कुम्ब् आधार में वही प्रभाव म्-वर्ण के रूप में उपस्थित है। इसलिए model में कुम्ब् से म् का Varna-लोप रखकर कुब् आधार बनाया गया है। कुब् + एर से कुबेर प्रातिपदिक सिद्ध होता है। वृत्ति केवल ‘कुबेरः’ रूप देती है, अतः पुल्लिङ्ग रखा गया है लेकिन इस प्रविष्टि में न दिए गए देवता अथवा धनाध्यक्ष आदि अर्थ अनुमान से नहीं जोड़े गए।",
)

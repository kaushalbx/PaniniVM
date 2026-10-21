package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.68: स्यन्देः संप्रसारणं च
object SyandehSamprasaranamCaUranSutra : UnadiSutra(
    number = "1.68",
    text = "स्यन्देः संप्रसारणं च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0865" }),
    pratyaya = "ऊरन्",
    pratyayaSurface = "ऊर",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("स्यन्दूँ" to Samjna.Rudhi("सिन्दूर")),
    rootMorphology = mapOf(
        "स्यन्दूँ" to UnadiRootMorphology(
            conditionedStem = "स्यन्द्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.YA, Svara.I)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("सिन्दूर".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.68"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("सिन्दूर ऊरन्-प्रत्ययान्त नपुंसकलिङ्ग प्रातिपदिक है; वृत्ति यहाँ उसका पृथक् अर्थ नहीं बताती।"),
    hindiExplanation = "प्रस्रवणार्थक भ्वादिगणीय स्यन्दूँ धातु के स्यन्द् आधार के बाद पिछले सूत्र से ऊरन् प्रत्यय की अनुवृत्ति होती है। ऊरन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है और ऊर शेष रहता है। सूत्र का ‘संप्रसारणम्’ स्यन्द् के य् को उसके सम्प्रसारण-स्वर इ से बदलता है; इस स्पष्ट Varna-आदेश से स्यन्द् आधार सिन्द् बनता है। सिन्द् + ऊर से सिन्दूर प्रातिपदिक सिद्ध होता है। वृत्ति ‘सिन्दूरम्’ रूप देती है, इसलिए नपुंसकलिङ्ग रखा गया है; वृत्ति में अभाव के कारण इसका परिचित द्रव्य अथवा वर्णार्थ इस प्रविष्टि में नहीं जोड़ा गया।",
)

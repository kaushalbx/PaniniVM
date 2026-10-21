package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.73: चायः किः
object CayahKihSutra : UnadiSutra(
    number = "1.73",
    text = "चायः किः",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1023" }),
    pratyaya = "तु",
    pratyayaSurface = "तु",
    itMarkers = setOf(ItMarker.KIT),
    blocksItAgama = true,
    rootSamjnaMap = mapOf("चायृँ" to Samjna.Rudhi("केतु")),
    rootMeaningMap = mapOf("चायृँ" to Artha.Explanation("ग्रह अथवा पताका")),
    rootMorphology = mapOf(
        "चायृँ" to UnadiRootMorphology(
            conditionedStem = "चाय्".toVarnas(),
            stemAdesha = "कि".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("केतु".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("ग्रह अथवा पताका"), "1.73"),
    ),
    relatedSutraNumbers = setOf("7.2.9"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("केतु कित् तु-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और ग्रह अथवा पताका का वाचक है।"),
    hindiExplanation = "पूजा और निशामन अर्थ वाले भ्वादिगणीय चायृँ धातु के चाय् आधार के बाद कित् तु प्रत्यय होता है और 7.2.9 के अनुसार इट्-आगम नहीं होता। सूत्र का ‘किः’ चाय्-प्रकृति के स्थान पर कि-आदेश बताता है; इसे पूर्ण stemAdesha के रूप में रखा गया है, न कि चाय् के वर्णों पर कोई अप्रमाणित क्रमिक परिवर्तन मानकर। आदेश के इ-वर्ण का गुण ए होता है और के + तु से केतु प्रातिपदिक सिद्ध होता है। वृत्ति ‘केतुर्ग्रहपताकयोः’ कहती है, इसलिए पुल्लिङ्ग केतु को ग्रह और पताका—इन्हीं दो अर्थों में रखा गया है।",
)

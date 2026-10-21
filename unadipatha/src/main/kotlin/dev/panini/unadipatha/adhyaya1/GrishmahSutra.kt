package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra

private val sutra147Gras = BhvadiDhatus.all.single { it.id == "01.0717" }

// 1.147: ग्रीष्मः
object GrishmahSutra : UnadiSutra(
    number = "1.147",
    text = "ग्रीष्मः",
    roots = setOf(sutra147Gras),
    pratyaya = "मक्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(sutra147Gras.upadesha to Samjna.Rudhi("ग्रीष्म")),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("ग्रीष्म".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.147"),
    ),
    relatedSutraNumbers = setOf("1.142"),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "अदनार्थक भ्वादिगणीय ग्रसुँ धातु से ग्रीष्म रूप इस सूत्र द्वारा निपातित होता है। पिछले मक्-विधान से प्रत्यय मक् है; उसका अन्तिम क् इत्संज्ञक होकर लुप्त होने पर म श्रव्य रहता है। वृत्ति केवल ‘ग्रसतेर्निपातोऽयम्’ कहती है और ग्रस् से ग्रीष् बनने के पृथक् स्वरादेश, षत्व अथवा अन्य मध्यवर्ती चरण नहीं बताती। इसलिए अप्रमाणित Varna-परिवर्तन गढ़ने के बजाय प्रमाणित अंतिम प्रातिपदिक ग्रीष्म को `isNipatita` के साथ सुरक्षित किया गया है। सूत्र ‘ग्रीष्मः’ रूप देता है, अतः पुल्लिङ्ग रखा गया है; इस स्रोतांश में ऋतु आदि कोई कोशार्थ नहीं दिया गया, इसलिए वह परिचित अर्थ भी अनुमान से नहीं जोड़ा गया।",
)

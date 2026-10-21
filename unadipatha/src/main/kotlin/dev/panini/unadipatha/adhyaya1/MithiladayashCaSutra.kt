package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSecondaryDerivative
import dev.panini.unadipatha.UnadiSutra

// 1.57: मिथिलादयश्च
object MithiladayashCaSutra : UnadiSutra(
    number = "1.57",
    text = "मिथिलादयश्च",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.1008" },
        BhvadiDhatus.all.single { it.id == "01.0982" },
    ),
    pratyaya = "इलच्",
    pratyayaSurface = "इल",
    itMarkers = setOf(ItMarker.CIT, ItMarker.KIT),
    rootSamjnaMap = mapOf(
        "मिथृँ" to Samjna.Rudhi("मिथिल"),
        "पथेँ" to Samjna.Rudhi("पथिल"),
    ),
    rootMeaningMap = mapOf(
        "मिथृँ" to Artha.Explanation("मिथिला नगरी; जहाँ शत्रु मथे जाते हैं"),
        "पथेँ" to Artha.Explanation("पथिक अथवा यात्री"),
    ),
    secondaryDerivatives = listOf(
        UnadiSecondaryDerivative(
            base = "मिथिल".toVarnas(),
            affix = "टाप्",
            output = "मिथिला".toVarnas(),
            meaning = Artha.Explanation("नगरी"),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "मिथिला".toVarnas(),
            lingas = setOf(Linga.STRI),
            meaning = Artha.Explanation("नगरी; जहाँ शत्रु मथे जाते हैं"),
            attributedSutra = "1.57",
        ),
        UnadiLexicalAttestation(
            form = "पथिल".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("पथिक अथवा यात्री"),
            attributedSutra = "1.57",
        ),
    ),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मिथिला नगरी का नाम है और पथिल पथिक अर्थात् यात्री का वाचक है।"),
    hindiExplanation = "‘मिथिलादयश्च’ पूर्वसूत्र के इलच् तथा कित्त्व के अधिकार में मिथिल आदि रूपों का निपातन करता है। मेधा और हिंसा अर्थ वाले मिथृँ धातु में इल जोड़ने से मिथिल आधार बनता है; स्त्रीप्रत्यय टाप् से मिथिला रूप सिद्ध होता है। वृत्ति ‘मथ्यन्तेऽत्र रिपवः’—जहाँ शत्रु मथे जाते हैं—इस व्युत्पत्तिपरक अर्थ के साथ मिथिला को नगरी बताती है। गत्यर्थक पथेँ धातु में इल जोड़ने से पथिल बनता है। वृत्ति के बाद आया ‘पथिकः’ कोई दूसरा इलच्-प्रत्ययान्त रूप नहीं, बल्कि पथिल का अर्थ है; कोशीय प्रमाण भी पथिल को पथिक/यात्री कहता है। इसलिए model में पथिल ही उणादि output है और पथिक को उसके अर्थ के रूप में रखा गया है, जिससे अप्रमाणित ल् → क् परिवर्तन नहीं गढ़ा गया।",
)

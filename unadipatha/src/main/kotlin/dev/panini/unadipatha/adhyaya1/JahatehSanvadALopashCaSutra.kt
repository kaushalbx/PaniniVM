package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSanvatTreatment
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

private val sutra138Ha = JuhotyadiDhatus.all.single { it.id == "03.0009" }

// 1.138: जहातेः सन्वदालोपश्च
object JahatehSanvadALopashCaSutra : UnadiSutra(
    number = "1.138",
    text = "जहातेः सन्वदालोपश्च",
    roots = setOf(sutra138Ha),
    pratyaya = "मन्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.N),
    rootSamjnaMap = mapOf(sutra138Ha.upadesha to Samjna.Rudhi("जिह्म")),
    rootMeaningMap = mapOf(
        sutra138Ha.upadesha to Artha.Explanation("टेढ़ा अथवा मन्द"),
    ),
    rootMorphology = mapOf(
        sutra138Ha.upadesha to UnadiRootMorphology(
            conditionedStem = "जिहा".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.AA)),
        ),
    ),
    sanvatTreatment = UnadiSanvatTreatment(),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "जिह्म".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("टेढ़ा अथवा मन्द"),
            attributedSutra = "1.138",
        ),
    ),
    relatedSutraNumbers = setOf("1.137"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("जिह्म मन्-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और कुटिल अथवा मन्द का वाचक है।"),
    hindiExplanation = "त्यागार्थक जुहोत्यादिगणीय ओँहाक् धातु के जहाति-रूप से यह सूत्र धातु को पहचानता है। पिछले सूत्र १.१३७ से मन् प्रत्यय आता है; उसके अन्तिम न् की इत्संज्ञा और लोप से श्रव्य प्रत्यय म रहता है। ‘सन्वत्’ के विधान से वास्तविक सन् प्रत्यय लगाए बिना सन् के समान अभ्यासादि कार्य होते हैं और हा का जिहा आधार बनता है। सूत्र का ‘आलोपश्च’ इसी आधार के अन्तिम आ-वर्ण का स्पष्ट लोप करता है: जिहा → जिह्। फिर जिह् + म से जिह्म प्रातिपदिक सिद्ध होता है। धातुपाठ में हा-रूप वाली दो प्रविष्टियाँ हैं, किन्तु ‘जहाति’ परस्मैपदी त्यागार्थक ओँहाक् की पहचान है; इसलिए गतिअर्थक आत्मनेपदी ओँहाङ् नहीं ली गई। वृत्ति ‘जिह्मः कुटिलमन्दयोः’ कहती है, अतः पुल्लिङ्ग और टेढ़ा तथा मन्द—दोनों अर्थ ही सुरक्षित किए गए हैं; कोई अतिरिक्त कोशार्थ नहीं जोड़ा गया।",
)

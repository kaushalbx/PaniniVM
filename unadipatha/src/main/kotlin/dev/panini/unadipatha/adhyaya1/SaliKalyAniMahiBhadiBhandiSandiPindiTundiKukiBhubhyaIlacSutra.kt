package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.54: सलिकल्यनिमहिभडिभण्डिशण्डिपिण्डितुण्डिकुकिभूभ्य इलच्
object SaliKalyAniMahiBhadiBhandiSandiPindiTundiKukiBhubhyaIlacSutra : UnadiSutra(
    number = "1.54",
    text = "सलिकल्यनिमहिभडिभण्डिशण्डिपिण्डितुण्डिकुकिभूभ्य इलच्",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0253" },
        BhvadiDhatus.all.single { it.id == "01.0570" },
        AdadiDhatus.all.single { it.id == "02.0065" },
        BhvadiDhatus.all.single { it.id == "01.0831" },
        BhvadiDhatus.all.single { it.id == "01.0306" },
        BhvadiDhatus.all.single { it.id == "01.0313" },
        BhvadiDhatus.all.single { it.id == "01.0307" },
        BhvadiDhatus.all.single { it.id == "01.0309" },
        BhvadiDhatus.all.single { it.id == "01.0096" },
        BhvadiDhatus.all.single { it.id == "01.0001" },
        TudadiDhatus.all.single { it.id == "06.0093" },
    ),
    rootReferences = setOf("भड"),
    pratyaya = "इलच्",
    pratyayaSurface = "इल",
    itMarkers = setOf(ItMarker.CIT),
    rootSamjnaMap = mapOf(
        "षलृँ" to Samjna.Rudhi("सलिल"),
        "कलँ" to Samjna.Rudhi("कलिल"),
        "अनँ" to Samjna.Rudhi("अनिल"),
        "महँ" to Samjna.Rudhi("महिला"),
        "भड" to Samjna.Rudhi("भडिल"),
        "भडिँ" to Samjna.Rudhi("भण्डिल"),
        "शडिँ" to Samjna.Rudhi("शण्डिल"),
        "पिडिँ" to Samjna.Rudhi("पिण्डिल"),
        "तुडिँ" to Samjna.Rudhi("तुण्डिल"),
        "कुकँ" to Samjna.Rudhi("कोकिल"),
        "भू" to Samjna.Rudhi("भविल"),
    ),
    rootMeaningMap = mapOf(
        "षलृँ" to Artha.Explanation("जल; नीचे की ओर जाने वाला"),
        "भड" to Artha.Explanation("शूर अथवा सेवक"),
        "भडिँ" to Artha.Explanation("दूत अथवा कल्याण"),
        "शडिँ" to Artha.Explanation("मुनि"),
        "पिडिँ" to Artha.Explanation("गणक"),
        "तुडिँ" to Artha.Explanation("मुखर व्यक्ति"),
        "भू" to Artha.Explanation("भव्य"),
    ),
    rootMorphology = mapOf(
        "कुकँ" to UnadiRootMorphology(
            conditionedStem = "कुक्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.U, Svara.O)),
        ),
        "भू" to UnadiRootMorphology(
            conditionedStem = "भू".toVarnas(),
            varnaExpansions = listOf(
                UnadiVarnaExpansion(Svara.UU, listOf(Svara.A, Vyanjana.VA)),
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            sourceRoot = "महँ",
            output = "महेला".toVarnas(),
            morphology = UnadiRootMorphology(
                conditionedStem = "मह्".toVarnas(),
                pratyayaVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
            ),
        ),
        UnadiDerivedVariant(
            sourceRoot = "कुटँ",
            output = "कुटिल".toVarnas(),
            morphology = UnadiRootMorphology(conditionedStem = "कुट्".toVarnas()),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("सलिल".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("जल; नीचे की ओर जाने वाला"), "1.54"),
        UnadiLexicalAttestation("कलिल".toVarnas(), attributedSutra = "1.54"),
        UnadiLexicalAttestation("अनिल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.54"),
        UnadiLexicalAttestation("महिला".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.54"),
        UnadiLexicalAttestation("महेला".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.54"),
        UnadiLexicalAttestation("भडिल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("शूर अथवा सेवक"), "1.54"),
        UnadiLexicalAttestation("भण्डिल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("दूत"), "1.54"),
        UnadiLexicalAttestation("भण्डिल".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("कल्याण"), "1.54"),
        UnadiLexicalAttestation("शण्डिल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("मुनि"), "1.54"),
        UnadiLexicalAttestation("पिण्डिल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("गणक"), "1.54"),
        UnadiLexicalAttestation("तुण्डिल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("मुखर व्यक्ति"), "1.54"),
        UnadiLexicalAttestation("कोकिल".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.54"),
        UnadiLexicalAttestation("भविल".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("भव्य"), "1.54"),
        UnadiLexicalAttestation("कुटिल".toVarnas(), attributedSutra = "1.54"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("सलिल से भविल तक इलच्-प्रत्ययान्त रूप तथा बाहुलक से कुटिल इस नियम में स्रोतगत रूप और अर्थ के अनुसार संगृहीत हैं।"),
    hindiExplanation = "सूत्र में सलि, कलि, अनि, महि, सौत्र भडि, भण्डि, शण्डि, पिण्डि, तुण्डि, कुकि और भू—इन आधारों के बाद इलच् प्रत्यय होता है। इलच् का अन्तिम च् इत्संज्ञक होकर लुप्त होता है और इल शेष रहता है। षल् के मूर्धन्य ष का धातु-रूप में दन्त्य स होकर सल् + इल से सलिल बनता है; वृत्ति इसकी व्युत्पत्ति ‘नीचे की ओर जाता है’ कहकर जल के निम्नगामी स्वभाव से जोड़ती है। कलिल, अनिल, महिला, भडिल, भण्डिल, शण्डिल, पिण्डिल और तुण्डिल अपने दिए हुए आधारों में इल जोड़ने से बनते हैं। ‘भड’ को वृत्ति स्पष्टतः सौत्र धातु कहती है, इसलिए उसे समान दिखने वाले भण्ड् धातु में नहीं मिलाया गया है। कुक् के उकार का गुण ओ होकर कोक् + इल से कोकिल बनता है। भू के ऊकार का गुण ओ और फिर अयवायावादेश से अव्-क्रम होकर भव् + इल से भविल बनता है; model में इसका संयुक्त फल ऊ → अव् Varna-expansion है। पृषोदरादि अपवाद से महिला के साथ महेला पाठ भी मिलता है, जिसमें प्रत्यय के इ का एत्त्व अलग variant में रखा गया है। बाहुलक से कुटँ कौटिल्ये धातु के बाद इलच् होकर कुटिल भी मिलता है। वृत्ति के स्पष्ट अर्थ ही जोड़े गए हैं—भडिल शूर/सेवक, भण्डिल दूत अथवा नपुंसकलिङ्ग में कल्याण, शण्डिल मुनि, पिण्डिल गणक, तुण्डिल मुखर और भविल भव्य। जिन रूपों का अर्थ इस प्रविष्टि में नहीं बताया गया, उनके अर्थ अनुमान से नहीं भरे गए हैं।",
)

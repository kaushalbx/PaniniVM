package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra141Av = BhvadiDhatus.all.single { it.id == "01.0684" }
private val sutra141Siv = DivadiDhatus.all.single { it.id == "04.0002" }
private val sutra141Si = SvadiDhatus.all.single { it.id == "05.0002" }
private val sutra141Shush = DivadiDhatus.all.single { it.id == "04.0080" }

// 1.141: अविसिविसिशुषिभ्यः कित्
object AviSiviSiShushibhyahKitSutra : UnadiSutra(
    number = "1.141",
    text = "अविसिविसिशुषिभ्यः कित्",
    roots = setOf(sutra141Av, sutra141Siv, sutra141Si, sutra141Shush),
    pratyaya = "मन्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.N, ItMarker.KIT),
    rootSamjnaMap = mapOf(
        sutra141Av.upadesha to Samjna.Rudhi("ऊम"),
        sutra141Siv.upadesha to Samjna.Rudhi("स्यूम"),
        sutra141Si.upadesha to Samjna.Rudhi("सिम"),
        sutra141Shush.upadesha to Samjna.Rudhi("शुष्म"),
    ),
    rootMeaningMap = mapOf(
        sutra141Av.upadesha to Artha.Explanation("नगर"),
        sutra141Siv.upadesha to Artha.Explanation("किरण"),
        sutra141Si.upadesha to Artha.Explanation("सर्व"),
        sutra141Shush.upadesha to Artha.Explanation("अग्नि अथवा समीप; परम्परागत पाठान्तर में समीर"),
    ),
    rootMorphology = mapOf(
        sutra141Av.upadesha to UnadiRootMorphology(
            conditionedStem = "अव्".toVarnas(),
            stemAdesha = "ऊ".toVarnas(),
        ),
        sutra141Siv.upadesha to UnadiRootMorphology(
            conditionedStem = "सिव्".toVarnas(),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Svara.I, Vyanjana.YA),
                UnadiVarnaSubstitution(Vyanjana.VA, Svara.UU),
            ),
        ),
        sutra141Si.upadesha to UnadiRootMorphology(conditionedStem = "सि".toVarnas()),
        sutra141Shush.upadesha to UnadiRootMorphology(conditionedStem = "शुष्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("ऊम".toVarnas(), setOf(Linga.NAPUMSAKA), Artha.Explanation("नगर"), "1.141"),
        UnadiLexicalAttestation("स्यूम".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("किरण"), "1.141"),
        UnadiLexicalAttestation("सिम".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("सर्व"), "1.141"),
        UnadiLexicalAttestation(
            "शुष्म".toVarnas(),
            setOf(Linga.NAPUMSAKA),
            Artha.Explanation("अग्नि अथवा समीप; परम्परागत पाठान्तर में समीर"),
            "1.141",
        ),
    ),
    relatedSutraNumbers = setOf("1.137", "6.4.20"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("ऊम, स्यूम, सिम और शुष्म कित् मन्-प्रत्ययान्त प्रातिपदिक हैं।"),
    hindiExplanation = "अवँ, षिवुँ, षिञ् और शुषँ—इन चार धातुओं के बाद १.१३७ से मन् प्रत्यय आता है और यह सूत्र उसे कित् करता है। मन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है तथा म श्रव्य रहता है; कित्त्व धातु के सामान्य गुण को रोकता है। अव् पर ६.४.२० के ऊठ्-विधान का संयुक्त फल ऊ आधार है, इसलिए उसे अप्रामाणिक छोटे चरणों में तोड़ने के बजाय ठीक ऊ stem-आदेश रखा गया है; ऊ + म से ऊम ‘नगर’ बनता है। षिवुँ के सिव् आधार में व् के स्थान पर ऊठ् का ऊ आने पर पूर्व इ का यण् य् होता है; इन दोनों को इ → य् और व् → ऊ के स्पष्ट Varna-आदेशों से रखकर स्यू + म से स्यूम ‘किरण’ सिद्ध किया गया है। षिञ् के सि आधार में कित्त्व से गुण न होने पर सि + म से सिम ‘सर्व’ और शुष् + म से शुष्म बनता है। वेबसाइट-वृत्ति शुष्म के लिए ‘अग्निसमीपयोः’ पढ़ती है, इसलिए अग्नि और समीप अर्थ अक्षुण्ण रखे गए हैं; पारम्परिक मुद्रित पाठ में ‘अग्निसमीरयोः’ मिलता है, अतः समीर को केवल पाठान्तर के रूप में स्पष्ट किया गया है, मूल वेबसाइट-पाठ को चुपचाप बदला नहीं गया। वृत्ति के उद्धरणानुसार ऊम तथा शुष्म नपुंसकलिङ्ग और स्यूम तथा सिम पुल्लिङ्ग हैं।",
)

package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.divadi.JanDhatu
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra157Shvi = BhvadiDhatus.all.single { it.id == "01.1165" }
private val sutra157Plih = BhvadiDhatus.all.single { it.id == "01.0729" }
private val sutra157Klid = DivadiDhatus.all.single { it.id == "04.0157" }
private val sutra157Snih = DivadiDhatus.all.single { it.id == "04.0097" }
private val sutra157Muh = DivadiDhatus.all.single { it.id == "04.0095" }
private val sutra157Masj = TudadiDhatus.all.single { it.id == "06.0151" }
private val sutra157Ma = JuhotyadiDhatus.all.single { it.id == "03.0007" }
private val sutra157Psa = AdadiDhatus.all.single { it.id == "02.0050" }
private val sutra157Jan = JanDhatu()
private val sutra157Mah = CuradiDhatus.all.single { it.id == "10.0406" }

// 1.157: श्वन्नुक्षन्पूषन्प्लीहन्क्लेदन्स्नेहन्मूर्धन्मज्जन्नर्यमन्विश्वप्सन्परिज्जन्मातरिश्वन्मघवन्निति
object ShvanUkshanPushanPlihanKledanSnehanMurdhanMajjanAryamanVishvapsanParijmanMatarishvanMaghavanItiSutra : UnadiSutra(
    number = "1.157",
    text = "श्वन्नुक्षन्पूषन्प्लीहन्क्लेदन्स्नेहन्मूर्धन्मज्जन्नर्यमन्विश्वप्सन्परिज्जन्मातरिश्वन्मघवन्निति",
    roots = setOf(
        sutra157Shvi, sutra157Plih, sutra157Klid, sutra157Snih, sutra157Muh,
        sutra157Masj, sutra157Ma, sutra157Psa, sutra157Jan, sutra157Mah,
    ),
    rootReferences = setOf("उक्ष्", "पूष्"),
    pratyaya = "कनिन्",
    pratyayaSurface = "अन्",
    itMarkers = setOf(ItMarker.KIT, ItMarker.I),
    rootSamjnaMap = mapOf(
        sutra157Shvi.upadesha to Samjna.Rudhi("श्वन्"),
        "उक्ष्" to Samjna.Rudhi("उक्षन्"),
        "पूष्" to Samjna.Rudhi("पूषन्"),
        sutra157Plih.upadesha to Samjna.Rudhi("प्लीहन्"),
        sutra157Klid.upadesha to Samjna.Rudhi("क्लेदन्"),
        sutra157Snih.upadesha to Samjna.Rudhi("स्नेहन्"),
        sutra157Muh.upadesha to Samjna.Rudhi("मूर्धन्"),
        sutra157Masj.upadesha to Samjna.Rudhi("मज्जन्"),
        sutra157Ma.upadesha to Samjna.Rudhi("अर्यमन्"),
        sutra157Psa.upadesha to Samjna.Rudhi("विश्वप्सन्"),
        sutra157Jan.upadesha to Samjna.Rudhi("परिज्मन्"),
        sutra157Mah.upadesha to Samjna.Rudhi("मघवन्"),
    ),
    rootMeaningMap = mapOf(
        sutra157Plih.upadesha to Artha.Explanation("कुक्षि का रोग"),
        sutra157Klid.upadesha to Artha.Explanation("चन्द्र"),
        sutra157Snih.upadesha to Artha.Explanation("सुहृद् अथवा चन्द्र"),
        sutra157Muh.upadesha to Artha.Explanation("जिस पर आघात होने पर मोह होता है"),
        sutra157Masj.upadesha to Artha.Explanation("अस्थिसार"),
        sutra157Psa.upadesha to Artha.Explanation("अग्नि"),
        sutra157Jan.upadesha to Artha.Explanation("चन्द्र अथवा अग्नि"),
        sutra157Mah.upadesha to Artha.Explanation("इन्द्र"),
    ),
    rootMorphology = mapOf(
        sutra157Shvi.upadesha to UnadiRootMorphology(
            conditionedStem = "श्वि".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.I)),
        ),
        "उक्ष्" to UnadiRootMorphology(conditionedStem = "उक्ष्".toVarnas()),
        "पूष्" to UnadiRootMorphology(conditionedStem = "पूष्".toVarnas()),
        sutra157Plih.upadesha to UnadiRootMorphology(
            conditionedStem = "प्लिह्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.II)),
        ),
        sutra157Klid.upadesha to UnadiRootMorphology(
            conditionedStem = "क्लिद्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
        ),
        sutra157Snih.upadesha to UnadiRootMorphology(
            conditionedStem = "स्निह्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
        ),
        sutra157Muh.upadesha to UnadiRootMorphology(
            conditionedStem = "मुह्".toVarnas(),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Svara.U, Svara.UU),
                UnadiVarnaSubstitution(Vyanjana.HA, Vyanjana.DHA),
            ),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.RA), UnadiAgamaPlacement.AFTER_FINAL_ROOT_VOWEL, "रम्"),
            ),
        ),
        sutra157Masj.upadesha to UnadiRootMorphology(conditionedStem = "मज्ज्".toVarnas()),
        sutra157Ma.upadesha to UnadiRootMorphology(
            conditionedStem = "मा".toVarnas(),
            requiredUpapadas = setOf("अर्य".toVarnas()),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.AA)),
        ),
        sutra157Psa.upadesha to UnadiRootMorphology(
            conditionedStem = "प्सा".toVarnas(),
            requiredUpapadas = setOf("विश्व".toVarnas()),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.AA)),
        ),
        sutra157Jan.upadesha to UnadiRootMorphology(
            conditionedStem = "जन्".toVarnas(),
            requiredUpasargas = setOf("परि".toVarnas()),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.A)),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.MA)),
        ),
        sutra157Mah.upadesha to UnadiRootMorphology(
            conditionedStem = "मह्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.HA, Vyanjana.GHA)),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.VA), UnadiAgamaPlacement.BEFORE_SUFFIX, "वुक्"),
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "अर्यमन्".toVarnas(),
            upapada = "अर्य".toVarnas(),
            sourceRoot = sutra157Ma.upadesha,
        ),
        UnadiDerivedVariant(
            output = "विश्वप्सन्".toVarnas(),
            meaning = Artha.Explanation("अग्नि"),
            upapada = "विश्व".toVarnas(),
            sourceRoot = sutra157Psa.upadesha,
        ),
        UnadiDerivedVariant(
            upasarga = "परि".toVarnas(),
            output = "परिज्मन्".toVarnas(),
            meaning = Artha.Explanation("चन्द्र अथवा अग्नि"),
            sourceRoot = sutra157Jan.upadesha,
        ),
        UnadiDerivedVariant(
            output = "मातरिश्वन्".toVarnas(),
            upapada = "मातरि".toVarnas(),
            sourceRoot = sutra157Shvi.upadesha,
            morphology = UnadiRootMorphology(varnaLopas = listOf(UnadiVarnaLopa(Svara.I))),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("श्वा".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.157"),
        UnadiLexicalAttestation("उक्षा".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.157"),
        UnadiLexicalAttestation("पूषा".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.157"),
        UnadiLexicalAttestation("प्लीहा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कुक्षि का रोग"), "1.157"),
        UnadiLexicalAttestation("क्लेदा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("चन्द्र"), "1.157"),
        UnadiLexicalAttestation("स्नेहा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("सुहृद् अथवा चन्द्र"), "1.157"),
        UnadiLexicalAttestation("मूर्धा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("जिस पर आघात होने पर मोह होता है"), "1.157"),
        UnadiLexicalAttestation("मज्जा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("अस्थिसार"), "1.157"),
        UnadiLexicalAttestation("अर्यमा".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.157"),
        UnadiLexicalAttestation("विश्वप्सा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("अग्नि"), "1.157"),
        UnadiLexicalAttestation("परिज्मा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("चन्द्र अथवा अग्नि"), "1.157"),
        UnadiLexicalAttestation("मातरिश्वा".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.157"),
        UnadiLexicalAttestation("मघवा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("इन्द्र"), "1.157"),
    ),
    relatedSutraNumbers = setOf("1.154"),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "यह सूत्र श्वन्, उक्षन्, पूषन्, प्लीहन्, क्लेदन्, स्नेहन्, मूर्धन्, मज्जन्, अर्यमन्, विश्वप्सन्, परिज्मन्, मातरिश्वन् और मघवन्—इन तेरह कनिन्-प्रत्ययान्त प्रातिपदिकों को निपातित करता है। कनिन् के क् और इ इत्संज्ञक होकर लुप्त होते हैं तथा अन् शेष रहता है; उद्धृत प्रथमा-एकवचन रूप क्रमशः श्वा आदि आकारान्त दिखाई देते हैं। श्वि के इ-लोप से श्वन् बनता है; उक्षन् और पूषन् के लिए वृत्ति कोई पृथक् धातु-क्रिया नहीं बताती, अतः केवल पाठगत धातु-सन्दर्भ रखे गए हैं। गतिवाचक प्लिहँ के इ का दीर्घ ई होकर प्लीहन् ‘कुक्षिरोग’ बनता है। क्लिदूँ और ष्णिहँ के इ का गुण ए होकर क्लेदन् ‘चन्द्र’ तथा स्नेहन् ‘सुहृद् अथवा चन्द्र’ बनते हैं। मुहँ में उपधा उ का दीर्घ ऊ, अन्तिम ह् का ध् आदेश और अन्तिम मूलस्वर के बाद रम् आगम का प्रभावी र् जोड़ने पर मूर्धन् बनता है। टुमस्जोँ के मज्ज् आधार से मज्जन् ‘अस्थिसार’ है। अर्य उपपदपूर्वक माङ् के आकार-लोप से अर्यमन्, विश्व उपपदपूर्वक प्सा के आकार-लोप से अग्नि-अर्थक विश्वप्सन्, और परि-पूर्वक जनीँ में उपधा अ-लोप तथा अन्तिम न् के म् आदेश से चन्द्र अथवा अग्नि-अर्थक परिज्मन् बनता है। मातरि उपपदपूर्वक श्वि के इ-लोप से मातरिश्वन् बनता है। पूजार्थक मह में ह् का घ् आदेश और वुक् आगम का प्रभावी व् जोड़ने पर इन्द्र-अर्थक मघवन् सिद्ध होता है। प्रत्येक स्पष्ट ध्वनि-क्रिया Varna के रूप में रखी गई है और जिन पदों का अर्थ वृत्ति नहीं बताती, उनके अर्थ अनुमान से नहीं जोड़े गए हैं।",
)

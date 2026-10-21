package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra150In = AdadiDhatus.all.single { it.id == "02.0040" }
private val sutra150Shi = AdadiDhatus.all.single { it.id == "02.0026" }

// 1.150: इण्शीभ्यां वन्
object InShibhyamVanSutra : UnadiSutra(
    number = "1.150",
    text = "इण्शीभ्यां वन्",
    roots = setOf(sutra150In, sutra150Shi),
    pratyaya = "वन्",
    pratyayaSurface = "व",
    itMarkers = setOf(ItMarker.N),
    rootSamjnaMap = mapOf(
        sutra150In.upadesha to Samjna.Rudhi("एव"),
        sutra150Shi.upadesha to Samjna.Rudhi("शेव"),
    ),
    rootMeaningMap = mapOf(
        sutra150In.upadesha to Artha.Explanation("गमन करने वाला"),
    ),
    rootMorphology = mapOf(
        sutra150In.upadesha to UnadiRootMorphology(
            conditionedStem = "इ".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
        ),
        sutra150Shi.upadesha to UnadiRootMorphology(
            conditionedStem = "शी".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.E)),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            output = "एव".toVarnas(),
            sourceRoot = sutra150In.upadesha,
            meaning = Artha.Explanation("असत्त्वार्थ में प्रयुक्त निपात"),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "एव".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("गमन करने वाला; मरुतों के लिए प्रयुक्त पद"),
            attributedSutra = "1.150",
        ),
        UnadiLexicalAttestation(
            form = "एव".toVarnas(),
            meaning = Artha.Explanation("असत्त्वार्थ में प्रयुक्त निपात"),
            attributedSutra = "1.150",
        ),
        UnadiLexicalAttestation(
            form = "शेव".toVarnas(),
            attributedSutra = "1.150",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "इण् (गतौ) और शीङ् (स्वप्ने) धातुओं के बाद वन् प्रत्यय होता है। वन् का अन्तिम न् इत्संज्ञक होकर लुप्त होता है, अतः श्रव्य प्रत्यय व है। इण् के इ का गुण ए और शीङ् के ई का गुण ए—दोनों परिवर्तन स्पष्ट Varna-आदेश के रूप में रखे गए हैं। इस प्रकार इ + व से एव और शी + व से शेव सिद्ध होते हैं। वृत्ति ‘एवो गन्ता’ कहकर एव का कर्तृवाचक प्रयोग तथा ‘ये च एवा मरुतः’ कहकर मरुतों के लिए उसका बहुवचन-प्रयोग दिखाती है। वही एव जब किसी सत्त्व अथवा द्रव्य का वाचक नहीं होता, तब ‘असत्वे निपातोऽयम्’ के अनुसार निपात/अव्यय है; इसलिए कृदन्त और अव्यय—दोनों प्रयोग अलग अभिलेखों में सुरक्षित हैं। ‘शेवं मित्राय वरुणाय’ से शेव का प्रयोग प्रमाणित है, पर वृत्ति उसका स्वतंत्र अर्थ या लिङ्ग स्पष्ट नहीं करती, इसलिए वे अनुमान से नहीं जोड़े गए हैं।",
)

package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.dhatupatha.tudadi.GrDhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra152Shi = AdadiDhatus.all.single { it.id == "02.0026" }
private val sutra152Ya = AdadiDhatus.all.single { it.id == "02.0044" }
private val sutra152Lih = AdadiDhatus.all.single { it.id == "02.0006" }
private val sutra152Gr = GrDhatu()
private val sutra152Ap = SvadiDhatus.all.single { it.id == "05.0016" }

// 1.152: शेवयह्वजिह्वाग्रीवाप्वामीवाः
object ShevaYahvaJihvaGrivaApvaMivahSutra : UnadiSutra(
    number = "1.152",
    text = "शेवयह्वजिह्वाग्रीवाप्वामीवाः",
    roots = setOf(sutra152Shi, sutra152Ya, sutra152Lih, sutra152Gr, sutra152Ap),
    rootReferences = setOf("मी"),
    pratyaya = "वण्",
    pratyayaSurface = "व",
    itMarkers = setOf(ItMarker.N),
    rootSamjnaMap = mapOf(
        sutra152Shi.upadesha to Samjna.Rudhi("शेव"),
        sutra152Ya.upadesha to Samjna.Rudhi("यह्व"),
        sutra152Lih.upadesha to Samjna.Rudhi("जिह्वा"),
        sutra152Gr.upadesha to Samjna.Rudhi("ग्रीवा"),
        sutra152Ap.upadesha to Samjna.Rudhi("आप्वा"),
        "मी" to Samjna.Rudhi("मीवा"),
    ),
    rootMeaningMap = mapOf(
        sutra152Ya.upadesha to Artha.Explanation("जिससे जाया जाता है"),
        sutra152Lih.upadesha to Artha.Explanation("जिह्वा; जिससे चाटा जाता है"),
        sutra152Gr.upadesha to Artha.Explanation("ग्रीवा; जिससे निगला जाता है"),
        sutra152Ap.upadesha to Artha.Explanation("वायु"),
        "मी" to Artha.Explanation("उदरकृमि; अन्य मत में वायु"),
    ),
    rootMorphology = mapOf(
        sutra152Shi.upadesha to UnadiRootMorphology(
            conditionedStem = "शी".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.E)),
            assignedAccent = Accent.UDATTA,
            accentVowelIndex = 1,
        ),
        sutra152Ya.upadesha to UnadiRootMorphology(
            conditionedStem = "या".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.AA, Svara.A)),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.HA), UnadiAgamaPlacement.BEFORE_SUFFIX, "हुग्"),
            ),
        ),
        sutra152Lih.upadesha to UnadiRootMorphology(
            conditionedStem = "लिह्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.LA, Vyanjana.JA)),
        ),
        sutra152Gr.upadesha to UnadiRootMorphology(
            conditionedStem = "ग्र्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Svara.II), UnadiAgamaPlacement.BEFORE_SUFFIX, "ईड्"),
            ),
        ),
        sutra152Ap.upadesha to UnadiRootMorphology(conditionedStem = "आप्".toVarnas()),
        "मी" to UnadiRootMorphology(conditionedStem = "मी".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("शेव".toVarnas(), attributedSutra = "1.152"),
        UnadiLexicalAttestation("यह्व".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("जिससे जाया जाता है"), "1.152"),
        UnadiLexicalAttestation("जिह्वा".toVarnas(), setOf(Linga.STRI), Artha.Explanation("जिससे चाटा जाता है"), "1.152"),
        UnadiLexicalAttestation("ग्रीवा".toVarnas(), setOf(Linga.STRI), Artha.Explanation("जिससे निगला जाता है"), "1.152"),
        UnadiLexicalAttestation("आप्वा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("वायु"), "1.152"),
        UnadiLexicalAttestation("मीवा".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("उदरकृमि; अन्य मत में वायु"), "1.152"),
    ),
    relatedSutraNumbers = setOf("1.150"),
    isNipatita = true,
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "इस सूत्र में वण् प्रत्यय के साथ शेव, यह्व, जिह्वा, ग्रीवा, आप्वा और मीवा रूप निपातित हैं। वण् का अन्तिम ण् इत्संज्ञक होकर लुप्त होता है और व शेष रहता है। शेव को फिर से पढ़ने का प्रयोजन केवल उसका अन्तिम स्वर उदात्त करना है; इसलिए यह अन्तोदात्त विधान केवल शीङ्-शाखा में रखा गया है। शीङ् के ई का गुण ए करके शी → शे और शे + व से शेव बनता है। या धातु के आ का ह्रस्व अ और हुग् आगम का प्रभावी ह् प्रत्यय से पहले जोड़ने पर य + अ + ह् + व से यह्व बनता है—‘जिससे जाया जाता है’। लिहँ में ल् के स्थान पर ज् का स्पष्ट Varna-आदेश होता है और वृत्ति के गुणाभाव से इ यथावत् रहता है; निपातित जिह्वा ‘जिससे चाटा जाता है’ है। निगरणार्थक गॄ के ग्र् आधार और प्रत्यय के बीच ईड् का प्रभावी ई-वर्ण जुड़ने से ग्रीवा बनती है—‘जिससे निगला जाता है’। आपॢँ + व से निपातित आप्वा वायु का वाचक है। मीवा को वृत्ति उदरकृमि और अन्य आचार्यों के मत से वायु बताती है, किन्तु उसके लिए कोई निश्चित धातुपाठ-गण या अतिरिक्त वर्णक्रिया नहीं बताती; इसलिए केवल पाठ में निहित मी धातु-सन्दर्भ रखा गया है, कोई मनमाना गण नहीं चुना गया। जिह्वा, ग्रीवा, आप्वा और मीवा के अन्तिम आकार स्रोत के निपातित रूप का अंग हैं; वृत्ति टाप् आदि पृथक् प्रत्यय नहीं बताती, अतः ऐसा अप्रयुक्त चरण नहीं गढ़ा गया।",
)

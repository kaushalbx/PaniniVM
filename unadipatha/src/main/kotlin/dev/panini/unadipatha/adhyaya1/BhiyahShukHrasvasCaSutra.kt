package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
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

private val sutra135Bhi = JuhotyadiDhatus.all.single { it.id == "03.0002" }

// 1.135: भियः षुक् ह्रस्वश्च
object BhiyahShukHrasvasCaSutra : UnadiSutra(
    number = "1.135",
    text = "भियः षुक् ह्रस्वश्च",
    roots = setOf(sutra135Bhi),
    pratyaya = "अजि",
    pratyayaSurface = "अज्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(sutra135Bhi.upadesha to Samjna.Rudhi("भिषक्")),
    rootMorphology = mapOf(
        sutra135Bhi.upadesha to UnadiRootMorphology(
            conditionedStem = "भी".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.SSA), UnadiAgamaPlacement.BEFORE_SUFFIX, "षुक्"),
            ),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.II, Svara.I)),
            pratyayaVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.KA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("भिषक्".toVarnas(), attributedSutra = "1.135"),
    ),
    relatedSutraNumbers = setOf("1.133"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "भयार्थक ञिभी धातु के भी आधार के बाद १.१३३ से अजि प्रत्यय आता है। अजि का अन्तिम इ इत्संज्ञक होकर लुप्त होता है और अज् शेष रहता है। इस सूत्र से धातु के ई का ह्रस्व इ होता है तथा प्रत्यय से पहले षुक् आगम आता है। षुक् के उ और क् इत्संज्ञक होकर लुप्त होने पर ष्-वर्ण बचता है। इसलिए भि + ष् + अज् बनता है; प्रत्यय-अन्त ज् के क् होने से भिषक् सिद्ध होता है। ई → इ, ष्-आगम और प्रत्यय के ज् → क्—तीनों को पृथक् Varna operations बनाया गया है। वृत्ति केवल ‘भिषक्’ रूप देती है; वह चिकित्सक आदि अर्थ अथवा लिङ्ग यहाँ नहीं बताती, इसलिए परिचित किंतु इस स्रोत में अनुल्लिखित जानकारी अनुमान से नहीं जोड़ी गई।",
)

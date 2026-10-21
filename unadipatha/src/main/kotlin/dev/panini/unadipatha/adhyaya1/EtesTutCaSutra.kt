package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
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

private val sutra130In = AdadiDhatus.all.single { it.id == "02.0040" }

// 1.130: एतेस्तुट् च
object EtesTutCaSutra : UnadiSutra(
    number = "1.130",
    text = "एतेस्तुट् च",
    roots = setOf(sutra130In),
    pratyaya = "अदि",
    pratyayaSurface = "अद्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(sutra130In.upadesha to Samjna.Rudhi("एतद्")),
    rootMorphology = mapOf(
        sutra130In.upadesha to UnadiRootMorphology(
            conditionedStem = "इ".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.TA), UnadiAgamaPlacement.BEFORE_SUFFIX, "तुट्"),
            ),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.I, Svara.E)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            "एतद्".toVarnas(),
            setOf(Linga.PUMS, Linga.STRI, Linga.NAPUMSAKA),
            attributedSutra = "1.130",
        ),
    ),
    relatedSutraNumbers = setOf("1.127"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA, Samjna.SARVANAMA),
    hindiExplanation = "गतिअर्थक इण् धातु के बाद १.१२७ से अदि प्रत्यय आता है और इस सूत्र से उस प्रत्यय के पहले तुट् आगम होता है। इण् का ण् इत्संज्ञक होने से धातु का आधार इ है। इ का गुण ए होता है। तुट् के उ और ट् इत्संज्ञक होकर लुप्त होते हैं, अतः त्-वर्ण बचता है; अदि के इ का भी इत्संज्ञा-लोप होकर अद् शेष रहता है। इसलिए ए + त् + अद् से एतद् बनता है। पिछले सूत्र १.१२९ का डित्त्व यहाँ नहीं लिया गया है, क्योंकि वृत्ति इस नियम में केवल अदि और तुट् बताती है तथा डित्त्वजन्य टिलोप करने पर प्रमाणित एतद् रूप बन ही नहीं सकता। ‘एतद्’ सर्वादिगण में पठित सर्वनाम है और तीनों लिङ्गों में प्रयुक्त होता है। संक्षिप्त वृत्ति कोई पृथक् lexical अर्थ नहीं देती, इसलिए अर्थ अनुमान से नहीं जोड़ा गया।",
)

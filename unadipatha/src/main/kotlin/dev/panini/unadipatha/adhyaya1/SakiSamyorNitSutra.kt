package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.dhatupatha.svadi.SvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.109: शकिशम्योर्नित्
object SakiSamyorNitSutra : UnadiSutra(
    number = "1.109",
    text = "शकिशम्योर्नित्",
    roots = setOf(
        SvadiDhatus.all.single { it.id == "05.0017" },
        DivadiDhatus.all.single { it.id == "04.0098" },
    ),
    pratyaya = "कल",
    pratyayaSurface = "अल",
    itMarkers = setOf(ItMarker.KIT, ItMarker.NIT),
    rootSamjnaMap = mapOf("शकॢँ" to Samjna.Rudhi("शकल"), "शमुँ" to Samjna.Rudhi("शमल")),
    rootMorphology = mapOf(
        "शकॢँ" to UnadiRootMorphology(conditionedStem = "शक्".toVarnas()),
        "शमुँ" to UnadiRootMorphology(conditionedStem = "शम्".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("शकल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.109"),
        UnadiLexicalAttestation("शमल".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.109"),
    ),
    relatedSutraNumbers = setOf("1.104"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शकल और शमल नित्-कित् कल-प्रत्ययान्त नपुंसकलिङ्ग प्रातिपदिक हैं।"),
    hindiExplanation = "शकि अर्थात् शक्तौ प्रयुक्त शकॢँ तथा उपशमे प्रयुक्त शमुँ—इन दोनों के बाद अनुवृत्त कल प्रत्यय होता है। इस सूत्र का ‘नित्’ कल को नित् बनाता है और कल का क् भी इत्संज्ञक है; दोनों इत्-चिह्नों के लोप के बाद प्रत्यय का श्रव्य रूप अल रहता है। शक् + अल से शकल और शम् + अल से शमल बनते हैं। वृत्ति दोनों को ‘शकलम्, शमलम्’ रूप में देती है, इसलिए नपुंसकलिङ्ग रखा गया है; अलग lexical अर्थ स्रोत में न होने से अनुमान नहीं किया गया।",
)

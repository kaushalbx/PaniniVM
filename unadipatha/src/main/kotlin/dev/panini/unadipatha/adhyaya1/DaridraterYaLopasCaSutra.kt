package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.adadi.AdadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa

// 1.90: दरिद्रातेर्यालोपश्च
object DaridraterYaLopasCaSutra : UnadiSutra(
    number = "1.90",
    text = "दरिद्रातेर्यालोपश्च",
    roots = setOf(AdadiDhatus.all.single { it.id == "02.0068" }),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    rootSamjnaMap = mapOf("दरिद्रा" to Samjna.Rudhi("दर्द्रू")),
    rootMeaningMap = mapOf("दरिद्रा" to Artha.Explanation("कुष्ठप्रभेद")),
    rootMorphology = mapOf(
        "दरिद्रा" to UnadiRootMorphology(
            conditionedStem = "दरिद्रा".toVarnas(),
            varnaLopas = listOf(UnadiVarnaLopa(Svara.I), UnadiVarnaLopa(Svara.AA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("दर्द्रू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("कुष्ठप्रभेद"), "1.90"),
    ),
    relatedSutraNumbers = setOf("1.81"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("दर्द्रू ऊ-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और कुष्ठ के एक भेद का वाचक है।"),
    hindiExplanation = "दरिद्रा धातु के बाद ऊ प्रत्यय होता है। सूत्र का ‘यालोपश्च’ वृत्ति में ‘इश्च आश्च यौ तयोर्लोपः’ कहकर स्पष्ट किया गया है—दरिद्रा आधार के इ-वर्ण और अन्तिम आ-वर्ण, दोनों का लोप होता है। इन दोनों पृथक् Varna-लोपों के बाद दरिद्रा से दर्द्र् आधार शेष रहता है। दर्द्र् + ऊ के संयोग से दर्द्रू प्रातिपदिक सिद्ध होता है। वृत्ति के अनुसार यह पुल्लिङ्ग में कुष्ठ के एक विशेष भेद का नाम है; इससे आगे कोई रोगार्थ अनुमानित नहीं किया गया।",
)

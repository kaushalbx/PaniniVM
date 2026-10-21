package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.kryadi.ShrDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra

// 1.88: के श्र एरङ् चास्य
object KeSraErangCasyaSutra : UnadiSutra(
    number = "1.88",
    text = "के श्र एरङ् चास्य",
    roots = setOf(ShrDhatu()),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    upapadas = setOf("क".toVarnas()),
    rootSamjnaMap = mapOf("शॄ" to Samjna.Rudhi("कशेरू")),
    rootMeaningMap = mapOf("शॄ" to Artha.Explanation("तृणकन्द")),
    rootMorphology = mapOf(
        "शॄ" to UnadiRootMorphology(
            conditionedStem = "शृ".toVarnas(),
            stemAdesha = "शेर्".toVarnas(),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(
            upapada = "क".toVarnas(),
            output = "कशेरु".toVarnas(),
            meaning = Artha.Explanation("तृणकन्द; बाहुलक से ह्रस्व उ-प्रत्ययान्त रूप"),
            sourceRoot = "शॄ",
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("कशेरू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("तृणकन्द"), "1.88"),
        UnadiLexicalAttestation("कशेरु".toVarnas(), setOf(Linga.PUMS, Linga.NAPUMSAKA), Artha.Explanation("तृणकन्द"), "1.88"),
    ),
    relatedSutraNumbers = setOf("1.81"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("कशेरू और बाहुलक से कशेरु तृणकन्द के वाचक प्रातिपदिक हैं।"),
    hindiExplanation = "क शब्द के उपपद रहते शॄ धातु के बाद ऊ प्रत्यय होता है। सूत्र का ‘एरङ् चास्य’ शृ आधार के स्थान पर शेर् आदेश देता है; इसे अनुमानित सामान्य गुण न मानकर सम्पूर्ण शेर् Varna-आदेश के रूप में रखा गया है। क उपपद + शेर् + ऊ से कशेरू बनता है, जो वृत्ति के अनुसार स्त्रीलिङ्ग में तृणकन्द का नाम है। वृत्ति बाहुलक से अलग ह्रस्व उ प्रत्यय वाला कशेरु रूप भी देती है; वह पुल्लिङ्ग और नपुंसकलिङ्ग दोनों में प्रयुक्त होता है, इसलिए उसे मुख्य ऊ-रूप में मिलाने के बजाय पृथक् derived variant और lexical attestation के रूप में सुरक्षित किया गया है।",
)

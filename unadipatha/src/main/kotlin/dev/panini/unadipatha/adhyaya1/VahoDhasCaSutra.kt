package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.83: वहो धश्च
object VahoDhasCaSutra : UnadiSutra(
    number = "1.83",
    text = "वहो धश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1159" }),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    rootSamjnaMap = mapOf("वहँ" to Samjna.Rudhi("वधू")),
    rootMeaningMap = mapOf("वहँ" to Artha.Explanation("जाया, स्नुषा अथवा स्त्री")),
    rootMorphology = mapOf(
        "वहँ" to UnadiRootMorphology(
            conditionedStem = "वह्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.HA, Vyanjana.DHA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("वधू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("जाया, स्नुषा अथवा स्त्री"), "1.83"),
    ),
    relatedSutraNumbers = setOf("1.81"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("वधू ऊ-प्रत्ययान्त स्त्रीलिङ्ग प्रातिपदिक है और जाया, स्नुषा अथवा स्त्री का वाचक है।"),
    hindiExplanation = "पिछले सूत्र से ऊ प्रत्यय की अनुवृत्ति होती है। वहँ धातु के वह् आधार में सूत्र का ‘धश्च’ अन्तिम ह्-वर्ण के स्थान पर ध् का स्पष्ट आदेश करता है। इस Varna-आदेश से वह् आधार वध् बनता है और वध् + ऊ के संयोग से वधू प्रातिपदिक सिद्ध होता है। वृत्ति ‘वधूर्जायास्नुषास्त्रीषु’ कहकर इसके तीन स्त्रीलिङ्ग अर्थ देती है—जाया अर्थात् पत्नी, स्नुषा अर्थात् पुत्रवधू और सामान्यतः स्त्री।",
)

package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaLopa
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.87: नञि लम्बेर्नलोपश्च
object NanjiLamberNaLopasCaSutra : UnadiSutra(
    number = "1.87",
    text = "नञि लम्बेर्नलोपश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0439" }),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    itMarkers = setOf(ItMarker.NIT),
    upapadas = setOf("नञ्".toVarnas()),
    rootSamjnaMap = mapOf("लबिँ" to Samjna.Rudhi("अलाबू")),
    rootMeaningMap = mapOf("लबिँ" to Artha.Explanation("तुम्बी अर्थात् लौकी")),
    rootMorphology = mapOf(
        "लबिँ" to UnadiRootMorphology(
            // इदित् लबिँ को मिलने वाले नुम् सहित आधार; सूत्र उसी नकार का लोप करता है।
            conditionedStem = "लन्ब्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Svara.A, Svara.AA)),
            varnaLopas = listOf(UnadiVarnaLopa(Vyanjana.NA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("अलाबू".toVarnas(), setOf(Linga.STRI), Artha.Explanation("तुम्बी अर्थात् लौकी"), "1.87"),
    ),
    relatedSutraNumbers = setOf("1.85", "7.1.58"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("नञ्-पूर्वक लबिँ से बना अलाबू णित् ऊ-प्रत्ययान्त स्त्रीलिङ्ग प्रातिपदिक है और तुम्बी का पर्याय है।"),
    hindiExplanation = "नञ् उपपद के साथ लबिँ धातु के बाद पूर्वसूत्र से णित् ऊ प्रत्यय होता है। लबिँ इदित् धातु होने से 7.1.58 द्वारा नुम् पाकर लन्ब् आधार बनाती है; सूत्र का ‘नलोपश्च’ इसी नुम् के न्-वर्ण का लोप करता है, इसलिए लब् शेष रहता है। णित्त्व से लब् के अ की वृद्धि आ होती है और नञ् का प्रचलित अ-रूप पूर्व में जुड़ता है। इस प्रकार अ + लाब् + ऊ से अलाबू सिद्ध होता है। वृत्ति अमरकोश के ‘तुम्ब्यलाबूरुभे समे’ वचन से अलाबू और तुम्बी को समानार्थक स्त्रीलिङ्ग शब्द बताती है।",
)

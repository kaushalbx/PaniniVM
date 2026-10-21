package dev.panini.unadipatha.adhyaya1

import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.11: स्यन्देः संप्रसारणं धश्च
object SyandehSamprasaranamDhashCaSutra : UnadiSutra(
    number = "1.11",
    text = "स्यन्देः संप्रसारणं धश्च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.0865" }),
    // The उ-affix continues from 1.7; this rule additionally prescribes two stem operations.
    pratyaya = "उ",
    pratyayaSurface = "उ",
    itMarkers = setOf(dev.panini.core.ItMarker.NIT),
    assignedAccent = dev.panini.shiksha.Accent.UDATTA,
    accentVowelIndex = 0,
    rootSamjnaMap = mapOf("स्यन्दूँ" to Samjna.Rudhi("सिन्धु")),
    rootMeaningMap = mapOf(
        "स्यन्दूँ" to Artha.Rudhi(
            "सिन्धु",
            "country, a particular river, ocean, or river",
        ),
    ),
    varnaSubstitutions = listOf(
        // संप्रसारणम्: the य् of स्यन्द् is replaced by its corresponding इक् vowel इ.
        UnadiVarnaSubstitution(source = Vyanjana.YA, replacement = Svara.I),
        // धश्च: the द् of the root is replaced by ध्.
        UnadiVarnaSubstitution(source = Vyanjana.DA, replacement = Vyanjana.DHA),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Rudhi(
        "सिन्धु",
        "country, a particular river, ocean, or river",
    ),
    hindiExplanation = "स्यन्दूँ धातु के बाद पूर्वसूत्र से अनुवृत्त उ प्रत्यय होता है। सूत्र का ‘संप्रसारणम्’ पद धातु के य् को उसके इक्-वर्ण इ से बदलता है; अतः स्यन्द् से सिन्द् बनता है। " +
        "‘धश्च’ के विधान से द् के स्थान पर ध् आता है और उ प्रत्यय के योग से सिन्धु रूप सिद्ध होता है। सिन्धु रूढ़ रूप से देश, विशेष नदी, समुद्र अथवा सामान्य नदी का वाचक है।",
)

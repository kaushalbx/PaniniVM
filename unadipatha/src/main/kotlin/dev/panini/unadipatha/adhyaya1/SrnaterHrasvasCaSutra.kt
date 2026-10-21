package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.kryadi.KryadiDhatus
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

// 1.123: शृणातेर्ह्रस्वश्च
object SrnaterHrasvasCaSutra : UnadiSutra(
    number = "1.123",
    text = "शृणातेर्ह्रस्वश्च",
    roots = setOf(KryadiDhatus.all.single { it.id == "09.0015" }),
    pratyaya = "गन्",
    pratyayaSurface = "ग",
    itMarkers = setOf(ItMarker.KIT, ItMarker.N),
    rootSamjnaMap = mapOf("शॄञ्" to Samjna.Rudhi("शृङ्ग")),
    rootMorphology = mapOf(
        "शॄञ्" to UnadiRootMorphology(
            conditionedStem = "शॄ".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.NA), UnadiAgamaPlacement.BEFORE_SUFFIX, "नुट्"),
            ),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Svara.RR, Svara.R),
                UnadiVarnaSubstitution(Vyanjana.NA, Vyanjana.NGA),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("शृङ्ग".toVarnas(), setOf(Linga.NAPUMSAKA), attributedSutra = "1.123"),
    ),
    relatedSutraNumbers = setOf("1.122"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("शृङ्ग नुट्-सहित कित् गन्-प्रत्ययान्त नपुंसकलिङ्ग प्रातिपदिक है।"),
    hindiExplanation = "शृणाति रूप वाली शॄञ् धातु के बाद पिछले सूत्र से कित् गन् प्रत्यय और नुट् आगम दोनों आते हैं। गन् के अन्तिम न् का इत्संज्ञा-लोप होने पर श्रव्य प्रत्यय ग है। इस सूत्र का ‘ह्रस्वश्च’ धातु के दीर्घ ॠ के स्थान पर ह्रस्व ऋ का स्पष्ट Varna-आदेश करता है: शॄ → शृ। फिर धातु और प्रत्यय के बीच नुट् का न्-वर्ण आता है और अगले कण्ठ्य ग् के कारण न् का परसवर्ण ङ् होता है। इस प्रकार शृ + ङ् + ग से शृङ्ग बनता है। वृत्ति ‘शृङ्गम्’ कहती है, इसलिए नपुंसकलिङ्ग रखा गया है। इस स्रोतांश में शृङ्ग का कोई कोशार्थ नहीं बताया गया, अतः अर्थ अनुमान से नहीं जोड़ा गया।",
)

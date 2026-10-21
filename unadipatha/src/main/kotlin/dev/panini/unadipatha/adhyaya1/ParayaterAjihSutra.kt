package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.core.SanadiAffix
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra133Para = CuradiDhatus.all.single { it.id == "10.0453" }

// 1.133: पारयतेरजिः
object ParayaterAjihSutra : UnadiSutra(
    number = "1.133",
    text = "पारयतेरजिः",
    roots = setOf(sutra133Para),
    pratyaya = "अजि",
    pratyayaSurface = "अज्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(sutra133Para.upadesha to Samjna.Rudhi("पारक्")),
    rootMeaningMap = mapOf(sutra133Para.upadesha to Artha.Explanation("सुवर्ण")),
    rootMorphology = mapOf(
        sutra133Para.upadesha to UnadiRootMorphology(
            conditionedStem = "पार".toVarnas(),
            requiredSanadiAffixes = setOf(SanadiAffix.NIC),
            lopaSanadiAffixes = setOf(SanadiAffix.NIC),
            pratyayaVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.KA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("पारक्".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("सुवर्ण"), "1.133"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("पारक् सुवर्ण का वाचक अजि-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है।"),
    hindiExplanation = "कर्मसमाप्ति अर्थ वाले चुरादिगणीय पार धातु का णिच्-युक्त रूप पारयति है; सूत्र इसी ‘पारयतेः’ रूप को ग्रहण करता है। इसके बाद अजि प्रत्यय होता है। अजि का अन्तिम इ इत्संज्ञक होकर लुप्त होता है और अज् शेष रहता है। कोशीय व्युत्पत्ति में णि-लोप स्पष्ट दिया गया है, इसलिए morphology में णिच् की आवश्यकता और उसका लोप दोनों अलग-अलग दर्ज किए गए हैं; फलतः पार आधार बचता है। पार + अज् में प्रत्यय-अन्त ज् का क् होकर पारक् सिद्ध होता है, जिसे suffix पर ज् → क् के Varna-आदेश के रूप में रखा गया है। वृत्ति ‘पारक् सुवर्णम्’ कहती है, इसलिए अर्थ केवल सुवर्ण रखा गया है। कोश में पारक् पुल्लिङ्ग प्रमाणित है; सुवर्णम् gloss का नपुंसकलिङ्ग पारक् शब्द पर आरोपित नहीं किया गया।",
)

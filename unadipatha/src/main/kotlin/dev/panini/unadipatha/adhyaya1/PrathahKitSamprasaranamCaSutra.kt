package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.curadi.CuradiDhatus
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

private val sutra134PrathBhvadi = BhvadiDhatus.all.single { it.id == "01.0869" }
private val sutra134PrathCuradi = CuradiDhatus.all.single { it.id == "10.0027" }

// 1.134: प्रथः कित्संप्रसारणं च
object PrathahKitSamprasaranamCaSutra : UnadiSutra(
    number = "1.134",
    text = "प्रथः कित्संप्रसारणं च",
    roots = setOf(sutra134PrathBhvadi, sutra134PrathCuradi),
    pratyaya = "अजि",
    pratyayaSurface = "अज्",
    itMarkers = setOf(ItMarker.I, ItMarker.KIT),
    rootSamjnaMap = mapOf("प्रथँ" to Samjna.Rudhi("पृथक्")),
    rootMorphology = mapOf(
        "प्रथँ" to UnadiRootMorphology(
            conditionedStem = "प्रथ्".toVarnas(),
            varnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.RA, Svara.R)),
            pratyayaVarnaSubstitutions = listOf(UnadiVarnaSubstitution(Vyanjana.JA, Vyanjana.KA)),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("पृथक्".toVarnas(), attributedSutra = "1.134"),
    ),
    relatedSutraNumbers = setOf("1.133"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA, Samjna.AVYAYA),
    hindiExplanation = "प्रथ् धातु के बाद पिछले सूत्र १.१३३ से अजि प्रत्यय आता है। यह सूत्र उस प्रत्यय को कित् करता है और धातु में सम्प्रसारण बताता है। अजि का इ इत्संज्ञक होकर लुप्त होता है; सम्प्रसारण से प्रथ् के र् के स्थान पर ऋ होता है और पृथ् आधार मिलता है। इस परिवर्तन को र् → ऋ के स्पष्ट Varna-आदेश के रूप में रखा गया है। प्रत्यय का श्रव्य अज् रूप पृथ् से जुड़ता है और प्रत्यय-अन्त ज् के क् होने पर पृथक् सिद्ध होता है। सूत्र में प्रथ् के गण का भेद नहीं है; १.२८ के समान प्रख्यानार्थ वाली भ्वादि तथा चुरादि—दोनों धातुपाठ प्रविष्टियाँ सुरक्षित रखी गई हैं। वृत्ति ‘स्वरादिपाठादव्ययत्वम्’ कहती है, इसलिए पृथक् को अव्ययसंज्ञा दी गई है और उस पर कोई लिङ्ग नहीं लगाया गया। वृत्ति यहाँ पृथक् का lexical अर्थ नहीं बताती, इसलिए परिचित अर्थ अनुमान से नहीं जोड़ा गया।",
)

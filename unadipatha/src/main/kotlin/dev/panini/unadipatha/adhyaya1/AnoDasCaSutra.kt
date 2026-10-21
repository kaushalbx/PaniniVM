package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.divadi.DivadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.86: अणो डश्च
object AnoDasCaSutra : UnadiSutra(
    number = "1.86",
    text = "अणो डश्च",
    roots = setOf(
        BhvadiDhatus.all.single { it.id == "01.0512" },
        DivadiDhatus.all.single { it.id == "04.0071" },
    ),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    itMarkers = setOf(ItMarker.NIT),
    rootSamjnaMap = mapOf("अणँ" to Samjna.Rudhi("आडू")),
    rootMeaningMap = mapOf("अणँ" to Artha.Explanation("जलप्लवद्रव्य अर्थात् जल में तैरने वाला पदार्थ")),
    rootMorphology = mapOf(
        "अणँ" to UnadiRootMorphology(
            conditionedStem = "अण्".toVarnas(),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Svara.A, Svara.AA),
                UnadiVarnaSubstitution(Vyanjana.NNA, Vyanjana.DDA),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            "आडू".toVarnas(),
            setOf(Linga.NAPUMSAKA),
            Artha.Explanation("जलप्लवद्रव्य अर्थात् जल में तैरने वाला पदार्थ"),
            "1.86",
        ),
    ),
    relatedSutraNumbers = setOf("1.85"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("आडू णित् ऊ-प्रत्ययान्त नपुंसकलिङ्ग प्रातिपदिक है और जल में तैरने वाले पदार्थ का वाचक है।"),
    hindiExplanation = "पिछले सूत्र से णित् ऊ प्रत्यय की अनुवृत्ति होती है। अणँ धातु के अण् आधार में णित्त्व के कारण आदि अ-वर्ण की वृद्धि होकर आ होता है और इस सूत्र के ‘डश्च’ से अन्तिम ण् के स्थान पर ड् का स्पष्ट Varna-आदेश होता है। इस प्रकार अण् से आड् आधार बनता है और आड् + ऊ से आडू प्रातिपदिक सिद्ध होता है। वृत्ति इसे नपुंसकलिङ्ग में जलप्लवद्रव्य—जल पर तैरने वाली वस्तु—का वाचक बताती है। सूत्र गणभेद नहीं बताता, अतः अणँ की भ्वादि और दिवादि दोनों धातुपाठ-प्रविष्टियाँ सुरक्षित हैं।",
)

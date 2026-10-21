package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.kryadi.KryadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaSubstitution

// 1.52: अशेर्णित्
object AsherNitSutra : UnadiSutra(
    number = "1.52",
    text = "अशेर्णित्",
    roots = setOf(KryadiDhatus.all.single { it.id == "09.0059" }),
    pratyaya = "किरच्",
    pratyayaSurface = "इर",
    itMarkers = setOf(ItMarker.KIT, ItMarker.CIT, ItMarker.NIT),
    rootSamjnaMap = mapOf("अशँ" to Samjna.Rudhi("आशिर")),
    rootMeaningMap = mapOf("अशँ" to Artha.Explanation("अग्नि अथवा राक्षस")),
    varnaSubstitutions = listOf(
        UnadiVarnaSubstitution(source = Svara.A, replacement = Svara.AA),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "आशिर".toVarnas(),
            lingas = setOf(Linga.PUMS),
            meaning = Artha.Explanation("अग्नि अथवा राक्षस"),
            attributedSutra = "1.52",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("आशिर पुल्लिङ्ग में अग्नि अथवा राक्षस का वाचक है।"),
    hindiExplanation = "भोजन अर्थ वाले क्र्यादिगणीय अशँ धातु के बाद पिछले सूत्र से अनुवृत्त किरच् प्रत्यय होता है। किरच् का आरम्भिक क् और अन्तिम च् इत्संज्ञक होकर लुप्त होते हैं, अतः इर शेष रहता है। इस सूत्र का ‘णित्’ प्रत्यय को अनिवार्य णित्संज्ञा देता है; उसके फलस्वरूप अश् के आद्य अकार की वृद्धि होकर आकार आता है। इस Varna-परिवर्तन से अश् का आश् आधार बनता है और आश् + इर से आशिर प्रातिपदिक सिद्ध होता है। वृत्ति ‘आशिरः’ को पुल्लिङ्ग में अग्नि तथा राक्षस—दोनों का वाचक बताती है; इसलिए दोनों अर्थ एक ही प्रमाणित lexical attestation में रखे गए हैं।",
)

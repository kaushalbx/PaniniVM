package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.tudadi.TudadiDhatus
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama

// 1.50: किलेर्बुक् च
object KilerBukCaSutra : UnadiSutra(
    number = "1.50",
    text = "किलेर्बुक् च",
    roots = setOf(TudadiDhatus.all.single { it.id == "06.0080" }),
    pratyaya = "टिषच्",
    pratyayaSurface = "इष",
    itMarkers = setOf(ItMarker.T, ItMarker.CIT),
    rootSamjnaMap = mapOf("किलँ" to Samjna.Rudhi("किल्बिष")),
    rootMeaningMap = mapOf("किलँ" to Artha.Explanation("पाप, रोग अथवा अपराध")),
    rootMorphology = mapOf(
        "किलँ" to UnadiRootMorphology(
            conditionedStem = "किल्".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(
                    varnas = listOf(Vyanjana.BA),
                    placement = UnadiAgamaPlacement.BEFORE_SUFFIX,
                    upadesha = "बुक्",
                ),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation(
            form = "किल्बिष".toVarnas(),
            lingas = setOf(Linga.NAPUMSAKA),
            meaning = Artha.Explanation("पाप, रोग अथवा अपराध"),
            attributedSutra = "1.50",
        ),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("किल्बिष पाप, रोग अथवा अपराध का वाचक है।"),
    hindiExplanation = "श्वैत्य और क्रीडन अर्थ वाले तुदादिगणीय किलँ धातु के बाद पूर्वसूत्र से अनुवृत्त टिषच् प्रत्यय होता है। टिषच् के ट् और च् इत्संज्ञक होकर लुप्त होते हैं, जिससे इष प्रत्ययांश बचता है। सूत्र ‘बुक् च’ द्वारा प्रकृतिभूत धातु और प्रत्यय के बीच बुक् आगम भी करता है। बुक् में उकार उच्चारण-सहायक और ककार इत्संज्ञक होने के कारण प्रभावी ब्-वर्ण रहता है। अतः किल् + ब् + इष से किल्बिष प्रातिपदिक सिद्ध होता है। वृत्ति में ‘किल्बिषम्’ रूप दिया गया है; मेदिनी के प्रमाण से यह पाप और रोग तथा अपराध का भी वाचक है। इसी कारण इन तीनों प्रमाणित अर्थों को एक नपुंसकलिङ्ग lexical attestation में रखा गया है।",
)

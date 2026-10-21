package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.dhatupatha.tudadi.GrDhatu
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
import dev.panini.unadipatha.UnadiVarnaExpansion

// 1.95: ग्रो मुट् च
object GroMutCaSutra : UnadiSutra(
    number = "1.95",
    text = "ग्रो मुट् च",
    roots = setOf(GrDhatu()),
    pratyaya = "उति",
    pratyayaSurface = "उत्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf("गॄ" to Samjna.Rudhi("गर्मुत्")),
    rootMeaningMap = mapOf("गॄ" to Artha.Explanation("सुवर्ण अथवा तृणविशेष")),
    rootMorphology = mapOf(
        "गॄ" to UnadiRootMorphology(
            conditionedStem = "गॄ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA))),
            varnaAgamas = listOf(
                UnadiVarnaAgama(
                    varnas = listOf(Vyanjana.MA),
                    placement = UnadiAgamaPlacement.BEFORE_SUFFIX,
                    upadesha = "मुट्",
                ),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("गर्मुत्".toVarnas(), meaning = Artha.Explanation("सुवर्ण अथवा तृणविशेष"), attributedSutra = "1.95"),
    ),
    relatedSutraNumbers = setOf("1.94"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("गर्मुत् मुट्-आगमयुक्त उति-प्रत्ययान्त प्रातिपदिक है और सुवर्ण अथवा एक तृणविशेष का वाचक है।"),
    hindiExplanation = "पिछले सूत्र से गॄ धातु और इकारानुबन्धक उति प्रत्यय की अनुवृत्ति होती है। गॄ के ॠ-वर्ण का गुण अर् होकर गर् आधार बनता है। यह सूत्र उति प्रत्यय से ठीक पहले मुट् आगम करता है; मुट् का उ उच्चारणार्थ और ट् इत्संज्ञक होने से केवल म्-वर्ण शेष रहता है। फलतः गर् + म् + उत् से गर्मुत् प्रातिपदिक सिद्ध होता है। वृत्ति इसे सुवर्ण तथा एक विशेष प्रकार के तृण का वाचक बताती है; उसका लिङ्ग यहाँ स्पष्ट न होने से अनुमान से नहीं जोड़ा गया।",
)

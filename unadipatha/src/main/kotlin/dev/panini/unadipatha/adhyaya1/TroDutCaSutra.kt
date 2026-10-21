package dev.panini.unadipatha.adhyaya1

import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
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

// 1.89: त्रो दुट् च
object TroDutCaSutra : UnadiSutra(
    number = "1.89",
    text = "त्रो दुट् च",
    roots = setOf(BhvadiDhatus.all.single { it.id == "01.1124" }),
    pratyaya = "ऊ",
    pratyayaSurface = "ऊ",
    rootSamjnaMap = mapOf("तॄ" to Samjna.Rudhi("तर्दू")),
    rootMeaningMap = mapOf("तॄ" to Artha.Explanation("दारुहस्तक")),
    rootMorphology = mapOf(
        "तॄ" to UnadiRootMorphology(
            conditionedStem = "तॄ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA))),
            varnaAgamas = listOf(
                UnadiVarnaAgama(
                    varnas = listOf(Vyanjana.DA),
                    placement = UnadiAgamaPlacement.BEFORE_SUFFIX,
                    upadesha = "दुट्",
                ),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("तर्दू".toVarnas(), setOf(Linga.PUMS), Artha.Explanation("दारुहस्तक"), "1.89"),
    ),
    relatedSutraNumbers = setOf("1.81"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("तर्दू ऊ-प्रत्ययान्त पुल्लिङ्ग प्रातिपदिक है और दारुहस्तक का वाचक है।"),
    hindiExplanation = "तॄ धातु के बाद ऊ प्रत्यय होता है और सूत्र उसके पहले दुट् आगम करता है। दुट् में उ उच्चारणार्थ तथा ट् इत्संज्ञक है, इसलिए सुनाई देने वाला द्-वर्ण प्रत्यय से ठीक पहले रहता है। तॄ के ॠ-वर्ण का गुण अर् होता है; इसे अ और र् के क्रमबद्ध Varna-विस्तार के रूप में अभिलेखित किया गया है। फलतः तॄ से तर्, फिर द्-आगम से तर्द् और ऊ प्रत्यय लगने पर तर्दू सिद्ध होता है। वृत्ति इसे पुल्लिङ्ग में दारुहस्तक का वाचक बताती है।",
)

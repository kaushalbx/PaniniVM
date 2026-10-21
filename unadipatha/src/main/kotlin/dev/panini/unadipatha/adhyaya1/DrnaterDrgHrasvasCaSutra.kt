package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.kryadi.DrDhatu
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

private val sutra128Dr = DrDhatu()

// 1.128: दृणातेर्दृग् ह्रस्वश्च (the source commentary analyzes the operation as षुक्)
object DrnaterDrgHrasvasCaSutra : UnadiSutra(
    number = "1.128",
    text = "दृणातेर्दृग् ह्रस्वश्च",
    roots = setOf(sutra128Dr),
    pratyaya = "अदि",
    pratyayaSurface = "अद्",
    itMarkers = setOf(ItMarker.I),
    rootSamjnaMap = mapOf(sutra128Dr.upadesha to Samjna.Rudhi("दृषत्")),
    rootMorphology = mapOf(
        sutra128Dr.upadesha to UnadiRootMorphology(
            conditionedStem = "दॄ".toVarnas(),
            varnaAgamas = listOf(
                UnadiVarnaAgama(listOf(Vyanjana.SSA), UnadiAgamaPlacement.BEFORE_SUFFIX, "षुक्"),
            ),
            varnaSubstitutions = listOf(
                UnadiVarnaSubstitution(Svara.RR, Svara.R),
                UnadiVarnaSubstitution(Vyanjana.DA, Vyanjana.TA),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("दृषत्".toVarnas(), setOf(Linga.STRI), attributedSutra = "1.128"),
    ),
    relatedSutraNumbers = setOf("1.127"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "दॄ धातु के बाद पिछले सूत्र १.१२७ से अदि प्रत्यय आता है। अदि का इ इत्संज्ञक होकर लुप्त होता है और अद् शेष रहता है। इस सूत्र से धातु के ॠ का ह्रस्व ऋ होता है तथा प्रत्यय से पहले षुक् आगम आता है; षुक् के उ और क् इत्संज्ञक होकर लुप्त होने पर ष् बचता है। इस प्रकार दृ + ष् + अद् बनता है और पदान्त द् के त् हो जाने से दृषत् सिद्ध होता है। वेबसाइट के संकलित सूत्र-पाठ में ‘दृग्’ मिलता है, जबकि सिद्धान्तकौमुदी, काशिका-सन्दर्भ और कोशीय व्युत्पत्ति इस operation को ‘षुग्/षुक्’ बताती हैं; अतः source text को जस का तस रखकर executable morphology में प्रमाणित षुक् आगम लिखा गया है। वृत्ति केवल ‘दृषत्’ रूप देती है, कोई अर्थ नहीं देती; इसलिए lexical meaning अनुमान से नहीं जोड़ा गया। कोशों में इसका स्त्रीलिङ्ग प्रमाणित होने के कारण लिङ्ग स्त्री रखा गया है।",
)

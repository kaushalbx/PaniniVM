package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.juhotyadi.JuhotyadiDhatus
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiAgamaPlacement
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaAgama

private val sutra145Bhi = JuhotyadiDhatus.all.single { it.id == "03.0002" }

// 1.145: भियः षुग्वा
object BhiyahShugVaSutra : UnadiSutra(
    number = "1.145",
    text = "भियः षुग्वा",
    roots = setOf(sutra145Bhi),
    pratyaya = "मक्",
    pratyayaSurface = "म",
    itMarkers = setOf(ItMarker.KIT),
    rootSamjnaMap = mapOf(sutra145Bhi.upadesha to Samjna.Rudhi("भीम")),
    rootMorphology = mapOf(
        sutra145Bhi.upadesha to UnadiRootMorphology(
            conditionedStem = "भी".toVarnas(),
            optionalVarnaAgamas = listOf(
                UnadiVarnaAgama(
                    varnas = listOf(Vyanjana.SSA),
                    placement = UnadiAgamaPlacement.BEFORE_SUFFIX,
                    upadesha = "षुक्",
                ),
            ),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(output = "भीम".toVarnas(), sourceRoot = sutra145Bhi.upadesha),
        UnadiDerivedVariant(
            output = "भीष्म".toVarnas(),
            sourceRoot = sutra145Bhi.upadesha,
            morphology = UnadiRootMorphology(
                varnaAgamas = listOf(
                    UnadiVarnaAgama(listOf(Vyanjana.SSA), UnadiAgamaPlacement.BEFORE_SUFFIX, "षुक्"),
                ),
            ),
        ),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("भीम".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.145"),
        UnadiLexicalAttestation("भीष्म".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.145"),
    ),
    relatedSutraNumbers = setOf("1.142"),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    hindiExplanation = "भयार्थक जुहोत्यादिगणीय ञिभी धातु के भी आधार के बाद १.१४२ से मक् प्रत्यय आता है। मक् का अन्तिम क् इत्संज्ञक होकर लुप्त होता है, इसलिए श्रव्य प्रत्यय म है; कित्त्व के कारण भी के ई का गुण नहीं होता। षुक् आगम विकल्प से प्रत्यय के ठीक पहले आता है। षुक् के उ और क् इत्संज्ञक होकर लुप्त होने पर केवल ष्-वर्ण शेष रहता है। आगम के अभाव में भी + म से भीम और आगम-पक्ष में भी + ष् + म से भीष्म सिद्ध होता है। वैकल्पिक ष् को `optionalVarnaAgamas` में तथा भीष्म शाखा में वास्तविक षुक्-आगम को अलग morphology में रखा गया है। वृत्ति दोनों शब्द पुल्लिङ्ग में उद्धृत करती है, पर यहाँ उनका कोई पृथक् कोशार्थ नहीं देती; इसलिए प्रसिद्ध व्यक्तिवाचक अथवा विशेषणार्थ अनुमान से नहीं जोड़े गए।",
)

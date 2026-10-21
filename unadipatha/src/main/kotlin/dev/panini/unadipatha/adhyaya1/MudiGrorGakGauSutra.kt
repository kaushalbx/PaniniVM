package dev.panini.unadipatha.adhyaya1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.dhatupatha.bhvadi.BhvadiDhatus
import dev.panini.dhatupatha.tudadi.GrDhatu
import dev.panini.shiksha.Artha
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.unadipatha.UnadiDerivedVariant
import dev.panini.unadipatha.UnadiLexicalAttestation
import dev.panini.unadipatha.UnadiRootMorphology
import dev.panini.unadipatha.UnadiRootPratyaya
import dev.panini.unadipatha.UnadiSutra
import dev.panini.unadipatha.UnadiVarnaExpansion

private val sutra125Mud = BhvadiDhatus.all.single { it.id == "01.0016" }
private val sutra125Gr = GrDhatu()

// 1.125: मुदिग्रोर्गग्गौ
object MudiGrorGakGauSutra : UnadiSutra(
    number = "1.125",
    text = "मुदिग्रोर्गग्गौ",
    roots = setOf(sutra125Mud, sutra125Gr),
    pratyaya = "गक्, ग",
    pratyayaSurface = "",
    rootPratyayaMap = mapOf(
        sutra125Mud.upadesha to UnadiRootPratyaya("गक्", "ग", setOf(ItMarker.KIT)),
        sutra125Gr.upadesha to UnadiRootPratyaya("ग", "ग"),
    ),
    rootSamjnaMap = mapOf(
        sutra125Mud.upadesha to Samjna.Rudhi("मुद्ग"),
        sutra125Gr.upadesha to Samjna.Rudhi("गर्ग"),
    ),
    rootMorphology = mapOf(
        sutra125Mud.upadesha to UnadiRootMorphology(conditionedStem = "मुद्".toVarnas()),
        sutra125Gr.upadesha to UnadiRootMorphology(
            conditionedStem = "गॄ".toVarnas(),
            varnaExpansions = listOf(UnadiVarnaExpansion(Svara.RR, listOf(Svara.A, Vyanjana.RA))),
        ),
    ),
    derivedVariants = listOf(
        UnadiDerivedVariant(sourceRoot = sutra125Mud.upadesha, output = "मुद्ग".toVarnas()),
        UnadiDerivedVariant(sourceRoot = sutra125Gr.upadesha, output = "गर्ग".toVarnas()),
    ),
    lexicalAttestations = listOf(
        UnadiLexicalAttestation("मुद्ग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.125"),
        UnadiLexicalAttestation("गर्ग".toVarnas(), setOf(Linga.PUMS), attributedSutra = "1.125"),
    ),
    baseSamjnas = setOf(Samjna.Affix.KRT, Samjna.Unit.PRATIPADIKA),
    meaning = Artha.Explanation("मुदँ से गक् और गॄ से ग प्रत्यय लगकर क्रमशः मुद्ग तथा गर्ग प्रातिपदिक बनते हैं।"),
    hindiExplanation = "यह सूत्र दो धातुओं के लिए अलग-अलग प्रत्यय विधान करता है। मुदँ धातु का इत्संज्ञा-लोप के बाद आधार मुद् है। उससे गक् प्रत्यय होता है; गक् का अन्तिम क् इत्संज्ञक होकर लुप्त होता है और श्रव्य ग शेष रहता है। अतः मुद् + ग से मुद्ग बनता है। गॄ धातु से पृथक् ग प्रत्यय होता है। यहाँ ॠ का गुण अर् होता है, जिसे ॠ → अ + र् के स्पष्ट Varna-विस्तार से रखा गया है; इस प्रकार गर् + ग से गर्ग बनता है। दोनों प्राप्त रूप वृत्ति में मुद्गः और गर्गः के रूप में दिए गए हैं, इसलिए पुल्लिङ्ग रखा गया है। सूत्र-वृत्ति इन शब्दों का कोई विशिष्ट कोशार्थ नहीं बताती, अतः अर्थ अनुमान से नहीं जोड़ा गया है।",
)

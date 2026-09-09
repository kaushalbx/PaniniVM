package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.analysis.*
import dev.panini.core.SamasaType
import dev.panini.sutra.*

private val vowels = setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')
private val prakrtiUttara = setOf("भ्राज्", "पात्", "वेदस्", "असत्या", "मुचि", "कुल", "ख", "पुंसक", "क्षत्र", "क्र", "आक")
private fun nanContext(c: SamasaRuleContext) = c.padas.size >= 2 && c.samasaType == SamasaType.NAN_TATPURUSA && c.purvaPada.upadesha in setOf("न", "नञ्")
private fun prefixed(prefix: String, c: SamasaRuleContext) = prefix + c.padas.drop(1).joinToString("") { it.upadesha }

/** 6.3.73: नलोपो नञः. */
object NalopoNanjahSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.73", text="नलोपो नञः", hindiExplanation="उत्तरपद परे नञ् के नकार का लोप होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630073,
    role=SutraRole.Vidhi, action=SutraAction.LOPA, scope=SutraScope.DERIVATION,
    samasaType=SamasaType.NAN_TATPURUSA, samasaPriority=10,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext) = nanContext(context) && context.uttaraPada.upadesha.firstOrNull() !in vowels && context.uttaraPada.upadesha !in prakrtiUttara
    override fun apply(context: SamasaRuleContext) = SamasaRuleResult.Formed(prefixed("अ", context), "6.3.73 deletes न् of नञ्.")
}

/** 6.3.74: तस्मान्नुडचि. */
object TasmanNudAciSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.74", text="तस्मान्नुडचि", hindiExplanation="लुप्तनकार नञ् से परे अजादि उत्तरपद में नुट् आगम होता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630074,
    role=SutraRole.Vidhi, action=SutraAction.AGAMA, scope=SutraScope.DERIVATION,
    samasaType=SamasaType.NAN_TATPURUSA, samasaPriority=20,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext) = nanContext(context) && context.uttaraPada.upadesha.firstOrNull() in vowels && context.uttaraPada.upadesha !in prakrtiUttara
    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val u = context.uttaraPada.upadesha
        val matra = mapOf('अ' to "", 'आ' to "ा", 'इ' to "ि", 'ई' to "ी", 'उ' to "ु", 'ऊ' to "ू", 'ऋ' to "ृ", 'ए' to "े", 'ऐ' to "ै", 'ओ' to "ो", 'औ' to "ौ")[u.first()]
        val stem = if (matra == null) "अन्$u" else "अन$matra${u.drop(1)}"
        return SamasaRuleResult.Formed(stem, "6.3.73 and 6.3.74 yield नुट् before a vowel-initial uttarapada.")
    }
}

/** 6.3.75: नभ्राण्नपान्नवेदानासत्या नमुचिनकुलनखनपुंसकनक्षत्रनक्रनाकेषु प्रकृत्या. */
object NabhraanNapaanNavedaSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number="6.3.75", text="नभ्राण्नपान्नवेदानासत्या नमुचिनकुलनखनपुंसकनक्षत्रनक्रनाकेषु प्रकृत्या",
    hindiExplanation="सूत्र में पठित शब्दों में नञ् प्रकृतिभाव से रहता है।",
    type=SutraType.NITYA, chapter=6, pada=3, optional=false, kramaValue=630075,
    role=SutraRole.Niyama, action=SutraAction.NIYAMA, scope=SutraScope.DERIVATION,
    samasaType=SamasaType.NAN_TATPURUSA, samasaPriority=30,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext) = nanContext(context) && context.uttaraPada.upadesha in prakrtiUttara
    override fun apply(context: SamasaRuleContext) = SamasaRuleResult.Formed(prefixed("न", context), "6.3.75 preserves नञ् in the listed lexical forms.")
}

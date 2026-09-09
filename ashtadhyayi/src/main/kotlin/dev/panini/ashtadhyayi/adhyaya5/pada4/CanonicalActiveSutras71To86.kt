package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import dev.panini.sutra.SamasaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

abstract class ActiveSamasantaSutra(
    number: Int,
    text: String,
    optional: Boolean = false,
    samasaType: SamasaType,
    priority: Int = 10,
) : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.$number",
    text = text,
    hindiExplanation = "प्रामाणिक अर्थ-सन्दर्भ और उत्तरपद की शर्तों के अनुसार समासान्त-कार्य होता है।",
    type = if (optional) SutraType.VIBHASHA else SutraType.NITYA,
    chapter = 5,
    pada = 4,
    optional = optional,
    kramaValue = 540000 + number,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    samasaType = samasaType,
    samasaPriority = priority,
), SamasaSutra {
    protected fun formed(context: SamasaRuleContext, stem: String): SamasaRuleResult.Formed {
        val members=context.padas.map { it.upadesha }
        val base=members.joinToString("")
        val leading=members.dropLast(1).joinToString("")
        return when {
            stem.startsWith(base) -> SamasaRuleResult.Formed(stem,"$number forms samāsānta stem '$stem'.",samasantaSuffix=stem.removePrefix(base))
            stem.startsWith(leading) -> SamasaRuleResult.Formed(stem,"$number forms samāsānta stem '$stem'.",memberEdits=mapOf(members.lastIndex to stem.removePrefix(leading)))
            else -> SamasaRuleResult.Formed(stem,"$number forms irregular samāsānta stem '$stem'.",wholeStemOverride=true)
        }
    }
}

/** 5.4.71 blocks the following samāsānta affixes in a nañ-tatpuruṣa. */
object NanjastatpurusatSutra : Sutra<SamasaRuleContext, SamasaRuleResult>(
    number = "5.4.71", text = "नञस्तत्पुरुषात्",
    hindiExplanation = "नञ्-तत्पुरुष से उत्तरवर्ती समासान्त प्रत्ययों का निषेध होता है।",
    type = SutraType.NITYA, chapter = 5, pada = 4, optional = false, kramaValue = 540071,
    role = SutraRole.Niyama, action = SutraAction.NISHEDHA, scope = SutraScope.DERIVATION,
    samasaType = SamasaType.NAN_TATPURUSA, samasaPriority = 30,
), SamasaSutra {
    override fun matches(context: SamasaRuleContext) = context.samasaType == SamasaType.NAN_TATPURUSA
    override fun apply(context: SamasaRuleContext) = SamasaRuleResult.NotApplicable
}

/** 5.4.72 optionally restores the a-affix for nañ + pathin. */
object PathoVibhasaSutra : ActiveSamasantaSutra(72, "पथो विभाषा", true, SamasaType.NAN_TATPURUSA, 40) {
    override fun matches(context: SamasaRuleContext) = context.padas.size >= 2 && context.uttaraPada.upadesha == "पथिन्"
    override fun apply(context: SamasaRuleContext) = formed(context,"अपथ")
}

/** 5.4.73 requires a genuinely numerical external referent; lexical shape alone is insufficient. */
object BahuvrihauSankhyeyeDajabahuganatSutra : ActiveSamasantaSutra(73, "बहुव्रीहौ संख्येये डजबहुगणात्", samasaType = SamasaType.BAHUVRIHI) {
    override fun matches(context: SamasaRuleContext) =
        context.padas.size >= 2 && SamasaSemanticRelation.NUMERICAL_REFERENT in context.semanticRelations &&
            context.purvaPada.upadesha !in setOf("बहु", "गण")
    override fun apply(context: SamasaRuleContext) = formed(context,context.padas.joinToString("") { it.upadesha } + "अ")
}

object AcPratyanvavapurvatSamalomnahSutra : ActiveSamasantaSutra(75, "अच् प्रत्यन्ववपूर्वात् सामलोम्नः", samasaType = SamasaType.TATPURUSA) {
    override fun matches(context: SamasaRuleContext) = context.padas.size >= 2 &&
        context.purvaPada.upadesha in setOf("प्रति", "अनु", "अव") && context.uttaraPada.upadesha in setOf("सामन्", "लोमन्")
    override fun apply(context: SamasaRuleContext) = formed(context,context.purvaPada.upadesha + context.uttaraPada.upadesha.removeSuffix("न्"))
}

object UpasargadAdhvanahSutra : ActiveSamasantaSutra(85, "उपसर्गादध्वनः", samasaType = SamasaType.TATPURUSA) {
    override fun matches(context: SamasaRuleContext) = context.padas.size >= 2 && context.uttaraPada.upadesha == "अध्वन्" &&
        Samjna.UPASARGA in context.purvaPada.samjnas
    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val prefix = context.purvaPada.upadesha
        val stem = if (prefix == "प्र") "प्राध्व" else prefix + "अध्व"
        return formed(context,stem)
    }
}

object TatpurusasyangulehSankhyavyayadehSutra : ActiveSamasantaSutra(86, "तत्पुरुषस्याङ्गुलेः संख्याऽव्ययादेः", samasaType = SamasaType.TATPURUSA) {
    private val numerals = setOf("द्वि", "त्रि", "चतुर्", "पञ्च", "षट्", "सप्त", "अष्ट", "नव", "दश")
    override fun matches(context: SamasaRuleContext) = context.padas.size >= 2 && context.uttaraPada.upadesha == "अङ्गुलि" &&
        (context.purvaPada.upadesha in numerals || Samjna.AVYAYA in context.purvaPada.samjnas)
    override fun apply(context: SamasaRuleContext): SamasaRuleResult {
        val prefix = if (context.purvaPada.upadesha == "द्वि") "द्व्य" else context.purvaPada.upadesha
        return formed(context,prefix + "ङ्गुल")
    }
}

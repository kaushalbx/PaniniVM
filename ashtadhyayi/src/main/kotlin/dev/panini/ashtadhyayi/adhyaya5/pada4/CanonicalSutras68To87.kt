package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.analysis.SamasaRuleContext
import dev.panini.analysis.SamasaRuleResult
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Canonical identities whose transformations are intentionally inactive until
 * their full semantic conditions are represented by [SamasaRuleContext].
 * An inactive canonical rule is safer than attaching invented behaviour to its number.
 */
abstract class CanonicalInactiveSamasaSutra(number: Int, text: String) :
    Sutra<SamasaRuleContext, SamasaRuleResult>(
        number = "5.4.$number",
        text = text,
        hindiExplanation = "इस सूत्र की प्रामाणिक पहचान सुरक्षित है; कार्यान्वयन हेतु आवश्यक अर्थ-सन्दर्भ अभी उपलब्ध नहीं है।",
        type = SutraType.NITYA,
        chapter = 5,
        pada = 4,
        optional = false,
        kramaValue = 540000 + number,
        role = SutraRole.Vidhi,
        action = SutraAction.VIDHI,
        scope = SutraScope.DERIVATION,
    ) {
    final override fun matches(context: SamasaRuleContext) = false
    final override fun apply(context: SamasaRuleContext): SamasaRuleResult = SamasaRuleResult.NotApplicable
}

object SamasantahSutra : CanonicalInactiveSamasaSutra(68, "समासान्ताः")
object AcaturadicCanonicalSutra : CanonicalInactiveSamasaSutra(77, "अचतुरविचतुरसुचतुरस्त्रीपुंसधेन्वनडुहर्क्सामवाङ्मनसाक्षिभ्रुवदारगवोर्वष्ठीवपदष्ठीवनक्तंदिवरत्रिंदिवाहर्दिवसरजसनिःश्रेयसपुरुषायुषद्व्यायुषत्र्यायुषर्ग्यजुषजातोक्षमहोक्षवृद्धोक्षोपशुनगोष्ठश्वाः")
object BrahmahastibhyamVarcasahSutra : CanonicalInactiveSamasaSutra(78, "ब्रह्महस्तिभ्यां वर्चसः")
object AvasamandhebhyasTamasahSutra : CanonicalInactiveSamasaSutra(79, "अवसमन्धेभ्यस्तमसः")
object SvasoVasiyahSreyasahSutra : CanonicalInactiveSamasaSutra(80, "श्वसो वसीयःश्रेयसः")
object AnvavataptadRahasahSutra : CanonicalInactiveSamasaSutra(81, "अन्ववतप्ताद्रहसः")
object PraterUrasahSaptamisthatSutra : CanonicalInactiveSamasaSutra(82, "प्रतेरुरसः सप्तमीस्थात्")
object AnugavamAyameSutra : CanonicalInactiveSamasaSutra(83, "अनुगवमायामे")
object DvistavaTristavaVedihSutra : CanonicalInactiveSamasaSutra(84, "द्विस्तावा त्रिस्तावा वेदिः")
object AhasRatrehSutra : CanonicalInactiveSamasaSutra(87, "अहस्सर्वैकदेशसंख्यातपुण्याच्च रात्रेः")

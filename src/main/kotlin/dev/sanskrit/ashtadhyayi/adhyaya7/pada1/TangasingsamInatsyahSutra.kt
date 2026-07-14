package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.ashtadhyayi.adhyaya1.pada3.YathasamkhyamSutra
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.1.12: ṭā-ṅasi-ṅasām-ina-āt-syāḥ.
 * For an a-ending stem, the affixes ṭā, ṅasi, and ṅas are replaced by ina, āt, and sya respectively.
 * Uses 1.3.10 (Yathāsaṃkhyam) logic for ordered mapping.
 */
object TangasingsamInatsyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.12",
    text = "टाङसिङसामिनात्स्याः",
    hindiExplanation = "अकारान्त अङ्ग के बाद टा, ङसि और ङस् के स्थान पर क्रमशः इन, आत् और स्य आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710012,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    dependencies = setOf("6.4.1", "1.3.10")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Jurisdictional check: Must be in Aṅga section
        if ("6.4.1" !in context.activeAdhikaras) return false

        if (context.terms.size < 2) return false
        if (context.effectiveContext.rupa.linga == Linga.STRI) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Stem must end in 'a'
        if (!dev.sanskrit.shiksha.Varnamala.endsWithA(stem.surface) && !dev.sanskrit.shiksha.Varnamala.endsWithAA(stem.surface)) return false

        val replacement = YathasamkhyamSutra.map(affix.upadesha, sources, targets) ?: return false
        return affix.surface != replacement
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val replacement = requireNotNull(YathasamkhyamSutra.map(affix.upadesha, sources, targets))
        
        // Genitive 'sya' often triggers immediate merger in simplified logic, 
        // but here we keep them as distinct terms for the engine to process normally.
        val newState = context.replaceTerm(affix.id, affix.copy(surface = replacement))
        
        return DerivationChange(
            state = newState,
            explanation = "7.1.12: Substituted $replacement for ${affix.upadesha} after a-stem (Yathāsaṃkhyam)."
        )
    }

    private val sources = listOf("टा", "ङसि", "ङस्")
    private val targets = listOf("इन", "आत्", "स्य")
}

package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.ashtadhyayi.adhyaya1.pada3.YathasamkhyamSutra
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 
 * 7.1.1: yuvoranākau. 
 * Replaces the elements 'yu' and 'vu' of an affix with 'ana' and 'aka' respectively.
 * Uses 1.3.10 (Yathāsaṃkhyam) logic.
 */
object YuvoranakauSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.1",
    text = "युवोरनाकौ",
    hindiExplanation = "प्रत्यय के अवयव 'यु' और 'वु' के स्थान पर क्रमशः 'अन' और 'अक' आदेश होते हैं।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710001,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    dependencies = setOf("1.3.10")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Matches if any term (usually an affix) contains 'yu' or 'vu' 
        // as its core content (upadeśa or surface).
        return context.terms.any { it.surface == "यु" || it.surface == "वु" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val target = context.terms.find { it.surface == "यु" || it.surface == "वु" }!!
        val replacement = requireNotNull(YathasamkhyamSutra.map(target.surface, sources, targets))
        
        return DerivationChange(
            state = context.replaceTerm(target.id, target.copy(surface = replacement)),
            explanation = "7.1.1: Substituted $replacement for ${target.surface} (Yuvoranākau)."
        )
    }

    private val sources = listOf("यु", "वु")
    private val targets = listOf("अन", "अक")
}

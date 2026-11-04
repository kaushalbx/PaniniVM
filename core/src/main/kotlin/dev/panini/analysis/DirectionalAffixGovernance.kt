package dev.panini.analysis

import dev.panini.core.TaddhitaAffix
import dev.panini.vyakaranam.ast.AvyayaTaddhitaDerivation

/** Direct affix identity, not a guess from surface -tas/-ena or a direction marker. */
object DirectionalAffixGovernance {
    fun affix(context: VibhaktiRuleContext): TaddhitaAffix? {
        val derivation = context.participant?.governingAvyaya?.derivation as? AvyayaTaddhitaDerivation ?: return null
        // Verified dakṣiṇa/uttara slice; the full atas-artha formation class is open.
        if (derivation.pratipadika !in setOf("दक्षिण", "उत्तर")) return null
        return TaddhitaAffix.fromUpadesha(derivation.pratyaya)
    }
}

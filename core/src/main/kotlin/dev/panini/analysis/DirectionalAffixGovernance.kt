package dev.panini.analysis

import dev.panini.core.TaddhitaAffix
import dev.panini.vyakaranam.ast.AvyayaTaddhitaDerivation

/** Direct affix identity, not a guess from surface -tas/-ena or a direction marker. */
object DirectionalAffixGovernance {
    fun affix(context: VibhaktiRuleContext): TaddhitaAffix? {
        val derivation = context.participant?.governingAvyaya?.derivation as? AvyayaTaddhitaDerivation ?: return null
        val affix = TaddhitaAffix.fromUpadesha(derivation.pratyaya) ?: return null
        // Each affix is tied to the direction words 5.3.27-35 attach it to.
        val bases = when (affix) {
            TaddhitaAffix.ATASUC, TaddhitaAffix.ENAP -> setOf("दक्षिण", "उत्तर")
            TaddhitaAffix.ASTATI, TaddhitaAffix.ASI -> setOf("पूर्व", "अधर", "अवर", "दक्षिण", "उत्तर")
            TaddhitaAffix.RIL, TaddhitaAffix.RISTATIL -> setOf("ऊर्ध्व")
            else -> return null
        }
        return affix.takeIf { derivation.pratipadika in bases }
    }

    /** 2.3.30 names suffixes having the sense of atasuc, not atasuc alone. */
    fun isAtasartha(affix: TaddhitaAffix?): Boolean = affix in setOf(
        TaddhitaAffix.ATASUC, TaddhitaAffix.ASTATI, TaddhitaAffix.ASI, TaddhitaAffix.RIL, TaddhitaAffix.RISTATIL)
}

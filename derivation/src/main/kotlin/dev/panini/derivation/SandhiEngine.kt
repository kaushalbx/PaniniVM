package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.EcoYavayavahSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.EngahPadantadatiSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.EtattadohSulopoKoAnanjparoHaliSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.IkoYanAciSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.VrddhirEciSutra
import dev.panini.ashtadhyayi.adhyaya6.pada3.DhralopePurvasyaDirghonahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.BhoBhagoAghoApurvasyaYoshiSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.DhoDheLopaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.HaliSarveshamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.MonusvarahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.NashcapadantasyaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.JharoJhariSavarneSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.JhayoHonyatarasyamSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.ShashChoAtiSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.StosShcunaShcuhSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.StunaShtuhSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.TorliSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.VaPadantasyaSutra
import dev.panini.shiksha.Samjna

/** Applies the implemented external-sandhi rules to two fully formed padas. */
class SandhiEngine(
    private val engine: DerivationEngine = DerivationEngine(externalSandhiSutras)
) {
    fun join(left: String, right: String): DerivationResult {
        require(left.isNotBlank() && right.isNotBlank()) { "Two words are required for sandhi." }

        val leftTerm = DerivationTerm("sandhi_left", left.trim(), TermKind.PRATIPADIKA, upadesha = left.trim())
        val rightTerm = DerivationTerm("sandhi_right", right.trim(), TermKind.PRATIPADIKA, upadesha = right.trim())
        val initial = DerivationState(
            terms = listOf(leftTerm, rightTerm),
            samjnas = setOf(
                SamjnaAssignment(leftTerm.id, Samjna.PADA),
                SamjnaAssignment(rightTerm.id, Samjna.PADA)
            ),
            stage = DerivationStage.PADA_FORMED
        )
        return engine.derive(initial)
    }

    companion object {
        /** Single grammar-owned registry shared by every external-Sandhi entry point. */
        val externalSandhiSutras: List<DerivationSutra> = listOf(
            SavarnaDirghaSutra,
            IkoYanAciSutra,
            EcoYavayavahSutra,
            AdGunaSutra,
            VrddhirEciSutra,
            EngahPadantadatiSutra,
            StosShcunaShcuhSutra,
            StunaShtuhSutra,
            TorliSutra,
            JhayoHonyatarasyamSutra,
            ShashChoAtiSutra,
            JharoJhariSavarneSutra,
            MonusvarahSutra,
            NashcapadantasyaSutra,
            VaPadantasyaSutra,
            DhoDheLopaSutra,
            DhralopePurvasyaDirghonahSutra,
            BhoBhagoAghoApurvasyaYoshiSutra,
            HaliSarveshamSutra,
            EtattadohSulopoKoAnanjparoHaliSutra
        )
    }
}

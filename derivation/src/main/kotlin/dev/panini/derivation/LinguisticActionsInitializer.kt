package dev.panini.derivation

import dev.panini.actions.linguistic.SandhiAction
import dev.panini.actions.linguistic.SubantaDerivationAction
import dev.panini.core.Karaka
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.ashtadhyayi.adhyaya6.pada1.AdGunaSutra
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

object LinguisticActionsInitializer {
    private var initialized = false

    fun initialize() {
        if (initialized) return
        initialized = true

        val sandhiSutras = listOf(
            SavarnaDirghaSutra,
            IkoYanAciSutra,
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
        val derivationEngine = DerivationEngine(sandhiSutras)

        SandhiAction.sandhiHandler = { left: String, right: String ->
            if (left.isEmpty()) right
            else if (right.isEmpty()) left
            else {
                val terms = listOf(
                    DerivationTerm("term_0", left, TermKind.PRATIPADIKA, upadesha = left),
                    DerivationTerm("term_1", right, TermKind.PRATIPADIKA, upadesha = right)
                )
                val initialState = DerivationState(
                    terms = terms,
                    stage = DerivationStage.PADA_FORMED
                ).withSamjnas(setOf(SamjnaAssignment("term_0", Samjna.PADA), SamjnaAssignment("term_1", Samjna.PADA)))

                val derivationResult = derivationEngine.derive(initialState)
                derivationResult.final.terms.joinToString("") { it.surface }
            }
        }

        SubantaDerivationAction.subantaHandler = { stem: String ->
            val engine = SubantaEngine()
            val request = SubantaDerivationRequest(
                pratipadika = stem,
                vibhakti = Vibhakti.PRATHAMA,
                vacana = Vacana.EKAVACANA,
                stemClass = SubantaStemClass.guess(stem),
            )
            engine.derive(request).final.surface
        }
    }
}

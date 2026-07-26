package dev.panini.actions.linguistic

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
import dev.panini.core.Karaka
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna

/** Sandhi joining (saṃhitā) over text operands using the Panini Ashtadhyayi rules via DerivationEngine. */
object SanskritSandhiAction : dev.panini.execution.DhatuAction("संहिताकरणम्", "पदानां सन्धियोगः") {
    private val sandhiSutras: List<DerivationSutra> = listOf(
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
        EtattadohSulopoKoAnanjparoHaliSutra,
    )

    private val derivationEngine = DerivationEngine(sandhiSutras)

    override fun execute(context: dev.panini.execution.ExecutionContext, operation: dev.panini.execution.DhatuOperation): dev.panini.execution.ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val padas = context.literals(expression)
        val operands = if (padas != null && padas.size >= 2) {
            padas.map { it.prakriti }
        } else {
            context.resolve(expression)
        }

        if (operands.size < 2) {
            return _root_ide_package_.dev.panini.execution.ExecutionResult.Failure(
                _root_ide_package_.dev.panini.execution.ExecutionError.INVALID_VALUE,
                "Sandhi joining requires at least 2 text operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }

        val result = operands.drop(1).fold(operands.first()) { acc, next ->
            applySandhiPair(acc, next)
        }

        return _root_ide_package_.dev.panini.execution.ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Joined ${operands.joinToString(" + ")} via DerivationEngine.",
                "Produced $result.",
            ),
        )
    }

    private fun applySandhiPair(left: String, right: String): String {
        if (left.isEmpty()) return right
        if (right.isEmpty()) return left

        val terms = listOf(
            DerivationTerm("term_0", left, TermKind.PRATIPADIKA, upadesha = left),
            DerivationTerm("term_1", right, TermKind.PRATIPADIKA, upadesha = right)
        )
        val initialState = DerivationState(
            terms = terms,
            stage = DerivationStage.PADA_FORMED
        ).withSamjnas(setOf(SamjnaAssignment("term_0", Samjna.PADA), SamjnaAssignment("term_1", Samjna.PADA)))

        val derivationResult = derivationEngine.derive(initialState)
        return derivationResult.final.terms.joinToString("") { it.surface }
    }
}

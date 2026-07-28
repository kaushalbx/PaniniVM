package dev.panini.actions.control

import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraRelation
import dev.panini.sutra.runtime.SutraSource

/**
 * Kriyā-Viśeṣaṇa Governance Modifier for loop repetition and iteration scope.
 * Modelled as an Adhikāra modifier ([SutraRole.Adhikara]) rather than a primary Kriyā (DhatuAction).
 */
object LoopGovernanceModifier {
    fun createLoopBlueprint(targetSutraId: SutraId, loopCount: Int): SutraBlueprint {
        val modifierId = SutraId("kriya-visheshana.loop.${targetSutraId.value}")
        return SutraBlueprint(
            id = modifierId,
            source = SutraSource.Program(
                grantha = "control-governance",
                location = "loop",
                text = "क्रियायाः पुनः पुनः अनुष्ठानम् (क्रियाविशेषणम्)",
            ),
            role = SutraRole.Vidhi,
            artha = SutraArtha(
                kind = "kriya_visheshana",
                fields = mapOf(
                    "type" to SutraArthaValue.Symbol("loop"),
                    "target" to SutraArthaValue.Symbol(targetSutraId.value),
                    "count" to SutraArthaValue.Number(loopCount.toLong()),
                ),
            ),
            relations = setOf(SutraRelation.PhalaPravaha(source = modifierId, target = targetSutraId)),
            governance = SutraGovernance(),
        )
    }

    fun executeStructured(
        condition: () -> ExecutionResult,
        body: () -> List<ExecutionResult>,
        maximumIterations: Int = 100_000,
    ): List<ExecutionResult> {
        val results = mutableListOf<ExecutionResult>()
        var iterations = 0
        while (true) {
            val conditionResult = condition()
            results += conditionResult
            if (conditionResult !is ExecutionResult.Success) break
            val truth = conditionResult.typedValue as? SanskritValue.Satya
                ?: error("यावत् condition must produce a सत्य value.")
            if (!truth.boolean) break
            check(iterations++ < maximumIterations) {
                "यावत् loop exceeded $maximumIterations iterations."
            }
            val bodyResults = body()
            results += bodyResults
            if (bodyResults.any { it !is ExecutionResult.Success }) break
        }
        return results
    }
}

package dev.panini.execution.sutra

import dev.panini.derivation.LinguisticActionsInitializer
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionScope
import dev.panini.execution.Phala
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.runtime.ExecutionPipeline
import dev.panini.sankhya.SankhyaCountingFormRenderer
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ExecutableUktiSutraMigrationTest {
    @BeforeTest
    fun initializeRegistries() {
        SankhyaCountingFormRenderer.init()
        DhatuPathaRegistration.ensureRegistered()
        LinguisticActionsInitializer.initialize()
    }

    @Test
    fun `one existing command has parity through the runtime sutra adapter`() {
        val conversation = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
        )
        val input = SanskritUktiInput(
            speaker = conversation.speaker,
            listener = conversation.listener,
            text = "दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।",
        )
        val scope = ExecutionScope(capabilities = setOf(ExecutionEffect.PURE))
        val bound = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(input, conversation),
        )

        val legacy = assertIs<Phala.Siddha>(
            ExecutionPipeline.execute(bound.ukti, conversation, scope),
        )

        val program = ExecutableUktiSutraCompiler.compile(bound.ukti)
        val migrated = assertIs<SutraMachineResult.Success<ProgramAvastha>>(
            SutraMachine(ProgramSutraEffectInterpreter(scope)).process(
                program,
                ProgramAvastha(ValueEnvironment()),
            ),
        )
        val migratedPhala = assertIs<Phala.Siddha>(migrated.state.lastPhala)

        assertEquals(legacy.values, migratedPhala.values)
        assertEquals(legacy.typedValues, migratedPhala.typedValues)
        assertEquals(program.sutras.map { it.id }.toSet(), migrated.state.completedSutras)
        assertEquals(1, migrated.trace.size)
    }
}

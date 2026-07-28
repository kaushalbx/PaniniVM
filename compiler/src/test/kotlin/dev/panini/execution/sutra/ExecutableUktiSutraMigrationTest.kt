package dev.panini.execution.sutra

import dev.panini.derivation.LinguisticActionsInitializer
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionScope
import dev.panini.execution.PaniniExecutionArchitecture
import dev.panini.execution.PaniniVM
import dev.panini.execution.Phala
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.runtime.ExecutionPipeline
import dev.panini.sankhya.SankhyaCountingFormRenderer
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.GranthaId
import java.io.File
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
        val grantha = ExecutableUktiSutraCompiler.compileGrantha(bound.ukti)
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
        assertEquals(GranthaId("ukti"), grantha.id)
        assertEquals(program.sutras.map { it.id }.toSet(), grantha.exports)
        assertEquals(1, migrated.trace.size)
    }

    @Test
    fun `multi clause result flow has parity through the sutra pipeline`() {
        val conversation = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
        )
        val input = SanskritUktiInput(
            speaker = conversation.speaker,
            listener = conversation.listener,
            text = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । " +
                "फल + अम् द्वि + औट् च गण + णिच् + लोट् + सिप् । " +
                "फल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।",
        )
        val scope = ExecutionScope(capabilities = setOf(ExecutionEffect.PURE))

        val legacy = assertIs<Phala.Siddha>(
            ExecutionPipeline.execute(input, conversation, scope),
        )
        val migrated = assertIs<Phala.Siddha>(
            SutraExecutionPipeline.execute(input, conversation, scope),
        )

        assertEquals(legacy.values, migrated.values)
        assertEquals(legacy.typedValues, migrated.typedValues)
        assertEquals(legacy.localBindings, migrated.localBindings)
    }

    @Test
    fun `PaniniVM can opt into sutra machine and comparison modes`() {
        val sutraVm = PaniniVM(
            storageDir = temporaryDirectory("sutra"),
            executionArchitecture = PaniniExecutionArchitecture.SUTRA_MACHINE,
        )
        val sutraResult = assertIs<ExecutionResult.Success>(
            sutraVm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"),
        )
        assertEquals("द्वादश", sutraResult.value)

        val compareVm = PaniniVM(
            storageDir = temporaryDirectory("compare"),
            defaultScope = ExecutionScope(capabilities = setOf(ExecutionEffect.PURE)),
            executionArchitecture = PaniniExecutionArchitecture.COMPARE,
        )
        val compared = assertIs<ExecutionResult.Success>(
            compareVm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"),
        )
        assertEquals("द्वादश", compared.value)
    }

    private fun temporaryDirectory(label: String): File =
        File(
            System.getProperty("java.io.tmpdir"),
            "paninivm_sutra_migration_${label}_${java.util.UUID.randomUUID()}",
        )
}

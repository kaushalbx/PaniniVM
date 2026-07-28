package dev.panini.execution.sutra

import dev.panini.derivation.LinguisticActionsInitializer
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.dhatupatha.bhvadi.DrshDhatu
import dev.panini.core.Karaka
import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionScope
import dev.panini.execution.GrammaticalFeatures
import dev.panini.execution.PaniniExecutionArchitecture
import dev.panini.execution.PaniniVM
import dev.panini.execution.Phala
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.VakyaPrayojana
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.runtime.ExecutionPipeline
import dev.panini.sankhya.SankhyaCountingFormRenderer
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.GranthaImport
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraGranthaRegistry
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraNirnaya
import dev.panini.sutra.runtime.SutraSource
import dev.panini.sutra.runtime.toBlueprint
import dev.panini.sutra.SutraRole
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
        assertEquals("kriya", grantha.sutras.single().artha.kind)
        assertEquals(
            SutraArthaValue.Text("युज्"),
            grantha.sutras.single().artha.fields["dhatu"],
        )
        assertEquals(
            SutraArthaValue.Text("युजिँर्"),
            grantha.sutras.single().artha.fields["upadesha"],
        )
        val generatedBlueprint = grantha.sutras.single().toBlueprint().specializedAs(
            SutraId("generated-addition"),
            mapOf("generated" to SutraArthaValue.Truth(true)),
        )
        val compiledBlueprint = assertIs<ProgramBlueprintCompilation.Success>(
            ProgramBlueprintCompiler.compile(
                generatedBlueprint,
                ProgramBlueprintContext(
                    speaker = bound.ukti.speaker,
                    listener = bound.ukti.listener,
                    text = bound.ukti.text,
                    prayojana = bound.ukti.prayojana,
                    polarity = bound.ukti.polarity,
                    lakara = bound.ukti.lakara,
                ),
            ),
        )
        val compiledDecision = assertIs<SutraNirnaya.Applicable<ProgramAvastha>>(
            compiledBlueprint.sutra.evaluator.evaluate(
                compiledBlueprint.sutra,
                ProgramAvastha(ValueEnvironment()),
            ),
        )
        val compiledInvocation = assertIs<InvokeDhatuEffect>(
            compiledDecision.effects.single(),
        ).invocation
        val originalInvocation = bound.ukti.invocations.single()
        assertEquals(originalInvocation.metadata, compiledInvocation.metadata)
        assertEquals(originalInvocation.ambiguousBindings, compiledInvocation.ambiguousBindings)
        assertEquals(originalInvocation.karakaTrace, compiledInvocation.karakaTrace)
        val blueprintResult = assertIs<SutraMachineResult.Success<ProgramAvastha>>(
            SutraMachine(ProgramSutraEffectInterpreter(scope)).process(
                dev.panini.sutra.runtime.SutraProgram(
                    "blueprint-round-trip",
                    listOf(compiledBlueprint.sutra),
                ),
                ProgramAvastha(ValueEnvironment()),
            ),
        )
        val legacyNumber = assertIs<SanskritValue.Sankhya>(
            legacy.typedValues.values.single(),
        )
        val generatedNumber = assertIs<SanskritValue.Sankhya>(
            assertIs<Phala.Siddha>(blueprintResult.state.lastPhala)
                .typedValues["generated-addition"],
        )
        assertEquals(legacyNumber.value, generatedNumber.value)
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

    @Test
    fun `sutra registry reaches reflection action through both execution pipelines`() {
        val public = RuntimeSutra<ProgramAvastha>(
            id = SutraId("public"),
            source = SutraSource.Program("library", "public", "public"),
            role = SutraRole.Vidhi,
            artha = SutraArtha(
                "kriya",
                mapOf("dhatu" to SutraArthaValue.Text("युज्")),
            ),
            evaluator = { _, _ -> SutraNirnaya.Applicable(emptyList()) },
        )
        val library = SutraGrantha(
            GranthaId("library"),
            listOf(public),
            exports = setOf(public.id),
        )
        val application = SutraGrantha(
            GranthaId("application"),
            emptyList<RuntimeSutra<ProgramAvastha>>(),
            imports = listOf(GranthaImport(library.id, "lib")),
        )
        val registry = SutraGranthaRegistry(listOf(library, application))
        val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
        val invocation = DhatuInvocation(
            id = "inspect",
            dhatu = DrshDhatu(),
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("public"),
                Karaka.ADHIKARANA to ExecutionExpression.Pada("lib"),
                Karaka.KARANA to ExecutionExpression.Pada("dhatu"),
            ),
            grammaticalFeatures = GrammaticalFeatures(upasargas = setOf("नि")),
        )
        val ukti = ExecutableUkti(
            speaker = conversation.speaker,
            listener = conversation.listener,
            text = "सूत्रार्थं निरीक्षते",
            prayojana = VakyaPrayojana.AJNA,
            invocations = listOf(invocation),
        )
        val scope = ExecutionScope(
            capabilities = setOf(ExecutionEffect.PURE),
            sutraRegistry = registry,
            currentGrantha = application.id,
        )

        val legacy = assertIs<Phala.Siddha>(
            ExecutionPipeline.execute(ukti, conversation, scope),
        )
        val migrated = assertIs<Phala.Siddha>(
            SutraExecutionPipeline.execute(ukti, conversation, scope),
        )

        assertEquals(SanskritValue.Shabda("युज्"), legacy.typedValues["inspect"])
        assertEquals(legacy.typedValues, migrated.typedValues)
    }

    private fun temporaryDirectory(label: String): File =
        File(
            System.getProperty("java.io.tmpdir"),
            "paninivm_sutra_migration_${label}_${java.util.UUID.randomUUID()}",
        )
}

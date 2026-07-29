package dev.panini.execution.sutra

import dev.panini.derivation.LinguisticActionsInitializer
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.dhatupatha.bhvadi.DrshDhatu
import dev.panini.dhatupatha.rudhadi.VidDhatu
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.core.Karaka
import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionControlRelation
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.ExecutionScope
import dev.panini.execution.GrammaticalFeatures
import dev.panini.execution.PaniniVM
import dev.panini.execution.Phala
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.ValueEnvironment
import dev.panini.execution.VakyaPrayojana
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.sankhya.SankhyaCountingFormRenderer
import dev.panini.sutra.runtime.SutraMachine
import dev.panini.sutra.runtime.SutraMachineResult
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.GranthaImport
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextEncoding
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
    fun `one existing command executes through the runtime sutra adapter`() {
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

        val program = ExecutableUktiSutraCompiler.compile(bound.ukti)
        val blueprintGrantha = ExecutableUktiSutraCompiler.compileBlueprintGrantha(bound.ukti)
        val grantha = ExecutableUktiSutraCompiler.compileGrantha(bound.ukti)
        val migrated = assertIs<SutraMachineResult.Success<ProgramAvastha>>(
            SutraMachine(ProgramSutraEffectInterpreter(scope)).process(
                program,
                ProgramAvastha(ValueEnvironment()),
            ),
        )
        val migratedPhala = assertIs<Phala.Siddha>(migrated.state.lastPhala)

        assertEquals(mapOf("योग-1" to "द्वादश"), migratedPhala.values)
        assertEquals(
            12L,
            assertIs<SanskritValue.Sankhya>(migratedPhala.typedValues["योग-1"]).value,
        )
        assertEquals(program.sutras.map { it.id }.toSet(), migrated.state.completedSutras)
        assertEquals(GranthaId("ukti"), grantha.id)
        assertEquals(GranthaId("ukti"), blueprintGrantha.id)
        assertEquals(program.sutras.map { it.id }.toSet(), grantha.exports)
        assertEquals(grantha.exports, blueprintGrantha.exports)
        assertEquals(
            blueprintGrantha.sutras,
            grantha.sutras.map { it.toBlueprint() },
        )
        assertEquals("kriya", grantha.sutras.single().artha.kind)
        assertEquals(
            SutraArthaValue.Text("युज्"),
            grantha.sutras.single().artha.fields["dhatu"],
        )
        assertEquals(
            SutraArthaValue.Text("युजिँर्"),
            grantha.sutras.single().artha.fields["upadesha"],
        )
        assertEquals(
            SutraArthaValue.Symbol(bound.ukti.invocations.single().dhatu.id),
            grantha.sutras.single().artha.fields["dhatuId"],
        )
        val generatedBlueprint = blueprintGrantha.sutras.single().specializedAs(
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
        val generatedNumber = assertIs<SanskritValue.Sankhya>(
            assertIs<Phala.Siddha>(blueprintResult.state.lastPhala)
                .typedValues["generated-addition"],
        )
        assertEquals(12L, generatedNumber.value)
        assertEquals(1, migrated.trace.size)
    }

    @Test
    fun `conditional duration repeatedly evaluates ordinary kriya effects`() {
        val condition = DhatuInvocation(
            id = "condition",
            dhatu = VidDhatu(),
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(3, "त्रि"),
                    ExecutionExpression.Reference("body"),
                ),
            ),
        )
        val body = DhatuInvocation(
            id = "body",
            dhatu = YujirDhatu(),
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Reference("body"),
                    ExecutionExpression.sankhya(1, "एक"),
                ),
            ),
        )
        val ukti = ExecutableUkti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "यावत् अवस्था तावत् क्रिया",
            prayojana = VakyaPrayojana.AJNA,
            invocations = listOf(condition, body),
            controlRelations = setOf(
                ExecutionControlRelation.ConditionalDuration(
                    condition = condition.id,
                    body = body.id,
                    maximumIterations = 10,
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<ProgramAvastha>>(
            SutraMachine(
                ProgramSutraEffectInterpreter(
                    ExecutionScope(capabilities = setOf(ExecutionEffect.PURE)),
                ),
            ).process(
                ExecutableUktiSutraCompiler.compile(ukti),
                ProgramAvastha(
                    ValueEnvironment(
                        mapOf("body" to SanskritValue.Sankhya(0, "शून्य")),
                    ),
                ),
            ),
        )
        assertEquals(
            3L,
            assertIs<SanskritValue.Sankhya>(result.state.invocationValues["body"]).value,
        )
        assertEquals(
            false,
            assertIs<SanskritValue.Satya>(result.state.invocationValues["condition"]).boolean,
        )
        assertEquals(setOf(SutraId("condition"), SutraId("body")), result.state.completedSutras)
    }

    @Test
    fun `multi clause result flows through canonical grantha source`() {
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
        val bound = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(input, conversation),
        )

        val migrated = assertIs<Phala.Siddha>(
            SutraExecutionPipeline.execute(input, conversation, scope),
        )
        assertEquals(3, migrated.values.size)

        val sourceGrantha = ExecutableUktiSutraCompiler.compileBlueprintGrantha(bound.ukti)
        val generatedGrantha = sourceGrantha.copy(
            id = GranthaId("generated-program"),
            sutras = sourceGrantha.sutras.reversed(),
        )
        val generatedSource = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(generatedGrantha),
        ).text
        val planning = assertIs<ProgramGranthaPlanning.Success>(
            ProgramBlueprintGranthaPlanner.plan(
                generatedGrantha,
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
        val generatedExecution = assertIs<ProgramGranthaExecution.Completed>(
            ProgramBlueprintGranthaEngine.execute(
                generatedSource,
                ProgramBlueprintContext(
                    speaker = bound.ukti.speaker,
                    listener = bound.ukti.listener,
                    text = bound.ukti.text,
                    prayojana = bound.ukti.prayojana,
                    polarity = bound.ukti.polarity,
                    lakara = bound.ukti.lakara,
                ),
                scope,
                ProgramAvastha(ValueEnvironment()),
            ),
        )
        val generatedResult = assertIs<SutraMachineResult.Success<ProgramAvastha>>(
            generatedExecution.result,
        )

        assertEquals(
            sourceGrantha.sutras.map { it.id.value },
            planning.program.invocations.map { it.id },
        )
        assertEquals(bound.ukti.dependencies, planning.program.dependencies)
        assertEquals(sourceGrantha.sutras.map { it.id }, generatedResult.trace.map { it.sutraId })
        assertEquals(migrated.typedValues, generatedResult.state.invocationValues)
        assertEquals(migrated.localBindings, generatedResult.state.localBindings)
    }

    @Test
    fun `PaniniVM executes through the sutra machine`() {
        val sutraVm = PaniniVM(
            storageDir = temporaryDirectory("sutra"),
        )
        val sutraResult = assertIs<ExecutionResult.Success>(
            sutraVm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"),
        )
        assertEquals("द्वादश", sutraResult.value)
    }

    @Test
    fun `executing grantha registers itself and resolves an imported sutra`() {
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
            sutraRegistry = SutraGranthaRegistry(listOf(library)),
        )
        val inspectingGrantha = ExecutableUktiSutraCompiler
            .compileBlueprintGrantha(ukti, application.id)
            .copy(imports = application.imports)
        val execution = assertIs<ProgramGranthaExecution.Completed>(
            ProgramBlueprintGranthaEngine.execute(
                inspectingGrantha,
                ProgramBlueprintContext(
                    speaker = ukti.speaker,
                    listener = ukti.listener,
                    text = ukti.text,
                    prayojana = ukti.prayojana,
                ),
                scope,
                ProgramAvastha(ValueEnvironment()),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<ProgramAvastha>>(execution.result)
        assertEquals(SanskritValue.Shabda("युज्"), result.state.invocationValues["inspect"])
    }

    @Test
    fun `a grantha sutra can inspect a sibling sutra in its own artha`() {
        val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
        val targetUkti = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(
                SanskritUktiInput(
                    speaker = conversation.speaker,
                    listener = conversation.listener,
                    text = "दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।",
                ),
                conversation,
            ),
        ).ukti
        val granthaId = GranthaId("self-reflective")
        val target = ExecutableUktiSutraCompiler
            .compileBlueprintGrantha(targetUkti, granthaId)
            .sutras
            .single()
        val inspectUkti = ExecutableUkti(
            speaker = conversation.speaker,
            listener = conversation.listener,
            text = "स्वसूत्रार्थं निरीक्षते",
            prayojana = VakyaPrayojana.AJNA,
            invocations = listOf(
                DhatuInvocation(
                    id = "inspect-self",
                    dhatu = DrshDhatu(),
                    bindings = mapOf(
                        Karaka.KARMAN to ExecutionExpression.Pada(target.id.value),
                        Karaka.KARANA to ExecutionExpression.Pada("dhatu"),
                    ),
                    grammaticalFeatures = GrammaticalFeatures(upasargas = setOf("नि")),
                ),
            ),
        )
        val inspector = ExecutableUktiSutraCompiler
            .compileBlueprintGrantha(inspectUkti, granthaId)
            .sutras
            .single()
        val grantha = SutraBlueprintGrantha(
            id = granthaId,
            sutras = listOf(target, inspector),
            exports = setOf(target.id, inspector.id),
        )
        val source = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(grantha),
        ).text

        val execution = assertIs<ProgramGranthaExecution.Completed>(
            ProgramBlueprintGranthaEngine.execute(
                source,
                ProgramBlueprintContext(
                    speaker = conversation.speaker,
                    listener = conversation.listener,
                    text = "self-reflective.sutra",
                ),
                ExecutionScope(capabilities = setOf(ExecutionEffect.PURE)),
                ProgramAvastha(ValueEnvironment()),
            ),
        )

        val result = assertIs<SutraMachineResult.Success<ProgramAvastha>>(execution.result)
        assertEquals(
            SanskritValue.Shabda("युज्"),
            result.state.invocationValues["inspect-self"],
        )
    }

    private fun temporaryDirectory(label: String): File =
        File(
            System.getProperty("java.io.tmpdir"),
            "paninivm_sutra_migration_${label}_${java.util.UUID.randomUUID()}",
        )
}

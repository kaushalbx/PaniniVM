package dev.panini.execution

import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.sutra.ExecutableUktiSutraCompiler
import dev.panini.execution.sutra.ProgramBlueprintCompilation
import dev.panini.execution.sutra.ProgramBlueprintCompiler
import dev.panini.execution.sutra.ProgramBlueprintContext
import dev.panini.execution.sutra.ProgramBlueprintDiagnosticCode
import dev.panini.execution.sutra.SutraExecutionPipeline
import dev.panini.execution.sutra.SutraPipelineContinuation
import dev.panini.execution.external.ExternalCapabilityDispatcher
import dev.panini.execution.persistence.FileStateStore
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.shiksha.Samjna
import java.nio.file.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.io.TempDir

class ExecutionLifecycleTest {
    @TempDir
    lateinit var storageDir: Path

    @Test
    fun `conditional execution selects only the matching branch`() {
        val result = assertIs<Phala.Siddha>(
            SutraExecutionPipeline.execute(
                SanskritUktiInput(
                    speaker = "प्रयोक्ता",
                    listener = "यन्त्रम्",
                    text = "यदि दश + अम् द्वि + अम् च विद् + णिच् + लोट् + सिप् " +
                        "तर्हि एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् " +
                        "अन्यथा द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।",
                ),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                PaniniVM().defaultScope,
            ),
        )

        assertEquals(setOf("योग-1", "योग-2"), result.typedValues.keys)
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(result.typedValues.getValue("योग-2")).value)
    }

    @Test
    fun `conditional execution selects the alternate when condition is false`() {
        val result = assertIs<Phala.Siddha>(
            SutraExecutionPipeline.execute(
                SanskritUktiInput(
                    speaker = "प्रयोक्ता",
                    listener = "यन्त्रम्",
                    text = "यदि एक + अम् द्वि + अम् च विद् + णिच् + लोट् + सिप् " +
                        "तर्हि एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् " +
                        "अन्यथा द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।",
                ),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                PaniniVM().defaultScope,
            ),
        )

        assertEquals(setOf("योग-1", "योग-3"), result.typedValues.keys)
        assertEquals(5L, assertIs<SanskritValue.Sankhya>(result.typedValues.getValue("योग-3")).value)
    }

    @Test
    fun `conditional branch resumes after approval without entering alternate`() {
        val vm = PaniniVM()
        val scope = vm.defaultScope
        val paused = assertIs<Phala.AnumatiApekshita>(
            SutraExecutionPipeline.execute(
                SanskritUktiInput(
                    speaker = "प्रयोक्ता",
                    listener = "यन्त्रम्",
                    text = "यदि दश + अम् द्वि + अम् च विद् + णिच् + लोट् + सिप् " +
                        "तर्हि वार्ता + अम् प्रेष् + णिच् + लोट् + सिप् " +
                        "अन्यथा द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ।",
                ),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                scope,
            ),
        )
        val continuation = assertIs<SutraPipelineContinuation>(paused.pipelineContinuation)

        val resumed = SutraExecutionPipeline.resume(
            continuation,
            scope.copy(
                capabilities = scope.capabilities + paused.effects,
                externalDispatcher = ExternalCapabilityDispatcher(),
            ),
        )
        val completed = assertIs<Phala.Siddha>(resumed, resumed.toString())

        assertEquals(setOf("योग-1", "योग-2"), completed.typedValues.keys)
    }

    @Test
    fun `planner honors dependencies rather than source order`() {
        val first = invocation("first")
        val second = invocation("second")
        val third = invocation("third")
        val program = program(
            invocations = listOf(third, first, second),
            dependencies = setOf(
                ActionDependency("first", "second"),
                ActionDependency("second", "third"),
            ),
        )

        val planned = assertIs<PlanningResult.Planned>(ExecutionPlanner.plan(program, ValueEnvironment()))

        assertEquals(listOf("first", "second", "third"), planned.plans.map { it.invocationId })
    }

    @Test
    fun `planner rejects cyclic dependencies`() {
        val program = program(
            invocations = listOf(invocation("first"), invocation("second")),
            dependencies = setOf(
                ActionDependency("first", "second"),
                ActionDependency("second", "first"),
            ),
        )

        val failure = assertIs<PlanningResult.Failed>(ExecutionPlanner.plan(program, ValueEnvironment()))
        val result = assertIs<ExecutionResult.Failure>(failure.result)

        assertEquals(ExecutionError.ACTION_FAILED, result.error)
        assertTrue(result.message.contains("cyclic action dependencies"))
    }

    @Test
    fun `approval continuation resumes after required effect is granted`() {
        val planned = plannedProgram(
            invocation("network", effects = setOf(ExecutionEffect.NETWORK)),
            VakyaPrayojana.AJNA,
        )
        val paused = assertIs<Phala.AnumatiApekshita>(
            ExecutionRuntime.execute(planned, ExecutionScope(capabilities = setOf(ExecutionEffect.PURE))),
        )

        assertEquals(setOf(ExecutionEffect.NETWORK), paused.effects)
        val completed = assertIs<Phala.Siddha>(
            ExecutionRuntime.resume(
                paused.continuation,
                ExecutionScope(capabilities = setOf(ExecutionEffect.PURE, ExecutionEffect.NETWORK)),
            ),
        )

        assertEquals("network-result", completed.values["network"])
    }

    @Test
    fun `acceptance continuation resumes after invocation is accepted`() {
        val planned = plannedProgram(invocation("request"), VakyaPrayojana.PRARTHANA)
        val paused = assertIs<Phala.SvikaraApekshita>(
            ExecutionRuntime.execute(planned, ExecutionScope(capabilities = setOf(ExecutionEffect.PURE))),
        )

        assertEquals("request", paused.invocationId)
        val completed = assertIs<Phala.Siddha>(
            ExecutionRuntime.resume(
                paused.continuation,
                ExecutionScope(
                    capabilities = setOf(ExecutionEffect.PURE),
                    acceptedInvocations = setOf("request"),
                ),
            ),
        )

        assertEquals("request-result", completed.values["request"])
    }

    @Test
    fun `VM sessions persist independently and survive a new VM instance`() {
        val directory = storageDir.toFile()
        val vm = PaniniVM(directory)
        val addition = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"

        assertIs<ExecutionResult.Success>(vm.eval(addition, sessionKey = "alpha"))
        assertIs<ExecutionResult.Success>(vm.eval(addition, sessionKey = "alpha"))
        assertIs<ExecutionResult.Success>(vm.eval(addition, sessionKey = "beta"))

        val alpha = requireNotNull(vm.loadSession("alpha"))
        val beta = requireNotNull(vm.loadSession("beta"))
        assertEquals(2, alpha.turnNumber)
        assertEquals(1, beta.turnNumber)
        assertNotEquals(alpha.resultHistory.size, beta.resultHistory.size)

        val restarted = PaniniVM(directory)
        assertEquals(alpha, restarted.loadSession("alpha"))
        assertEquals(setOf("alpha", "beta"), restarted.listSessions().toSet())
    }

    @Test
    fun `successful session turns automatically remember kriya frames with typed phala`() {
        val memoryDirectory = storageDir.resolve("kriya-memory").toFile()
        val vm = PaniniVM(memoryDirectory)

        assertIs<ExecutionResult.Success>(
            vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "memory"),
        )

        val remembered = vm.kriyaMemory("memory").latest().single()
        assertEquals(1, remembered.turn)
        assertEquals("युज्", remembered.frame.kriya?.analysis?.pada?.dhatu?.mulaDhatu)
        assertEquals(2, remembered.frame.relations.size)
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(remembered.phala).value)
        assertTrue(Samjna.SANKHYA in requireNotNull(remembered.phala).samjnas)
        assertEquals(remembered, vm.kriyaMemory("memory").latestKriyas("युजिँर्").single())
        assertEquals(2, vm.kriyaMemory("memory").latestKarakaRelations(Karaka.KARMAN, count = 2).size)

        val restored = PaniniVM(memoryDirectory).kriyaMemory("memory").latest().single()
        assertEquals(remembered, restored)
        assertTrue(restored.frame.relations.all { it.kriyaId == restored.frame.id })

        val recalled = assertIs<Phala.Siddha>(
            SutraExecutionPipeline.execute(
                SanskritUktiInput(
                    speaker = "प्रयोक्ता",
                    listener = "यन्त्रम्",
                    text = "युज् + घञ् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                ),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                vm.defaultScope,
                dev.panini.execution.memory.KriyaMemory(listOf(restored)),
            ),
        )
        assertEquals(remembered.phala.toDisplayText(), recalled.typedValues.values.single().toDisplayText())
    }

    @Test
    fun `resumed session turn remembers its kriya exactly once`() {
        val vm = PaniniVM(storageDir.resolve("resumed-memory").toFile())
        val paused = assertIs<ExecutionResult.NeedsApproval>(
            vm.eval("वार्ता + अम् प्रेष् + णिच् + लोट् + सिप् ।", sessionKey = "resume"),
        )
        assertTrue(vm.kriyaMemory("resume").entries.isEmpty())
        val approvedScope = vm.defaultScope.copy(
            capabilities = vm.defaultScope.capabilities + paused.requiredEffects,
        )

        assertIs<ExecutionResult.Success>(vm.resume(paused.continuation, "resume", approvedScope))
        assertIs<ExecutionResult.Failure>(vm.resume(paused.continuation, "resume", approvedScope))

        val remembered = vm.kriyaMemory("resume").entries.single()
        assertEquals("प्रेषँ", remembered.frame.kriya?.dhatu?.upadesha)
        assertEquals(1, remembered.turn)
    }

    @Test
    fun `state store round trips nested typed collections`() {
        val store = FileStateStore(storageDir.resolve("typed-state").toFile())
        val value = SanskritValue.Gana(
            listOf(
                SanskritValue.Sankhya(2, "द्वि"),
                SanskritValue.Suchi(
                    listOf(SanskritValue.Shabda("शब्द"), SanskritValue.Satya(true), SanskritValue.Lopa),
                ),
            ),
        )
        val context = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            previousResults = mapOf("फल" to value.toDisplayText()),
            previousResultSamjnas = mapOf("फल" to value.samjnas),
            previousTypedResults = mapOf("फल" to value),
            resultHistory = listOf(SmrtaPhala("उक्ति-१/योग-1", 1, "योग-1", value.toDisplayText(), value.samjnas, value)),
            turnNumber = 1,
        )

        store.save("collections", context)

        assertEquals(context, store.load("collections"))
    }

    @Test
    fun `eval reports terminal script failure after an earlier success`() {
        val vm = PaniniVM(storageDir.resolve("script-result").toFile())
        val script = """
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            अज्ञात् + लोट् + सिप् ।
        """.trimIndent()
        assertIs<ExecutionResult.Failure>(vm.evalScript(script).last())

        assertIs<ExecutionResult.Failure>(vm.eval(script))
    }

    @Test
    fun `source classification uses parsed statements instead of text markers`() {
        val utterance = """
            एक + अम् द्वि + औट् च
            युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        assertEquals(PvmSourceKind.UTTERANCE, PvmScript.classify(utterance))
        assertEquals("panini.eval", assertIs<ExecutionResult.Success>(PaniniVM().eval(utterance)).operation)
        assertEquals(PvmSourceKind.UTTERANCE, PvmScript.classify("गणित + सुँ ।"))
        assertEquals(
            PvmSourceKind.SCRIPT,
            PvmScript.classify("गणित + सुँ इति संज्ञा + सुँ ।"),
        )
        assertEquals(
            PvmSourceKind.UTTERANCE,
            PvmScript.classify("संज्ञा + सुँ ।"),
            "A marker lexeme without the parsed इति construction remains an utterance.",
        )

        val script = "$utterance\n$utterance"
        assertEquals(PvmSourceKind.SCRIPT, PvmScript.classify(script))
    }

    @Test
    fun `kriya memory distinguishes ordinal previous and latest results`() {
        val vm = PaniniVM(storageDir.resolve("ordered-memory").toFile())
        assertIs<ExecutionResult.Success>(
            vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "order"),
        )
        assertIs<ExecutionResult.Success>(
            vm.eval("त्रि + अम् चतुर् + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "order"),
        )

        val memory = vm.kriyaMemory("order")
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(memory.ordinalKriya(1)?.phala).value)
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(memory.latestKriya(offset = 1)?.phala).value)
        assertEquals(7L, assertIs<SanskritValue.Sankhya>(memory.latestKriya()?.phala).value)
        assertEquals(null, memory.latestKriya("युजिँर्", offset = 2))

        val previousResult = assertIs<ExecutionResult.Success>(
            vm.eval(
                "युज् + ल्युट् + ङस् पूर्व + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                sessionKey = "order",
            ),
        )
        assertEquals("त्रीणि", previousResult.value)

        val firstResult = assertIs<ExecutionResult.Success>(
            vm.eval(
                "युज् + ल्युट् + ङस् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                sessionKey = "order",
            ),
        )
        assertEquals("त्रीणि", firstResult.value)

        val secondResult = assertIs<ExecutionResult.Success>(
            vm.eval(
                "युज् + ल्युट् + ङस् द्वि + तीय + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                sessionKey = "order",
            ),
        )
        assertEquals("सप्त", secondResult.value)

        val rememberedObjects = assertIs<ExecutionResult.Success>(
            vm.eval(
                "युज् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                sessionKey = "order",
            ),
        )
        assertEquals("त्रि चतुर्", rememberedObjects.value)

        val firstObjects = assertIs<ExecutionResult.Success>(
            vm.eval(
                "युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                sessionKey = "order",
            ),
        )
        assertEquals("एक द्वि", firstObjects.value)

        val previousObjects = assertIs<ExecutionResult.Success>(
            vm.eval(
                "युज् + ल्युट् + ङस् पूर्व + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                sessionKey = "order",
            ),
        )
        assertEquals("एक द्वि", previousObjects.value)
    }

    @Test
    fun `structured references distinguish samasa and kridanta identities across cases`() {
        val source = storageDir.resolve("structured-references.pvm").toFile()
        source.writeText(
            """
            एक + अम् पूर्व-पद + ङे दा + लोट् + सिप् ।
            द्वि + अम् जन् + ल्युट् + ङे दा + लोट् + सिप् ।
            त्रि + अम् जन् + घञ् + ङे दा + लोट् + सिप् ।
            मुद्र् + णिच् + लोट् + सिप् पूर्व-पद + अम् ।
            मुद्र् + णिच् + लोट् + सिप् जन् + ल्युट् + अम् ।
            मुद्र् + णिच् + लोट् + सिप् जन् + घञ् + अम् ।
            """.trimIndent(),
        )

        val results = PaniniVM(storageDir.resolve("sessions").toFile()).evalFile(source)

        assertEquals(listOf("एक", "द्वि", "त्रि"), results.takeLast(3).map { assertIs<ExecutionResult.Success>(it).value })
    }

    @Test
    fun `readable Sanskrit generation is explicit and eval does not rewrite it`() {
        val source = storageDir.resolve("addition.pvm").toFile()
        val readable = storageDir.resolve("addition.txt").toFile()
        source.writeText("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n")
        readable.writeText("sentinel\n")

        assertIs<ExecutionResult.Success>(PaniniVM(storageDir.resolve("sessions").toFile()).evalFile(source).single())
        assertEquals("sentinel\n", readable.readText())

        assertEquals(readable, PvmReadableSanskrit.renderFile(source))
        assertEquals("एकम् द्वे च योजय ।\n", readable.readText())
    }

    @Test
    fun `operation resolver reports incomparable overloads as ambiguous`() {
        val ambiguousDhatu = dhatu(
            operations = listOf(
                operation("first-overload"),
                operation("second-overload"),
            ),
        )
        val resolution = OperationResolver.resolve(
            DhatuInvocation("ambiguous", ambiguousDhatu, emptyMap()),
            emptyMap(),
        )

        val ambiguous = assertIs<OperationResolution.Ambiguous>(resolution)
        assertEquals(setOf("first-overload", "second-overload"), ambiguous.operations.toSet())
    }

    @Test
    fun `blueprint compiler rejects surface form in place of exact upadesha`() {
        val registered = DhatuPathaRegistration.resolve("एधँ").first()
        val blueprint = ExecutableUktiSutraCompiler.compileBlueprintGrantha(
            program(listOf(DhatuInvocation("invoke", registered, emptyMap())), emptySet()).ukti,
        ).sutras.single()
        val wrongFields = blueprint.artha.fields + ("upadesha" to SutraArthaValue.Text(registered.sourceSurface))
        val compilation = ProgramBlueprintCompiler.compile(
            blueprint.copy(artha = blueprint.artha.copy(fields = wrongFields)),
            ProgramBlueprintContext("speaker", "listener", "exact upadesha test"),
        )

        val invalid = assertIs<ProgramBlueprintCompilation.Invalid>(compilation)
        assertTrue(invalid.diagnostics.any { diagnostic ->
            diagnostic.code == ProgramBlueprintDiagnosticCode.UNKNOWN_DHATU &&
                diagnostic.message.contains("exact upadeśa")
        })
    }

    private fun plannedProgram(
        invocation: DhatuInvocation,
        prayojana: VakyaPrayojana,
    ): PlanningResult.Planned = assertIs(
        ExecutionPlanner.plan(
            program(listOf(invocation), emptySet(), prayojana),
            ValueEnvironment(),
        ),
    )

    private fun program(
        invocations: List<DhatuInvocation>,
        dependencies: Set<ActionDependency>,
        prayojana: VakyaPrayojana = VakyaPrayojana.AJNA,
    ): ExecutionProgram = ExecutionProgram(
        ExecutableUkti(
            speaker = "speaker",
            listener = "listener",
            text = "execution lifecycle test",
            prayojana = prayojana,
            invocations = invocations,
            dependencies = dependencies,
        ),
        dependencies,
    )

    private fun invocation(
        id: String,
        effects: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE),
    ): DhatuInvocation = DhatuInvocation(id, dhatu(listOf(operation("$id-operation", effects))), emptyMap())

    private fun dhatu(operations: List<DhatuOperation>): Dhatu = Dhatu(
        id = "test.${operations.hashCode()}",
        krama = 1,
        upadesha = "परीक्ष्",
        sourceSurface = "परीक्ष्",
        artha = "परीक्षणे",
        arthaHindi = "परीक्षण",
        arthaEnglish = "test",
        gana = DhatuGana.BHVADI,
        operations = operations,
    )

    private fun operation(
        name: String,
        effects: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE),
    ): DhatuOperation = DhatuOperation(
        signature = OperationSignature(emptyList()),
        action = object : DhatuAction(name, "test action") {
            override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult =
                ExecutionResult.Success(
                    value = name.substringBefore("-operation") + "-result",
                    operation = name,
                )
        },
        effects = effects,
    )
}

package dev.panini.execution

import dev.panini.core.DhatuGana
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.sutra.ExecutableUktiSutraCompiler
import dev.panini.execution.sutra.ProgramBlueprintCompilation
import dev.panini.execution.sutra.ProgramBlueprintCompiler
import dev.panini.execution.sutra.ProgramBlueprintContext
import dev.panini.execution.sutra.ProgramBlueprintDiagnosticCode
import dev.panini.sutra.runtime.SutraArthaValue
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
    fun `readable Sanskrit generation is explicit and eval does not rewrite it`() {
        val source = storageDir.resolve("addition.pvm").toFile()
        val readable = storageDir.resolve("addition.txt").toFile()
        source.writeText("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n")
        readable.writeText("sentinel\n")

        assertIs<ExecutionResult.Success>(PaniniVM(storageDir.resolve("sessions").toFile()).evalFile(source).single())
        assertEquals("sentinel\n", readable.readText())

        assertEquals(readable, PvmReadableSanskrit.renderFile(source))
        assertEquals("एकम् द्वी च योजय ।\n", readable.readText())
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

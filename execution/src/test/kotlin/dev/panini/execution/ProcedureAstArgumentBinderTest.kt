package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.depthFirst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import java.io.File

class ProcedureAstArgumentBinderTest {
    @Test
    fun `ordinal placeholders bind in stored AST without reparsing`() {
        val definition = PvmScript.parse(
            """
            गणित + ङस् वृध् + ल्युट् + सुँ ।
            प्रथ् + अमच् + अम् द्वि + तीय + अम् च युज् + लोट् + सिप् ॥
            """.trimIndent(),
        ).single() as PvmScriptStatement.SamjnaDefinition

        val bound = ProcedureAstArgumentBinder.bind(
            requireNotNull(definition.body.single().program),
            parameters = emptyList(),
            argumentCount = 2,
        )
        val operands = bound.depthFirst().filterIsInstance<Invocation>().single().vakya.padas
            .flatMap {
                when (it) {
                    is dev.panini.vyakaranam.ast.SamuccitaSubanta -> it.members.map { member -> member.pratipadika.sourceText }
                    is dev.panini.vyakaranam.ast.SubantaPada -> listOf(it.pratipadika.sourceText)
                    else -> emptyList()
                }
            }

        assertEquals(
            listOf("__pvm_parameter_0", "__pvm_parameter_1"),
            operands.take(2),
            bound.depthFirst().filterIsInstance<Invocation>().single().vakya.padas
                .joinToString { "${it::class.simpleName}:${it.sourceText}" },
        )
    }

    @Test
    fun `call frame parameters shadow caller values without mutating caller scope`() {
        val kriya = SamjnaKriya(
            nameSegmented = "युज् + ल्युट् + सुँ",
            nameStem = "युज् + ल्युट्",
            body = emptyList(),
            signatureOverride = SamjnaSignature(
                parameters = listOf(SamjnaParameter("वाम", SamjnaValueType.SANKHYA)),
            ),
        )
        val argument = SanskritValue.Sankhya(2, "द्वि")
        val invocation = SamjnaInvocation(
            kriya = kriya,
            karmaText = "द्वि + अम्",
            fullText = "",
            arguments = listOf(
                ProcedureArgument("द्वि", value = argument, origin = ProcedureArgumentOrigin.PIPE),
            ),
        )
        val callerScope = ExecutionScope(
            environment = ValueEnvironment(mapOf("वाम" to SanskritValue.Sankhya(99, "नवनवतिः"))),
        )

        val frame = ProcedureCallFrame.create(invocation, listOf("द्वि"), callerScope, "caller.pvm")

        assertEquals(argument, frame.localScope.environment.values["वाम"])
        assertEquals(99, (callerScope.environment.values["वाम"] as SanskritValue.Sankhya).value)
    }

    @Test
    fun `call frame preserves list range and structured argument identities`() {
        val values = listOf(
            SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक"))),
            SanskritValue.Range(SanskritValue.Sankhya(1, "एक"), SanskritValue.Sankhya(10, "दश")),
            SanskritValue.Rupa("फलरूप", mapOf("मान" to SanskritValue.Sankhya(2, "द्वि"))),
        )
        val parameters = listOf("सूची", "सीमा", "रूप").map {
            SamjnaParameter(it, SamjnaValueType.SHABDA)
        }
        val invocation = SamjnaInvocation(
            kriya = SamjnaKriya(
                nameSegmented = "वह् + ल्युट् + सुँ",
                nameStem = "वह् + ल्युट्",
                body = emptyList(),
                signatureOverride = SamjnaSignature(parameters = parameters),
            ),
            karmaText = "",
            fullText = "",
            arguments = values.mapIndexed { index, value ->
                ProcedureArgument("मान$index", value = value, origin = ProcedureArgumentOrigin.PIPE)
            },
        )

        val frame = ProcedureCallFrame.create(
            invocation,
            invocation.arguments.map(ProcedureArgument::term),
            ExecutionScope(),
            null,
        )

        assertEquals(values, frame.arguments)
        assertEquals(values, parameters.map { frame.parameterBindings.getValue(it.nameStem) })
    }

    @Test
    fun `procedure execution does not reparse rendered body text`() {
        val source = File("execution/src/main/kotlin/dev/panini/execution/PvmScriptExecutor.kt").readText()

        assertFalse("PvmScript.parse(sentenceText)" in source)
    }
}

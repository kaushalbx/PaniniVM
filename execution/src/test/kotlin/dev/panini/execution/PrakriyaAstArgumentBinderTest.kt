package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.depthFirst
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import java.io.File

class PrakriyaAstArgumentBinderTest {
    @Test
    fun `string only pipeline arguments cannot impersonate parsed parameters`() {
        val pipeline = Pipeline(
            sourceText = "legacy compatibility node",
            arguments = listOf("वाम"),
            stages = emptyList(),
            argumentPadas = emptyList(),
        )

        val bound = assertIs<Pipeline>(
            PrakriyaAstArgumentBinder.bind(
                pipeline,
                parameters = listOf(PrakriyaParameter("वाम", PrakriyaValueType.SANKHYA)),
                argumentCount = 1,
            ),
        )

        assertEquals(listOf("वाम"), bound.arguments)
    }

    @Test
    fun `ordinal placeholders bind in stored AST without reparsing`() {
        val definition = PvmScript.parse(
            """
            गणित + ङस् वृध् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथ् + अमच् + अम् द्वि + तीय + अम् च युज् + लोट् + सिप् ॥
            """.trimIndent(),
        ).single() as PvmScriptStatement.PrakriyaDefinition

        val bound = PrakriyaAstArgumentBinder.bind(
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
        val kriya = Prakriya(
            nameSegmented = "युज् + ल्युट् + सुँ",
            nameStem = "युज् + ल्युट्",
            body = emptyList(),
            signatureOverride = PrakriyaSignature(
                parameters = listOf(PrakriyaParameter("वाम", PrakriyaValueType.SANKHYA)),
            ),
        )
        val argument = SanskritValue.Sankhya(2, "द्वि")
        val invocation = PrakriyaInvocation(
            kriya = kriya,
            karmaText = "द्वि + अम्",
            fullText = "",
            arguments = listOf(
                PrakriyaArgument("द्वि", value = argument, origin = PrakriyaArgumentOrigin.PIPE),
            ),
        )
        val callerScope = ExecutionScope(
            environment = ValueEnvironment(mapOf("वाम" to SanskritValue.Sankhya(99, "नवनवतिः"))),
        )

        val frame = PrakriyaCallFrame.createResolved(invocation, resolved(invocation, listOf("द्वि")), callerScope)

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
            PrakriyaParameter(it, PrakriyaValueType.SHABDA)
        }
        val invocation = PrakriyaInvocation(
            kriya = Prakriya(
                nameSegmented = "वह् + ल्युट् + सुँ",
                nameStem = "वह् + ल्युट्",
                body = emptyList(),
                signatureOverride = PrakriyaSignature(parameters = parameters),
            ),
            karmaText = "",
            fullText = "",
            arguments = values.mapIndexed { index, value ->
                PrakriyaArgument("मान$index", value = value, origin = PrakriyaArgumentOrigin.PIPE)
            },
        )

        val frame = PrakriyaCallFrame.createResolved(
            invocation,
            resolved(invocation, invocation.arguments.map(PrakriyaArgument::term)),
            ExecutionScope(),
        )

        assertEquals(values, frame.arguments)
        assertEquals(values, parameters.map { frame.parameterBindings.getValue(it.nameStem) })
    }

    @Test
    fun `call frame distinguishes arguments that share a verbal root`() {
        val plain = SanskritValue.Shabda("योजनम्")
        val prefixed = SanskritValue.Shabda("वियोजनम्")
        val invocation = PrakriyaInvocation(
            kriya = Prakriya(
                nameSegmented = "वह् + ल्युट् + सुँ",
                nameStem = "वह् + ल्युट्",
                body = emptyList(),
                signatureOverride = PrakriyaSignature(
                    parameters = listOf(
                        PrakriyaParameter("पूर्व", PrakriyaValueType.SHABDA),
                        PrakriyaParameter("पर", PrakriyaValueType.SHABDA),
                    ),
                ),
            ),
            karmaText = "",
            fullText = "",
            arguments = listOf(
                PrakriyaArgument("युज् + ल्युट्", value = plain, origin = PrakriyaArgumentOrigin.WRITTEN),
                PrakriyaArgument("वि + युज् + ल्युट्", value = prefixed, origin = PrakriyaArgumentOrigin.WRITTEN),
            ),
        )

        val frame = PrakriyaCallFrame.createResolved(
            invocation,
            resolved(invocation, listOf("वि + युज् + ल्युट्", "युज् + ल्युट्")),
            ExecutionScope(),
        )

        assertEquals(listOf(prefixed, plain), frame.arguments)
    }

    @Test
    fun `positional resolution preserves argument AST objects without text rematching`() {
        val sourcePada = (PvmScript.parse("युज् + ल्युट् + अम् मुद्र् + लोट् + सिप् ।")
            .single() as PvmScriptStatement.Sentence).ukti!!
            .grammaticalVakyas().single().padas.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        val argument = PrakriyaArgument(
            term = "deliberately-not-the-rendered-pada",
            pada = sourcePada,
            value = SanskritValue.Shabda("योजनम्"),
            origin = PrakriyaArgumentOrigin.WRITTEN,
        )
        val invocation = PrakriyaInvocation(
            kriya = Prakriya(
                nameSegmented = "वह् + ल्युट् + सुँ",
                nameStem = "वह् + ल्युट्",
                body = emptyList(),
                signatureOverride = PrakriyaSignature(
                    parameters = listOf(PrakriyaParameter("मान", PrakriyaValueType.SHABDA)),
                ),
            ),
            karmaText = "unrelated compatibility text",
            fullText = "",
            arguments = listOf(argument),
            argumentSyntax = listOf(sourcePada),
        )

        val resolved = assertIs<PrakriyaArgumentResolution.Success>(
            PrakriyaInvocationArgumentResolver.resolve(invocation),
        ).arguments.single()

        assertTrue(resolved.argument === argument)
        assertTrue(resolved.argument.pada === sourcePada)
    }

    @Test
    fun `prakriya execution does not reparse rendered body text`() {
        val source = File("execution/src/main/kotlin/dev/panini/execution/PvmScriptExecutor.kt").readText()

        assertFalse("PvmScript.parse(sentenceText)" in source)
    }

    @Test
    fun `nishedha guard binds ordinal parameters in its stored AST`() {
        val definition = PvmScript.parse(
            """
            विभाज् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            न द्वितीय + अम् शून्य + अम् ।
            प्रथम + अम् द्वितीय + अम् च भाज् + णिच् + लोट् + सिप् ॥
            """.trimIndent(),
        ).single() as PvmScriptStatement.PrakriyaDefinition
        val guard = definition.body.single { it.isNishedha }

        assertTrue(
            NishedhaGuardEvaluator.isProhibited(
                guard,
                parameters = emptyList(),
                argumentValues = listOf(
                    SanskritValue.Sankhya(10, "दश"),
                    SanskritValue.Sankhya(0, "शून्य"),
                ),
            ),
        )
        assertFalse(
            NishedhaGuardEvaluator.isProhibited(
                guard,
                parameters = emptyList(),
                argumentValues = listOf(
                    SanskritValue.Sankhya(10, "दश"),
                    SanskritValue.Sankhya(2, "द्वि"),
                ),
            ),
        )

        val comparisonGuard = (PvmScript.parse(
            """
            तुल् + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            न प्रथम + अम् द्वितीय + अम् ।
            प्रथम + अम् द्वितीय + अम् च युज् + लोट् + सिप् ॥
            """.trimIndent(),
        ).single() as PvmScriptStatement.PrakriyaDefinition).body.single { it.isNishedha }
        assertFalse(
            NishedhaGuardEvaluator.isProhibited(
                comparisonGuard,
                parameters = emptyList(),
                argumentValues = listOf(
                    SanskritValue.Sankhya(11, "एकादश"),
                    SanskritValue.Sankhya(21, "एकविंशति"),
                ),
            ),
            "Compound numerals that share their first stem must remain semantically distinct.",
        )
    }

    private fun resolved(
        invocation: PrakriyaInvocation,
        orderedTerms: List<String>,
    ): List<ResolvedPrakriyaArgument> = orderedTerms.mapIndexed { index, term ->
        ResolvedPrakriyaArgument(
            parameter = invocation.kriya.signature.parameters.getOrNull(index),
            argument = invocation.arguments.single { it.term == term },
            bindingKind = PrakriyaArgumentBindingKind.POSITIONAL,
        )
    }
}

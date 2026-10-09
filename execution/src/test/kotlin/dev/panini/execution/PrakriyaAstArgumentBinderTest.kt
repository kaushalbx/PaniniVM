package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.depthFirst
import dev.panini.vyakaranam.ast.semanticKey
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import java.io.File

class PrakriyaAstArgumentBinderTest {
    @Test
    fun `additional kridanta derivation does not disappear into parameter binding`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val plain = assertIs<Invocation>(parser.parse("युज् + ल्युट् + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        val nominal = assertIs<dev.panini.vyakaranam.ast.SubantaPada>(plain.vakya.padas.first())
        val parameters = listOf(PrakriyaParameter(nominal.pratipadika.semanticKey(), PrakriyaValueType.SHABDA))
        for (suffix in listOf("मतुप्", "तरप्", "टाप्")) {
            val original = assertIs<Invocation>(parser.parse(
                "युज् + ल्युट् + $suffix + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
            assertEquals(original.vakya.padas,
                assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(original, parameters, 1)).vakya.padas)
        }
        val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(plain, parameters, 1))
        assertEquals(PrakriyaAstArgumentBinder.referenceKey(0),
            assertIs<dev.panini.vyakaranam.ast.SubantaPada>(bound.vakya.padas.first()).pratipadika.sourceText)
    }

    @Test
    fun `derived nominal cannot impersonate a plain named parameter`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val parameters = listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA))
        for (suffix in listOf("मतुप्", "तरप्", "टाप्")) {
            val original = assertIs<Invocation>(parser.parse(
                "मान + $suffix + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
            val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(original, parameters, 1))
            assertEquals(original.vakya.padas, bound.vakya.padas)
        }
        val plain = assertIs<Invocation>(parser.parse("मान + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(plain, parameters, 1))
        assertEquals(PrakriyaAstArgumentBinder.referenceKey(0),
            assertIs<dev.panini.vyakaranam.ast.SubantaPada>(bound.vakya.padas.first()).pratipadika.sourceText)
    }

    @Test
    fun `numeric prohibition does not drop an unresolved extra operand`() {
        val definition = PvmScript.parse(
            "वाचन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "न शून्य + अम् राम + अम् शून्य + अम् ।\n" +
                "एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥"
        ).single() as PvmScriptStatement.PrakriyaDefinition
        val guard = definition.body.single { it.isNishedha }
        assertEquals(null, NishedhaGuardEvaluator.numericProhibition(guard, emptyList(), 0))
        assertFalse(NishedhaGuardEvaluator.isProhibited(guard, emptyList(), emptyList()))
    }

    @Test
    fun `prior action member protection respects clause boundaries`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        for ((name, phrase) in listOf(
            "अन्तिम" to "मान + ङस् अन्तिम + अम् उद् + हृ + ल्यप्",
            "सङ्ख्या" to "मान + ङस् सङ्ख्या + शस् युज् + णिच् + क्त्वा",
        )) {
            val original = assertIs<Invocation>(parser.parse(
                "$phrase $name + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
            val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(original,
                listOf(PrakriyaParameter(name, PrakriyaValueType.SANKHYA),
                    PrakriyaParameter("मान", PrakriyaValueType.SUCHI)), 2))
            val nominals = bound.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>()
            assertEquals(PrakriyaAstArgumentBinder.referenceKey(1), nominals[0].pratipadika.sourceText)
            assertEquals(name, nominals[1].pratipadika.sourceText)
            assertEquals(PrakriyaAstArgumentBinder.referenceKey(0), nominals[2].pratipadika.sourceText)
            val clauses = requireNotNull(PriorActionLowering.expand(bound)).statements
            assertEquals(2, clauses.size)
        }
    }

    @Test
    fun `collection member roles survive same named procedure parameters`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        for ((name, phrase) in listOf(
            "अन्तिम" to "मान + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप्",
            "अन्तिम" to "अन्तिम + मतुप् + अम् मान + ङस् उद् + हृ + लोट् + सिप्",
            "सङ्ख्या" to "मान + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप्",
        )) {
            val original = assertIs<Invocation>(parser.parse("$phrase ।").body)
            val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(original,
                listOf(PrakriyaParameter(name, PrakriyaValueType.SANKHYA),
                    PrakriyaParameter("मान", PrakriyaValueType.SUCHI)), 2))
            val originals = original.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>()
            val rebound = bound.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>()
            originals.zip(rebound).forEach { (before, after) ->
                if (before.pratipadika.sourceText.startsWith(name)) assertEquals(before, after)
                else assertEquals(PrakriyaAstArgumentBinder.referenceKey(1), after.pratipadika.sourceText)
            }
        }
        val ordinary = assertIs<Invocation>(parser.parse("अन्तिम + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(ordinary,
            listOf(PrakriyaParameter("अन्तिम", PrakriyaValueType.SANKHYA)), 1))
        assertEquals(PrakriyaAstArgumentBinder.referenceKey(0),
            assertIs<dev.panini.vyakaranam.ast.SubantaPada>(bound.vakya.padas.first()).pratipadika.sourceText)
    }

    @Test
    fun `collection argument shortcut preserves derivation case and declared parameter identity`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        for (phrase in listOf("समवाय + मतुप् + अम्", "समवाय + तरप् + अम्", "समवाय + ङस्")) {
            val original = assertIs<Invocation>(parser.parse("$phrase युज् + णिच् + लोट् + सिप् ।").body)
            val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(original, emptyList(), 2))
            assertEquals(original.vakya.padas, bound.vakya.padas, phrase)
        }
        val plain = assertIs<Invocation>(parser.parse("समवाय + अम् युज् + णिच् + लोट् + सिप् ।").body)
        val legacy = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(plain, emptyList(), 2))
        assertIs<dev.panini.vyakaranam.ast.SamuccitaSubanta>(legacy.vakya.padas.first())
        val named = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(plain,
            listOf(PrakriyaParameter("समवाय", PrakriyaValueType.SUCHI)), 1))
        val parameter = assertIs<dev.panini.vyakaranam.ast.SubantaPada>(named.vakya.padas.first())
        assertEquals(PrakriyaAstArgumentBinder.referenceKey(0), parameter.pratipadika.sourceText)
    }

    @Test
    fun `procedure execution preserves ambiguous history failure`() {
        val source = "वाचन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
            "मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।\n" +
            "युज् + ल्युट् + ङस् प्रथम + अम् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n" +
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् वाचन + टा कृ + लोट् + सिप् ।"
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
    }

    @Test
    fun `ambiguous history qualifiers cannot disappear into positional rebinding`() {
        val original = assertIs<Invocation>(dev.panini.vyakaranam.parser.PaniniParser().parse(
            "युज् + ल्युट् + ङस् प्रथम + अम् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body)
        val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(original,
            listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA)), 1))
        assertEquals(original.vakya.padas, bound.vakya.padas)
        val reference = dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(bound.vakya.padas).single()
        assertFalse(reference.orderingAgrees)
        assertEquals(2, reference.orderingQualifiers.size)
    }

    @Test
    fun `reordered history relations survive invocation and pipeline parameter binding`() {
        for (phrase in listOf(
            "प्रथम + अम् युज् + ल्युट् + ङस् फल + अम्",
            "युज् + ल्युट् + ङस् फल + अम् प्रथम + अम्",
            "फल + अम् युज् + ल्युट् + ङस् प्रथम + अम्",
        )) {
            val invocation = assertIs<Invocation>(dev.panini.vyakaranam.parser.PaniniParser()
                .parse("$phrase मुद्र् + णिच् + लोट् + सिप् ।").body)
            val parameters = listOf(PrakriyaParameter("फल", PrakriyaValueType.SANKHYA))
            assertEquals(invocation.vakya.padas,
                assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(invocation, parameters, 1)).vakya.padas)
            val padas = invocation.vakya.padas.dropLast(1)
            val pipeline = Pipeline(sourceText = phrase, arguments = padas.map { it.sourceText },
                stages = emptyList(), argumentPadas = padas, renderPadas = padas)
            val bound = assertIs<Pipeline>(PrakriyaAstArgumentBinder.bind(pipeline, parameters, 1))
            assertEquals(padas, bound.argumentPadas)
            assertEquals(padas, bound.renderPadas)
            assertEquals(pipeline.arguments, bound.arguments)
        }
    }

    @Test
    fun `karaka history qualifiers are protected from procedure parameter rebinding`() {
        val original = assertIs<Invocation>(dev.panini.vyakaranam.parser.PaniniParser()
            .parse("युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        val parameters = listOf(PrakriyaParameter("कर्मन्", PrakriyaValueType.SANKHYA))
        val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(original, parameters, 1))
        assertEquals(original.vakya.padas, bound.vakya.padas)
        val padas = original.vakya.padas.dropLast(1)
        val pipeline = Pipeline(sourceText = "typed karaka history pipeline", stages = emptyList(),
            arguments = padas.map { it.sourceText }, argumentPadas = padas, renderPadas = padas)
        val boundPipeline = assertIs<Pipeline>(PrakriyaAstArgumentBinder.bind(pipeline, parameters, 1))
        assertEquals(pipeline.arguments, boundPipeline.arguments)
        assertEquals(padas, boundPipeline.argumentPadas)
        assertEquals(padas, boundPipeline.renderPadas)
    }
    @Test
    fun `pipeline history modifiers cannot become positional or named parameters`() {
        val invocation = assertIs<Invocation>(dev.panini.vyakaranam.parser.PaniniParser()
            .parse("युज् + ल्युट् + ङस् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        val padas = invocation.vakya.padas.dropLast(1)
        val pipeline = Pipeline(
            sourceText = "typed history pipeline", arguments = padas.map { it.sourceText },
            stages = emptyList(), argumentPadas = padas, renderPadas = padas,
        )
        val bound = assertIs<Pipeline>(PrakriyaAstArgumentBinder.bind(pipeline,
            listOf(PrakriyaParameter("फल", PrakriyaValueType.SANKHYA)), 1))
        assertEquals(pipeline.arguments, bound.arguments)
        assertEquals(padas, bound.argumentPadas)
        assertEquals(padas, bound.renderPadas)
    }
    @Test
    fun `history ordinal qualifier is not a procedure positional placeholder`() {
        val original = assertIs<Invocation>(dev.panini.vyakaranam.parser.PaniniParser()
            .parse("युज् + ल्युट् + ङस् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(original,
            listOf(PrakriyaParameter("मान", PrakriyaValueType.SANKHYA)), 1))
        assertEquals(original.vakya.padas, bound.vakya.padas)
    }
    @Test
    fun `wide typed ordinal cannot impersonate a positional parameter`() {
        val original = assertIs<Invocation>(dev.panini.vyakaranam.parser.PaniniParser()
            .parse("प्रथ् + अमच् + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        val sentence = assertIs<dev.panini.vyakaranam.ast.AkhyataVakya>(original.vakya)
        val operand = dev.panini.vyakaranam.ast.SankhyaPuranaPada(
            sourceText = "typed ordinal", stems = emptyList(), value = 4_294_967_297L,
            sup = dev.panini.vyakaranam.ast.SupPratyaya("अम्", "अम्"),
        )
        val node = original.copy(vakya = sentence.copy(padas = listOf(operand, sentence.tinganta)))
        val bound = assertIs<Invocation>(PrakriyaAstArgumentBinder.bind(node, emptyList(), 1))
        assertEquals(operand, bound.vakya.padas.first())
    }
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

        assertEquals(NishedhaGuardEvaluator.NumericProhibition(
            NishedhaGuardEvaluator.NumericOperand.Argument(1),
            NishedhaGuardEvaluator.NumericOperand.Literal(0)),
            NishedhaGuardEvaluator.numericProhibition(guard, emptyList(), 2))

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

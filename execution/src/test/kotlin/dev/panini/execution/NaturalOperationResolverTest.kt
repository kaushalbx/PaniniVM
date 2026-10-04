package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.kryadi.GrahDhatu
import dev.panini.dhatupatha.adadi.AsDhatu
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.dhatupatha.curadi.GanDhatu
import dev.panini.execution.planning.ResolvedLeafPlanner
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.vyakaranam.ast.Ukti
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class NaturalOperationResolverTest {
    @Test
    fun `natural placement remains visible inside repeated arithmetic`() {
        val source = """
            एक + अम् गुणक + ङि स्था + णिच् + लोट् + सिप् ।
            एक + अम् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।
            पञ्चन् + कृत्वसुच् गुणक + अम् सङ्ख्या + अम् च गण + णिच् + लोट् + सिप्
                ततः फल + अम् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप्
                ततः एक + अम् गुणक + अम् च युज् + णिच् + लोट् + सिप्
                ततः फल + अम् गुणक + ङि स्था + णिच् + लोट् + सिप् ।
            """.trimIndent()
        val repeatedSentence = PvmScript.parse(source)[2] as PvmScriptStatement.Sentence
        val repeat = assertIs<Repeat>(repeatedSentence.program, repeatedSentence.program.toString())
        val sequence = assertIs<Sequence>(repeat.body, repeat.body.toString())
        assertEquals(4, sequence.statements.size, sequence.toString())
        assertEquals(
            listOf("गण", "स्था", "युज्", "स्था"),
            sequence.statements.map { statement ->
                assertIs<Invocation>(statement).vakya.padas.filterIsInstance<TingantaPada>().single().dhatu.mulaDhatu
            },
            sequence.toString(),
        )
        val yujInvocation = assertIs<Invocation>(sequence.statements[2])
        assertTrue(
            yujInvocation.vakya.padas.size >= 3,
            yujInvocation.vakya.toString(),
        )
        val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
        val bound = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", sequence.sourceText),
                Ukti(sequence.sourceText, body = sequence),
                conversation,
                environment = ValueEnvironment(
                    mapOf(
                        "गुणक" to SanskritValue.Sankhya(1, "एक"),
                        "सङ्ख्या" to SanskritValue.Sankhya(1, "एक"),
                    ),
                ),
            ),
        )
        assertEquals(
            listOf("गण", "स्थाञँ", "युजिँर्", "स्थाञँ"),
            bound.ukti.invocations.map { it.dhatu.upadesha },
            bound.ukti.invocations.toString(),
        )
        assertTrue(
            Karaka.KARMAN in bound.ukti.invocations[2].bindings,
            bound.ukti.invocations[2].toString(),
        )
        val planningEnvironment = ValueEnvironment(
            mapOf(
                "गुणक" to SanskritValue.Sankhya(1, "एक"),
                "सङ्ख्या" to SanskritValue.Sankhya(1, "एक"),
            ),
        )
        val planningResult = ExecutionPlanner.plan(ExecutionProgram(bound.ukti), planningEnvironment)
        val planning = assertIs<PlanningResult.Planned>(planningResult, planningResult.toString())
        assertEquals(
            listOf("सङ्ख्यागुणनम्", "मूल्यदानम्", "सङ्ख्यायोजनम्", "मूल्यदानम्"),
            planning.plans.map { it.resolved.operation.name },
            planning.toString(),
        )

        val vm = PaniniVM()
        val sessionKey = "natural-repeated-placement"
        val results = vm.evalScript(source, sessionKey = sessionKey)
        val invalid = results.filterNot { it is ExecutionResult.Success }
        assertTrue(invalid.isEmpty(), results.toString())
        assertEquals(120L, (vm.runtimeValue(sessionKey, "सङ्ख्या") as SanskritValue.Sankhya).value)
    }

    @Test
    fun `state placement is derived from karman and adhikarana`() {
        val operation = assertIs<NaturalOperation.StatePlacement>(
            resolve("सप्त + अम् अवस्था + ङि स्था + णिच् + लोट् + सिप्", allowStore = true),
        )

        assertEquals("सप्त", operation.value.bindingName())
        assertEquals("अवस्था", operation.destination.bindingName())
    }

    @Test
    fun `collection insertion is derived from karman and adhikarana`() {
        val operation = assertIs<NaturalOperation.CollectionInsertion>(
            resolve("द्वि + अम् क्रम + ङि नि + क्षिप् + लोट् + सिप्"),
        )

        assertEquals("द्वि", operation.item.bindingName())
        assertEquals("क्रम", operation.collection.bindingName())
    }

    @Test
    fun `collection formation is derived from coordinated karman`() {
        val operation = assertIs<NaturalOperation.CollectionFormation>(
            resolve("एक + अम् द्वि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्"),
        )

        val items = assertIs<ExecutionExpression.Coordination>(operation.items)
        assertEquals(listOf("एक", "द्वि"), items.members.map { it.bindingName() })
    }

    @Test
    fun `parsed invocation can be planned without rendering and reparsing`() {
        val parsed = PaniniParser().parse("सूची + अम् उद् + हृ + लोट् + सिप् ।")
        val invocation = assertIs<Invocation>(parsed.body)

        val plan = ResolvedLeafPlanner.planAny(invocation)

        assertEquals("सूच्युद्धरणम्", requireNotNull(plan).resolved.operation.name)
    }

    @Test
    fun `indexed retrieval is derived from apadana adhikarana and karman`() {
        val operationDefinition = GrahDhatu().operations.single { it.name == "सूचीस्थानम्" }
        val operation = assertIs<NaturalOperation.IndexedRetrieval>(
            NaturalOperationResolver.resolve(
                operationDefinition,
                ExecutionContext(
                    bindings = mapOf(
                        Karaka.APADANA to ExecutionExpression.Reference("क्रम"),
                        Karaka.ADHIKARANA to ExecutionExpression.Reference("क्रमाङ्क"),
                        Karaka.KARMAN to ExecutionExpression.Reference("मूल्य"),
                    ),
                ),
            ),
        )

        assertEquals("क्रम", operation.collection.bindingName())
        assertEquals("क्रमाङ्क", operation.index.bindingName())
        assertEquals("मूल्य", operation.result.bindingName())
    }

    @Test
    fun `collection membership is derived from nominative and locative karakas`() {
        val definition = AsDhatu().operations.first {
            it.name == "सूच्यस्तित्वम्" && it.signature.requirements.any { requirement ->
                requirement.karaka == Karaka.ADHIKARANA
            }
        }
        val operation = assertIs<NaturalOperation.CollectionMembership>(
            NaturalOperationResolver.resolve(
                definition,
                ExecutionContext(
                    bindings = mapOf(
                        Karaka.KARTR to ExecutionExpression.Reference("द्वि"),
                        Karaka.ADHIKARANA to ExecutionExpression.Reference("सूची"),
                    ),
                ),
            ),
        )
        assertEquals("द्वि", operation.item.bindingName())
        assertEquals("सूची", operation.collection.bindingName())

        val results = PaniniVM().evalScript(
            """
            एक + अम् द्वि + अम् त्रि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् सूची + ङि स्था + णिच् + लोट् + सिप् ।
            यदि द्वि + सुँ सूची + ङि असँ + लट् + तिप्
                तर्हि सत्य + अम् सदस्यता + ङि स्था + णिच् + लोट् + सिप्
                अन्यथा असत्य + अम् सदस्यता + ङि स्था + णिच् + लोट् + सिप् ।
            """.trimIndent(),
        )
        assertTrue(results.none { it is ExecutionResult.Failure || it is ExecutionResult.NeedsInput }, results.toString())
        assertEquals(
            SanskritValue.Satya(true),
            results.filterIsInstance<ExecutionResult.Success>().last().typedValue,
        )
    }

    @Test
    fun `collection concatenation is derived from object and instrumental companion`() {
        val definition = YujirDhatu().operations.single { it.name == "सूचीसंयोगः" }
        val operation = assertIs<NaturalOperation.CollectionConcatenation>(
            NaturalOperationResolver.resolve(
                definition,
                ExecutionContext(
                    bindings = mapOf(
                        Karaka.KARMAN to ExecutionExpression.Reference("पूर्वसूची"),
                        Karaka.KARTR to ExecutionExpression.Reference("उत्तरसूची"),
                    ),
                ),
            ),
        )
        assertEquals("पूर्वसूची", operation.collection.bindingName())
        assertEquals("उत्तरसूची", operation.companion.bindingName())

        val list = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एक")))
        val parsedCall = PaniniParser().parse(
            "पूर्वसूची + अम् उत्तरसूची + टा सम् + युज् + णिच् + लोट् + सिप् ।",
        )
        val boundCall = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", parsedCall.sourceText),
                parsedCall,
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                environment = ValueEnvironment(mapOf("पूर्वसूची" to list, "उत्तरसूची" to list)),
            ),
        )
        assertTrue(
            boundCall.ukti.invocations.single().dhatu.operations.any { it.name == "सूचीसंयोगः" },
            boundCall.ukti.invocations.single().dhatu.toString(),
        )
        assertEquals(
            setOf(Karaka.KARMAN, Karaka.KARTR),
            boundCall.ukti.invocations.single().bindings.keys,
            boundCall.ukti.invocations.single().toString(),
        )

        val results = PaniniVM().evalScript(
            """
            एक + अम् द्वि + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् पूर्वसूची + ङि स्था + णिच् + लोट् + सिप् ।
            त्रि + अम् चतुर् + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्
                ततः फल + अम् उत्तरसूची + ङि स्था + णिच् + लोट् + सिप् ।
            पूर्वसूची + अम् उत्तरसूची + टा सम् + युज् + णिच् + लोट् + सिप् ।
            """.trimIndent(),
        )
        assertTrue(results.none { it is ExecutionResult.Failure || it is ExecutionResult.NeedsInput }, results.toString())
        val concatenated = assertIs<SanskritValue.Suchi>(
            results.filterIsInstance<ExecutionResult.Success>().last().typedValue,
        )
        assertEquals(listOf(1L, 2L, 3L, 4L), concatenated.items.map { (it as SanskritValue.Sankhya).value })
    }

    @Test
    fun `collection cardinality is derived from the counted object`() {
        val definition = GanDhatu().operations.single { it.name == "सूच्याकारः" }
        val operation = assertIs<NaturalOperation.CollectionCardinality>(
            NaturalOperationResolver.resolve(
                definition,
                ExecutionContext(
                    bindings = mapOf(
                        Karaka.KARMAN to ExecutionExpression.Reference("सूची"),
                    ),
                ),
            ),
        )

        assertEquals("सूची", operation.collection.bindingName())
    }

    @Test
    fun `collection slice is derived from genitive collection and ordinal limits`() {
        val definition = GrahDhatu().operations.single { operation ->
            operation.name == "सूचीविभागः" &&
                operation.signature.requirements.any { it.karaka == Karaka.SAMBANDHA }
        }
        val operation = assertIs<NaturalOperation.CollectionSlice>(
            NaturalOperationResolver.resolve(
                definition,
                ExecutionContext(
                    bindings = mapOf(
                        Karaka.SAMBANDHA to ExecutionExpression.Reference("सूची"),
                        Karaka.APADANA to ExecutionExpression.sankhya(2L, "द्वितीय"),
                        Karaka.ADHIKARANA to ExecutionExpression.sankhya(3L, "तृतीय"),
                        Karaka.KARMAN to ExecutionExpression.Reference("अंश"),
                    ),
                ),
            ),
        )

        assertEquals("सूची", operation.collection.bindingName())
        assertEquals("द्वितीय", operation.start.bindingName())
        assertEquals("तृतीय", operation.endInclusive.bindingName())
        assertEquals("अंश", operation.result.bindingName())
    }

    private fun resolve(
        source: String,
        allowStore: Boolean = false,
        environment: ValueEnvironment = ValueEnvironment(),
    ): NaturalOperation? {
        val plan = requireNotNull(ResolvedLeafPlanner.plan(source, environment, allowStore))
        return NaturalOperationResolver.resolve(plan.resolved)
    }
}

package dev.panini.execution

import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.Gana
import dev.panini.dhatupatha.YujirDhatu
import dev.panini.derivation.Lakara
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DhatuExecutionTest {
    private val yuj = assertIs<YujirDhatu>(DhatuPatha.find("07.0007"))

    @Test
    fun `yuj adds a coordinated expression of Sanskrit number words`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्यायोजनम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(yuj, context))

        assertEquals("षट्", result.value)
        assertEquals("सङ्ख्यायोजनम्", result.operation)
    }

    @Test
    fun `execution reports a missing required karaka`() {
        val result = ExecutionEngine.execute(yuj, ExecutionContext(selectedOperation = "सङ्ख्यायोजनम्"))

        val needsInput = assertIs<ExecutionResult.NeedsInput>(result)
        assertEquals(setOf(Karaka.KARMAN), needsInput.missingKarakas)
    }

    @Test
    fun `operation signature rejects values without sankhya samjna`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SHABDA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SHABDA)),
                )
            )
        )

        val result = assertIs<ExecutionResult.Failure>(ExecutionEngine.execute(yuj, context))

        assertEquals(ExecutionError.INVALID_VALUE, result.error)
    }

    @Test
    fun `operation signature requires a coordinated expression`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA))
            )
        )

        val result = assertIs<ExecutionResult.Failure>(ExecutionEngine.execute(yuj, context))

        assertEquals(ExecutionError.INVALID_VALUE, result.error)
    }

    @Test
    fun `ordinary catalogue dhatu reports that it is not executable`() {
        val bhu = requireNotNull(DhatuPatha.find("01.0001"))

        val result = assertIs<ExecutionResult.Failure>(ExecutionEngine.execute(bhu, ExecutionContext()))

        assertEquals(ExecutionError.DHATU_NOT_EXECUTABLE, result.error)
    }

    @Test
    fun `imperative utterance is interpreted planned authorized and executed`() {
        val invocation = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val ukti = Ukti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "हे यन्त्र, एकं द्वे त्रीणि च योजय।",
            prayojana = VakyaPrayojana.AJNA,
            lakara = Lakara.LOT,
            invocations = listOf(invocation),
        )

        val phala = BhashaExecutionEngine.execute(
            ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val siddha = assertIs<Phala.Siddha>(phala)
        assertEquals("षट्", siddha.values["योग-१"])
    }

    @Test
    fun `question is understood without being treated as execution failure`() {
        val invocation = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val ukti = Ukti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "किम् एकस्य द्वयोश्च योगः त्रि भवति?",
            prayojana = VakyaPrayojana.PRASHNA,
            invocations = listOf(invocation),
        )

        val phala = BhashaExecutionEngine.execute(
            ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(),
        )

        val understood = assertIs<Phala.Avagata>(phala)
        assertEquals(ExecutionDisposition.QUERY, understood.disposition)
        assertEquals(1, understood.plans.size)
    }

    @Test
    fun `later action consumes the Sanskrit result of an earlier action`() {
        val first = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val second = DhatuInvocation(
            id = "योग-२",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Reference("योग-१"),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val ukti = Ukti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "एकं द्वे च योजय, ततः फले द्वे योजय।",
            prayojana = VakyaPrayojana.AJNA,
            lakara = Lakara.LOT,
            invocations = listOf(first, second),
        )

        val phala = BhashaExecutionEngine.execute(
            ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val siddha = assertIs<Phala.Siddha>(phala)
        assertEquals("त्रि", siddha.values["योग-१"])
        assertEquals("पञ्च", siddha.values["योग-२"])
    }

    @Test
    fun `trusted conversation participants cannot be changed by utterance text`() {
        val invocation = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val ukti = Ukti(
            speaker = "अन्यः",
            listener = "यन्त्रम्",
            text = "एकं द्वे च योजय।",
            prayojana = VakyaPrayojana.AJNA,
            invocations = listOf(invocation),
        )

        val phala = BhashaExecutionEngine.execute(
            ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(),
        )

        val failure = assertIs<Phala.Asiddha>(phala)
        assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(failure.result).error)
    }

    @Test
    fun `effectful instruction waits for host capability`() {
        val effectfulDhatu = object : Dhatu(
            id = "test.write",
            krama = 1,
            upadesha = "लिख्",
            sourceSurface = "लिख्",
            artha = "लेखने",
            arthaHindi = "लिखना",
            arthaEnglish = "to write",
            gana = Gana.TUDADI,
        ) {
            override val operations = listOf(
                DhatuOperation(
                    id = "संसाधनलेखनम्",
                    description = "संसाधने लेखनम्",
                    signature = OperationSignature(emptyList()),
                    action = DhatuAction { _, operation ->
                        ExecutionResult.Success("लिखितम्", operation.id)
                    },
                    effects = setOf(ExecutionEffect.WRITE_RESOURCE),
                    resultSamjnas = setOf(ExecutionSamjna.SHABDA),
                )
            )
        }
        val ukti = Ukti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "लिख।",
            prayojana = VakyaPrayojana.AJNA,
            invocations = listOf(DhatuInvocation("लेखन-१", effectfulDhatu, emptyMap())),
        )

        val phala = BhashaExecutionEngine.execute(
            ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val approval = assertIs<Phala.AnumatiApekshita>(phala)
        assertEquals(setOf(ExecutionEffect.WRITE_RESOURCE), approval.effects)

        val resumed = BhashaExecutionEngine.resume(
            approval.continuation,
            ExecutionScope(
                capabilities = setOf(ExecutionEffect.PURE, ExecutionEffect.WRITE_RESOURCE),
                authorizedSpeakers = setOf("प्रयोक्ता"),
            ),
        )

        assertEquals("लिखितम्", assertIs<Phala.Siddha>(resumed).values["लेखन-१"])
    }

    @Test
    fun `command from understood but unauthorized speaker is rejected`() {
        val invocation = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val ukti = Ukti(
            speaker = "अतिथिः",
            listener = "यन्त्रम्",
            text = "एकं द्वे च योजय।",
            prayojana = VakyaPrayojana.AJNA,
            invocations = listOf(invocation),
        )

        val phala = BhashaExecutionEngine.execute(
            ukti,
            SambhashanaContext("अतिथिः", "यन्त्रम्"),
            ExecutionScope(),
        )

        val rejected = assertIs<Phala.Nirasta>(phala)
        assertEquals("योग-१", rejected.invocationId)
    }

    @Test
    fun `request waits for listener acceptance`() {
        val invocation = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val ukti = Ukti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "कृपया एकं द्वे च योजय।",
            prayojana = VakyaPrayojana.PRARTHANA,
            invocations = listOf(invocation),
        )

        val phala = BhashaExecutionEngine.execute(
            ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(),
        )

        val pending = assertIs<Phala.SvikaraApekshita>(phala)

        val resumed = BhashaExecutionEngine.resume(
            pending.continuation,
            ExecutionScope(acceptedInvocations = setOf("योग-१")),
        )

        assertEquals("त्रि", assertIs<Phala.Siddha>(resumed).values["योग-१"])
    }

    @Test
    fun `planner rejects cyclic action dependencies`() {
        val invocation = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val nirdesha = Nirdesha(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            prayojana = VakyaPrayojana.AJNA,
            polarity = Polarity.POSITIVE,
            lakara = Lakara.LOT,
            invocations = listOf(invocation),
            sourceText = "योजय।",
        )
        val program = BhashaProgram(
            nirdesha,
            listOf(invocation),
            setOf(ActionDependency("योग-१", "योग-१")),
        )

        val planning = assertIs<PlanningResult.Failed>(ExecutionPlanner.plan(program, emptyMap()))

        assertEquals(ExecutionError.ACTION_FAILED, assertIs<ExecutionResult.Failure>(planning.result).error)
    }

    @Test
    fun `successful instruction produces Sanskrit response`() {
        val invocation = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val ukti = Ukti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "एकं द्वे च योजय।",
            prayojana = VakyaPrayojana.AJNA,
            invocations = listOf(invocation),
        )

        val response = BhashaExecutionEngine.executeAndRespond(
            ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        assertIs<Phala.Siddha>(response.phala)
        assertTrue("कार्यम् सिद्धम्" in response.text)
        assertTrue("त्रि" in response.text)
    }

    @Test
    fun `pending request produces acceptance question in Sanskrit`() {
        val invocation = DhatuInvocation(
            id = "योग-१",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val ukti = Ukti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "कृपया एकं द्वे च योजय।",
            prayojana = VakyaPrayojana.PRARTHANA,
            invocations = listOf(invocation),
        )

        val response = BhashaExecutionEngine.executeAndRespond(
            ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(),
        )

        assertIs<Phala.SvikaraApekshita>(response.phala)
        assertTrue("स्वीकरोषि किम्" in response.text)
    }

    @Test
    fun `analyzed Sanskrit sentence compiles into executable utterance`() {
        val analysis = VakyaAnalysis(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            sourceText = "एकं द्वे त्रीणि च योजय।",
            prayojana = VakyaPrayojana.AJNA,
            lakara = Lakara.LOT,
            kriyas = listOf(
                KriyaAnalysis(
                    id = "योग-१",
                    dhatuId = "07.0007",
                    karakas = mapOf(
                        Karaka.KARMAN to ExecutionExpression.Coordination(
                            ExecutionExpression.Literal("एक", setOf(ExecutionSamjna.SANKHYA)),
                            ExecutionExpression.Literal("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                            ExecutionExpression.Literal("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                        )
                    ),
                )
            ),
        )

        val response = BhashaExecutionEngine.executeAndRespond(
            analysis,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        assertEquals("षट्", assertIs<Phala.Siddha>(response.phala).values["योग-१"])
    }

    @Test
    fun `compiler rejects unknown dhatupatha identity`() {
        val analysis = VakyaAnalysis(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            sourceText = "कुरु।",
            prayojana = VakyaPrayojana.AJNA,
            kriyas = listOf(KriyaAnalysis("क्रिया-१", "unknown", emptyMap())),
        )

        val compilation = assertIs<UktiCompilation.Invalid>(BhashaCompiler.compile(analysis))

        assertTrue("Unknown Dhātupāṭha identity" in compilation.message)
    }

    @Test
    fun `controlled Sanskrit sentence is analyzed and executed from raw text`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "हे यन्त्र, एकं द्वे त्रीणि च योजय।",
        )

        val response = BhashaExecutionEngine.executeAndRespond(
            input,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        assertEquals("षट्", assertIs<Phala.Siddha>(response.phala).values["योग-१"])
        assertTrue("षट्" in response.text)
    }

    @Test
    fun `controlled analyzer asks for another operand`() {
        val analysis = ControlledSanskritAnalyzer.analyze(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "एकं योजय।")
        )

        assertIs<VakyaAnalysisResult.NeedsClarification>(analysis)
    }

    @Test
    fun `raw Sanskrit request waits for acceptance`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "कृपया एकं द्वे च योजय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(),
        )

        assertIs<Phala.SvikaraApekshita>(response.phala)
    }

    @Test
    fun `raw Sanskrit prohibition is understood as constraint and not executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "मा एकं द्वे च योजय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(),
        )

        val understood = assertIs<Phala.Avagata>(response.phala)
        assertEquals(ExecutionDisposition.CONSTRAIN, understood.disposition)
    }

    @Test
    fun `raw Sanskrit multi-action sentence refers to previous result`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput(
                "प्रयोक्ता",
                "यन्त्रम्",
                "एकं द्वे च योजय, ततः फले द्वे योजय।",
            ),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("त्रि", result.values["योग-१"])
        assertEquals("पञ्च", result.values["योग-२"])
    }

    @Test
    fun `raw Sanskrit sentence consumes result remembered from earlier utterance`() {
        val conversation = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            previousResults = linkedMapOf("पूर्व-योग" to "त्रि"),
            previousResultSamjnas = mapOf("पूर्व-योग" to setOf(ExecutionSamjna.SANKHYA)),
        )

        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "फले द्वे योजय।"),
            conversation,
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        assertEquals("पञ्च", assertIs<Phala.Siddha>(response.phala).values["योग-१"])
    }

    @Test
    fun `successful conversational turn remembers result for next Sanskrit utterance`() {
        val initial = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
        val scope = ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता"))

        val first = BhashaExecutionEngine.executeTurn(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "एकं द्वे च योजय।"),
            initial,
            scope,
        )
        val second = BhashaExecutionEngine.executeTurn(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "फले द्वे योजय।"),
            first.context,
            scope,
        )

        assertEquals("त्रि", first.context.previousResults["योग-१"])
        assertEquals(setOf(ExecutionSamjna.SANKHYA), first.context.previousResultSamjnas["योग-१"])
        assertEquals("पञ्च", assertIs<Phala.Siddha>(second.response.phala).values["योग-१"])
        assertEquals(listOf("उक्ति-१/योग-१"), first.context.resultHistory.map { it.id })
        assertEquals(
            listOf("उक्ति-१/योग-१", "उक्ति-२/योग-१"),
            second.context.resultHistory.map { it.id },
        )
        assertEquals(listOf("त्रि", "पञ्च"), second.context.resultHistory.map { it.value })
    }
}

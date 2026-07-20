package dev.panini.execution

import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.Gana
import dev.panini.dhatupatha.rudhadi.YujirDhatu
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SHABDA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SHABDA)),
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
                Karaka.KARMAN to ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA))
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
        )
        val second = DhatuInvocation(
            id = "योग-२",
            dhatu = yuj,
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Reference("योग-१"),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
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
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
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
                            ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                            ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                            ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
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

    @Test
    fun `yuj subtracts coordinated expression of Sanskrit number words`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("दश", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्यावियोगः",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(yuj, context))

        assertEquals("पञ्च", result.value)
        assertEquals("सङ्ख्यावियोगः", result.operation)
    }

    @Test
    fun `subtraction reports error when result is negative`() {
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("दश", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्यावियोगः",
        )

        val result = assertIs<ExecutionResult.Failure>(ExecutionEngine.execute(yuj, context))

        assertEquals(ExecutionError.INVALID_VALUE, result.error)
        assertTrue(result.message.contains("outside the supported Sanskrit number vocabulary"))
    }

    @Test
    fun `hr divides coordinated expression of Sanskrit number words`() {
        val hr = requireNotNull(DhatuPatha.find("01.1046"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("दश", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्याहरणम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(hr, context))

        assertEquals("पञ्च", result.value)
        assertEquals("सङ्ख्याहरणम्", result.operation)
    }

    @Test
    fun `division by zero reports error`() {
        val hr = requireNotNull(DhatuPatha.find("01.1046"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("दश", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("शून्य", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्याहरणम्",
        )

        val result = assertIs<ExecutionResult.Failure>(ExecutionEngine.execute(hr, context))

        assertEquals(ExecutionError.INVALID_VALUE, result.error)
        assertTrue(result.message.contains("Division by zero"))
    }

    @Test
    fun `raw Sanskrit subtraction utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, दश त्रि च वियोजय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("सप्त", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit division utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, दश द्वि च हर।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("पञ्च", result.values["योग-१"])
    }

    @Test
    fun `gan multiplies coordinated expression of Sanskrit number words`() {
        val gan = requireNotNull(DhatuPatha.find("10.0391"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्यागुणनम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(gan, context))

        assertEquals("षट्", result.value)
        assertEquals("सङ्ख्यागुणनम्", result.operation)
    }

    @Test
    fun `gan counts elements in a coordinated expression`() {
        val gan = requireNotNull(DhatuPatha.find("10.0391"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("एक", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्यागणनम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(gan, context))

        assertEquals("त्रि", result.value)
        assertEquals("सङ्ख्यागणनम्", result.operation)
    }

    @Test
    fun `raw Sanskrit multiplication utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, त्रि द्वे च गुणय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("षट्", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit counting utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, एकं द्वे त्रीणि च गणय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("त्रि", result.values["योग-१"])
    }

    @Test
    fun `kru performs sandhi joining on text operands`() {
        val kru = requireNotNull(DhatuPatha.find("08.0010"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("राम", setOf(ExecutionSamjna.SHABDA)),
                    ExecutionExpression.Pada("इति", setOf(ExecutionSamjna.SHABDA)),
                )
            ),
            selectedOperation = "संहिताकरणम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(kru, context))

        assertEquals("रामेति", result.value)
        assertEquals("संहिताकरणम्", result.operation)
    }

    @Test
    fun `kru derives subanta form for nominal stem`() {
        val kru = requireNotNull(DhatuPatha.find("08.0010"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("राम", setOf(ExecutionSamjna.SHABDA)),
            ),
            selectedOperation = "पदनिष्पत्तिः",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(kru, context))

        assertEquals("रामः", result.value)
        assertEquals("पदनिष्पत्तिः", result.operation)
    }

    @Test
    fun `raw Sanskrit sandhi utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, राम इति च कुरु।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("रामेति", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit subanta derivation utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, राम निष्पादय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("रामः", result.values["योग-१"])
    }

    @Test
    fun `shish computes modulo remainder of Sanskrit number words`() {
        val shish = requireNotNull(DhatuPatha.find("07.0014"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("दश", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्याशेषः",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(shish, context))

        assertEquals("एक", result.value)
        assertEquals("सङ्ख्याशेषः", result.operation)
    }

    @Test
    fun `vridh computes exponentiation power of Sanskrit number words`() {
        val vridh = requireNotNull(DhatuPatha.find("01.0863"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्याघातः",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(vridh, context))

        assertEquals("नव", result.value)
        assertEquals("सङ्ख्याघातः", result.operation)
    }

    @Test
    fun `vid compares Sanskrit number words and returns maximum`() {
        val vid = requireNotNull(DhatuPatha.find("07.0013"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("पञ्च", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्यातुलना",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(vid, context))

        assertEquals("पञ्च", result.value)
        assertEquals("सङ्ख्यातुलना", result.operation)
    }

    @Test
    fun `raw Sanskrit modulo utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, दश त्रि च शेषय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("एक", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit exponentiation utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, त्रि द्वे च वर्धय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("नव", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit comparison utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, त्रि पञ्च च तुलय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("पञ्च", result.values["योग-१"])
    }

    @Test
    fun `mul computes square root of Sanskrit number word`() {
        val mul = requireNotNull(DhatuPatha.find("01.0607"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("नव", setOf(ExecutionSamjna.SANKHYA)),
            ),
            selectedOperation = "सङ्ख्यामूलम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(mul, context))

        assertEquals("त्रि", result.value)
        assertEquals("सङ्ख्यामूलम्", result.operation)
    }

    @Test
    fun `gan computes average of Sanskrit number words`() {
        val gan = requireNotNull(DhatuPatha.find("10.0391"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("चतुर्", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्यासाम्यम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(gan, context))

        assertEquals("त्रि", result.value)
        assertEquals("सङ्ख्यासाम्यम्", result.operation)
    }

    @Test
    fun `bhaj computes fraction of Sanskrit number words`() {
        val bhaj = requireNotNull(DhatuPatha.find("01.1153"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("दश", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("द्वि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्याभागः",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(bhaj, context))

        assertEquals("पञ्च", result.value)
        assertEquals("सङ्ख्याभागः", result.operation)
    }

    @Test
    fun `vid computes minimum of Sanskrit number words`() {
        val vid = requireNotNull(DhatuPatha.find("07.0013"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.Pada("पञ्च", setOf(ExecutionSamjna.SANKHYA)),
                    ExecutionExpression.Pada("त्रि", setOf(ExecutionSamjna.SANKHYA)),
                )
            ),
            selectedOperation = "सङ्ख्यान्यूनत्वम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(vid, context))

        assertEquals("त्रि", result.value)
        assertEquals("सङ्ख्यान्यूनत्वम्", result.operation)
    }

    @Test
    fun `raw Sanskrit square root utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, नव मूलय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("त्रि", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit average utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, द्वि चत्वारि च समय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("त्रि", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit fraction utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, दश द्वि च भज।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("पञ्च", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit minimum utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, पञ्च त्रि च न्यूनय।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("त्रि", result.values["योग-१"])
    }

    @Test
    fun `da assigns variable value`() {
        val da = requireNotNull(DhatuPatha.find("03.0010"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("दश", setOf(ExecutionSamjna.SANKHYA)),
            ),
            selectedOperation = "मूल्यदानम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(da, context))

        assertEquals("दश", result.value)
        assertEquals("मूल्यदानम्", result.operation)
    }

    @Test
    fun `drsh inspects variable target`() {
        val drsh = requireNotNull(DhatuPatha.find("01.1143"))
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("योग-१", setOf(ExecutionSamjna.SHABDA)),
            ),
            selectedOperation = "मूल्यदर्शनम्",
        )

        val result = assertIs<ExecutionResult.Success>(ExecutionEngine.execute(drsh, context))

        assertEquals("योग-१", result.value)
        assertEquals("मूल्यदर्शनम्", result.operation)
    }

    @Test
    fun `raw Sanskrit variable assignment utterance is executed`() {
        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, दश देहि।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("दश", result.values["योग-१"])
    }

    @Test
    fun `raw Sanskrit variable inspection utterance is executed`() {
        val conversation = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            previousResults = linkedMapOf("पूर्व-योग" to "पञ्च"),
            previousResultSamjnas = mapOf("पूर्व-योग" to setOf(ExecutionSamjna.SANKHYA)),
        )

        val response = BhashaExecutionEngine.executeAndRespond(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "हे यन्त्र, पूर्वफलं पश्य।"),
            conversation,
            ExecutionScope(authorizedSpeakers = setOf("प्रयोक्ता")),
        )

        val result = assertIs<Phala.Siddha>(response.phala)
        assertEquals("पञ्च", result.values["योग-१"])
    }
}







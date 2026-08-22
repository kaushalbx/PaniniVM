package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertIs

class ConditionControlledLoopTest {
    @Test
    fun `host may budget an otherwise unbounded yavat loop`() {
        val results = PaniniVM(executionLimits = ExecutionLimits(maxConditionIterations = 2L)).evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            वारः + अम् मुद्र् + णिच् + लोट् + सिप् ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val failure = assertIs<ExecutionResult.Failure>(results.last())
        assertTrue(failure.message.contains("host execution budget of 2"), results.toString())
        assertEquals(2, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "वारः" })
    }

    @Test
    fun `a grammatical loop bound is not restricted by the former host safety ceiling`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् एक + अम् च विद् + लोट् + सिप् ॥
            एक + दश + सहस्र + कृत्वः यावत् फल + सुँ तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        val completion = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.loopOutcome != null }
        assertEquals(0L, completion.iterationCount)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
    }

    @Test
    fun `bounded yavat loop runs until its Sanskrit upper bound`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            वारः + अम् मुद्र् + णिच् + लोट् + सिप् ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            त्रि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(3, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "वारः" }, results.toString())
        assertEquals(1, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "समाप्तम्" }, results.toString())
        val completion = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.loopOutcome != null }
        assertEquals(ExecutionResult.LoopOutcome.SAMAPTI, completion.loopOutcome)
        assertEquals(3L, completion.iterationCount)
        assertTrue(results.any { it is ExecutionResult.Success && it.value == "समाप्ति" })
    }

    @Test
    fun `true body result terminates a negated phala loop immediately`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            वारः + अम् मुद्र् + णिच् + लोट् + सिप् ।
            द्वि + अम् एक + अम् च विद् + लोट् + सिप् ॥
            पञ्च + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् अन्यथा समाप्तम् + अम् मुद्र् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(1, results.filterIsInstance<ExecutionResult.Success>()
            .count { it.outputKind == OutputKind.CONSOLE && it.value == "वारः" }, results.toString())
        assertTrue(results.none { it is ExecutionResult.Success && it.value == "समाप्तम्" }, results.toString())
        val completion = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.loopOutcome != null }
        assertEquals(ExecutionResult.LoopOutcome.VIJAYA, completion.loopOutcome)
        assertEquals(1L, completion.iterationCount)
        assertTrue(results.any { it is ExecutionResult.Success && it.value == "विजय" })
        assertTrue(results.none { it is ExecutionResult.Failure })
    }

    @Test
    fun `loop publishes a structured outcome for genitive access without assignment`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            द्वि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            परिणाम + मतुप् + ङस् अवस्था + अम् ।
            परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् ।
            """.trimIndent(),
        )

        val values = results.filterIsInstance<ExecutionResult.Success>().map { it.value }
        assertEquals(listOf("समाप्ति", "द्वि"), values.takeLast(2), results.toString())
        val attemptCount = assertIs<SanskritValue.Sankhya>(
            results.filterIsInstance<ExecutionResult.Success>().last().typedValue,
        )
        assertEquals(2, attemptCount.value)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
    }

    @Test
    fun `typed numeric attribute keeps its type while sup controls pipeline rendering`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            द्वि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + औ ततः मुद्र् + लोट् + सिप् ।
            परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + भ्याम् ततः मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        val console = results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }
        assertEquals(listOf("द्वे", "द्वाभ्याम्"), console.map { it.value }, results.toString())
        console.forEach {
            assertEquals(2, assertIs<SanskritValue.Sankhya>(it.typedValue).value)
        }
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
    }

    @Test
    fun `plural numeral attribute renders the requested case`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            त्रि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + भिस् ततः मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        val printed = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.outputKind == OutputKind.CONSOLE }
        assertEquals("त्रिभिः", printed.value)
        assertEquals(3, assertIs<SanskritValue.Sankhya>(printed.typedValue).value)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
    }

    @Test
    fun `structured attribute may participate directly in a condition`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            त्रि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            यदि परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् द्वि + अम् च विद् + लोट् + सिप् तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        val printed = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.outputKind == OutputKind.CONSOLE }
        assertEquals(OutputKind.CONSOLE, printed.outputKind)
        assertEquals("जय", printed.value, results.toString())
    }

    @Test
    fun `nested structured attribute remains typed inside a condition`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            त्रि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            परिणाम + अम् फलित + अम् क्रीडा + मतुप् + सुँ ।
            यदि क्रीडा + मतुप् + ङस् फलित + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् द्वि + अम् च विद् + लोट् + सिप् तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        val printed = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.outputKind == OutputKind.CONSOLE }
        assertEquals("जय", printed.value, results.toString())
    }

    @Test
    fun `attribute result flows through multiple typed tatah stages`() {
        val results = PaniniVM().evalScript(
            """
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            त्रि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् ततः द्वि + अम् च गण् + णिच् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        val printed = results.filterIsInstance<ExecutionResult.Success>()
            .single { it.outputKind == OutputKind.CONSOLE }
        val number = assertIs<SanskritValue.Sankhya>(printed.typedValue)
        assertEquals(6, number.value)
        assertEquals("षट्", printed.value)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
    }

    @Test
    fun `ordinary action result may start a pipeline`() {
        val result = PaniniVM().eval(
            "त्रि + अम् द्वि + अम् च गण् + णिच् + लोट् + सिप् ततः मुद्र् + लोट् + सिप् ।",
        )

        val printed = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals(OutputKind.CONSOLE, printed.outputKind)
        assertEquals("षट्", printed.value)
    }

    @Test
    fun `pipeline result may control a conditional stage`() {
        val result = PaniniVM().eval(
            "त्रि + अम् द्वि + अम् च गण् + णिच् + लोट् + सिप् " +
                "ततः यदि फल + अम् द्वि + अम् च विद् + लोट् + सिप् " +
                "तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।",
        )

        val printed = assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("जय", printed.value)
    }

    @Test
    fun `declared result schema validates the automatic loop structure`() {
        val results = PaniniVM().evalScript(
            """
            अवस्था + अम् प्रयत्नसङ्ख्या + अम् परिणाम + मतुप् + सुँ ।
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            द्वि + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् ।
            """.trimIndent(),
        )

        val count = assertIs<SanskritValue.Sankhya>(
            results.filterIsInstance<ExecutionResult.Success>().last().typedValue,
        )
        assertEquals(2, count.value)
        assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
    }

    @Test
    fun `result schema rejects a mismatched automatic structure`() {
        val results = PaniniVM().evalScript(
            """
            अवस्था + अम् क्षेत्र + अम् परिणाम + मतुप् + सुँ ।
            प्रयत्न + ल्युट् + सुँ ।
            एक + अम् द्वि + अम् च विद् + लोट् + सिप् ॥
            एक + कृत्वः यावत् फल + सुँ न तावत् प्रयत्न + ल्युट् + टा कृ + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertTrue(results.any { it is ExecutionResult.Failure }, results.toString())
    }
}

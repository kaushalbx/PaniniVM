package dev.panini.execution

import kotlin.test.*

class DerivedActionHistoryTest {
    @org.junit.jupiter.api.io.TempDir lateinit var directory: java.nio.file.Path
    private val first = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।"
    private val second = "द्वि + औट् त्रि + शस् च युज् + लोट् + सिप् ।"

    @Test fun `failed actions cannot advance derived history`() {
        val vm = PaniniVM()
        val key = "failed-derived-history"
        assertIs<ExecutionResult.Success>(vm.eval(first, sessionKey = key))
        val identity = assertNotNull(vm.kriyaMemory(key).entries.single().frame.kriya?.actionIdentity)
        val scope = ExecutionScope(environment = ValueEnvironment(mapOf(
            "पूर्व" to SanskritValue.Sankhya(Long.MAX_VALUE, "maximum"),
            "उत्तर" to SanskritValue.Sankhya(1L, "one"))))
        assertIs<ExecutionResult.Failure>(vm.eval(
            "पूर्व + अम् उत्तर + अम् च युज् + णिच् + लोट् + सिप् ।", sessionKey = key, scope = scope))
        assertEquals(1, vm.kriyaMemory(key).matchingActions(identity).size)
    }

    @Test fun `host budget failure records only successful qualified actions`() {
        val vm = PaniniVM(executionLimits = ExecutionLimits(maxConditionIterations = 1L))
        val key = "budget-derived-history"
        val body = first.removeSuffix("।")
        val results = vm.evalScript("यावत् एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् तावत् $body ।", sessionKey = key)
        assertIs<ExecutionResult.Failure>(results.last())
        val padas = dev.panini.vyakaranam.parser.PaniniParser().parse(
            "युज् + णिच् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas
        val reference = dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(padas).single()
        assertEquals(1, vm.kriyaMemory(key).matchingActions(reference.actionIdentity).size)
        assertNotNull(reference.select(vm.kriyaMemory(key)))
    }

    @Test fun `persisted ordered prefixes do not collapse into a set`() {
        val key = "ordered-prefix-history"
        val vm = PaniniVM(directory.toFile())
        assertIs<ExecutionResult.Success>(vm.eval(
            "नवन् + शस् द्वि + औट् च प्र + वि + युज् + णिच् + लोट् + सिप् ।", sessionKey = key))
        assertIs<ExecutionResult.Success>(vm.eval(
            "नवन् + शस् त्रि + शस् च वि + प्र + युज् + णिच् + लोट् + सिप् ।", sessionKey = key))
        val restored = PaniniVM(directory.toFile())
        val result = assertIs<ExecutionResult.Success>(restored.eval(
            "प्र + वि + युज् + णिच् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।", sessionKey = key))
        assertEquals("सप्त", result.value)
    }

    @Test fun `qualified action identity and participant snapshots survive a fresh VM reload`() {
        val key = "derived-history"
        val original = PaniniVM(directory.toFile())
        assertIs<ExecutionResult.Success>(original.eval(first, sessionKey = key))
        assertIs<ExecutionResult.Success>(original.eval(second, sessionKey = key))
        val before = original.kriyaMemory(key).entries.map { it.frame.kriya?.actionIdentity }
        val restored = PaniniVM(directory.toFile())
        assertEquals(before, restored.kriyaMemory(key).entries.map { it.frame.kriya?.actionIdentity })
        for (head in listOf("फल", "कर्मन्")) {
            val query = "युज् + णिच् + ल्युट् + ङस् $head + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val expected = assertIs<ExecutionResult.Success>(PaniniVM().evalScript("$first $query").last())
            assertEquals(expected.typedValue, assertIs<ExecutionResult.Success>(restored.eval(query, sessionKey = key)).typedValue)
            val missing = "युज् + णिच् + ल्युट् + ङस् द्वि + तीय + अम् $head + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertIs<ExecutionResult.Failure>(restored.eval(missing, sessionKey = key))
        }
    }

    @Test fun `explicit causative participant reference cannot read later plain action operands`() {
        val query = "युज् + णिच् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val expected = assertIs<ExecutionResult.Success>(PaniniVM().evalScript("$first $query").last())
        val actual = assertIs<ExecutionResult.Success>(PaniniVM().evalScript("$first $second $query").last())
        assertEquals(expected.typedValue, actual.typedValue)
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript("$second $query").last())
    }

    @Test fun `explicit causative action noun selects its own result rather than latest root result`() {
        val query = "युज् + णिच् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val result = assertIs<ExecutionResult.Success>(PaniniVM().evalScript("$first $second $query").last())
        assertEquals("त्रीणि", result.value)
        val missing = PaniniVM().evalScript("$second $query").last()
        assertIs<ExecutionResult.Failure>(missing)
    }

    @Test fun `qualified history filters before applying ordinals and previous`() {
        for (qualifier in listOf("द्वि + तीय + अम्", "पूर्व + अम्")) {
            val query = "युज् + णिच् + ल्युट् + ङस् $qualifier फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript("$first $second $query").last())
        }
    }

    @Test fun `qualified result matching retains local clause morphology`() {
        val source = first.removeSuffix("।") + "ततः " + second.removeSuffix("।") +
            "ततः युज् + णिच् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("त्रीणि", assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last()).value)
    }
}

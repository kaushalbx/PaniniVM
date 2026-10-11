package dev.panini.execution

import kotlin.test.*

class CoordinatedHistoryReferenceTest {
    private val query = "युज् + ल्युट् + ङस् फल + अम् च नवन् + अम् च मुद्र् + णिच् + लोट् + सिप् ।"

    @Test fun `coordinated history participants retain their grammatical role during parameter binding`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        for (head in listOf("कर्मन्", "फल")) {
            val invocation = assertIs<dev.panini.vyakaranam.ast.Invocation>(parser.parse(
                "युज् + ल्युट् + ङस् $head + अम् च नवन् + अम् च मुद्र् + णिच् + लोट् + सिप् ।").body)
            val group = invocation.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SamuccitaSubanta>().single()
            val protected = PrakriyaAstArgumentBinder.protectedParameterPadas(invocation.vakya.padas)
            if (head == "कर्मन्") {
                val reference = dev.panini.execution.binding.KarakaReferenceResolver.references(invocation.vakya.padas).single()
                assertSame(group.members.first(), reference.referent)
            }
            assertTrue(protected.any { it === group.members.first() }, head)
            assertTrue(protected.any { it === invocation.vakya.padas.first() }, head)
            assertFalse(protected.any { it === group.members.last() }, head)
        }
    }

    @Test fun `coordinated remembered participants resolve independently of ordinary operands`() {
        val vm = PaniniVM()
        assertIs<ExecutionResult.Success>(vm.eval(
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "participants"))
        val result = assertIs<ExecutionResult.Success>(vm.eval(
            "युज् + ल्युट् + ङस् कर्मन् + अम् च नवन् + अम् च मुद्र् + णिच् + लोट् + सिप् ।",
            sessionKey = "participants"))
        assertEquals("एक द्वि नवन्", result.value)
    }

    @Test fun `coordinated named action result requires a completed antecedent`() {
        val result = PaniniVM().eval(query)
        assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
    }

    @Test fun `coordinated named action result resolves the remembered value`() {
        val vm = PaniniVM()
        assertIs<ExecutionResult.Success>(vm.eval(
            "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "coordination"))
        val result = assertIs<ExecutionResult.Success>(vm.eval(query, sessionKey = "coordination"))
        assertEquals("त्रीणि नवन्", result.value)
    }
}

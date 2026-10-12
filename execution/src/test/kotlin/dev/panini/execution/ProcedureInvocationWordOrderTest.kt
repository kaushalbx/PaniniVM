package dev.panini.execution

import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ProcedureInvocationWordOrderTest {
    @Test fun `dual and plural procedure identities cannot collapse to one registry entry`() {
        for (ending in listOf("भ्याम्", "भिस्")) {
            val error = assertFailsWith<IllegalArgumentException> {
                PrakriyaInvocationMatcher.match(PaniniParser().parse(
                    "पञ्च + अम् परीक्षण + $ending डुकृञ् + उ + लोट् + सिप् ।"), setOf("परीक्षण"))
            }
            assertTrue(error.message.orEmpty().contains("instrumental singular"))
        }
    }

    @Test fun `dual and plural domains cannot collapse to one scope identity`() {
        for (ending in listOf("ओस्", "आम्")) {
            val error = assertFailsWith<IllegalArgumentException> {
                PrakriyaInvocationMatcher.match(PaniniParser().parse(
                    "पञ्च + अम् परीक्षण + टा गणित + $ending डुकृञ् + उ + लोट् + सिप् ।"), setOf("परीक्षण"))
            }
            assertTrue(error.message.orEmpty().contains("genitive singular"))
        }
    }

    @Test fun `unrelated printing action never executes a same named procedure body`() {
        val source = """
            परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            नव + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् परीक्षण + टा मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val baseline = PaniniVM().evalScript("पञ्च + अम् परीक्षण + टा मुद्र् + णिच् + लोट् + सिप् ।")
        val expected = assertIs<ExecutionResult.Success>(baseline.last())
        val results = PaniniVM().evalScript(source)
        val actual = assertIs<ExecutionResult.Success>(results.last(), results.toString())
        assertEquals(expected.typedValue, actual.typedValue)
        assertEquals(expected.value, actual.value)
        assertEquals(baseline.filterIsInstance<ExecutionResult.Success>().filter { it.outputKind == OutputKind.CONSOLE }.map { it.value },
            results.filterIsInstance<ExecutionResult.Success>().filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }

    @Test fun `known instrumental name cannot turn an unrelated finite verb into a procedure call`() {
        for (head in listOf("मुद्र् + णिच् + लोट् + सिप्", "युज् + णिच् + लोट् + सिप्",
            "कृ + णिच् + लोट् + सिप्", "सम् + कृ + लोट् + सिप्")) {
            assertNull(PrakriyaInvocationMatcher.match(PaniniParser().parse(
                "पञ्च + अम् परीक्षण + टा $head ।"), setOf("परीक्षण")), head)
        }
    }

    @Test fun `domain is not restricted to the prefix of a procedure noun`() {
        for (phrase in listOf("पञ्च + अम् गणित + ङस् परीक्षण + टा डुकृञ् + उ + लोट् + सिप्",
            "पञ्च + अम् परीक्षण + टा गणित + ङस् डुकृञ् + उ + लोट् + सिप्",
            "डुकृञ् + उ + लोट् + सिप् परीक्षण + टा पञ्च + अम् गणित + ङस्")) {
            val shape = assertNotNull(PrakriyaInvocationMatcher.match(PaniniParser().parse("$phrase ।"), setOf("परीक्षण")))
            assertEquals("गणित", shape.domainStem)
            assertEquals(1, shape.argumentPadas.size)
        }
    }

    @Test fun `competing unassigned domains cannot be resolved by nearest position`() {
        for (domains in listOf("गणित + ङस् भाषा + ङस्", "भाषा + ङस् गणित + ङस्")) {
            assertFailsWith<IllegalArgumentException> {
                PrakriyaInvocationMatcher.match(PaniniParser().parse("पञ्च + अम् $domains परीक्षण + टा डुकृञ् + उ + लोट् + सिप् ।"), setOf("परीक्षण"))
            }
        }
    }

    @Test fun `instrumental procedure identity does not impose an operand prefix`() {
        for (phrase in listOf("पञ्च + अम् परीक्षण + टा डुकृञ् + उ + लोट् + सिप्",
            "परीक्षण + टा पञ्च + अम् डुकृञ् + उ + लोट् + सिप्",
            "डुकृञ् + उ + लोट् + सिप् परीक्षण + टा पञ्च + अम्")) {
            val shape = assertNotNull(PrakriyaInvocationMatcher.match(PaniniParser().parse("$phrase ।"), setOf("परीक्षण")))
            assertEquals("परीक्षण", shape.operationStem)
            assertEquals(1, shape.argumentPadas.size)
        }
    }

    @Test fun `two known instrumental procedures are ambiguous in either order`() {
        for (names in listOf("परीक्षण + टा गणन + टा", "गणन + टा परीक्षण + टा")) {
            assertFailsWith<IllegalArgumentException> {
                PrakriyaInvocationMatcher.match(PaniniParser().parse("पञ्च + अम् $names डुकृञ् + उ + लोट् + सिप् ।"),
                    setOf("परीक्षण", "गणन"))
            }
        }
    }
}

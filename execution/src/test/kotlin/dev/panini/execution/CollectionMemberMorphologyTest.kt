package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class CollectionMemberMorphologyTest {
    @Test
    fun `final selector is typed and survives a same named variable and codec`() {
        for (phrase in listOf("सूची + ङस् अन्तिम + अम्", "अन्तिम + अम् सूची + ङस्")) {
            val bound = assertIs<ExecutionBindingResult.Bound>(
                dev.panini.execution.binding.VyakaranamExecutionAdapter.bind(
                    SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "$phrase उद् + हृ + लोट् + सिप् ।"),
                    SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                    environment = ValueEnvironment(mapOf("अन्तिम" to SanskritValue.Sankhya(9, "नव")))))
            val member = assertIs<ExecutionExpression.Pada>(
                bound.ukti.invocations.single().bindings[dev.panini.core.Karaka.KARMAN])
            assertEquals(CollectionMemberSelection.FINAL, member.memberSelection)
            assertEquals(member, dev.panini.execution.sutra.ProgramSutraArthaCodec.decodeExpression(
                dev.panini.execution.sutra.ProgramSutraArthaCodec.encodeExpression(member)))
        }
    }

    @Test
    fun `nonfinite extraction retains selector morphology in shared lowering`() {
        for (selector in listOf("अन्तिम + मतुप् + अम्", "अन्तिम + तरप् + अम्", "अन्तिम + शस्")) {
            val source = "सूची + ङस् $selector उद् + हृ + ल्यप् मुद्र् + णिच् + लोट् + सिप् ।"
            val invocation = PaniniParser().parse(source).body as Invocation
            assertFailsWith<IllegalArgumentException>(selector) { PriorActionLowering.expand(invocation) }
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(
                "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n$source",
            ).last(), selector)
        }
        val results = PaniniVM().evalScript(
            "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङस् अन्तिम + अम् उद् + हृ + ल्यप् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals("द्वि", assertIs<ExecutionResult.Success>(results.last()).value)
    }

    @Test
    fun `final member morphology is validated before execution binding`() {
        for (selector in listOf("अन्तिम + मतुप् + अम्", "अन्तिम + तरप् + अम्",
            "अन्तिम + टाप् + अम्", "अन्तिम + शस्", "अन्तिम + अम् अन्तिम + अम्")) {
            val source = "सूची + ङस् $selector उद् + हृ + लोट् + सिप् ।"
            val invocation = PaniniParser().parse(source).body as Invocation
            assertFailsWith<IllegalArgumentException>(selector) {
                CollectionMemberMorphology.validate(invocation)
            }
            val results = PaniniVM().evalScript(
                "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n$source",
            )
            assertIs<ExecutionResult.Failure>(results.last(), selector)
        }
    }

    @Test
    fun `plain final member supports either nominal word order`() {
        for (phrase in listOf("सूची + ङस् अन्तिम + अम्", "अन्तिम + अम् सूची + ङस्")) {
            val results = PaniniVM().evalScript(
                "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                    "$phrase उद् + हृ + लोट् + सिप् ।",
            )
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals(2L, assertIs<SanskritValue.Sankhya>(
                assertIs<ExecutionResult.Success>(results.last()).typedValue).value)
        }
    }
}

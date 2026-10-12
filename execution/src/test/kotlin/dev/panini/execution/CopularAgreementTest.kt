package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class CopularAgreementTest {
    @Test fun `copular gender check rejects established mismatch without guessing unknown names`() {
        fun node(subject: String) = PaniniParser().parse(
            "$subject + सुँ मान + ङसिँ न्यून + टाप् + सुँ असँ + लट् + तिप् ।").body as Invocation
        assertFailsWith<IllegalArgumentException> { CopularAgreement.validate(node("मूल्य")) }
        CopularAgreement.validate(node("प्रथम + टाप्"))
        CopularAgreement.validate(node("अज्ञातनाम"))
    }

    @Test fun `written subject number is checked independently of scalar host value`() {
        for (ending in listOf("औ", "जस्")) {
            val source = "वस्तु + $ending मान + ङसिँ न्यून + सुँ असँ + लट् + तिप् ।"
            val node = PaniniParser().parse(source).body as Invocation
            assertFailsWith<IllegalArgumentException>(ending) { CopularAgreement.validate(node) }
        }
    }

    @Test fun `plural or dual predicate cannot lose its number before singular copula binding`() {
        for (ending in listOf("औ", "जस्")) {
            val clause = "एक + सुँ द्वि + ङसिँ न्यून + $ending असँ + लट् + तिप् ।"
            val node = PaniniParser().parse(clause).body as Invocation
            assertFailsWith<IllegalArgumentException>(ending) { CopularAgreement.validate(node) }
            val results = PaniniVM().evalScript("यदि ${clause.removeSuffix("।")} तर्हि " +
                "एक + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा " +
                "द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।")
            assertIs<ExecutionResult.Failure>(results.last(), results.toString())
        }
    }

    @Test fun `written singular agreement does not depend on numeric payload magnitude`() {
        val node = PaniniParser().parse("द्वि + सुँ त्रि + ङसिँ न्यून + सुँ असँ + लट् + तिप् ।").body as Invocation
        CopularAgreement.validate(node)
    }

    @Test fun `agreement validator permits matching dual and plural morphology`() {
        for ((ending, ting) in listOf("औ" to "तस्", "जस्" to "झि")) {
            val node = PaniniParser().parse(
                "वस्तु + $ending मान + ङसिँ न्यून + $ending असँ + लट् + $ting ।").body as Invocation
            CopularAgreement.validate(node)
        }
    }
}

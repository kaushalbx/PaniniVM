package dev.panini

import dev.panini.derivation.UnresolvedKarakaDerivationException
import kotlin.test.*

class KarakaDerivationCliTest {
    @Test fun `recipient derivation requires object mediation and accepts explicit omitted object`() {
        val arguments = listOf("राम", "SAMPRADANA", "EKAVACANA", "दा")
        assertFailsWith<UnresolvedKarakaDerivationException> { DerivationCliCommands.karaka(arguments) }
        for (voice in listOf(emptyList(), listOf("KARTARI"), listOf("KARMANI"))) {
            val output = DerivationCliCommands.karaka(arguments + voice + "--implied-object")
            assertEquals("CHATURTHI EKAVACANA: रामाय", output.first())
            assertTrue(output.any { it.contains("1.4.32") })
            assertTrue(output.any { it.contains("2.3.13") })
        }
        assertFailsWith<IllegalArgumentException> {
            DerivationCliCommands.karaka(arguments + "--implied-objet")
        }
    }
}

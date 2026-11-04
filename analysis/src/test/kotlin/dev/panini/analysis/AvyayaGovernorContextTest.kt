package dev.panini.analysis

import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class AvyayaGovernorContextTest {
    @Test fun `governor binding uses actual clause occurrence rather than equal text`() {
        val actual = AvyayaPada("दक्षिणेन", "दक्षिणेन", AvyayaTaddhitaDerivation("दक्षिण", "एनप्"))
        val equalButUnattached = actual.copy()
        assertEquals(actual, equalButUnattached)
        VakyaSemanticContext(avyayaGovernors = mapOf(0 to actual)).validate(1, listOf(actual))
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(avyayaGovernors = mapOf(0 to equalButUnattached)).validate(1, listOf(actual))
        }
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(avyayaGovernors = mapOf(-1 to actual))
        }
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(avyayaGovernors = mapOf(1 to actual)).validate(1, listOf(actual))
        }
        assertFailsWith<IllegalArgumentException> {
            VakyaSemanticContext(krtGovernors = mapOf(0 to KrtGovernorReading(1, emptySet())),
                avyayaGovernors = mapOf(0 to actual))
        }
    }
}

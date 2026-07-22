package dev.panini.vyakaranam.analysis

import dev.panini.core.Karaka
import dev.panini.core.Prayoga
import kotlin.test.Test
import kotlin.test.assertEquals

class KarakaInferenceTest {
    @Test
    fun `bhyam retains all kartari karaka possibilities`() {
        assertEquals(
            setOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
            KarakaInference.candidates("भ्याम्", Prayoga.KARTARI),
        )
    }

    @Test
    fun `bhyam in passive includes agent and non-agent possibilities`() {
        assertEquals(
            setOf(Karaka.KARTR, Karaka.SAMPRADANA, Karaka.APADANA),
            KarakaInference.candidates("भ्याम्", Prayoga.KARMANI),
        )
    }
}

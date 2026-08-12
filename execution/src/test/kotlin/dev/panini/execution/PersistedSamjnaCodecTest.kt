package dev.panini.execution

import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals

class PersistedSamjnaCodecTest {

    @Test
    fun `round trips enum and rudhi samjnas`() {
        listOf<Samjna>(Samjna.SHABDA, Samjna.Rudhi("विशेष")).forEach { samjna ->
            assertEquals(samjna, PersistedSamjnaCodec.decode(PersistedSamjnaCodec.encode(samjna)))
        }
    }

    @Test
    fun `preserves established rudhi wire format`() {
        assertEquals("RUDHI:विशेष", PersistedSamjnaCodec.encode(Samjna.Rudhi("विशेष")))
    }
}

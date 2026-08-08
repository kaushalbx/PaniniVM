package dev.panini.execution

import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PradayaUpasargaEngineTest {

    @Test
    fun `recognizes actions from parsed tinganta structure`() {
        val source = "प्र + कृ + लोट् + सिप् ।"
        val ukti = PaniniParser().parse(source)
        assertTrue(PradayaUpasargaEngine.isVerbAction(source))
        assertTrue(PradayaUpasargaEngine.isVerbAction("ignored", ukti))
    }

    @Test
    fun `does not infer actions from lexical fragments`() {
        assertFalse(PradayaUpasargaEngine.isVerbAction("लोट्"))
        assertFalse(PradayaUpasargaEngine.isVerbAction("कृ"))
        assertFalse(PradayaUpasargaEngine.isVerbAction("कृ + अम् ।"))
    }
}

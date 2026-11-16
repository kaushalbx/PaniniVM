package dev.panini.derivation

import dev.panini.dhatupatha.DhatuPatha
import kotlin.test.Test
import kotlin.test.assertEquals

class UpasargaNatvaTest {
    private val engine = SandhiEngine()
    private fun root(upadesha: String) = DhatuPatha.all.first { it.upadesha == upadesha }

    @Test fun `8_4_14 retroflexes the n of a nopadesha root after an upasarga`() {
        assertEquals("प्रणमति", engine.joinPrefix("प्र", "नमति", root("णमँ")))
        assertEquals("पराणयति", engine.joinPrefix("परा", "नयति", root("णीञ्")))
        // नन्दति is from टुनदिँ, not taught with ण.
        assertEquals("प्रनन्दति", engine.joinPrefix("प्र", "नन्दति", root("टुनदिँ")))
        // No र्/ष् in the upasarga.
        assertEquals("उपनमति", engine.joinPrefix("उप", "नमति", root("णमँ")))
        // Without the root the spelling alone decides nothing.
        assertEquals("प्रनमति", engine.joinPrefix("प्र", "नमति"))
    }

    @Test fun `8_4_17 retroflexes ni before the listed roots`() {
        assertEquals("प्रणिगदति", engine.joinPrefix("प्र", "निगदति", root("गदँ")))
        assertEquals("परिणिवहति", engine.joinPrefix("परि", "निवहति", root("वहँ")))
        // नि's न comes first: 8.4.14 does not take it for the root's न.
        assertEquals("प्रणिनदति", engine.joinPrefix("प्र", "निनदति", root("णदँ")))
    }

    @Test fun `8_4_18 is optional and declined by default`() {
        assertEquals("प्रनिपचति", engine.joinPrefix("प्र", "निपचति", root("डुपचँष्")))
    }
}

class HinumiAnitehTest {
    private val engine = SandhiEngine()
    private fun root(id: String) = requireNotNull(DhatuPatha.find(id))

    @Test fun `8_4_15 hinu and mina`() {
        assertEquals("प्रहिणोति", engine.joinPrefix("प्र", "हिनोति", root("05.0012")))
        assertEquals("प्रमीणाति", engine.joinPrefix("प्र", "मीनाति", root("09.0004")))
    }

    @Test fun `8_4_19 an after the vowel join`() {
        assertEquals("प्राणिति", engine.joinPrefix("प्र", "अनिति", root("02.0065")))
        assertEquals("पराणिति", engine.joinPrefix("परा", "अनिति", root("02.0065")))
        // No upasarga र्: उप + अनिति keeps न.
        assertEquals("उपानिति", engine.joinPrefix("उप", "अनिति", root("02.0065")))
    }
}

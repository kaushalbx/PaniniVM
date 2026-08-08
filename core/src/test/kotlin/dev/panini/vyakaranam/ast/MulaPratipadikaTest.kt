package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MulaPratipadikaTest {

    @Test
    fun `classifies canonical lexical identities`() {
        assertEquals(MulaPratipadikaIdentity.ADHIKARA, pratipadika("अधिकार").lexicalIdentity)
        assertEquals(MulaPratipadikaIdentity.SAMAVAYA, pratipadika("समवाय").lexicalIdentity)
        assertNull(pratipadika("राम").lexicalIdentity)
    }

    private fun pratipadika(text: String) = MulaPratipadika(text, text)
}

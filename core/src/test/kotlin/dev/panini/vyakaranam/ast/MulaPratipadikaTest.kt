package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MulaPratipadikaTest {

    @Test
    fun `classifies canonical lexical identities`() {
        assertEquals(MulaPratipadikaIdentity.ADHIKARA, pratipadika("अधिकार").lexicalIdentity)
        assertEquals(MulaPratipadikaIdentity.ANTARANGA, pratipadika("अन्तरङ्ग").lexicalIdentity)
        assertEquals(MulaPratipadikaIdentity.APAVADA, pratipadika("अपवाद").lexicalIdentity)
        assertEquals(MulaPratipadikaIdentity.NITYA, pratipadika("नित्य").lexicalIdentity)
        assertEquals(MulaPratipadikaIdentity.SAMJNA, pratipadika("संज्ञा").lexicalIdentity)
        assertEquals(MulaPratipadikaIdentity.SAMAVAYA, pratipadika("समवाय").lexicalIdentity)
        assertNull(pratipadika("राम").lexicalIdentity)
    }

    private fun pratipadika(text: String) = MulaPratipadika(text, text)
}

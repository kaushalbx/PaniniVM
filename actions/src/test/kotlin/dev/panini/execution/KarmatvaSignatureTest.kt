package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.shiksha.Karmatva
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KarmatvaSignatureTest {

    @Test
    fun `fromKarmatva creates Kartr requirement for Akarmaka`() {
        val sig = OperationSignature.fromKarmatva(Karmatva.AKARMAKA)
        assertEquals(1, sig.requirements.size)
        assertEquals(Karaka.KARTR, sig.requirements[0].karaka)
    }

    @Test
    fun `fromKarmatva creates Kartr and Karman requirement for Sakarmaka`() {
        val sig = OperationSignature.fromKarmatva(Karmatva.SAKARMAKA)
        assertEquals(2, sig.requirements.size)
        assertEquals(Karaka.KARTR, sig.requirements[0].karaka)
        assertEquals(Karaka.KARMAN, sig.requirements[1].karaka)
        assertEquals(1, sig.requirements[1].minimumMembers)
    }

    @Test
    fun `fromKarmatva creates dual Karman requirement for Dvikarmaka`() {
        val sig = OperationSignature.fromKarmatva(Karmatva.DVIKARMAKA)
        assertEquals(2, sig.requirements.size)
        assertEquals(Karaka.KARTR, sig.requirements[0].karaka)
        assertEquals(Karaka.KARMAN, sig.requirements[1].karaka)
        assertEquals(2, sig.requirements[1].minimumMembers)
    }

    @Test
    fun `fromKarmatva respects optional karakas`() {
        val sig = OperationSignature.fromKarmatva(
            Karmatva.SAKARMAKA,
            optionalKarakas = setOf(Karaka.ADHIKARANA, Karaka.KARANA),
        )
        assertEquals(2, sig.requirements.size)
        assertTrue(Karaka.ADHIKARANA in sig.optionalKarakas)
        assertTrue(Karaka.KARANA in sig.optionalKarakas)
    }
}

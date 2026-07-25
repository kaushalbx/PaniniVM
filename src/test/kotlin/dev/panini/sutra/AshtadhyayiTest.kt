package dev.panini.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AshtadhyayiTest {


    @Test
    fun `exposes executable derivation sutras by sutra number`() {
        val sutra = Ashtadhyayi.registry.require("6.1.101")

        assertEquals("अकः सवर्णे दीर्घः", sutra.sutraText)
        assertEquals("6.1.101", sutra.sutra)
    }

    @Test
    fun `navigates loaded sutras by purpose and dependency`() {
        assertEquals(
            listOf("1.1.3"),
            Ashtadhyayi.registry.dependentsOf("1.1.2").map { it.sutra },
        )
        assertEquals(
            emptyList(),
            Ashtadhyayi.registry.dependenciesOf("6.1.87").map { it.sutra },
        )
        assertTrue(Ashtadhyayi.registry.withAction(SutraAction.NISHEDHA).isNotEmpty())
        assertEquals("4.1.2", Ashtadhyayi.registry.require("4.1.2").sutra)
        val vidhiSutras = Ashtadhyayi.registry.withRole(SutraRole.Vidhi).map { it.sutra }
        assertTrue(
            vidhiSutras.containsAll(
                listOf("1.3.9", "4.1.2", "6.1.101", "7.1.54", "7.3.103", "8.3.59", "8.4.58"),
            ),
        )
    }

}

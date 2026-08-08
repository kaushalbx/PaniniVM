package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class KridantaPratipadikaTest {

    @Test
    fun `classifies exact kta identity`() {
        assertEquals(KrtPratyayaIdentity.KTA, kridanta("क्त").krtPratyayaIdentity)
        assertNull(kridanta("क्तवतुँ").krtPratyayaIdentity)
        assertNull(kridanta("ल्युट्").krtPratyayaIdentity)
    }

    @Test
    fun `classifies exact ghan identity`() {
        assertEquals(KrtPratyayaIdentity.GHAN, kridanta("घञ्").krtPratyayaIdentity)
        assertNull(kridanta("ल्युट्").krtPratyayaIdentity)
    }

    private fun kridanta(pratyaya: String) = KridantaPratipadika(
        sourceText = "सिध् + $pratyaya",
        upasargas = emptyList(),
        dhatu = DhatuPrakriti("सिध्", "सिध्"),
        krtPratyaya = pratyaya,
    )
}

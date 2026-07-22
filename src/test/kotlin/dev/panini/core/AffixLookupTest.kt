package dev.panini.core

import kotlin.test.Test
import kotlin.test.assertEquals

class AffixLookupTest {

    @Test
    fun `resolves annotated sup upadeshas through canonical affixes`() {
        assertEquals(SupAffix.SU, SupAffix.fromUpadesha("सुँ"))
        assertEquals(SupAffix.NGASI, SupAffix.fromUpadesha("ङसिँ"))
        assertEquals(
            listOf(SupAffix.BHYAM_3, SupAffix.BHYAM_4, SupAffix.BHYAM_5),
            SupAffix.candidates("भ्याम्"),
        )
    }

    @Test
    fun `resolves all annotated ting upadeshas through canonical affixes`() {
        TingAffix.entries.forEach { affix ->
            assertEquals(affix, TingAffix.fromUpadesha(affix.upadesha))
        }
    }
}

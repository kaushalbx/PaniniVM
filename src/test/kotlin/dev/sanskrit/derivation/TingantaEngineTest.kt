package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TingantaEngineTest {

    @Test
    fun `conjugation engine derives individual forms for bhu`() {
        val engine = TingantaEngine()

        // Lat (Present)
        assertEquals("भवति", engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LAT)).final.surface)
        assertEquals("भवन्ति", engine.derive(TingantaDerivationRequest("भू", Vacana.BAHUVACANA, Purusha.PRATHAMA, Lakara.LAT)).final.surface)
        assertEquals("भवामि", engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.UTTAMA, Lakara.LAT)).final.surface)

        // Lrt (Future)
        val resultLrt = engine.derive(TingantaDerivationRequest("भू", Vacana.DVIVACANA, Purusha.UTTAMA, Lakara.LRT))
        println("--- LRT VAS DERIVATION TRACE ---")
        resultLrt.applications.forEach { println("${it.sutra} — ${it.explanation}") }
        println("Final terms: " + resultLrt.final.terms.map { "${it.id}: ${it.surface} (kind=${it.kind}, upadesha=${it.upadesha}, it=${it.itMarkers})" })
        assertEquals("भविष्यति", engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LRT)).final.surface)
        assertEquals("भविष्यन्ति", engine.derive(TingantaDerivationRequest("भू", Vacana.BAHUVACANA, Purusha.PRATHAMA, Lakara.LRT)).final.surface)
        assertEquals("भविष्यामि", engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.UTTAMA, Lakara.LRT)).final.surface)
    }

    @Test
    fun `conjugation engine derives complete paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LAT)
        val forms = paradigm.surfaces

        assertEquals(9, forms.size)
        assertEquals("भवति", forms[TingAffix.TIP])
        assertEquals("भवतः", forms[TingAffix.TAS])
        assertEquals("भवन्ति", forms[TingAffix.JHI])
        assertEquals("भवसि", forms[TingAffix.SIP])
        assertEquals("भवथः", forms[TingAffix.THAS])
        assertEquals("भवथ", forms[TingAffix.THA])
        assertEquals("भवामि", forms[TingAffix.MIP])
        assertEquals("भवावः", forms[TingAffix.VAS])
        assertEquals("भवामः", forms[TingAffix.MAS])
        
        paradigm.coverage.forEach { row ->
            assertTrue("3.4.78" in row.appliedSutras, "Form for ${row.affix} is missing 3.4.78")
            assertEquals("derived", row.note)
        }
    }

    @Test
    fun `conjugation engine derives complete future paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LRT)
        val forms = paradigm.surfaces

        assertEquals(9, forms.size)
        assertEquals("भविष्यति", forms[TingAffix.TIP])
        assertEquals("भविष्यतः", forms[TingAffix.TAS])
        assertEquals("भविष्यन्ति", forms[TingAffix.JHI])
        assertEquals("भविष्यसि", forms[TingAffix.SIP])
        assertEquals("भविष्यथः", forms[TingAffix.THAS])
        assertEquals("भविष्यथ", forms[TingAffix.THA])
        assertEquals("भविष्यामि", forms[TingAffix.MIP])
        assertEquals("भविष्यावः", forms[TingAffix.VAS])
        assertEquals("भविष्यामः", forms[TingAffix.MAS])
        
        paradigm.coverage.forEach { row ->
            assertTrue("3.4.78" in row.appliedSutras, "Form for ${row.affix} is missing 3.4.78")
            assertTrue("3.1.33" in row.appliedSutras, "Form for ${row.affix} is missing 3.1.33")
            assertTrue("8.3.59" in row.appliedSutras, "Form for ${row.affix} is missing 8.3.59")
            assertEquals("derived", row.note)
        }
    }

    @Test
    fun `conjugation engine rejects unknown dhatu`() {
        assertFailsWith<IllegalArgumentException> {
            TingantaEngine().derive(TingantaDerivationRequest("unknown_dhatu"))
        }
    }
}

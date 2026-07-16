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
        assertEquals("भवतः", engine.derive(TingantaDerivationRequest("भू", Vacana.DVIVACANA, Purusha.PRATHAMA, Lakara.LAT)).final.surface)
        assertEquals("भवन्ति", engine.derive(TingantaDerivationRequest("भू", Vacana.BAHUVACANA, Purusha.PRATHAMA, Lakara.LAT)).final.surface)
        assertEquals("भवामि", engine.derive(TingantaDerivationRequest("भू", Vacana.EKAVACANA, Purusha.UTTAMA, Lakara.LAT)).final.surface)

        // Lrt (Future)
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
    fun `conjugation engine derives complete imperfect past paradigm for bhu`() {

        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LANG)
        val forms = paradigm.surfaces

        assertEquals(9, forms.size)
        assertEquals("अभवत्", forms[TingAffix.TIP])
        assertEquals("अभवताम्", forms[TingAffix.TAS])
        assertEquals("अभवन्", forms[TingAffix.JHI])
        assertEquals("अभवः", forms[TingAffix.SIP])
        assertEquals("अभवतम्", forms[TingAffix.THAS])
        assertEquals("अभवत", forms[TingAffix.THA])
        assertEquals("अभवम्", forms[TingAffix.MIP])
        assertEquals("अभवाव", forms[TingAffix.VAS])
        assertEquals("अभवाम", forms[TingAffix.MAS])
        
        paradigm.coverage.forEach { row ->
            assertTrue("3.4.78" in row.appliedSutras, "Form for ${row.affix} is missing 3.4.78")
            assertTrue("3.2.111" in row.appliedSutras, "Form for ${row.affix} is missing 3.2.111")
            assertTrue("6.4.71" in row.appliedSutras, "Form for ${row.affix} is missing 6.4.71")
            assertEquals("derived", row.note)
        }
    }

    @Test
    fun `conjugation engine derives complete imperative paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LOT)
        val forms = paradigm.surfaces

        assertEquals(9, forms.size)
        assertEquals("भवतु", forms[TingAffix.TIP])
        assertEquals("भवताम्", forms[TingAffix.TAS])
        assertEquals("भवन्तु", forms[TingAffix.JHI])
        assertEquals("भव", forms[TingAffix.SIP])
        assertEquals("भवतम्", forms[TingAffix.THAS])
        assertEquals("भवत", forms[TingAffix.THA])
        assertEquals("भवानि", forms[TingAffix.MIP])
        assertEquals("भवाव", forms[TingAffix.VAS])
        assertEquals("भवाम", forms[TingAffix.MAS])

        paradigm.coverage.forEach { row ->
            assertTrue("3.3.162" in row.appliedSutras, "Form for ${row.affix} is missing 3.3.162")
        }
    }

    @Test
    fun `conjugation engine rejects unknown dhatu`() {
        assertFailsWith<IllegalArgumentException> {
            TingantaEngine().derive(TingantaDerivationRequest("unknown_dhatu"))
        }
    }

    @Test
    fun `liṅ selection uses 3 3 161`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "भू" }
        val result = DerivationEngine().derive(TingantaDerivationRequest("भू", lakara = Lakara.LING).initialState(dhatu))
        assertEquals("भवेत्", result.final.surface)

        assertTrue(result.applications.any { it.sutra == "3.3.161" })
        assertTrue(result.applications.any { it.sutra == "3.4.103" })
        assertTrue(result.applications.any { it.sutra == "7.2.80" })
        assertTrue(result.applications.none { it.sutra == "7.2.79" })
        assertTrue(result.applications.any { it.sutra == "7.3.84" })

        val shapApplication = result.applications.first { it.sutra == "3.1.68" }
        val termsAfterShap = shapApplication.after.terms
        assertTrue(termsAfterShap.indexOfFirst { it.id == "shap" } < termsAfterShap.indexOfFirst { it.id == "yasut" })

        val trace = result.applications.map { it.sutra }
        assertTrue(trace.indexOf("3.3.161") < trace.indexOf("3.4.78"))
        assertTrue(trace.indexOf("3.4.78") < trace.indexOf("3.4.103"))
        assertTrue(trace.indexOf("3.4.103") < trace.indexOf("7.2.80"))
    }

    @Test
    fun `liṅ first plural selects jus through 3 4 108`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "भू" }
        val result = DerivationEngine().derive(
            TingantaDerivationRequest("भू", purusha = Purusha.PRATHAMA, vacana = Vacana.BAHUVACANA, lakara = Lakara.LING)
                .initialState(dhatu),
        )

        assertTrue(result.applications.any { it.sutra == "3.4.108" })
        assertEquals("भवेयुः", result.final.surface)
    }

    @Test
    fun `liṅ third dual derives bhavetam`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "भू" }
        val result = DerivationEngine().derive(
            TingantaDerivationRequest("भू", purusha = Purusha.PRATHAMA, vacana = Vacana.DVIVACANA, lakara = Lakara.LING)
                .initialState(dhatu),
        )

        assertTrue(result.applications.any { it.sutra == "3.4.101" })
        assertEquals("भवेताम्", result.final.surface)
    }

    @Test
    fun `liṅ second singular derives bhaveh`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "भू" }
        val result = DerivationEngine().derive(
            TingantaDerivationRequest("भू", purusha = Purusha.MADHYAMA, vacana = Vacana.EKAVACANA, lakara = Lakara.LING)
                .initialState(dhatu),
        )

        assertEquals("भवेः", result.final.surface)
    }

    @Test
    fun `liṅ remaining parasmaipada endings derive their expected forms`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "भू" }
        val expected = listOf(
            Purusha.MADHYAMA to Vacana.DVIVACANA to "भवेतम्",
            Purusha.MADHYAMA to Vacana.BAHUVACANA to "भवेत",
            Purusha.UTTAMA to Vacana.EKAVACANA to "भवेयम्",
            Purusha.UTTAMA to Vacana.DVIVACANA to "भवेव",
            Purusha.UTTAMA to Vacana.BAHUVACANA to "भवेम",
        )

        expected.forEach { (personAndNumber, form) ->
            val (purusha, vacana) = personAndNumber
            val result = DerivationEngine().derive(
                TingantaDerivationRequest("भू", purusha = purusha, vacana = vacana, lakara = Lakara.LING).initialState(dhatu),
            )
            assertEquals(form, result.final.surface)
        }
    }

    @Test
    fun `conjugation engine derives complete vidhi ling paradigm for bhu`() {
        val forms = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LING).surfaces

        assertEquals("भवेत्", forms[TingAffix.TIP])
        assertEquals("भवेताम्", forms[TingAffix.TAS])
        assertEquals("भवेयुः", forms[TingAffix.JHI])
        assertEquals("भवेः", forms[TingAffix.SIP])
        assertEquals("भवेतम्", forms[TingAffix.THAS])
        assertEquals("भवेत", forms[TingAffix.THA])
        assertEquals("भवेयम्", forms[TingAffix.MIP])
        assertEquals("भवेव", forms[TingAffix.VAS])
        assertEquals("भवेम", forms[TingAffix.MAS])
    }

    @Test
    fun `atmanepada ling selects siyut for labh`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "डुलभँष्" }
        val result = DerivationEngine().derive(TingantaDerivationRequest("लभ्", lakara = Lakara.LING).initialState(dhatu))

        assertTrue(result.applications.any { it.sutra == "3.4.102" })
        assertTrue(result.applications.any { it.sutra == "7.2.79" })
        assertEquals("लभेत", result.final.surface)
    }

    @Test
    fun `atmanepada ling third dual derives labheyatam`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "डुलभँष्" }
        val result = DerivationEngine().derive(
            TingantaDerivationRequest("लभ्", purusha = Purusha.PRATHAMA, vacana = Vacana.DVIVACANA, lakara = Lakara.LING)
                .initialState(dhatu),
        )

        assertEquals("लभेयाताम्", result.final.surface)
    }

    @Test
    fun `atmanepada ling third plural derives labheran`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "डुलभँष्" }
        val result = DerivationEngine().derive(
            TingantaDerivationRequest("लभ्", purusha = Purusha.PRATHAMA, vacana = Vacana.BAHUVACANA, lakara = Lakara.LING)
                .initialState(dhatu),
        )

        assertTrue(result.applications.any { it.sutra == "3.4.105" })
        assertEquals("लभेरन्", result.final.surface)
    }

    @Test
    fun `atmanepada ling first singular derives labheya`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "डुलभँष्" }
        val result = DerivationEngine().derive(
            TingantaDerivationRequest("लभ्", purusha = Purusha.UTTAMA, vacana = Vacana.EKAVACANA, lakara = Lakara.LING)
                .initialState(dhatu),
        )

        assertTrue(result.applications.any { it.sutra == "3.4.106" })
        assertEquals("लभेय", result.final.surface)
    }

    @Test
    fun `atmanepada ling second singular derives labhethah`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "डुलभँष्" }
        val result = DerivationEngine().derive(
            TingantaDerivationRequest("लभ्", purusha = Purusha.MADHYAMA, vacana = Vacana.EKAVACANA, lakara = Lakara.LING)
                .initialState(dhatu),
        )

        assertTrue(result.applications.any { it.sutra == "1.3.4" }, result.applications.joinToString { it.sutra })
        assertTrue(result.applications.any { it.sutra == "8.2.66" }, result.applications.joinToString { it.sutra })
        assertTrue(result.applications.any { it.sutra == "8.3.15" }, result.applications.joinToString { it.sutra })
        assertEquals("लभेथाः", result.final.surface)
    }

    @Test
    fun `atmanepada ling remaining endings derive complete forms`() {
        val dhatu = dev.sanskrit.dhatupatha.DhatuPatha.all.first { it.upadesha == "डुलभँष्" }
        val engine = DerivationEngine()
        val expectedForms = mapOf(
            Purusha.MADHYAMA to Vacana.DVIVACANA to "लभेयाथाम्",
            Purusha.MADHYAMA to Vacana.BAHUVACANA to "लभेध्वम्",
            Purusha.UTTAMA to Vacana.DVIVACANA to "लभेवहि",
            Purusha.UTTAMA to Vacana.BAHUVACANA to "लभेमहि",
        )

        expectedForms.forEach { (personAndNumber, expectedSurface) ->
            val (purusha, vacana) = personAndNumber
            val result = engine.derive(
                TingantaDerivationRequest("लभ्", purusha = purusha, vacana = vacana, lakara = Lakara.LING)
                    .initialState(dhatu),
            )

            assertEquals(expectedSurface, result.final.surface, "$purusha $vacana")
        }
    }

    @Test
    fun `conjugation engine derives complete atmanepada vidhi ling paradigm for labh`() {
        val forms = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LING).surfaces

        assertEquals(
            mapOf(
                TingAffix.TA to "लभेत",
                TingAffix.ATAM to "लभेयाताम्",
                TingAffix.JHA to "लभेरन्",
                TingAffix.THAS_A to "लभेथाः",
                TingAffix.ATHAM to "लभेयाथाम्",
                TingAffix.DHVAM to "लभेध्वम्",
                TingAffix.IT to "लभेय",
                TingAffix.VAHI to "लभेवहि",
                TingAffix.MAHING to "लभेमहि",
            ),
            forms,
        )
    }

    @Test
    fun `conjugation engine derives complete atmanepada present paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LAT)
        val forms = paradigm.surfaces

        assertEquals(
            mapOf(
                TingAffix.TA to "लभते",
                TingAffix.ATAM to "लभेते",
                TingAffix.JHA to "लभन्ते",
                TingAffix.THAS_A to "लभसे",
                TingAffix.ATHAM to "लभेथे",
                TingAffix.DHVAM to "लभध्वे",
                TingAffix.IT to "लभे",
                TingAffix.VAHI to "लभावहे",
                TingAffix.MAHING to "लभामहे",
            ),
            forms,
        )
    }

    @Test
    fun `conjugation engine derives complete conditional paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LRNG)
        val forms = paradigm.surfaces

        assertEquals(9, forms.size)
        assertEquals("अभविष्यत्", forms[TingAffix.TIP])
        assertEquals("अभविष्यताम्", forms[TingAffix.TAS])
        assertEquals("अभविष्यन्", forms[TingAffix.JHI])
        assertEquals("अभविष्यः", forms[TingAffix.SIP])
        assertEquals("अभविष्यतम्", forms[TingAffix.THAS])
        assertEquals("अभविष्यत", forms[TingAffix.THA])
        assertEquals("अभविष्यम्", forms[TingAffix.MIP])
        assertEquals("अभविष्याव", forms[TingAffix.VAS])
        assertEquals("अभविष्याम", forms[TingAffix.MAS])
    }

    @Test
    fun `conjugation engine derives complete perfect paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LIT)
        val forms = paradigm.surfaces

        assertEquals(9, forms.size)
        assertEquals("बभूव", forms[TingAffix.TIP])
        assertEquals("बभूवतुः", forms[TingAffix.TAS])
        assertEquals("बभूवुः", forms[TingAffix.JHI])
        assertEquals("बभूविथ", forms[TingAffix.SIP])
        assertEquals("बभूवथुः", forms[TingAffix.THAS])
        assertEquals("बभूव", forms[TingAffix.THA])
        assertEquals("बभूव", forms[TingAffix.MIP])
        assertEquals("बभूविव", forms[TingAffix.VAS])
        assertEquals("बभूविम", forms[TingAffix.MAS])
    }

    @Test
    fun `conjugation engine derives complete periphrastic future paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LUT)
        val forms = paradigm.surfaces

        assertEquals(9, forms.size)
        assertEquals("भविता", forms[TingAffix.TIP])
        assertEquals("भवितारौ", forms[TingAffix.TAS])
        assertEquals("भवितारः", forms[TingAffix.JHI])
        assertEquals("भवितासि", forms[TingAffix.SIP])
        assertEquals("भवितास्थः", forms[TingAffix.THAS])
        assertEquals("भवितास्थ", forms[TingAffix.THA])
        assertEquals("भवितास्मि", forms[TingAffix.MIP])
        assertEquals("भवितास्वः", forms[TingAffix.VAS])
        assertEquals("भवितास्मः", forms[TingAffix.MAS])

        paradigm.forms.values.forEach { result ->
            assertTrue(result.applications.any { it.sutra == "3.3.15" })
            assertTrue(result.applications.any { it.sutra == "3.1.33" })
        }
        assertTrue(paradigm.forms.getValue(TingAffix.TIP).applications.any { it.sutra == "6.4.143" })
        listOf(TingAffix.TAS, TingAffix.JHI, TingAffix.SIP).forEach { affix ->
            assertTrue(paradigm.forms.getValue(affix).applications.any { it.sutra == "7.4.50" })
        }
    }

    @Test
    fun `conjugation engine derives complete root aorist paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LUNG)
        val forms = paradigm.surfaces

        assertEquals(
            mapOf(
                TingAffix.TIP to "अभूत्",
                TingAffix.TAS to "अभूताम्",
                TingAffix.JHI to "अभूवन्",
                TingAffix.SIP to "अभूः",
                TingAffix.THAS to "अभूतम्",
                TingAffix.THA to "अभूत",
                TingAffix.MIP to "अभूवम्",
                TingAffix.VAS to "अभूव",
                TingAffix.MAS to "अभूम",
            ),
            forms,
        )

        paradigm.forms.values.forEach { result ->
            val applied = result.applications.mapTo(mutableSetOf()) { it.sutra }
            assertTrue(applied.containsAll(setOf("3.2.110", "3.1.43", "3.1.44", "2.4.77", "3.4.78")))
        }
    }
}

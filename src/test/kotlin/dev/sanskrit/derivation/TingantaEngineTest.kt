package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import dev.sanskrit.dhatupatha.DhatuPatha
import dev.sanskrit.dhatupatha.Gana
import dev.sanskrit.dhatupatha.PadaType

class TingantaEngineTest {

    @Test
    fun `conjugation engine derives complete Kryadi imperative paradigms for kri`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("डुक्रीञ्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LOT)
        val atmanepada = engine.deriveSupportedParadigm("डुक्रीञ्", pada = PadaType.ATMANEPADA, lakara = Lakara.LOT)

        parasmaipada.assertSurfaces("क्रीणातु क्रीणीताम् क्रीणन्तु क्रीणीहि क्रीणीतम् क्रीणीत क्रीणानि क्रीणाव क्रीणाम")
        atmanepada.assertSurfaces("क्रीणीताम् क्रीणाताम् क्रीणताम् क्रीणीष्व क्रीणाथाम् क्रीणीध्वम् क्रीणै क्रीणावहै क्रीणामहै")

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
        }
    }

    @Test
    fun `conjugation engine derives complete Kryadi present paradigms for kri`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("डुक्रीञ्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("डुक्रीञ्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        parasmaipada.assertSurfaces("क्रीणाति क्रीणीतः क्रीणन्ति क्रीणासि क्रीणीथः क्रीणीथ क्रीणामि क्रीणीवः क्रीणीमः")
        atmanepada.assertSurfaces("क्रीणीते क्रीणाते क्रीणते क्रीणीषे क्रीणाथे क्रीणीध्वे क्रीणे क्रीणीवहे क्रीणीमहे")

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श्ना" })
        }
    }

    @Test
    fun `conjugation engine derives complete Rudhadi present paradigms for rudh`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("रुधिँर्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("रुधिँर्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        parasmaipada.assertSurfaces("रुणद्धि रुन्द्धः रुन्धन्ति रुणत्सि रुन्द्धः रुन्द्ध रुणध्मि रुन्ध्वः रुन्ध्मः")
        atmanepada.assertSurfaces("रुन्द्धे रुन्धाते रुन्धते रुन्त्से रुन्धाथे रुन्द्ध्वे रुन्धे रुन्ध्वहे रुन्ध्महे")

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.droppedTerms.any { it.upadesha == "श्नम्" })
        }
    }

    @Test
    fun `conjugation engine derives complete Juhotyadi present paradigm for hu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("हु", lakara = Lakara.LAT)

        paradigm.assertSurfaces("जुहोति जुहुतः जुह्वति जुहोषि जुहुथः जुहुथ जुहोमि जुहुवः जुहुमः")

        paradigm.forms.values.forEach { result ->
            assertTrue(result.final.droppedTerms.any { it.upadesha == "शप्" && it.deletionType == LopaType.SHLU })
        }
    }

    @Test
    fun `conjugation engine derives complete Curadi present paradigms for cur`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("चुरँ", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("चुरँ", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        parasmaipada.assertSurfaces("चोरयति चोरयतः चोरयन्ति चोरयसि चोरयथः चोरयथ चोरयामि चोरयावः चोरयामः")
        atmanepada.assertSurfaces("चोरयते चोरयेते चोरयन्ते चोरयसे चोरयेथे चोरयध्वे चोरये चोरयावहे चोरयामहे")

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.any { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "णिच्" })
        }
    }

    @Test
    fun `conjugation engine derives complete Tanadi present paradigms for tan`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("तनुँ", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("तनुँ", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        parasmaipada.assertSurfaces("तनोति तनुतः तन्वन्ति तनोसि तनुथः तनुथ तनोमि तनुवः तनुमः")
        atmanepada.assertSurfaces("तनुते तन्वाते तन्वते तनुषे तन्वाथे तनुध्वे तन्वे तनुवहे तनुमहे")

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.id == "tanadi-u" && it.upadesha == "उ" })
        }
    }

    @Test
    fun `conjugation engine derives complete Svadi present paradigms for su`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("षुञ्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("षुञ्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        parasmaipada.assertSurfaces("सुनोति सुनुतः सुन्वन्ति सुनोषि सुनुथः सुनुथ सुनोमि सुनुवः सुनुमः")
        atmanepada.assertSurfaces("सुनुते सुन्वाते सुन्वते सुनुषे सुन्वाथे सुनुध्वे सुन्वे सुनुवहे सुनुमहे")

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श्नु" })
        }
    }

    @Test
    fun `conjugation engine derives complete Tudadi present paradigms for tud`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("तुद्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("तुद्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        parasmaipada.assertSurfaces("तुदति तुदतः तुदन्ति तुदसि तुदथः तुदथ तुदामि तुदावः तुदामः")
        atmanepada.assertSurfaces("तुदते तुदेते तुदन्ते तुदसे तुदेथे तुदध्वे तुदे तुदावहे तुदामहे")

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श" })
        }
    }

    @Test
    fun `Tudadi imperative derives both complete padas`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("तुद्", PadaType.PARASMAIPADA, Lakara.LOT)
        val atmanepada = engine.deriveSupportedParadigm("तुद्", PadaType.ATMANEPADA, Lakara.LOT)

        parasmaipada.assertSurfaces("तुदतु तुदताम् तुदन्तु तुद तुदतम् तुदत तुदानि तुदाव तुदाम")
        atmanepada.assertSurfaces("तुदताम् तुदेताम् तुदन्ताम् तुदस्व तुदेथाम् तुदध्वम् तुदै तुदावहै तुदामहै")
        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
        }
    }

    @Test
    fun `all ganas derive every LOT LANG and LING slot`() {
        val roots = mapOf(
            Gana.BHVADI to "भू",
            Gana.ADADI to "अद्",
            Gana.JUHOTYADI to "हु",
            Gana.DIVADI to "दिव्",
            Gana.SVADI to "षुञ्",
            Gana.TUDADI to "नुद्",
            Gana.RUDHADI to "रुधिँर्",
            Gana.TANADI to "तनुँ",
            Gana.KRYADI to "डुक्रीञ्",
            Gana.CURADI to "चुरँ",
        )

        roots.forEach { (gana, root) ->
            val dhatu = DhatuPatha.all.first { it.gana == gana && (it.upadesha == root || it.derivationalSurface == root) }
            val padas = when (dhatu.pada) {
                PadaType.UBHAYAPADA -> listOf(PadaType.PARASMAIPADA, PadaType.ATMANEPADA)
                else -> listOf(requireNotNull(dhatu.pada))
            }
            padas.forEach { pada ->
                listOf(Lakara.LOT, Lakara.LANG, Lakara.LING).forEach { lakara ->
                    val paradigm = try {
                        TingantaEngine().deriveSupportedParadigm(root, pada, lakara)
                    } catch (error: IllegalArgumentException) {
                        throw AssertionError("$gana $lakara $pada: ${error.message}", error)
                    }
                    assertEquals(9, paradigm.forms.size, "$gana $lakara $pada")
                }
            }
        }
    }

    @Test
    fun `Curadi atmanepada optative contracts late shap plus siyut`() {
        val result = TingantaEngine().derive(
            TingantaDerivationRequest(
                "चुरँ", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LING,
                pada = PadaType.ATMANEPADA,
            ),
        )

        assertEquals("चोरयेत", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "6.1.66" })
    }

    @Test
    fun `conjugation engine derives complete Divadi present paradigm for div`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("दिव्", lakara = Lakara.LAT)

        paradigm.assertSurfaces("दिव्यति दिव्यतः दिव्यन्ति दिव्यसि दिव्यथः दिव्यथ दिव्यामि दिव्यावः दिव्यामः")

        paradigm.forms.values.forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श्यन्" })
        }
    }

    @Test
    fun `conjugation engine derives complete Adadi present paradigm for ad`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("अद्", lakara = Lakara.LAT)

        paradigm.assertSurfaces("अत्ति अत्तः अदन्ति अत्सि अत्थः अत्थ अद्मि अद्वः अद्मः")

        paradigm.forms.values.forEach { result ->
            assertTrue(result.final.droppedTerms.any { it.upadesha == "शप्" && it.deletionType == LopaType.LUK })
        }
        listOf(TingAffix.TIP, TingAffix.TAS, TingAffix.SIP, TingAffix.THAS, TingAffix.THA).forEach { affix ->
            assertTrue(paradigm.forms.getValue(affix).applications.any { it.sutra == "8.4.55" })
        }
    }

    @Test
    fun `ubhayapada roots derive in either explicitly requested pada`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("स्रम्भ्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("स्रम्भ्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        assertEquals(PadaType.PARASMAIPADA, parasmaipada.pada)
        assertEquals(PadaType.ATMANEPADA, atmanepada.pada)
        assertEquals(TingAffix.entries.filter { it.pada == PadaType.PARASMAIPADA }.toSet(), parasmaipada.forms.keys)
        assertEquals(TingAffix.entries.filter { it.pada == PadaType.ATMANEPADA }.toSet(), atmanepada.forms.keys)
        assertEquals("स्रम्भति", parasmaipada.surfaces[TingAffix.TIP])
        assertEquals("स्रम्भते", atmanepada.surfaces[TingAffix.TA])
    }

    @Test
    fun `ubhayapada roots derive both padas across every supported lakara`() {
        val engine = TingantaEngine()

        Lakara.entries.forEach { lakara ->
            listOf(PadaType.PARASMAIPADA, PadaType.ATMANEPADA).forEach { pada ->
                val paradigm = engine.deriveSupportedParadigm("स्रम्भ्", pada = pada, lakara = lakara)
                val expectedAffixes = TingAffix.entries.filter { it.pada == pada }.toSet()

                assertEquals(expectedAffixes, paradigm.forms.keys, "$lakara $pada")
                assertEquals(9, paradigm.surfaces.size, "$lakara $pada")
            }
        }
    }

    @Test
    fun `tinganta requests reject a pada unavailable to the root`() {
        val engine = TingantaEngine()

        assertFailsWith<IllegalArgumentException> {
            engine.derive(TingantaDerivationRequest("भू", pada = PadaType.ATMANEPADA))
        }
        assertFailsWith<IllegalArgumentException> {
            engine.derive(TingantaDerivationRequest("एध्", pada = PadaType.PARASMAIPADA))
        }
    }

    @Test
    fun `conjugation engine derives complete paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LAT)
        paradigm.assertSurfaces("भवति भवतः भवन्ति भवसि भवथः भवथ भवामि भवावः भवामः")
        
        paradigm.coverage.forEach { row ->
            assertTrue("3.4.78" in row.appliedSutras, "Form for ${row.affix} is missing 3.4.78")
            assertEquals("derived", row.note)
        }
    }

    @Test
    fun `conjugation engine derives complete future paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LRT)
        paradigm.assertSurfaces("भविष्यति भविष्यतः भविष्यन्ति भविष्यसि भविष्यथः भविष्यथ भविष्यामि भविष्यावः भविष्यामः")
        
        paradigm.coverage.forEach { row ->
            assertTrue("3.1.33" in row.appliedSutras, "Form for ${row.affix} is missing 3.1.33")
            assertTrue("8.3.59" in row.appliedSutras, "Form for ${row.affix} is missing 8.3.59")
        }
    }

    @Test
    fun `conjugation engine derives complete atmanepada future paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LRT)

        paradigm.assertSurfaces("लप्स्यते लप्स्येते लप्स्यन्ते लप्स्यसे लप्स्येथे लप्स्यध्वे लप्स्ये लप्स्यावहे लप्स्यामहे")

        paradigm.forms.values.forEach { result ->
            assertTrue(result.applications.any { it.sutra == "8.4.55" })
        }
    }

    @Test
    fun `conjugation engine derives complete imperfect past paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LANG)
        paradigm.assertSurfaces("अभवत् अभवताम् अभवन् अभवः अभवतम् अभवत अभवम् अभवाव अभवाम")
        
        paradigm.coverage.forEach { row ->
            assertTrue("3.2.111" in row.appliedSutras, "Form for ${row.affix} is missing 3.2.111")
            assertTrue("6.4.71" in row.appliedSutras, "Form for ${row.affix} is missing 6.4.71")
        }
    }

    @Test
    fun `conjugation engine derives complete atmanepada imperfect paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LANG)

        paradigm.assertSurfaces("अलभत अलभेताम् अलभन्त अलभथाः अलभेथाम् अलभध्वम् अलभे अलभावहि अलभामहि")

    }

    @Test
    fun `conjugation engine derives complete imperative paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LOT)
        paradigm.assertSurfaces("भवतु भवताम् भवन्तु भव भवतम् भवत भवानि भवाव भवाम")

    }

    @Test
    fun `conjugation engine derives complete atmanepada imperative paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LOT)

        paradigm.assertSurfaces("लभताम् लभेताम् लभन्ताम् लभस्व लभेथाम् लभध्वम् लभै लभावहै लभामहै")

    }

    @Test
    fun `conjugation engine rejects unknown dhatu`() {
        assertFailsWith<IllegalArgumentException> {
            TingantaEngine().derive(TingantaDerivationRequest("unknown_dhatu"))
        }
    }

    @Test
    fun `conjugation engine derives complete vidhi ling paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LING)
        paradigm.assertSurfaces("भवेत् भवेताम् भवेयुः भवेः भवेतम् भवेत भवेयम् भवेव भवेम")
        assertTrue(paradigm.forms.getValue(TingAffix.TAS).applications.any { it.sutra == "3.4.101" })
        assertTrue(paradigm.forms.getValue(TingAffix.JHI).applications.any { it.sutra == "3.4.108" })

        val result = paradigm.forms.getValue(TingAffix.TIP)
        val applied = result.applications.map { it.sutra }
        assertTrue(applied.containsAll(setOf("3.3.161", "3.4.103", "7.2.80", "7.3.84")))
        assertTrue("7.2.79" !in applied)
        val termsAfterShap = result.applications.first { it.sutra == "3.1.68" }.after.terms
        assertTrue(termsAfterShap.indexOfFirst { it.id == "shap" } < termsAfterShap.indexOfFirst { it.id == "yasut" })
        assertTrue(applied.indexOf("3.3.161") < applied.indexOf("3.4.78"))
        assertTrue(applied.indexOf("3.4.78") < applied.indexOf("3.4.103"))
        assertTrue(applied.indexOf("3.4.103") < applied.indexOf("7.2.80"))
    }

    @Test
    fun `conjugation engine derives complete atmanepada vidhi ling paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LING)
        paradigm.assertSurfaces("लभेत लभेयाताम् लभेरन् लभेथाः लभेयाथाम् लभेध्वम् लभेय लभेवहि लभेमहि")
        assertTrue(paradigm.forms.getValue(TingAffix.TA).applications.map { it.sutra }.containsAll(setOf("3.4.102", "7.2.79")))
        assertTrue(paradigm.forms.getValue(TingAffix.JHA).applications.any { it.sutra == "3.4.105" })
        assertTrue(paradigm.forms.getValue(TingAffix.IT).applications.any { it.sutra == "3.4.106" })
        assertTrue(paradigm.forms.getValue(TingAffix.THAS_A).applications.map { it.sutra }.containsAll(setOf("1.3.4", "8.2.66", "8.3.15")))
    }

    @Test
    fun `conjugation engine derives complete atmanepada present paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LAT)
        paradigm.assertSurfaces("लभते लभेते लभन्ते लभसे लभेथे लभध्वे लभे लभावहे लभामहे")
    }

    @Test
    fun `conjugation engine derives complete conditional paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LRNG)
        paradigm.assertSurfaces("अभविष्यत् अभविष्यताम् अभविष्यन् अभविष्यः अभविष्यतम् अभविष्यत अभविष्यम् अभविष्याव अभविष्याम")
    }

    @Test
    fun `conjugation engine derives complete atmanepada conditional paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LRNG)

        paradigm.assertSurfaces("अलप्स्यत अलप्स्येताम् अलप्स्यन्त अलप्स्यथाः अलप्स्येथाम् अलप्स्यध्वम् अलप्स्ये अलप्स्यावहि अलप्स्यामहि")

        paradigm.forms.values.forEach { result ->
            assertTrue(result.applications.any { it.sutra == "8.4.55" })
        }
    }

    @Test
    fun `conjugation engine derives complete perfect paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LIT)
        paradigm.assertSurfaces("बभूव बभूवतुः बभूवुः बभूविथ बभूवथुः बभूव बभूव बभूविव बभूविम")

        val required = setOf("3.2.115", "6.1.8", "6.4.88", "7.4.59", "7.4.73", "8.4.54")
        listOf(TingAffix.TIP, TingAffix.JHI).forEach { affix ->
            val applied = paradigm.forms.getValue(affix).applications.map { it.sutra }.toSet()
            assertTrue(applied.containsAll(required), "$affix is missing ${required - applied}")
        }
    }

    @Test
    fun `conjugation engine derives complete atmanepada perfect paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LIT)

        paradigm.assertSurfaces("लेभे लेभाते लेभिरे लेभिषे लेभाथे लेभिध्वे लेभे लेभिवहे लेभिमहे")

        paradigm.forms.values.forEach { result ->
            assertTrue(result.applications.any { it.sutra == "6.4.120" })
        }
    }

    @Test
    fun `conjugation engine derives complete periphrastic future paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LUT)
        paradigm.assertSurfaces("भविता भवितारौ भवितारः भवितासि भवितास्थः भवितास्थ भवितास्मि भवितास्वः भवितास्मः")

        assertTrue(paradigm.forms.getValue(TingAffix.TIP).applications.any { it.sutra == "6.4.143" })
        listOf(TingAffix.TAS, TingAffix.JHI, TingAffix.SIP).forEach { affix ->
            assertTrue(paradigm.forms.getValue(affix).applications.any { it.sutra == "7.4.50" })
        }
    }

    @Test
    fun `conjugation engine derives complete atmanepada periphrastic future paradigm for edh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("एध्", lakara = Lakara.LUT)

        paradigm.assertSurfaces("एधिता एधितारौ एधितारः एधितासे एधितासाथे एधिताध्वे एधिताहे एधितास्वहे एधितास्महे")

    }

    @Test
    fun `conjugation engine derives complete root aorist paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LUNG)
        paradigm.assertSurfaces("अभूत् अभूताम् अभूवन् अभूः अभूतम् अभूत अभूवम् अभूव अभूम")

        paradigm.forms.values.forEach { result ->
            assertTrue(result.applications.any { it.sutra == "2.4.77" })
        }
    }

    @Test
    fun `conjugation engine derives complete atmanepada ish aorist paradigm for labh`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("लभ्", lakara = Lakara.LUNG)

        paradigm.assertSurfaces("अलभिष्ट अलभिषाताम् अलभिषत अलभिष्ठाः अलभिषाथाम् अलभिढ्वम् अलभिषि अलभिष्वहि अलभिष्महि")

    }

    @Test
    fun `conjugation engine derives complete Vedic subjunctive paradigm for bhu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LET)

        paradigm.assertSurfaces("भवात् भवातः भवान् भवाः भवाथः भवाथ भवानि भवाव भवाम")

    }

    @Test
    fun `LET exposes the alternative at augment branch`() {
        val result = TingantaEngine().derive(
            TingantaDerivationRequest(
                "भू", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LET,
                letAugment = LetAugment.AT,
            )
        )

        assertEquals("भवत्", result.final.surface)
        assertTrue(result.applications.any { it.sutra == "3.4.94" && "अट्" in it.explanation })
    }

    @Test
    fun `LET derives atmanepada ai forms under 3 4 95 and 96`() {
        val obligatoryDual = TingantaEngine().derive(
            TingantaDerivationRequest(
                "लभ्", Vacana.DVIVACANA, Purusha.PRATHAMA, Lakara.LET,
                letAugment = LetAugment.AT,
            )
        )
        val optionalSingular = TingantaEngine().derive(
            TingantaDerivationRequest(
                "लभ्", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LET,
                letEOption = LetEOption.AI,
            )
        )

        assertEquals("लभैते", obligatoryDual.final.surface)
        assertTrue(obligatoryDual.applications.any { it.sutra == "3.4.95" })
        assertEquals("लभातै", optionalSingular.final.surface)
        assertTrue(optionalSingular.applications.any { it.sutra == "3.4.96" })
    }

    @Test
    fun `LET inserts sip for the attested aorist subjunctive of tr`() {
        val result = TingantaEngine().derive(
            TingantaDerivationRequest(
                "तॄ", Vacana.EKAVACANA, Purusha.PRATHAMA, Lakara.LET,
                letAugment = LetAugment.AT,
                letFormation = LetFormation.SIP_AORIST,
            )
        )

        assertEquals("तारिषत्", result.final.surface)
        assertTrue(result.applications.map { it.sutra }.containsAll(setOf("3.1.34", "3.4.94", "7.2.35", "8.3.59")))
    }

    private fun TingantaParadigm.assertSurfaces(expected: String) {
        val affixes = TingAffix.entries.filter { it.pada == pada }
        val expectedSurfaces = expected.trim().split(Regex("\\s+"))
        assertEquals(affixes.size, expectedSurfaces.size, "Expected one surface for each $pada slot")
        assertEquals(affixes.zip(expectedSurfaces).toMap(), surfaces)
    }

}

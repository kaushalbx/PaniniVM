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
    fun `Kryadi imperative uses shna without shap`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("डुक्रीञ्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LOT)
        val atmanepada = engine.deriveSupportedParadigm("डुक्रीञ्", pada = PadaType.ATMANEPADA, lakara = Lakara.LOT)

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श्ना" })
        }
    }

    @Test
    fun `Kryadi present uses shna without shap`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("डुक्रीञ्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("डुक्रीञ्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श्ना" })
        }
    }

    @Test
    fun `Rudhadi present uses shnam without shap`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("रुधिँर्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("रुधिँर्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.droppedTerms.any { it.upadesha == "श्नम्" })
        }
    }

    @Test
    fun `Juhotyadi present uses shap shlu`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("हु", lakara = Lakara.LAT)

        paradigm.forms.values.forEach { result ->
            assertTrue(result.final.droppedTerms.any { it.upadesha == "शप्" && it.deletionType == LopaType.SHLU })
        }
    }

    @Test
    fun `Curadi present uses nic and shap`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("चुरँ", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("चुरँ", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.any { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "णिच्" })
        }
    }

    @Test
    fun `Tanadi present uses u without shap`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("तनुँ", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("तनुँ", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.id == "tanadi-u" && it.upadesha == "उ" })
        }
    }

    @Test
    fun `Svadi present uses shnu without shap`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("षुञ्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("षुञ्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श्नु" })
        }
    }

    @Test
    fun `Tudadi present uses sha without shap`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("तुद्", pada = PadaType.PARASMAIPADA, lakara = Lakara.LAT)
        val atmanepada = engine.deriveSupportedParadigm("तुद्", pada = PadaType.ATMANEPADA, lakara = Lakara.LAT)

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श" })
        }
    }

    @Test
    fun `Tudadi imperative uses sha without shap`() {
        val engine = TingantaEngine()
        val parasmaipada = engine.deriveSupportedParadigm("तुद्", PadaType.PARASMAIPADA, Lakara.LOT)
        val atmanepada = engine.deriveSupportedParadigm("तुद्", PadaType.ATMANEPADA, Lakara.LOT)

        (parasmaipada.forms.values + atmanepada.forms.values).forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श" })
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
    fun `all ganas derive complete representative parasmaipada presents`() {
        val expected = mapOf(
            "भू" to "भवति भवतः भवन्ति भवसि भवथः भवथ भवामि भवावः भवामः",
            "द्विषँ" to "द्वेष्टि द्विष्टः द्विषन्ति द्वेक्षि द्विष्ठः द्विष्ठ द्वेष्मि द्विष्वः द्विष्मः",
            "हु" to "जुहोति जुहुतः जुह्वति जुहोषि जुहुथः जुहुथ जुहोमि जुहुवः जुहुमः",
            "दिव्" to "दीव्यति दीव्यतः दीव्यन्ति दीव्यसि दीव्यथः दीव्यथ दीव्यामि दीव्यावः दीव्यामः",
            "षुञ्" to "सुनोति सुनुतः सुन्वन्ति सुनोषि सुनुथः सुनुथ सुनोमि सुनुवः सुनुमः",
            "नुद्" to "नुदति नुदतः नुदन्ति नुदसि नुदथः नुदथ नुदामि नुदावः नुदामः",
            "रुधिँर्" to "रुणद्धि रुन्द्धः रुन्धन्ति रुणत्सि रुन्द्धः रुन्द्ध रुणध्मि रुन्ध्वः रुन्ध्मः",
            "तनुँ" to "तनोति तनुतः तन्वन्ति तनोसि तनुथः तनुथ तनोमि तनुवः तनुमः",
            "डुक्रीञ्" to "क्रीणाति क्रीणीतः क्रीणन्ति क्रीणासि क्रीणीथः क्रीणीथ क्रीणामि क्रीणीवः क्रीणीमः",
            "चुरँ" to "चोरयति चोरयतः चोरयन्ति चोरयसि चोरयथः चोरयथ चोरयामि चोरयावः चोरयामः",
        )

        expected.forEach { (root, surfaces) ->
            TingantaEngine()
                .deriveSupportedParadigm(root, PadaType.PARASMAIPADA, Lakara.LAT)
                .assertSurfaces(surfaces)
        }
    }

    @Test
    fun `all ganas derive complete representative atmanepada presents`() {
        val expected = mapOf(
            "एधँ" to "एधते एधेते एधन्ते एधसे एधेथे एधध्वे एधे एधावहे एधामहे",
            "द्विषँ" to "द्विष्टे द्विषाते द्विषते द्विक्षे द्विषाथे द्विड्ढ्वे द्विषे द्विष्वहे द्विष्महे",
            "डुभृञ्" to "बिभृते बिभ्राते बिभ्रते बिभृषे बिभ्राथे बिभृध्वे बिभ्रे बिभृवहे बिभृमहे",
            "दूङ्" to "दूयते दूयेते दूयन्ते दूयसे दूयेथे दूयध्वे दूये दूयावहे दूयामहे",
            "षुञ्" to "सुनुते सुन्वाते सुन्वते सुनुषे सुन्वाथे सुनुध्वे सुन्वे सुनुवहे सुनुमहे",
            "तुदँ" to "तुदते तुदेते तुदन्ते तुदसे तुदेथे तुदध्वे तुदे तुदावहे तुदामहे",
            "रुधिँर्" to "रुन्द्धे रुन्धाते रुन्धते रुन्त्से रुन्धाथे रुन्द्ध्वे रुन्धे रुन्ध्वहे रुन्ध्महे",
            "तनुँ" to "तनुते तन्वाते तन्वते तनुषे तन्वाथे तनुध्वे तन्वे तनुवहे तनुमहे",
            "डुक्रीञ्" to "क्रीणीते क्रीणाते क्रीणते क्रीणीषे क्रीणाथे क्रीणीध्वे क्रीणे क्रीणीवहे क्रीणीमहे",
            "चुरँ" to "चोरयते चोरयेते चोरयन्ते चोरयसे चोरयेथे चोरयध्वे चोरये चोरयावहे चोरयामहे",
        )

        expected.forEach { (root, surfaces) ->
            TingantaEngine()
                .deriveSupportedParadigm(root, PadaType.ATMANEPADA, Lakara.LAT)
                .assertSurfaces(surfaces)
        }
    }

    @Test
    fun `LAT handles vowel consonant irregular and ubhayapada roots`() {
        val expected = listOf(
            Triple("णीञ्", PadaType.PARASMAIPADA, "नयति नयतः नयन्ति नयसि नयथः नयथ नयामि नयावः नयामः"),
            Triple("णीञ्", PadaType.ATMANEPADA, "नयते नयेते नयन्ते नयसे नयेथे नयध्वे नये नयावहे नयामहे"),
            Triple("डुपचँष्", PadaType.PARASMAIPADA, "पचति पचतः पचन्ति पचसि पचथः पचथ पचामि पचावः पचामः"),
            Triple("डुपचँष्", PadaType.ATMANEPADA, "पचते पचेते पचन्ते पचसे पचेथे पचध्वे पचे पचावहे पचामहे"),
            Triple("गमॢँ", PadaType.PARASMAIPADA, "गच्छति गच्छतः गच्छन्ति गच्छसि गच्छथः गच्छथ गच्छामि गच्छावः गच्छामः"),
            Triple("स्रन्भुँ", PadaType.PARASMAIPADA, "स्रम्भति स्रम्भतः स्रम्भन्ति स्रम्भसि स्रम्भथः स्रम्भथ स्रम्भामि स्रम्भावः स्रम्भामः"),
            Triple("स्रन्भुँ", PadaType.ATMANEPADA, "स्रम्भते स्रम्भेते स्रम्भन्ते स्रम्भसे स्रम्भेथे स्रम्भध्वे स्रम्भे स्रम्भावहे स्रम्भामहे"),
        )

        expected.forEach { (root, pada, surfaces) ->
            TingantaEngine().deriveSupportedParadigm(root, pada, Lakara.LAT).assertSurfaces(surfaces)
        }

        TingantaEngine().deriveSupportedParadigm("गमॢँ", lakara = Lakara.LAT).forms.values.forEach { result ->
            assertTrue(result.applications.map { it.sutra }.containsAll(setOf("7.3.77", "6.1.73", "8.4.40")))
        }
    }

    @Test
    fun `all ganas derive complete representative parasmaipada imperfects`() {
        val expected = mapOf(
            "भू" to "अभवत् अभवताम् अभवन् अभवः अभवतम् अभवत अभवम् अभवाव अभवाम",
            "द्विषँ" to "अद्वेट् अद्विष्टाम् अद्विषन् अद्वेट् अद्विष्टम् अद्विष्ट अद्वेषम् अद्विष्व अद्विष्म",
            "हु" to "अजुहोत् अजुहुताम् अजुहवुः अजुहोः अजुहुतम् अजुहुत अजुहवम् अजुहुव अजुहुम",
            "नृतीँ" to "अनृत्यत् अनृत्यताम् अनृत्यन् अनृत्यः अनृत्यतम् अनृत्यत अनृत्यम् अनृत्याव अनृत्याम",
            "षुञ्" to "असुनोत् असुनुताम् असुन्वन् असुनोः असुनुतम् असुनुत असुनवम् असुनुव असुनुम",
            "नुद्" to "अनुदत् अनुदताम् अनुदन् अनुदः अनुदतम् अनुदत अनुदम् अनुदाव अनुदाम",
            "रुधिँर्" to "अरुणत् अरुन्द्धाम् अरुन्धन् अरुणत् अरुन्द्धम् अरुन्द्ध अरुणधम् अरुन्ध्व अरुन्ध्म",
            "तनुँ" to "अतनोत् अतनुताम् अतन्वन् अतनोः अतनुतम् अतनुत अतनवम् अतनुव अतनुम",
            "डुक्रीञ्" to "अक्रीणात् अक्रीणीताम् अक्रीणन् अक्रीणाः अक्रीणीतम् अक्रीणीत अक्रीणाम् अक्रीणीव अक्रीणीम",
            "चुरँ" to "अचोरयत् अचोरयताम् अचोरयन् अचोरयः अचोरयतम् अचोरयत अचोरयम् अचोरयाव अचोरयाम",
        )

        expected.forEach { (root, surfaces) ->
            TingantaEngine()
                .deriveSupportedParadigm(root, PadaType.PARASMAIPADA, Lakara.LANG)
                .assertSurfaces(surfaces)
        }
    }

    @Test
    fun `all ganas derive complete representative atmanepada imperfects`() {
        val expected = mapOf(
            "एधँ" to "ऐधत ऐधेताम् ऐधन्त ऐधथाः ऐधेथाम् ऐधध्वम् ऐधे ऐधावहि ऐधामहि",
            "द्विषँ" to "अद्विष्ट अद्विषाताम् अद्विषत अद्विष्ठाः अद्विषाथाम् अद्विड्ढ्वम् अद्विषि अद्विष्वहि अद्विष्महि",
            "डुभृञ्" to "अबिभृत अबिभ्राताम् अबिभ्रत अबिभृथाः अबिभ्राथाम् अबिभृध्वम् अबिभ्रि अबिभृवहि अबिभृमहि",
            "दूङ्" to "अदूयत अदूयेताम् अदूयन्त अदूयथाः अदूयेथाम् अदूयध्वम् अदूये अदूयावहि अदूयामहि",
            "षुञ्" to "असुनुत असुन्वाताम् असुन्वत असुनुथाः असुन्वाथाम् असुनुध्वम् असुन्वि असुनुवहि असुनुमहि",
            "तुदँ" to "अतुदत अतुदेताम् अतुदन्त अतुदथाः अतुदेथाम् अतुदध्वम् अतुदे अतुदावहि अतुदामहि",
            "रुधिँर्" to "अरुन्द्ध अरुन्धाताम् अरुन्धत अरुन्द्धाः अरुन्धाथाम् अरुन्द्ध्वम् अरुन्धि अरुन्ध्वहि अरुन्ध्महि",
            "तनुँ" to "अतनुत अतन्वाताम् अतन्वत अतनुथाः अतन्वाथाम् अतनुध्वम् अतन्वि अतनुवहि अतनुमहि",
            "डुक्रीञ्" to "अक्रीणीत अक्रीणाताम् अक्रीणत अक्रीणीथाः अक्रीणाथाम् अक्रीणीध्वम् अक्रीणि अक्रीणीवहि अक्रीणीमहि",
            "चुरँ" to "अचोरयत अचोरयेताम् अचोरयन्त अचोरयथाः अचोरयेथाम् अचोरयध्वम् अचोरये अचोरयावहि अचोरयामहि",
        )

        expected.forEach { (root, surfaces) ->
            TingantaEngine()
                .deriveSupportedParadigm(root, PadaType.ATMANEPADA, Lakara.LANG)
                .assertSurfaces(surfaces)
        }
    }

    @Test
    fun `all ganas derive complete representative parasmaipada imperatives`() {
        val expected = mapOf(
            "भू" to "भवतु भवताम् भवन्तु भव भवतम् भवत भवानि भवाव भवाम",
            "अद्" to "अत्तु अत्ताम् अदन्तु अद्धि अत्तम् अत्त अदानि अदाव अदाम",
            "हु" to "जुहोतु जुहुताम् जुह्वतु जुहुधि जुहुतम् जुहुत जुहवानि जुहवाव जुहवाम",
            "दिव्" to "दीव्यतु दीव्यताम् दीव्यन्तु दीव्य दीव्यतम् दीव्यत दीव्यानि दीव्याव दीव्याम",
            "षुञ्" to "सुनोतु सुनुताम् सुन्वन्तु सुनु सुनुतम् सुनुत सुनवानि सुनवाव सुनवाम",
            "नुद्" to "नुदतु नुदताम् नुदन्तु नुद नुदतम् नुदत नुदानि नुदाव नुदाम",
            "रुधिँर्" to "रुणद्धु रुन्द्धाम् रुन्धन्तु रुन्द्धि रुन्द्धम् रुन्द्ध रुणधानि रुणधाव रुणधाम",
            "तनुँ" to "तनोतु तनुताम् तन्वन्तु तनु तनुतम् तनुत तनवानि तनवाव तनवाम",
            "डुक्रीञ्" to "क्रीणातु क्रीणीताम् क्रीणन्तु क्रीणीहि क्रीणीतम् क्रीणीत क्रीणानि क्रीणाव क्रीणाम",
            "चुरँ" to "चोरयतु चोरयताम् चोरयन्तु चोरय चोरयतम् चोरयत चोरयानि चोरयाव चोरयाम",
        )

        expected.forEach { (root, surfaces) ->
            TingantaEngine()
                .deriveSupportedParadigm(root, PadaType.PARASMAIPADA, Lakara.LOT)
                .assertSurfaces(surfaces)
        }
    }

    @Test
    fun `all ganas derive complete representative atmanepada imperatives`() {
        val expected = mapOf(
            "एधँ" to "एधताम् एधेताम् एधन्ताम् एधस्व एधेथाम् एधध्वम् एधै एधावहै एधामहै",
            "द्विषँ" to "द्विष्टाम् द्विषाताम् द्विषताम् द्विक्ष्व द्विषाथाम् द्विड्ढ्वम् द्वेषै द्वेषावहै द्वेषामहै",
            "डुभृञ्" to "बिभृताम् बिभ्राताम् बिभ्रताम् बिभृष्व बिभ्राथाम् बिभृध्वम् बिभरै बिभरावहै बिभरामहै",
            "दूङ्" to "दूयताम् दूयेताम् दूयन्ताम् दूयस्व दूयेथाम् दूयध्वम् दूयै दूयावहै दूयामहै",
            "षुञ्" to "सुनुताम् सुन्वाताम् सुन्वताम् सुनुष्व सुन्वाथाम् सुनुध्वम् सुनवै सुनवावहै सुनवामहै",
            "तुदँ" to "तुदताम् तुदेताम् तुदन्ताम् तुदस्व तुदेथाम् तुदध्वम् तुदै तुदावहै तुदामहै",
            "रुधिँर्" to "रुन्द्धाम् रुन्धाताम् रुन्धताम् रुन्त्स्व रुन्धाथाम् रुन्द्ध्वम् रुणधै रुणधावहै रुणधामहै",
            "तनुँ" to "तनुताम् तन्वाताम् तन्वताम् तनुष्व तन्वाथाम् तनुध्वम् तनवै तनवावहै तनवामहै",
            "डुक्रीञ्" to "क्रीणीताम् क्रीणाताम् क्रीणताम् क्रीणीष्व क्रीणाथाम् क्रीणीध्वम् क्रीणै क्रीणावहै क्रीणामहै",
            "चुरँ" to "चोरयताम् चोरयेताम् चोरयन्ताम् चोरयस्व चोरयेथाम् चोरयध्वम् चोरयै चोरयावहै चोरयामहै",
        )

        expected.forEach { (root, surfaces) ->
            TingantaEngine()
                .deriveSupportedParadigm(root, PadaType.ATMANEPADA, Lakara.LOT)
                .assertSurfaces(surfaces)
        }
    }

    @Test
    fun `all ganas derive complete representative parasmaipada optatives`() {
        val expected = mapOf(
            "भू" to "भवेत् भवेताम् भवेयुः भवेः भवेतम् भवेत भवेयम् भवेव भवेम",
            "अद्" to "अद्यात् अद्याताम् अद्युः अद्याः अद्यातम् अद्यात अद्याम् अद्याव अद्याम",
            "हु" to "जुहुयात् जुहुयाताम् जुहुयुः जुहुयाः जुहुयातम् जुहुयात जुहुयाम् जुहुयाव जुहुयाम",
            "दिव्" to "दीव्येत् दीव्येताम् दीव्येयुः दीव्येः दीव्येतम् दीव्येत दीव्येयम् दीव्येव दीव्येम",
            "षुञ्" to "सुनुयात् सुनुयाताम् सुनुयुः सुनुयाः सुनुयातम् सुनुयात सुनुयाम् सुनुयाव सुनुयाम",
            "नुद्" to "नुदेत् नुदेताम् नुदेयुः नुदेः नुदेतम् नुदेत नुदेयम् नुदेव नुदेम",
            "रुधिँर्" to "रुन्ध्यात् रुन्ध्याताम् रुन्ध्युः रुन्ध्याः रुन्ध्यातम् रुन्ध्यात रुन्ध्याम् रुन्ध्याव रुन्ध्याम",
            "तनुँ" to "तनुयात् तनुयाताम् तनुयुः तनुयाः तनुयातम् तनुयात तनुयाम् तनुयाव तनुयाम",
            "डुक्रीञ्" to "क्रीणीयात् क्रीणीयाताम् क्रीणीयुः क्रीणीयाः क्रीणीयातम् क्रीणीयात क्रीणीयाम् क्रीणीयाव क्रीणीयाम",
            "चुरँ" to "चोरयेत् चोरयेताम् चोरयेयुः चोरयेः चोरयेतम् चोरयेत चोरयेयम् चोरयेव चोरयेम",
        )

        expected.forEach { (root, surfaces) ->
            TingantaEngine()
                .deriveSupportedParadigm(root, PadaType.PARASMAIPADA, Lakara.LING)
                .assertSurfaces(surfaces)
        }
    }

    @Test
    fun `all ganas derive complete representative atmanepada optatives`() {
        val expected = mapOf(
            "एधँ" to "एधेत एधेयाताम् एधेरन् एधेथाः एधेयाथाम् एधेध्वम् एधेय एधेवहि एधेमहि",
            "द्विषँ" to "द्विषीत द्विषीयाताम् द्विषीरन् द्विषीथाः द्विषीयाथाम् द्विषीढ्वम् द्विषीय द्विषीवहि द्विषीमहि",
            "डुभृञ्" to "बिभ्रीत बिभ्रीयाताम् बिभ्रीरन् बिभ्रीथाः बिभ्रीयाथाम् बिभ्रीध्वम् बिभ्रीय बिभ्रीवहि बिभ्रीमहि",
            "दूङ्" to "दूयेत दूयेयाताम् दूयेरन् दूयेथाः दूयेयाथाम् दूयेध्वम् दूयेय दूयेवहि दूयेमहि",
            "षुञ्" to "सुन्वीत सुन्वीयाताम् सुन्वीरन् सुन्वीथाः सुन्वीयाथाम् सुन्वीध्वम् सुन्वीय सुन्वीवहि सुन्वीमहि",
            "तुदँ" to "तुदेत तुदेयाताम् तुदेरन् तुदेथाः तुदेयाथाम् तुदेध्वम् तुदेय तुदेवहि तुदेमहि",
            "रुधिँर्" to "रुन्धीत रुन्धीयाताम् रुन्धीरन् रुन्धीथाः रुन्धीयाथाम् रुन्धीध्वम् रुन्धीय रुन्धीवहि रुन्धीमहि",
            "तनुँ" to "तन्वीत तन्वीयाताम् तन्वीरन् तन्वीथाः तन्वीयाथाम् तन्वीध्वम् तन्वीय तन्वीवहि तन्वीमहि",
            "डुक्रीञ्" to "क्रीणीत क्रीणीयाताम् क्रीणीरन् क्रीणीथाः क्रीणीयाथाम् क्रीणीध्वम् क्रीणीय क्रीणीवहि क्रीणीमहि",
            "चुरँ" to "चोरयेत चोरयेयाताम् चोरयेरन् चोरयेथाः चोरयेयाथाम् चोरयेध्वम् चोरयेय चोरयेवहि चोरयेमहि",
        )

        expected.forEach { (root, surfaces) ->
            TingantaEngine()
                .deriveSupportedParadigm(root, PadaType.ATMANEPADA, Lakara.LING)
                .assertSurfaces(surfaces)
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
    fun `Divadi present uses shyan and lengthens div`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("दिव्", lakara = Lakara.LAT)

        paradigm.forms.values.forEach { result ->
            assertTrue(result.applications.none { it.sutra == "3.1.68" })
            assertTrue(result.final.terms.any { it.upadesha == "श्यन्" })
            assertTrue(result.applications.any { it.sutra == "8.2.77" })
        }
    }

    @Test
    fun `Adadi present uses shap luk`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("अद्", lakara = Lakara.LAT)

        paradigm.forms.values.forEach { result ->
            assertTrue(result.final.droppedTerms.any { it.upadesha == "शप्" && it.deletionType == LopaType.LUK })
        }
        listOf(TingAffix.TIP, TingAffix.TAS, TingAffix.SIP, TingAffix.THAS, TingAffix.THA).forEach { affix ->
            assertTrue(paradigm.forms.getValue(affix).applications.any { it.sutra == "8.4.55" })
        }
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
    fun `present paradigm reports derived coverage`() {
        val paradigm = TingantaEngine().deriveSupportedParadigm("भू", lakara = Lakara.LAT)

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

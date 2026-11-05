package dev.panini.execution.binding

import dev.panini.analysis.FrameDiagnosticCode
import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KriyaQualificationKind
import dev.panini.analysis.particleRoles
import dev.panini.core.Karaka
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Sentences taken from Kāśikā on the kāraka sūtras, with no meaning supplied. */
class KashikaExamplesTest {
    private val parser = PaniniParser()
    private fun frame(source: String) = VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("$source ।"))!!.frames.last()
    private fun karaka(source: String, pada: String) = (frame(source).relations.single { it.participant.pada.sourceText == pada }
        .resolution as? FrameKarakaResolution.Resolved)?.karaka

    @Test fun `active voice and the case decide these`() {
        assertEquals(Karaka.KARTR, karaka("देवदत्त + सुँ ओदन + अम् डुपचँष् + लट् + तिप्", "देवदत्त+सुँ"))
        assertEquals(Karaka.KARMAN, karaka("देवदत्त + सुँ ओदन + अम् डुपचँष् + लट् + तिप्", "ओदन+अम्"))
        assertEquals(Karaka.KARANA, karaka("दात्र + टा लूञ् + लट् + तिप्", "दात्र+टा"))
        assertEquals(Karaka.KARANA, karaka("परशु + टा छिदिँर् + लट् + तिप्", "परशु+टा"))
        assertEquals(Karaka.ADHIKARANA, karaka("कट + ङि आसँ + लट् + त", "कट+ङि"))
        assertEquals(Karaka.ADHIKARANA, karaka("स्थाली + ङि डुपचँष् + लट् + तिप्", "स्थाली+ङि"))
        assertEquals(Karaka.APADANA, karaka("ग्राम + ङसिँ आङ् + गम् + लट् + तिप्", "ग्राम+ङसिँ"))
        assertEquals(Karaka.SAMPRADANA, karaka("उपाध्याय + ङे गो + अम् दा + लट् + तिप्", "उपाध्याय+ङे"))
    }

    @Test fun `passive leaves its karta in the instrumental`() {
        assertEquals(Karaka.KARTR, karaka("देवदत्त + टा ग्राम + सुँ गम् + यक् + लट् + त", "देवदत्त+टा"))
        assertEquals(Karaka.KARTR, karaka("देवदत्त + टा आसँ + यक् + लट् + त", "देवदत्त+टा"))
    }

    @Test fun `causatives find the causee from the base root`() {
        assertEquals("1.4.52", frame("राम + सुँ लक्ष्मण + अम् शीङ् + णिच् + लट् + तिप्").relations
            .single { it.participant.pada.sourceText == "लक्ष्मण+अम्" }.evidence.first().sutra)
        val cooking = frame("देवदत्त + सुँ विश्वामित्र + टा ओदन + अम् डुपचँष् + णिच् + लट् + तिप्")
        assertEquals(FrameKarakaResolution.Resolved(Karaka.KARTR), cooking.relations.single { it.participant.pada.sourceText == "विश्वामित्र+टा" }.resolution)
        assertEquals(FrameKarakaResolution.Resolved(Karaka.KARMAN), cooking.relations.single { it.participant.pada.sourceText == "ओदन+अम्" }.resolution)
    }

    @Test fun `predicates and apposition are not second agents`() {
        val predicate = frame("दशरथ + सुँ खिन्न + सुँ भू + लङ् + तिप्")
        assertEquals(FrameKarakaResolution.Resolved(Karaka.KARTR), predicate.relations.first().resolution)
        assertTrue(predicate.relations[1].evidence.any { it.sutra == "2.3.46" }, predicate.relations[1].evidence.toString())
        assertEquals(Karaka.KARTR, karaka("दाशरथि + सुँ राम + सुँ वन + अम् गम् + लट् + तिप्", "राम+सुँ"))
    }

    @Test fun `passive expresses the object its root class selects`() {
        val milking = frame("गोपाल + टा गो + सुँ दुग्ध + अम् दुहँ + यक् + लट् + त")
        assertEquals(listOf("1.4.51", "2.3.46"), milking.relations.single { it.participant.pada.sourceText == "गो+सुँ" }.evidence.map { it.sutra })
        assertEquals(FrameKarakaResolution.Resolved(Karaka.KARMAN), milking.relations.single { it.participant.pada.sourceText == "दुग्ध+अम्" }.resolution)
        val leading = frame("तद् + टा अजा + सुँ ग्राम + अम् नी + यक् + लट् + त")
        assertEquals(listOf("1.4.49", "2.3.46"), leading.relations.single { it.participant.pada.sourceText == "अजा+सुँ" }.evidence.map { it.sutra })
        assertEquals(FrameKarakaResolution.Resolved(Karaka.KARMAN), leading.relations.single { it.participant.pada.sourceText == "ग्राम+अम्" }.resolution)
        val motion = frame("राम + टा लक्ष्मण + सुँ ग्राम + अम् गम् + णिच् + यक् + लट् + त")
        assertEquals(FrameKarakaResolution.Resolved(Karaka.KARMAN), motion.relations.single { it.participant.pada.sourceText == "लक्ष्मण+सुँ" }.resolution)
        assertEquals(FrameKarakaResolution.Resolved(Karaka.KARMAN), motion.relations.single { it.participant.pada.sourceText == "ग्राम+अम्" }.resolution)
    }

    @Test fun `upapadas govern the case beside them`() {
        fun chain(source: String, pada: String) = frame(source).relations.single { it.participant.pada.sourceText == pada }.evidence.map { it.sutra }
        assertEquals(listOf("2.3.19"), chain("राम + टा सह सीता + सुँ वन + अम् गम् + लट् + तिप्", "राम+टा"))
        assertEquals(listOf("2.3.32"), chain("राम + टा विना श्याम + सुँ गम् + लट् + तिप्", "राम+टा"))
        assertEquals(Karaka.KARTR, karaka("राम + टा सह सीता + सुँ वन + अम् गम् + लट् + तिप्", "सीता+सुँ"))
        assertEquals(Karaka.KARMAN, karaka("राम + टा सह सीता + सुँ वन + अम् गम् + लट् + तिप्", "वन+अम्"))
    }

    @Test fun `varttika uses read the written governor or the lost absolutive`() {
        fun chain(source: String, pada: String) = frame(source).relations.single { it.participant.pada.sourceText == pada }.evidence.map { it.sutra }
        assertEquals(listOf("2.3.2"), chain("ग्राम + अम् परितः वृक्ष + जस् अस् + लट् + झि", "ग्राम+अम्"))
        assertEquals(listOf("2.3.28"), chain("राम + सुँ आसन + ङसिँ प्र + ईक्षँ + लट् + त", "आसन+ङसिँ"))
    }

    @Test fun `concessive clauses parse as two actions linked by तथापि`() {
        val analysis = VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("यद्यपि वर्षा + सुँ भू + लट् + तिप् तथापि मयूर + सुँ न नृत् + लट् + तिप् ।"))!!
        assertEquals(2, analysis.frames.size)
        assertTrue(analysis.links.any { it is dev.panini.analysis.KriyaLink.Coordination && it.connector == "तथापि" })
    }

    @Test fun `particles are named`() {
        assertEquals(listOf(dev.panini.analysis.ParticleRole.PAST), 
            frame("कृष्ण + सुँ मथुरा + ङि वसँ + लट् + तिप् स्म").particleRoles.values.toList())
    }

    @Test fun `an adverb is labelled and defaults are marked`() {
        assertTrue(frame("हस्तिन् + सुँ मन्द + अम् गम् + लट् + तिप्").qualifications.any { it.kind == KriyaQualificationKind.MANNER })
        fun marked(source: String) = frame(source).diagnostics.filter { it.code == FrameDiagnosticCode.DEFAULT_READING }.map { it.sourceText }
        assertEquals(listOf("राम+टा"), marked("राम + टा गम् + यक् + लट् + त"))
        assertEquals(listOf("वृक्ष+ङि"), marked("वृक्ष + ङि वसँ + लट् + तिप्"))
        assertEquals(emptyList(), marked("राम + सुँ गम् + लट् + तिप्"), "The voice proves this kartā.")
    }

    @Test fun `root classes come from the Dhatupatha gloss`() {
        assertEquals(listOf("1.4.29"), frame("उपाध्याय + ङसिँ अधि + इङ् + लट् + त").relations.single().evidence.map { it.sutra }.take(1))
        assertEquals(Karaka.KARMAN, karaka("राम + सुँ ग्राम + ङे या + लट् + तिप्", "ग्राम+ङे"), "या is glossed प्रापणे.")
        assertEquals(Karaka.KARMAN, karaka("राम + सुँ ग्राम + ङे अटँ + लट् + तिप्", "ग्राम+ङे"), "A गतौ root that was on no hand list.")
    }

    @Test fun `avyayas come from the Ganapatha and stay free as stems`() {
        assertEquals(listOf("2.3.19"), frame("राम + टा सह सीता + सुँ गम् + लट् + तिप्").relations.first().evidence.map { it.sutra })
        assertTrue(frame("स्वस्ति गम् + लट् + तिप्").relations.isEmpty())
        assertEquals("सह+सुँ", frame("सह + सुँ गम् + लट् + तिप्").relations.single().participant.pada.sourceText)
        kotlin.test.assertFails { parser.parse("राम सीता + सुँ गम् + लट् + तिप् ।") }
    }

    @Test fun `a stem derived for means is not taken as a passive karta`() {
        for (tool in listOf("दात्र", "खनित्र", "नीति", "दण्ड")) {
            assertEquals(null, karaka("$tool + टा घट + सुँ डुकृञ् + यक् + लट् + त", "$tool+टा"), tool)
        }
        assertEquals(Karaka.KARTR, karaka("वायु + टा घट + सुँ गम् + यक् + लट् + त", "वायु+टा"), "Uṇādi 1.1 derives वायु as a doer.")
        assertEquals(Karaka.KARTR, karaka("डुकृञ् + ण्वुल् + टा घट + सुँ गम् + यक् + लट् + त", "डुकृञ्+ण्वुल्+टा"))
    }
}

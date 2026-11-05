package dev.panini.execution.binding

import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.participialRelations
import dev.panini.analysis.infinitiveRoles
import dev.panini.core.Karaka
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** Roots with no executable operation still have grammar: voice shows kartā and karma. */
class VoiceRoleConstructionTest {
    private val parser = PaniniParser()

    private fun roles(source: String): Map<String, Karaka?> {
        val frame = VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("$source ।"))!!.frames.last()
        return frame.relations.associate { relation ->
            relation.participant.pada.sourceText.replace(" ", "") to (relation.resolution as? FrameKarakaResolution.Resolved)?.karaka
        }
    }

    @Test fun `kartari voice shows the karta and a sole accusative the karma`() {
        assertEquals(Karaka.KARTR, roles("राम + सुँ गम् + लट् + तिप्")["राम+सुँ"])
        assertEquals(Karaka.KARTR, roles("राम + सुँ डुपचँष् + लट् + तिप्")["राम+सुँ"])
        val transitive = roles("राम + सुँ ग्राम + अम् गम् + लट् + तिप्")
        assertEquals(Karaka.KARTR, transitive["राम+सुँ"])
        assertEquals(Karaka.KARMAN, transitive["ग्राम+अम्"])
    }

    @Test fun `karmani voice shows the expressed karma`() {
        assertEquals(Karaka.KARMAN, roles("राम + टा ग्राम + सुँ गम् + यक् + लट् + त")["ग्राम+सुँ"])
    }

    @Test fun `causative nominative is the prompting karta`() {
        assertEquals(Karaka.KARTR, roles("राम + सुँ लक्ष्मण + अम् शीङ् + णिच् + लट् + तिप्")["राम+सुँ"])
    }

    @Test fun `coordinated nominatives share the karta role`() {
        val both = roles("राम + सुँ सीता + सुँ च वन + अम् गम् + लट् + तस्")
        assertEquals(Karaka.KARTR, both["राम+सुँ"])
        assertEquals(Karaka.KARTR, both["सीता+सुँ"])
    }

    @Test fun `open readings are not forced`() {
        val apposition = roles("दाशरथि + सुँ राम + सुँ वन + अम् गम् + लट् + तिप्")
        assertEquals(Karaka.KARTR, apposition["राम+सुँ"], "The noun named by the adjoining nominative.")
        val dvikarmaka = roles("गोपाल + सुँ गो + अम् दुग्ध + अम् दुहँ + लट् + तिप्")
        assertEquals(Karaka.KARMAN, dvikarmaka["गो+अम्"], "Both are karma; the sub-role follows commentary order and is marked.")
        val causee = roles("राम + सुँ लक्ष्मण + अम् शीङ् + णिच् + लट् + तिप्")
        assertEquals(Karaka.KARMAN, causee["लक्ष्मण+अम्"], "An akarmaka base makes its accusative the causee (1.4.52).")
    }

    @Test fun `homonymous surface stays unresolved while a unique one resolves`() {
        assertEquals("गमॢँ", VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("राम + सुँ गम् + लट् + तिप् ।"))!!
            .frames.single().kriya?.dhatu?.upadesha)
        assertEquals(null, VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("राम + सुँ मन् + लट् + इट् ।"))!!
            .frames.single().kriya?.dhatu)
    }

    @Test fun `time and manner accusatives are not the karma`() {
        val month = VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("राम + सुँ मास + अम् अधि + इङ् + लट् + त ।"))!!.frames.single()
        assertEquals(listOf("2.3.5"), month.relations.single { it.participant.pada.sourceText == "मास+अम्" }.evidence.map { it.sutra })
        assertEquals(Karaka.ANIRDHARITA, roles("हस्तिन् + सुँ मन्द + अम् गम् + लट् + तिप्")["मन्द+अम्"], "Said of the action, not a karma.")
        val cooking = roles("मास + अम् ओदन + अम् डुपचँष् + लट् + तिप्")
        assertEquals(Karaka.KARMAN, cooking["ओदन+अम्"])
        assertEquals(null, roles("राम + सुँ क्रोश + अम् गम् + लट् + तिप्")["क्रोश+अम्"], "Distance with motion may be the goal.")
    }

    private fun analysis(source: String) = VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("$source ।"))!!

    @Test fun `genitive before an action noun falls under 2_3_65`() {
        val relation = analysis("राम + ङस् गम् + ल्युट् + सुँ भू + लट् + तिप्").frames.single().nominalKarakas.single().relation
        assertTrue(relation.evidence.none { it.sutra == "2.3.50" }, relation.toString().take(300))
    }

    @Test fun `a plural beside subject and predicate is the group selected from`() {
        val group = analysis("राम + सुँ क्षत्रिय + सुप् शूर + सुँ अस् + लट् + तिप्").frames.single()
            .relations.single { it.participant.pada.sourceText == "क्षत्रिय+सुप्" }
        assertTrue(group.evidence.any { it.sutra == "2.3.41" }, group.evidence.map { it.sutra }.toString())
    }

    @Test fun `an agreeing participle is not a second object`() {
        val frame = analysis("बाल + अम् शीङ् + शानच् + अम् दृश् + लोट् + सिप्").frames.single()
        assertTrue(frame.diagnostics.none { it.code.name == "MULTIPLE_PRIMARY_OBJECTS" })
        assertEquals(Karaka.KARMAN, (frame.relations.first().resolution as FrameKarakaResolution.Resolved).karaka)
    }

    @Test fun `coordinated subjects are one position and agree in number`() {
        val codes = analysis("राम + सुँ सीता + सुँ च वन + अम् गम् + लट् + तस्").frames.single().diagnostics.map { it.code.name }
        assertTrue("MULTIPLE_INDEPENDENT_AGENTS" !in codes && "AGREEMENT_MISMATCH" !in codes, codes.toString())
    }

    @Test fun `च may stand after the first word of its clause`() {
        val frames = analysis("राम + सुँ फल + अम् खादृँ + लट् + तिप् जल + अम् च पा + लट् + तिप्").frames
        assertEquals(2, frames.size)
        assertEquals(listOf("जल+अम्"), frames[1].relations.map { it.participant.pada.sourceText })
    }

    @Test fun `absolute locative has its own subject and object`() {
        for ((affix, time) in listOf("शतृ" to dev.panini.analysis.ParticipialTime.SIMULTANEOUS, "क्त" to dev.panini.analysis.ParticipialTime.PRIOR)) {
            val frame = analysis("राम + ङि वन + अम् गम् + $affix + ङि सीता + सुँ अनु + सृ + शप् + लट् + तिप्").frames.single()
            val relation = frame.participialRelations.single()
            assertEquals(time, relation.time)
            assertTrue(relation.absolute)
            assertEquals(listOf("2.3.37"), frame.relations.single { it.participant.pada.sourceText == "राम+ङि" }.evidence.map { it.sutra })
            assertEquals(listOf("वन+अम्"), frame.nominalKarakas.map { it.relation.participant.pada.sourceText })
            assertEquals(Karaka.KARMAN, (frame.nominalKarakas.single().relation.resolution as FrameKarakaResolution.Resolved).karaka)
            assertEquals(Karaka.KARTR, (frame.relations.single { it.participant.pada.sourceText == "सीता+सुँ" }.resolution as FrameKarakaResolution.Resolved).karaka)
        }
    }

    @Test fun `participle agreeing with the subject is simultaneous, and a plain locative is not absolute`() {
        val frame = analysis("बालक + सुँ शीङ् + शानच् + सुँ गम् + लट् + तिप्").frames.single()
        val relation = frame.participialRelations.single()
        assertEquals(dev.panini.analysis.ParticipialTime.SIMULTANEOUS, relation.time)
        assertEquals(false, relation.absolute)
        assertEquals(Karaka.KARTR, (frame.relations.first().resolution as FrameKarakaResolution.Resolved).karaka)
        assertTrue(analysis("वानर + सुँ वृक्ष + ङि वसँ + लट् + तिप्").frames.single().participialRelations.isEmpty())
    }

    @Test fun `नी is the dvikarmaka root णीञ्`() {
        val frame = VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("अजा + अम् ग्राम + अम् नी + लट् + तिप् ।"))!!.frames.single()
        assertEquals("णीञ्", frame.kriya?.dhatu?.upadesha)
    }

    private fun chain(source: String, pada: String) = analysis(source).frames.single().relations
        .single { it.participant.pada.sourceText == pada }.evidence.map { it.sutra }

    @Test fun `case with stem class decides these relations`() {
        assertEquals(listOf("2.3.6"), chain("राम + सुँ मास + टा अधि + इङ् + लट् + त", "मास+टा"))
        assertEquals(listOf("2.3.15"), chain("छात्र + सुँ अध्ययन + ङे गम् + लट् + तिप्".replace("अध्ययन", "अधि + इङ् + ल्युट्"), "अधि+इङ्+ल्युट्+ङे"))
        assertEquals(listOf("2.3.20"), chain("बाल + सुँ अक्षि + टा काण + सुँ अस् + लट् + तिप्", "अक्षि+टा"))
        assertEquals(listOf("2.3.42"), chain("माथुर + जस् पाटलीपुत्रक + भ्यस् आढ्यतर + जस् अस् + लट् + झि", "पाटलीपुत्रक+भ्यस्"))
    }

    @Test fun `an action noun instrumental defaults to reason`() {
        assertEquals(listOf("2.3.23"), chain("विद्यार्थिन् + सुँ अधि + इङ् + ल्युट् + टा गम् + लट् + तिप्", "अधि+इङ्+ल्युट्+टा"))
    }

    @Test fun `tumun takes its own object and its role from the verb`() {
        val purpose = analysis("अस्मद् + सुँ शास्त्र + अम् पठँ + तुमुँन् विद्यालय + अम् गम् + लट् + मिप्").frames.single()
        assertEquals(listOf(dev.panini.analysis.InfinitiveRole.PURPOSE), purpose.infinitiveRoles.values.toList())
        assertEquals(listOf("शास्त्र+अम्"), purpose.purposeKarakas.map { it.relation.participant.pada.sourceText })
        assertEquals(Karaka.KARMAN, (purpose.relations.single { it.participant.pada.sourceText == "विद्यालय+अम्" }.resolution as FrameKarakaResolution.Resolved).karaka)
        assertEquals(listOf(dev.panini.analysis.InfinitiveRole.OBJECT_OF_DESIRE),
            analysis("बालक + सुँ पठँ + तुमुँन् इष् + लट् + तिप्").frames.single().infinitiveRoles.values.toList())
        assertEquals(listOf(dev.panini.analysis.InfinitiveRole.COMPLEMENT),
            analysis("तद् + सुँ गम् + तुमुँन् शकॢँ + लट् + तिप्").frames.single().infinitiveRoles.values.toList())
    }

    @Test fun `commentary examples hold`() {
        assertEquals(listOf("2.3.41"), chain("मनुष्य + आम् क्षत्रिय + सुँ शूरतम + सुँ अस् + लट् + तिप्", "मनुष्य+आम्"))
        assertEquals(listOf("2.3.41"), chain("मनुष्य + सुप् क्षत्रिय + सुँ शूरतम + सुँ अस् + लट् + तिप्", "मनुष्य+सुप्"))
        val locative = analysis("गो + सुप् दुहँ + शानच् + सुप् गम् + क्त + सुँ अस् + लट् + तिप्").frames.single().participialRelations
        assertTrue(locative.any { it.absolute }, locative.toString())
        val maker = analysis("जल + आम् सृज् + तृच् + सुँ भू + लट् + तिप्").frames.single()
        assertEquals(listOf("जल+आम्"), maker.nominalKarakas.map { it.relation.participant.pada.sourceText })
    }

    @Test fun `unresolved homonymous verb gets no invented infinitive role`() {
        assertEquals(listOf(dev.panini.analysis.InfinitiveRole.COMPLEMENT),
            analysis("तद् + सुँ गम् + तुमुँन् शक् + लट् + तिप्").frames.single().infinitiveRoles.values.toList())
        assertTrue(analysis("तद् + सुँ गम् + तुमुँन् मन् + लट् + इट्").frames.single().infinitiveRoles.isEmpty())
    }

    @Test fun `passive instrumental defaults to karta unless it names a tool`() {
        assertEquals(Karaka.KARTR, roles("राम + टा गम् + यक् + लट् + त")["राम+टा"])
        val passive = roles("राम + टा ग्राम + सुँ गम् + यक् + लट् + त")
        assertEquals(Karaka.KARTR, passive["राम+टा"])
        assertEquals(Karaka.KARMAN, passive["ग्राम+सुँ"])
        assertEquals(null, roles("दण्ड + टा घट + सुँ डुकृञ् + यक् + लट् + त")["दण्ड+टा"], "A tool stays open.")
        assertEquals(null, roles("राम + टा परशु + टा वृक्ष + सुँ गम् + यक् + लट् + त")["राम+टा"], "Two instrumentals stay open.")
        assertEquals(Karaka.KARTR, roles("राम + टा लक्ष्मण + सुँ ग्राम + अम् गम् + णिच् + यक् + लट् + त")["राम+टा"])
        assertEquals(Karaka.KARANA, roles("अध्यापक + सुँ राम + टा गम् + लट् + तिप्")["राम+टा"], "Active voice has its kartā, so this is the means.")
    }

    private fun rootOf(source: String) = analysis(source).frames.single().kriya?.dhatu

    @Test fun `a homonym takes its root from the written form`() {
        assertEquals(dev.panini.core.DhatuGana.BHVADI, rootOf("वानर + सुँ वृक्ष + ङि वसँ + लट् + तिप्")?.gana)
        assertEquals(dev.panini.core.DhatuGana.BHVADI, rootOf("शत्रु + शस् जि + लट् + तिप्")?.gana)
        assertEquals(null, rootOf("अस्मद् + सुँ मनँ + लट् + इट्"), "मन् has no bhvādi entry, so the form must name its vikaraṇa.")
        assertEquals(dev.panini.core.DhatuGana.DIVADI, rootOf("अस्मद् + सुँ मनँ + श्यन् + लट् + इट्")?.gana)
    }

    @Test fun `a plain locative is the adhikarana`() {
        assertEquals(listOf("1.4.45", "2.3.36"), chain("वानर + सुँ वृक्ष + ङि वसँ + लट् + तिप्", "वृक्ष+ङि"))
        assertEquals(listOf("1.4.45", "2.3.36"), chain("राम + टा मार्ग + ङि गम् + यक् + लट् + त", "मार्ग+ङि"))
        assertEquals(listOf("2.3.37"), chain("राम + ङि गम् + शतृ + ङि सीता + सुँ गम् + लट् + तिप्", "राम+ङि"), "An absolute locative is not a locus.")
    }

    @Test fun `motion roots decide the dative goal and the source`() {
        assertEquals(listOf("1.4.49", "2.3.12"), chain("राम + सुँ ग्राम + ङे गम् + लट् + तिप्", "ग्राम+ङे"))
        assertEquals(listOf("1.4.24", "2.3.28"), chain("राम + सुँ ग्राम + ङसिँ आङ् + गम् + लट् + तिप्", "ग्राम+ङसिँ"))
        assertEquals(listOf("1.4.24", "2.3.28"), chain("पर्ण + सुँ वृक्ष + ङसिँ पत् + लट् + तिप्", "वृक्ष+ङसिँ"))
    }

    @Test fun `these cases stay open without the root class`() {
        assertEquals(null, roles("राम + सुँ शिष्य + ङे ग्रन्थ + अम् पठँ + लट् + तिप्")["शिष्य+ङे"], "Sampradāna needs the object of giving (Kāśikā on 1.4.32).")
        assertEquals(null, roles("राम + सुँ फल + भ्यस् या + लट् + तिप्")["फल+भ्यस्"], "भ्यस् may be dative or ablative.")
    }
}

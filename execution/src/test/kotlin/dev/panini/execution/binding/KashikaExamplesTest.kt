package dev.panini.execution.binding

import dev.panini.analysis.FrameDiagnosticCode
import dev.panini.analysis.FrameKarakaResolution
import dev.panini.analysis.KriyaQualificationKind
import dev.panini.analysis.particleRoles
import dev.panini.analysis.akarakaReason
import dev.panini.analysis.karakaLabel
import dev.panini.analysis.participialRelations
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

    @Test fun `a sentence may end with a danda and no space`() {
        assertEquals(Karaka.KARTR, (VyakaranamExecutionAdapter.analyzeGrammar(parser.parse("राम + सुँ गम् + लट् + तिप्।"))!!.frames.single()
            .relations.single().resolution as FrameKarakaResolution.Resolved).karaka)
    }

    @Test fun `roots spelled like affixes parse`() {
        for (root in listOf("वस्", "लुप्", "सिच्", "अट्", "शप्", "मस्", "तस्")) {
            assertEquals(root, frame("राम + सुँ $root + लट् + तिप्").kriya?.analysis?.pada?.dhatu?.mulaDhatu)
        }
        assertEquals("गम्", frame("राम + सुँ सम् + गम् + लट् + तिप्").kriya?.analysis?.pada?.dhatu?.mulaDhatu, "सम् + root stays a prefix.")
    }

    @Test fun `future participles carry their time`() {
        val future = frame("बालक + सुँ पठँ + लृट् + शतृ + सुँ गम् + लृट् + तिप्")
        assertEquals(listOf(dev.panini.analysis.ParticipialTime.SUBSEQUENT), future.participialRelations.map { it.time })
        val absolute = frame("गो + सुप् दुहँ + लृट् + शानच् + सुप् राम + सुँ गम् + लट् + तिप्")
        assertEquals(listOf(dev.panini.analysis.ParticipialTime.SUBSEQUENT), absolute.participialRelations.filter { it.absolute }.map { it.time })
    }

    @Test fun `the lakara gives a participle its time and only sat affixes take one`() {
        assertEquals(listOf(dev.panini.analysis.ParticipialTime.SIMULTANEOUS),
            frame("बालक + सुँ पठँ + शतृ + सुँ गम् + लट् + तिप्").participialRelations.map { it.time })
        kotlin.test.assertFails { parser.parse("गम् + लृट् + क्त + ङि राम + सुँ गम् + लट् + तिप् ।") }
        kotlin.test.assertFails { parser.parse("पठँ + स्य + शतृ + सुँ गम् + लट् + तिप् ।") }
    }

    @Test fun `a prefix alone parses as karmapravacaniya`() {
        val frame = frame("पर्जन्य + सुँ जप + अम् अनु प्र + वृष् + लङ् + तिप्")
        assertEquals("प्र", frame.kriya?.analysis?.pada?.upasargas?.single())
        assertEquals(Karaka.KARTR, karaka("पर्जन्य + सुँ जप + अम् अनु प्र + वृष् + लङ् + तिप्", "पर्जन्य+सुँ"))
    }

    private fun printed(path: String) = dev.panini.execution.PaniniVM().evalScript(java.io.File(path).readText(Charsets.UTF_8))
        .filterIsInstance<dev.panini.execution.ExecutionResult.Success>().filter { it.outputKind == dev.panini.execution.OutputKind.CONSOLE }.map { it.value }

    @Test fun `demos print what they claim`() {
        assertEquals(listOf("रामः", "नदी", "ज्ञानम्"), printed("examples/linguistic/subanta.pvm"))
        assertEquals(listOf("चतुःपञ्चाशत्"), printed("examples/arithmetic/geometric_progression.pvm"))
        assertEquals(listOf("दशन्"), printed("examples/taddhita_structs/struct_mukhya.pvm"), "The stored value, shown as its stem.")
        val faces = setOf("एकम्", "द्वे", "त्रीणि", "चत्वारि", "पञ्च", "षट्")
        repeat(10) { assertTrue(printed("examples/arithmetic/random_demo.pvm").single() in faces) }
    }

    @Test fun `a vikarana the lakara decides is not written`() {
        for (source in listOf("भू + स्य + लृट् + तिप्", "भू + तास् + लुट् + तिप्", "भू + च्लि + लुङ् + तिप्", "भू + सिच् + लुङ् + तिप्")) {
            kotlin.test.assertFails(source) { parser.parse("$source ।") }
        }
        assertEquals(dev.panini.core.DhatuGana.DIVADI, frame("अस्मद् + सुँ मनँ + श्यन् + लट् + इट्").kriya?.dhatu?.gana)
    }

    @Test fun `free word order keeps these links`() {
        assertEquals(listOf("शास्त्र+अम्"), frame("पठँ + तुमुँन् शास्त्र + अम् विद्यालय + अम् गम् + लट् + तिप्").purposeKarakas.map { it.relation.participant.pada.sourceText })
        assertEquals(listOf("2.3.19"), frame("राम + टा सीता + सुँ सह गम् + लट् + तिप्").relations.single { it.participant.pada.sourceText == "राम+टा" }.evidence.map { it.sutra })
        assertEquals(listOf("वन+अम्"), frame("वन + अम् राम + ङि गम् + शतृ + ङि सीता + सुँ अनु + सृ + शप् + लट् + तिप्").nominalKarakas.map { it.relation.participant.pada.sourceText })
    }

    @Test fun `a non binding reading stays out of remembered operands`() {
        val frame = frame("राम + सुँ हस्त + टा डुकृञ् + लट् + तिप्")
        assertEquals(Karaka.KARANA, karaka("राम + सुँ हस्त + टा डुकृञ् + लट् + तिप्", "हस्त+टा"))
        assertTrue(frame.diagnostics.any { it.code == FrameDiagnosticCode.NON_BINDING_READING })
    }

    private fun reason(source: String, pada: String) = frame(source).let { f ->
        f.akarakaReason(f.relations.single { it.participant.pada.sourceText == pada }) }

    @Test fun `the display label names the reason`() {
        val f = frame("देवदत्त + सुँ अध्यापक + सुँ अस् + लट् + तिप्")
        assertEquals(listOf("कर्तृ", "कर्तृ-विधेय"), f.relations.map { f.karakaLabel(it) })
    }

    @Test fun `an akaraka word says why`() {
        assertEquals("सञ्ज्ञा", reason("एक + सुँ द्वि + औ इति दत्तसूची + सुँ नाम सूची + सुँ असँ + लट् + तिप्", "दत्तसूची+सुँ"))
        assertEquals("विधेय", reason("देवदत्त + सुँ अध्यापक + सुँ अस् + लट् + तिप्", "अध्यापक+सुँ"))
        assertEquals("अत्यन्तसंयोग", reason("राम + सुँ मास + अम् अधि + इङ् + लट् + त", "मास+अम्"))
        assertEquals("विभक्त", reason("माथुर + जस् पाटलीपुत्रक + भ्यस् आढ्यतर + जस् अस् + लट् + झि", "पाटलीपुत्रक+भ्यस्"))
        assertEquals(null, reason("राम + सुँ गम् + लट् + तिप्", "राम+सुँ"))
    }

    @Test fun `a method owner and an action noun means with karoti read as programs write them`() {
        val f = frame("गुण + मतुप् + ङस् वृध् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप्")
        assertEquals(listOf("2.3.50"), f.relations.single { it.participant.pada.sourceText == "गुण+मतुप्+ङस्" }.evidence.map { it.sutra })
        assertEquals(FrameKarakaResolution.Resolved(Karaka.KARANA), f.relations.single { it.participant.pada.sourceText == "वृध्+ल्युट्+टा" }.resolution)
    }

    private fun written(source: String) = VyakaranamExecutionAdapter.analyzeAsWritten(parser.parse("$source ।"))!!.frames.single()
        .relations.associate { it.participant.pada.sourceText.replace(" ", "") to (it.resolution as? FrameKarakaResolution.Resolved)?.karaka }

    @Test fun `comparison sentences still compute`() {
        fun truth(condition: String) = dev.panini.execution.PaniniVM().evalScript("यदि $condition तर्हि सत्य + सुँ असँ + लट् + तिप् अन्यथा असत्य + सुँ असँ + लट् + तिप् ।")
            .filterIsInstance<dev.panini.execution.ExecutionResult.Success>().last().value
        assertEquals("सत्यम्", truth("दशन् + सुँ द्वि + भ्याम् अधिक + सुँ असँ + लट् + तिप्"))
        assertEquals("असत्यम्", truth("द्वि + सुँ दशन् + भ्यस् अधिक + सुँ असँ + लट् + तिप्"))
        assertEquals("सत्यम्", truth("द्वि + सुँ दशन् + भ्यस् न्यून + सुँ असँ + लट् + तिप्"))
        assertEquals("सत्यम्", truth("द्वि + सुँ द्वि + भ्याम् सम + सुँ असँ + लट् + तिप्"))
    }

    @Test fun `no word in any program source lacks a karaka`() {
        val nominal = Regex("(?:[^\\s+।॥#]+ \\+ )+(?:सुँ|औ|जस्|अम्|औट्|शस्|टा|भ्याम्|भिस्|ङे|भ्यस्|ङसिँ|ङस्|ओस्|आम्|ङि|सुप्)(?=\\s|$)")
        val uncovered = mutableListOf<String>()
        val unexplained = mutableListOf<String>()
        val found = listOf(java.io.File("examples"), java.io.File("projects")).flatMap { it.walkTopDown().filter { f -> f.extension == "pvm" }.toList() }.flatMap { file ->
            val source = file.readText(Charsets.UTF_8)
            // A procedure's body is program text like any other, and the editor analyses it too.
            val frames = parser.parseDocument(source).items.flatMap { item -> when (item) {
                is dev.panini.vyakaranam.ast.Ukti -> listOf(item)
                is dev.panini.vyakaranam.ast.Prakriya -> item.body.map { dev.panini.vyakaranam.ast.Ukti(it.sourceText, body = it) }
                else -> emptyList()
            } }
                .flatMap { VyakaranamExecutionAdapter.analyzeAsWritten(it)?.frames.orEmpty() }
            // A word with a stated reason (विधेय, विशेषण, निर्धारण…) is analysed truly; only an unexplained one fails.
            val bad = frames.flatMap { f -> f.relations.filter {
                it.resolution !is FrameKarakaResolution.Resolved ||
                    ((it.resolution as FrameKarakaResolution.Resolved).karaka == Karaka.ANIRDHARITA && f.akarakaReason(it) == null)
            }.map { r -> file.name + ": " + r.participant.pada.sourceText } }
            // A written word with no analysis at all must fail too, or a whole clause could escape.
            val seen = frames.flatMap { f -> f.relations + f.nominalKarakas.map { it.relation } + f.purposeKarakas.map { it.relation } }
                .map { it.participant.pada.sourceText.replace(" ", "") }.toSet()
                .plus(VyakaranamExecutionAdapter.nonKriyaWords(parser.parseDocument(source)).map { it.text })
            val readings = VyakaranamExecutionAdapter.nonKriyaWords(parser.parseDocument(source))
            unexplained += readings.filter { it.reading !in dev.panini.analysis.NonKriyaWords.allowedReadings }.map { file.name + ": " + it.text + " " + it.reading }
            val written = source.lines().filterNot { it.trimStart().startsWith("#") }.joinToString("\n")
            uncovered += nominal.findAll(written).map { it.value.replace(" ", "") }.filter { w -> w !in seen && seen.none { w in it } }.map { file.name + ": " + it }.toSet()
            bad
        }
        java.io.File("build").mkdirs(); java.io.File("build/uncovered.txt").writeText(uncovered.joinToString("\n"), Charsets.UTF_8)
        assertEquals(emptyList(), uncovered)
        assertEquals(emptyList(), unexplained)
        assertEquals(emptyList(), found)
    }

    @Test fun `a loop header is analysed as the action of taking`() {
        val header = VyakaranamExecutionAdapter.analyzeAsWritten(parser.parse("दत्तसूची + ङस् एकैक + अम् सदस्य + अम् तद् + ङस् क्रम + अम् तद् + ङस् उत्तर + अम् च ग्रह् + क्त्वा क्रमशः क्रम + अम् मुद्र् + णिच् + लोट् + सिप् ।"))!!.frames.first()
        val roles = header.relations.associate { it.participant.pada.sourceText.replace(" ", "") to (it.resolution as FrameKarakaResolution.Resolved).karaka }
        assertEquals(Karaka.KARMAN, roles["सदस्य+अम्"]); assertEquals(Karaka.KARMAN, roles["क्रम+अम्"]); assertEquals(Karaka.KARMAN, roles["उत्तर+अम्"])
        assertEquals(Karaka.SAMBANDHA, roles["दत्तसूची+ङस्"]); assertEquals(Karaka.SAMBANDHA, roles["तद्+ङस्"])
    }

    @Test fun `words outside any action carry their true status`() {
        fun readings(source: String) = VyakaranamExecutionAdapter.nonKriyaWords(parser.parseDocument(source)).associate { it.text to it.reading }
        val list = readings("एक + सुँ द्वि + औ इति दत्तसूची + सुँ नाम सूची + सुँ असँ + लट् + तिप् ।")
        assertEquals("कर्तृ-समानाधिकरण", list["एक+सुँ"]); assertEquals("कर्तृ-समानाधिकरण", list["द्वि+औ"])
        val declaration = readings("गणित + ङस् गण + ल्युट् + सुँ नाम अपवाद + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।\nद्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥")
        assertEquals("सम्बन्ध", declaration["गणित+ङस्"]); assertEquals("कर्तृ-समानाधिकरण: सञ्ज्ञा", declaration["गण+ल्युट्+सुँ"])
        assertEquals("कर्तृ-विशेषण", declaration["अपवाद+सुँ"]); assertEquals(Karaka.KARTR.sanskritName, declaration["प्रक्रिया+सुँ"])
    }

    @Test fun `a named procedure header shows its roles`() {
        val header = "परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।"
        for (source in listOf(header, "$header\nद्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥", "# note\n$header\nद्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥")) {
            val document = parser.parseDocument(source)
            val words = VyakaranamExecutionAdapter.nonKriyaWords(document)
            assertEquals("कर्तृ-समानाधिकरण: सञ्ज्ञा", words.single { it.text == "परीक्षण+सुँ" }.reading)
            assertEquals(Karaka.KARTR.sanskritName, words.single { it.text == "प्रक्रिया+सुँ" }.reading)
            assertTrue(words.single { it.text == "परीक्षण+सुँ" }.start == source.indexOf("परीक्षण"))
        }
    }

    @Test fun `a verbless sentence is rejected`() {
        assertTrue(runCatching { parser.parseDocument("सङ्ख्या + सुँ परिणाम + सुँ ।") }.isFailure)
    }

    @Test fun `a declaration verb has its karta in a real frame`() {
        val declarations = listOf(java.io.File("examples"), java.io.File("projects")).flatMap { it.walkTopDown().filter { f -> f.extension == "pvm" }.toList() }
            .flatMap { file -> file.readLines(Charsets.UTF_8).map { it.trim() }.filter { !it.startsWith("#") && (" इति " in it || " नाम " in it) && listOf("प्रक्रिया + सुँ असँ + लट् + तिप् ।", "अधिकार + सुँ असँ + लट् + तिप् ।", "सीमा + सुँ असँ + लट् + तिप् ।").any { end -> it.endsWith(end) } } }.distinct()
        assertTrue(declarations.isNotEmpty())
        val wrong = declarations.filter { line ->
            val frame = VyakaranamExecutionAdapter.analyzeAsWritten(parser.parse(line))?.frames?.lastOrNull()
            frame?.relations?.none { (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == Karaka.KARTR } != false
        }
        assertEquals(emptyList(), wrong)
    }

    @Test fun `every written frequency word is read as qualifying its action`() {
        val frequency = Regex("(?:[^\\s+।॥#]+ \\+ )+(?:कृत्वसुच्|सुच्)(?=\\s|$|।|॥)")
        val missing = listOf(java.io.File("examples"), java.io.File("projects"), java.io.File("../examples"), java.io.File("../projects")).filter { it.exists() }
            .flatMap { it.walkTopDown().filter { f -> f.extension == "pvm" }.toList() }.flatMap { file ->
                val source = file.readText(Charsets.UTF_8)
                val words = VyakaranamExecutionAdapter.nonKriyaWords(parser.parseDocument(source)).filter { it.reading == dev.panini.analysis.NonKriyaWords.ADVERB && it.start >= 0 }.map { it.start }.toSet()
                val code = source.replace(Regex("#[^\\r\\n]*")) { " ".repeat(it.value.length) }
                frequency.findAll(code).filter { it.range.first !in words }.map { file.name + ": " + it.value }.toList()
            }
        assertEquals(emptyList(), missing)
    }

    @Test fun `a bare nominative before prakriya stays the subject`() {
        val frame = VyakaranamExecutionAdapter.analyzeAsWritten(parser.parse("गण + ल्युट् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।"))!!.frames.last()
        val roles = frame.relations.associate { it.participant.pada.sourceText.replace(" ", "") to (it.resolution as FrameKarakaResolution.Resolved).karaka }
        assertEquals(Karaka.KARTR, roles["गण+ल्युट्+सुँ"]); assertEquals(Karaka.ANIRDHARITA, roles["प्रक्रिया+सुँ"])
    }

    @Test fun `a written shap on a causative keeps the root's own gana`() {
        for (written in listOf("जन् + णिच् + लट् + तिप् ।", "जन् + णिच् + शप् + लट् + तिप् ।")) {
            val head = ((parser.parse(written).body as dev.panini.vyakaranam.ast.Invocation).vakya as dev.panini.vyakaranam.ast.AkhyataVakya).tinganta
            assertEquals("जनीँ", DhatuCache.resolve(head)?.upadesha, written)
        }
    }
}

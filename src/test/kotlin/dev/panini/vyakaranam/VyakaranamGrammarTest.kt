package dev.panini.vyakaranam

import dev.panini.core.Karaka
import dev.panini.core.Linga
import dev.panini.core.PadaType
import dev.panini.parser.VyakaranamLexer
import dev.panini.parser.VyakaranamParser
import dev.panini.dhatupatha.bhvadi.GamDhatu
import dev.panini.dhatupatha.bhvadi.PalayDhatu
import dev.panini.dhatupatha.juhotyadi.DaDhatu
import dev.panini.dhatupatha.tudadi.LikhDhatu
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.core.NominalCategory
import dev.panini.core.Prayoga
import dev.panini.vyakaranam.analysis.ParticipantRelationInferrer
import dev.panini.core.Vibhakti
import dev.panini.vyakaranam.analysis.AnalyzedSamuccita
import dev.panini.vyakaranam.analysis.DhatuIdentity
import dev.panini.vyakaranam.analysis.KarakaRuleContext
import dev.panini.vyakaranam.analysis.KarakaRuleEngine
import dev.panini.vyakaranam.analysis.ParticipantFacts
import dev.panini.vyakaranam.analysis.SemanticRelation
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class VyakaranamGrammarTest {

    @Test
    fun `parses an annotated finite sentence`() {
        assertParsesUkti("राम + सुँ फल + अम् खाद् + लट् + तिप् ।")
    }

    @Test
    fun `parses derivationally annotated nominal morphology`() {
        assertParsesUkti("प्र + कृ + क्त + मतुप् + ङीप् + सुँ अस्ति + लट् + तिप् ।")
    }

    @Test
    fun `parses sanadi morphology without treating it as surface Sanskrit`() {
        assertParsesUkti("राम + सुँ भू + णिच् + सन् + लट् + तिप् ।")
    }

    @Test
    fun `parses explicit compound sup deletion`() {
        assertParsesUkti("राजन् + ङस् + लुक्-पुरुष + सुँ भू + लट् + तिप् ।")
    }

    @Test
    fun `coordinated subantas participate in karaka analysis`() {
        val lexicon = InMemoryVyakaranamLexicon(
            pratipadikas = listOf(
                PratipadikaEntry("राम", setOf(Linga.PUMS)),
                PratipadikaEntry("लक्ष्मण", setOf(Linga.PUMS)),
            ),
            dhatus = listOf(
                GamDhatu(),
            ),
        )
        val analysis = PaniniyaVyakaranamEngine(lexicon).analyze(
            "राम + सुँ, लक्ष्मण + औ च गम् + लट् + झि ।",
        ).vakyas.single()

        assertIs<AnalyzedSamuccita>(analysis.padaAnalyses.first())
        assertEquals(2, analysis.karakas.count { it.karaka == Karaka.KARTR })
    }

    @Test
    fun `vakya analysis resolves bhyam from dhatu semantics with sutra trace`() {
        val lexicon = InMemoryVyakaranamLexicon(
            pratipadikas = listOf(
                PratipadikaEntry("राम", setOf(Linga.PUMS)),
                PratipadikaEntry("पुस्तक", setOf(Linga.NAPUMSAKA)),
                PratipadikaEntry("लेखनी", setOf(Linga.STRI)),
            ),
            dhatus = listOf(
                DaDhatu(),
                LikhDhatu(),
                PalayDhatu(),
            ),
        )
        val engine = PaniniyaVyakaranamEngine(lexicon)

        fun assignment(source: String) = engine.analyze(source).vakyas.single().karakas
            .single { it.pada.sup.text == "भ्याम्" }

        val recipient = assignment("राम + भ्याम् पुस्तक + अम् दा + लट् + तिप् ।")
        assertEquals(Karaka.SAMPRADANA, recipient.karaka)
        assertTrue("1.4.32" in recipient.reason && "2.3.13" in recipient.reason)

        val instrument = assignment("लेखनी + भ्याम् लिख् + लट् + तिप् ।")
        assertEquals(Karaka.KARANA, instrument.karaka)
        assertTrue("1.4.42" in instrument.reason && "2.3.18" in instrument.reason)

        val source = assignment("राम + भ्याम् पलाय् + लट् + तिप् ।")
        assertEquals(Karaka.APADANA, source.karaka)
        assertTrue("1.4.24" in source.reason && "2.3.28" in source.reason)
    }

    @Test
    fun `multi-participant sentence disambiguates distinct ambiguous syncretic arguments`() {
        val lexicon = InMemoryVyakaranamLexicon(
            pratipadikas = listOf(
                PratipadikaEntry("राम", setOf(Linga.PUMS)),
                PratipadikaEntry("पुस्तक", setOf(Linga.NAPUMSAKA)),
            ),
            dhatus = listOf(DaDhatu()),
        )
        val engine = PaniniyaVyakaranamEngine(lexicon)
        val vakyas = engine.analyze("राम + भ्याम् पुस्तक + अम् दा + लट् + तिप् ।").vakyas.single()

        val recipient = vakyas.karakas.single { it.pada.sourceText.startsWith("राम") }
        assertEquals(Karaka.SAMPRADANA, recipient.karaka)
    }

    @Test
    fun `verifies general karaka sutras and explicit anabhihite governing behavior`() {
        val possible = setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA, Vibhakti.TRTIYA, Vibhakti.SAPTAMI)

        // 1.4.45 (Adhikarana)
        val locContext = KarakaRuleContext(
            dhatu = DhatuIdentity("स्था"),
            participant = ParticipantFacts("p1", AvyayaPada("गृहे", "गृहे"), possible, setOf(SemanticRelation.LOCATION)),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
        )
        val locRes = KarakaRuleEngine.resolve(locContext)
        assertEquals(Karaka.ADHIKARANA, locRes.resolved)
        assertTrue(locRes.evidence.any { it.sutra == "1.4.45" })

        // 1.4.49 (Karma)
        val objContext = KarakaRuleContext(
            dhatu = DhatuIdentity("कृ"),
            participant = ParticipantFacts("p2", AvyayaPada("कटम्", "कटम्"), possible, setOf(SemanticRelation.DESIRED_OBJECT)),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
        )
        val objRes = KarakaRuleEngine.resolve(objContext)
        assertEquals(Karaka.KARMAN, objRes.resolved)
        assertTrue(objRes.evidence.any { it.sutra == "1.4.49" })
        assertTrue(objRes.evidence.any { it.sutra == "2.3.2" })

        // 2.3.1 (Anabhihite - Abhihita Karma in Karmani Prayoga gets Prathama via 2.3.1 blocking)
        val abhihitaObjContext = KarakaRuleContext(
            dhatu = DhatuIdentity("कृ"),
            participant = ParticipantFacts("p3", AvyayaPada("कटः", "कटः"), setOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA), setOf(SemanticRelation.DESIRED_OBJECT)),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARMANI,
        )
        val abhihitaRes = KarakaRuleEngine.resolve(abhihitaObjContext)
        assertEquals(Karaka.KARMAN, abhihitaRes.resolved)
        assertTrue(abhihitaRes.evidence.any { it.sutra == "2.3.1" })

        // 1.4.54 (Svatantrah Karta)
        val agentContext = KarakaRuleContext(
            dhatu = DhatuIdentity("पच्"),
            participant = ParticipantFacts("p4", AvyayaPada("देवदत्तभ्", "देवदत्तः"), possible, setOf(SemanticRelation.INDEPENDENT_AGENT)),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
        )
        val agentRes = KarakaRuleEngine.resolve(agentContext)
        assertEquals(Karaka.KARTR, agentRes.resolved)
        assertTrue(agentRes.evidence.any { it.sutra == "1.4.54" })

        // 1.4.55 (Tat Prayojako Hetus Ca)
        val causeContext = KarakaRuleContext(
            dhatu = DhatuIdentity("कारयति"),
            participant = ParticipantFacts("p5", AvyayaPada("यज्ञदत्तः", "यज्ञदत्तः"), possible, setOf(SemanticRelation.PROMPTER_CAUSE)),
            allParticipants = emptyList(),
            prayoga = Prayoga.CAUSATIVE,
        )
        val causeRes = KarakaRuleEngine.resolve(causeContext)
        assertEquals(Karaka.KARTR, causeRes.resolved)
        assertTrue(causeRes.evidence.any { it.sutra == "1.4.55" })
    }

    @Test
    fun `verifies dynamic participant relation inferencing from nominal categories`() {
        val locEntry = PratipadikaEntry("गृह", setOf(Linga.NAPUMSAKA), categories = setOf(NominalCategory.PLACE_LOCATION))
        val locRelations = ParticipantRelationInferrer.infer(locEntry, setOf(Vibhakti.SAPTAMI), emptySet())
        assertTrue(SemanticRelation.LOCATION in locRelations)

        val toolEntry = PratipadikaEntry("लेखनी", setOf(Linga.STRI), categories = setOf(NominalCategory.INSTRUMENT_TOOL))
        val toolRelations = ParticipantRelationInferrer.infer(toolEntry, setOf(Vibhakti.TRTIYA), emptySet())
        assertTrue(SemanticRelation.INSTRUMENT in toolRelations)
    }

    @Test
    fun `verifies 2 3 65 kartrkarmanoh krti assigns sasthi to unexpressed kartr or karman under krt governance`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("कृ"),
            participant = ParticipantFacts(
                id = "p_krti",
                expression = AvyayaPada("कृष्णस्य", "कृष्णस्य"),
                possibleVibhaktis = setOf(Vibhakti.SASTHI),
                semanticRelations = setOf(SemanticRelation.INDEPENDENT_AGENT),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.KARTR),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertEquals(Karaka.KARTR, res.resolved)
        assertTrue(res.evidence.any { it.sutra == "2.3.65" })
    }

    @Test
    fun `verifies 1 4 51 akathitam ca assigns karma samjna to secondary object of dvikarmaka root`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("दुह्"),
            participant = ParticipantFacts(
                id = "p_gauNa",
                expression = AvyayaPada("गाम्", "गाम्"),
                possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.KARMAN),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertEquals(Karaka.KARMAN, res.resolved)
        assertTrue(res.evidence.any { it.sutra == "1 4.51" || it.sutra == "1.4.51" })
    }

    @Test
    fun `verifies 1 4 52 gati buddhi promotes non-causative agent to karma in causative`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("गमयति"),
            participant = ParticipantFacts(
                id = "p_manavaka",
                expression = AvyayaPada("माणवकम्", "माणवकम्"),
                possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.CAUSATIVE,
            candidates = setOf(Karaka.KARMAN),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertEquals(Karaka.KARMAN, res.resolved)
        assertTrue(res.evidence.any { it.sutra == "1.4.52" })
    }

    @Test
    fun `verifies 2 3 5 kaladhvanor atyantasamyoge assigns dvitiya for continuous duration`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("स्था"),
            participant = ParticipantFacts(
                id = "p_masam",
                expression = AvyayaPada("मासम्", "मासम्"),
                possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.5" })
    }

    @Test
    fun `verifies 2 3 37 yasya ca bhavena bhavalaksanam assigns saptami for absolute locative action marking`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("गम्"),
            participant = ParticipantFacts(
                id = "p_gosu",
                expression = AvyayaPada("गोषु", "गोषु"),
                possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
                semanticRelations = setOf(SemanticRelation.ACTION_MARKING),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.37" })
    }

    @Test
    fun `verifies 1 4 25 jugupsa virama pramadarthanam assigns apadana samjna`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("जुगुप्सते"),
            participant = ParticipantFacts(
                id = "p_papad",
                expression = AvyayaPada("पापात्", "पापात्"),
                possibleVibhaktis = setOf(Vibhakti.PANCHAMI),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.APADANA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertEquals(Karaka.APADANA, res.resolved)
        assertTrue(res.evidence.any { it.sutra == "1.4.25" })
    }

    @Test
    fun `verifies 2 3 19 saha yukte apradhane assigns trtiya for accompaniment`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("गच्छति"),
            participant = ParticipantFacts(
                id = "p_putrena",
                expression = AvyayaPada("पुत्रेण", "पुत्रेण"),
                possibleVibhaktis = setOf(Vibhakti.TRTIYA),
                semanticRelations = setOf(SemanticRelation.ACCOMPANIMENT),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.19" })
    }

    @Test
    fun `verifies 2 3 20 yenangavikarah assigns trtiya for body limb deformity`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_aksna",
                expression = AvyayaPada("अक्ष्णा", "अक्ष्णा"),
                possibleVibhaktis = setOf(Vibhakti.TRTIYA),
                semanticRelations = setOf(SemanticRelation.BODY_DEFORMITY),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.20" })
    }

    @Test
    fun `verifies 2 3 23 hetau assigns trtiya for motive or cause`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("वसति"),
            participant = ParticipantFacts(
                id = "p_punyena",
                expression = AvyayaPada("पुण्येन", "पुण्येन"),
                possibleVibhaktis = setOf(Vibhakti.TRTIYA),
                semanticRelations = setOf(SemanticRelation.CAUSE_HETU),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.23" })
    }

    @Test
    fun `verifies 2 3 29 anyaraditararte assigns pancami in exclusion and direction context`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_jnanat",
                expression = AvyayaPada("ज्ञानात्", "ज्ञानात्"),
                possibleVibhaktis = setOf(Vibhakti.PANCHAMI),
                semanticRelations = setOf(SemanticRelation.DIRECTIONAL_EXCLUSION),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.29" })
    }

    @Test
    fun `verifies 2 3 36 yatas ca nirdharanam assigns sasthi or saptami for group selection`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_kavimam",
                expression = AvyayaPada("कवीनाम्", "कवीनाम्"),
                possibleVibhaktis = setOf(Vibhakti.SASTHI),
                semanticRelations = setOf(SemanticRelation.GROUP_SELECTION),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.36" })
    }

    @Test
    fun `verifies 2 3 50 sasthi sese assigns sasthi for remaining relational connections`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_rajnah",
                expression = AvyayaPada("राज्ञः", "राज्ञः"),
                possibleVibhaktis = setOf(Vibhakti.SASTHI),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.50" })
    }

    @Test
    fun `verifies 1 4 53 hr kror anyatarasyam optionally assigns karma to non-causative agent in causative`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("हारयति"),
            participant = ParticipantFacts(
                id = "p_devadattam",
                expression = AvyayaPada("देवदत्तम्", "देवदत्तम्"),
                possibleVibhaktis = setOf(Vibhakti.DVITIYA),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.CAUSATIVE,
            candidates = setOf(Karaka.KARMAN),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertEquals(Karaka.KARMAN, res.resolved)
        assertTrue(res.evidence.any { it.sutra == "1.4.53" })
    }

    @Test
    fun `verifies 2 3 12 gatyartha karmani assigns caturthi or dvitiya to motion goal`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("गच्छति"),
            participant = ParticipantFacts(
                id = "p_gramaya",
                expression = AvyayaPada("ग्रामाय", "ग्रामाय"),
                possibleVibhaktis = setOf(Vibhakti.CHATURTHI),
                semanticRelations = setOf(SemanticRelation.MOTION_GOAL),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.KARMAN),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.12" })
    }

    @Test
    fun `verifies 2 3 21 itthambhutalaksane assigns trtiya to characteristic emblem`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_jatabhih",
                expression = AvyayaPada("जटाभिः", "जटाभिः"),
                possibleVibhaktis = setOf(Vibhakti.TRTIYA),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.21" })
    }

    @Test
    fun `verifies 2 3 32 prthag vina nanabhih assigns trtiya pancami or dvitiya with vina`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_jnanat",
                expression = AvyayaPada("ज्ञानात्", "ज्ञानात्"),
                possibleVibhaktis = setOf(Vibhakti.PANCHAMI),
                semanticRelations = setOf(SemanticRelation.EXCLUSION_VINA),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.32" })
    }

    @Test
    fun `verifies 2 3 38 sasthi canadare assigns sasthi or saptami in disregard context`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("प्राव्राजीत्"),
            participant = ParticipantFacts(
                id = "p_rudatah",
                expression = AvyayaPada("रुदतः", "रुदतः"),
                possibleVibhaktis = setOf(Vibhakti.SASTHI),
                semanticRelations = setOf(SemanticRelation.DISREGARD_ANADARA),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.38" })
    }

    @Test
    fun `verifies 2 3 39 swamy isvara adhipati assigns sasthi or saptami with swamin`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_gosu",
                expression = AvyayaPada("गोषु", "गोषु"),
                possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
                semanticRelations = setOf(SemanticRelation.OWNERSHIP_SWAMIN),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.39" })
    }

    @Test
    fun `verifies 2 3 16 namah svasti svaha assigns caturthi for salutation offering`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_haraye",
                expression = AvyayaPada("हरये", "हरये"),
                possibleVibhaktis = setOf(Vibhakti.CHATURTHI),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.16" })
    }

    @Test
    fun `verifies 2 3 17 manya karmani anadare assigns caturthi or dvitiya to object of man`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("मन्यते"),
            participant = ParticipantFacts(
                id = "p_trnaya",
                expression = AvyayaPada("तृणाय", "तृणाय"),
                possibleVibhaktis = setOf(Vibhakti.CHATURTHI),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.KARTARI,
            candidates = setOf(Karaka.KARMAN),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.17" })
    }

    @Test
    fun `verifies 2 3 43 sadhu nipunabhyam assigns saptami in respect context`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("अस्ति"),
            participant = ParticipantFacts(
                id = "p_matari",
                expression = AvyayaPada("मातरि", "मातरि"),
                possibleVibhaktis = setOf(Vibhakti.SAPTAMI),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.43" })
    }

    @Test
    fun `verifies 2 3 26 ktasya ca vartamane assigns sasthi for present kta agent`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("पूजि"),
            participant = ParticipantFacts(
                id = "p_rajnah",
                expression = AvyayaPada("राज्ञः", "राज्ञः"),
                semanticRelations = setOf(SemanticRelation.PRESENT_PARTICIPLE_AGENT),
                possibleVibhaktis = setOf(Vibhakti.SASTHI),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.KARTR),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.26" })
    }

    @Test
    fun `verifies 2 3 44 prasitotsukabhyam trtiya ca assigns trtiya or saptami`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("प्रसित"),
            participant = ParticipantFacts(
                id = "p_harina",
                expression = AvyayaPada("हरिणा", "हरिणा"),
                semanticRelations = setOf(SemanticRelation.ENGROSSED_ATTACHMENT),
                possibleVibhaktis = setOf(Vibhakti.TRTIYA),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.ANIRDHARITA),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.44" })
    }

    @Test
    fun `verifies 2 3 52 adhigarthadayesam karmani assigns sasthi for memory object`() {
        val context = KarakaRuleContext(
            dhatu = DhatuIdentity("स्मृ"),
            participant = ParticipantFacts(
                id = "p_matuh",
                expression = AvyayaPada("मातुः", "मातुः"),
                semanticRelations = setOf(SemanticRelation.MEMORY_OR_RULING_OBJECT),
                possibleVibhaktis = setOf(Vibhakti.SASTHI),
            ),
            allParticipants = emptyList(),
            prayoga = Prayoga.ANIRDHARITA,
            candidates = setOf(Karaka.KARMAN),
        )
        val res = KarakaRuleEngine.resolve(context)
        assertTrue(res.evidence.any { it.sutra == "2.3.52" })
    }


    @Test
    fun `verifies 1 1 5 kngiti ca blocks guna and vrddhi for kit ngit affix`() {
        val context = dev.panini.vyakaranam.analysis.ProhibitionContext(
            targetSutraNumber = "1.1.2",
            affixItMarkers = setOf('ङ'),
        )
        val res = dev.panini.vyakaranam.analysis.NishedhaRuleEngine.evaluateProhibition(context)
        assertTrue(res is dev.panini.vyakaranam.analysis.NishedhaRuleResult.Blocked && res.blockerSutraNumber == "1.1.5")
    }

    @Test
    fun `verifies 1 1 6 didhivevitam blocks guna and vrddhi for it augment`() {
        val context = dev.panini.vyakaranam.analysis.ProhibitionContext(
            targetSutraNumber = "1.1.2",
            isDidhiVeviOrItAugment = true,
        )
        val res = dev.panini.vyakaranam.analysis.NishedhaRuleEngine.evaluateProhibition(context)
        assertTrue(res is dev.panini.vyakaranam.analysis.NishedhaRuleResult.Blocked && res.blockerSutraNumber == "1.1.6")
    }

    @Test
    fun `verifies 1 1 10 najjhalau blocks savarna samjna between vowel and consonant`() {
        val context = dev.panini.vyakaranam.analysis.ProhibitionContext(
            targetSutraNumber = "1.1.9",
            targetPhonemeIsVowel = true,
            secondPhonemeIsConsonant = true,
        )
        val res = dev.panini.vyakaranam.analysis.NishedhaRuleEngine.evaluateProhibition(context)
        assertTrue(res is dev.panini.vyakaranam.analysis.NishedhaRuleResult.Blocked && res.blockerSutraNumber == "1.1.10")
    }

    @Test
    fun `verifies 1 2 4 na ktva set blocks kit status for set ktva affix`() {
        val context = dev.panini.vyakaranam.analysis.ProhibitionContext(
            targetSutraNumber = "KIT_STATUS",
            isSetKtvaAffix = true,
        )
        val res = dev.panini.vyakaranam.analysis.NishedhaRuleEngine.evaluateProhibition(context)
        assertTrue(res is dev.panini.vyakaranam.analysis.NishedhaRuleResult.Blocked && res.blockerSutraNumber == "1.2.4")
    }


    private fun assertParsesUkti(source: String) {
        val errors = mutableListOf<String>()
        val listener = object : BaseErrorListener() {
            override fun syntaxError(
                recognizer: Recognizer<*, *>?,
                offendingSymbol: Any?,
                line: Int,
                charPositionInLine: Int,
                msg: String?,
                e: RecognitionException?,
            ) {
                errors += "$line:$charPositionInLine ${msg.orEmpty()}"
            }
        }

        val lexer = VyakaranamLexer(CharStreams.fromString(source)).apply {
            removeErrorListeners()
            addErrorListener(listener)
        }
        val parser = VyakaranamParser(CommonTokenStream(lexer)).apply {
            removeErrorListeners()
            addErrorListener(listener)
        }

        parser.ukti()

        assertEquals(emptyList(), errors)
    }
}

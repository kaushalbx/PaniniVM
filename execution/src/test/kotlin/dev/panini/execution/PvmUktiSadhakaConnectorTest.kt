package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PvmUktiSadhakaConnectorTest {
    @Test
    fun `prefixed nonfinite rendering preserves an optional h assimilation branch`() {
        val rendered = PvmUktiSadhaka().sadhayaLine(
            "सूची + ङस् अन्तिम + अम् उद् + हृ + ल्यप् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        assertTrue(rendered.contains("उद्हृत्य") || rendered.contains("उद्धृत्य"), rendered)
        assertFalse(rendered.contains("ल्यप्"), rendered)
        assertEquals("उद्धृत्य", dev.panini.derivation.SandhiEngine().joinPrefix("उद्", "हृत्य"))
    }

    @Test
    fun `lexical ordinal renders as an adjective rather than a cardinal`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("प्रथमे ।", renderer.sadhayaLine("प्रथम + ङि ।"))
        assertEquals("प्रथमम् ।", renderer.sadhayaLine("प्रथम + अम् ।"))
    }

    @Test
    fun `feminine agreement does not affix an already feminine lexical noun again`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val renderer = PvmUktiSadhaka()
        for ((source, expected) in listOf(
            "नदी + अम् ।" to "नदीम्",
            "नदी + टा ।" to "नद्या",
            "देवी + ङस् ।" to "देव्याः",
        )) {
            val noun = parser.parse(source).grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            assertEquals(expected, renderer.sadhayaSubanta(noun, dev.panini.core.Linga.STRI), source)
        }
        assertEquals("एका नदी गुप्ता अस्ति ।", renderer.sadhayaLine(
            "एक + सुँ नदी + सुँ गुप्त + सुँ असँ + लट् + तिप् ।",
        ))
    }

    @Test
    fun `unlicensed explicit tap retains complete source`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val renderer = PvmUktiSadhaka()
        for (source in listOf("कर्तृ + टाप् + ङस् ।", "लता + टाप् + सुँ ।", "धन + मतुप् + टाप् + सुँ ।")) {
            val noun = parser.parse(source).grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            assertEquals(noun.sourceText, renderer.sadhayaSubanta(noun), source)
        }
    }

    @Test
    fun `possessive feminine source retains the full case and number paradigm`() {
        val expected = """
            धनवती धनवत्यौ धनवत्यः धनवतीम् धनवत्यौ धनवतीः
            धनवत्या धनवतीभ्याम् धनवतीभिः धनवत्यै धनवतीभ्याम् धनवतीभ्यः
            धनवत्याः धनवतीभ्याम् धनवतीभ्यः धनवत्याः धनवत्योः धनवतीनाम्
            धनवत्याम् धनवत्योः धनवतीषु
        """.trim().split(Regex("\\s+"))
        val renderer = PvmUktiSadhaka()
        val slots = dev.panini.core.SupAffix.entries
        assertEquals(slots.size, expected.size)
        slots.zip(expected).forEach { (slot, surface) ->
            val sourceAffix = if (slot == dev.panini.core.SupAffix.NGASI) "ङसिँ" else slot.upadesha
            assertEquals("$surface ।", renderer.sadhayaLine("धन + मतुप् + ङीप् + $sourceAffix ।"), slot.name)
        }
    }

    @Test
    fun `nin source requires supported lexical licensing`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val unsupported = parser.parse("बाल + ङीन् + सुँ ।").grammaticalVakyas().single().padas
            .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        val renderer = PvmUktiSadhaka()
        assertEquals(unsupported.sourceText, renderer.sadhayaSubanta(unsupported))
        assertEquals("ब्राह्मणी ।", renderer.sadhayaLine("ब्राह्मण + ङीन् + सुँ ।"))
    }

    @Test
    fun `explicit possessive feminine affix retains licensing provenance`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("धनवती ।", renderer.sadhayaLine("धन + मतुप् + ङीप् + सुँ ।"))
        assertEquals("सङ्ख्यावती ।", renderer.sadhayaLine("सङ्ख्या + मतुप् + ङीप् + सुँ ।"))
        val noun = dev.panini.vyakaranam.parser.PaniniParser()
            .parse("धन + मतुप् + सुँ ।").grammaticalVakyas().single().padas
            .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        assertEquals("धनवती", renderer.sadhayaSubanta(noun, dev.panini.core.Linga.STRI))
    }

    @Test
    fun `aa final noun case rendering does not require sankhya spelling repairs`() {
        val renderer = PvmUktiSadhaka()
        for ((stem, genitive, locative) in listOf(
            Triple("सङ्ख्या", "सङ्ख्यायाः", "सङ्ख्यायाम्"),
            Triple("लता", "लतायाः", "लतायाम्"),
            Triple("बन्दिसङ्ख्या", "बन्दिसङ्ख्यायाः", "बन्दिसङ्ख्यायाम्"),
        )) {
            assertEquals("$genitive ।", renderer.sadhayaLine("$stem + ङस् ।"))
            assertEquals("$locative ।", renderer.sadhayaLine("$stem + ङि ।"))
        }
    }

    @Test
    fun `aa final noun instrumental uses nominal derivation`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("सङ्ख्यया ।", renderer.sadhayaLine("सङ्ख्या + टा ।"))
        assertEquals("लतया ।", renderer.sadhayaLine("लता + टा ।"))
    }

    @Test
    fun `copular fallback does not override an explicit homonymous gana`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("अस्ति ।", renderer.sadhayaLine("असँ + लट् + तिप् ।"))
        assertFalse(renderer.sadhayaLine("असँ + शप् + लट् + तिप् ।") == "अस्ति ।")
        assertFalse(renderer.sadhayaLine("असुँ + श्यन् + लट् + तिप् ।") == "अस्ति ।")
    }

    @Test
    fun `possessive source renders instrumental and locative through the engine`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("सङ्ख्यावता ।", renderer.sadhayaLine("सङ्ख्या + मतुप् + टा ।"))
        assertEquals("सङ्ख्यावति ।", renderer.sadhayaLine("सङ्ख्या + मतुप् + ङि ।"))
    }

    @Test
    fun `renderer does not silently collapse repeated possessive affixes`() {
        val noun = dev.panini.vyakaranam.parser.PaniniParser()
            .parse("धन + मतुप् + मतुप् + ङस् ।").grammaticalVakyas().single().padas
            .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        assertEquals(noun.sourceText, PvmUktiSadhaka().sadhayaSubanta(noun))
    }

    @Test
    fun `compound rendering retains its outer case and number`() {
        val renderer = PvmUktiSadhaka()
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val members = listOf("राज + ङस् ।", "पुरुष + सुँ ।").map { source ->
            val noun = parser.parse(source).grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            dev.panini.vyakaranam.ast.SamasaAnga(noun.sourceText, noun.pratipadika, noun.sup)
        }
        val compound = dev.panini.vyakaranam.ast.SamasaPratipadika("", members)
        val noun = dev.panini.vyakaranam.ast.SubantaPada("", compound,
            dev.panini.vyakaranam.ast.SupPratyaya("ङस्", "ङस्"))
        assertEquals("राजपुरुषस्य", renderer.sadhayaSubanta(noun))
        assertEquals("राजपुरुषौ", renderer.sadhayaSubanta(noun.copy(
            sup = dev.panini.vyakaranam.ast.SupPratyaya("औ", "औ"))))
    }

    @Test
    fun `failed compound derivation retains the complete source pada`() {
        val source = "अज्ञातसमास + ङस्"
        val noun = dev.panini.vyakaranam.ast.SubantaPada(
            source,
            dev.panini.vyakaranam.ast.SamasaPratipadika("अज्ञातसमास", emptyList()),
            dev.panini.vyakaranam.ast.SupPratyaya("ङस्", "ङस्"),
        )
        assertEquals(source, PvmUktiSadhaka().sadhayaSubanta(noun))
    }

    @Test
    fun `unresolved derived nouns retain root affixes prefixes and case ending`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val renderer = PvmUktiSadhaka()
        for (source in listOf(
            "सम् + अज्ञातधातु + णिच् + ल्युट् + ङस् ।",
            "भू + शतृ + सुँ ।",
        )) {
            val noun = parser.parse(source).grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            assertEquals(noun.sourceText, renderer.sadhayaSubanta(noun))
        }
    }

    @Test
    fun `coordinated derived nouns retain the supplied gender`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val members = listOf("भू", "कृ").map { root ->
            parser.parse("$root + क्त + सुँ ।").grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        }
        val coordinated = dev.panini.vyakaranam.ast.SamuccitaSubanta("", members)
        assertEquals("भूता कृता च", PvmUktiSadhaka().sadhayaPada(coordinated, dev.panini.core.Linga.STRI))
    }

    @Test
    fun `derived participles preserve requested gender and grammatical number`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val renderer = PvmUktiSadhaka()
        fun render(ending: String, gender: dev.panini.core.Linga): String {
            val noun = parser.parse("भू + क्त + $ending ।")
                .grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            return renderer.sadhayaSubanta(noun, gender)
        }
        assertEquals("भूतः", render("सुँ", dev.panini.core.Linga.PUMS))
        assertEquals("भूतम्", render("सुँ", dev.panini.core.Linga.NAPUMSAKA))
        assertEquals("भूता", render("सुँ", dev.panini.core.Linga.STRI))
        assertEquals("भूते", render("औ", dev.panini.core.Linga.NAPUMSAKA))
        assertEquals("भूतानि", render("जस्", dev.panini.core.Linga.NAPUMSAKA))
    }

    @Test
    fun `selection noun derives its stem and retains case morphology`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("चयनम् ।", renderer.sadhayaLine("चिञ् + ल्युट् + सुँ ।"))
        assertEquals("चयनस्य ।", renderer.sadhayaLine("चिञ् + ल्युट् + ङस् ।"))
    }

    @Test
    fun `derived indeclinables use the krdanta engine for every root`() {
        val renderer = PvmUktiSadhaka()
        val engine = dev.panini.derivation.KrdantaEngine()
        for (root in listOf("वृज्", "पच्")) {
            val expected = engine.deriveSourceStem(root, "क्त्वा", listOf("णिच्")).surface
            assertEquals("$expected ।", renderer.sadhayaLine("$root + णिच् + क्त्वा ।"))
        }
    }

    @Test
    fun `copular fallback does not erase a derivational affix`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("अस्ति ।", renderer.sadhayaLine("असँ + लट् + तिप् ।"))
        assertFalse(renderer.sadhayaLine("असँ + णिच् + लट् + तिप् ।") == "अस्ति ।")
    }

    @Test
    fun `imperatives use derived forms with and without prefixes`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("कुरु ।", renderer.sadhayaLine("कृ + उ + लोट् + सिप् ।"))
        assertEquals("गृहाण ।", renderer.sadhayaLine("ग्रहँ + श्ना + लोट् + सिप् ।"))
        assertEquals("सङ्गृहाण ।", renderer.sadhayaLine("सम् + ग्रहँ + श्ना + लोट् + सिप् ।"))
    }

    @Test
    fun `ordinary verbs retain explicit atmanepada ending`() {
        val renderer = PvmUktiSadhaka()
        assertEquals("पचते ।", renderer.sadhayaLine("डुपचँष् + लट् + त ।"))
        assertEquals("पचति ।", renderer.sadhayaLine("डुपचँष् + लट् + तिप् ।"))
    }

    @Test
    fun `ordinary stha and causative stha use distinct derived stems`() {
        assertEquals("तिष्ठ", PvmUktiSadhaka().sadhayaLine("स्था + शप् + लोट् + सिप् ।").removeSuffix(" ।"))
        assertEquals("स्थापय", PvmUktiSadhaka().sadhayaLine("स्था + णिच् + लोट् + सिप् ।").removeSuffix(" ।"))
    }
    @Test
    fun `failed verbal derivation retains prefixes affixes and ending`() {
        val verb = dev.panini.vyakaranam.parser.PaniniParser()
            .parse("सम् + अज्ञातधातु + णिच् + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas
            .filterIsInstance<dev.panini.vyakaranam.ast.TingantaPada>().single()
        assertEquals(verb.sourceText, PvmUktiSadhaka().sadhayaTinganta(verb))
    }
    @Test
    fun `renderer does not replace an explicit incompatible gana`() {
        val verb = dev.panini.vyakaranam.parser.PaniniParser()
            .parse("स्था + श्नु + लोट् + सिप् ।")
            .grammaticalVakyas().single().padas
            .filterIsInstance<dev.panini.vyakaranam.ast.TingantaPada>().single()
        assertEquals(verb.sourceText, PvmUktiSadhaka().sadhayaTinganta(verb))
    }
    @Test
    fun `source numeral boundary retains una construction during declension`() {
        val pada = dev.panini.vyakaranam.ast.SankhyaPada(
            sourceText = "एक ऊन विंशति + अम्",
            stems = listOf("एक", "ऊन", "विंशति"),
            sup = dev.panini.vyakaranam.ast.SupPratyaya("अम्", "अम्"),
        )
        assertEquals("एकोनविंशतिम्", PvmUktiSadhaka().sadhayaSankhya(pada))
        assertEquals("एकोनविंशतिः समा अस्ति ।", PvmUktiSadhaka().sadhayaLine(
            "एक + ऊन + विंशति + सुँ सम + सुँ असँ + लट् + तिप् ।",
        ))
    }
    @Test
    fun `predicate agrees with lexical gender of numeral noun subject`() {
        assertEquals("विंशतिः समा अस्ति ।", PvmUktiSadhaka().sadhayaLine(
            "विंशति + सुँ सम + सुँ असँ + लट् + तिप् ।",
        ))
        assertEquals("शतं समम् अस्ति ।", PvmUktiSadhaka().sadhayaLine(
            "शत + सुँ सम + सुँ असँ + लट् + तिप् ।",
        ))
    }
    @Test
    fun `higher numeral nouns retain feminine singular declension`() {
        for ((source, expected) in listOf(
            "विंशति + अम् ।" to "विंशतिम्",
            "त्रिंशत् + अम् ।" to "त्रिंशतम्",
            "पञ्चाशत् + सुँ ।" to "पञ्चाशत्",
        )) {
            val noun = dev.panini.vyakaranam.parser.PaniniParser()
                .parse(source).grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            assertEquals(expected, PvmUktiSadhaka().sadhayaSubanta(noun), source)
        }
    }
    @Test
    fun `failed numeral declension retains the supplied case and number`() {
        val noun = dev.panini.vyakaranam.parser.PaniniParser()
            .parse("द्वि + सुँ ।").grammaticalVakyas().single().padas
            .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        assertEquals(noun.sourceText, PvmUktiSadhaka().sadhayaSubanta(noun))
    }

    @Test
    fun `unsupported feminine suffixes retain their segmented provenance`() {
        for (suffix in listOf("डाप्", "चाप्", "ऊङ्", "तिच्", "ङीप्", "टाप् + ङीप्")) {
            val noun = dev.panini.vyakaranam.parser.PaniniParser()
                .parse("बाल + $suffix + सुँ ।").grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            assertEquals(noun.sourceText, PvmUktiSadhaka().sadhayaSubanta(noun), suffix)
        }
    }

    @Test
    fun `explicit stri affix governs numeral and predicate agreement`() {
        assertEquals("बाला", dev.panini.derivation.StriPratyayaEngine().derive(
            dev.panini.derivation.StriPratyayaRequest("बाल"),
        ).final.surface)
        assertEquals("बाला", dev.panini.derivation.SubantaEngine().derive(
            dev.panini.derivation.SubantaDerivationRequest("बाला", dev.panini.core.Vibhakti.PRATHAMA,
                dev.panini.core.Vacana.EKAVACANA, dev.panini.core.Linga.STRI, dev.panini.derivation.NominalStemFormation.AP),
        ).final.surface)
        assertEquals(
            "एका बाला गुप्ता अस्ति ।",
            PvmUktiSadhaka().sadhayaLine("एक + सुँ बाल + टाप् + सुँ गुप्त + सुँ असँ + लट् + तिप् ।"),
        )
    }

    @Test
    fun `predicate inherits the gender of the noun counted by its numeral subject`() {
        assertEquals(
            "एका लता गुप्ता अस्ति ।",
            PvmUktiSadhaka().sadhayaLine("एक + सुँ लता + सुँ गुप्त + सुँ असँ + लट् + तिप् ।"),
        )
        assertEquals(
            "एका सूची समा अस्ति ।",
            PvmUktiSadhaka().sadhayaLine("एक + सुँ सूची + सुँ सम + सुँ असँ + लट् + तिप् ।"),
        )
    }

    @Test
    fun `copular predicate agrees with a neuter numeral subject`() {
        assertEquals(
            "शून्यं सङ्ख्यायाः न्यूनम् अस्ति ।",
            PvmUktiSadhaka().sadhayaLine("शून्य + सुँ सङ्ख्या + ङसिँ न्यून + सुँ असँ + लट् + तिप् ।"),
        )
    }

    @Test
    fun `neuter accusative numeral three renders with retroflex nasal`() {
        assertEquals("त्रीणि सङ्गृहाण ।", PvmUktiSadhaka().sadhayaLine("त्रि + शस् सम् + ग्रहँ + श्ना + लोट् + सिप् ।"))
    }

    @Test
    fun `each prefix boundary receives consonant sandhi`() {
        assertEquals(
            "सन्निक्षिप ।",
            PvmUktiSadhaka().sadhayaLine("सम् + नि + क्षिप् + लोट् + सिप् ।"),
        )
    }

    @Test
    fun `final member extraction renders assimilated ud plus hr`() {
        assertEquals(
            "सूच्याः अन्तिमम् उद्धर ।",
            PvmUktiSadhaka().sadhayaLine("सूची + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप् ।"),
        )
    }

    @Test
    fun `sam plus grah renders as grammatical sangrhana`() {
        assertEquals(
            "एकं द्वे च सङ्गृहाण ।",
            PvmUktiSadhaka().sadhayaLine("एक + अम् द्वि + औट् च सम् + ग्रहँ + श्ना + लोट् + सिप् ।"),
        )
    }

    @Test
    fun `locative membership renders as an existential sentence`() {
        assertEquals(
            "फलं सूच्याम् अस्ति ।",
            PvmUktiSadhaka().sadhayaLine("फल + सुँ सूची + ङि असँ + लट् + तिप् ।"),
        )
    }

    @Test
    fun `instrumental collection companion renders with causative yuj`() {
        assertEquals(
            "पूर्वसूचीम् उत्तरसूच्या संयोजय ।",
            PvmUktiSadhaka().sadhayaLine(
                "पूर्वसूची + अम् उत्तरसूची + टा सम् + युज् + णिच् + लोट् + सिप् ।",
            ),
        )
    }

    @Test
    fun `collection cardinality renders as ordinary transitive counting`() {
        assertEquals(
            "सूचीं गणय ।",
            PvmUktiSadhaka().sadhayaLine("सूची + अम् गण् + णिच् + लोट् + सिप् ।"),
        )
    }

    @Test
    fun `collection slice renders with ordinal paryanta limits`() {
        assertEquals(
            "सूच्याः द्वितीयात् तृतीयपर्यन्तम् अंशं गृहाण ।",
            PvmUktiSadhaka().sadhayaLine(
                "सूची + ङस् द्वि + तीय + ङसिँ त्रि + तीय + शस् परि + अन्त + अम् " +
                    "अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
            ),
        )
    }
    private val sadhaka = PvmUktiSadhaka()

    @Test
    fun `surface rendering preserves sequence connectors instead of inserting dandas`() {
        val rendered = sadhaka.sadhayaLine(
            "राम + सुँ भू + लट् + तिप् च फल + अम् खाद् + लट् + तिप् ।",
        )

        assertTrue(" च " in rendered, rendered)
        assertFalse(" । " in rendered.removeSuffix(" ।"), rendered)
        assertEquals(1, rendered.count { it == '।' }, rendered)
    }

    @Test
    fun `conditional result pipeline does not show a danda before tatah`() {
        val rendered = sadhaka.sadhayaLine(
            "यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् " +
                "तर्हि लघु अन्यथा गुरु ततः मुद्र् + लोट् + सिप् ।",
        )

        assertTrue(" ततः " in rendered, rendered)
        assertFalse("। ततः" in rendered, rendered)
        assertFalse(" दा" in rendered, rendered)
        assertEquals(1, Regex("ततः").findAll(rendered).count(), rendered)
        assertTrue("लघु" in rendered && "गुरु" in rendered, rendered)
    }

    @Test
    fun `segmented nominal conditional result is rendered from its retained AST`() {
        val rendered = sadhaka.sadhayaLine(
            "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
                "तर्हि विजय + सुँ अन्यथा गुरु ततः फल + अम् मुद्र् + लोट् + सिप् ।",
        )

        assertTrue("तर्हि विजयः" in rendered, rendered)
        assertFalse("विजय+सुँ" in rendered, rendered)
        assertFalse("+" in rendered, rendered)
    }

    @Test
    fun `traditional numeral code words render with their grammatical case`() {
        val katapayadi = sadhaka.sadhayaLine(
            "कटपयादि माधव + अम् कटपयादि खग + अम् च युज् + णिच् + लोट् + सिप् ।",
        )
        val aryabhatiya = sadhaka.sadhayaLine(
            "आर्यभटीय गि + अम् आर्यभटीय चयि + अम् च गण् + णिच् + लोट् + सिप् ।",
        )

        assertFalse("+" in katapayadi, katapayadi)
        assertFalse("+" in aryabhatiya, aryabhatiya)
        assertTrue("माधवं" in katapayadi && "खगं" in katapayadi, katapayadi)
    }

    @Test
    fun `possessive matup on feminine aa stem renders the vat form`() {
        assertEquals(
            "सङ्ख्यावतः मूल्यं पञ्चाशद् अस्ति ।",
            sadhaka.sadhayaLine(
                "सङ्ख्या + मतुप् + ङस् मूल्य + सुँ पञ्चाशत् + सुँ असँ + लट् + तिप् ।",
            ),
        )
    }

    @Test
    fun `lyut action nouns render as neuter`() {
        assertEquals("गणनम् ।", sadhaka.sadhayaLine("गण + ल्युट् + सुँ ।"))
        assertEquals(
            "एकं गणनेन कुरु ।",
            sadhaka.sadhayaLine("एक + अम् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।"),
        )
    }

    @Test
    fun `script rendering parses a conditional split across physical lines`() {
        val rendered = sadhaka.sadhayaScript(
            """
            यदि सर्वजय + सुँ एक + टा सम + सुँ असँ + लट् + तिप्
                तर्हि जय + अम् मुद्र् + लोट् + सिप्
                अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।
            """.trimIndent(),
        )

        assertEquals(
            "यदि सर्वजयः एकेन समः अस्ति तर्हि जयं मुद्रय अन्यथा पराजयं मुद्रय ।",
            rendered,
        )
        assertFalse("+" in rendered, rendered)
    }

    @Test
    fun `explicit feminine predicate suffix is preserved in readable Sanskrit`() {
        val rendered = sadhaka.sadhayaLine(
            "यदि स्वसङ्ख्याप्राप्ति + सुँ सत्य + टा सम + टाप् + सुँ असँ + लट् + तिप् " +
                "तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।",
        )

        assertEquals(
            "यदि स्वसङ्ख्याप्राप्तिः सत्येन समा अस्ति तर्हि जयं मुद्रय अन्यथा पराजयं मुद्रय ।",
            rendered,
        )
    }

    @Test
    fun `script rendering parses a pipeline split across physical lines`() {
        val rendered = sadhaka.sadhayaScript(
            """
            क्रमाङ्क + अम् एक + अम् च युज् + णिच् + लोट् + सिप्
                ततः क्रमाङ्क + ङे दा + लोट् + सिप्
                ततः मुद्र् + लोट् + सिप् फल + अम् ।
            """.trimIndent(),
        )

        assertEquals(
            "क्रमाङ्कम् एकं च योजय ततः क्रमाङ्काय देहि ततः मुद्रय फलम् ।",
            rendered,
        )
        assertFalse("+" in rendered, rendered)
    }

    @Test
    fun `locative state placement and collection insertion render as natural imperatives`() {
        assertEquals(
            "सत्यं सर्वजये स्थापय ।",
            sadhaka.sadhayaLine(
                "सत्य + अम् सर्वजय + ङि स्था + णिच् + लोट् + सिप् ।",
            ),
        )
        assertEquals(
            "चयनं पेटिकाक्रमे निक्षिप ।",
            sadhaka.sadhayaLine(
                "चयन + अम् पेटिकाक्रम + ङि नि + क्षिप् + लोट् + सिप् ।",
            ),
        )
        assertEquals(
            "फलं बन्दिसङ्ख्यायां स्थापय ।",
            sadhaka.sadhayaLine(
                "फल + अम् बन्दिसङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।",
            ),
        )
    }

    @Test
    fun `indexed retrieval renders source position and object compositionally`() {
        assertEquals(
            "पेटिकाक्रमात् पेटिकाक्रमाङ्के मूल्यं गृहाण ।",
            sadhaka.sadhayaLine(
                "पेटिकाक्रम + ङसिँ पेटिकाक्रमाङ्क + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
            ),
        )
    }

    @Test
    fun `exclusion absolutive renders as a natural subordinate action`() {
        assertEquals(
            "पेटिकाक्रमं वर्जयित्वा चिनु ।",
            sadhaka.sadhayaLine(
                "पेटिकाक्रम + अम् वृज् + णिच् + क्त्वा चिञ् + श्नु + लोट् + सिप् ।",
            ),
        )
    }
}

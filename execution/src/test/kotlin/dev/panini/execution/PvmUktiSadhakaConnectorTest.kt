package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PvmUktiSadhakaConnectorTest {
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
        for (suffix in listOf("डाप्", "चाप्", "ऊङ्", "तिच्", "टाप् + ङीप्")) {
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

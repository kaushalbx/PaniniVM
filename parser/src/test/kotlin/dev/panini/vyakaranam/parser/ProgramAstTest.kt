package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.ConditionalRelation
import dev.panini.vyakaranam.ast.ForEach
import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.NamaVakya
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SequenceConnector
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProgramAstTest {
    @Test fun `productive ordinals retain native morphology in explicit coordination`() {
        val node = assertIs<Invocation>(PaniniParser().parse(
            "प्रथ् + अमच् + टाप् + अम् सङ्ख्या + अम् च द्वि + तीय + टाप् + अम् सङ्ख्या + अम् च युज् + णिच् + लोट् + सिप् ।").body)
        val group = node.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SamuccitaSubanta>().single()
        assertEquals(listOf(2, 2), group.conjunctSizes)
        assertEquals(listOf(listOf("प्रथ्", "अमच्"), listOf("द्वि", "तीय")),
            group.conjuncts().map { assertIs<SankhyaPuranaPada>(it.first()).stems })
        assertEquals(listOf(dev.panini.core.StriAffix.TAP, dev.panini.core.StriAffix.TAP),
            group.conjuncts().map { assertIs<SankhyaPuranaPada>(it.first()).striAffix })
    }

    @Test fun `explicit coordination preserves distinct agent and object case roles`() {
        val node = assertIs<Invocation>(PaniniParser().parse(
            "राम + सुँ च श्याम + सुँ च एक + अम् द्वि + औट् च युज् + णिच् + लट् + तस् ।").body)
        val groups = node.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SamuccitaSubanta>()
        assertEquals(2, groups.size)
        assertEquals(listOf(1, 1), groups.first().conjunctSizes)
        assertEquals(emptyList(), groups.last().conjunctSizes)
        assertEquals(listOf("राम", "श्याम"), groups.first().members.map { (assertIs<SubantaPada>(it).pratipadika as MulaPratipadika).text })
        assertEquals(listOf("एक", "द्वि"), groups.last().members.map { (assertIs<SubantaPada>(it).pratipadika as MulaPratipadika).text })
    }
    @Test fun `repeated cha preserves multiword nominal conjuncts`() {
        val source = "प्रथम + टाप् + अम् सङ्ख्या + अम् च द्वितीय + टाप् + अम् सङ्ख्या + अम् च युज् + णिच् + लोट् + सिप् ।"
        val node = assertIs<Invocation>(PaniniParser().parse(source).body)
        val group = node.vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SamuccitaSubanta>().single()
        assertEquals(listOf(2, 2), group.conjunctSizes)
        assertEquals(4, group.members.size)
        assertEquals(listOf("प्रथम", "द्वितीय"), group.conjuncts().map {
            (assertIs<SubantaPada>(it.first()).pratipadika as MulaPratipadika).text
        })
    }
    @Test fun `productive ordinal retains a typed feminine affix separately from numeric stems`() {
        val invocation = assertIs<Invocation>(PaniniParser().parse(
            "द्वि + तीय + टाप् + अम् सङ्ख्या + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        val ordinal = assertIs<SankhyaPuranaPada>(invocation.vakya.padas.first())
        assertEquals(listOf("द्वि", "तीय"), ordinal.stems)
        assertEquals(dev.panini.core.StriAffix.TAP, ordinal.striAffix)
        assertEquals(listOf(dev.panini.core.SupAffix.AM), ordinal.sup.affixCandidates)
        val bare = assertIs<Invocation>(PaniniParser().parse("द्वि + तीय + अम् मुद्र् + णिच् + लोट् + सिप् ।").body)
        assertNull(assertIs<SankhyaPuranaPada>(bare.vakya.padas.first()).striAffix)
        assertFailsWith<PaniniParseException> {
            PaniniParser().parse("द्वि + तीय + टाप् + टाप् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        }
    }
    @Test
    fun `amach is parsed as first ordinal morphology`() {
        val invocation = assertIs<Invocation>(
            PaniniParser().parse("प्रथ् + अमच् + अम् मुद्र् + लोट् + सिप् ।").body,
        )
        val ordinal = assertIs<SankhyaPuranaPada>(invocation.vakya.padas.first())
        assertEquals(listOf("प्रथ्", "अमच्"), ordinal.stems)
    }
    private val parser = PaniniParser()

    @Test
    fun `kryadi affix uses shna and rejects the unsupported alias`() {
        val verb = assertIs<AkhyataVakya>(assertIs<Invocation>(parser.parse(
            "ग्रहँ + श्ना + लोट् + सिप् ।",
        ).body).vakya).tinganta
        assertEquals(dev.panini.vyakaranam.ast.Vikarana.SHNA, verb.vikarana)
        assertFailsWith<PaniniParseException> {
            parser.parse("ग्रहँ + श्नाम् + लोट् + सिप् ।")
        }
    }

    @Test
    fun `block terminator has a distinct token without changing utterance acceptance`() {
        val lexer = dev.panini.parser.VyakaranamLexer(
            org.antlr.v4.kotlinruntime.CharStreams.fromString("। ॥ ."),
        )
        assertEquals(dev.panini.parser.VyakaranamLexer.Tokens.DANDA, lexer.nextToken().type)
        assertEquals(dev.panini.parser.VyakaranamLexer.Tokens.DOUBLE_DANDA, lexer.nextToken().type)
        assertEquals(dev.panini.parser.VyakaranamLexer.Tokens.DANDA, lexer.nextToken().type)
        for (terminator in listOf("।", "॥", ".", "")) {
            assertIs<Invocation>(parser.parse("एक + अम् मुद्र् + णिच् + लोट् + सिप् $terminator").body)
        }
        assertFailsWith<PaniniParseException> {
            parser.parse("एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥ द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।")
        }
    }

    @Test
    fun `clause rules can build adjacent native AST nodes without consuming document EOF`() {
        val tokens = org.antlr.v4.kotlinruntime.CommonTokenStream(
            dev.panini.parser.VyakaranamLexer(org.antlr.v4.kotlinruntime.CharStreams.fromString(
                "एक + अम् मुद्र् + णिच् + लोट् + सिप् । " +
                    "द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।",
            )),
        )
        val grammar = dev.panini.parser.VyakaranamParser(tokens)
        val builder = VyakaranamAstBuilder()
        val first = builder.build(grammar.utterance())
        assertIs<Invocation>(first.body)
        assertEquals(dev.panini.parser.VyakaranamLexer.Tokens.DANDA, tokens.LA(1))
        tokens.consume()
        val second = builder.build(grammar.utterance())
        assertIs<Invocation>(second.body)
        assertEquals(dev.panini.parser.VyakaranamLexer.Tokens.DANDA, tokens.LA(1))
        tokens.consume()
        assertEquals(org.antlr.v4.kotlinruntime.Token.EOF, tokens.LA(1))
        assertEquals(false, first.sourceText.contains("<EOF>"))
        assertEquals(false, second.sourceText.contains("<EOF>"))
    }

    @Test
    fun `every grammar vikarana retains typed upadesha identity`() {
        for (affix in dev.panini.vyakaranam.ast.Vikarana.entries) {
            val invocation = assertIs<Invocation>(
                parser.parse("भू + ${affix.upadesha} + लट् + तिप् ।").body,
            )
            assertEquals(affix, assertIs<AkhyataVakya>(invocation.vakya).tinganta.vikarana)
        }
        assertFailsWith<IllegalStateException> {
            dev.panini.vyakaranam.ast.Vikarana.fromUpadesha("ना")
        }
    }

    @Test
    fun `segmented paryanta construction becomes a typed inclusive range`() {
        val invocation = assertIs<Invocation>(
            parser.parse(
                "एक + ङसिँ दशन् + शस् परि + अन्त + अम् सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ।",
            ).body,
        )

        val range = assertIs<ParyantaRangePada>(invocation.vakya.padas.first())
        assertEquals(listOf("एक"), range.lowerLimit.stems)
        assertEquals("ङसिँ", range.lowerLimit.sup.text)
        assertEquals(listOf("दशन्"), range.upperLimit.stems)
        assertEquals("शस्", range.upperLimit.sup.text)
        assertEquals("पर्यन्त", assertIs<MulaPratipadika>(range.marker.pratipadika).text)
        assertEquals(dev.panini.vyakaranam.ast.Vikarana.SHNU, assertIs<AkhyataVakya>(invocation.vakya).tinganta.vikarana)
    }

    @Test
    fun `paryanta construction retains ordinal boundary morphology`() {
        val invocation = assertIs<Invocation>(
            parser.parse(
                "सूची + ङस् द्वि + तीय + ङसिँ त्रि + तीय + शस् परि + अन्त + अम् " +
                    "अंश + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
            ).body,
        )

        val range = assertIs<ParyantaRangePada>(invocation.vakya.padas[1])
        assertEquals(listOf("द्वि", "तीय"), assertIs<SankhyaPuranaPada>(range.lowerLimit).stems)
        assertEquals("ङसिँ", range.lowerLimit.sup.text)
        assertEquals(listOf("त्रि", "तीय"), assertIs<SankhyaPuranaPada>(range.upperLimit).stems)
        assertEquals("शस्", range.upperLimit.sup.text)
    }

    @Test
    fun `sequence owns its statements and connectors`() {
        val ukti = parser.parse(
            "राम + सुँ भू + लट् + तिप् च फल + अम् खाद् + लट् + तिप् ।",
        )

        val sequence = assertIs<Sequence>(ukti.body)
        assertEquals(2, sequence.statements.size)
        assertEquals(listOf("च"), sequence.connectors)
        assertEquals(listOf(SequenceConnector.SAMUCCAYA), sequence.connectorKinds)
        sequence.statements.forEach { assertIs<Invocation>(it) }
        assertEquals(2, ukti.grammaticalVakyas().size)
    }

    @Test
    fun `tatah starts a new clause with its pre-verbal operands`() {
        val sequence = assertIs<Sequence>(
            parser.parse(
                "फल + अम् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् " +
                    "ततः एक + अम् गुणक + अम् च युज् + णिच् + लोट् + सिप् ।",
            ).body,
        )

        assertEquals(listOf("ततः"), sequence.connectors)
        assertEquals(listOf(SequenceConnector.ANANTARYA), sequence.connectorKinds)
        val second = assertIs<Invocation>(sequence.statements[1])
        assertEquals(4, second.vakya.padas.size)
        assertEquals(
            listOf("एक+अम्", "गुणक+अम्"),
            second.vakya.padas.take(2).map { it.sourceText },
        )
    }

    @Test
    fun `iti connects a quoted command to its reporting command`() {
        val ukti = parser.parse(
            "सङ्ख्या + अम् अनुमिनु + लोट् + सिप् इति मुद्र् + णिच् + लोट् + सिप् ।",
        )

        val quotation = assertIs<Quotation>(ukti.body)
        assertIs<Invocation>(quotation.quoted)
        assertIs<Invocation>(quotation.reporting)
    }

    @Test
    fun `quotation remains structured inside a longer sequence`() {
        val ukti = parser.parse(
            "राम + सुँ भू + लट् + तिप् च सङ्ख्या + अम् अनुमिनु + लोट् + सिप् " +
                "इति मुद्र् + णिच् + लोट् + सिप् ततः फल + अम् खाद् + लट् + तिप् ।",
        )

        val sequence = assertIs<Sequence>(ukti.body)
        assertIs<Invocation>(sequence.statements.first())
        assertIs<Quotation>(sequence.statements[1])
        assertIs<Invocation>(sequence.statements.last())
        assertEquals(listOf("च", "ततः"), sequence.connectors)
    }

    @Test
    fun `yavat tavat builds a bounded condition loop`() {
        val loop = assertIs<WhileLoop>(
            parser.parse(
                "पञ्च + कृत्वसुच् यावत् विजय + सुँ न तावत् प्रयत्न + अम् कृ + लोट् + सिप् ।",
            ).body,
        )

        assertEquals(listOf("पञ्च"), loop.maximumIterationStems)
        assertEquals("विजय+सुँन", loop.condition.sourceText)
        assertIs<Invocation>(loop.body)
    }

    @Test
    fun `bounded loop requires the grammatical source affix rather than a rendered form`() {
        listOf("कृत्वस्", "कृत्वः").forEach { renderedForm ->
            assertFailsWith<PaniniParseException> {
                parser.parse(
                    "पञ्च + $renderedForm यावत् विजय + सुँ न तावत् प्रयत्न + अम् कृ + लोट् + सिप् ।",
                )
            }
        }
    }

    @Test
    fun `ordinal attempt boundary builds a bounded condition loop`() {
        val loop = assertIs<WhileLoop>(
            parser.parse(
                "यावत् विजय + सुँ न भू + लट् + तिप् तावत् " +
                    "पञ्चन् + म + ङस् प्रयत्न + ङस् परि + अन्त + अम् " +
                    "प्रयत्न + अम् डुकृञ् + उ + लोट् + सिप् ।",
            ).body,
        )

        assertEquals(listOf("पञ्चन्"), loop.maximumIterationStems)
        assertEquals(3, loop.maximumBoundaryPadas.size)
    }

    @Test
    fun `bounded loop may own an exhaustion clause`() {
        val loop = assertIs<WhileLoop>(
            parser.parse(
                "द्वि + कृत्वसुच् यावत् फल + सुँ न तावत् प्रयत्न + अम् कृ + लोट् + सिप् " +
                    "अन्यथा समाप्त + अम् मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        assertIs<Invocation>(loop.exhausted)
    }

    @Test
    fun `conditional owns explicit condition and branches`() {
        val ukti = parser.parse(
            "यदि राम + सुँ भू + लट् + तिप् तर्हि फल + अम् खाद् + लट् + तिप् " +
                "अन्यथा जल + अम् पा + लट् + तिप् ।",
        )

        val conditional = assertIs<Conditional>(ukti.body)
        assertIs<Invocation>(conditional.condition)
        assertIs<Invocation>(conditional.consequent)
        assertIs<Invocation>(conditional.alternate)
        assertEquals(3, ukti.grammaticalVakyas().size)
    }

    @Test
    fun `conditional without otherwise has no alternate node`() {
        val ukti = parser.parse(
            "यदि राम + सुँ भू + लट् + तिप् तर्हि फल + अम् खाद् + लट् + तिप् ।",
        )

        assertNull(assertIs<Conditional>(ukti.body).alternate)
    }

    @Test
    fun `otherwise may contain a nested conditional`() {
        val ukti = parser.parse(
            "यदि एक + अम् एक + अम् च अस् + लोट् + सिप् तर्हि जय + अम् मुद्र् + लोट् + सिप् " +
                "अन्यथा यदि एक + अम् द्वि + अम् च नि + विद् + लोट् + सिप् तर्हि लघु + अम् मुद्र् + लोट् + सिप् " +
                "अन्यथा गुरु + अम् मुद्र् + लोट् + सिप् ।",
        )

        val outer = assertIs<Conditional>(ukti.body)
        val nested = assertIs<Conditional>(outer.alternate)
        assertIs<Invocation>(nested.consequent)
        assertIs<Invocation>(nested.alternate)
    }

    @Test
    fun `conditional result may flow to one tatah target`() {
        val conditional = assertIs<Conditional>(
            parser.parse(
                "यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् " +
                    "तर्हि लघु + अम् दा + लोट् + सिप् अन्यथा गुरु + अम् दा + लोट् + सिप् " +
                    "ततः मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        val consequent = assertIs<Sequence>(conditional.consequent)
        assertIs<Invocation>(consequent.statements.last())
        assertEquals(listOf("ततः"), consequent.connectors)
        assertIs<Sequence>(conditional.alternate)
    }

    @Test
    fun `nominal attribute pipeline has two explicit AST stages`() {
        val sequence = assertIs<Sequence>(
            parser.parse(
                "परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + भिस् ततः मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        assertEquals(listOf("ततः"), sequence.connectors)
        assertIs<NamaVakya>(assertIs<Invocation>(sequence.statements.first()).vakya)
        assertIs<AkhyataVakya>(assertIs<Invocation>(sequence.statements.last()).vakya)
    }

    @Test
    fun `attribute pipeline retains every tatah stage`() {
        val sequence = assertIs<Sequence>(
            parser.parse(
                "परिणाम + मतुप् + ङस् प्रयत्नसङ्ख्या + अम् " +
                    "ततः द्वि + अम् च गण् + णिच् + लोट् + सिप् " +
                    "ततः मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        assertEquals(3, sequence.statements.size)
        assertEquals(listOf("ततः", "ततः"), sequence.connectors)
    }

    @Test
    fun `an action pipeline may end in conditional control flow`() {
        val sequence = assertIs<Sequence>(
            parser.parse(
                "त्रि + अम् द्वि + अम् च गण् + णिच् + लोट् + सिप् " +
                    "ततः यदि फल + अम् द्वि + अम् च विद् + लोट् + सिप् " +
                    "तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        assertIs<Invocation>(sequence.statements.first())
        assertIs<Conditional>(sequence.statements.last())
        assertEquals(listOf("ततः"), sequence.connectors)
    }

    @Test
    fun `conditional consequent may contain an action pipeline ending in a nested conditional`() {
        val outer = assertIs<Conditional>(
            parser.parse(
                "यदि एक + अम् द्वि + अम् च विद् + लोट् + सिप् " +
                    "तर्हि द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् " +
                    "ततः यदि फल + सुँ पञ्चन् + टा सम + सुँ असँ + लट् + तिप् " +
                    "तर्हि जय + अम् मुद्र् + लोट् + सिप् अन्यथा पराजय + अम् मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        val consequent = assertIs<Sequence>(outer.consequent)
        assertIs<Invocation>(consequent.statements.first())
        assertIs<Conditional>(consequent.statements.last())
    }

    @Test
    fun `conditional consequent may coordinate independent actions`() {
        val conditional = assertIs<Conditional>(
            parser.parse(
                "यदि एक + अम् एक + अम् च विद् + लोट् + सिप् " +
                    "तर्हि सत्य + अम् प्राप्त + ङि स्था + णिच् + लोट् + सिप् " +
                    "अथ एक + अम् मुद्र् + णिच् + लोट् + सिप् " +
                    "अथ वि + स्था + लोट् + सिप् अन्यथा असत्य + सुँ ।",
            ).body,
        )

        val consequent = assertIs<Sequence>(conditional.consequent)
        assertEquals(3, consequent.statements.size)
        assertEquals(listOf("अथ", "अथ"), consequent.connectors)
    }

    @Test
    fun `tada sentences continue the nearest conditional consequent`() {
        val conditional = assertIs<Conditional>(
            parser.parse(
                "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
                    "तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् । " +
                    "तदा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् । " +
                    "तदा वि + स्था + लोट् + सिप् ।",
            ).body,
        )

        val consequent = assertIs<Sequence>(conditional.consequent)
        assertEquals(3, consequent.statements.size)
        assertEquals(listOf("तदा", "तदा"), consequent.connectors)
        assertEquals(listOf(SequenceConnector.TADANIM, SequenceConnector.TADANIM), consequent.connectorKinds)
    }

    @Test
    fun `danda separated alternate follows all tada consequents`() {
        val conditional = assertIs<Conditional>(
            parser.parse(
                "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
                    "तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् । " +
                    "तदा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् । " +
                    "अन्यथा त्रि + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            ).body,
        )

        assertEquals(2, assertIs<Sequence>(conditional.consequent).statements.size)
        assertIs<Invocation>(assertNotNull(conditional.alternate))
    }

    @Test
    fun `yada tada has typed temporal relation and natural continuations`() {
        val conditional = assertIs<Conditional>(parser.parse(
            "यदा एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
                "तदा एक + अम् मुद्र् + णिच् + लोट् + सिप् । " +
                "तदा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body)

        assertEquals(ConditionalRelation.TEMPORAL, conditional.relation)
        val consequent = assertIs<Sequence>(conditional.consequent)
        assertEquals(2, consequent.statements.size)
        assertEquals(listOf(SequenceConnector.TADANIM), consequent.connectorKinds)
        assertNull(conditional.alternate)
    }

    @Test
    fun `yada tada rejects an alternate branch`() {
        assertFailsWith<PaniniParseException> {
            parser.parse(
                "यदा एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
                    "तदा एक + अम् मुद्र् + णिच् + लोट् + सिप् अन्यथा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            )
        }
    }

    @Test
    fun `nested yada attaches to the nearest hypothetical arm`() {
        val outer = assertIs<Conditional>(parser.parse(
            "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
                "तर्हि यदा द्वि + सुँ द्वि + टा सम + सुँ असँ + लट् + तिप् " +
                "तदा एक + अम् मुद्र् + णिच् + लोट् + सिप् " +
                "अन्यथा द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body)

        assertEquals(ConditionalRelation.HYPOTHETICAL, outer.relation)
        assertEquals(ConditionalRelation.TEMPORAL, assertIs<Conditional>(outer.consequent).relation)
        assertIs<Invocation>(assertNotNull(outer.alternate))
    }

    @Test
    fun `result pipeline may end in a paired temporal condition`() {
        val pipeline = assertIs<Sequence>(parser.parse(
            "एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् " +
                "ततः यदा फल + सुँ त्रि + टा सम + सुँ असँ + लट् + तिप् " +
                "तदा सत्य + अम् प्राप्त + ङि स्था + णिच् + लोट् + सिप् " +
                "अथ एक + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body)

        assertEquals(SequenceConnector.ANANTARYA, pipeline.connectorKinds.single())
        val temporal = assertIs<Conditional>(pipeline.statements.last())
        assertEquals(ConditionalRelation.TEMPORAL, temporal.relation)
        val arm = assertIs<Sequence>(temporal.consequent)
        assertEquals(SequenceConnector.VAKYANTARA, arm.connectorKinds.single())
    }

    @Test
    fun `cha coordinates complete finite actions in a selected arm`() {
        val conditional = assertIs<Conditional>(parser.parse(
            "यदि एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
                "तर्हि एक + अम् मुद्र् + णिच् + लोट् + सिप् " +
                "च द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body)

        val actions = assertIs<Sequence>(conditional.consequent)
        assertTrue(actions.independentActions)
        assertEquals(listOf(SequenceConnector.SAMUCCAYA), actions.connectorKinds)
        assertEquals(2, actions.statements.size)
    }

    @Test
    fun `cha coordinates a finite action with a prior action construction`() {
        val conditional = assertIs<Conditional>(parser.parse(
            "यदा एक + सुँ एक + टा सम + सुँ असँ + लट् + तिप् " +
                "तदा सत्य + अम् प्राप्त + ङि स्था + णिच् + लोट् + सिप् " +
                "च प्रथम + अम् द्वितीय + अम् च मुद्र् + णिच् + क्त्वा स्था + लोट् + सिप् ।",
        ).body)

        val actions = assertIs<Sequence>(conditional.consequent)
        assertTrue(actions.independentActions)
        assertEquals(listOf(SequenceConnector.SAMUCCAYA), actions.connectorKinds)
        assertEquals(2, actions.statements.size)
    }

    @Test
    fun `result pipeline retains finite action cha before a prior action construction`() {
        val pipeline = assertIs<Sequence>(parser.parse(
            "प्रथममूल्य + अम् द्वितीयमूल्य + अम् च युज् + णिच् + लोट् + सिप् " +
                "ततः यदा फल + सुँ लक्ष्य + टा सम + सुँ असँ + लट् + तिप् " +
                "तदा सत्य + अम् प्राप्त + ङि स्था + णिच् + लोट् + सिप् " +
                "च प्रथमक्रम + अम् द्वितीयक्रम + अम् च मुद्र् + णिच् + क्त्वा स्था + लोट् + सिप् ।",
        ).body)

        val temporal = assertIs<Conditional>(pipeline.statements.last())
        val actions = assertIs<Sequence>(temporal.consequent)
        assertTrue(actions.independentActions)
        assertEquals(listOf(SequenceConnector.SAMUCCAYA), actions.connectorKinds)
        assertEquals(2, actions.statements.size)
    }

    @Test
    fun `top level action cha attaches immediately after the finite head`() {
        val actions = assertIs<Sequence>(parser.parse(
            "सत्य + अम् प्राप्त + ङि स्था + णिच् + लोट् + सिप् " +
                "च प्रथम + अम् द्वितीय + अम् च मुद्र् + णिच् + क्त्वा स्था + लोट् + सिप् ।",
        ).body)

        assertTrue(actions.independentActions)
        assertEquals(listOf(SequenceConnector.SAMUCCAYA), actions.connectorKinds)
        val first = assertIs<Invocation>(actions.statements.first())
        assertEquals(2, first.vakya.padas.filterIsInstance<SubantaPada>().size)
    }

    @Test
    fun `action cha after postposed operands is rejected as ambiguous`() {
        val failure = assertFailsWith<IllegalArgumentException> {
            parser.parse(
                "एक + अम् मुद्र् + णिच् + लोट् + सिप् राम + सुँ " +
                    "च द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            )
        }

        assertTrue(failure.message.orEmpty().contains("immediately follow"))
    }

    @Test
    fun `natural indexed iteration lowers to foreach member and index bindings`() {
        val loop = assertIs<ForEach>(
            parser.parse(
                "सूची + ङस् प्रत्येक + अम् सदस्य + अम् तद् + ङस् क्रम + अम् च ग्रह् + क्त्वा " +
                    "क्रम + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            ).body,
        )

        assertEquals("सूची", loop.collection.pratipadika.semanticKey())
        assertEquals("सदस्य", loop.member.pratipadika.semanticKey())
        assertEquals("क्रम", assertNotNull(loop.index).pratipadika.semanticKey())
    }

    @Test
    fun `natural indexed iteration retains previous and next member bindings`() {
        val loop = assertIs<ForEach>(parser.parse(
            "सूची + ङस् प्रत्येक + अम् सदस्य + अम् तद् + ङस् क्रम + अम् " +
                "तद् + ङस् पूर्व + अम् तद् + ङस् उत्तर + अम् च ग्रह् + क्त्वा " +
                "सदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body)

        assertEquals("पूर्व", loop.relativeSelections.single {
            it.direction == dev.panini.vyakaranam.ast.CollectionRelativeDirection.PREVIOUS
        }.binding.pratipadika.semanticKey())
        assertEquals("उत्तर", loop.relativeSelections.single {
            it.direction == dev.panini.vyakaranam.ast.CollectionRelativeDirection.NEXT
        }.binding.pratipadika.semanticKey())
    }

    @Test
    fun `natural neighbour iteration does not require an index binding`() {
        val loop = assertIs<ForEach>(parser.parse(
            "सूची + ङस् प्रत्येक + अम् सदस्य + अम् तद् + ङस् उत्तर + शस् च ग्रह् + क्त्वा " +
                "उत्तर + शस् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body)

        assertNull(loop.index)
        val next = loop.relativeSelections.single()
        assertEquals(dev.panini.vyakaranam.ast.CollectionRelativeDirection.NEXT, next.direction)
        assertEquals(dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEMS, next.cardinality)
        assertEquals("उत्तर", next.binding.pratipadika.semanticKey())
    }

    @Test
    fun `natural neighbour iteration retains feminine anaphoric morphology`() {
        val loop = assertIs<ForEach>(parser.parse(
            "सूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम् " +
                "तद् + टाप् + ङस् उत्तर + टाप् + शस् च ग्रह् + क्त्वा " +
                "उत्तर + टाप् + शस् मुद्र् + णिच् + लोट् + सिप् ।",
        ).body)

        val next = loop.relativeSelections.single()
        assertEquals("तद्", next.relation.pratipadika.semanticKey())
        assertEquals("उत्तर", next.binding.pratipadika.semanticKey())
    }

    @Test
    fun `direct relative action lowers tad uttara compound to typed pairwise iteration`() {
        val loop = assertIs<ForEach>(parser.parse(
            "सूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम् " +
                "तद् + ङस् + लुक् - उत्तर + टाप् + भिस् सङ्ख्या + भिस् " +
                "युज् + णिच् + लोट् + सिप् ।",
        ).body)

        val relative = loop.relativeSelections.single()
        assertEquals(dev.panini.vyakaranam.ast.CollectionRelativeDirection.NEXT, relative.direction)
        assertEquals(dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEMS, relative.cardinality)
        assertNotNull(loop.directRelativeAction)
        assertIs<ForEach>(loop.body)
    }

    @Test
    fun `direct relative action derives previous direction from the compound`() {
        val loop = assertIs<ForEach>(parser.parse(
            "सूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम् " +
                "तद् + ङस् + लुक् - पूर्व + टाप् + भिस् सङ्ख्या + भिस् " +
                "युज् + णिच् + लोट् + सिप् ।",
        ).body)

        assertEquals(
            dev.panini.vyakaranam.ast.CollectionRelativeDirection.PREVIOUS,
            loop.relativeSelections.single().direction,
        )
        assertEquals(
            dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEMS,
            loop.relativeSelections.single().cardinality,
        )
    }

    @Test
    fun `direct singular relative action selects exactly the adjacent item`() {
        val loop = assertIs<ForEach>(parser.parse(
            "सूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम् " +
                "तद् + ङस् + लुक् - उत्तर + टाप् + टा सङ्ख्या + टा " +
                "युज् + णिच् + लोट् + सिप् ।",
        ).body)

        assertEquals(
            dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEM,
            loop.relativeSelections.single().cardinality,
        )
        assertIs<Invocation>(loop.body)
    }

    @Test
    fun `direct relative action requires number and gender agreement`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse(
                "सूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम् " +
                    "तद् + ङस् + लुक् - उत्तर + टाप् + टा सङ्ख्या + भिस् " +
                    "युज् + णिच् + लोट् + सिप् ।",
            )
        }
        val genderFailure = assertFailsWith<IllegalArgumentException> {
            parser.parse(
                "सूची + ङस् प्रत्येक + अम् फल + अम् " +
                    "तद् + ङस् + लुक् - उत्तर + टाप् + टा फल + टा " +
                    "युज् + णिच् + लोट् + सिप् ।",
            )
        }
        assertTrue(genderFailure.message.orEmpty().contains("gender"))
    }

    @Test
    fun `direct relative action rejects a compound without the genitive luk anchor`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse(
                "सूची + ङस् प्रत्येक + टाप् + अम् सङ्ख्या + अम् " +
                    "तद् + ङस् - उत्तर + टाप् + भिस् सङ्ख्या + भिस् " +
                    "युज् + णिच् + लोट् + सिप् ।",
            )
        }
    }

    @Test
    fun `natural indexed iteration requires its semantic relation and acquisition`() {
        val body = "क्रम + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertFailsWith<IllegalArgumentException> {
            parser.parse(
                "सूची + ङस् प्रत्येक + अम् सदस्य + अम् अन्य + ङस् क्रम + अम् च ग्रह् + क्त्वा $body",
            )
        }
        assertFailsWith<IllegalArgumentException> {
            parser.parse(
                "सूची + ङस् प्रत्येक + अम् सदस्य + अम् तद् + ङस् क्रम + अम् च मुद्र् + क्त्वा $body",
            )
        }
    }


    @Test
    fun `conditional arms may be bare nominal values`() {
        val conditional = assertIs<Conditional>(
            parser.parse(
                "यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् " +
                    "तर्हि लघु अन्यथा गुरु ततः मुद्र् + लोट् + सिप् ।",
            ).body,
        )

        assertIs<Sequence>(conditional.consequent)
        assertIs<Sequence>(conditional.alternate)
        val alternateValue = assertIs<Invocation>(
            assertIs<Sequence>(conditional.alternate).statements.first(),
        )
        assertEquals("गुरु", assertIs<SubantaPada>(alternateValue.implicitValuePada).pratipadika.semanticKey())
    }

    @Test
    fun `loop may pipe its named outcome to a target`() {
        val loop = assertIs<WhileLoop>(
            parser.parse(
                "द्वि + कृत्वसुच् यावत् फल + सुँ न तावत् प्रयत्न + अम् कृ + लोट् + सिप् " +
                    "ततः परिणाम + ङे दा + लोट् + सिप् ।",
            ).body,
        )

        assertIs<Invocation>(loop.resultTarget)
    }

    @Test
    fun `purvapara syntax builds a pipeline directly`() {
        val source = "पञ्च + अम् द्वि + अम् च गणित + ङस् गुण् + ल्युट् + ङस् " +
            "गणित + ङस् रन्ध्र + ल्युट् + ङस् पूर्व + ङस् पर + ङस् एका + सुँ कृ + लोट् + सिप् ।"

        val pipeline = assertIs<Pipeline>(parser.parse(source).body)

        assertEquals(listOf("पञ्च", "द्वि"), pipeline.arguments)
        assertEquals(listOf("गुण् + ल्युट्", "रन्ध्र + ल्युट्"), pipeline.stages.map { it.operationStem })
    }

    @Test
    fun `instrumental stages joined by tatah build a pipeline directly`() {
        val source =
            "पञ्च + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + टा ततः " +
                "गणित + ङस् वि + युज् + णिच् + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।"

        val pipeline = assertIs<Pipeline>(parser.parse(source).body)

        assertEquals(listOf("पञ्च", "द्वि"), pipeline.arguments)
        assertEquals(
            listOf("पञ्च", "द्वि"),
            pipeline.argumentPadas.map { assertIs<SubantaPada>(it).pratipadika.semanticKey() },
        )
        assertEquals(listOf("गण + ल्युट्", "वि + युज् + णिच् + ल्युट्"), pipeline.stages.map { it.operationStem })
    }

    @Test
    fun `sequential pipeline stages require instrumental operations`() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse(
                "पञ्च + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + ङस् ततः " +
                    "गणित + ङस् वि + युज् + णिच् + ल्युट् + ङस् डुकृञ् + उ + लोट् + सिप् ।",
            )
        }
    }
}

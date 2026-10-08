package dev.panini.execution

import kotlin.test.*

class ListDeclarationTest {
    @Test
    fun `list declaration does not erase derivation from its head or type qualifier`() {
        for (source in listOf(
            "एक + ङस् सूची + मतुप् + सुँ असँ + लट् + तिप् ।",
            "एक + ङस् सङ्ख्या + मतुप् + आम् सूची + सुँ असँ + लट् + तिप् ।",
            "राम + ङस् शब्द + मतुप् + आम् सूची + सुँ असँ + लट् + तिप् ।",
        )) {
            val node = dev.panini.vyakaranam.parser.PaniniParser().parse(source).body
                as dev.panini.vyakaranam.ast.Invocation
            assertFailsWith<IllegalArgumentException>(source) { ListDeclarationLowering.expand(node) }
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last(), source)
        }
    }

    @Test
    fun `feminine ordinal morphology cannot qualify a neuter value object`() {
        for (ordinal in listOf("प्रथमा", "द्वितीया", "तृतीया", "प्रथम + टाप्")) {
            val results = PaniniVM().evalScript(
                "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                    "सूची + ङस् $ordinal + अम् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
            )
            assertIs<ExecutionResult.Failure>(results.last(), ordinal)
        }
    }

    @Test
    fun `ordinal object relation is independent of participant word order`() {
        for (phrase in listOf(
            "सूची + ङस् द्वितीय + अम् मूल्य + अम्",
            "मूल्य + अम् सूची + ङस् द्वितीय + अम्",
            "द्वि + तीय + अम् सूची + ङस् मूल्य + अम्",
        )) {
            val results = PaniniVM().evalScript(
                "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                    "$phrase ग्रहँ + श्ना + लोट् + सिप् ।",
            )
            assertTrue(results.all { it is ExecutionResult.Success }, "$phrase: $results")
            assertEquals(2L, assertIs<SanskritValue.Sankhya>(
                assertIs<ExecutionResult.Success>(results.last()).typedValue).value)
        }
    }

    @Test
    fun `competing ordinal modifiers are rejected rather than chosen by adjacency`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङस् प्रथम + अम् द्वितीय + अम् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
        )
        assertIs<ExecutionResult.Failure>(results.last())
    }

    @Test
    fun `ordinal object phrase selects one member rather than two objects`() {
        for (ordinal in listOf("द्वितीय", "द्वि + तीय")) {
            val results = PaniniVM().evalScript(
                "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                    "सूची + ङस् $ordinal + अम् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
            )
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals(2L, assertIs<SanskritValue.Sankhya>(
                assertIs<ExecutionResult.Success>(results.last()).typedValue).value)
        }
    }

    @Test
    fun `ordinal object phrase rejects disagreeing case and number`() {
        for (suffix in listOf("टा", "जस्")) {
            val results = PaniniVM().evalScript(
                "एक + ङस् सूची + सुँ असँ + लट् + तिप् ।\n" +
                    "सूची + ङस् प्रथम + $suffix मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
            )
            assertIs<ExecutionResult.Failure>(results.last(), suffix)
        }
    }

    @Test
    fun `bare list tracks the latest declaration while earlier explicit names remain distinct`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् क्रम + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
                "त्रि + आम् उत्तर + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
                "क्रम + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।\n" +
                "सूची + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(listOf("एक", "त्रि"), results.filterIsInstance<ExecutionResult.Success>()
            .filter { it.outputKind == OutputKind.CONSOLE }.map { it.value })
    }
    @Test
    fun `nama follows the members and bare list refers to that declaration after printing`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् द्वि + ओस् त्रि + आम् च क्रम + सुँ नाम सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
                "क्रम + ङस् प्रथम + अम् मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।\n" +
                "सूची + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप् ततः मुद्र् + णिच् + लोट् + सिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val outputs = results.filterIsInstance<ExecutionResult.Success>().filter { it.outputKind == OutputKind.CONSOLE }
        assertEquals(listOf("एक", "त्रि"), outputs.map { it.value })
    }

    @Test
    fun `nama rejects missing or disagreeing name`() {
        for (name in listOf("नाम", "क्रम + अम् नाम", "क्रम + जस् नाम")) {
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(
                "एक + ङस् $name सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।",
            ).last(), name)
        }
    }
    @Test
    fun `named list rejects nonsingular or nonnominative names without crashing`() {
        for (sup in listOf("अम्", "जस्")) {
            val results = PaniniVM().evalScript("क्रम + $sup इति एक + ङस् सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।")
            assertIs<ExecutionResult.Failure>(results.last(), sup)
        }
    }

    @Test
    fun `quoted commands are not interpreted as list names`() {
        val node = dev.panini.vyakaranam.parser.PaniniParser().parse(
            "एक + अम् मुद्र् + णिच् + लोट् + सिप् इति " +
                "द्वि + ओस् सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।",
        ).body
        assertNull(ListDeclarationLowering.expand(assertIs<dev.panini.vyakaranam.ast.Quotation>(node)))
    }
    @Test
    fun `nominal iti naming stores a typed list under its inflected referent`() {
        val results = PaniniVM().evalScript(
            "क्रम + सुँ इति एक + ङस् द्वि + ओस् च सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
                "क्रम + ङसिँ द्वि + तीय + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(2L, assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(results.last()).typedValue).value)
    }
    @Test
    fun `word qualifier declares a list of words`() {
        val results = PaniniVM().evalScript("राम + ङस् सीता + ङस् च शब्द + आम् सूची + सुँ असँ + लट् + तिप् ।")
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val value = assertIs<SanskritValue.Suchi>(assertIs<ExecutionResult.Success>(results.last()).typedValue)
        assertEquals(ListMemberType.TEXT, value.memberType)
        assertEquals(listOf("राम", "सीता"), value.items.map { assertIs<SanskritValue.Shabda>(it).text })
    }

    @Test
    fun `list declaration rejects disagreeing verbs and duplicate type qualifiers`() {
        for (source in listOf(
            "एक + ङस् सूची + सुँ असँ + लट् + तस् ।",
            "एक + ङस् सङ्ख्या + आम् शब्द + आम् सूची + सुँ असँ + लट् + तिप् ।",
            "एक + ङस् सङ्ख्या + आम् सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।",
        )) assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last(), source)
    }

    @Test
    fun `word declaration rejects numbers`() {
        assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(
            "एक + ङस् शब्द + आम् सूची + सुँ असँ + लट् + तिप् ।",
        ).last())
    }
    @Test
    fun `genitive number qualifier creates a typed list of four values`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् द्वि + ओस् त्रि + आम् चतुर् + आम् च " +
                "सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val value = assertIs<SanskritValue.Suchi>(assertIs<ExecutionResult.Success>(results.last()).typedValue)
        assertEquals(ListMemberType.NUMBER, value.memberType)
        assertEquals(listOf(1L, 2L, 3L, 4L), value.items.map { assertIs<SanskritValue.Sankhya>(it).value })
    }

    @Test
    fun `numeric declaration rejects word members`() {
        val results = PaniniVM().evalScript(
            "राम + ङस् सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।",
        )
        assertIs<ExecutionResult.Failure>(results.last())
    }

    @Test
    fun `insertion cannot remove the numeric member constraint`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् सङ्ख्या + आम् सूची + सुँ असँ + लट् + तिप् ।\n" +
                "राम + अम् सूची + ङि नि + क्षिप् + लोट् + सिप् ।",
        )
        assertIs<ExecutionResult.Failure>(results.last())
    }
    @Test
    fun `genitive members declare an ordered numeric list`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val value = assertIs<SanskritValue.Suchi>(assertIs<ExecutionResult.Success>(results.first()).typedValue)
        assertEquals(listOf(1L, 2L, 3L), value.items.map { assertIs<SanskritValue.Sankhya>(it).value })
        assertEquals(value.toDisplayText(), assertIs<ExecutionResult.Success>(results.last()).value)
    }

    @Test
    fun `single member remains a list`() {
        val results = PaniniVM().evalScript("एक + ङस् सूची + सुँ असँ + लट् + तिप् ।")
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(1, assertIs<SanskritValue.Suchi>(assertIs<ExecutionResult.Success>(results.last()).typedValue).items.size)
    }

    @Test
    fun `declared list supports existing one based selection`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङसिँ प्रथम + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(1L, assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(results.last()).typedValue).value)
    }

    @Test
    fun `declaration retains repeated word members`() {
        val results = PaniniVM().evalScript("राम + ङस् राम + ङस् च सूची + सुँ असँ + लट् + तिप् ।")
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(2, assertIs<SanskritValue.Suchi>(assertIs<ExecutionResult.Success>(results.last()).typedValue).items.size)
    }

    @Test
    fun `accusative members cannot masquerade as genitive declaration`() {
        val results = PaniniVM().evalScript("एक + अम् सूची + सुँ असँ + लट् + तिप् ।")
        assertIs<ExecutionResult.Failure>(results.last())
    }
}

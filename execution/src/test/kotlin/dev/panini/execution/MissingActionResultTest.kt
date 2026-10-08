package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MissingActionResultTest {
    @Test
    fun `procedure history karaka ordinal selects participants not its argument`() {
        val source = """
            परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
            द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
            नवन् + शस् परीक्षण + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        kotlin.test.assertTrue(results.none { it is ExecutionResult.Failure }, results.toString())
        assertEquals("एक द्वि", assertIs<ExecutionResult.Success>(results.last()).value)
    }
    @Test
    fun `invalid typed ordinal produces a language failure for parsed result references`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        for (value in listOf(0L, -1L, Long.MIN_VALUE)) {
            val parsed = parser.parse("युज् + ल्युट् + ङस् प्रथम + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।")
            val invocation = parsed.body as dev.panini.vyakaranam.ast.Invocation
            val sentence = invocation.vakya as dev.panini.vyakaranam.ast.AkhyataVakya
            val padas = sentence.padas.toMutableList()
            padas[1] = dev.panini.vyakaranam.ast.SankhyaPuranaPada(
                sourceText = "invalid typed ordinal", stems = emptyList(), value = value,
                sup = dev.panini.vyakaranam.ast.SupPratyaya("अम्", "अम्"),
            )
            val vm = PaniniVM()
            vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "invalid-order")
            val result = vm.evalParsed(
                parsed.copy(body = invocation.copy(vakya = sentence.copy(padas = padas))),
                "invalid-order", vm.defaultScope, "प्रयोक्ता", "यन्त्रम्",
            )
            assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
        }
    }
    @Test
    fun `equal looking karaka references keep their own ordering`() {
        val vm = PaniniVM()
        val key = "karaka-occurrences"
        vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = key)
        vm.eval("द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।", sessionKey = key)
        fun query(operands: String) = assertIs<ExecutionResult.Success>(vm.eval(
            "$operands मुद्र् + णिच् + लोट् + सिप् ।", sessionKey = key,
        )).value
        val first = query("युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम्")
        val second = query("युज् + ल्युट् + ङस् द्वि + तीय + अम् कर्मन् + अम्")
        val combined = query("युज् + ल्युट् + ङस् प्रथम + अम् कर्मन् + अम् " +
            "युज् + ल्युट् + ङस् द्वि + तीय + अम् कर्मन् + अम्")
        assertEquals("$first $second", combined)
    }
    @Test
    fun `karaka ordering cannot ignore modifier agreement`() {
        for (sup in listOf("सुँ", "शस्")) {
            val vm = PaniniVM()
            vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "karaka-order")
            val query = "युज् + ल्युट् + ङस् पूर्व + $sup कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val result = vm.eval(query, sessionKey = "karaka-order")
            assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
        }
    }

    @Test
    fun `agreeing karaka ordering still resolves remembered participants`() {
        val vm = PaniniVM()
        vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "karaka-order")
        val first = assertIs<ExecutionResult.Success>(vm.eval(
            "युज् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            sessionKey = "karaka-order",
        )).value
        vm.eval("द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "karaka-order")
        val previous = assertIs<ExecutionResult.Success>(vm.eval(
            "युज् + ल्युट् + ङस् पूर्व + अम् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            sessionKey = "karaka-order",
        )).value
        assertEquals(first, previous)
        val latest = assertIs<ExecutionResult.Success>(vm.eval(
            "युज् + ल्युट् + ङस् कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            sessionKey = "karaka-order",
        )).value
        kotlin.test.assertNotEquals(latest, previous)
    }

    @Test
    fun `missing karaka references never become literal operands`() {
        for (qualifier in listOf("", "पूर्व + अम्", "तृतीय + अम्")) {
            val vm = PaniniVM()
            vm.eval("नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।", sessionKey = "karaka-missing")
            val result = vm.eval(
                "युज् + ल्युट् + ङस् $qualifier कर्मन् + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                sessionKey = "karaka-missing",
            )
            assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
        }
    }

    @Test
    fun `remembered action without requested karaka reports a missing relation`() {
        val vm = PaniniVM()
        vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "karaka-absent")
        val result = vm.eval(
            "युज् + ल्युट् + ङस् करण + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            sessionKey = "karaka-absent",
        )
        assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
    }
    @Test
    fun `ordering spans earlier discourse and current utterance clauses`() {
        for ((qualifier, expected) in listOf("" to "पञ्च", "प्रथम + अम्" to "त्रीणि", "पूर्व + अम्" to "त्रीणि", "द्वि + तीय + अम्" to "पञ्च")) {
            val vm = PaniniVM()
            vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "ordering")
            val source = "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ततः " +
                "युज् + ल्युट् + ङस् $qualifier फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val ukti = dev.panini.vyakaranam.parser.PaniniParser().parse(source)
            val result = vm.evalParsed(ukti, "ordering", vm.defaultScope, "प्रयोक्ता", "यन्त्रम्")
            assertEquals(expected, assertIs<ExecutionResult.Success>(result).value)
        }
    }

    @Test
    fun `out of bounds ordinal fails across the combined discourse sequence`() {
        val vm = PaniniVM()
        vm.eval("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।", sessionKey = "ordering")
        val source = "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ततः " +
            "युज् + ल्युट् + ङस् तृतीय + अम् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val ukti = dev.panini.vyakaranam.parser.PaniniParser().parse(source)
        val result = vm.evalParsed(ukti, "ordering", vm.defaultScope, "प्रयोक्ता", "यन्त्रम्")
        assertEquals(ExecutionError.INVALID_VALUE, assertIs<ExecutionResult.Failure>(result).error)
    }
    @Test
    fun `result ordering cannot ignore case or number disagreement`() {
        for (sup in listOf("सुँ", "शस्")) {
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                "द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् पूर्व + $sup फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        }
    }
    @Test
    fun `unavailable named results never fall back to unrelated latest values`() {
        for (qualifier in listOf("", "पूर्व + अम् ", "द्वि + तीय + अम् ")) {
            val source = "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् ${qualifier}फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
            assertEquals(ExecutionError.INVALID_VALUE, failure.error)
        }
    }

    @Test
    fun `one matching action cannot satisfy a previous or second result`() {
        for (qualifier in listOf("पूर्व + अम्", "द्वि + तीय + अम्")) {
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
                "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
                "युज् + ल्युट् + ङस् $qualifier फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        }
    }

    @Test
    fun `missing result in loop condition is a failure rather than successful completion`() {
        val source = "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "द्वि + कृत्वसुच् यावत् युज् + ल्युट् + ङस् फल + सुँ त्रि + भिस् सम + सुँ असँ + लट् + तिप् " +
            "तावत् एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val failure = assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        assertEquals(ExecutionError.INVALID_VALUE, failure.error)
    }

    @Test
    fun `static grantha retains action identities across intervening turns`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n" +
            "नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।\n" +
            "युज् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertIs<dev.panini.execution.sutra.SanskritGranthaSourceCompilation.Success>(
            dev.panini.execution.sutra.SanskritGranthaSourceCompiler.compile(source,
                dev.panini.sutra.runtime.GranthaId("named-history")),
        )
        assertIs<dev.panini.execution.sutra.SanskritGranthaSourceCompilation.Invalid>(
            dev.panini.execution.sutra.SanskritGranthaSourceCompiler.compile(
                "युज् + ल्युट् + ङस् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
                dev.panini.sutra.runtime.GranthaId("missing-history")),
        )
    }
}

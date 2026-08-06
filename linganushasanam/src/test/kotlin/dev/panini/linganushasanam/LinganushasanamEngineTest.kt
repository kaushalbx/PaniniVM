package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import kotlin.test.Test
import kotlin.test.assertEquals

class LinganushasanamEngineTest {

    private val engine = LinganushasanamEngine()

    @Test
    fun `test feminine gender sutra 1 2 Abantah`() {
        val res = engine.resolve(LingaRuleContext("राजसभा"))
        assertEquals(Linga.STRI, res.linga)
        assertEquals("1.2", res.ruleId)
    }

    @Test
    fun `test neuter gender sutra 3 3 AsunIsunUsunantah`() {
        val res = engine.resolve(LingaRuleContext("हविस्"))
        assertEquals(Linga.NAPUMSAKA, res.linga)
        assertEquals("3.3", res.ruleId)
    }

    @Test
    fun `test samasa gender sutra 5 2 SaNapumsakam for avyayibhava`() {
        val res = engine.resolve(
            LingaRuleContext(
                pratipadika = "उपकृष्ण",
                padas = listOf("उप", "कृष्ण"),
                samasaType = SamasaType.AVYAYIBHAVA,
            )
        )
        assertEquals(Linga.NAPUMSAKA, res.linga)
        assertEquals("5.2", res.ruleId)
    }

    @Test
    fun `test samasa gender sutra 5 1 Paravallingam for feminine uttarapada tatpurusa`() {
        val res = engine.resolve(
            LingaRuleContext(
                pratipadika = "राजसभा",
                padas = listOf("राजन्", "सभा"),
                samasaType = SamasaType.TATPURUSA,
            )
        )
        assertEquals(Linga.STRI, res.linga)
        assertEquals("5.1", res.ruleId)
    }

    @Test
    fun `test default masculine gender rules`() {
        val res = engine.resolve(LingaRuleContext("राम"))
        assertEquals(Linga.PUMS, res.linga)
    }
}

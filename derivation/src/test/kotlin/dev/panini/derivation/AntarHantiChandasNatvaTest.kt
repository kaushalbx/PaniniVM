package dev.panini.derivation

import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** 8.4.20–8.4.28. */
class AntarHantiChandasNatvaTest {
    private val sandhi = SandhiEngine()
    private val han = DhatuPatha.all.first { it.upadesha == "हनँ" }
    private val an = DhatuPatha.all.first { it.upadesha == "अनँ" }

    private fun pada(id: String, surface: String, upadesha: String = surface, dhatuId: String? = null, samjnas: Set<Samjna> = emptySet()) =
        SandhiEngine.Pada(DerivationTerm(id, surface, TermKind.PRATIPADIKA, upadesha = upadesha, sourceDhatuId = dhatuId), samjnas = samjnas)

    private fun prefixJoin(prefix: String, verb: String, dhatuId: String, choice: Map<String, Int> = emptyMap(),
        prefixSamjnas: Set<Samjna> = setOf(Samjna.UPASARGA)) =
        sandhi.join(pada("l", prefix, samjnas = prefixSamjnas), pada("r", verb, dhatuId = dhatuId),
            DerivationConfig(alternativeChoices = choice), environments = setOf(DerivationalEnvironment.KRIYAYOGA))

    @Test fun `8_4_20 and 8_4_21 for an`() {
        val pran = prefixJoin("प्र", "अन्", an.id)
        assertEquals("प्राण्", pran.final.surface)
        assertTrue("8.4.20" in pran.final.appliedSutras)
        assertEquals("प्राणिति", sandhi.joinPrefix("प्र", "अनिति", an))
        assertEquals("प्राणिणिषति", sandhi.joinPrefix("प्र", "अनिनिषति", an))
    }

    @Test fun `8_4_22 han after a`() {
        assertEquals("प्रहण्यते", sandhi.joinPrefix("प्र", "हन्यते", han))
        assertEquals("प्रघ्नन्ति", sandhi.joinPrefix("प्र", "घ्नन्ति", han))
        assertEquals("प्रहन्ति", sandhi.joinPrefix("प्र", "हन्ति", han))
        assertEquals("प्रहणन", KrdantaEngine().derive(KrdantaDerivationRequest("हन्", Samjna.LYUT, upasarga = "प्र")).final.surface)
    }

    @Test fun `8_4_23 optional before va and ma`() {
        assertEquals("प्रहन्मः", sandhi.joinPrefix("प्र", "हन्मः", han))
        assertEquals("प्रहण्मः", prefixJoin("प्र", "हन्मः", han.id, mapOf("8.4.23" to 0)).final.surface)
        assertEquals("प्रहण्वः", prefixJoin("प्र", "हन्वः", han.id, mapOf("8.4.23" to 0)).final.surface)
    }

    @Test fun `8_4_24 and 8_4_25 after antar`() {
        assertEquals("अन्तर्हन्यते", sandhi.joinPrefix("अन्तर्", "हन्यते", han))
        assertEquals("अन्तर्हण्यते", prefixJoin("अन्तर्", "हन्यते", han.id, mapOf("8.4.24" to 0), emptySet()).final.surface)
        assertEquals("अन्तरयन", sandhi.joinSamasaMembers("अन्तर्", "अयन").final.surface)
        val ayana = sandhi.join(pada("l", "अन्तर्"), pada("r", "अयन"), DerivationConfig(alternativeChoices = mapOf("8.4.25" to 0)))
        assertEquals("अन्तरयण", ayana.final.surface)
    }

    @Test fun `vedic rules need chandas`() {
        val vedic = setOf(DerivationalEnvironment.CHANDAS)
        assertTrue(sandhi.joinSamasaMembers("नृ", "मनस्", environments = vedic).final.surface.startsWith("नृमण"))
        assertTrue(sandhi.joinSamasaMembers("नृ", "मनस्").final.surface.startsWith("नृमन"))
        val nas = pada("r", "नस्", upadesha = "नस्")
        assertEquals("ण", sandhi.join(pada("l", "उरु"), nas, environments = vedic).final.terms.last().surface.take(1))
        assertEquals("न", sandhi.join(pada("l", "उरु"), nas).final.terms.last().surface.take(1))
        val pra = pada("l", "प्र", samjnas = setOf(Samjna.UPASARGA))
        assertEquals("ण", sandhi.join(pra, nas, DerivationConfig(alternativeChoices = mapOf("8.4.28" to 0)), environments = vedic)
            .final.terms.last().surface.take(1))
    }
}

package dev.panini.vyakaranam.ast

import dev.panini.core.*
import kotlin.test.*

class TypedUpasargaTest {
    @Test fun `prefix identities round trip without guessing sandhi surfaces`() {
        for (prefix in Upasarga.entries) assertEquals(prefix, Upasarga.fromUpadesha(prefix.upadesha))
        assertNull(Upasarga.fromUpadesha("unknown"))
        assertNull(Upasarga.fromUpadesha("अन्तर्"))
    }

    @Test fun `all verbal forms preserve prefix order and unclassified entries`() {
        val prefixes = listOf(" सम् ", "unknown", "उद्")
        val expected = listOf(Upasarga.SAM, null, Upasarga.UD)
        val root = DhatuPrakriti("ignored", "हृ")
        val finite = TingantaPada("ignored", prefixes, root, Lakara.LOT, TingPratyaya("सिप्", "सिप्"))
        assertEquals(expected, finite.upasargaIdentities)
        assertEquals(expected, KridantaPratipadika("ignored", prefixes, root, "ल्युट्").upasargaIdentities)
        assertEquals(expected, AvyayaKridantaDerivation(prefixes, root, "ल्यप्").upasargaIdentities)
        assertEquals(prefixes, finite.upasargas)
    }
}

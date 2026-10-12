package dev.panini.vyakaranam.ast

import dev.panini.core.SanadiAffix
import dev.panini.derivation.TingantaDerivationRequest
import kotlin.test.*

class TypedSanadiRequestTest {
    @Test fun `unknown sanadi cannot bypass lexical pada checks even in permissive analysis`() {
        val root = dev.panini.dhatupatha.Dhatu("test", 1, "भू", "भू", "", "", "",
            dev.panini.core.DhatuGana.BHVADI, dev.panini.core.PadaType.PARASMAIPADA)
        val lexicon = dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon(emptyList(), listOf(root))
        val form = TingantaPada("ignored", emptyList(), DhatuPrakriti("ignored", "भू", listOf("unknown")),
            dev.panini.core.Lakara.LAT, TingPratyaya("त", "त"))
        for (validate in listOf(true, false)) {
            assertFailsWith<IllegalArgumentException> {
                dev.panini.analysis.PadaAnalyzer(lexicon, validatePadaCompatibility = validate).analyzeTinganta(form)
            }
        }
    }

    @Test fun `request uses canonical typed identities and preserves input order`() {
        val request = TingantaDerivationRequest("भू", sanadiPratyayas = listOf(" सन् ", "णिच्"))
        assertEquals(listOf(SanadiAffix.SAN, SanadiAffix.NIC), request.sanadiAffixes)
        assertEquals(setOf("सन्", "णिच्"), request.initialState().context.requestedSanadi)
    }

    @Test fun `unknown unsupported and duplicate identities fail before derivation`() {
        for (affixes in listOf(listOf("unknown"), listOf("क्यच्"), listOf("णिच्", " णिच् "))) {
            assertFailsWith<IllegalArgumentException> {
                TingantaDerivationRequest("भू", sanadiPratyayas = affixes).initialState()
            }
        }
    }
}

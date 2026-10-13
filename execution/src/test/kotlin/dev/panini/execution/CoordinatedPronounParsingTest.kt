package dev.panini.execution

import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class CoordinatedPronounParsingTest {
    @Test fun `repeated cha preserves nominal coordination across case relations`() {
        for (sup in listOf("सुँ", "अम्", "टा", "ङे", "ङसिँ", "ङस्", "ङि")) {
            val source = "तद् + $sup च तद् + $sup च मुद्र् + णिच् + लोट् + सिप् ।"
            val invocation = assertIs<Invocation>(PaniniParser().parse(source).body)
            val group = invocation.vakya.padas.filterIsInstance<SamuccitaSubanta>().single()
            assertEquals(2, group.members.size, source)
            assertTrue(group.members.all { (assertIs<SubantaPada>(it).pratipadika as MulaPratipadika).text == "तद्" })
            assertTrue(group.members.all { it.sup.text == sup })
            assertTrue(invocation.vakya.padas.none { it is SubantaPada })
        }
    }
}

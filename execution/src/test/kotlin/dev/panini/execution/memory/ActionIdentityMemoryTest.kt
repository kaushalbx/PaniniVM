package dev.panini.execution.memory

import dev.panini.analysis.*
import dev.panini.core.*
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class ActionIdentityMemoryTest {
    @Test fun `memory distinguishes prefixes causatives and ordered morphology without changing root queries`() {
        val lexical = DhatuPatha.all.single { it.id == "01.1153" }
        val vakya = PaniniParser().parse("एक + अम् भज् + लोट् + सिप् ।").grammaticalVakyas().single()
        val base = vakya.padas.filterIsInstance<TingantaPada>().single()
        fun entry(index: Int, prefixes: List<String>, sanadi: List<String>): RememberedKriya {
            val verb = base.copy(upasargas = prefixes, dhatu = base.dhatu.copy(sanadiPratyayas = sanadi))
            val head = KriyaHead(TingantaAnalysis(verb, lexical, TingAffix.SIP, Lakara.LOT), lexical)
            return RememberedKriya(1, KriyaFrame(KriyaId("action-$index"), vakya, head,
                Prayoga.KARTARI, emptyList(), emptyList()), null)
        }
        val entries = listOf(entry(1, emptyList(), emptyList()), entry(2, listOf("वि"), emptyList()),
            entry(3, listOf("वि"), listOf("णिच्")), entry(4, listOf("वि", "प्र"), listOf("णिच्", "सन्")),
            entry(5, listOf("प्र", "वि"), listOf("सन्", "णिच्")), entry(6, listOf("unknown"), emptyList()))
        val memory = KriyaMemory().remember(entries)
        for (entry in entries.dropLast(1)) {
            val identity = assertNotNull(entry.frame.kriya?.actionIdentity)
            assertEquals(listOf(entry), memory.matchingActions(identity))
        }
        assertNull(entries.last().frame.kriya?.actionIdentity)
        assertEquals(entries, memory.latestKriyas(lexical.upadesha, entries.size))
        assertTrue(memory.matchingActions(ActionIdentity("different.lexical.entry")).isEmpty())
        for (head in listOf("फल", "कर्मन्")) {
            val padas = PaniniParser().parse(
                "वि + भज् + णिच् + ल्युट् + ङस् $head + अम् मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas
            val identity = if (head == "फल")
                dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(padas).single().actionIdentity
            else dev.panini.execution.binding.KarakaReferenceResolver.references(padas).single().actionIdentity
            assertEquals(entries[2].frame.kriya?.actionIdentity, identity)
            val qualifier = dev.panini.execution.binding.MemoryOrderQualifier(ordinalNumber = 1L)
            assertEquals(entries[2], qualifier.select(memory, identity))
            assertNull(dev.panini.execution.binding.MemoryOrderQualifier(ordinalNumber = 2L).select(memory, identity))
            assertNull(dev.panini.execution.binding.MemoryOrderQualifier(previous = true).select(memory, identity))
            assertNull(dev.panini.execution.binding.MemoryOrderQualifier(ordinalNumber = Long.MAX_VALUE).select(memory, identity))
        }
    }
}

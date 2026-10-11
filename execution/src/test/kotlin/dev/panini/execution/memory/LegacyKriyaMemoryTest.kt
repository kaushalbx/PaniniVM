package dev.panini.execution.memory

import dev.panini.analysis.KriyaFrame
import dev.panini.analysis.KriyaId
import dev.panini.core.Prayoga
import dev.panini.vyakaranam.parser.PaniniParser
import java.io.DataOutputStream
import java.nio.file.Path
import org.junit.jupiter.api.io.TempDir
import kotlin.test.*

class LegacyKriyaMemoryTest {
    @TempDir lateinit var directory: Path

    @Test fun `invalid stored action counts fail before allocation`() {
        for (count in listOf(-1, Int.MAX_VALUE)) {
            DataOutputStream(directory.resolve("bGVnYWN5.kriya-memory").toFile().outputStream()).use { out ->
                out.writeUTF("PANINI_KRIYA_MEMORY_V3")
                out.writeInt(count)
            }
            assertFailsWith<IllegalArgumentException> {
                FileKriyaMemoryStore(directory.toFile()).load("legacy") { error("Must reject before analysis") }
            }
        }
    }

    @Test fun `duplicate action identities or participant roles cannot overwrite memory`() {
        val source = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val frame = KriyaFrame(KriyaId("old"), PaniniParser().parse(source).grammaticalVakyas().single(),
            null, Prayoga.KARTARI, emptyList(), emptyList())
        for (duplicateRole in listOf(false, true)) {
            DataOutputStream(directory.resolve("bGVnYWN5.kriya-memory").toFile().outputStream()).use { out ->
                out.writeUTF("PANINI_KRIYA_MEMORY_V3")
                out.writeInt(if (duplicateRole) 1 else 2)
                repeat(if (duplicateRole) 1 else 2) {
                    out.writeInt(1)
                    out.writeUTF("same-action")
                    out.writeUTF(source)
                    out.writeBoolean(false)
                    out.writeInt(if (duplicateRole) 2 else 0)
                    if (duplicateRole) repeat(2) {
                        out.writeUTF("KARMAN")
                        out.writeInt(0)
                    }
                }
            }
            val failure = assertFailsWith<IllegalArgumentException> {
                FileKriyaMemoryStore(directory.toFile()).load("legacy") { listOf(frame) }
            }
            assertTrue(failure.message.orEmpty().contains("Duplicate"))
        }
    }

    @Test fun `V2 entries remain readable without inventing participant snapshots`() {
        val source = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val frame = KriyaFrame(KriyaId("old"), PaniniParser().parse(source).grammaticalVakyas().single(),
            null, Prayoga.KARTARI, emptyList(), emptyList())
        val file = directory.resolve("bGVnYWN5.kriya-memory").toFile()
        DataOutputStream(file.outputStream()).use { out ->
            out.writeUTF("PANINI_KRIYA_MEMORY_V2")
            out.writeInt(1)
            out.writeInt(3)
            out.writeUTF("old")
            out.writeUTF(source)
            out.writeBoolean(false)
        }
        val memory = assertNotNull(FileKriyaMemoryStore(directory.toFile()).load("legacy") { listOf(frame) })
        val entry = memory.entries.single()
        assertEquals(3, entry.turn)
        assertEquals(KriyaId("old"), entry.frame.id)
        assertNull(entry.phala)
        assertTrue(entry.participants.isEmpty())
    }
}

package dev.panini.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varnamala
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Svara
import kotlin.test.*

class TypedPratyaharaAndSubstitutionTest {
    private val jash = linkedSetOf(Vyanjana.JA, Vyanjana.BA, Vyanjana.GA, Vyanjana.DDA, Vyanjana.DA)

    @Test fun `typed membership preserves every migrated pratyahara decision`() {
        val engine = Ashtadhyayi.pratyaharaEngine
        val sounds = buildList<Varna> { addAll(Svara.entries); addAll(Vyanjana.entries) }
        for (pratyahara in listOf(Pratyahara.HAL, Pratyahara.HAS, Pratyahara.ASH, Pratyahara.KHAR, Pratyahara.JHAL))
            for (varna in sounds) {
            assertEquals(engine.contains(pratyahara, varna.devanagari.single()), engine.contains(pratyahara, varna),
                "$pratyahara / $varna")
        }
    }

    @Test fun `typed nearest substitute preserves articulation scoring and tie order`() {
        for (source in Vyanjana.entries) {
            // Independent reference to the former written-character boundary, only in this regression test.
            val places = Varnamala.getSthana(source.devanagari.single())
            val expected = jash.maxByOrNull { (places intersect Varnamala.getSthana(it.devanagari.single())).size }
            assertEquals(expected, SthaneAntaratamahSutra.selectBest(source, jash), source.name)
        }
        assertNull(SthaneAntaratamahSutra.selectBest(Vyanjana.KA, emptySet<Vyanjana>()))
        assertEquals(Vyanjana.GA, SthaneAntaratamahSutra.selectBest(Vyanjana.KA, jash))
        assertEquals(Vyanjana.JA, SthaneAntaratamahSutra.selectBest(Vyanjana.CA, jash))
        assertEquals(Vyanjana.DDA, SthaneAntaratamahSutra.selectBest(Vyanjana.TTA, jash))
        assertEquals(Vyanjana.DA, SthaneAntaratamahSutra.selectBest(Vyanjana.TA, jash))
        assertEquals(Vyanjana.BA, SthaneAntaratamahSutra.selectBest(Vyanjana.PA, jash))
    }
}

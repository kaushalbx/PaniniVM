package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.NumVisarjaniyaSharvyavayeSutra
import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Vyanjana
import kotlin.test.*

class NumVisargaShatvaTest {
    private fun form(stem: String, v: Vibhakti, l: Linga) =
        SubantaEngine().derive(SubantaDerivationRequest(stem, v, Vacana.BAHUVACANA, l)).final

    @Test fun `8_3_58 across visarga`() {
        val sarpis = form("सर्पिस्", Vibhakti.SAPTAMI, Linga.NAPUMSAKA)
        assertEquals("सर्पिःषु", sarpis.surface)
        assertTrue("8.3.58" in sarpis.appliedSutras)
        assertEquals("धनुःषु", form("धनुस्", Vibhakti.SAPTAMI, Linga.NAPUMSAKA).surface)
        // After अ, no ṣatva.
        assertEquals("पयःसु", form("पयस्", Vibhakti.SAPTAMI, Linga.NAPUMSAKA).surface)
    }

    private fun numState(insertedBy: String): DerivationState {
        // An इ-final stem + नुम् (न् → anusvāra) + an affix स्.
        val base = DerivationState(listOf(
            DerivationTerm("stem", "हवि", TermKind.PRATIPADIKA),
            DerivationTerm("affix", "सि", TermKind.PRATYAYA),
        ), stage = DerivationStage.FINAL)
        return base.insertTermVarnas("stem", 4, listOf(Vyanjana.NA), insertedBy)
            .replaceTermVarna("stem", 4, listOf(Ayogavaha.ANUSVARA), "8.3.24")
    }

    @Test fun `8_3_58 across num only`() {
        val num = numState("7.1.72")
        assertTrue(NumVisarjaniyaSharvyavayeSutra.matches(num), num.substitutions.toString())
        assertEquals("हविंषि", NumVisarjaniyaSharvyavayeSutra.apply(num).state.surface)
        // An anusvāra not from नुम् does not intervene.
        assertFalse(NumVisarjaniyaSharvyavayeSutra.matches(numState("8.2.66")))
    }
}

class RohSupiTest {
    private fun form(stem: String, l: Linga) =
        SubantaEngine().derive(SubantaDerivationRequest(stem, Vibhakti.SAPTAMI, Vacana.BAHUVACANA, l)).final

    @Test fun `8_3_16 keeps a lexical r before sup`() {
        val catur = form("चतुर्", Linga.PUMS)
        assertEquals("चतुर्षु", catur.surface)
        assertTrue("8.3.16" in catur.appliedSutras)
        // रु still takes visarga: पयःसु.
        assertEquals("पयःसु", form("पयस्", Linga.NAPUMSAKA).surface)
    }
}

class MoRajiSamahKvauTest {
    private val engine = SandhiEngine()
    @Test fun `8_3_25 keeps m of sam before raj`() {
        assertTrue(engine.join("सम्", "राट्").final.surface.startsWith("सम्रा"))
        // Not before another र-initial word.
        assertEquals("संरक्षति", engine.join("सम्", "रक्षति").final.surface)
    }
}

class SamyogantaBeforeSupTest {
    private fun form(v: Vibhakti) =
        SubantaEngine().derive(SubantaDerivationRequest("पुम्स्", v, Vacana.BAHUVACANA, Linga.PUMS)).final

    @Test fun `8_2_23 deletes the cluster final of a pada before sup`() {
        val saptami = form(Vibhakti.SAPTAMI)
        assertEquals("पुंसु", saptami.surface)
        assertTrue("8.2.23" in saptami.appliedSutras && "8.2.66" !in saptami.appliedSutras)
        assertEquals("पुंभिः", form(Vibhakti.TRTIYA).surface)
        // Before a vowel-initial ending the stem is not a pada: पुंसः keeps स्.
        assertEquals("पुंसः", form(Vibhakti.DVITIYA).surface)
    }
}

class PumsoAsunTest {
    private fun form(v: Vibhakti, n: Vacana) =
        SubantaEngine().derive(SubantaDerivationRequest("पुम्स्", v, n, Linga.PUMS)).final

    @Test fun `7_1_89 7_1_70 and 6_4_10 give pumAn`() {
        val pumAn = form(Vibhakti.PRATHAMA, Vacana.EKAVACANA)
        assertEquals("पुमान्", pumAn.surface)
        assertTrue(listOf("7.1.89", "7.1.70", "6.4.10").all { it in pumAn.appliedSutras }, pumAn.appliedSutras.toString())
        assertEquals("पुमांसः", form(Vibhakti.PRATHAMA, Vacana.BAHUVACANA).surface)
        assertEquals("पुमांसौ", form(Vibhakti.PRATHAMA, Vacana.DVIVACANA).surface)
        // Not a सर्वनामस्थान: no असुँङ्.
        assertEquals("पुंसः", form(Vibhakti.DVITIYA, Vacana.BAHUVACANA).surface)
        assertEquals("पुंसा", form(Vibhakti.TRTIYA, Vacana.EKAVACANA).surface)
    }
}

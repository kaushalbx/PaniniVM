package dev.panini.derivation

import dev.panini.analysis.SamasaPada
import dev.panini.analysis.SamasaSemanticRelation
import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.shiksha.Samjna
import kotlin.test.*

class SamasantaBoundarySandhiTest {
    @Test fun `kap selection retains canonical upadesha and p marker rather than guessing surface identity`() {
        val result = SamasaEngine().derive(
            listOf(SamasaPada("बहु"), SamasaPada("कर्तृ")), SamasaType.BAHUVRIHI,
            outputLinga = Linga.PUMS,
        )
        val selection = result.applications.first { it.sutra == "5.4.153" }
        val affix = selection.after.terms.single { it.id == "samasanta-5.4.153" }
        assertEquals("कप्", affix.upadesha)
        assertEquals("क", affix.surface)
        assertEquals(setOf(dev.panini.core.ItMarker.P), affix.itMarkers)
        assertEquals("5.4.153", affix.createdBySutra)
        assertEquals(dev.panini.core.SamasantaAffix.KAP,
            requireNotNull(result.samasaResolution).operations.single { it.sutra == "5.4.153" }.samasantaAffix)
    }

    @Test fun `nonfinal n deletion retains member identity and its rule trace`() {
        val result = SamasaEngine().derive(
            listOf(SamasaPada("राजन्"), SamasaPada("पुरुष")), SamasaType.TATPURUSA,
        )
        assertEquals("राजपुरुष", requireNotNull(result.samasaResolution).compoundStem)
        val deletion = result.applications.first { it.sutra == "8.2.7" }
        assertEquals("pada_0", deletion.after.terms.first().id)
        assertEquals("राजन्", deletion.after.terms.first().upadesha)
        assertEquals("राज", deletion.after.terms.first().surface)
        assertEquals("pada_1", deletion.after.terms[1].id)
    }

    @Test fun `compound joining uses phonological vowel boundaries without interior consonant changes`() {
        val vowel = SamasaEngine().derive(
            listOf(SamasaPada("देव"), SamasaPada("ऋषि")), SamasaType.TATPURUSA,
        )
        assertEquals("देवर्षि", requireNotNull(vowel.samasaResolution).compoundStem)
        assertTrue(vowel.applications.any { it.sutra == "6.1.87" })
        val consonant = SamasaEngine().derive(
            listOf(SamasaPada("सर्प"), SamasaPada("भय")), SamasaType.TATPURUSA,
        )
        assertEquals("सर्पभय", requireNotNull(consonant.samasaResolution).compoundStem)
    }

    @Test fun `vedic as ending exposes the affixed and unmodified branches`() {
        val result = SamasaEngine().derive(
            listOf(SamasaPada("देव"), SamasaPada("छन्दस्")), SamasaType.TATPURUSA,
            outputLinga = Linga.NAPUMSAKA,
            semanticRelations = setOf(SamasaSemanticRelation.VEDIC_REGISTER),
        )
        val alternatives = requireNotNull(result.samasaResolution).alternatives
        assertTrue(alternatives.any { "5.4.103" in it.transformationSutras && it.compoundStem == "देवछन्दस" })
        assertTrue(alternatives.any { "5.4.103" !in it.transformationSutras && it.compoundStem == "देवछन्दस्" })
    }

    @Test fun `giri and nadi retain distinct applied and omitted samasanta branches`() {
        for ((last, number, applied) in listOf(
            Triple("गिरि", "5.4.112", "उपगिर"),
            Triple("नदी", "5.4.110", "उपनद"),
        )) {
            val result = SamasaEngine().derive(
                listOf(SamasaPada("उप"), SamasaPada(last)), SamasaType.AVYAYIBHAVA,
                outputLinga = Linga.NAPUMSAKA,
            )
            val alternatives = requireNotNull(result.samasaResolution).alternatives
            assertTrue(alternatives.any { number in it.transformationSutras && it.compoundStem == applied })
            assertTrue(alternatives.any { number !in it.transformationSutras && it.compoundStem == "उप" + last })
        }
    }

    @Test fun `country brahman forms the prescribed a stem`() {
        val result = SamasaEngine().derive(
            listOf(SamasaPada("अवन्ति"), SamasaPada("ब्रह्मन्")), SamasaType.TATPURUSA,
            outputLinga = Linga.PUMS,
            semanticRelations = setOf(SamasaSemanticRelation.COUNTRY_PERSON),
        )
        assertEquals("अवन्तिब्रह्म", requireNotNull(result.samasaResolution).compoundStem)
        assertTrue(result.applications.any { it.sutra == "5.4.104" })
    }

    @Test fun `collective consonant dvandva receives a before inflection`() {
        val result = SamasaEngine().derive(
            listOf(SamasaPada("वाक्"), SamasaPada("त्वच्")), SamasaType.DVANDVA,
            outputLinga = Linga.NAPUMSAKA,
            semanticRelations = setOf(SamasaSemanticRelation.COLLECTIVE),
        )
        assertEquals("वाक्त्वच", requireNotNull(result.samasaResolution).compoundStem)
        assertTrue(result.applications.any { it.sutra == "5.4.106" })
    }

    @Test fun `anguli substitution joins by separately traced yan`() {
        val result=SamasaEngine().derive(
            listOf(SamasaPada("त्रि"), SamasaPada("अङ्गुलि")), SamasaType.TATPURUSA,
        )
        assertEquals("त्र्यङ्गुल", requireNotNull(result.samasaResolution).compoundStem)
        assertTrue(result.applications.any { it.sutra=="5.4.86" })
        assertTrue(result.applications.any { it.sutra=="6.1.77" })
    }

    @Test fun `adhvan substitution joins by separately traced savarna dirgha`() {
        val result=SamasaEngine().derive(
            listOf(SamasaPada("प्र", samjnas=setOf(Samjna.UPASARGA)), SamasaPada("अध्वन्")),
            SamasaType.TATPURUSA,
        )
        assertEquals("प्राध्व", requireNotNull(result.samasaResolution).compoundStem)
        assertTrue(result.applications.any { it.sutra=="5.4.85" })
        assertTrue(result.applications.any { it.sutra=="6.1.101" })
    }
}

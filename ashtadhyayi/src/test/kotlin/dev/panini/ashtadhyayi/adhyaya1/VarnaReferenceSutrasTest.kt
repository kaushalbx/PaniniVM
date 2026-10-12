package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.AloAntyatPurvaUpadhaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AnuditSavarnasyaCapratyayahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TaparasTatKalasyaSutra
import dev.panini.core.ItMarker
import dev.panini.shiksha.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VarnaReferenceSutrasTest {
    @Test
    fun `udit is an explicit marker not a pronounced u vowel`() {
        for (consonant in listOf(Vyanjana.KA, Vyanjana.CA, Vyanjana.TTA, Vyanjana.TA, Vyanjana.PA)) {
            assertFalse(AnuditSavarnasyaCapratyayahSutra.matches(VarnaReference(consonant)))
            val udit = VarnaReference(consonant, itMarkers = setOf(ItMarker.U))
            assertTrue(AnuditSavarnasyaCapratyayahSutra.matches(udit))
            assertFalse(AnuditSavarnasyaCapratyayahSutra.matches(udit.copy(use = VarnaReferenceUse.PRESCRIPTION)))
            assertEquals(setOf(ItMarker.U), udit.itMarkers)
        }
    }

    @Test
    fun `later an includes semivowels but not sibilants or prescribed vowels`() {
        for (varna in listOf(Svara.A, Svara.R, Svara.E, Vyanjana.HA, Vyanjana.YA, Vyanjana.VA, Vyanjana.RA, Vyanjana.LA)) {
            val reference = VarnaReference(varna)
            assertTrue(AnuditSavarnasyaCapratyayahSutra.matches(reference))
            assertFalse(AnuditSavarnasyaCapratyayahSutra.matches(reference.copy(use = VarnaReferenceUse.PRESCRIPTION)))
        }
        for (varna in listOf(Vyanjana.SHA, Vyanjana.SSA, Vyanjana.SA)) {
            assertFalse(AnuditSavarnasyaCapratyayahSutra.matches(VarnaReference(varna)))
        }
    }

    @Test
    fun `t on either side selects the duration restriction instead of unrestricted savarna`() {
        for (position in TMarkerPosition.entries) {
            val reference = VarnaReference(Svara.A, tMarkerPosition = position)
            assertTrue(TaparasTatKalasyaSutra.matches(reference))
            assertFalse(AnuditSavarnasyaCapratyayahSutra.matches(reference))
            assertFalse(TaparasTatKalasyaSutra.matches(reference.copy(use = VarnaReferenceUse.PRESCRIPTION)))
        }
        assertFalse(TaparasTatKalasyaSutra.matches(VarnaReference(Svara.A)))
        assertFalse(TaparasTatKalasyaSutra.matches(VarnaReference(Vyanjana.TA)))
    }

    @Test
    fun `upadha is the penultimate phonological token including inherent vowels and clusters`() {
        for ((surface, expected) in listOf(
            "राम" to Vyanjana.MA, "राम्" to Svara.AA, "अग्नि" to Vyanjana.NA,
            "भू" to Vyanjana.BHA, "इ" to null, "" to null,
        )) {
            val varnas = surface.toVarnas()
            assertEquals(expected != null, AloAntyatPurvaUpadhaSutra.matches(varnas), surface)
            assertEquals(expected, AloAntyatPurvaUpadhaSutra.apply(varnas), surface)
        }
    }
}

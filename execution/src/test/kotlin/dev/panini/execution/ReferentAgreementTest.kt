package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.core.Vacana
import kotlin.test.*

class ReferentAgreementTest {
    @Test fun `unknown feminine wrapper cannot prove grammatical gender or preserve ordinal meaning`() {
        val unknown = dev.panini.vyakaranam.ast.StriVikara("display only", "unknown")
        val stem = dev.panini.vyakaranam.ast.MulaPratipadika("ignored", "प्रथम", listOf(unknown))
        val declaration = dev.panini.vyakaranam.ast.SubantaPada("ignored", stem,
            dev.panini.vyakaranam.ast.SupPratyaya("सुँ", "सुँ"))
        assertTrue(ReferentAgreement.fromDeclaration(declaration).genders.isEmpty())
        assertNull(stem.lexicalOrdinalValue)
        assertNull(unknown.affix)
        for (affix in dev.panini.core.StriAffix.entries) {
            val recognized = unknown.copy(pratyaya = affix.upadesha)
            assertEquals(affix, recognized.affix)
            assertEquals(setOf(Linga.STRI), ReferentAgreement.fromDeclaration(declaration.copy(
                pratipadika = stem.copy(vikaras = listOf(recognized)))).genders)
        }
    }

    @Test fun `gender evidence follows the final derivational layer not any earlier feminine suffix`() {
        val feminine = dev.panini.vyakaranam.ast.StriVikara("टाप्", "टाप्")
        val later = dev.panini.vyakaranam.ast.TaddhitaVikara("त्व", "त्व")
        fun declaration(vikaras: List<dev.panini.vyakaranam.ast.PratipadikaVikara>) =
            dev.panini.vyakaranam.ast.SubantaPada("derived", dev.panini.vyakaranam.ast.MulaPratipadika(
                "derived", "प्रथम", vikaras), dev.panini.vyakaranam.ast.SupPratyaya("सुँ", "सुँ"))
        assertTrue(ReferentAgreement.fromDeclaration(declaration(listOf(feminine, later))).genders.isEmpty())
        assertEquals(setOf(Linga.STRI),
            ReferentAgreement.fromDeclaration(declaration(listOf(later, feminine))).genders)
    }

    @Test fun `explicit feminine derivation supplies referent gender evidence`() {
        val parsed = dev.panini.vyakaranam.parser.PaniniParser().parse("प्रथम + टाप् + सुँ ।")
        val declaration = parsed.grammaticalVakyas().single().padas.single() as dev.panini.vyakaranam.ast.SubantaPada
        val agreement = ReferentAgreement.fromDeclaration(declaration)
        assertEquals(setOf(Linga.STRI), agreement.genders)
        assertEquals(setOf(Vacana.EKAVACANA), agreement.numbers)
        assertEquals(ReferentAgreementResolution.Selected("derived"), ReferentAgreementResolver.resolve(
            ReferentAgreement(setOf(Vacana.EKAVACANA), setOf(Linga.STRI)),
            listOf(AgreementCandidate("derived", agreement))))
    }

    @Test fun `nonfeminine derivation does not inherit base noun gender without evidence`() {
        val parsed = dev.panini.vyakaranam.parser.PaniniParser().parse("राम + मतुप् + सुँ ।")
        val declaration = parsed.grammaticalVakyas().single().padas.single() as dev.panini.vyakaranam.ast.SubantaPada
        assertTrue(ReferentAgreement.fromDeclaration(declaration).genders.isEmpty())
    }

    private val singularNeuter = ReferentAgreement(setOf(Vacana.EKAVACANA), setOf(Linga.NAPUMSAKA))
    private val pluralNeuter = singularNeuter.copy(numbers = setOf(Vacana.BAHUVACANA))

    @Test fun `agreement selects identity without preferring the most recent candidate`() {
        val selected = ReferentAgreementResolver.resolve(singularNeuter, listOf(
            AgreementCandidate("first", singularNeuter), AgreementCandidate("last", pluralNeuter)))
        assertEquals(ReferentAgreementResolution.Selected("first"), selected)
        assertEquals(ReferentAgreementResolution.Missing, ReferentAgreementResolver.resolve(
            singularNeuter, listOf(AgreementCandidate("plural", pluralNeuter))))
    }

    @Test fun `unknown evidence cannot disambiguate or prove agreement`() {
        val unknown = AgreementCandidate("unknown", ReferentAgreement())
        assertEquals(ReferentAgreementResolution.IncompleteEvidence("unknown"),
            ReferentAgreementResolver.resolve(singularNeuter, listOf(unknown)))
        assertIs<ReferentAgreementResolution.Ambiguous<String>>(ReferentAgreementResolver.resolve(
            singularNeuter, listOf(AgreementCandidate("known", singularNeuter), unknown)))
        assertIs<ReferentAgreementResolution.IncompleteEvidence<String>>(ReferentAgreementResolver.resolve(
            ReferentAgreement(), listOf(AgreementCandidate("known", singularNeuter))))
    }

    @Test fun `gender alternatives remain possible without choosing a gender`() {
        val alternatives = singularNeuter.copy(genders = setOf(Linga.PUMS, Linga.NAPUMSAKA))
        assertEquals(ReferentAgreementResolution.Selected("noun"), ReferentAgreementResolver.resolve(
            singularNeuter, listOf(AgreementCandidate("noun", alternatives))))
        assertEquals(ReferentAgreementResolution.Missing, ReferentAgreementResolver.resolve(
            singularNeuter.copy(genders = setOf(Linga.STRI)), listOf(AgreementCandidate("noun", alternatives))))
        assertFailsWith<IllegalArgumentException> {
            ReferentAgreementResolver.resolve(singularNeuter, listOf(
                AgreementCandidate("alias", singularNeuter), AgreementCandidate("alias", alternatives)))
        }
    }
}

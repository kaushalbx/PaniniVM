package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.core.Vacana
import kotlin.test.*

class ReferentAgreementTest {
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

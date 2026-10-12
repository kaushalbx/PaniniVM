package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class AnaphoricOperandProjectionTest {
    @Test fun `syncretic pronouns retain dual number and every case slot after resolution`() {
        val dual = ReferentAgreement(setOf(Vacana.DVIVACANA), setOf(Linga.NAPUMSAKA))
        for (ending in listOf("भ्याम्", "ओस्")) {
            val node = invocation("तद् + $ending मुद्र् + णिच् + लोट् + सिप् ।")
            val original = node.vakya.padas.filterIsInstance<SubantaPada>().single()
            assertEquals(setOf(Vacana.DVIVACANA), AnaphoricOperandProjection.agreement(original)?.numbers)
            assertEquals(setOf(Vacana.DVIVACANA), ReferentAgreement.fromDeclaration(original).numbers)
            val projected = AnaphoricOperandProjection.project(node, listOf(
                AgreementCandidate("singular", singular), AgreementCandidate("dual", dual)))
            assertEquals("dual", projected.operands.single().identity)
            val replacement = projected.invocation.vakya.padas.filterIsInstance<SubantaPada>().single()
            assertSame(original.sup, replacement.sup)
            assertTrue(replacement.sup.affixCandidates.size > 1)
        }
    }

    @Test fun `unknown sup provides no number evidence and cannot select a pronoun antecedent`() {
        val node = invocation("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val original = node.vakya.padas.filterIsInstance<SubantaPada>().single()
        val unknown = original.copy(sup = SupPratyaya("unknown", "unknown"))
        assertTrue(assertNotNull(AnaphoricOperandProjection.agreement(unknown)).numbers.isEmpty())
        assertTrue(ReferentAgreement.fromDeclaration(unknown).numbers.isEmpty())
        val vakya = (node.vakya as AkhyataVakya).copy(padas = node.vakya.padas.map {
            if (it === original) unknown else it
        })
        assertTrue(assertFailsWith<IllegalArgumentException> {
            AnaphoricOperandProjection.project(node.copy(vakya = vakya), listOf(AgreementCandidate("slot", singular)))
        }.message.orEmpty().contains("Incomplete"))
    }

    private val singular = ReferentAgreement(setOf(Vacana.EKAVACANA), setOf(Linga.NAPUMSAKA))
    private fun invocation(source: String) = assertIs<Invocation>(PaniniParser().parse(source).body)

    @Test fun `implicit coordinated result shares resolved members and respects protection`() {
        val node = invocation("तद् + अम् च तद् + अम् च मुद्र् + णिच् + लोट् + सिप् ।")
        val original = node.vakya.padas.filterIsInstance<SamuccitaSubanta>().single()
        val withResult = node.copy(implicitValuePada = original)
        val projection = AnaphoricOperandProjection.project(withResult,
            listOf(AgreementCandidate("slot", singular)))
        val replacement = projection.invocation.vakya.padas.filterIsInstance<SamuccitaSubanta>().single()
        assertSame(replacement, projection.invocation.implicitValuePada)
        assertEquals(2, projection.operands.size)
        replacement.members.zip(original.members).forEach { (resolved, source) ->
            assertSame(source.sup, resolved.sup)
            assertNotEquals(source.pratipadika, resolved.pratipadika)
        }
        val protected = AnaphoricOperandProjection.project(withResult,
            emptyList<AgreementCandidate<String>>(), protectedPadas = setOf(original))
        assertSame(original, protected.invocation.implicitValuePada)
        assertTrue(protected.operands.isEmpty())
        val implicitOnly = AnaphoricOperandProjection.project(node.copy(
            vakya = (node.vakya as AkhyataVakya).copy(padas = node.vakya.padas.filterNot { it === original }),
            implicitValuePada = original), listOf(AgreementCandidate("slot", singular)))
        assertEquals(2, implicitOnly.operands.size)
        assertIs<SamuccitaSubanta>(implicitOnly.invocation.implicitValuePada)
    }

    @Test fun `implicit result shares the projected nominal rather than retaining a raw pronoun`() {
        val node = invocation("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val nominal = node.vakya.padas.filterIsInstance<SubantaPada>().single()
        val projection = AnaphoricOperandProjection.project(node.copy(implicitValuePada = nominal),
            listOf(AgreementCandidate("slot", singular)))
        assertEquals(1, projection.operands.size)
        assertSame(projection.invocation.vakya.padas.filterIsInstance<SubantaPada>().single(),
            projection.invocation.implicitValuePada)
    }

    @Test fun `projection retains selected identity original AST and grammatical case`() {
        val node = invocation("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val original = node.vakya.padas.filterIsInstance<SubantaPada>().single()
        val projection = AnaphoricOperandProjection.project(node, listOf(AgreementCandidate("slot", singular)))
        val operand = projection.operands.single()
        assertEquals("slot", operand.identity)
        assertSame(original, operand.original)
        val replacement = projection.invocation.vakya.padas.filterIsInstance<SubantaPada>().single()
        assertSame(original.sup, replacement.sup)
        assertEquals(operand.name, (replacement.pratipadika as MulaPratipadika).text)
        assertEquals(setOf(Vacana.EKAVACANA), AnaphoricOperandProjection.agreement(original)?.numbers)
        assertEquals(Linga.entries.toSet(), AnaphoricOperandProjection.agreement(original)?.genders)
    }

    @Test fun `unresolved anaphors never become literal text`() {
        val node = invocation("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        for ((candidates, message) in listOf(
            emptyList<AgreementCandidate<String>>() to "No compatible",
            listOf(AgreementCandidate("plural", singular.copy(numbers = setOf(Vacana.BAHUVACANA)))) to "No compatible",
            listOf(AgreementCandidate("unknown", ReferentAgreement())) to "Incomplete",
            listOf(AgreementCandidate("one", singular), AgreementCandidate("two", singular)) to "Ambiguous",
        )) assertTrue(assertFailsWith<IllegalArgumentException> {
            AnaphoricOperandProjection.project(node, candidates)
        }.message.orEmpty().contains(message))
    }

    @Test fun `projection preserves each case relation independently`() {
        for (sup in listOf("सुँ", "अम्", "टा", "ङे", "ङसिँ", "ङस्", "ङि")) {
            val node = invocation("तद् + $sup मुद्र् + णिच् + लोट् + सिप् ।")
            val original = node.vakya.padas.filterIsInstance<SubantaPada>().single()
            val projection = AnaphoricOperandProjection.project(node,
                listOf(AgreementCandidate("referent", singular)))
            val replacement = projection.invocation.vakya.padas.filterIsInstance<SubantaPada>().single()
            assertSame(original.sup, replacement.sup, sup)
            assertSame(original, projection.operands.single().original, sup)
            assertEquals("referent", projection.operands.single().identity, sup)
        }
    }

    @Test fun `derived and nonanaphoric sarvanama forms are not automatic references`() {
        for (source in listOf("तद् + त्व + अम्", "सर्व + अम्", "अन्य + अम्", "यद् + अम्")) {
            val node = invocation("$source मुद्र् + णिच् + लोट् + सिप् ।")
            assertTrue(AnaphoricOperandProjection.project(node, emptyList<AgreementCandidate<String>>()).operands.isEmpty())
        }
        val node = invocation("तद् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val pada = node.vakya.padas.filterIsInstance<SubantaPada>().single()
        assertTrue(AnaphoricOperandProjection.project(node, emptyList<AgreementCandidate<String>>(),
            protectedPadas = setOf(pada)).operands.isEmpty())
    }
}

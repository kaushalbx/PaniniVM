package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

class PersistentListOperationsTest {
    private fun number(value: Long) = SanskritValue.Sankhya(value, value.toString())
    private val source = SanskritValue.Suchi(listOf(number(1), number(2), number(3)), ListMemberType.NUMBER)

    @Test
    fun `indexed transformations are one based persistent and type preserving`() {
        val replaced = replaceListMember(source, number(2), number(9))
        val exchanged = exchangeListMembers(source, number(1), SanskritValue.Purana(3, "तृतीय"))
        val inserted = insertListMember(source, SanskritValue.Purana(2, "द्वितीय"), number(9))
        val removed = removeListMember(source, number(2))

        assertEquals(listOf(1L, 9L, 3L), replaced.items.map { assertIs<SanskritValue.Sankhya>(it).value })
        assertEquals(listOf(3L, 2L, 1L), exchanged.items.map { assertIs<SanskritValue.Sankhya>(it).value })
        assertEquals(listOf(1L, 9L, 2L, 3L), inserted.items.map { assertIs<SanskritValue.Sankhya>(it).value })
        assertEquals(listOf(1L, 3L), removed.items.map { assertIs<SanskritValue.Sankhya>(it).value })
        assertEquals(ListMemberType.NUMBER, replaced.memberType)
        assertEquals(ListMemberType.NUMBER, exchanged.memberType)
        assertEquals(listOf(1L, 2L, 3L), source.items.map { assertIs<SanskritValue.Sankhya>(it).value })
    }

    @Test
    fun `exchange at one position is a persistent no op`() {
        val exchanged = exchangeListMembers(source, number(2), number(2))
        assertEquals(source, exchanged)
        assertTrue(source !== exchanged)
    }

    @Test
    fun `final removal returns shortened list and removed value`() {
        val result = removeFinalListMember(source)
        assertEquals(LIST_REMOVAL_RESULT_SCHEMA, result.schema)
        val remaining = assertIs<SanskritValue.Suchi>(result.fields.getValue(LIST_REMOVAL_LIST_FIELD))
        assertEquals(listOf(1L, 2L), remaining.items.map { assertIs<SanskritValue.Sankhya>(it).value })
        assertEquals(3L, assertIs<SanskritValue.Sankhya>(
            result.fields.getValue(LIST_REMOVAL_VALUE_FIELD)).value)
        assertEquals(ListMemberType.NUMBER, remaining.memberType)
    }

    @Test
    fun `invalid bounds types and members fail precisely`() {
        assertFailsWith<PersistentListOperationException> { replaceListMember(source, number(0), number(9)) }
        assertFailsWith<PersistentListOperationException> { insertListMember(source, number(5), number(9)) }
        assertFailsWith<PersistentListOperationException> { removeListMember(source, number(4)) }
        assertFailsWith<PersistentListOperationException> { exchangeListMembers(source, number(0), number(2)) }
        assertFailsWith<PersistentListOperationException> { exchangeListMembers(source, number(2), number(4)) }
        assertFailsWith<PersistentListOperationException> {
            replaceListMember(source, number(1), SanskritValue.Shabda("राम"))
        }
        assertFailsWith<PersistentListOperationException> {
            removeFinalListMember(SanskritValue.Suchi(emptyList(), ListMemberType.NUMBER))
        }
    }
}

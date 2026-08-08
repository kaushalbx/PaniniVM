package dev.panini.sankhya

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SankhyaOperationMarkersTest {

    @Test
    fun `classifies shared arithmetic markers`() {
        listOf("गुणित", "हते").forEach { assertTrue(SankhyaOperationMarkers.isMultiplication(it)) }
        listOf("भक्त", "हृत").forEach { assertTrue(SankhyaOperationMarkers.isDivision(it)) }
        listOf("वर्ग", "घन", "मूल").forEach { assertTrue(SankhyaOperationMarkers.isBindingPrefix(it)) }
        assertTrue(SankhyaOperationMarkers.isConstruction("कृत"))
        assertFalse(SankhyaOperationMarkers.isBindingPrefix("पद"))
        assertTrue(SankhyaOperationMarkers.isSquareRoot("पद"))
    }

    @Test
    fun `detects operation fragments awaiting a following operand`() {
        assertTrue(SankhyaOperationMarkers.needsFollowingOperand(listOf("द्वि", "गुणित")))
        assertTrue(SankhyaOperationMarkers.needsFollowingOperand(listOf("वर्ग", "कृत")))
        assertFalse(SankhyaOperationMarkers.needsFollowingOperand(listOf("द्वि")))
    }
}

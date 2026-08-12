package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DynamicNishedhaEvaluatorTest {

    @Test
    fun `prohibits when the selected argument equals zero`() {
        assertTrue(DynamicNishedhaEvaluator.evaluateProhibition("न शून्य + अम् शून्य + अम् ।"))
        assertTrue(DynamicNishedhaEvaluator.evaluateProhibition("मा ० + अम् शून्य + अम् ।"))
    }

    @Test
    fun `does not treat a guard literal as an independently prohibited argument`() {
        assertFalse(DynamicNishedhaEvaluator.evaluateProhibition("न द्वि + अम् शून्य + अम् ।"))
        assertFalse(DynamicNishedhaEvaluator.evaluateProhibition("न शून्य + अम् द्वि + अम् ।"))
        assertFalse(DynamicNishedhaEvaluator.evaluateProhibition("द्वि + अम् द्वि + अम् ।"))
    }
}

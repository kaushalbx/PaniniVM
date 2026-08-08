package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TechnicalSamjnaIdentityTest {

    @Test
    fun `classifies canonical technical identities`() {
        listOf("सङ्ख्या", "गुण", "वृद्धि", "लोप", "साधकतमम्", "कर्म", "करणम्").forEach {
            assertTrue(TechnicalSamjnaIdentity.contains(it))
            assertFalse(SvamRupamEngine.isSelfReferentialLiteral(it))
        }
    }

    @Test
    fun `leaves ordinary lexical identity self referential`() {
        assertFalse(TechnicalSamjnaIdentity.contains("राम"))
        assertTrue(SvamRupamEngine.isSelfReferentialLiteral("राम"))
    }
}

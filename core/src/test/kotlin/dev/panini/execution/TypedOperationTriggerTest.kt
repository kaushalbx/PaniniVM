package dev.panini.execution

import dev.panini.core.*
import kotlin.test.*

class TypedOperationTriggerTest {
    @Test fun `recognized trigger markers match grammatical identities not whitespace`() {
        val trigger = OperationTrigger(requiredUpasargas = setOf("सम्"), requiredSanadi = setOf("णिच्"))
        assertTrue(trigger.matches(GrammaticalFeatures(upasargas = setOf(" सम् "), sanadi = setOf(" णिच् "))))
        assertFalse(trigger.matches(GrammaticalFeatures(upasargas = setOf("वि"), sanadi = setOf("णिच्"))))
        assertEquals(setOf(Upasarga.SAM), trigger.requiredUpasargaIdentities)
        assertEquals(setOf(SanadiAffix.NIC), trigger.requiredSanadiIdentities)
    }

    @Test fun `canonical identity contradictions cannot hide behind notation`() {
        assertFailsWith<IllegalArgumentException> {
            OperationTrigger(requiredUpasargas = setOf(" सम् "), forbiddenUpasargas = setOf("सम्"))
        }
        assertFailsWith<IllegalArgumentException> {
            OperationTrigger(requiredSanadi = setOf(" णिच् "), forbiddenSanadi = setOf("णिच्"))
        }
    }

    @Test fun `unclassified compatibility markers remain exact constraints and never become wildcards`() {
        val required = OperationTrigger(requiredUpasargas = setOf("legacy"), requiredSanadi = setOf("unknown"))
        assertFalse(required.matches(GrammaticalFeatures()))
        assertTrue(required.matches(GrammaticalFeatures(upasargas = setOf("legacy"), sanadi = setOf("unknown"))))
        assertFalse(OperationTrigger(forbiddenUpasargas = setOf("legacy"))
            .matches(GrammaticalFeatures(upasargas = setOf("legacy"))))
    }
}

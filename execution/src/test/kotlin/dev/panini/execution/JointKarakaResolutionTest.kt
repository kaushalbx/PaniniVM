package dev.panini.execution

import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.dhatupatha.Dhatu
import kotlin.test.*

class JointKarakaResolutionTest {
    private fun resolve(secondType: OperationValueType, values: List<SanskritValue>,
                        fixed: Map<Karaka, ExecutionExpression> = emptyMap(),
                        candidateRoles: Set<Karaka> = setOf(Karaka.KARANA, Karaka.SAMPRADANA)): OperationResolution {
        val operation = DhatuOperation(
            signature = OperationSignature(listOf(
                KarakaRequirement(Karaka.KARANA, maximumMembers = 1, valueType = OperationValueType.INTEGER),
                KarakaRequirement(Karaka.SAMPRADANA, maximumMembers = 1, valueType = secondType),
            )),
            action = object : DhatuAction("test", "test") {
                override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult =
                    error("Analysis must not execute actions")
            },
        )
        val dhatu = Dhatu(
            id = "test.joint", krama = 1, upadesha = "परीक्ष्", sourceSurface = "परीक्ष्",
            artha = "परीक्षणे", arthaHindi = "परीक्षण", arthaEnglish = "test",
            gana = DhatuGana.BHVADI, operations = listOf(operation),
        )
        return OperationResolver.resolve(DhatuInvocation("test", dhatu, fixed,
            ambiguousBindings = values.map { value -> AmbiguousKarakaBinding(
                ExecutionExpression.TypedOperand(value, SupAffix.BHYAM_3),
                candidateRoles,
            ) }), emptyMap())
    }

    @Test fun `typed valency jointly resolves two ambiguous participants regardless of order`() {
        val number = SanskritValue.Sankhya(2, "द्वि")
        val text = SanskritValue.Shabda("शब्द")
        for (values in listOf(listOf(number, text), listOf(text, number))) {
            val resolved = assertIs<OperationResolution.Resolved>(resolve(OperationValueType.TEXT, values)).value
            assertEquals(listOf(number), resolved.context.resolveValues(resolved.context.bindings.getValue(Karaka.KARANA)))
            assertEquals(listOf(text), resolved.context.resolveValues(resolved.context.bindings.getValue(Karaka.SAMPRADANA)))
        }
    }

    @Test fun `two valid assignments stay ambiguous rather than choosing word order`() {
        val result = assertIs<OperationResolution.Ambiguous>(resolve(OperationValueType.INTEGER,
            listOf(SanskritValue.Sankhya(1, "एक"), SanskritValue.Sankhya(2, "द्वि"))))
        assertEquals(listOf("test"), result.operations)
    }

    @Test fun `established role is preserved while ambiguous participant fills remaining role`() {
        val fixed = ExecutionExpression.sankhya(1, "एक")
        val resolved = assertIs<OperationResolution.Resolved>(resolve(OperationValueType.INTEGER,
            listOf(SanskritValue.Sankhya(2, "द्वि")), mapOf(Karaka.KARANA to fixed))).value
        assertEquals(fixed, resolved.context.bindings[Karaka.KARANA])
    }

    @Test fun `incompatible joint assignments are rejected`() {
        assertIs<OperationResolution.Invalid>(resolve(OperationValueType.TEXT,
            listOf(SanskritValue.Sankhya(1, "एक"), SanskritValue.Sankhya(2, "द्वि"))))
    }

    @Test fun `analysis budget exhaustion never selects a partial assignment`() {
        val result = assertIs<OperationResolution.Invalid>(resolve(OperationValueType.TEXT,
            List(13) { SanskritValue.Sankhya(1, "एक") }))
        assertTrue(result.message.contains("assignment budget"))
    }

    @Test fun `unassignable participants cannot disappear from an otherwise complete sentence`() {
        val result = assertIs<OperationResolution.Invalid>(resolve(OperationValueType.TEXT,
            listOf(SanskritValue.Sankhya(2, "द्वि")),
            fixed = mapOf(
                Karaka.KARANA to ExecutionExpression.sankhya(1, "एक"),
                Karaka.SAMPRADANA to ExecutionExpression.TypedOperand(SanskritValue.Shabda("शब्द"), SupAffix.NGE),
            ), candidateRoles = setOf(Karaka.APADANA, Karaka.ADHIKARANA)))
        assertTrue(result.message.contains("no grammatical role"))
    }
}

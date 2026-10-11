package dev.panini.execution

import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.dhatupatha.Dhatu
import kotlin.test.Test
import kotlin.test.assertIs

class OperationReferenceShapeTest {
    private fun resolve(shape: ExpressionShape, expression: ExecutionExpression): OperationResolution {
        val operation = DhatuOperation(
            signature = OperationSignature(listOf(KarakaRequirement(Karaka.KARMAN, shape = shape))),
            action = object : DhatuAction("test", "test") {
                override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult =
                    error("Signature resolution must not execute the action")
            },
        )
        val dhatu = Dhatu(
            id = "test.reference", krama = 1, upadesha = "परीक्ष्", sourceSurface = "परीक्ष्",
            artha = "परीक्षणे", arthaHindi = "परीक्षण", arthaEnglish = "test",
            gana = DhatuGana.BHVADI, operations = listOf(operation),
        )
        return OperationResolver.resolve(
            DhatuInvocation("test", dhatu, mapOf(Karaka.KARMAN to expression)),
            mapOf("क्रम" to SanskritValue.Shabda("क्रम")),
        )
    }

    @Test fun `resolving a named operand preserves reference shape`() {
        val named = ExecutionExpression.TypedOperand(SanskritValue.Shabda("क्रम"), SupAffix.AM, "क्रम")
        assertIs<OperationResolution.Resolved>(resolve(ExpressionShape.REFERENCE, named))
        assertIs<OperationResolution.Resolved>(resolve(ExpressionShape.REFERENCE, ExecutionExpression.Reference("क्रम")))
        assertIs<OperationResolution.Invalid>(resolve(ExpressionShape.LITERAL, named))
    }

    @Test fun `same spelling does not make a literal into a reference`() {
        val literal = ExecutionExpression.TypedOperand(SanskritValue.Shabda("क्रम"), SupAffix.AM)
        assertIs<OperationResolution.Resolved>(resolve(ExpressionShape.LITERAL, literal))
        assertIs<OperationResolution.Invalid>(resolve(ExpressionShape.REFERENCE, literal))
    }
}

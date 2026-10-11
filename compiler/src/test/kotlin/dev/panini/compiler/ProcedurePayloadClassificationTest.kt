package dev.panini.compiler

import dev.panini.execution.*
import kotlin.test.*

class ProcedurePayloadClassificationTest {
    @Test fun `predicate instructions retain truth kind in typed frames`() {
        for (prefix in listOf(
            listOf(CompilerInstruction.Constant(SanskritValue.Sankhya(2L, "द्वि")), CompilerInstruction.IsEven),
            listOf(CompilerInstruction.Constant(SanskritValue.Suchi(emptyList())),
                CompilerInstruction.Constant(SanskritValue.Shabda("राम")),
                CompilerInstruction.Collection(CollectionOperator.CONTAINS)),
        )) CompilerIrVerifier.verify(prefix + listOf(
            CompilerInstruction.EnterFrame(listOf("मान"), listOf(CompilerValueKind.BOOLEAN)),
            CompilerInstruction.ExitFrame,
        ))
    }

    @Test fun `IR classifies rational and truth constants for typed frames`() {
        for ((value, kind) in listOf(
            SanskritValue.Rational(1L, 2L, "अर्ध") to CompilerValueKind.NUMBER,
            SanskritValue.Satya(true) to CompilerValueKind.BOOLEAN,
            SanskritValue.Gana(emptyList()) to CompilerValueKind.LIST,
        )) {
            CompilerIrVerifier.verify(listOf(
                CompilerInstruction.Constant(value),
                CompilerInstruction.EnterFrame(listOf("मान"), listOf(kind)),
                CompilerInstruction.ExitFrame,
            ))
            assertFailsWith<IllegalArgumentException> {
                CompilerIrVerifier.verify(listOf(
                    CompilerInstruction.Constant(value),
                    CompilerInstruction.EnterFrame(listOf("मान"), listOf(CompilerValueKind.TEXT)),
                    CompilerInstruction.ExitFrame,
                ))
            }
        }
    }

    @Test fun `non scalar payloads do not inherit number or text contracts`() {
        val values = listOf(
            SanskritValue.Range(SanskritValue.Sankhya(1L, "एक"), SanskritValue.Sankhya(6L, "षष्")),
            SanskritValue.Satya(true),
            SanskritValue.Rupa("परीक्षण", emptyMap()),
            SanskritValue.Lopa,
        )
        for (value in values) {
            assertNotEquals(PrakriyaValueType.SANKHYA, PrakriyaValueClassifier.classifyValue(value))
            assertNotEquals(PrakriyaValueType.SHABDA, PrakriyaValueClassifier.classifyValue(value))
            for (kind in listOf("NUMBER", "TEXT")) {
                assertFailsWith<CompiledPaniniExecutionException> {
                    CompilerValueOperations.requireArgumentKind(value, kind)
                }
                assertFailsWith<CompiledPaniniExecutionException> {
                    CompilerValueOperations.requireResultKind(value, kind)
                }
            }
        }
    }

    @Test fun `compiled number and collection contracts accept their full payload families`() {
        val number = SanskritValue.Rational(1L, 2L, "अर्ध")
        val collection = SanskritValue.Gana(listOf(SanskritValue.Shabda("राम")))
        assertEquals(PrakriyaValueType.SANKHYA, PrakriyaValueClassifier.classifyValue(number))
        assertEquals(PrakriyaValueType.SUCHI, PrakriyaValueClassifier.classifyValue(collection))
        CompilerValueOperations.requireArgumentKind(number, "NUMBER")
        CompilerValueOperations.requireResultKind(number, "NUMBER")
        CompilerValueOperations.requireArgumentKind(collection, "LIST")
        CompilerValueOperations.requireResultKind(collection, "LIST")
    }
}

package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class DhatuOperationRegistryTest {
    private val action = DhatuAction { _, operation ->
        ExecutionResult.Success("सिद्धम्", operation.id)
    }

    @Test
    fun `registry rejects unknown dhatu ids`() {
        val error = assertFailsWith<IllegalArgumentException> {
            DhatuOperationRegistry(mapOf("unknown" to listOf(testOperation("one"))))
        }
        assertTrue("Unknown dhātu id" in error.message.orEmpty())
    }

    @Test
    fun `registry rejects duplicate operation ids for one dhatu`() {
        assertFailsWith<IllegalArgumentException> {
            DhatuOperationRegistry(
                mapOf("07.0007" to listOf(testOperation("same"), testOperation("same"))),
            )
        }
    }

    @Test
    fun `registry rejects indistinguishable operations`() {
        assertFailsWith<IllegalArgumentException> {
            DhatuOperationRegistry(
                mapOf("07.0007" to listOf(testOperation("one"), testOperation("two"))),
            )
        }
    }

    @Test
    fun `trigger rejects contradictory upasarga requirements`() {
        assertFailsWith<IllegalArgumentException> {
            OperationTrigger(
                requiredUpasargas = setOf("वि"),
                forbiddenUpasargas = setOf("वि"),
            )
        }
    }

    @Test
    fun `trigger rejects contradictory avyaya requirements`() {
        assertFailsWith<IllegalArgumentException> {
            OperationTrigger(
                requiredAvyayas = setOf("पुनः"),
                forbiddenAvyayas = setOf("पुनः"),
            )
        }
    }

    @Test
    fun `every built-in operation is explicitly resolvable`() {
        val registered = DhatuPatha.all.flatMap { dhatu ->
            DhatuOperationRegistry.DEFAULT.operationsFor(dhatu).map { dhatu to it }
        }
        assertEquals(19, registered.size)

        registered.forEach { (dhatu, operation) ->
            val variables = mutableMapOf<String, SanskritValue>()
            val bindings = operation.signature.requirements.associate { requirement ->
                requirement.karaka to expressionFor(requirement, variables)
            }
            val resolution = OperationResolver.resolve(
                DhatuInvocation("test", dhatu, bindings, selectedOperation = operation.id),
                variables,
            )
            assertIs<OperationResolution.Resolved>(resolution, "${dhatu.id}/${operation.id}: $resolution")
        }
    }

    @Test
    fun `operation signature resolves a syncretic binding by required karaka`() {
        val operation = DhatuOperation(
            id = "सम्प्रदान-परीक्षा",
            description = "test",
            signature = OperationSignature(listOf(KarakaRequirement(Karaka.SAMPRADANA))),
            action = action,
        )
        val registry = DhatuOperationRegistry(mapOf("07.0007" to listOf(operation)))
        val expression = ExecutionExpression.Pada("रामाभ्याम्")
        val invocation = DhatuInvocation(
            id = "test",
            dhatu = YujirDhatu(),
            bindings = emptyMap(),
            selectedOperation = operation.id,
            ambiguousBindings = listOf(
                AmbiguousKarakaBinding(
                    expression,
                    setOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
                ),
            ),
        )

        val resolved = assertIs<OperationResolution.Resolved>(
            OperationResolver.resolve(invocation, emptyMap(), registry),
        )

        assertEquals(expression, resolved.value.context.bindings[Karaka.SAMPRADANA])
    }

    private fun testOperation(id: String) = DhatuOperation(
        id = id,
        description = "test",
        signature = OperationSignature(listOf(KarakaRequirement(Karaka.KARMAN))),
        action = action,
    )

    private fun expressionFor(
        requirement: KarakaRequirement,
        variables: MutableMap<String, SanskritValue>,
    ): ExecutionExpression {
        fun member(index: Int): ExecutionExpression.Pada =
            if (ExecutionSamjna.SANKHYA in requirement.memberSamjnas) {
                ExecutionExpression.sankhya((index + 1).toLong(), "सङ्ख्या-${index + 1}")
            } else {
                ExecutionExpression.Pada(
                    "पदम्-${index + 1}",
                    requirement.memberSamjnas + ExecutionSamjna.SHABDA,
                )
            }

        return when (requirement.shape) {
            ExpressionShape.COORDINATION -> ExecutionExpression.Coordination(
                List(requirement.minimumMembers) { member(it) },
            )
            ExpressionShape.REFERENCE -> {
                val name = "पूर्वफल"
                variables[name] = member(0).value ?: SanskritValue.Shabda(member(0).prakriti)
                ExecutionExpression.Reference(name)
            }
            ExpressionShape.LITERAL, null -> member(0)
        }
    }
}

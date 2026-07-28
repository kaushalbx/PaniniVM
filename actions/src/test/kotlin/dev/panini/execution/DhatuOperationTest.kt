package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.shiksha.Samjna
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class DhatuOperationTest {

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
            dhatu.operations.map { dhatu to it }
        }
        assertEquals(47, registered.size)

        registered.forEach { (dhatu, operation) ->
            val variables = mutableMapOf<String, SanskritValue>()
            val bindings = operation.signature.requirements.associate { requirement ->
                requirement.karaka to expressionFor(requirement, variables)
            }
            val resolution = OperationResolver.resolve(
                DhatuInvocation(
                    "test",
                    dhatu,
                    bindings,
                    selectedOperation = operation.name,
                    grammaticalFeatures = GrammaticalFeatures(
                        upasargas = operation.trigger.requiredUpasargas,
                        sanadi = operation.trigger.requiredSanadi,
                        avyayas = operation.trigger.requiredAvyayas,
                        lakara = operation.trigger.allowedLakaras.firstOrNull(),
                    ),
                ),
                variables,
            )
            assertIs<OperationResolution.Resolved>(resolution, "${dhatu.id}/${operation.name}: $resolution")
        }
    }

    @Test
    fun `operation signature resolves a syncretic binding by required karaka`() {
        val customAction = object : DhatuAction("सम्प्रदान-परीक्षा", "test") {
            override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult =
                ExecutionResult.Success("सिद्धम्", operation.name)
        }
        val operation = DhatuOperation(
            signature = OperationSignature(listOf(KarakaRequirement(Karaka.SAMPRADANA))),
            action = customAction,
        )
        val yujirWithCustomOp = object : YujirDhatu() {
            override val operations = listOf(operation)
        }
        val expression = ExecutionExpression.Pada("रामाभ्याम्")
        val invocation = DhatuInvocation(
            id = "test",
            dhatu = yujirWithCustomOp,
            bindings = emptyMap(),
            selectedOperation = operation.name,
            ambiguousBindings = listOf(
                AmbiguousKarakaBinding(
                    expression,
                    setOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
                ),
            ),
        )

        val resolved = assertIs<OperationResolution.Resolved>(
            OperationResolver.resolve(invocation, emptyMap()),
        )

        assertEquals(expression, resolved.value.context.bindings[Karaka.SAMPRADANA])
    }

    private fun expressionFor(
        requirement: KarakaRequirement,
        variables: MutableMap<String, SanskritValue>,
    ): ExecutionExpression {
        fun member(index: Int): ExecutionExpression.Pada =
            if (Samjna.SANKHYA in requirement.memberSamjnas) {
                ExecutionExpression.sankhya((index + 1).toLong(), "सङ्ख्या-${index + 1}")
            } else {
                ExecutionExpression.Pada(
                    "पदम्-${index + 1}",
                    requirement.memberSamjnas + Samjna.SHABDA,
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

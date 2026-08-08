package dev.panini.execution

import dev.panini.core.Lakara

data class ActionDependency(val before: String, val after: String)

sealed interface ExecutionNode

data class ExecuteInvocation(val invocationId: String) : ExecutionNode

data class ExecuteSequence(val nodes: List<ExecutionNode>) : ExecutionNode

data class ExecuteConditional(
    val condition: ExecutionNode,
    val consequent: ExecutionNode,
    val alternate: ExecutionNode? = null,
) : ExecutionNode

/** Each iteration owns distinct invocation identities after semantic lowering. */
data class ExecuteRepeat(val iterations: List<ExecutionNode>) : ExecutionNode

data class ExecutableUkti(
    val speaker: String,
    val listener: String,
    val text: String,
    val prayojana: VakyaPrayojana,
    val polarity: Polarity = Polarity.POSITIVE,
    val lakara: Lakara? = null,
    val invocations: List<DhatuInvocation>,
    val control: ExecutionNode = ExecuteSequence(invocations.map { ExecuteInvocation(it.id) }),
    val dependencies: Set<ActionDependency> = control.dependencies(),
) {
    init {
        require(text.isNotBlank()) { "An utterance requires text." }
        require(invocations.isNotEmpty()) { "An executable utterance requires at least one dhātu invocation." }
        require(control.invocationIds() == invocations.map { it.id }) {
            "Executable control flow must reference every invocation exactly within the utterance."
        }
    }
}

fun ExecutionNode.invocationIds(): List<String> = when (this) {
    is ExecuteInvocation -> listOf(invocationId)
    is ExecuteSequence -> nodes.flatMap(ExecutionNode::invocationIds)
    is ExecuteConditional -> condition.invocationIds() + consequent.invocationIds() + alternate?.invocationIds().orEmpty()
    is ExecuteRepeat -> iterations.flatMap(ExecutionNode::invocationIds)
}

fun ExecutionNode.dependencies(): Set<ActionDependency> = controlShape().dependencies

data class ExecutionBranchGuard(val conditionInvocationId: String, val expected: Boolean)

fun ExecutionNode.branchGuards(): Map<String, ExecutionBranchGuard> = buildMap {
    fun visit(node: ExecutionNode) {
        when (node) {
            is ExecuteInvocation -> Unit
            is ExecuteSequence -> node.nodes.forEach(::visit)
            is ExecuteRepeat -> node.iterations.forEach(::visit)
            is ExecuteConditional -> {
                visit(node.condition)
                val conditionExit = node.condition.controlShape().exits.single()
                node.consequent.controlShape().entries.forEach {
                    put(it, ExecutionBranchGuard(conditionExit, true))
                }
                node.alternate?.controlShape()?.entries?.forEach {
                    put(it, ExecutionBranchGuard(conditionExit, false))
                }
                visit(node.consequent)
                node.alternate?.let(::visit)
            }
        }
    }
    visit(this@branchGuards)
}

private data class ControlShape(
    val entries: Set<String>,
    val exits: Set<String>,
    val dependencies: Set<ActionDependency>,
)

private fun ExecutionNode.controlShape(): ControlShape = when (this) {
    is ExecuteInvocation -> ControlShape(setOf(invocationId), setOf(invocationId), emptySet())
    is ExecuteSequence -> sequenceShape(nodes)
    is ExecuteRepeat -> sequenceShape(iterations)
    is ExecuteConditional -> {
        val conditionShape = condition.controlShape()
        val branchShapes = listOfNotNull(consequent.controlShape(), alternate?.controlShape())
        ControlShape(
            entries = conditionShape.entries,
            exits = branchShapes.flatMapTo(linkedSetOf()) { it.exits },
            dependencies = conditionShape.dependencies +
                branchShapes.flatMap { it.dependencies } +
                conditionShape.exits.flatMap { before ->
                    branchShapes.flatMap { branch -> branch.entries.map { after -> ActionDependency(before, after) } }
                },
        )
    }
}

private fun sequenceShape(nodes: List<ExecutionNode>): ControlShape {
    require(nodes.isNotEmpty()) { "Executable sequences and repetitions cannot be empty." }
    val shapes = nodes.map(ExecutionNode::controlShape)
    val boundaryDependencies = shapes.zipWithNext().flatMap { (before, after) ->
        before.exits.flatMap { beforeId ->
            after.entries.map { afterId -> ActionDependency(beforeId, afterId) }
        }
    }
    return ControlShape(
        entries = shapes.first().entries,
        exits = shapes.last().exits,
        dependencies = shapes.flatMapTo(linkedSetOf()) { it.dependencies } + boundaryDependencies,
    )
}

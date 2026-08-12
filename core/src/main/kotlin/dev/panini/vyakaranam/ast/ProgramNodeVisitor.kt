package dev.panini.vyakaranam.ast

interface ProgramNodeVisitor<out R> {
    fun visitInvocation(node: Invocation): R
    fun visitSequence(node: Sequence): R
    fun visitConditional(node: Conditional): R
    fun visitQuotation(node: Quotation): R
    fun visitRepeat(node: Repeat): R
    fun visitWhileLoop(node: WhileLoop): R
    fun visitPipeline(node: Pipeline): R
    fun visitProcedure(node: Procedure): R
    fun visitScope(node: Scope): R
}

fun <R> ProgramNode.accept(visitor: ProgramNodeVisitor<R>): R = when (this) {
    is Invocation -> visitor.visitInvocation(this)
    is Sequence -> visitor.visitSequence(this)
    is Conditional -> visitor.visitConditional(this)
    is Quotation -> visitor.visitQuotation(this)
    is Repeat -> visitor.visitRepeat(this)
    is WhileLoop -> visitor.visitWhileLoop(this)
    is Pipeline -> visitor.visitPipeline(this)
    is Procedure -> visitor.visitProcedure(this)
    is Scope -> visitor.visitScope(this)
}

/** Direct structural children in source order. */
fun ProgramNode.children(): List<ProgramNode> = accept(ProgramNodeChildren)

/**
 * Visits this node and all of its descendants in depth-first source order.
 *
 * When [expandRepeats] is true, a repeat body is visited once per iteration.
 * Conditional alternatives remain structural alternatives; choosing a branch
 * is an execution concern rather than an AST traversal concern.
 */
fun ProgramNode.depthFirst(expandRepeats: Boolean = false): kotlin.sequences.Sequence<ProgramNode> = sequence {
    yield(this@depthFirst)
    val structuralChildren = children()
    if (expandRepeats && this@depthFirst is Repeat) {
        repeat(count) {
            yieldAll(body.depthFirst(expandRepeats = true))
        }
    } else {
        structuralChildren.forEach { child ->
            yieldAll(child.depthFirst(expandRepeats))
        }
    }
}

private object ProgramNodeChildren : ProgramNodeVisitor<List<ProgramNode>> {
    override fun visitInvocation(node: Invocation): List<ProgramNode> = emptyList()
    override fun visitSequence(node: Sequence): List<ProgramNode> = node.statements
    override fun visitConditional(node: Conditional): List<ProgramNode> =
        listOfNotNull(node.condition, node.consequent, node.alternate)
    override fun visitQuotation(node: Quotation): List<ProgramNode> = listOf(node.reporting)
    override fun visitRepeat(node: Repeat): List<ProgramNode> = listOf(node.body)
    override fun visitWhileLoop(node: WhileLoop): List<ProgramNode> =
        listOfNotNull(node.body, node.exhausted, node.resultTarget)
    override fun visitPipeline(node: Pipeline): List<ProgramNode> = emptyList()
    override fun visitProcedure(node: Procedure): List<ProgramNode> = node.body
    override fun visitScope(node: Scope): List<ProgramNode> = node.body
}

open class ProgramNodeTransformer : ProgramNodeVisitor<ProgramNode> {
    fun transform(node: ProgramNode): ProgramNode = node.accept(this)

    override fun visitInvocation(node: Invocation): ProgramNode = node

    override fun visitSequence(node: Sequence): ProgramNode =
        node.copy(statements = node.statements.map(::transform))

    override fun visitConditional(node: Conditional): ProgramNode = node.copy(
        condition = transform(node.condition),
        consequent = transform(node.consequent),
        alternate = node.alternate?.let(::transform),
    )

    override fun visitQuotation(node: Quotation): ProgramNode =
        node.copy(reporting = transform(node.reporting))

    override fun visitRepeat(node: Repeat): ProgramNode =
        node.copy(body = transform(node.body))

    override fun visitWhileLoop(node: WhileLoop): ProgramNode =
        node.copy(
            body = transform(node.body),
            exhausted = node.exhausted?.let(::transform),
            resultTarget = node.resultTarget?.let(::transform),
        )

    override fun visitPipeline(node: Pipeline): ProgramNode = node

    override fun visitProcedure(node: Procedure): ProgramNode =
        node.copy(body = node.body.map(::transform))

    override fun visitScope(node: Scope): ProgramNode =
        node.copy(body = node.body.map(::transform))
}

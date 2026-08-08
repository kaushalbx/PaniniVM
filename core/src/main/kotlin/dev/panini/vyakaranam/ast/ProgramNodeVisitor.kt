package dev.panini.vyakaranam.ast

interface ProgramNodeVisitor<out R> {
    fun visitInvocation(node: Invocation): R
    fun visitSequence(node: Sequence): R
    fun visitConditional(node: Conditional): R
    fun visitRepeat(node: Repeat): R
    fun visitPipeline(node: Pipeline): R
    fun visitProcedure(node: Procedure): R
    fun visitScope(node: Scope): R
}

fun <R> ProgramNode.accept(visitor: ProgramNodeVisitor<R>): R = when (this) {
    is Invocation -> visitor.visitInvocation(this)
    is Sequence -> visitor.visitSequence(this)
    is Conditional -> visitor.visitConditional(this)
    is Repeat -> visitor.visitRepeat(this)
    is Pipeline -> visitor.visitPipeline(this)
    is Procedure -> visitor.visitProcedure(this)
    is Scope -> visitor.visitScope(this)
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

    override fun visitRepeat(node: Repeat): ProgramNode =
        node.copy(body = transform(node.body))

    override fun visitPipeline(node: Pipeline): ProgramNode = node

    override fun visitProcedure(node: Procedure): ProgramNode =
        node.copy(body = node.body.map(::transform))

    override fun visitScope(node: Scope): ProgramNode =
        node.copy(body = node.body.map(::transform))
}

package dev.panini.plugin

import dev.panini.vyakaranam.ast.Pada
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.SamuccitaSubanta
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.VyakaranamNode
import dev.panini.vyakaranam.ast.grammaticalInvocations

/** Read-only PSI projection helpers; grammatical identity remains in the canonical AST. */
internal fun VyakaranamNode.canonicalProgramNodes(): List<ProgramNode> = when (this) {
    is Prakriya -> body
    is Scope -> body
    is Ukti -> listOf(body)
    else -> emptyList()
}

internal fun Pada.sourceLocatedLeaves(): List<Pada> = when (this) {
    is SourceLocatedNode -> listOf(this)
    is SamuccitaSubanta -> members.flatMap { it.sourceLocatedLeaves() }
    is ParyantaRangePada -> listOf(lowerLimit, upperLimit, marker).flatMap { it.sourceLocatedLeaves() }
    else -> emptyList()
}

internal fun VyakaranamNode.canonicalLocatedPadas(): List<Pada> =
    canonicalProgramNodes()
        .flatMap(ProgramNode::grammaticalInvocations)
        .flatMap { it.vakya.padas }
        .flatMap(Pada::sourceLocatedLeaves)
        .distinctBy { (it as SourceLocatedNode).sourceSpan }

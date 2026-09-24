package dev.panini.execution

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Sequence

/** Semantic classification computed once when a script sentence is parsed. */
sealed interface PvmSentenceSemantics {
    data class SchemaDeclaration(val schema: TaddhitaStructSchema) : PvmSentenceSemantics
    data class StructConstruction(val struct: TaddhitaStruct) : PvmSentenceSemantics
    data class AttributeAccess(val access: TaddhitaAttributeAccess) : PvmSentenceSemantics
    data class AttributePipeline(val access: TaddhitaAttributeAccess, val targets: List<Invocation>) :
        PvmSentenceSemantics
    data class StructuredConditional(val conditional: Conditional) : PvmSentenceSemantics
    data object Executable : PvmSentenceSemantics
}

/** Performs the one-time AST inspection needed to dispatch a script sentence. */
internal object PvmSentenceClassifier {
    fun classify(sentence: PvmScriptStatement.Sentence): PvmSentenceSemantics {
        TaddhitaStructEngine.detectResultSchema(sentence.text, sentence.ukti)?.let {
            return PvmSentenceSemantics.SchemaDeclaration(it)
        }
        TaddhitaStructEngine.detectStructConstruction(sentence.text, sentence.ukti)?.let {
            return PvmSentenceSemantics.StructConstruction(it)
        }
        sentence.ukti?.grammaticalVakyas()?.singleOrNull()
            ?.let(TaddhitaStructEngine::detectAttributeAccess)?.let {
                return PvmSentenceSemantics.AttributeAccess(it)
            }
        val program = sentence.program
        detectAttributePipeline(program)?.let { return it }
        val conditional = program as? Conditional
        if (conditional != null && containsAttributeCondition(conditional)) {
            return PvmSentenceSemantics.StructuredConditional(conditional)
        }
        return PvmSentenceSemantics.Executable
    }

    fun containsAttributeCondition(conditional: Conditional): Boolean =
        ((conditional.condition as? Invocation)?.vakya
            ?.let(TaddhitaStructEngine::detectAttributeReference) != null) ||
            (conditional.alternate as? Conditional)?.let(::containsAttributeCondition) == true

    private fun detectAttributePipeline(program: dev.panini.vyakaranam.ast.ProgramNode?):
        PvmSentenceSemantics.AttributePipeline? {
        val sequence = program as? Sequence ?: return null
        if (sequence.statements.size < 2 || sequence.connectors.any { it != "ततः" }) return null
        val source = sequence.statements.first() as? Invocation ?: return null
        val targets = sequence.statements.drop(1).map { it as? Invocation ?: return null }
        val access = TaddhitaStructEngine.detectAttributeAccess(source.vakya) ?: return null
        return PvmSentenceSemantics.AttributePipeline(access, targets)
    }
}

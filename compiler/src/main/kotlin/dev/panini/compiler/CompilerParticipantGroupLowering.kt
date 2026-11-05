package dev.panini.compiler

import dev.panini.core.Karaka
import dev.panini.execution.*
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import dev.panini.execution.binding.referenceKey
import dev.panini.vyakaranam.ast.*

/** Keep value-dependent overload selection at runtime while preserving analyzed operand structure. */
internal object CompilerParticipantGroupLowering {
    fun lower(node: Invocation, environment: ValueEnvironment): List<CompilerInstruction>? {
        val sentence = node.vakya as? AkhyataVakya ?: return null
        val ukti = Ukti(node.sourceText, body = node)
        val frame = VyakaranamExecutionAdapter.analyzeGrammar(ukti)?.frames?.singleOrNull() ?: return null
        val objects = frame.relations.filter {
            (it.resolution as? dev.panini.analysis.FrameKarakaResolution.Resolved)?.karaka == Karaka.KARMAN
        }
        if (objects.size < 2 || objects.any { it.resolvedVibhakti == null }) return null
        val head = sentence.tinganta
        val features = GrammaticalFeatures(head.upasargas.toSet(), head.dhatu.sanadiPratyayas.toSet(), lakara = head.lakara)
        val candidates = frame.kriya?.dhatu?.operations.orEmpty().filter { it.trigger.matches(features) }
        if (candidates.isEmpty() || candidates.any { it.name !in participantGroupOperations } ||
            candidates.all { it.action is dev.panini.actions.numeric.NumericFoldDhatuAction }) return null
        val names = objects.mapNotNull { relation ->
            val stem = (relation.participant.pada as? SubantaPada)?.pratipadika as? MulaPratipadika ?: return null
            if (stem.vikaras.isNotEmpty()) return null
            when (relation.participant.semanticValue) {
                is SanskritValue.Sankhya, is SanskritValue.Purana, is SanskritValue.Satya -> null
                else -> stem.referenceKey()
            }
        }
        val symbolic = ValueEnvironment(values = environment.values,
            constraints = names.associateWith { SemanticValueConstraint() } + environment.constraints)
        val bound = VyakaranamExecutionAdapter.bind(SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", node.sourceText), ukti,
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"), environment = symbolic) as? ExecutionBindingResult.Bound ?: return null
        val invocation = bound.ukti.invocations.singleOrNull() ?: return null
        val operands = invocation.bindings[Karaka.KARMAN] as? ExecutionExpression.Coordination ?: return null
        val loads = operands.members.flatMap(CompilerIrLowering::lowerParticipantOperands)
        return buildList {
            loads.forEach { addAll(it) }
            add(CompilerInstruction.PackOperands(List(loads.size) { false }, asGroup = true))
            add(CompilerInstruction.CallParticipantGroup(invocation.dhatu.upadesha, invocation.grammaticalFeatures,
                invocation.selectedOperation, invocation.bindings.filterKeys { it !in setOf(Karaka.KARMAN, Karaka.ANIRDHARITA) },
                historyParticipants = invocation.historyParticipants))
            add(CompilerInstruction.Store("LastResult"))
        }
    }
}

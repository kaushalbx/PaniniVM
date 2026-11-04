package dev.panini.analysis

import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.ProgramNodeVisitor
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.ast.grammaticalInvocations
import dev.panini.vyakaranam.ast.accept
import dev.panini.vyakaranam.ast.morphologicalKey

data class UktiAnalysis(
    val ukti: Ukti,
    val frames: List<KriyaFrame>,
    val links: List<KriyaLink>,
    val diagnostics: List<FrameDiagnostic> = emptyList(),
) {
    val blockingDiagnostics: List<FrameDiagnostic>
        get() = (diagnostics + frames.flatMap { it.diagnostics }).filter(FrameDiagnosticPolicy::blocksExecution)
}

/**
 * Performs the second, utterance-level stage of analysis.
 *
 * A [VakyaAnalyzer] first assigns the padas of each finite clause to that
 * clause's kriya frame. This analyzer then relates those completed frames
 * according to the construction retained by the parser.
 */
class UktiAnalyzer(
    private val analyzeVakya: (dev.panini.vyakaranam.ast.Vakya, KriyaId) -> KriyaFrame,
) {
    constructor(vakyaAnalyzer: VakyaAnalyzer) : this(vakyaAnalyzer::analyze)

    fun analyze(ukti: Ukti): UktiAnalysis {
        val diagnostics = mutableListOf<FrameDiagnostic>()
        val sourceVakyas = java.util.IdentityHashMap<dev.panini.vyakaranam.ast.Vakya, dev.panini.vyakaranam.ast.Vakya>()
        val grammaticalBody = object : dev.panini.vyakaranam.ast.ProgramNodeTransformer() {
            override fun visitInvocation(node: Invocation): ProgramNode =
                when (val prior = priorActionStructure(node)) {
                    dev.panini.vyakaranam.ast.PriorActionStructure.Analysis.NotPriorAction -> node
                    is dev.panini.vyakaranam.ast.PriorActionStructure.Analysis.Resolved -> prior.sequence.also {
                        it.statements.filterIsInstance<Invocation>().forEach { action -> sourceVakyas[action.vakya] = node.vakya }
                    }
                    is dev.panini.vyakaranam.ast.PriorActionStructure.Analysis.Invalid -> {
                        diagnostics += FrameDiagnostic(FrameDiagnosticCode.PRIOR_ACTION_STRUCTURE,
                            "${prior.diagnostic.code}: ${prior.diagnostic.message}", node.sourceText)
                        node
                    }
                }
        }.transform(ukti.body)
        val initialFrames = grammaticalBody.grammaticalInvocations().mapIndexed { index, invocation ->
            analyzeVakya(invocation.vakya, KriyaId("kriya-${index + 1}")).copy(
                sourceVakya = sourceVakyas[invocation.vakya] ?: invocation.vakya)
        }
        val links = buildLinks(ukti.copy(body = grammaticalBody), initialFrames, diagnostics)
        return UktiAnalysis(ukti, initialFrames, links, diagnostics)
    }

    private fun priorActionStructure(node: Invocation): dev.panini.vyakaranam.ast.PriorActionStructure.Analysis {
        val structure = dev.panini.vyakaranam.ast.PriorActionStructure
        val initial = structure.analyze(node)
        if (initial !is dev.panini.vyakaranam.ast.PriorActionStructure.Analysis.Invalid ||
            initial.diagnostic.code != "PRIOR_ACTION_PASSIVE_PREPOSED_INSTRUMENTAL") return initial
        val candidate = structure.passiveOwnershipCandidate(node) ?: return initial
        val frame = analyzeVakya(candidate, KriyaId("prior-ownership-candidate"))
        val agents = frame.relations.filter { relation ->
            (relation.resolution as? FrameKarakaResolution.Resolved)?.karaka == dev.panini.core.Karaka.KARTR &&
                relation.resolvedVibhakti == dev.panini.core.Vibhakti.TRTIYA &&
                relation.evidence.flatMap { it.semanticEvidence }.any {
                    it.relation in setOf(SemanticRelation.INDEPENDENT_AGENT, SemanticRelation.PROMPTER_CAUSE) &&
                        it.source in setOf(SemanticEvidenceSource.LEXICAL_ENTRY, SemanticEvidenceSource.CLAUSE_CONTEXT)
                }
        }.map { it.participant.pada }
        return if (agents.isEmpty()) initial else structure.analyze(node, establishedFiniteAgents = agents)
    }

    private fun buildLinks(
        ukti: Ukti, frames: List<KriyaFrame>, diagnostics: MutableList<FrameDiagnostic>,
    ): List<KriyaLink> {
        if (frames.size < 2) return emptyList()
        val structuralLinks = buildStructuralLinks(ukti.body, frames)

        val semanticLinks = buildList {
            val byId = frames.associateBy { it.id }
            structuralLinks.filterIsInstance<KriyaLink.Coordination>().forEach { edge ->
                val source = byId.getValue(edge.source)
                val target = byId.getValue(edge.target)
                val nonfiniteAffixes = source.vakya.padas.mapNotNull { pada ->
                    when (pada) {
                        is dev.panini.vyakaranam.ast.AvyayaPada ->
                            (pada.derivation as? dev.panini.vyakaranam.ast.AvyayaKridantaDerivation)?.krtAffix
                        is dev.panini.vyakaranam.ast.TingantaPada -> pada.priorAction?.krtAffix
                        else -> null
                    }
                }
                val isPurvakalika = nonfiniteAffixes.any {
                    it == dev.panini.core.KrtAffix.KTVA || it == dev.panini.core.KrtAffix.LYAP
                }
                val isPurpose = dev.panini.core.KrtAffix.TUMUN in nonfiniteAffixes

                if (isPurvakalika) {
                    add(KriyaLink.Purvakalika(source.id, target.id))
                }
                if (isPurpose) {
                    add(KriyaLink.Purpose(source.id, target.id))
                }
                if (isPurvakalika || isPurpose) {
                    fun agents(frame: KriyaFrame) = frame.relations.filter {
                        (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == dev.panini.core.Karaka.KARTR
                    }.map { it.participant.pada }
                    fun uncertainAgent(frame: KriyaFrame) = frame.relations.any {
                        val resolution = it.resolution
                        (resolution is FrameKarakaResolution.Unassigned &&
                            dev.panini.core.Karaka.KARTR in resolution.candidates) ||
                            resolution is FrameKarakaResolution.Ambiguous && dev.panini.core.Karaka.KARTR in resolution.candidates
                    }
                    fun identities(padas: List<dev.panini.vyakaranam.ast.SupBearingPada>) = padas.map {
                        val nominalIdentity: Any = when (it) {
                            is dev.panini.vyakaranam.ast.SubantaPada -> it.pratipadika.morphologicalKey()
                            is dev.panini.vyakaranam.ast.SankhyaPuranaPada -> Triple(it.stems, it.value, it.striAffix)
                            else -> it
                        }
                        nominalIdentity to it.sup.affixCandidates.map { affix -> affix.vacana }.toSet()
                    }.groupingBy { it }.eachCount()
                    val sourceAgents = agents(source)
                    val targetAgents = agents(target)
                    val explicitMatch = sourceAgents.isNotEmpty() && targetAgents.isNotEmpty() &&
                        identities(sourceAgents) == identities(targetAgents)
                    if (isPurvakalika && sourceAgents.isNotEmpty() && targetAgents.isNotEmpty() && !explicitMatch) {
                        diagnostics += FrameDiagnostic(FrameDiagnosticCode.CONFLICTING_SHARED_AGENT,
                            "Prior-action and main-action agents are not structurally identical; coreference must not be assumed.",
                            source.vakya.sourceText)
                    }
                    val omittedPriorAgent = isPurvakalika && (sourceAgents.isEmpty() || targetAgents.isEmpty())
                    if ((explicitMatch || omittedPriorAgent) && !uncertainAgent(source) && !uncertainAgent(target)) {
                        val explicitIn = buildSet {
                            if (sourceAgents.isNotEmpty()) add(source.id)
                            if (targetAgents.isNotEmpty()) add(target.id)
                        }
                        val omittedIn = setOf(source.id, target.id) - explicitIn
                        (sourceAgents.ifEmpty { targetAgents }).forEach { agent ->
                            add(KriyaLink.SharedParticipant(
                                source.id,
                                target.id,
                                agent.sourceText,
                                dev.panini.core.Karaka.KARTR,
                                explicitIn,
                                omittedIn,
                            ))
                        }
                    }
                }
            }
        }

        // A written chain may contain several prior actions. Adjacent temporal
        // edges alone cannot carry the finite action's agent to the first one.
        val chainAgents = buildList {
            frames.filter { it.kriya?.isFinite == true }.forEach { main ->
                val agents = main.relations.filter {
                    (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == dev.panini.core.Karaka.KARTR
                }
                fun uncertain(frame: KriyaFrame) = frame.relations.any {
                    when (val resolution = it.resolution) {
                        is FrameKarakaResolution.Unassigned -> dev.panini.core.Karaka.KARTR in resolution.candidates
                        is FrameKarakaResolution.Ambiguous -> dev.panini.core.Karaka.KARTR in resolution.candidates
                        else -> false
                    }
                }
                if (uncertain(main)) return@forEach
                frames.filter { prior ->
                    prior.sourceVakya === main.sourceVakya && prior !== main &&
                        prior.kriya?.nonfiniteDerivation?.krtAffix in setOf(
                            dev.panini.core.KrtAffix.KTVA, dev.panini.core.KrtAffix.LYAP)
                }.forEach { prior ->
                    if (!uncertain(prior) && prior.relations.none {
                            (it.resolution as? FrameKarakaResolution.Resolved)?.karaka == dev.panini.core.Karaka.KARTR
                        }) {
                        agents.forEach { agent ->
                            add(KriyaLink.SharedParticipant(prior.id, main.id,
                                agent.participant.pada.sourceText, dev.panini.core.Karaka.KARTR,
                                setOf(main.id), setOf(prior.id)))
                        }
                    }
                }
            }
        }
        return (structuralLinks + semanticLinks + chainAgents).distinct()
    }

    private data class FrameBoundary(val entry: KriyaId, val exits: Set<KriyaId>)

    private fun buildStructuralLinks(
        root: ProgramNode,
        frames: List<KriyaFrame>,
    ): List<KriyaLink> {
        var nextFrame = 0
        val links = mutableListOf<KriyaLink>()

        val visitor = object : ProgramNodeVisitor<FrameBoundary> {
            private fun visit(node: ProgramNode): FrameBoundary = node.accept(this)

            override fun visitInvocation(node: Invocation): FrameBoundary =
                frames[nextFrame++].id.let { FrameBoundary(it, setOf(it)) }

            override fun visitSequence(node: Sequence): FrameBoundary {
                val ranges = node.statements.map(::visit)
                ranges.zipWithNext().forEachIndexed { index, (source, target) ->
                    source.exits.forEach { exit ->
                        links += KriyaLink.Coordination(
                            source = exit,
                            target = target.entry,
                            connector = node.connectors.getOrElse(index) { "" },
                        )
                    }
                }
                return FrameBoundary(ranges.first().entry, ranges.last().exits)
            }

            override fun visitConditional(node: Conditional): FrameBoundary {
                val condition = visit(node.condition)
                val consequent = visit(node.consequent)
                condition.exits.forEach { links += KriyaLink.Condition(it, consequent.entry) }
                val alternate = node.alternate?.let(::visit)
                if (alternate != null) {
                    condition.exits.forEach { links += KriyaLink.Alternative(it, alternate.entry) }
                }
                // Without an alternate, the false condition bypasses the body.
                return FrameBoundary(condition.entry, consequent.exits + (alternate?.exits ?: condition.exits))
            }
            override fun visitQuotation(node: Quotation): FrameBoundary = visit(node.reporting)

            override fun visitRepeat(node: Repeat): FrameBoundary = visit(node.body)
            override fun visitRecurrence(node: dev.panini.vyakaranam.ast.Recurrence): FrameBoundary = visit(node.body)
            override fun visitWhileLoop(node: WhileLoop): FrameBoundary {
                val condition = visit(node.condition)
                val body = visit(node.body)
                val exhausted = node.exhausted?.let(::visit)
                val result = node.resultTarget?.let(::visit)
                condition.exits.forEach { links += KriyaLink.Condition(it, body.entry) }
                body.exits.forEach { links += KriyaLink.LoopBack(it, condition.entry) }
                if (exhausted != null) {
                    body.exits.forEach { links += KriyaLink.Exhaustion(it, exhausted.entry) }
                }
                // Body completion can also exit through break or a reached bound.
                // Detailed termination signals are an execution/IR concern.
                val exits = condition.exits + body.exits + exhausted?.exits.orEmpty()
                if (result != null) {
                    exits.forEach { links += KriyaLink.LoopResult(it, result.entry) }
                }
                return FrameBoundary(condition.entry, result?.exits ?: exits)
            }
            override fun visitForEach(node: dev.panini.vyakaranam.ast.ForEach): FrameBoundary = visit(node.body)
            override fun visitCollectionRelativeAction(
                node: dev.panini.vyakaranam.ast.CollectionRelativeAction,
            ): FrameBoundary {
                val action = visit(node.actionInvocation())
                val continuation = node.continuation?.let(::visit) ?: return action
                action.exits.forEach { exit ->
                    links += KriyaLink.Coordination(exit, continuation.entry, "ततः")
                }
                return FrameBoundary(action.entry, continuation.exits)
            }
            override fun visitQuantifiedCollectionPredicate(
                node: dev.panini.vyakaranam.ast.QuantifiedCollectionPredicate,
            ): FrameBoundary = visit(node.predicate)
            override fun visitPipeline(node: Pipeline): FrameBoundary =
                error("A semantic pipeline does not contain grammatical kriya frames.")
            override fun visitPrakriya(node: Prakriya): FrameBoundary =
                error("A procedure declaration is not an analyzable utterance.")
            override fun visitScope(node: Scope): FrameBoundary =
                error("A scope declaration is not an analyzable utterance.")
        }

        root.accept(visitor)
        return links
    }
}

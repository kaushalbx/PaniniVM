package dev.panini.vyakaranam.parser

import dev.panini.core.Lakara
import dev.panini.core.Linga
import dev.panini.core.SupAffix
import dev.panini.core.TingAffix
import dev.panini.core.Vibhakti
import dev.panini.core.SupLopa
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon
import dev.panini.parser.VyakaranamParser as PaniniyaVyakaranamParser
import org.antlr.v4.kotlinruntime.ParserRuleContext

class VyakaranamAstBuilder {

    private fun semanticError(
        context: ParserRuleContext,
        code: PaniniSemanticDiagnosticCode,
        message: String,
    ): Nothing {
        val start = requireNotNull(context.start)
        val stop = requireNotNull(context.stop)
        throw PaniniSemanticException(PaniniSemanticDiagnostic(
            code = code,
            message = message,
            span = DocumentSourceSpan(start.startIndex, stop.stopIndex + 1),
            line = start.line,
            column = start.charPositionInLine,
            offendingText = context.text,
        ))
    }

    private fun semanticRequire(
        condition: Boolean,
        context: ParserRuleContext,
        code: PaniniSemanticDiagnosticCode,
        message: String,
    ) {
        if (!condition) semanticError(context, code, message)
    }

    fun buildDocument(context: PaniniyaVyakaranamParser.DocumentContext): ProgramDocument {
        val spans = context.documentItem().mapTo(mutableListOf()) { item ->
            DocumentSourceSpan(requireNotNull(item.start).startIndex, requireNotNull(item.stop).stopIndex + 1)
        }
        val items = context.documentItem().mapTo(mutableListOf<VyakaranamNode>()) { item ->
            item.prakriyaBlock()?.let(::buildPrakriya)
                ?: item.prakriyaHeader()?.let { buildPrakriyaHeader(it, item.text, emptyList()) }
                ?: classifyDocumentUtterance(build(requireNotNull(item.utterance())))
        }
        context.trailing?.let {
            items += classifyDocumentUtterance(build(it))
            spans += DocumentSourceSpan(requireNotNull(it.start).startIndex, requireNotNull(it.stop).stopIndex + 1)
        }
        context.trailingHeader?.let {
            items += buildPrakriyaHeader(it, it.text, emptyList())
            spans += DocumentSourceSpan(requireNotNull(it.start).startIndex, requireNotNull(it.stop).stopIndex + 1)
        }
        val headers = context.documentItem().mapIndexedNotNull { index, item ->
            (item.prakriyaBlock()?.prakriyaHeader() ?: item.prakriyaHeader())?.let { header ->
                index to DocumentSourceSpan(requireNotNull(header.start).startIndex,
                    requireNotNull(header.stop).stopIndex + 1)
            }
        }.toMap().toMutableMap()
        context.trailingHeader?.let {
            headers[items.lastIndex] = DocumentSourceSpan(requireNotNull(it.start).startIndex,
                requireNotNull(it.stop).stopIndex + 1)
        }
        val bodies = context.documentItem().mapIndexedNotNull { index, item ->
            item.prakriyaBlock()?.let { block ->
                index to block.body.mapIndexed { bodyIndex, body ->
                    val terminator = if (bodyIndex == block.body.lastIndex) requireNotNull(block.stop)
                        else block.DANDA()[bodyIndex + 1].symbol
                    DocumentSourceSpan(requireNotNull(body.start).startIndex, terminator.stopIndex + 1)
                }
            }
        }.toMap()
        val names = context.documentItem().mapIndexedNotNull { index, item ->
            (item.prakriyaBlock()?.prakriyaHeader() ?: item.prakriyaHeader())?.names?.lastOrNull()?.let {
                index to DocumentSourceSpan(requireNotNull(it.start).startIndex, requireNotNull(it.stop).stopIndex + 1)
            }
        }.toMap().toMutableMap()
        context.trailingHeader?.names?.lastOrNull()?.let {
            names[items.lastIndex] = DocumentSourceSpan(requireNotNull(it.start).startIndex,
                requireNotNull(it.stop).stopIndex + 1)
        }
        fun scopeSpan(utterance: PaniniyaVyakaranamParser.UtteranceContext): DocumentSourceSpan? {
            val domain = utterance.vakya().firstOrNull()?.namaVakya()?.vakyaPada()?.firstOrNull()
                ?: return null
            return DocumentSourceSpan(requireNotNull(domain.start).startIndex, requireNotNull(domain.stop).stopIndex + 1)
        }
        val domains = context.documentItem().mapIndexedNotNull { index, item ->
            if (items[index] is Scope) item.utterance()?.let(::scopeSpan)?.let { index to it } else null
        }.toMap().toMutableMap()
        context.trailing?.let { if (items.last() is Scope) scopeSpan(it)?.let { span -> domains[items.lastIndex] = span } }
        return ProgramDocument(context.text, items, spans, headers, bodies, names, domains)
    }

    private fun classifyDocumentUtterance(ukti: Ukti): VyakaranamNode {
        val quotation = ukti.body as? Quotation
        if (quotation != null && quotation.quoted.vakya is NamaVakya &&
            quotation.reporting.invocations().flatMap { it.vakya.padas }.filterIsInstance<SubantaPada>()
                .any { (it.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.PRAKRIYA }) {
            error("A prakriyā declaration requires a body ending in ॥.")
        }
        val padas = ((ukti.body as? Invocation)?.vakya as? NamaVakya)?.padas ?: return ukti
        if (padas.none { (it as? AvyayaPada)?.function == AvyayaFunction.QUOTATIVE }) return ukti
        val marker = padas.lastOrNull() as? SubantaPada ?: return ukti
        val identity = (marker.pratipadika as? MulaPratipadika)?.lexicalIdentity
        if (identity == MulaPratipadikaIdentity.SIMA) {
            require(padas.size == 3 && padas.first() is ParyantaRangePada &&
                marker.sup.supports(Vibhakti.PRATHAMA)) {
                "A सीमा declaration requires ablative and पर्यन्त bounds and nominative सीमा."
            }
            return RangeDeclaration(ukti.sourceText, padas.first() as ParyantaRangePada, marker)
        }
        val isScope = identity == MulaPratipadikaIdentity.ADHIKARA ||
            (marker.pratipadika as? KridantaPratipadika)?.lexicalIdentity == KridantaLexicalIdentity.ADHIKARA
        if (isScope) {
            val domain = padas.first() as? SubantaPada
            require(padas.size == 3 && domain != null &&
                domain.sup.supports(Vibhakti.PRATHAMA) &&
                marker.sup.supports(Vibhakti.PRATHAMA)) {
                "An अधिकार declaration requires a nominative domain and marker."
            }
            return Scope(ukti.sourceText, domain.sourceText, domain.pratipadika.morphologicalKey())
        }
        return ukti
    }

    fun buildScopeDeclaration(context: PaniniyaVyakaranamParser.ScopeDeclarationContext): Scope {
        val domain = buildSubanta(requireNotNull(context.domain))
        val marker = buildSubanta(requireNotNull(context.marker))
        require(domain.sup.supports(Vibhakti.PRATHAMA) &&
            marker.sup.supports(Vibhakti.PRATHAMA)) {
            "An अधिकार declaration requires nominative domain and marker."
        }
        require(when (val stem = marker.pratipadika) {
            is MulaPratipadika -> stem.lexicalIdentity == MulaPratipadikaIdentity.ADHIKARA
            is KridantaPratipadika -> stem.lexicalIdentity == KridantaLexicalIdentity.ADHIKARA
            else -> false
        }) { "A scope declaration requires अधिकार." }
        return Scope(context.text, domain.sourceText, domain.pratipadika.morphologicalKey())
    }

    fun buildRangeDeclaration(context: PaniniyaVyakaranamParser.RangeDeclarationContext): RangeDeclaration {
        val marker = buildSubanta(requireNotNull(context.marker))
        require((marker.pratipadika as? MulaPratipadika)?.lexicalIdentity == MulaPratipadikaIdentity.SIMA &&
            marker.sup.supports(Vibhakti.PRATHAMA)) {
            "An inclusive range declaration requires nominative सीमा."
        }
        return RangeDeclaration(context.text, buildParyantaRange(requireNotNull(context.boundary)), marker)
    }

    fun buildPrakriya(context: PaniniyaVyakaranamParser.PrakriyaBlockContext): Prakriya {
        return buildPrakriyaHeader(context.prakriyaHeader(), context.text, context.body.map { build(it).body })
    }

    private fun buildPrakriyaHeader(
        context: PaniniyaVyakaranamParser.PrakriyaHeaderContext,
        sourceText: String,
        body: List<ProgramNode>,
    ): Prakriya {
        val names = context.names.map(::buildSubanta)
        semanticRequire(names.size in 1..2, context,
            PaniniSemanticDiagnosticCode.PRAKRIYA_HEADER_CARDINALITY,
            "A prakriyā declaration names one operation and optionally one domain.")
        val operation = names.last()
        semanticRequire(operation.sup.supports(Vibhakti.PRATHAMA, dev.panini.core.Vacana.EKAVACANA),
            context.names.last(), PaniniSemanticDiagnosticCode.PRAKRIYA_OPERATION_AGREEMENT,
            "A single prakriyā declaration requires a nominative singular name.")
        val domain = names.dropLast(1).singleOrNull()
        semanticRequire(domain == null || domain.sup.supports(Vibhakti.SASTHI, dev.panini.core.Vacana.EKAVACANA),
            context.names.first(), PaniniSemanticDiagnosticCode.PRAKRIYA_DOMAIN_AGREEMENT,
            "A single prakriyā declaration requires a genitive singular domain.")
        val declarationContext = requireNotNull(context.declaration)
        val marker = SubantaPada(
            "प्रक्रिया+${declarationContext.markerSup!!.text}",
            MulaPratipadika("प्रक्रिया", "प्रक्रिया"),
            SupPratyaya(declarationContext.markerSup!!.text, declarationContext.markerSup!!.text),
        )
        val copula = buildTinganta(requireNotNull(declarationContext.copula))
        val result = declarationContext.result?.let(::buildSubanta)
        val declaration = AkhyataVakya(declarationContext.text,
            declarationContext.qualifiers.map(::buildSubanta) + marker + copula, copula)
        val predicate = declaration.tinganta
        val validPredicate = if (result == null) {
            predicate.dhatu.mulaDhatu in setOf("असँ", "अस्") &&
                predicate.dhatu.sanadiPratyayas.isEmpty()
        } else {
            semanticRequire(result.sup.affixCandidates == listOf(SupAffix.AM),
                requireNotNull(declarationContext.result), PaniniSemanticDiagnosticCode.PRAKRIYA_RESULT_AGREEMENT,
                "A procedure's declared product requires the accusative singular.")
            semanticRequire(marker.sup.affixCandidates == listOf(SupAffix.SU), declarationContext,
                PaniniSemanticDiagnosticCode.PRAKRIYA_MISSING_MARKER,
                "A result-producing procedure must be singular.")
            // The parser retains morphology; the lexical dhātu class owns meaning.
            true
        }
        semanticRequire(validPredicate &&
            declaration.tinganta.upasargas.isEmpty() &&
            declaration.tinganta.lakara == Lakara.LAT && declaration.tinganta.ting.affix == TingAffix.TIP,
            declarationContext, PaniniSemanticDiagnosticCode.PRAKRIYA_DECLARATION_PREDICATE,
            "A prakriyā declaration requires अस्ति, or an accusative product with जनयति.")
        val qualifierContexts = declarationContext.qualifiers
        val qualifiers = declaration.padas.filterIsInstance<SubantaPada>().mapIndexed { index, pada ->
            val qualifierContext = qualifierContexts.getOrNull(index) ?: declarationContext
            semanticRequire(pada.sup.supports(Vibhakti.PRATHAMA, dev.panini.core.Vacana.EKAVACANA),
                qualifierContext, PaniniSemanticDiagnosticCode.PRAKRIYA_QUALIFIER_AGREEMENT,
                "Prakriyā subject and qualifiers require nominative singular agreement with the declaration predicate.")
            pada.prakriyaDefinitionQualifier() ?: semanticError(qualifierContext,
                PaniniSemanticDiagnosticCode.PRAKRIYA_UNSUPPORTED_QUALIFIER,
                "Unsupported prakriyā qualifier.")
        }
        semanticRequire(PrakriyaDefinitionQualifier.PRAKRIYA in qualifiers, declarationContext,
            PaniniSemanticDiagnosticCode.PRAKRIYA_MISSING_MARKER,
            "A reusable declaration requires प्रक्रिया.")
        return Prakriya(
            sourceText = sourceText,
            name = operation.sourceText,
            domain = domain?.pratipadika?.morphologicalKey(includeTaddhita = false),
            nameIdentity = operation.pratipadika.morphologicalKey(),
            domainIdentity = domain?.pratipadika?.morphologicalKey(includeTaddhita = false),
            body = body,
            declaredResult = result,
            resultPredicate = predicate.takeIf { result != null },
            modifiers = PrakriyaModifiers(
                visibility = if (PrakriyaDefinitionQualifier.ANTARANGA in qualifiers) PrakriyaVisibility.INTERNAL else PrakriyaVisibility.PUBLIC,
                precedence = when {
                    PrakriyaDefinitionQualifier.APAVADA in qualifiers -> PrakriyaPrecedence.APAVADA
                    PrakriyaDefinitionQualifier.NITYA in qualifiers -> PrakriyaPrecedence.NITYA
                    PrakriyaDefinitionQualifier.ANTARANGA in qualifiers -> PrakriyaPrecedence.ANTARANGA
                    else -> PrakriyaPrecedence.DEFAULT
                },
            ),
        )
    }

    fun build(
        context: PaniniyaVyakaranamParser.UktiContext,
    ): Ukti = build(context.utterance())

    fun build(
        context: PaniniyaVyakaranamParser.UtteranceContext,
    ): Ukti {
        val body = context.reductionClause()?.let(::buildReductionClause)
            ?: context.quantifiedCollectionClause()?.let(::buildQuantifiedCollectionClause)
            ?: context.naturalRelativeActionClause()?.let(::buildNaturalRelativeActionClause)
            ?: context.naturalIndexedDistributiveClause()?.let(::buildNaturalIndexedDistributiveClause)
            ?: context.distributiveClause()?.let(::buildDistributiveClause) ?: context.quotationClause()?.let { quotation ->
            Quotation(
                sourceText = quotation.text,
                quoted = Invocation(buildVakya(requireNotNull(quotation.quoted))),
                reporting = Invocation(buildAkhyataVakya(requireNotNull(quotation.reporting))),
            )
        } ?: context.whileClause()?.let { loop ->
            val limit = loop.limit?.let(::buildSankhyaAbhyasaPada)
            val boundary = loop.boundary?.let { boundaryContext ->
                val ordinal = buildSankhyaPuranaPada(boundaryContext.ordinal!!)
                val attempt = buildSubanta(boundaryContext.attempt!!)
                require(
                    (attempt.pratipadika as? MulaPratipadika)?.lexicalIdentity ==
                        MulaPratipadikaIdentity.PRAYATNA,
                ) {
                    "A bounded attempt loop requires प्रयत्नस्य as its boundary noun."
                }
                require(
                    MulaPratipadikaIdentity.fromText(
                        requireNotNull(requireNotNull(boundaryContext.limitBase).text),
                    ) ==
                        MulaPratipadikaIdentity.ANTA,
                ) {
                    "Only अन्त licenses the पर्यन्त boundary relation."
                }
                listOf<Pada>(
                    ordinal,
                    attempt,
                    AvyayaPada(boundaryContext.text, "पर्यन्तम्"),
                )
            }.orEmpty()
            WhileLoop(
                sourceText = loop.text,
                condition = Invocation(buildVakya(loop.condition!!)),
                body = Invocation(buildVakya(loop.body!!)),
                maximumIterationStems = limit?.stems?.dropLast(1)
                    ?: (boundary.firstOrNull() as? SankhyaPuranaPada)?.stems?.dropLast(1).orEmpty(),
                maximumBoundaryPadas = boundary,
                exhausted = loop.exhausted?.let { exhausted ->
                    exhausted.plain?.let { Invocation(buildVakya(it)) }
                        ?: Quotation(
                            sourceText = exhausted.text,
                            quoted = Invocation(buildVakya(requireNotNull(exhausted.quoted))),
                            reporting = Invocation(buildVakya(requireNotNull(exhausted.reporting))),
                        )
                },
                resultTarget = loop.target?.let { Invocation(buildVakya(it)) },
            )
        } ?: context.conditionalPipelineClause()?.let(::buildConditionalPipeline)
            ?: context.attributePipelineClause()?.let(::buildAttributePipeline)
            ?: context.pipelineClause()?.let(::buildPipeline)
            ?: context.conditionalClause()?.let { clause ->
                val conditional = clause.conditionalExpression()?.let(::buildConditional)
                    ?: buildTemporalConditional(requireNotNull(clause.temporalConditionalExpression()))
                clause.target?.let { target ->
                    pipeConditionalResult(conditional, target)
                } ?: conditional
            }
            ?: context.conditionalActionSequence()?.let(::buildActionSequence)
            ?: run {
            val statements = context.vakya().mapTo(mutableListOf<ProgramNode>()) { Invocation(buildVakya(it)) }
            val connectors = context.vakyaSambandha().mapTo(mutableListOf()) { it.text }
            while ("इति" in connectors) {
                val boundary = connectors.indexOf("इति")
                val quoted = statements[boundary] as? Invocation
                    ?: error("The command before इति must be one grammatical invocation.")
                val reporting = statements[boundary + 1]
                statements[boundary] = Quotation(
                    sourceText = "${quoted.sourceText} इति ${reporting.sourceText}",
                    quoted = quoted,
                    reporting = reporting,
                )
                statements.removeAt(boundary + 1)
                connectors.removeAt(boundary)
            }
            connectors.forEachIndexed { index, connector ->
                if (connector == SequenceConnector.SAMUCCAYA.surface) {
                    val preceding = (statements.getOrNull(index) as? Invocation)?.vakya as? AkhyataVakya
                    require(preceding == null || preceding.padas.lastOrNull() == preceding.tinganta) {
                        "Action-level च must immediately follow the preceding finite verb; " +
                            "place nominal च before that verb to coordinate operands."
                    }
                }
            }
            if (statements.size == 1) statements.single()
            else Sequence(context.text, statements, connectors)
        }

        return Ukti(
            sourceText = context.text,
            sambodhana = context.sambodhana()?.let(::buildSambodhana),
            body = body,
        )
    }

    private fun buildConditionalPipeline(
        context: PaniniyaVyakaranamParser.ConditionalPipelineClauseContext,
    ): Sequence {
        val stages = listOf(Invocation(buildAkhyataVakya(requireNotNull(context.source)))) +
            context.stages.map { Invocation(buildAkhyataVakya(it)) } +
            (context.conditional?.let(::buildConditional)
                ?: buildTemporalConditional(requireNotNull(context.temporal)))
        return Sequence(
            sourceText = context.text,
            statements = stages,
            connectors = List(stages.size - 1) { "ततः" },
        )
    }

    private fun buildAttributePipeline(
        context: PaniniyaVyakaranamParser.AttributePipelineClauseContext,
    ): Sequence {
        val sourcePadas = context.source.map(::buildSubanta)
        val source = Invocation(NamaVakya(
            sourceText = sourcePadas.joinToString("") { it.sourceText },
            padas = sourcePadas,
        ))
        val targets = context.targets.map { Invocation(buildAkhyataVakya(it)) }
        return Sequence(
            sourceText = context.text,
            statements = listOf(source) + targets,
            connectors = List(targets.size) { "ततः" },
        )
    }

    private fun buildConditional(
        context: PaniniyaVyakaranamParser.ConditionalExpressionContext,
    ): Conditional {
        val firstConsequent = buildConditionalArm(context.consequent!!)
        val consequent = if (context.continuations.isEmpty()) firstConsequent else Sequence(
            sourceText = context.text,
            statements = listOf(firstConsequent) + context.continuations.map { build(it).body },
            connectors = List(context.continuations.size) { SequenceConnector.TADANIM.surface },
            independentActions = true,
        )
        return Conditional(
            sourceText = context.text,
            condition = buildConditionClause(requireNotNull(context.condition)),
            consequent = consequent,
            alternate = context.nested?.let(::buildConditional)
                ?: context.alternate?.let(::buildConditionalArm),
        )
    }

    private fun buildTemporalConditional(
        context: PaniniyaVyakaranamParser.TemporalConditionalExpressionContext,
    ): Conditional {
        val firstConsequent = buildConditionalArm(requireNotNull(context.consequent))
        val consequent = if (context.continuations.isEmpty()) firstConsequent else Sequence(
            sourceText = context.text,
            statements = listOf(firstConsequent) + context.continuations.map { build(it).body },
            connectors = List(context.continuations.size) { SequenceConnector.TADANIM.surface },
            independentActions = true,
        )
        return Conditional(
            sourceText = context.text,
            condition = buildConditionClause(requireNotNull(context.condition)),
            consequent = consequent,
            relation = ConditionalRelation.TEMPORAL,
        )
    }

    private fun buildConditionClause(
        context: PaniniyaVyakaranamParser.ConditionClauseContext,
    ): ProgramNode = context.quantifiedCollectionClause()?.let(::buildQuantifiedCollectionClause)
        ?: Invocation(buildVakya(requireNotNull(context.vakya())))

    private fun buildQuantifiedCollectionClause(
        clause: PaniniyaVyakaranamParser.QuantifiedCollectionClauseContext,
    ): QuantifiedCollectionPredicate {
        val collection = buildSubanta(requireNotNull(clause.collection))
        val member = buildSubanta(requireNotNull(clause.member))
        require(collection.sup.affixCandidates == listOf(SupAffix.NGAS)) {
            "A quantified collection predicate requires a genitive singular collection."
        }
        require(member.sup.affixCandidates == listOf(SupAffix.SU)) {
            "A quantified collection predicate requires a nominative singular member."
        }
        require(member.pratipadika is MulaPratipadika && (member.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "The quantified member must introduce an underived nominal referent."
        }
        val predicate = buildAkhyataVakya(requireNotNull(clause.predicate))
        val predicateWithSubject = predicate.copy(
            sourceText = member.sourceText + predicate.sourceText,
            padas = listOf(member) + predicate.padas,
        )
        return QuantifiedCollectionPredicate(
            sourceText = clause.text,
            collection = collection,
            quantifier = if (clause.KASHCHIT() != null) CollectionQuantifier.ANY else CollectionQuantifier.ALL,
            member = member,
            predicate = Invocation(predicateWithSubject),
        )
    }

    private fun buildDistributiveClause(clause: PaniniyaVyakaranamParser.DistributiveClauseContext): ForEach {
        val collection = buildSubanta(requireNotNull(clause.collection))
        val member = buildSubanta(requireNotNull(clause.member))
        val index = clause.index?.let(::buildSubanta)
        require(collection.sup.affixCandidates == listOf(SupAffix.NGAS)) { "Distributive iteration requires a genitive singular collection." }
        require(member.sup.affixCandidates == listOf(SupAffix.AM)) { "Distributive प्रति requires a singular accusative member." }
        require(member.pratipadika is MulaPratipadika && (member.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "The iteration member must introduce an underived nominal referent."
        }
        require(index == null || index.sup.affixCandidates == listOf(SupAffix.AM)) {
            "An indexed distributive iteration requires a singular accusative index."
        }
        require(index == null || index.pratipadika is MulaPratipadika &&
            (index.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "The iteration index must introduce an underived nominal referent."
        }
        require(index == null || index.pratipadika.semanticKey() != member.pratipadika.semanticKey()) {
            "The iteration member and index require distinct referents."
        }
        return ForEach(clause.text, collection, member, index, build(requireNotNull(clause.body)).body)
    }

    private fun buildReductionClause(clause: PaniniyaVyakaranamParser.ReductionClauseContext): ForEach {
        val collection = buildSubanta(requireNotNull(clause.collection))
        val initial = buildSubanta(requireNotNull(clause.initial))
        val accumulator = buildSubanta(requireNotNull(clause.accumulator))
        val member = buildSubanta(requireNotNull(clause.member))
        require(requireNotNull(clause.connector).text == "आरभ्य") {
            "Reduction requires आरभ्य after प्रथम + अम्."
        }
        require(collection.sup.affixCandidates == listOf(SupAffix.NGAS)) {
            "Reduction requires a genitive singular collection."
        }
        require(initial.pratipadika.semanticKey() == "प्रथम" &&
            initial.sup.affixCandidates == listOf(SupAffix.AM)) {
            "Reduction begins with the collection's first member: use प्रथम + अम् before आरभ्य."
        }
        require(accumulator.sup.affixCandidates == listOf(SupAffix.AM) &&
            accumulator.pratipadika is MulaPratipadika &&
            (accumulator.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "Reduction requires an underived singular accusative accumulator."
        }
        require(member.sup.affixCandidates == listOf(SupAffix.AM) &&
            member.pratipadika is MulaPratipadika && (member.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "Reduction requires an underived singular accusative member."
        }
        require(accumulator.pratipadika.semanticKey() != member.pratipadika.semanticKey()) {
            "Reduction accumulator and member require distinct referents."
        }
        return ForEach(
            clause.text,
            collection,
            member,
            body = build(requireNotNull(clause.body)).body,
            reduction = CollectionReduction(initial, accumulator),
        )
    }

    private fun buildNaturalIndexedDistributiveClause(
        clause: PaniniyaVyakaranamParser.NaturalIndexedDistributiveClauseContext,
    ): ForEach {
        val collection = buildSubanta(requireNotNull(clause.collection))
        val quantifier = buildSubanta(requireNotNull(clause.quantifier))
        val member = buildSubanta(requireNotNull(clause.member))
        val relations = clause.relations.map(::buildSubanta)
        val selections = clause.selections.map(::buildSubanta)
        require(collection.sup.affixCandidates == listOf(SupAffix.NGAS)) {
            "Natural indexed iteration requires a genitive singular collection."
        }
        require(quantifier.pratipadika.semanticKey() == "प्रत्येक" &&
            quantifier.sup.affixCandidates == listOf(SupAffix.AM)) {
            "Natural indexed iteration requires प्रत्येक + अम्."
        }
        require(member.sup.affixCandidates == listOf(SupAffix.AM) &&
            member.pratipadika is MulaPratipadika && (member.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "Natural indexed iteration requires an underived singular accusative member."
        }
        require(relations.size == selections.size && relations.all {
            it.pratipadika.semanticKey() == "तद्" && it.sup.affixCandidates == listOf(SupAffix.NGAS)
        }) { "An iteration selection must be related to its member by तद् + ङस्." }
        val indices = selections.filter { it.pratipadika.semanticKey() == "क्रम" }
        require(indices.size <= 1) { "Natural iteration permits at most one क्रम binding." }
        val index = indices.singleOrNull()
        require(index == null || index.sup.affixCandidates == listOf(SupAffix.AM) &&
            index.pratipadika is MulaPratipadika && (index.pratipadika as MulaPratipadika).vikaras.isEmpty()) {
            "Natural indexed iteration requires an underived singular accusative index."
        }
        val neighbors = selections.filter { it !== index }
        require(neighbors.all {
            it.sup.affixCandidates.singleOrNull() in setOf(SupAffix.AM, SupAffix.SAS) &&
                it.pratipadika is MulaPratipadika &&
                (it.pratipadika as MulaPratipadika).vikaras.all { vikara -> vikara.isRecognizedStriAffix }
        }) { "A neighbouring member requires a recognised singular or plural accusative nominal." }
        val previous = neighbors.singleOrNull {
            it.pratipadika.semanticKey() == "पूर्व"
        }
        val next = neighbors.singleOrNull {
            it.pratipadika.semanticKey() == "उत्तर"
        }
        require(neighbors.size == listOfNotNull(previous, next).size) {
            "Natural indexed iteration supports only one पूर्व and one उत्तर neighbouring member."
        }
        require(listOfNotNull(member, index) + neighbors == (listOfNotNull(member, index) + neighbors)
            .distinctBy { it.pratipadika.semanticKey() }) {
            "Iteration member, index, and neighbour bindings require distinct referents."
        }
        val acquisition = requireNotNull(clause.acquisition)
        require(acquisition.dhatuPrakriti().text == "ग्रह्" && acquisition.avyayaKrtPratyaya().text == "क्त्वा") {
            "Natural indexed iteration requires ग्रह् + क्त्वा."
        }
        fun relationOf(selection: SubantaPada?): SubantaPada? = selection?.let { selected ->
            selections.indexOfFirst { it === selected }
                .takeIf { it >= 0 }
                ?.let(relations::get)
        }
        val relativeSelections = listOfNotNull(
            previous?.let { binding -> CollectionRelativeSelection(
                CollectionRelativeDirection.PREVIOUS,
                if (binding.sup.affixCandidates == listOf(SupAffix.SAS))
                    CollectionRelativeCardinality.ITEMS else CollectionRelativeCardinality.ITEM,
                binding,
                requireNotNull(relationOf(binding)),
            ) },
            next?.let { binding -> CollectionRelativeSelection(
                CollectionRelativeDirection.NEXT,
                if (binding.sup.affixCandidates == listOf(SupAffix.SAS))
                    CollectionRelativeCardinality.ITEMS else CollectionRelativeCardinality.ITEM,
                binding,
                requireNotNull(relationOf(binding)),
            ) },
        )
        return ForEach(clause.text, collection, member, index, build(requireNotNull(clause.body)).body,
            indexRelation = relationOf(index),
            quantifier = quantifier,
            relativeSelections = relativeSelections,
            sequential = clause.sequential != null,
        )
    }

    private fun buildNaturalRelativeActionClause(
        clause: PaniniyaVyakaranamParser.NaturalRelativeActionClauseContext,
    ): CollectionRelativeAction {
        val collection = buildSubanta(requireNotNull(clause.collection))
        val quantifier = buildSubanta(requireNotNull(clause.quantifier))
        val member = buildSubanta(requireNotNull(clause.member))
        val relative = buildCompoundSubanta(requireNotNull(clause.relative))
        val relativeAffix = relative.sup.affixCandidates.singleOrNull()
            ?: semanticError(requireNotNull(clause.relative),
                PaniniSemanticDiagnosticCode.RELATIVE_INSTRUMENTAL,
                "A direct relative action requires an unambiguous instrumental relative selector.")
        require(relativeAffix in setOf(SupAffix.TA, SupAffix.BHIS)) {
            "A direct relative action requires an instrumental singular or plural relative selector."
        }
        val relativeTypeWritten = clause.relativeType != null
        val relativeType = clause.relativeType?.let(::buildSubanta) ?: member.copy(
            sourceText = member.pratipadika.morphologicalKey() + " + " + relativeAffix.upadesha,
            sup = SupPratyaya(relativeAffix.upadesha, relativeAffix.upadesha),
        ).inheritLocation(member)
        val action = buildTinganta(requireNotNull(clause.action))
        semanticRequire(collection.sup.affixCandidates == listOf(SupAffix.NGAS), requireNotNull(clause.collection),
            PaniniSemanticDiagnosticCode.COLLECTION_SOURCE_CASE,
            "A direct relative action requires a genitive singular collection.")
        semanticRequire(quantifier.pratipadika.semanticKey() == "प्रत्येक" &&
            quantifier.sup.affixCandidates == listOf(SupAffix.AM), requireNotNull(clause.quantifier),
            PaniniSemanticDiagnosticCode.DISTRIBUTIVE_AGREEMENT,
            "A direct relative action requires an accusative प्रत्येक qualifier.")
        semanticRequire(member.sup.affixCandidates == listOf(SupAffix.AM), requireNotNull(clause.member),
            PaniniSemanticDiagnosticCode.CURRENT_MEMBER_CASE,
            "A direct relative action requires an accusative current member.")
        semanticRequire(relativeType.sup.affixCandidates == listOf(relativeAffix),
            clause.relativeType ?: requireNotNull(clause.relative),
            PaniniSemanticDiagnosticCode.RELATIVE_INSTRUMENTAL,
            "A direct relative action requires agreeing instrumental singular or plural relative items.")
        val compound = relative.pratipadika as? SamasaPratipadika
            ?: semanticError(requireNotNull(clause.relative), PaniniSemanticDiagnosticCode.RELATIVE_COMPOUND,
                "A relative collection operand must be a तद् compound.")
        semanticRequire(compound.angas.size == 2, requireNotNull(clause.relative),
            PaniniSemanticDiagnosticCode.RELATIVE_COMPOUND,
            "A relative collection operand requires exactly two compound members.")
        val anchor = compound.angas[0]
        val selector = compound.angas[1]
        semanticRequire(anchor.pratipadika.semanticKey() == "तद्" &&
            anchor.sup?.affixCandidates == listOf(SupAffix.NGAS) &&
            anchor.supLopa == dev.panini.core.SupLopa.LUK, requireNotNull(clause.relative),
            PaniniSemanticDiagnosticCode.RELATIVE_ANCHOR,
            "A relative collection operand requires तद् + ङस् + लुक् as its anchor.")
        val direction = when (selector.pratipadika.semanticKey()) {
            "पूर्व" -> CollectionRelativeDirection.PREVIOUS
            "उत्तर" -> CollectionRelativeDirection.NEXT
            else -> semanticError(requireNotNull(clause.relative),
                PaniniSemanticDiagnosticCode.RELATIVE_DIRECTION,
                "A relative collection operand requires पूर्व or उत्तर.")
        }
        semanticRequire(relativeType.pratipadika.semanticKey() == member.pratipadika.semanticKey(),
            clause.relativeType ?: requireNotNull(clause.relative),
            PaniniSemanticDiagnosticCode.MEMBER_TYPE_MISMATCH,
            "The current and relative collection members require the same written member type.")
        fun genders(pada: SubantaPada): Set<Linga> {
            val stem = pada.pratipadika
            val vikaras = when (stem) {
                is MulaPratipadika -> stem.vikaras
                is SankhyaPratipadika -> stem.vikaras
                is KridantaPratipadika -> stem.vikaras
                is UnadyantaPratipadika -> stem.vikaras
                is SamasaPratipadika -> stem.vikaras
            }
            if (vikaras.lastOrNull()?.isRecognizedStriAffix == true) return setOf(Linga.STRI)
            if (vikaras.isNotEmpty()) return emptySet()
            val lexical = when (stem) {
                is MulaPratipadika -> stem.text
                is SankhyaPratipadika -> stem.semanticValue.word
                else -> return emptySet()
            }
            return StandardPratipadikaLexicon.findPratipadika(lexical)?.linga.orEmpty()
        }
        fun requireGenderAgreement(left: SubantaPada, right: SubantaPada,
            context: ParserRuleContext, message: String) {
            val leftGenders = genders(left)
            val rightGenders = genders(right)
            semanticRequire(leftGenders.isEmpty() || rightGenders.isEmpty() ||
                leftGenders.any { it in rightGenders }, context,
                PaniniSemanticDiagnosticCode.GENDER_MISMATCH, message)
        }
        requireGenderAgreement(quantifier, member,
            requireNotNull(clause.quantifier),
            "The distributive qualifier and current member have incompatible gender evidence.")
        requireGenderAgreement(relative, relativeType,
            clause.relativeType ?: requireNotNull(clause.relative),
            "The relative selector and member type have incompatible gender evidence.")
        requireGenderAgreement(member, relativeType,
            clause.relativeType ?: requireNotNull(clause.relative),
            "The current and relative member types have incompatible gender evidence.")
        val cardinality = if (relativeAffix == SupAffix.BHIS)
            CollectionRelativeCardinality.ITEMS else CollectionRelativeCardinality.ITEM
        val continuation = clause.continuation?.let { continuation ->
            continuation.conditionalExpression()?.let(::buildConditional)
                ?: Invocation(buildVakya(requireNotNull(continuation.vakya())))
        }
        return CollectionRelativeAction(
            sourceText = clause.text,
            collection = collection,
            quantifier = quantifier,
            member = member,
            operand = CollectionRelativeOperand(
                relative = relative,
                memberType = relativeType,
                memberTypeWritten = relativeTypeWritten,
                direction = direction,
                cardinality = cardinality,
            ),
            action = action,
            continuation = continuation,
        )
    }

    private fun buildCompoundSubanta(
        context: PaniniyaVyakaranamParser.CompoundSubantaPadaContext,
    ): SubantaPada {
        val parsed = buildSamasa(requireNotNull(context.samasaPratipadika()))
        // Without grouping punctuation, ANTLR naturally attaches a suffix after
        // the final compound member to that member.  In this surface construction
        // it forms the compound as a whole (tad-uttara + ṭāp), so retain the
        // written morphology while lifting that suffix to the samāsa node.
        val finalMula = parsed.angas.last().pratipadika as? MulaPratipadika
        val compound = if (finalMula != null && finalMula.vikaras.isNotEmpty()) parsed.copy(
            angas = parsed.angas.dropLast(1) + parsed.angas.last().copy(
                pratipadika = finalMula.copy(vikaras = emptyList()),
            ),
            vikaras = parsed.vikaras + finalMula.vikaras,
        ) else parsed
        return SubantaPada(
            sourceText = context.text,
            pratipadika = attachVikaras(
                compound,
                compound.vikaras + context.pratipadikaVikara().map(::buildPratipadikaVikara),
            ),
            sup = SupPratyaya(
                sourceText = requireNotNull(context.supPratyaya()).text,
                text = requireNotNull(context.supPratyaya()).text,
            ),
        )
    }

    private fun buildConditionalArm(
        context: PaniniyaVyakaranamParser.ConditionalArmContext,
    ): ProgramNode = context.reductionClause()?.let(::buildReductionClause)
        ?: context.naturalIndexedDistributiveClause()?.let(::buildNaturalIndexedDistributiveClause)
        ?: context.distributiveClause()?.let(::buildDistributiveClause)
        ?: context.conditionalPipelineClause()?.let(::buildConditionalPipeline)
        ?: context.temporalConditionalExpression()?.let(::buildTemporalConditional)
        ?: context.conditionalActionSequence()?.let(::buildActionSequence)
        ?: context.vakya()?.let {
        val vakya = buildVakya(it)
        val nominal = (vakya as? NamaVakya)?.padas?.singleOrNull()
        if (nominal is SubantaPada || nominal is SankhyaBoundaryPada || nominal is KatapayadiPada ||
            nominal is AryabhatiyaPada || nominal is BhutasamkhyaPada) {
            implicitNominalReturn(requireNotNull(nominal))
        } else Invocation(vakya)
    }
        ?: implicitPratipadikaReturn(buildPratipadika(requireNotNull(context.value)))

    private fun buildActionSequence(
        sequence: PaniniyaVyakaranamParser.ConditionalActionSequenceContext,
    ): Sequence {
        val statements = sequence.statements.map { coordinated ->
            val padas = coordinated.purvaVakyaPada()
                .flatMap { buildVakyaPada(requireNotNull(it.vakyaPada())) }
            val tinganta = buildTinganta(requireNotNull(coordinated.tingantaPada()))
            Invocation(AkhyataVakya(coordinated.text, padas + tinganta, tinganta))
        } + Invocation(buildVakya(requireNotNull(sequence.finalStatement)))
        return Sequence(sequence.text, statements, sequence.connectors.map { it.text }, independentActions = true)
    }

    /** A one-word nominative branch is a Sanskrit zero-copula value clause. */
    private fun implicitNominalReturn(value: Pada): ProgramNode {
        val sup = when (value) {
            is SubantaPada -> value.sup
            is SankhyaBoundaryPada -> value.sup
            is KatapayadiPada -> value.sup
            is AryabhatiyaPada -> value.sup
            is BhutasamkhyaPada -> value.sup
            else -> error("A nominal result requires an inflected nominal.")
        }
        require(SupAffix.candidates(sup.text).any { it.vibhakti == Vibhakti.PRATHAMA }) {
            "A zero-copula nominal result requires nominative morphology."
        }
        val cardinal = when (value) {
            is SubantaPada -> primitiveCardinalValue(value.pratipadika)
            is SankhyaPada -> value.value ?: value.stems.singleOrNull()?.let {
                dev.panini.sankhya.PrimitiveSankhya.fromAnnotatedPratipadika(it)?.value
            }
            else -> null
        }
        cardinal?.let { number ->
            val ending = SupAffix.candidates(sup.text).first { it.vibhakti == Vibhakti.PRATHAMA }
            dev.panini.sankhya.SankhyaVacana.requireCompatible(number, ending.vacana)
        }
        return Invocation(
            NamaVakya(value.sourceText, listOf(value)),
            implicitValue = value.sourceText,
            implicitValuePada = value,
        )
    }

    private fun primitiveCardinalValue(value: Pratipadika): Long? = when (value) {
        is SankhyaPratipadika -> value.semanticValue.value
        is MulaPratipadika -> if (value.vikaras.all { it.isRecognizedStriAffix })
            dev.panini.sankhya.PrimitiveSankhya.fromAnnotatedPratipadika(value.text)?.value else null
        else -> null
    }

    /** A bare nominal branch has an understood nominative ending, not a fictitious action. */
    private fun implicitPratipadikaReturn(value: Pratipadika): ProgramNode {
        val cardinal = primitiveCardinalValue(value)
        val vacana = cardinal?.let(dev.panini.sankhya.SankhyaVacana::requiredFor) ?: dev.panini.core.Vacana.EKAVACANA
        val sup = SupAffix.entries.first { it.vibhakti == Vibhakti.PRATHAMA && it.vacana == vacana }.upadesha
        return implicitNominalReturn(
            SubantaPada(
                sourceText = "${value.sourceText} + $sup",
                pratipadika = value,
                sup = SupPratyaya(sourceText = sup, text = sup),
            ),
        )
    }

    /** Lowers one written pipeline target into each mutually exclusive branch. */
    private fun pipeConditionalResult(
        conditional: Conditional,
        target: PaniniyaVyakaranamParser.VakyaContext,
        exposeSurfaceTarget: Boolean = true,
    ): Conditional = conditional.copy(
        consequent = pipeBranch(conditional.consequent, target),
        alternate = conditional.alternate?.let { alternate ->
            if (alternate is Conditional) {
                pipeConditionalResult(alternate, target, exposeSurfaceTarget = false)
            } else {
                pipeBranch(alternate, target)
            }
        },
        surfacePipelineTarget = if (exposeSurfaceTarget) Invocation(buildVakya(target)) else null,
    )

    private fun pipeBranch(
        branch: ProgramNode,
        target: PaniniyaVyakaranamParser.VakyaContext,
    ): Sequence = Sequence(
        sourceText = branch.sourceText + "ततः" + target.text,
        statements = listOf(branch, Invocation(buildVakya(target))),
        connectors = listOf("ततः"),
    )

    private fun buildPipeline(
        context: PaniniyaVyakaranamParser.PipelineClauseContext,
    ): Pipeline {
        val arguments = context.arguments.map(::buildSubanta)
        val isNaturalSequence = context.purvaparaDirective() == null
        val stagePadas = context.stages.map { stage ->
            val domain = buildSubanta(stage.domain!!)
            val operation = buildSubanta(stage.operation!!)
            if (isNaturalSequence) {
                require(domain.sup.supports(Vibhakti.SASTHI)) {
                    "A sequential procedure stage requires its domain in ṣaṣṭhī."
                }
                require(operation.sup.supports(Vibhakti.TRTIYA)) {
                    "A sequential procedure stage requires its operation in tṛtīyā."
                }
            }
            Triple(
                PipelineStage(
                    sourceText = "${canonicalSegmented(domain.sourceText)} ${canonicalSegmented(operation.pratipadika.sourceText)}",
                    domainStem = canonicalSegmented(domain.pratipadika.sourceText),
                    operationStem = canonicalSegmented(operation.pratipadika.sourceText),
                ),
                domain,
                operation,
            )
        }
        val legacyDirective = context.purvaparaDirective()
        val renderedStages = if (isNaturalSequence) {
            stagePadas.flatMapIndexed { index, (_, domain, operation) ->
                buildList<Pada> {
                    if (index > 0) add(AvyayaPada(sourceText = "ततः", form = "ततः"))
                    add(domain)
                    add(operation)
                }
            }
        } else {
            val purvaPada = buildSubanta(requireNotNull(legacyDirective).purva!!)
            val paraPada = buildSubanta(legacyDirective.para!!)
            stagePadas.flatMap { listOf(it.second, it.third) } +
                listOf(purvaPada, paraPada) +
                buildSubanta(context.pipelineResult()!!.subantaPada()!!)
        }
        return Pipeline(
            sourceText = context.text,
            arguments = arguments.map { it.pratipadika.sourceText },
            stages = stagePadas.map { it.first },
            argumentPadas = arguments,
            renderPadas = arguments +
                AvyayaPada(sourceText = "च", form = "च") +
                renderedStages +
                buildTinganta(context.tingantaPada()!!),
        )
    }

    private fun canonicalSegmented(source: String): String =
        source.replace("+", " + ").replace(Regex("\\s+"), " ").trim()

    private fun buildVakya(
        context: PaniniyaVyakaranamParser.VakyaContext,
    ): Vakya =
        when {
            context.akhyataVakya() != null ->
                buildAkhyataVakya(context.akhyataVakya()!!)

            context.namaVakya() != null ->
                buildNamaVakya(context.namaVakya()!!)

            else -> error("अज्ञातः वाक्यप्रकारः: ${context.text}")
        }

    private fun buildAkhyataVakya(
        context: PaniniyaVyakaranamParser.AkhyataVakyaContext,
    ): AkhyataVakya {
        val purvaPadas = context.purvaVakyaPada()
            .flatMap { buildVakyaPada(it.vakyaPada()!!) }

        val tinganta = buildTinganta(context.tingantaPada()!!)

        val uttaraPadas = context.uttaraVakyaPada()
            .flatMap { buildVakyaPada(it.vakyaPada()!!) }

        return AkhyataVakya(
            sourceText = context.text,
            padas = purvaPadas + tinganta + uttaraPadas,
            tinganta = tinganta,
        )
    }

    private fun buildNamaVakya(
        context: PaniniyaVyakaranamParser.NamaVakyaContext,
    ): NamaVakya {
        val padas = context.vakyaPada()
            .flatMap(::buildVakyaPada)

        return NamaVakya(
            sourceText = context.text,
            padas = padas,
        )
    }

    private fun buildVakyaPada(
        context: PaniniyaVyakaranamParser.VakyaPadaContext,
    ): List<Pada> =
        when {
            context.paryantaRange() != null ->
                listOf(buildParyantaRange(context.paryantaRange()!!))

            context.subantaVakyaPada() != null ->
                buildSubantaVakyaPada(context.subantaVakyaPada()!!)

            context.avyayaPada() != null ->
                listOf(buildAvyaya(context.avyayaPada()!!))

            else -> error("अज्ञातं वाक्यपदम्: ${context.text}")
        }

    private fun buildParyantaRange(
        context: PaniniyaVyakaranamParser.ParyantaRangeContext,
    ): ParyantaRangePada {
        require(
            MulaPratipadikaIdentity.fromText(requireNotNull(context.limitBase!!.text)) ==
                MulaPratipadikaIdentity.ANTA,
        ) {
            "पर्यन्त-range marker requires परि + अन्त + अम्."
        }
        val lower = context.lower!!
        val upper = context.upper!!
        fun lowerBoundary(): SankhyaBoundaryPada {
            lower.ablativeNumeral()?.let { numeral ->
                return SankhyaPada(
                    sourceText = numeral.text,
                    stems = numeral.sankhyaStem().map { it.text },
                    sup = SupPratyaya(numeral.ablativeSup().text, numeral.ablativeSup().text),
                )
            }
            val ordinal = requireNotNull(lower.ablativeOrdinal())
            return SankhyaPuranaPada(
                sourceText = ordinal.text,
                stems = ordinal.sankhyaStem().map { it.text } + ordinal.puranaPratyaya().text,
                sup = SupPratyaya(ordinal.ablativeSup().text, ordinal.ablativeSup().text),
            )
        }
        fun upperBoundary(): SankhyaBoundaryPada {
            upper.accusativeNumeral()?.let { numeral ->
                return SankhyaPada(
                    sourceText = numeral.text,
                    stems = numeral.sankhyaStem().map { it.text },
                    sup = SupPratyaya(numeral.accusativeSup().text, numeral.accusativeSup().text),
                )
            }
            val ordinal = requireNotNull(upper.accusativeOrdinal())
            return SankhyaPuranaPada(
                sourceText = ordinal.text,
                stems = ordinal.sankhyaStem().map { it.text } + ordinal.puranaPratyaya().text,
                sup = SupPratyaya(ordinal.accusativeSup().text, ordinal.accusativeSup().text),
            )
        }
        return ParyantaRangePada(
            sourceText = context.text,
            lowerLimit = lowerBoundary(),
            upperLimit = upperBoundary(),
            marker = SubantaPada(
                sourceText = "परि+अन्त+अम्",
                pratipadika = MulaPratipadika(sourceText = "परि+अन्त", text = "पर्यन्त"),
                sup = SupPratyaya(sourceText = "अम्", text = "अम्"),
            ),
        )
    }

    private fun buildSankhyaPada(
        context: PaniniyaVyakaranamParser.SankhyaPadaContext,
    ): SankhyaPada {
        val stems = context.sankhyaStem().map { it.text }
        return SankhyaPada(
            sourceText = context.text,
            stems = stems,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildSankhyaPuranaPada(
        context: PaniniyaVyakaranamParser.SankhyaPuranaPadaContext,
    ): SankhyaPuranaPada {
        val stems = context.sankhyaStem().map { it.text } + context.puranaPratyaya()!!.text
        return SankhyaPuranaPada(
            sourceText = context.text,
            stems = stems,
            striAffix = context.striPratyaya()?.let {
                requireNotNull(dev.panini.core.StriAffix.fromUpadesha(it.text)) { "Unknown feminine affix: ${it.text}" }
            },
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildSankhyaAbhyasaPada(
        context: PaniniyaVyakaranamParser.SankhyaAbhyasaPadaContext,
    ): SankhyaAbhyasaPada {
        val stems = context.sankhyaStem().map { it.text } + (context.KRITVAS()?.text ?: context.SUC()?.text ?: context.DHAA()!!.text)
        return SankhyaAbhyasaPada(
            sourceText = context.text,
            stems = stems,
        )
    }

    private fun buildKatapayadiPada(
        context: PaniniyaVyakaranamParser.KatapayadiPadaContext,
    ): KatapayadiPada {
        val word = context.IDENTIFIER().text
        return KatapayadiPada(
            sourceText = context.text,
            word = word,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildAryabhatiyaPada(
        context: PaniniyaVyakaranamParser.AryabhatiyaPadaContext,
    ): AryabhatiyaPada {
        val word = context.IDENTIFIER().text
        return AryabhatiyaPada(
            sourceText = context.text,
            word = word,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildBhutasamkhyaPada(
        context: PaniniyaVyakaranamParser.BhutasamkhyaPadaContext,
    ): BhutasamkhyaPada {
        val terms = context.IDENTIFIER().map { it.text }
        return BhutasamkhyaPada(
            sourceText = context.text,
            terms = terms,
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        )
    }

    private fun buildSubantaVakyaPada(
        context: PaniniyaVyakaranamParser.SubantaVakyaPadaContext,
    ): List<Pada> =
        when {
            context.explicitSamuccitaSubanta() != null -> {
                buildExplicitCoordination(context.explicitSamuccitaSubanta()!!)
            }
            context.subantaPada() != null ->
                listOf(buildSubanta(context.subantaPada()!!))

            context.samuccitaSubanta() != null ->
                listOf(buildSamuccitaSubanta(context.samuccitaSubanta()!!))

            context.sankhyaPada() != null ->
                listOf(buildSankhyaPada(context.sankhyaPada()!!))

            context.sankhyaPuranaPada() != null ->
                listOf(buildSankhyaPuranaPada(context.sankhyaPuranaPada()!!))

            context.sankhyaAbhyasaPada() != null ->
                listOf(buildSankhyaAbhyasaPada(context.sankhyaAbhyasaPada()!!))

            context.katapayadiPada() != null ->
                listOf(buildKatapayadiPada(context.katapayadiPada()!!))

            context.aryabhatiyaPada() != null ->
                listOf(buildAryabhatiyaPada(context.aryabhatiyaPada()!!))

            context.bhutasamkhyaPada() != null ->
                listOf(buildBhutasamkhyaPada(context.bhutasamkhyaPada()!!))

            else -> error("अज्ञातं सुबन्तवाक्यपदम्: ${context.text}")
        }

    /** Case changes delimit distinct grammatical roles, not multiword conjuncts. */
    private fun buildExplicitCoordination(
        context: PaniniyaVyakaranamParser.ExplicitSamuccitaSubantaContext,
    ): List<Pada> {
        val result = mutableListOf<Pada>()
        val pending = mutableListOf<List<SupBearingPada>>()
        var sharedCases = emptySet<Vibhakti>()
        fun cases(pada: SupBearingPada) = pada.sup.affixCandidates.mapTo(mutableSetOf()) { it.vibhakti }
        fun flush() {
            if (pending.isEmpty()) return
            val members = pending.flatten()
            val source = pending.joinToString(" ") { words -> words.joinToString(" ") { it.sourceText } + " च" }
            result += SamuccitaSubanta(source, members,
                conjunctSizes = if (pending.size > 1) pending.map { it.size } else emptyList())
            pending.clear()
            sharedCases = emptySet()
        }
        for (group in context.subantaSamudaya()) {
            val words = group.supantaPada().map(::buildSupanta)
            var start = words.lastIndex
            var tailCases: Set<Vibhakti> = cases(words.last())
            while (start > 0) {
                val common = tailCases.intersect(cases(words[start - 1]))
                if (common.isEmpty()) break
                tailCases = common
                start--
            }
            if (start > 0) {
                flush()
                // Preserve differently cased dependents (for example a genitive
                // action nominal) outside the following accusative coordination.
                result += words.take(start)
            }
            if (pending.isNotEmpty() && sharedCases.intersect(tailCases).isEmpty()) flush()
            sharedCases = if (pending.isEmpty()) tailCases else sharedCases.intersect(tailCases)
            pending += words.drop(start)
        }
        flush()
        return result
    }

    private fun buildSamuccitaSubanta(
        context: PaniniyaVyakaranamParser.SamuccitaSubantaContext,
    ): SamuccitaSubanta =
        SamuccitaSubanta(
            sourceText = context.text,
            members = context.supantaPada().map(::buildSupanta),
        )

    private fun buildSupanta(context: PaniniyaVyakaranamParser.SupantaPadaContext): SupBearingPada = when {
        context.subantaPada() != null -> buildSubanta(context.subantaPada()!!)
        context.sankhyaPuranaPada() != null -> buildSankhyaPuranaPada(context.sankhyaPuranaPada()!!)
        context.sankhyaPada() != null -> buildSankhyaPada(context.sankhyaPada()!!)
        context.katapayadiPada() != null -> buildKatapayadiPada(context.katapayadiPada()!!)
        context.aryabhatiyaPada() != null -> buildAryabhatiyaPada(context.aryabhatiyaPada()!!)
        else -> buildBhutasamkhyaPada(context.bhutasamkhyaPada()!!)
    }

    private fun buildSambodhana(
        context: PaniniyaVyakaranamParser.SambodhanaContext,
    ): Sambodhana =
        Sambodhana(
            sourceText = context.text,
            suchaka = context.sambodhanaSuchaka()?.text,
            subanta = buildSubanta(context.subantaPada()!!),
        )

    private fun buildSubanta(
        context: PaniniyaVyakaranamParser.SubantaPadaContext,
    ): SubantaPada =
        SubantaPada(
            sourceText = context.text,
            pratipadika = buildPratipadika(context.pratipadika()!!),
            sup = SupPratyaya(
                sourceText = context.supPratyaya()!!.text,
                text = context.supPratyaya()!!.text,
            ),
        ).locatedAt(DocumentSourceSpan(
            requireNotNull(context.start).startIndex,
            requireNotNull(context.stop).stopIndex + 1,
        ))

    private fun buildPratipadika(
        context: PaniniyaVyakaranamParser.PratipadikaContext,
    ): Pratipadika {
        val mula = buildPratipadikaMula(context.pratipadikaMula()!!)
        val vikaras = context.pratipadikaVikara().map(::buildPratipadikaVikara)

        return attachVikaras(mula, vikaras)
    }

    private fun buildPratipadikaMula(
        context: PaniniyaVyakaranamParser.PratipadikaMulaContext,
    ): Pratipadika =
        when {
            context.mulaPratipadika() != null ->
                MulaPratipadika(
                    sourceText = context.text,
                    text = context.mulaPratipadika()!!.text,
                )

            context.samjnaQualifierPratipadika() != null ->
                MulaPratipadika(
                    sourceText = context.text,
                    text = context.samjnaQualifierPratipadika()!!.text,
                )

            context.kridantaPratipadika() != null ->
                buildKridanta(context.kridantaPratipadika()!!)

            context.unadyantaPratipadika() != null ->
                buildUnadyanta(context.unadyantaPratipadika()!!)

            context.samasaPratipadika() != null ->
                buildSamasa(context.samasaPratipadika()!!)

            context.pratipadika() != null ->
                buildPratipadika(context.pratipadika()!!)

            else -> error("अज्ञातं प्रातिपदिकमूलम्: ${context.text}")
        }

    private fun buildKridanta(
        context: PaniniyaVyakaranamParser.KridantaPratipadikaContext,
    ): KridantaPratipadika =
        KridantaPratipadika(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()!!),
            krtPratyaya = context.krtPratyaya()!!.text,
        )

    private fun buildUnadyanta(
        context: PaniniyaVyakaranamParser.UnadyantaPratipadikaContext,
    ): UnadyantaPratipadika =
        UnadyantaPratipadika(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()!!),
            unadiPratyaya = context.unadiPratyaya()!!.IDENTIFIER()!!.text,
        )

    private fun buildSamasa(
        context: PaniniyaVyakaranamParser.SamasaPratipadikaContext,
    ): SamasaPratipadika =
        SamasaPratipadika(
            sourceText = context.text,
            angas = context.samasaAnga().map(::buildSamasaAnga),
        )

    private fun buildSamasaAnga(
        context: PaniniyaVyakaranamParser.SamasaAngaContext,
    ): SamasaAnga {
        val supAvastha = context.samasaSupAvastha()

        return SamasaAnga(
            sourceText = context.text,
            pratipadika = buildAsamasikaPratipadika(
                context.asamasikaPratipadika()!!,
            ),
            sup = supAvastha?.supPratyaya()?.let {
                SupPratyaya(
                    sourceText = it.text,
                    text = it.text,
                )
            },
            supLopa = supAvastha?.supAvastha()?.let {
                SupLopa.fromUpadesha(it.text)
            },
        )
    }

    private fun buildAsamasikaPratipadika(
        context: PaniniyaVyakaranamParser.AsamasikaPratipadikaContext,
    ): Pratipadika {
        val mulaContext = context.asamasikaPratipadikaMula()!!

        val mula = when {
            mulaContext.mulaPratipadika() != null ->
                MulaPratipadika(
                    sourceText = mulaContext.text,
                    text = mulaContext.mulaPratipadika()!!.text,
                )

            mulaContext.samjnaQualifierPratipadika() != null ->
                MulaPratipadika(
                    sourceText = mulaContext.text,
                    text = mulaContext.samjnaQualifierPratipadika()!!.text,
                )

            mulaContext.kridantaPratipadika() != null ->
                buildKridanta(mulaContext.kridantaPratipadika()!!)

            mulaContext.unadyantaPratipadika() != null ->
                buildUnadyanta(mulaContext.unadyantaPratipadika()!!)

            mulaContext.samasaPratipadika() != null ->
                buildSamasa(mulaContext.samasaPratipadika()!!)

            else -> error("अज्ञातम् असमासिकप्रातिपदिकम्: ${context.text}")
        }

        return attachVikaras(
            mula,
            context.pratipadikaVikara().map(::buildPratipadikaVikara),
        )
    }

    private fun buildPratipadikaVikara(
        context: PaniniyaVyakaranamParser.PratipadikaVikaraContext,
    ): PratipadikaVikara =
        when {
            context.taddhitaPratyaya() != null ->
                TaddhitaVikara(
                    sourceText = context.text,
                    pratyaya = context.taddhitaPratyaya()!!.text,
                )

            context.striPratyaya() != null ->
                StriVikara(
                    sourceText = context.text,
                    pratyaya = context.striPratyaya()!!.text,
                )

            else -> error("अज्ञातः प्रातिपदिकविकारः: ${context.text}")
        }

    private fun buildTinganta(
        context: PaniniyaVyakaranamParser.TingantaPadaContext,
    ): TingantaPada =
        TingantaPada(
            sourceText = context.text,
            upasargas = buildUpasargas(context.upasargaKrama()),
            dhatu = buildDhatu(context.dhatuPrakriti()!!),
            lakara = Lakara.fromUpadesha(context.lakara()!!.text),
            ting = TingPratyaya(
                sourceText = context.tingPratyaya()!!.text,
                text = context.tingPratyaya()!!.text,
            ),
            vikarana = context.vikarana()?.text?.let(dev.panini.vyakaranam.ast.Vikarana::fromUpadesha),
        ).locatedAt(DocumentSourceSpan(
            requireNotNull(context.start).startIndex,
            requireNotNull(context.stop).stopIndex + 1,
        ))

    private fun buildDhatu(
        context: PaniniyaVyakaranamParser.DhatuPrakritiContext,
    ): DhatuPrakriti =
        DhatuPrakriti(
            sourceText = context.text,
            mulaDhatu = context.dhatuMula()!!.text,
            sanadiPratyayas = context.sanadiPratyaya().map { it.text },
        )

    private fun buildAvyaya(
        context: PaniniyaVyakaranamParser.AvyayaPadaContext,
    ): AvyayaPada =
        when {
            context.mulaAvyaya() != null ->
                AvyayaPada(
                    sourceText = context.text,
                    form = context.mulaAvyaya()!!.text,
                )

            context.avyayaKridanta() != null -> {
                val kridanta = context.avyayaKridanta()!!

                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = AvyayaKridantaDerivation(
                        upasargas = buildUpasargas(kridanta.upasargaKrama()),
                        dhatu = buildDhatu(kridanta.dhatuPrakriti()!!),
                        pratyaya = kridanta.avyayaKrtPratyaya()!!.text,
                    ),
                )
            }

            context.avyayaTaddhitanta() != null -> {
                val taddhitanta = context.avyayaTaddhitanta()!!

                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = AvyayaTaddhitaDerivation(
                        pratipadika = taddhitanta.mulaPratipadika()!!.text,
                        pratyaya = taddhitanta.avyayaTaddhitaPratyaya()!!.text,
                    ),
                )
            }

            context.avyayibhavaPada() != null ->
                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = AvyayibhavaDerivation(
                        samasa = buildSamasa(
                            context
                                .avyayibhavaPada()!!
                                .samasaPratipadika()!!,
                        ),
                    ),
                )

            context.sankhyaAvyaya() != null -> {
                val saCtx = context.sankhyaAvyaya()!!
                val kind = when {
                    saCtx.ADHIKA() != null -> "ADHIKA"
                    saCtx.UNA() != null -> "UNA"
                    saCtx.SAKRIT() != null -> "SAKRIT"
                    saCtx.DVIH() != null -> "DVIH"
                    saCtx.TRIH() != null -> "TRIH"
                    saCtx.CHATUH() != null -> "CHATUH"
                    saCtx.KRITVAS() != null -> "KRITVAS"
                    saCtx.DHAA() != null -> "DHA"
                    saCtx.SHAH() != null -> "SHAS"
                    else -> "UNKNOWN"
                }
                val stemList = saCtx.IDENTIFIER()?.let { listOf(it.text) } ?: emptyList()
                AvyayaPada(
                    sourceText = context.text,
                    form = context.text,
                    derivation = SankhyaAvyayaDerivation(kind = kind, stems = stemList),
                )
            }

            else -> error("अज्ञातम् अव्ययपदम्: ${context.text}")
        }

    private fun buildUpasargas(
        context: PaniniyaVyakaranamParser.UpasargaKramaContext?,
    ): List<String> =
        context?.upasarga()?.map { it.text }.orEmpty()

    private fun attachVikaras(
        pratipadika: Pratipadika,
        vikaras: List<PratipadikaVikara>,
    ): Pratipadika =
        when (pratipadika) {
            is MulaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is SankhyaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is KridantaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is UnadyantaPratipadika -> pratipadika.copy(vikaras = vikaras)
            is SamasaPratipadika -> pratipadika.copy(vikaras = vikaras)
        }

    private fun startTokenIndex(pada: Pada): Int = 0
}

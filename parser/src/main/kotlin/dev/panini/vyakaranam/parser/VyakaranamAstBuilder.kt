package dev.panini.vyakaranam.parser

import dev.panini.core.Lakara
import dev.panini.core.SupAffix
import dev.panini.core.Vibhakti
import dev.panini.core.SupLopa
import dev.panini.vyakaranam.ast.*
import dev.panini.parser.VyakaranamParser as PaniniyaVyakaranamParser

class VyakaranamAstBuilder {

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
                SupAffix.fromUpadesha(marker.sup.text)?.vibhakti == Vibhakti.PRATHAMA) {
                "A सीमा declaration requires ablative and पर्यन्त bounds and nominative सीमा."
            }
            return RangeDeclaration(ukti.sourceText, padas.first() as ParyantaRangePada, marker)
        }
        val isScope = identity == MulaPratipadikaIdentity.ADHIKARA ||
            (marker.pratipadika as? KridantaPratipadika)?.lexicalIdentity == KridantaLexicalIdentity.ADHIKARA
        if (isScope) {
            val domain = padas.first() as? SubantaPada
            require(padas.size == 3 && domain != null &&
                SupAffix.fromUpadesha(domain.sup.text)?.vibhakti == Vibhakti.PRATHAMA &&
                SupAffix.fromUpadesha(marker.sup.text)?.vibhakti == Vibhakti.PRATHAMA) {
                "An अधिकार declaration requires a nominative domain and marker."
            }
            return Scope(ukti.sourceText, domain.sourceText, domain.pratipadika.morphologicalKey())
        }
        return ukti
    }

    fun buildScopeDeclaration(context: PaniniyaVyakaranamParser.ScopeDeclarationContext): Scope {
        val domain = buildSubanta(requireNotNull(context.domain))
        val marker = buildSubanta(requireNotNull(context.marker))
        require(SupAffix.fromUpadesha(domain.sup.text)?.vibhakti == Vibhakti.PRATHAMA &&
            SupAffix.fromUpadesha(marker.sup.text)?.vibhakti == Vibhakti.PRATHAMA) {
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
            SupAffix.fromUpadesha(marker.sup.text)?.vibhakti == Vibhakti.PRATHAMA) {
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
        require(names.size in 1..2) { "A prakriyā declaration names one operation and optionally one domain." }
        val operation = names.last()
        require(SupAffix.fromUpadesha(operation.sup.text)?.vibhakti == Vibhakti.PRATHAMA) {
            "A prakriyā name requires the nominative case."
        }
        val domain = names.dropLast(1).singleOrNull()
        require(domain == null || SupAffix.fromUpadesha(domain.sup.text)?.vibhakti == Vibhakti.SASTHI) {
            "A prakriyā domain requires the genitive case."
        }
        val declarationContext = requireNotNull(context.declaration)
        val marker = SubantaPada(
            "प्रक्रिया+${declarationContext.markerSup!!.text}",
            MulaPratipadika("प्रक्रिया", "प्रक्रिया"),
            SupPratyaya(declarationContext.markerSup!!.text, declarationContext.markerSup!!.text),
        )
        val copula = buildTinganta(requireNotNull(declarationContext.copula))
        val declaration = AkhyataVakya(declarationContext.text,
            declarationContext.qualifiers.map(::buildSubanta) + marker + copula, copula)
        require(declaration.tinganta.dhatu.mulaDhatu in setOf("असँ", "अस्") &&
            declaration.tinganta.dhatu.sanadiPratyayas.isEmpty() &&
            declaration.tinganta.upasargas.isEmpty() &&
            declaration.tinganta.lakara == Lakara.LAT && declaration.tinganta.ting.text == "तिप्") {
            "A prakriyā declaration requires अस्ति."
        }
        val qualifiers = declaration.padas.filterIsInstance<SubantaPada>().map { pada ->
            require(SupAffix.fromUpadesha(pada.sup.text)?.vibhakti == Vibhakti.PRATHAMA) {
                "Prakriyā qualifiers require the nominative case."
            }
            when (val stem = pada.pratipadika) {
                is MulaPratipadika -> stem.lexicalIdentity
                is KridantaPratipadika -> MulaPratipadikaIdentity.APAVADA.takeIf {
                    stem.lexicalIdentity == KridantaLexicalIdentity.APAVADA
                }
                else -> null
            }
        }
        require(MulaPratipadikaIdentity.PRAKRIYA in qualifiers) { "A reusable declaration requires प्रक्रिया." }
        require(qualifiers.all { it in setOf(MulaPratipadikaIdentity.PRAKRIYA,
            MulaPratipadikaIdentity.ANTARANGA, MulaPratipadikaIdentity.NITYA, MulaPratipadikaIdentity.APAVADA) }) {
            "Unsupported prakriyā qualifier."
        }
        return Prakriya(
            sourceText = sourceText,
            name = operation.sourceText,
            domain = domain?.pratipadika?.morphologicalKey(includeTaddhita = false),
            nameIdentity = operation.pratipadika.morphologicalKey(),
            domainIdentity = domain?.pratipadika?.morphologicalKey(includeTaddhita = false),
            body = body,
            modifiers = PrakriyaModifiers(
                visibility = if (MulaPratipadikaIdentity.ANTARANGA in qualifiers) PrakriyaVisibility.INTERNAL else PrakriyaVisibility.PUBLIC,
                precedence = when {
                    MulaPratipadikaIdentity.APAVADA in qualifiers -> PrakriyaPrecedence.APAVADA
                    MulaPratipadikaIdentity.NITYA in qualifiers -> PrakriyaPrecedence.NITYA
                    MulaPratipadikaIdentity.ANTARANGA in qualifiers -> PrakriyaPrecedence.ANTARANGA
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
        val body = context.quotationClause()?.let { quotation ->
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
                val conditional = buildConditional(clause.conditionalExpression())
                clause.target?.let { target ->
                    pipeConditionalResult(conditional, target)
                } ?: conditional
            }
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
            buildConditional(requireNotNull(context.conditional))
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
    ): Conditional = Conditional(
        sourceText = context.text,
        condition = Invocation(buildVakya(context.condition!!)),
        consequent = buildConditionalArm(context.consequent!!),
        alternate = context.nested?.let(::buildConditional)
            ?: context.alternate?.let(::buildConditionalArm),
    )

    private fun buildConditionalArm(
        context: PaniniyaVyakaranamParser.ConditionalArmContext,
    ): ProgramNode = context.vakya()?.let {
        val vakya = buildVakya(it)
        val nominal = (vakya as? NamaVakya)?.padas?.singleOrNull() as? SubantaPada
        if (nominal != null) implicitSubantaReturn(nominal) else Invocation(vakya)
    }
        ?: implicitPratipadikaReturn(buildPratipadika(requireNotNull(context.value)))

    /** A one-word nominative branch is a Sanskrit zero-copula value clause. */
    private fun implicitSubantaReturn(value: SubantaPada): ProgramNode =
        (PaniniParser().parse("${value.pratipadika.sourceText} + अम् दा + लोट् + सिप् ।").body as Invocation)
            .copy(implicitValue = value.sourceText, implicitValuePada = value)

    /** A bare nominal branch has an understood nominative ending and return verb. */
    private fun implicitPratipadikaReturn(value: Pratipadika): ProgramNode =
        implicitSubantaReturn(
            SubantaPada(
                sourceText = "${value.sourceText} + सुँ",
                pratipadika = value,
                sup = SupPratyaya(sourceText = "सुँ", text = "सुँ"),
            ),
        )

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
                require(SupAffix.fromUpadesha(domain.sup.text)?.vibhakti == Vibhakti.SASTHI) {
                    "A sequential procedure stage requires its domain in ṣaṣṭhī."
                }
                require(SupAffix.fromUpadesha(operation.sup.text)?.vibhakti == Vibhakti.TRTIYA) {
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
            .map { buildVakyaPada(it.vakyaPada()!!) }

        val tinganta = buildTinganta(context.tingantaPada()!!)

        val uttaraPadas = context.uttaraVakyaPada()
            .map { buildVakyaPada(it.vakyaPada()!!) }

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
            .map(::buildVakyaPada)

        return NamaVakya(
            sourceText = context.text,
            padas = padas,
        )
    }

    private fun buildVakyaPada(
        context: PaniniyaVyakaranamParser.VakyaPadaContext,
    ): Pada =
        when {
            context.paryantaRange() != null ->
                buildParyantaRange(context.paryantaRange()!!)

            context.subantaVakyaPada() != null ->
                buildSubantaVakyaPada(context.subantaVakyaPada()!!).single()

            context.avyayaPada() != null ->
                buildAvyaya(context.avyayaPada()!!)

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

    private fun buildSamuccitaSubanta(
        context: PaniniyaVyakaranamParser.SamuccitaSubantaContext,
    ): SamuccitaSubanta =
        SamuccitaSubanta(
            sourceText = context.text,
            members = context.subantaPada().map(::buildSubanta),
        )

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
        )

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
        )

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

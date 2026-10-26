package dev.panini.compiler
import dev.panini.execution.binding.referenceKey

import dev.panini.vyakaranam.ast.AkhyataVakya
import dev.panini.vyakaranam.ast.expandedInvocations

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.bindingName
import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.PrakriyaInvocationArgumentResolver
import dev.panini.execution.Prakriya
import dev.panini.execution.PrakriyaRegistry
import dev.panini.execution.PrakriyaArgumentResolution
import dev.panini.execution.ResolvedPrakriyaArgument
import dev.panini.execution.PrakriyaSignatureDeclarationParser
import dev.panini.execution.PrakriyaValueClassifier
import dev.panini.execution.PrakriyaParameter
import dev.panini.execution.PrakriyaValueType
import dev.panini.execution.TaddhitaInheritanceEngine
import dev.panini.execution.TaddhitaStructEngine
import dev.panini.execution.SanskritValue
import dev.panini.execution.NishedhaGuardEvaluator
import dev.panini.execution.NaturalSemanticNormalizer
import dev.panini.execution.ExecutionPlan
import dev.panini.execution.InjectedKarmanBinding
import dev.panini.execution.planning.ResolvedLeafPlanner
import dev.panini.vyakaranam.ast.Conditional
import dev.panini.vyakaranam.ast.PrakriyaVisibility
import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Pipeline
import dev.panini.vyakaranam.ast.Prakriya as PrakriyaNode
import dev.panini.vyakaranam.ast.ProgramNode
import dev.panini.vyakaranam.ast.Quotation
import dev.panini.vyakaranam.ast.Repeat
import dev.panini.vyakaranam.ast.Recurrence
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.ast.Sequence
import dev.panini.vyakaranam.ast.SequenceConnector
import dev.panini.vyakaranam.ast.WhileLoop
import dev.panini.vyakaranam.ast.AvyayaPada
import dev.panini.vyakaranam.ast.AvyayaFunction
import dev.panini.vyakaranam.ast.MulaPratipadika
import dev.panini.vyakaranam.ast.MulaPratipadikaIdentity
import dev.panini.vyakaranam.ast.semanticKey
import dev.panini.vyakaranam.ast.depthFirst
import dev.panini.vyakaranam.ast.ParyantaRangePada
import dev.panini.vyakaranam.ast.SankhyaPuranaPada
import dev.panini.vyakaranam.ast.SankhyaPada
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Ukti

/** Lowers grammatical control flow to JVM branches while leaves use the normal action runtime. */
internal object CompilerFrontend {
    private sealed interface ProcedureTarget {
        data class Local(val methodName: String) : ProcedureTarget
        data class Dependency(val className: String, val methodName: String) : ProcedureTarget
    }
    internal data class SourceUnit(val name: String, val content: String, val isEntryPoint: Boolean = true)

    fun compile(scriptContent: String, className: String): ByteArray =
        CompilerProgramJvmEmitter.emit(lower(scriptContent, className))

    /** Frontend boundary: parses and lowers a complete source unit without emitting JVM bytecode. */
    internal fun lower(scriptContent: String, className: String): CompilerProgram {
        return lowerModule(listOf(SourceUnit("<memory>", scriptContent)), className)
    }

    /** Analyzes every source file in a module before any executable body is lowered. */
    internal fun lowerModule(sourceUnits: List<SourceUnit>, className: String): CompilerProgram {
        val descriptor = PaniniModuleDescriptor(
            className,
            sourceUnits.map { PaniniModuleSource(it.name, it.content, it.isEntryPoint) },
        )
        return lowerModule(descriptor, className)
    }

    internal fun lowerModule(descriptor: PaniniModuleDescriptor, className: String,
        parseSource: (String) -> List<PvmScriptStatement> = PvmScript::parse): CompilerProgram =
        try {
            lowerModuleUnchecked(descriptor, className, parseSource)
        } catch (error: dev.panini.execution.PvmSemanticException) {
            throw CompilerUnsupportedException(
                CompilerUnsupportedKind.REPETITION,
                error.diagnostic.offendingText,
                "${error.diagnostic.code}: ${error.diagnostic.message}",
                error.diagnostic,
            )
        } catch (error: dev.panini.vyakaranam.parser.PaniniSemanticException) {
            val diagnostic = dev.panini.execution.ExecutionDiagnostic.from(error.diagnostic)
            throw CompilerUnsupportedException(
                CompilerUnsupportedKind.REPETITION,
                diagnostic.offendingText,
                "${diagnostic.code}: ${diagnostic.message}",
                diagnostic,
            )
        }

    /** Keeps semantic failures from every lowering phase behind the compiler diagnostic boundary. */
    private fun lowerModuleUnchecked(descriptor: PaniniModuleDescriptor, className: String,
        parseSource: (String) -> List<PvmScriptStatement>): CompilerProgram {
        val analyzed = PaniniModuleAnalyzer.analyze(descriptor, parseSource)
        val registry = PrakriyaRegistry()
        val methodsByProcedure = mutableMapOf<Prakriya, ProcedureTarget>()
        analyzed.inheritance.forEach { (child, parent) ->
            registry.registerInheritance(dev.panini.execution.InheritanceRelation(child, parent))
        }
        analyzed.procedures.forEach { procedure ->
            val kriya = Prakriya(
                    nameSegmented = procedure.definition.nameSegmented,
                    nameStem = procedure.symbol,
                    body = procedure.definition.body,
                    sourceFile = procedure.source.name,
                    domainStem = procedure.domain,
                    visibility = if (procedure.visibility == PaniniSymbolVisibility.INTERNAL) {
                        PrakriyaVisibility.INTERNAL
                    } else {
                        PrakriyaVisibility.PUBLIC
                    },
                    signatureOverride = procedure.signature,
                    precedence = procedure.definition.prakriya.modifiers.precedence,
                )
            registry.register(kriya)
            methodsByProcedure[kriya] = ProcedureTarget.Local(procedure.methodName)
        }
        descriptor.dependencies.forEach { dependency ->
            dependency.inheritance.forEach { (child, parent) ->
                registry.registerInheritance(dev.panini.execution.InheritanceRelation(child, parent))
            }
            dependency.procedures.forEach { procedure ->
                val kriya = Prakriya(
                        nameSegmented = procedure.symbol,
                        nameStem = procedure.symbol,
                        body = emptyList(),
                        sourceFile = "${dependency.moduleName}.pvmmeta",
                        precedence = procedure.precedence,
                        domainStem = procedure.domain,
                        signatureOverride = dev.panini.execution.PrakriyaSignature(
                            parameters = procedure.parameters,
                            resultType = procedure.resultType,
                            resultSchema = procedure.resultSchema,
                        ),
                    )
                registry.register(kriya)
                methodsByProcedure[kriya] = ProcedureTarget.Dependency(dependency.className, procedure.methodName)
            }
        }
        val methods = analyzed.procedures.associate { it.definition to it.methodName }
        val lowering = Lowering(registry, methodsByProcedure)
        val resultSchemas = descriptor.dependencies.flatMap { it.schemas.entries }
            .associate { it.key to it.value } + analyzed.schemas
        val entryPoint = analyzed.statements.filterKeys(PaniniModuleSource::isEntryPoint).flatMap { (source, statements) ->
            lowering.callerSourceFile = source.name
            statements.flatMap { statement ->
                when (statement) {
                    is PvmScriptStatement.RangeDefinition -> listOf(CompilerInstruction.Constant(statement.range),
                        CompilerInstruction.Store(dev.panini.execution.ACTIVE_RANGE_NAME))
                    is PvmScriptStatement.Sentence -> lowering.lowerTopLevel(statement)
                    else -> emptyList()
                }
            }
        }
        val procedures = analyzed.procedures.map { procedure ->
            lowering.callerSourceFile = procedure.source.name
            val definition = procedure.definition
            val signature = procedure.signature
            val exitLabel = "procedure_contract_exit_${procedure.methodName}"
            lowering.setProcedureReferents(signature.parameters, definition.body)
            val instructions = definition.body.filterNot { sentence ->
                sentence.isNishedha || PrakriyaSignatureDeclarationParser.isDeclaration(sentence)
            }.flatMap { sentence ->
                sentence.program?.let {
                    lowering.lowerProcedureSentence(it, sentence.text, signature.parameters) +
                        listOf(CompilerInstruction.IsProcedureExitRequested,
                            CompilerInstruction.Branch(exitLabel, whenTrue = true))
                }.orEmpty()
            } + CompilerInstruction.Label(exitLabel) + signature.resultSchema?.let { schema ->
                listOf(CompilerInstruction.CheckProcedureResultSchema(schema,
                    requireNotNull(resultSchemas[schema]) {
                        "No schema named '$schema' is declared for procedure '${procedure.symbol}'."
                    }))
            }.orEmpty() + CompilerInstruction.Return
            CompilerProcedure(
                methodName = procedure.methodName,
                instructions = instructions,
                parameterNames = signature.parameters.map { it.nameStem },
                parameterKinds = signature.parameters.map { it.type.toCompilerValueKind() },
                returnKind = signature.resultType?.toCompilerValueKind()
                    ?: signature.resultSchema?.let { CompilerValueKind.RECORD },
            )
        }
        val dependencyProcedures = descriptor.dependencies.flatMap { dependency ->
            dependency.procedures.map { procedure ->
                CompilerDependencyProcedure(
                    dependency.className,
                    procedure.methodName,
                    procedure.parameters.map(PrakriyaParameter::nameStem),
                    procedure.parameters.map { it.type.toCompilerValueKind() },
                    procedure.resultType?.toCompilerValueKind()
                        ?: procedure.resultSchema?.let { CompilerValueKind.RECORD },
                )
            }
        }
        return CompilerProgram(className, entryPoint, procedures, dependencyProcedures)
            .also(CompilerProgramVerifier::verify)
    }

    private class Lowering(
        private val registry: PrakriyaRegistry,
        private val methodsByProcedure: Map<Prakriya, ProcedureTarget>,
    ) {
        private var nextLabel = 0
        var callerSourceFile: String? = null
        private var referents = emptyList<dev.panini.execution.AgreementCandidate<String>>()
        private var procedureEnvironment = dev.panini.execution.ValueEnvironment()
        private var inProcedure = false
        private var resultRoleShadowed = false
        private var loopDepth = 0

        private inline fun <T> withinLoop(block: () -> T): T {
            loopDepth++
            return try { block() } finally { loopDepth-- }
        }

        fun setProcedureReferents(parameters: List<PrakriyaParameter>, body: List<PvmScriptStatement.Sentence>) {
            inProcedure = true
            resultRoleShadowed = parameters.any { it.nameStem == "फल" }
            val declarations = dev.panini.execution.PrakriyaParameterDeclarations.resolve(parameters, body)
            referents = parameters.mapIndexed { index, parameter ->
                dev.panini.execution.AgreementCandidate(dev.panini.execution.PrakriyaAstArgumentBinder.referenceKey(index),
                    dev.panini.execution.ReferentAgreement.fromDeclaration(declarations[parameter.nameStem]))
            }
            procedureEnvironment = dev.panini.execution.ValueEnvironment(
                constraints = parameters.indices.associate { index ->
                    dev.panini.execution.PrakriyaAstArgumentBinder.referenceKey(index) to
                        dev.panini.execution.SemanticValueConstraint()
                },
            )
        }

        private fun resolveAnaphors(node: Invocation): Invocation =
            when (val analysis = dev.panini.execution.AnaphoricOperandProjection.analyze(node, referents,
                protectedPadas = dev.panini.execution.PrakriyaAstArgumentBinder.protectedParameterPadas(
                    node.vakya.padas, registry.all().mapTo(mutableSetOf()) { it.nameStem }),
                referenceName = { it })) {
                is dev.panini.execution.AnaphoricOperandProjection.Analysis.Resolved ->
                    analysis.projection.invocation
                is dev.panini.execution.AnaphoricOperandProjection.Analysis.Invalid ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.INVOCATION,
                        normalized(render(node)),
                        "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                    )
            }
        fun lowerProcedureSentence(node: ProgramNode, source: String, parameters: List<PrakriyaParameter>): List<CompilerInstruction> {
            val bound = dev.panini.execution.PrakriyaAstArgumentBinder.bind(node, parameters,
                parameters.size, registry.all().mapTo(mutableSetOf()) { it.nameStem })
            return lower(bound, source)
        }
        private val resultLexicon by lazy {
            dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon(
                emptyList(), dev.panini.dhatupatha.DhatuPatha.all,
            )
        }
        private val assertedStructFields =
            mutableMapOf<String, LinkedHashMap<String, dev.panini.execution.TaddhitaFieldAssertion>>()

        fun lowerTopLevel(sentence: PvmScriptStatement.Sentence): List<CompilerInstruction> {
            // The module analyzer retains these declarations as schema metadata.
            if (TaddhitaStructEngine.detectResultSchema(sentence.text, sentence.ukti) != null) return emptyList()
            TaddhitaStructEngine.detectFieldAssertion(sentence.text, sentence.ukti)?.let { assertion ->
                val fields = assertedStructFields.getOrPut(assertion.ownerStem, ::linkedMapOf)
                fields[assertion.fieldStem] = assertion
                return buildList {
                    fields.values.forEach {
                        add(CompilerInstruction.Constant(TaddhitaStructEngine.assertionValue(it)))
                    }
                    add(CompilerInstruction.BuildRecord(assertion.ownerStem, fields.keys.toList()))
                    add(CompilerInstruction.Store(assertion.ownerStem))
                }
            }
            return sentence.program?.let {
                lower(it, sentence.text, allowDirectStore = true)
            }.orEmpty()
        }

        fun lower(
            node: ProgramNode,
            exactSource: String? = null,
            allowDirectStore: Boolean = false,
        ): List<CompilerInstruction> = when (node) {
            is Invocation -> if (dev.panini.execution.NaturalScriptStop.matches(node)) {
                dev.panini.execution.NaturalScriptStop.contextDiagnostic(node, inProcedure)?.let { diagnostic ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.PROCEDURE,
                        render(node),
                        "${diagnostic.code}: ${diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(diagnostic),
                    )
                }
                listOf(CompilerInstruction.HaltScript)
            } else if (loopDepth > 0 && dev.panini.execution.NaturalLoopStop.matches(node)) {
                listOf(CompilerInstruction.RequestBreak)
            } else lowerInvocation(node, exactSource, allowDirectStore = allowDirectStore)
            is Sequence -> lowerSequence(node, exactSource)
            is Pipeline -> lowerPipeline(node)
            is Quotation -> when (val analysis = dev.panini.execution.ListDeclarationLowering.analyze(node)) {
                dev.panini.execution.ListDeclarationLowering.Analysis.NotDeclaration -> lowerPlannedNode(node)
                is dev.panini.execution.ListDeclarationLowering.Analysis.Resolved -> lowerSequence(analysis.sequence, null)
                is dev.panini.execution.ListDeclarationLowering.Analysis.Invalid ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.INVOCATION, render(node),
                        "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                    )
            }
            is Conditional -> lowerConditionalIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.CONDITIONAL, render(node), "Cannot lower conditional to compiler IR.",
            )
            is Repeat -> lowerRepeatIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.REPETITION, render(node), "Cannot lower repetition body to compiler IR.",
            )
            is Recurrence -> lowerRecurrenceIr(node)
            is WhileLoop -> lowerWhileIr(node) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.LOOP, render(node), "Cannot lower condition-controlled loop to compiler IR.",
            )
            is dev.panini.vyakaranam.ast.ForEach -> lowerForEach(node)
            is dev.panini.vyakaranam.ast.CollectionRelativeAction -> lowerCollectionRelativeAction(node)
            is dev.panini.vyakaranam.ast.QuantifiedCollectionPredicate -> lowerQuantifiedCollectionPredicate(node)
            is PrakriyaNode -> node.body.flatMap { lower(it) }
            is Scope -> node.body.flatMap { lower(it) }
        }

        private fun lowerSequence(node: Sequence, exactSource: String?): List<CompilerInstruction> {
            val structuralConnectors = setOf(
                SequenceConnector.ANANTARYA,
                SequenceConnector.PURVAKALA,
                SequenceConnector.SAMUCCAYA,
                SequenceConnector.VAKYANTARA,
                SequenceConnector.TADANIM,
            )
            if (node.connectorKinds.any { it !in structuralConnectors }) {
                return lowerPlannedNode(node)
            }
            val exit = "sequence_return_${nextLabel++}"
            return node.statements.flatMapIndexed { index, statement ->
                val instructions =
                if (index == 0) {
                    lower(statement)
                } else if (statement is Invocation) {
                    if (dev.panini.execution.NaturalScriptStop.matches(statement)) lower(statement)
                    else lowerInvocation(statement, exactSource = null,
                        piped = node.boundaries.getOrNull(index - 1)?.resultFlow ==
                            dev.panini.vyakaranam.ast.SequenceResultFlow.PREVIOUS_RESULT)
                } else {
                    lower(statement)
                }
                instructions + listOf(CompilerInstruction.IsProcedureExitRequested,
                    CompilerInstruction.Branch(exit, whenTrue = true))
            } + CompilerInstruction.Label(exit)
        }

        private fun lowerFieldAccess(access: dev.panini.execution.TaddhitaAttributeAccess): List<CompilerInstruction> = buildList {
            add(CompilerInstruction.Load(access.chain.first()))
            access.chain.drop(1).dropLast(1).forEach { field ->
                add(CompilerInstruction.LoadField(field))
                add(CompilerInstruction.ResolveRecordReference)
            }
            add(CompilerInstruction.LoadFieldOrLopa(access.chain.last()))
        }

        private fun lowerInvocation(
            node: Invocation,
            exactSource: String?,
            piped: Boolean = false,
            allowDirectStore: Boolean = false,
        ): List<CompilerInstruction> {
            when (val analysis = dev.panini.execution.CopularAgreement.analyze(node)) {
                dev.panini.execution.CopularAgreement.Analysis.Valid -> Unit
                is dev.panini.execution.CopularAgreement.Analysis.Invalid ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.INVOCATION, render(node),
                        "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                    )
            }
            if (piped && dev.panini.execution.PriorResultInjection.hasExplicitDisplayObject(node))
                return lowerInvocation(node, exactSource, piped = false, allowDirectStore = allowDirectStore)
            val anaphoric = resolveAnaphors(node)
            if (anaphoric != node) return lowerInvocation(anaphoric, null, piped, allowDirectStore)
            if (inProcedure) when (val analysis = dev.panini.execution.ProcedureResultStatement.analyze(node,
                roleShadowed = resultRoleShadowed || referents.any { it.identity == "फल" })) {
                dev.panini.execution.ProcedureResultStatement.Analysis.NotResult -> Unit
                is dev.panini.execution.ProcedureResultStatement.Analysis.Resolved -> {
                    val value = dev.panini.execution.NominalResultSemantics.operand(analysis.relation.value)
                    return listOf(lowerSemanticOperand(value.semantic, value.fallback),
                        CompilerInstruction.RequestProcedureReturn)
                }
                is dev.panini.execution.ProcedureResultStatement.Analysis.Invalid ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.PROCEDURE,
                        render(node),
                        "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                    )
            }
            node.implicitValuePada?.let { return lowerNominalResult(it) }
            if (node.vakya.padas.any { it is dev.panini.vyakaranam.ast.SamuccitaSubanta }) {
                val members = node.vakya.padas.flatMap {
                    if (it is dev.panini.vyakaranam.ast.SamuccitaSubanta) it.members else listOf(it)
                }
                if (dev.panini.execution.binding.KarakaReferenceResolver.references(members).isNotEmpty() ||
                    dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(members).isNotEmpty()) {
                    val vakya = when (val original = node.vakya) {
                        is dev.panini.vyakaranam.ast.AkhyataVakya -> original.copy(padas = members)
                        is dev.panini.vyakaranam.ast.NamaVakya -> original.copy(padas = members)
                    }
                    return lowerInvocation(node.copy(vakya = vakya), null, piped, allowDirectStore)
                }
            }
            TaddhitaStructEngine.detectAttributeAccess(node.vakya)?.let { access ->
                return lowerFieldAccess(access) + CompilerInstruction.Store("LastResult")
            }
            val attributeProjection = dev.panini.execution.AttributeOperandProjection.project(
                node, "__pvm_attribute_${nextLabel++}_", registry.all().mapTo(mutableSetOf()) { it.nameStem })
            if (attributeProjection.operands.isNotEmpty()) return buildList {
                attributeProjection.operands.forEach { operand ->
                    addAll(lowerFieldAccess(operand.access))
                    add(CompilerInstruction.Store(operand.name))
                }
                addAll(lowerInvocation(attributeProjection.invocation, null, piped, allowDirectStore))
            }
            when (val analysis = dev.panini.execution.CollectionMemberMorphology.analyze(node)) {
                dev.panini.execution.CollectionMemberMorphology.Analysis.Valid -> Unit
                is dev.panini.execution.CollectionMemberMorphology.Analysis.Invalid ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.INVOCATION, render(node),
                        "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                    )
            }
            when (val analysis = dev.panini.execution.ListDeclarationLowering.analyze(node)) {
                dev.panini.execution.ListDeclarationLowering.Analysis.NotDeclaration -> Unit
                is dev.panini.execution.ListDeclarationLowering.Analysis.Resolved ->
                    return lowerSequence(analysis.sequence, null)
                is dev.panini.execution.ListDeclarationLowering.Analysis.Invalid ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.INVOCATION, render(node),
                        "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                    )
            }
            when (val analysis = dev.panini.execution.OrdinalObjectLowering.analyze(node)) {
                dev.panini.execution.OrdinalObjectLowering.Analysis.NotOrdinalObject -> Unit
                is dev.panini.execution.OrdinalObjectLowering.Analysis.Resolved ->
                    return lowerSequence(analysis.sequence, null)
                is dev.panini.execution.OrdinalObjectLowering.Analysis.Invalid ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.INVOCATION, render(node),
                        "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                    )
            }
            lowerPriorAction(node)?.let { return it }
            lowerKarakaHistoryInvocation(node)?.let { return withActionIdentity(it, node) }
            rejectUnsupportedKarakaHistory(node)
            val rendered = exactSource ?: render(node)
            val nominalPadas = node.vakya.padas.filterIsInstance<SubantaPada>()
            val hasNamedActionResult = nominalPadas.withIndex().any { (index, pada) ->
                NaturalSemanticNormalizer.isPriorResult(pada) && nominalPadas.take(index).lastOrNull { modifier ->
                    dev.panini.core.SupAffix.candidates(modifier.sup.text).any {
                        it.vibhakti == dev.panini.core.Vibhakti.SASTHI
                    }
                }?.pratipadika is dev.panini.vyakaranam.ast.KridantaPratipadika
            }
            val namedReferences = dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(node.vakya.padas)
            dev.panini.execution.binding.NamedActionResultReferenceResolver.agreementDiagnostic(namedReferences)?.let { diagnostic ->
                throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.INVOCATION,
                    rendered,
                    "${diagnostic.code}: ${diagnostic.message}",
                    dev.panini.execution.ExecutionDiagnostic.from(diagnostic),
                )
            }
            if (namedReferences.isNotEmpty()) {
                val (projected, loads) = projectNamedHistoryOperands(node, namedReferences)
                // Explicit result operands already satisfy the original prior-result
                // rule; projection must not turn them into an implicit pipeline input.
                return loads + lowerInvocation(projected, null, piped = false, allowDirectStore = allowDirectStore) +
                    CompilerInstruction.ExitFrame
            }
            lowerProcedureCall(node, piped)?.let { return it }
            if (hasNamedActionResult) throw CompilerUnsupportedException(
                CompilerUnsupportedKind.INVOCATION, rendered,
                "The named result has no resolved action-history operand.",
            )
            val alreadyReferencesResult = node.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.SubantaPada &&
                    NaturalSemanticNormalizer.isPriorResult(pada)
            }
            val source = normalized(rendered)
            val naturalSemantic = NaturalSemanticNormalizer.normalize(node)
            lowerRangeChoice(node)?.let { return withActionIdentity(it, node) }
            val consumesPriorResult = piped && !alreadyReferencesResult ||
                naturalSemantic ==
                NaturalSemanticNormalizer.Operation.DisplayPriorResult
            val injectedBindings = if (consumesPriorResult) {
                mapOf(Karaka.KARMAN to ExecutionExpression.Reference("LastResult"))
            } else {
                emptyMap()
            }
            return (ResolvedLeafPlanner.plan(
                node,
                environment = procedureEnvironment,
                allowStore = allowDirectStore,
                injectedBindings = injectedBindings,
            ) ?: ResolvedLeafPlanner.planAny(
                node,
                environment = procedureEnvironment,
                injectedBindings = injectedBindings,
            ) ?: if (consumesPriorResult) ResolvedLeafPlanner.planAny(node, procedureEnvironment) else null
            )?.let { withActionIdentity(lowerDirect(it), node, it.resolved.invocation.dhatu) }
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.INVOCATION, source, "Cannot resolve invocation as a compiler leaf.",
                )
        }

        private fun projectNamedHistoryOperands(node: Invocation,
            references: List<dev.panini.execution.binding.NamedActionResultReference>
        ): Pair<Invocation, List<CompilerInstruction>> {
            val occupied = node.vakya.padas.flatMap {
                if (it is dev.panini.vyakaranam.ast.SamuccitaSubanta) it.members else listOf(it)
            }.filterIsInstance<SubantaPada>().mapTo(mutableSetOf()) { it.pratipadika.referenceKey() }
            val names = references.map {
                var name: String
                do { name = "__pvm_history_operand_${nextLabel++}" } while (!occupied.add(name))
                name
            }
            val padas = dev.panini.execution.binding.NamedActionResultReferenceResolver
                .operandPadas(node.vakya.padas).map { pada ->
                    val index = references.indexOfFirst { it.result === pada }
                    if (index < 0) pada else references[index].result.copy(
                        pratipadika = MulaPratipadika(names[index], names[index]))
                        .inheritLocation(references[index].result)
                }
            val vakya = when (val original = node.vakya) {
                is AkhyataVakya -> original.copy(padas = padas)
                is dev.panini.vyakaranam.ast.NamaVakya -> original.copy(padas = padas)
            }
            val loads = buildList {
                references.forEach { reference ->
                    add(historyLoad(reference))
                }
                add(CompilerInstruction.EnterFrame(names, scopedBinding = true))
            }
            return node.copy(vakya = vakya) to loads
        }

        private fun lowerRangeChoice(node: Invocation): List<CompilerInstruction>? {
            val semantic = NaturalSemanticNormalizer.normalize(node)
                as? NaturalSemanticNormalizer.Operation.RangeChoice
                ?: return null
            val evaluator = dev.panini.sankhya.SankhyaEvaluator()
            val astBounds = node.vakya.padas.flatMap { pada ->
                when (pada) {
                    is ParyantaRangePada -> listOf(pada.lowerLimit, pada.upperLimit).map { bound ->
                        requireNotNull(NaturalSemanticNormalizer.boundaryValue(bound))
                    }
                    is SankhyaPada -> listOf(pada.value ?: evaluator.evaluateStems(pada.stems).value)
                    is SubantaPada -> (pada.pratipadika as? MulaPratipadika)?.text?.let { stem ->
                        runCatching { evaluator.evaluateStems(listOf(stem)).value }.getOrNull()
                    }?.let(::listOf).orEmpty()
                    else -> emptyList()
                }
            }
            val exclusionName = semantic.exclusionName
            return buildList {
                if (astBounds.size < 2) add(CompilerInstruction.Load(dev.panini.execution.ACTIVE_RANGE_NAME))
                exclusionName?.let { add(CompilerInstruction.Load(it)) }
                if (astBounds.size >= 2) add(CompilerInstruction.RandomRange(astBounds[0], astBounds[1], exclusionName != null))
                else add(CompilerInstruction.RandomActiveRange(exclusionName != null))
                add(CompilerInstruction.Store("LastResult"))
                val head = node.vakya.padas.filterIsInstance<TingantaPada>().single()
                val dhatu = requireNotNull(resultLexicon.findDhatu(head.dhatu.mulaDhatu))
                add(CompilerInstruction.LoadLastResult)
                add(CompilerInstruction.RecordActionResult(dhatu.upadesha))
            }
        }

        /** Lowers an already parsed call without rendering and reparsing its Sanskrit. */
        private fun lowerProcedureCall(node: Invocation, piped: Boolean): List<CompilerInstruction>? {
            val ukti = Ukti(node.sourceText, body = node)
            val injected = if (piped) InjectedKarmanBinding("LastResult", null) else null
            val invocation = registry.detectInvocation(ukti, callerSourceFile = callerSourceFile,
                injectedKarman = injected, staticAnalysis = true) ?: return null
            return lowerProcedureInvocation(invocation)
        }

        private fun lowerProcedureInvocation(
            invocation: dev.panini.execution.PrakriyaInvocation,
        ): List<CompilerInstruction>? {
            invocation.resolutionError?.let { message ->
                val diagnostic = dev.panini.execution.PrakriyaCallDiagnostics.atInvocation(
                    invocation, "AMBIGUOUS_PROCEDURE_OVERLOAD", message)
                throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.PROCEDURE,
                    invocation.fullText,
                    "${diagnostic.code}: ${diagnostic.message}",
                    dev.panini.execution.ExecutionDiagnostic.from(diagnostic),
                )
            }
            val target = methodsByProcedure[invocation.kriya] ?: return null
            val signature = invocation.kriya.signature
            val parameterNames = signature.parameters.map { it.nameStem }
            val parameterKinds = signature.parameters.map { it.type.toCompilerValueKind() }
            val arguments = resolveArguments(invocation)
            return buildList {
                arguments.take(parameterNames.size).forEachIndexed { index, argument ->
                    add(lowerCallArgument(argument, invocation.argumentValues.getOrNull(index)))
                }
                invocation.kriya.nishedhaGuards.forEach { guard ->
                    NishedhaGuardEvaluator.numericProhibition(guard, signature.parameters, arguments.size)?.let { prohibition ->
                        fun operand(value: NishedhaGuardEvaluator.NumericOperand): CompilerInstruction = when (value) {
                            is NishedhaGuardEvaluator.NumericOperand.Argument -> lowerCallArgument(
                                arguments[value.index], invocation.argumentValues.getOrNull(value.index))
                            is NishedhaGuardEvaluator.NumericOperand.Literal -> CompilerInstruction.Constant(
                                dev.panini.execution.SanskritValue.Sankhya(value.value, value.value.toString()))
                        }
                        add(operand(prohibition.left))
                        add(operand(prohibition.right))
                        add(CompilerInstruction.CheckNumericProhibition)
                    }
                }
                val argumentDiagnostics = signature.parameters.mapIndexed { index, parameter ->
                    val diagnostic = dev.panini.execution.PrakriyaCallDiagnostics.atInvocation(
                        invocation,
                        "PROCEDURE_ARGUMENT_TYPE_MISMATCH",
                        dev.panini.execution.PrakriyaDiagnostics.parameterType(parameter),
                        index,
                    )
                    dev.panini.execution.ExecutionDiagnostic.from(diagnostic)
                }
                add(CompilerInstruction.EnterFrame(
                    parameterNames,
                    parameterKinds,
                    typedParticipants = signature.hasTypedParticipants,
                    argumentDiagnostics = argumentDiagnostics,
                ))
                add(when (target) {
                    is ProcedureTarget.Local -> CompilerInstruction.InvokeProcedure(
                        target.methodName,
                        parameterNames.size,
                        signature.resultType?.toCompilerValueKind()
                            ?: signature.resultSchema?.let { CompilerValueKind.RECORD },
                    )
                    is ProcedureTarget.Dependency -> CompilerInstruction.InvokeDependencyProcedure(
                        target.className,
                        target.methodName,
                        parameterNames.size,
                        signature.resultType?.toCompilerValueKind()
                            ?: signature.resultSchema?.let { CompilerValueKind.RECORD },
                    )
                })
                add(CompilerInstruction.ExitFrame)
            }
        }

        private fun lowerCallArgument(
            argument: ResolvedPrakriyaArgument,
            fallbackValue: dev.panini.execution.SanskritValue?,
        ): CompilerInstruction {
            return when {
                argument.argument.actionResult != null -> historyLoad(requireNotNull(argument.argument.actionResult))
                argument.isPriorResult -> CompilerInstruction.LoadLastResult
                else -> lowerSemanticOperand(argument.semanticOperand(fallbackValue))
            }
        }

        private fun lowerSemanticOperand(
            operand: dev.panini.execution.SemanticOperand,
            referenceFallback: dev.panini.execution.SanskritValue? = null,
        ): CompilerInstruction = when (operand) {
            is dev.panini.execution.SemanticOperand.Literal -> CompilerInstruction.Constant(operand.value)
            is dev.panini.execution.SemanticOperand.Resolved -> CompilerInstruction.Constant(operand.value)
            is dev.panini.execution.SemanticOperand.Reference ->
                CompilerInstruction.ResolveArgument(operand.name, referenceFallback ?: operand.fallback)
        }

        private fun lowerPipeline(node: Pipeline): List<CompilerInstruction> = buildList {
            fun pipelineFailure(code: String, message: String, argumentIndex: Int? = null): Nothing {
                val diagnostic = dev.panini.execution.PipelineSemanticDiagnostics.atPipeline(
                    node, code, message, argumentIndex)
                throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.PIPELINE,
                    node.sourceText,
                    "${diagnostic.code}: ${diagnostic.message}",
                    dev.panini.execution.ExecutionDiagnostic.from(diagnostic),
                )
            }
            val originalArguments = node.arguments.mapIndexed { index, term ->
                dev.panini.execution.PrakriyaArgument(term, pada = node.argumentPadas.getOrNull(index),
                    origin = dev.panini.execution.PrakriyaArgumentOrigin.WRITTEN)
            }
            val originalTypes = originalArguments.map { argument ->
                argument.pada?.let(PrakriyaValueClassifier::classifyStaticPada)
                    ?: PrakriyaValueType.UNCONSTRAINED
            }
            var precedingType = PrakriyaValueType.UNCONSTRAINED
            node.stages.forEachIndexed { stageIndex, stage ->
                val arguments = if (stageIndex == 0) originalArguments else listOf(
                    dev.panini.execution.PrakriyaArgument("फल", origin = dev.panini.execution.PrakriyaArgumentOrigin.PIPE)
                ) + originalArguments.drop(1)
                val types = if (stageIndex == 0) originalTypes else listOf(precedingType) + originalTypes.drop(1)
                val invocation = registry.resolveStructuredInvocation(stage.operationStem, stage.domainStem,
                    arguments, node.sourceText, callerSourceFile = callerSourceFile, analyzedArgumentTypes = types)
                    ?: throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.PIPELINE,
                        node.sourceText,
                        "Missing signature for pipeline stage '${stage.operationStem}'.",
                    )
                invocation.resolutionError?.let {
                    pipelineFailure("AMBIGUOUS_PIPELINE_STAGE", it)
                }
                val kriya = invocation.kriya
                val target = methodsByProcedure[kriya] ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.PIPELINE, node.sourceText,
                    "Unknown compiled pipeline stage '${stage.operationStem}'.")
                val parameters = kriya.signature.parameters
                if (parameters.isNotEmpty() && parameters.size != arguments.size) {
                    pipelineFailure(
                        "PROCEDURE_ARITY_MISMATCH",
                        dev.panini.execution.PrakriyaDiagnostics.arity(
                            stage.operationStem, parameters.size, arguments.size),
                    )
                }
                parameters.forEachIndexed { parameterIndex, _ ->
                    if (stageIndex > 0 && parameterIndex == 0) {
                        add(CompilerInstruction.LoadLastResult)
                    } else {
                        val argumentPada = node.argumentPadas.getOrNull(parameterIndex) as? SubantaPada
                            ?: throw CompilerUnsupportedException(
                                CompilerUnsupportedKind.PIPELINE,
                                node.sourceText,
                                "Pipeline stage '${stage.operationStem}' lacks parsed nominal argument ${parameterIndex + 1}.",
                            )
                        val literal = PrakriyaValueClassifier.literalValue(argumentPada)
                        add(literal?.let(CompilerInstruction::Constant)
                            ?: CompilerInstruction.ResolveArgument(argumentPada.pratipadika.referenceKey(),
                                PrakriyaValueClassifier.fallbackValue(argumentPada)))
                    }
                }
                val kinds = parameters.map { it.type.toCompilerValueKind() }
                add(CompilerInstruction.EnterFrame(parameters.map { it.nameStem }, kinds,
                    typedParticipants = kriya.signature.hasTypedParticipants))
                add(when (target) {
                    is ProcedureTarget.Local -> CompilerInstruction.InvokeProcedure(
                        target.methodName,
                        parameters.size,
                        kriya.signature.resultType?.toCompilerValueKind()
                            ?: kriya.signature.resultSchema?.let { CompilerValueKind.RECORD },
                    )
                    is ProcedureTarget.Dependency -> CompilerInstruction.InvokeDependencyProcedure(
                        target.className,
                        target.methodName,
                        parameters.size,
                        kriya.signature.resultType?.toCompilerValueKind()
                            ?: kriya.signature.resultSchema?.let { CompilerValueKind.RECORD },
                    )
                })
                add(CompilerInstruction.ExitFrame)
                precedingType = kriya.signature.resultType ?: kriya.signature.resultSchema?.let { PrakriyaValueType.RUPA }
                    ?: PrakriyaValueType.UNCONSTRAINED
            }
        }

        private fun lowerKarakaHistoryInvocation(node: Invocation): List<CompilerInstruction>? {
            val references = dev.panini.execution.binding.KarakaReferenceResolver.references(node.vakya.padas)
            if (references.isEmpty()) return null
            references.forEach(CompilerIrLowering::lowerKarakaHistory)
            if (references.any { reference -> dev.panini.core.SupAffix.candidates(reference.referent.sup.text).none {
                it.vibhakti == dev.panini.core.Vibhakti.DVITIYA
            } }) return null
            lowerKarakaHistoryValueOperation(node, references)?.let { return it }
            val protected = dev.panini.execution.binding.KarakaReferenceResolver.protectedPadas(node.vakya.padas)
            val remaining = node.vakya.padas.filter { it !in protected }
            val sentence = node.vakya as? dev.panini.vyakaranam.ast.AkhyataVakya ?: return null
            val heads = remaining.filter { it is TingantaPada || it is dev.panini.vyakaranam.ast.AvyayaPada }
            val projected = node.copy(vakya = sentence.copy(padas = heads))
            if (references.size == 1 && remaining.all { pada ->
                pada in heads || (pada is SubantaPada && dev.panini.core.SupAffix.candidates(pada.sup.text)
                    .any { it.vibhakti == dev.panini.core.Vibhakti.CHATURTHI })
            }) {
                lowerBareParticipantGroup(node.copy(vakya = sentence.copy(padas = remaining)), references.single())?.let { return it }
            }
            val loads = references.mapIndexed { index, reference ->
                "__history_operand_${nextLabel++}_$index" to CompilerIrLowering.lowerKarakaHistory(reference)
            }.toMap()
            val names = loads.keys.toList()
            val operands = node.vakya.padas.mapNotNull { pada ->
                val referenceIndex = references.indexOfFirst { it.referent === pada }
                if (referenceIndex >= 0) return@mapNotNull ExecutionExpression.Reference(names[referenceIndex])
                if (pada in protected || pada is TingantaPada || pada is dev.panini.vyakaranam.ast.AvyayaPada) return@mapNotNull null
                // Ordinary operands retain the same grammatical binder as other leaves.
                val ordinary = ResolvedLeafPlanner.planAny(node.copy(vakya = sentence.copy(padas = listOf(pada) + heads)))
                    ?: return null
                ordinary.resolved.context.bindings[Karaka.KARMAN] ?: return null
            }
            val expression = if (operands.size == 1) operands.single() else ExecutionExpression.Coordination(operands)
            val plan = ResolvedLeafPlanner.planAny(projected, injectedBindings = mapOf(Karaka.KARMAN to expression)) ?: return null
            // Other operations need participant-group expansion according to their valency.
            if (plan.resolved.operation.name != "प्रदर्शनम्") return null
            return lowerDirect(plan).map { instruction ->
                when (instruction) {
                    is CompilerInstruction.Load -> loads[instruction.name] ?: instruction
                    is CompilerInstruction.ResolveArgument -> loads[instruction.name] ?: instruction
                    else -> instruction
                }
            }
        }

        private fun lowerBareParticipantGroup(node: Invocation,
            reference: dev.panini.execution.binding.KarakaHistoryReference): List<CompilerInstruction>? {
            val conversation = dev.panini.execution.SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
            val bound = dev.panini.execution.binding.VyakaranamExecutionAdapter.bind(
                dev.panini.execution.SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", node.sourceText),
                dev.panini.vyakaranam.ast.Ukti(node.sourceText, body = node), conversation,
                injectedBindings = mapOf(Karaka.KARMAN to ExecutionExpression.Reference("__participant_group")))
                as? dev.panini.execution.ExecutionBindingResult.Bound ?: return null
            val invocation = bound.ukti.invocations.singleOrNull() ?: return null
            val candidates = invocation.dhatu.operations.filter { it.trigger.matches(invocation.grammaticalFeatures) }
            if (candidates.isEmpty() || candidates.any { it.name !in participantGroupOperations }) return null
            val load = when (val instruction = CompilerIrLowering.lowerKarakaHistory(reference)) {
                is CompilerInstruction.LoadActionParticipants -> instruction.copy(asGroup = true)
                is CompilerInstruction.LoadOrdinalActionParticipants -> instruction.copy(asGroup = true)
                else -> error("Expected participant history load")
            }
            return listOf(load, CompilerInstruction.CallParticipantGroup(invocation.dhatu.upadesha,
                invocation.grammaticalFeatures, invocation.selectedOperation,
                invocation.bindings.filterKeys { it !in setOf(Karaka.KARMAN, Karaka.ANIRDHARITA) }), CompilerInstruction.Store("LastResult"))
        }

        private fun lowerKarakaHistoryValueOperation(node: Invocation,
            references: List<dev.panini.execution.binding.KarakaHistoryReference>): List<CompilerInstruction>? {
            val sentence = node.vakya as? AkhyataVakya ?: return null
            val padas = sentence.padas.flatMap {
                if (it is dev.panini.vyakaranam.ast.SamuccitaSubanta) it.members else listOf(it)
            }
            val occupied = padas.filterIsInstance<SubantaPada>().mapTo(mutableSetOf()) { it.pratipadika.referenceKey() }
            val loads = linkedMapOf<String, CompilerInstruction>()
            val replacements = java.util.IdentityHashMap<SubantaPada, SubantaPada>()
            references.forEach { reference ->
                var name: String
                do { name = "__pvm_history_group_${nextLabel++}" } while (!occupied.add(name))
                loads[name] = when (val load = CompilerIrLowering.lowerKarakaHistory(reference)) {
                    is CompilerInstruction.LoadActionParticipants -> load.copy(asGroup = true)
                    is CompilerInstruction.LoadOrdinalActionParticipants -> load.copy(asGroup = true)
                    else -> error("Expected a participant history load")
                }
                replacements[reference.referent] = reference.referent.copy(
                    pratipadika = MulaPratipadika(name, name)).inheritLocation(reference.referent)
            }
            val protected = dev.panini.execution.binding.KarakaReferenceResolver.protectedPadas(sentence.padas)
            val projected = node.copy(vakya = sentence.copy(padas = padas.mapNotNull { pada ->
                (pada as? SubantaPada)?.let(replacements::get) ?: pada.takeUnless { it in protected }
            }))
            val plan = ResolvedLeafPlanner.planAny(projected)
            val symbolicInvocation = if (plan == null) {
                val conversation = dev.panini.execution.SambhashanaContext("प्रयोक्ता", "यन्त्रम्")
                (dev.panini.execution.binding.VyakaranamExecutionAdapter.bind(
                    dev.panini.execution.SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", projected.sourceText),
                    dev.panini.vyakaranam.ast.Ukti(projected.sourceText, body = projected),
                    conversation,
                    environment = dev.panini.execution.ValueEnvironment(
                        constraints = loads.keys.associateWith { dev.panini.execution.SemanticValueConstraint() },
                    ),
                ) as? dev.panini.execution.ExecutionBindingResult.Bound)?.ukti?.invocations?.singleOrNull()
                    ?: return null
            } else plan.resolved.invocation
            val operations = symbolicInvocation.dhatu.operations.filter {
                it.trigger.matches(symbolicInvocation.grammaticalFeatures)
            }
            val selectedOperation = plan?.resolved?.operation
                ?: operations.firstOrNull { it.action is dev.panini.actions.numeric.NumericFoldDhatuAction }
                ?: operations.singleOrNull()
                ?: return null
            val numericAction = selectedOperation.action as? dev.panini.actions.numeric.NumericFoldDhatuAction
            val arithmetic = when (numericAction?.operator) {
                dev.panini.actions.numeric.NumericFoldOperator.ADD -> ArithmeticOperator.ADD
                dev.panini.actions.numeric.NumericFoldOperator.MULTIPLY -> ArithmeticOperator.MULTIPLY
                dev.panini.actions.numeric.NumericFoldOperator.SUBTRACT -> ArithmeticOperator.SUBTRACT
                dev.panini.actions.numeric.NumericFoldOperator.DIVIDE -> ArithmeticOperator.DIVIDE
                dev.panini.actions.numeric.NumericFoldOperator.MODULO -> ArithmeticOperator.REMAINDER
                else -> null
            }
            if (selectedOperation.name != "मूल्यदानम्" && arithmetic == null) return null
            val resolvedBindings = plan?.resolved?.context?.bindings ?: symbolicInvocation.bindings
            val destination = selectedOperation.resultBindingKaraka
                ?.let(resolvedBindings::get)?.bindingName()
            if (arithmetic == null && destination == null) return null
            val participants = resolvedBindings.entries.filter { it.key != Karaka.ANIRDHARITA }
                .flatMap { (role, expression) ->
                    CompilerIrLowering.lowerParticipantOperands(expression).map { instructions ->
                        var group = false
                        val lowered = instructions.map { instruction ->
                            val name = when (instruction) {
                                is CompilerInstruction.Load -> instruction.name
                                is CompilerInstruction.ResolveArgument -> instruction.name
                                else -> null
                            }
                            loads[name]?.also { group = true } ?: instruction
                        }
                        Triple(role, lowered, group)
                    }
                }
            val operands = participants.indices.filter { participants[it].first == Karaka.KARMAN }
            if (operands.isEmpty()) return null
            val candidates = operations
            val dynamic = arithmetic != null && candidates.any { it.action !is dev.panini.actions.numeric.NumericFoldDhatuAction }
            if (dynamic && (participants.any { it.first !in setOf(Karaka.KARMAN, Karaka.KARTR, Karaka.SAMPRADANA) } ||
                    candidates.any { it.name !in participantGroupOperations })) return null
            val locals = participants.indices.map { "history_assignment_${nextLabel++}_$it" }
            return buildList {
                participants.forEachIndexed { index, (_, instructions, _) ->
                    addAll(instructions)
                    add(CompilerInstruction.StoreLocal(locals[index]))
                }
                operands.forEach { add(CompilerInstruction.LoadLocal(locals[it])) }
                val flags = operands.map { participants[it].third }
                if (dynamic) {
                    add(CompilerInstruction.PackOperands(flags, asGroup = true))
                    add(CompilerInstruction.CallParticipantGroup(symbolicInvocation.dhatu.upadesha,
                        symbolicInvocation.grammaticalFeatures, symbolicInvocation.selectedOperation,
                        resolvedBindings.filterKeys { it !in setOf(Karaka.KARMAN, Karaka.ANIRDHARITA) }))
                    add(CompilerInstruction.Store("LastResult"))
                    return@buildList
                }
                add(if (arithmetic == null) CompilerInstruction.PackOperands(flags)
                    else CompilerInstruction.FoldNumericOperands(arithmetic, flags,
                        minimumOperands = requireNotNull(numericAction).minimumOperands))
                if (destination != null) add(CompilerInstruction.Duplicate)
                add(CompilerInstruction.Store("LastResult"))
                if (destination != null) add(CompilerInstruction.Store(destination))
                locals.forEach { add(CompilerInstruction.LoadLocal(it)) }
                add(CompilerInstruction.LoadLastResult)
                add(CompilerInstruction.RecordActionFrame(symbolicInvocation.dhatu.upadesha,
                    participants.map { it.first }, participants.map { it.third }))
            }
        }

        private fun rejectUnsupportedKarakaHistory(node: Invocation) {
            if (dev.panini.execution.binding.KarakaReferenceResolver.protectedPadas(node.vakya.padas).isNotEmpty()) {
                throw CompilerUnsupportedException(CompilerUnsupportedKind.INVOCATION, render(node),
                    "Kāraka history references require participant-frame IR lowering; literal operand lowering would change their meaning.")
            }
        }

        private fun historyLoad(history: dev.panini.execution.binding.NamedActionResultReference): CompilerInstruction {
            val identity = history.actionIdentity.takeIf { it.upasargas.isNotEmpty() || it.sanadi.isNotEmpty() }
            return history.ordinalFromOldest?.let {
                CompilerInstruction.LoadOrdinalActionResult(history.dhatuUpadesha, it, identity)
            } ?: CompilerInstruction.LoadActionResult(history.dhatuUpadesha, if (history.previous) 2 else 1, identity)
        }

        /** Keep ordered source morphology out of set-valued runtime operation features. */
        private fun withActionIdentity(code: List<CompilerInstruction>, node: Invocation,
            resolvedDhatu: dev.panini.dhatupatha.Dhatu? = null): List<CompilerInstruction> {
            val head = node.vakya.padas.filterIsInstance<TingantaPada>().singleOrNull() ?: return code
            fun identity(root: String): dev.panini.core.ActionIdentity {
                val candidates = dev.panini.dhatupatha.DhatuPatha.all.filter { it.upadesha == root }
                val lexical = requireNotNull(resolvedDhatu?.takeIf { it.upadesha == root }
                    ?: candidates.filter { it.operations.isNotEmpty() }.distinctBy { it.id }.singleOrNull()
                    ?: candidates.distinctBy { it.id }.singleOrNull())
                require(head.upasargaIdentities.all { it != null } && head.dhatu.sanadiAffixes.all { it != null })
                return dev.panini.core.ActionIdentity(lexical.id, head.upasargaIdentities.filterNotNull(),
                    head.dhatu.sanadiAffixes.filterNotNull())
            }
            return code.map { instruction -> when (instruction) {
                is CompilerInstruction.RecordActionFrame -> instruction.copy(actionIdentity = identity(instruction.dhatuUpadesha))
                is CompilerInstruction.RecordActionResult -> instruction.copy(actionIdentity = identity(instruction.dhatuUpadesha))
                is CompilerInstruction.CallParticipantGroup -> instruction.copy(actionIdentity = identity(instruction.dhatuUpadesha))
                else -> instruction
            } }
        }

        private fun lowerDirect(plan: ExecutionPlan): List<CompilerInstruction> {
            val participants = plan.resolved.context.bindings.entries
                .filter { it.key != dev.panini.core.Karaka.ANIRDHARITA }
                .flatMap { (role, expression) ->
                    CompilerIrLowering.lowerParticipantOperands(expression).map { role to it }
                }
            val locals = participants.indices.map { "action_input_${nextLabel++}_$it" }
            val objects = participants.indices.filter { participants[it].first == Karaka.KARMAN }
            val candidates = plan.resolved.invocation.dhatu.operations.filter {
                it.trigger.matches(plan.resolved.invocation.grammaticalFeatures)
            }
            fun staticNumeric(expression: ExecutionExpression): Boolean = when (expression) {
                is ExecutionExpression.Coordination -> expression.members.isNotEmpty() && expression.members.all(::staticNumeric)
                is ExecutionExpression.Pada -> expression.value is dev.panini.execution.SanskritValue.Sankhya &&
                    dev.panini.shiksha.Samjna.REFERENCE !in expression.samjnas
                is ExecutionExpression.TypedOperand -> expression.referenceName == null &&
                    expression.value is dev.panini.execution.SanskritValue.Sankhya
                is ExecutionExpression.Reference -> false
            }
            val dynamic = objects.isNotEmpty() && candidates.all { it.name in participantGroupOperations } &&
                candidates.any { it.action !is dev.panini.actions.numeric.NumericFoldDhatuAction } &&
                plan.resolved.context.bindings[Karaka.KARMAN]?.let(::staticNumeric) != true
            return buildList {
                participants.forEachIndexed { index, (_, instructions) ->
                    addAll(instructions)
                    add(CompilerInstruction.StoreLocal(locals[index]))
                }
                if (dynamic) {
                    objects.forEach { add(CompilerInstruction.LoadLocal(locals[it])) }
                    add(CompilerInstruction.PackOperands(List(objects.size) { false }, asGroup = true))
                    add(CompilerInstruction.CallParticipantGroup(plan.resolved.invocation.dhatu.upadesha,
                        plan.resolved.invocation.grammaticalFeatures, plan.resolved.invocation.selectedOperation,
                        plan.resolved.context.bindings.filterKeys { it !in setOf(Karaka.KARMAN, Karaka.ANIRDHARITA) }))
                    add(CompilerInstruction.Store("LastResult"))
                    return@buildList
                }
                addAll(CompilerIrLowering.lowerLeafValues(plan))
                locals.forEach { add(CompilerInstruction.LoadLocal(it)) }
                add(CompilerInstruction.LoadLastResult)
                val identity = plan.resolved.invocation.dhatu.upadesha
                add(CompilerInstruction.RecordActionFrame(identity, participants.map { it.first }))
            }
        }

        /** Lowers branches and their invocation/frame semantics into compiler IR. */
        private fun lowerConditionalIr(node: Conditional): List<CompilerInstruction>? {
            val truthTest = (node.condition as? Invocation)?.let(NaturalSemanticNormalizer::normalize)
                as? NaturalSemanticNormalizer.Operation.TruthTest
            val condition = if (truthTest != null) {
                listOf(
                    CompilerInstruction.Load(truthTest.stateName),
                    CompilerInstruction.Constant(dev.panini.execution.SanskritValue.Satya(!truthTest.negated)),
                    CompilerInstruction.Compare(ComparisonOperator.EQUAL),
                )
            } else (node.condition as? Invocation)?.let(::lowerResolvedCondition)
                ?: (lower(node.condition) + CompilerInstruction.LoadLastResult + CompilerInstruction.Booleanize)
            val consequent = lowerPrimitiveBranchIr(node.consequent) ?: throw CompilerUnsupportedException(
                CompilerUnsupportedKind.CONDITIONAL,
                render(node.consequent),
                "Cannot lower the consequent branch to compiler IR (${node.consequent::class.simpleName}).",
            )
            val alternate = node.alternate?.let { alternateNode ->
                lowerPrimitiveBranchIr(alternateNode) ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.CONDITIONAL,
                    render(alternateNode),
                    "Cannot lower the alternate branch to compiler IR (${alternateNode::class.simpleName}).",
                )
            } ?: emptyList()
            return CompilerIrLowering.lowerConditional(
                condition = condition,
                consequent = consequent,
                alternate = alternate,
                labelPrefix = "conditional_${nextLabel++}",
            )
        }

        private fun lowerResolvedCondition(node: Invocation): List<CompilerInstruction>? {
            when (val analysis = dev.panini.execution.CopularAgreement.analyze(node)) {
                dev.panini.execution.CopularAgreement.Analysis.Valid -> Unit
                is dev.panini.execution.CopularAgreement.Analysis.Invalid ->
                    throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.CONDITIONAL, render(node),
                        "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                        dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                    )
            }
            val anaphoric = resolveAnaphors(node)
            if (anaphoric != node) return lowerResolvedCondition(anaphoric)
            val projection = dev.panini.execution.AttributeOperandProjection.project(
                node, "__pvm_condition_field_${nextLabel++}_", registry.all().mapTo(mutableSetOf()) { it.nameStem })
            if (projection.operands.isNotEmpty()) {
                val condition = lowerResolvedCondition(projection.invocation) ?: return null
                return buildList {
                    projection.operands.forEach { operand ->
                        addAll(lowerFieldAccess(operand.access))
                        add(CompilerInstruction.Store(operand.name))
                    }
                    addAll(condition)
                }
            }
            (NaturalSemanticNormalizer.normalize(node) as? NaturalSemanticNormalizer.Operation.TruthTest)?.let { test ->
                return listOf(CompilerInstruction.Load(test.stateName), CompilerInstruction.Duplicate,
                    CompilerInstruction.Booleanize, CompilerInstruction.Pop,
                    CompilerInstruction.Constant(SanskritValue.Satya(!test.negated)),
                    CompilerInstruction.Compare(ComparisonOperator.EQUAL))
            }
            rejectUnsupportedKarakaHistory(node)
            val references = dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(node.vakya.padas)
            dev.panini.execution.binding.NamedActionResultReferenceResolver.agreementDiagnostic(references)?.let { diagnostic ->
                throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.CONDITIONAL,
                    render(node),
                    "${diagnostic.code}: ${diagnostic.message}",
                    dev.panini.execution.ExecutionDiagnostic.from(diagnostic),
                )
            }
            if (references.isNotEmpty()) {
                val (projected, loads) = projectNamedHistoryOperands(node, references)
                return loads + (lowerResolvedCondition(projected) ?: return null) + CompilerInstruction.ExitFrame
            }
            val plan = ResolvedLeafPlanner.planAny(node)
                ?.takeIf { dev.panini.shiksha.Samjna.SATYA in it.resolved.operation.resultSamjnas } ?: return null
            return CompilerIrLowering.lowerCondition(plan)
        }

        private fun lowerPrimitiveBranchIr(node: ProgramNode): List<CompilerInstruction>? = when (node) {
            is Invocation -> {
                lowerPriorAction(node)?.let { return it }
                if (node.implicitValuePada != null) {
                    lowerNominalResult(requireNotNull(resolveAnaphors(node).implicitValuePada))
                } else if (node.vakya.padas.none { it is TingantaPada }) {
                    val nominal = resolveAnaphors(node).vakya.padas.singleOrNull() as? SubantaPada ?: return null
                    lowerNominalResult(nominal)
                } else {
                    runCatching { lowerInvocation(node, exactSource = null) }.getOrNull()
                        ?: throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.INVOCATION,
                        normalized(render(node)),
                        "Cannot lower conditional invocation with padas " +
                            node.vakya.padas.joinToString { it::class.simpleName.orEmpty() },
                    )
                }
            }
            is Conditional -> lowerConditionalIr(node)
            is Recurrence -> lowerRecurrenceIr(node)
            is Sequence -> if (node.statements.all { it is Invocation } &&
                node.connectorKinds.all { it in setOf(
                    SequenceConnector.ANANTARYA,
                    SequenceConnector.PURVAKALA,
                    SequenceConnector.SAMUCCAYA,
                    SequenceConnector.VAKYANTARA,
                    SequenceConnector.TADANIM,
                ) }) {
                lowerSequence(node, exactSource = null)
            } else buildList {
                val exit = "branch_result_${nextLabel++}"
                for (statement in node.statements) {
                    addAll(lowerPrimitiveBranchIr(statement) ?: throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.CONDITIONAL,
                        render(statement),
                        "Cannot lower a sequence statement in a conditional branch (${statement::class.simpleName}).",
                    ))
                    add(CompilerInstruction.IsProcedureExitRequested)
                    add(CompilerInstruction.Branch(exit, whenTrue = true))
                }
                add(CompilerInstruction.Label(exit))
            }
            is PrakriyaNode -> buildList {
                for (statement in node.body) {
                    addAll(lowerPrimitiveBranchIr(statement) ?: return null)
                }
            }
            is Scope -> buildList {
                for (statement in node.body) {
                    addAll(lowerPrimitiveBranchIr(statement) ?: return null)
                }
            }
            is Repeat -> lowerRepeatIr(node)
            is WhileLoop -> lowerWhileIr(node)
            is dev.panini.vyakaranam.ast.ForEach -> lowerForEach(node)
            is dev.panini.vyakaranam.ast.CollectionRelativeAction -> lowerCollectionRelativeAction(node)
            is dev.panini.vyakaranam.ast.QuantifiedCollectionPredicate -> lowerQuantifiedCollectionPredicate(node)
            is Pipeline, is Quotation -> null
        }

        private fun lowerPriorAction(node: Invocation): List<CompilerInstruction>? = when (
            val analysis = dev.panini.execution.PriorActionLowering.analyze(node)
        ) {
            dev.panini.execution.PriorActionLowering.Analysis.NotPriorAction -> null
            is dev.panini.execution.PriorActionLowering.Analysis.Resolved ->
                lowerSequence(analysis.sequence, null)
            is dev.panini.execution.PriorActionLowering.Analysis.Invalid ->
                throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.INVOCATION, render(node),
                    "${analysis.diagnostic.code}: ${analysis.diagnostic.message}",
                    dev.panini.execution.ExecutionDiagnostic.from(analysis.diagnostic),
                )
        }

        private fun lowerRecurrenceIr(node: Recurrence): List<CompilerInstruction> {
            val invocation = node.body.depthFirst().filterIsInstance<Invocation>().firstOrNull()
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.REPETITION, render(node),
                    "पुनः requires an executable action.",
                )
            val plan = ResolvedLeafPlanner.planAny(invocation)
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.REPETITION, render(node),
                    "Cannot resolve the action governed by पुनः.",
                )
            val head = invocation.vakya.padas.filterIsInstance<TingantaPada>().single()
            require(head.upasargaIdentities.all { it != null } && head.dhatu.sanadiAffixes.all { it != null }) {
                "पुनः requires a fully resolved action identity."
            }
            val dhatu = plan.resolved.invocation.dhatu
            val identity = dev.panini.core.ActionIdentity(
                dhatu.id,
                head.upasargaIdentities.filterNotNull(),
                head.dhatu.sanadiAffixes.filterNotNull(),
            )
            return listOf(
                CompilerInstruction.LoadActionResult(dhatu.upadesha, actionIdentity = identity),
                CompilerInstruction.Pop,
            ) + lower(node.body)
        }

        private fun lowerWhileIr(node: WhileLoop): List<CompilerInstruction>? {
            val hasNamedResult = dev.panini.execution.binding.NamedActionResultReferenceResolver
                .resolve(node.condition.vakya.padas).isNotEmpty()
            val usesLatestResult = !hasNamedResult && node.condition.vakya.padas.any { pada ->
                pada is dev.panini.vyakaranam.ast.SubantaPada &&
                    NaturalSemanticNormalizer.isPriorResult(pada)
            }
            val normalizedTruth = NaturalSemanticNormalizer.normalize(node.condition)
                as? NaturalSemanticNormalizer.Operation.TruthTest
            val reportedOutcome = NaturalSemanticNormalizer.normalize(node.condition)
                as? NaturalSemanticNormalizer.Operation.ReportedOutcomeTest
            val isNegated = normalizedTruth?.negated ?: node.condition.vakya.padas.any { pada ->
                pada is AvyayaPada && pada.function == AvyayaFunction.NISHEDHA
            }
            val condition = if (usesLatestResult) null else {
                if (reportedOutcome != null) {
                    listOf(
                        CompilerInstruction.LoadLastResult,
                        CompilerInstruction.Constant(
                            dev.panini.execution.SanskritValue.Shabda(reportedOutcome.outcomeName),
                        ),
                        CompilerInstruction.Compare(ComparisonOperator.EQUAL),
                    )
                } else {
                    lowerResolvedCondition(node.condition)
                        ?: normalizedTruth?.let { truth ->
                            listOf(
                                CompilerInstruction.Load(truth.stateName),
                                CompilerInstruction.Constant(dev.panini.execution.SanskritValue.Satya(!truth.negated)),
                                CompilerInstruction.Compare(ComparisonOperator.EQUAL),
                            )
                        }
                        ?: runCatching { lower(node.condition) }
                            .getOrNull()
                            ?.plus(
                                listOf(
                                    CompilerInstruction.LoadLastResult,
                                    CompilerInstruction.Booleanize,
                                ),
                            )
                        ?: return null
                }
            }
            val body = withinLoop { lower(node.body) }
            val exhausted = node.exhausted?.let { runCatching { lower(it) }.getOrNull() } ?: emptyList()
            if (node.exhausted != null && exhausted.isEmpty()) return null
            val resultTarget = node.resultTarget?.let { target ->
                val plan = (target as? Invocation)?.let { invocation ->
                    ResolvedLeafPlanner.planAny(
                        invocation,
                        injectedBindings = mapOf(
                            Karaka.KARMAN to ExecutionExpression.Reference("चक्रफल"),
                        ),
                    ) ?: ResolvedLeafPlanner.planAny(invocation)
                } ?: ResolvedLeafPlanner.plansAny(target)?.singleOrNull()
                    ?: return null
                CompilerIrLowering.lowerLoopTarget(plan)
            } ?: emptyList()
            val maximumIterations = node.maximumIterationStems.takeIf(List<String>::isNotEmpty)?.let {
                dev.panini.sankhya.SankhyaEvaluator().evaluateStems(it).value
            }
            return CompilerIrLowering.lowerWhileInstructions(
                condition = condition,
                body = body,
                maximumIterations = maximumIterations,
                exhausted = exhausted,
                resultTarget = resultTarget,
                usesReportedCondition = usesLatestResult,
                negatedReportedCondition = reportedOutcome?.negated ?: isNegated,
                namePrefix = "while_${nextLabel++}",
            )
        }

        private fun lowerNominalResult(pada: dev.panini.vyakaranam.ast.Pada): List<CompilerInstruction> {
            val operand = dev.panini.execution.NominalResultSemantics.operand(pada)
            return listOf(
                lowerSemanticOperand(operand.semantic, operand.fallback),
                CompilerInstruction.Store("LastResult"),
            )
        }

        private fun lowerForEach(node: dev.panini.vyakaranam.ast.ForEach): List<CompilerInstruction> {
            val prefix = "foreach_${nextLabel++}"
            val whole = resolveAnaphors(Invocation(dev.panini.vyakaranam.ast.NamaVakya(
                node.collection.sourceText, listOf(node.collection))))
            val source = (whole.vakya.padas.single() as SubantaPada).pratipadika.referenceKey()
            val member = node.member.pratipadika.referenceKey()
            val indexName = node.index?.pratipadika?.referenceKey()
            val previousSelection = node.relativeSelections.singleOrNull {
                it.direction == dev.panini.vyakaranam.ast.CollectionRelativeDirection.PREVIOUS
            }
            val nextSelection = node.relativeSelections.singleOrNull {
                it.direction == dev.panini.vyakaranam.ast.CollectionRelativeDirection.NEXT
            }
            val previousName = previousSelection?.binding?.pratipadika?.referenceKey()
            val nextName = nextSelection?.binding?.pratipadika?.referenceKey()
            val previousIsPlural = previousSelection?.cardinality ==
                dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEMS
            val nextIsPlural = nextSelection?.cardinality ==
                dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEMS
            val previousReferents = referents
            val loopNames = setOfNotNull(member, indexName, previousName, nextName)
            referents = referents.filterNot { it.identity in loopNames } +
                dev.panini.execution.AgreementCandidate(
                    member, dev.panini.execution.ReferentAgreement.fromDeclaration(node.member)) +
                listOfNotNull(node.index?.let {
                    dev.panini.execution.AgreementCandidate(indexName!!,
                        dev.panini.execution.ReferentAgreement.fromDeclaration(it))
                }, previousSelection?.binding?.let {
                    dev.panini.execution.AgreementCandidate(previousName!!,
                        dev.panini.execution.ReferentAgreement.fromDeclaration(it))
                }, nextSelection?.binding?.let {
                    dev.panini.execution.AgreementCandidate(nextName!!,
                        dev.panini.execution.ReferentAgreement.fromDeclaration(it))
                })
            val body = try { withinLoop { lower(node.body) } } finally { referents = previousReferents }
            fun number(value: Long) = CompilerInstruction.Constant(dev.panini.execution.SanskritValue.Sankhya(
                value, dev.panini.execution.renderSankhyaResult(value) ?: value.toString()))
            return buildList {
                add(CompilerInstruction.Load(source))
                add(CompilerInstruction.Collection(CollectionOperator.SNAPSHOT))
                add(CompilerInstruction.StoreLocal("${prefix}_list"))
                add(number(if (previousName == null || previousIsPlural) 1 else 2))
                add(CompilerInstruction.StoreLocal("${prefix}_index"))
                add(CompilerInstruction.Label("${prefix}_check"))
                add(CompilerInstruction.LoadLocal("${prefix}_index"))
                add(CompilerInstruction.LoadLocal("${prefix}_list"))
                add(CompilerInstruction.Collection(CollectionOperator.LENGTH))
                add(CompilerInstruction.Compare(ComparisonOperator.LESS_THAN_OR_EQUAL))
                add(CompilerInstruction.Branch("${prefix}_done"))
                if (nextName != null && !nextIsPlural) {
                    add(CompilerInstruction.LoadLocal("${prefix}_index"))
                    add(CompilerInstruction.LoadLocal("${prefix}_list"))
                    add(CompilerInstruction.Collection(CollectionOperator.LENGTH))
                    add(CompilerInstruction.Compare(ComparisonOperator.LESS_THAN))
                    add(CompilerInstruction.Branch("${prefix}_done"))
                }
                add(CompilerInstruction.LoadLocal("${prefix}_index"))
                add(CompilerInstruction.CheckCollectionIteration)
                add(CompilerInstruction.LoadLocal("${prefix}_list"))
                add(CompilerInstruction.LoadLocal("${prefix}_index"))
                add(CompilerInstruction.Collection(CollectionOperator.INDEX))
                if (indexName != null) add(CompilerInstruction.LoadLocal("${prefix}_index"))
                if (previousName != null) {
                    add(CompilerInstruction.LoadLocal("${prefix}_list"))
                    if (previousIsPlural) {
                        add(number(1)); add(CompilerInstruction.LoadLocal("${prefix}_index")); add(number(1))
                        add(CompilerInstruction.Arithmetic(ArithmeticOperator.SUBTRACT))
                        add(CompilerInstruction.Collection(CollectionOperator.SLICE))
                    } else {
                        add(CompilerInstruction.LoadLocal("${prefix}_index")); add(number(1))
                        add(CompilerInstruction.Arithmetic(ArithmeticOperator.SUBTRACT))
                        add(CompilerInstruction.Collection(CollectionOperator.INDEX))
                    }
                }
                if (nextName != null) {
                    add(CompilerInstruction.LoadLocal("${prefix}_list")); add(CompilerInstruction.LoadLocal("${prefix}_index"))
                    add(number(1)); add(CompilerInstruction.Arithmetic(ArithmeticOperator.ADD))
                    if (nextIsPlural) {
                        add(CompilerInstruction.LoadLocal("${prefix}_list"));
                        add(CompilerInstruction.Collection(CollectionOperator.LENGTH))
                        add(CompilerInstruction.Collection(CollectionOperator.SLICE))
                    } else add(CompilerInstruction.Collection(CollectionOperator.INDEX))
                }
                add(CompilerInstruction.EnterFrame(
                    listOfNotNull(member, indexName, previousName, nextName), scopedBinding = true))
                addAll(body)
                add(CompilerInstruction.ExitFrame)
                add(CompilerInstruction.IsProcedureReturnRequested)
                add(CompilerInstruction.Branch("${prefix}_done", whenTrue = true))
                add(CompilerInstruction.ConsumeBreak)
                add(CompilerInstruction.Branch("${prefix}_done", whenTrue = true))
                add(CompilerInstruction.LoadLocal("${prefix}_index"))
                add(number(1))
                add(CompilerInstruction.Arithmetic(ArithmeticOperator.ADD))
                add(CompilerInstruction.StoreLocal("${prefix}_index"))
                add(CompilerInstruction.Jump("${prefix}_check"))
                add(CompilerInstruction.Label("${prefix}_done"))
            }
        }

        /**
         * Projects the typed relative operand onto private IR bindings. These names never enter
         * the parsed/source AST and exist only long enough for the ordinary collection-loop IR.
         */
        private fun lowerCollectionRelativeAction(
            node: dev.panini.vyakaranam.ast.CollectionRelativeAction,
        ): List<CompilerInstruction> {
            val suffix = nextLabel++
            fun privatePada(template: SubantaPada, name: String) = template.copy(
                sourceText = "$name + अम्",
                pratipadika = MulaPratipadika(name, name),
                sup = dev.panini.vyakaranam.ast.SupPratyaya("अम्", "अम्"),
            ).inheritLocation(template)
            val current = privatePada(node.member, "__relative_current_$suffix")
            val relative = privatePada(node.operand.memberType, "__relative_value_$suffix")
            val action = Invocation(AkhyataVakya(
                node.action.sourceText,
                listOf(current, relative, node.action),
                node.action,
            ))
            val body = node.continuation?.let {
                Sequence(node.sourceText, listOf(action, it), listOf(SequenceConnector.ANANTARYA.surface))
            } ?: action
            val outerBody: ProgramNode
            val selectionBinding: SubantaPada
            if (node.operand.cardinality == dev.panini.vyakaranam.ast.CollectionRelativeCardinality.ITEMS) {
                val relatives = privatePada(node.operand.memberType, "__relative_values_$suffix")
                selectionBinding = relatives
                outerBody = dev.panini.vyakaranam.ast.ForEach(
                    sourceText = node.sourceText,
                    collection = relatives,
                    member = relative,
                    body = body,
                )
            } else {
                selectionBinding = relative
                outerBody = body
            }
            return lowerForEach(dev.panini.vyakaranam.ast.ForEach(
                sourceText = node.sourceText,
                collection = node.collection,
                member = current,
                body = outerBody,
                quantifier = node.quantifier,
                relativeSelections = listOf(dev.panini.vyakaranam.ast.CollectionRelativeSelection(
                    node.operand.direction,
                    node.operand.cardinality,
                    selectionBinding,
                    node.operand.relative,
                )),
            ))
        }

        private fun lowerQuantifiedCollectionPredicate(
            node: dev.panini.vyakaranam.ast.QuantifiedCollectionPredicate,
        ): List<CompilerInstruction> {
            val prefix = "quantifier_${nextLabel++}"
            val whole = resolveAnaphors(Invocation(dev.panini.vyakaranam.ast.NamaVakya(
                node.collection.sourceText,
                listOf(node.collection),
            )))
            val source = (whole.vakya.padas.single() as SubantaPada).pratipadika.referenceKey()
            val member = node.member.pratipadika.referenceKey()
            val previousReferents = referents
            referents = referents.filterNot { it.identity == member } + dev.panini.execution.AgreementCandidate(
                member,
                dev.panini.execution.ReferentAgreement.fromDeclaration(node.member),
            )
            val predicate = try {
                lowerResolvedCondition(node.predicate)
                    ?: throw CompilerUnsupportedException(
                        CompilerUnsupportedKind.CONDITIONAL,
                        render(node.predicate),
                        "A quantified member predicate must produce a truth value.",
                    )
            } finally {
                referents = previousReferents
            }
            val isAny = node.quantifier == dev.panini.vyakaranam.ast.CollectionQuantifier.ANY
            val exhaustedValue = SanskritValue.Satya(!isAny)
            val decisiveValue = SanskritValue.Satya(isAny)
            fun number(value: Long) = CompilerInstruction.Constant(SanskritValue.Sankhya(value, value.toString()))
            return buildList {
                add(CompilerInstruction.Load(source))
                add(CompilerInstruction.Collection(CollectionOperator.QUANTIFIER_SNAPSHOT))
                add(CompilerInstruction.StoreLocal("${prefix}_collection"))
                add(number(1))
                add(CompilerInstruction.StoreLocal("${prefix}_index"))
                add(CompilerInstruction.Label("${prefix}_check"))
                add(CompilerInstruction.LoadLocal("${prefix}_index"))
                add(CompilerInstruction.LoadLocal("${prefix}_collection"))
                add(CompilerInstruction.Collection(CollectionOperator.LENGTH))
                add(CompilerInstruction.Compare(ComparisonOperator.LESS_THAN_OR_EQUAL))
                add(CompilerInstruction.Branch("${prefix}_exhausted"))
                add(CompilerInstruction.LoadLocal("${prefix}_index"))
                add(CompilerInstruction.CheckCollectionIteration)
                add(CompilerInstruction.LoadLocal("${prefix}_collection"))
                add(CompilerInstruction.LoadLocal("${prefix}_index"))
                add(CompilerInstruction.Collection(CollectionOperator.INDEX))
                add(CompilerInstruction.EnterFrame(listOf(member), scopedBinding = true))
                addAll(predicate)
                add(CompilerInstruction.ExitFrame)
                add(CompilerInstruction.Branch("${prefix}_continue", whenTrue = !isAny))
                add(CompilerInstruction.Constant(decisiveValue))
                add(CompilerInstruction.Store("LastResult"))
                add(CompilerInstruction.Jump("${prefix}_done"))
                add(CompilerInstruction.Label("${prefix}_continue"))
                add(CompilerInstruction.LoadLocal("${prefix}_index"))
                add(number(1))
                add(CompilerInstruction.Arithmetic(ArithmeticOperator.ADD))
                add(CompilerInstruction.StoreLocal("${prefix}_index"))
                add(CompilerInstruction.Jump("${prefix}_check"))
                add(CompilerInstruction.Label("${prefix}_exhausted"))
                add(CompilerInstruction.Constant(exhaustedValue))
                add(CompilerInstruction.Store("LastResult"))
                add(CompilerInstruction.Label("${prefix}_done"))
            }
        }

        private fun lowerRepeatIr(node: Repeat): List<CompilerInstruction>? {
            val body = node.body
            val bodyInstructions = runCatching { withinLoop { lower(body) } }.getOrNull() ?: return null
            return CompilerIrLowering.lowerRepeat(
                count = node.count,
                body = bodyInstructions,
                namePrefix = "repeat_${nextLabel++}",
            )
        }

        private fun lowerPlannedNode(node: ProgramNode): List<CompilerInstruction> {
            val plans = ResolvedLeafPlanner.plansAny(node)
                ?: throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.PIPELINE,
                    render(node),
                    "Cannot resolve parsed compound node as compiler leaves.",
                )
            val invocations = node.expandedInvocations()
            require(plans.size == invocations.size) { "Planned actions must retain their source morphology." }
            return plans.zip(invocations).flatMap { (plan, invocation) ->
                withActionIdentity(lowerDirect(plan), invocation, plan.resolved.invocation.dhatu)
            }
        }

        private fun resolveArguments(
            invocation: dev.panini.execution.PrakriyaInvocation,
        ): List<ResolvedPrakriyaArgument> {
            fun semanticFailure(code: String, message: String, argumentIndex: Int? = null): Nothing {
                val diagnostic = dev.panini.execution.PrakriyaCallDiagnostics.atInvocation(
                    invocation, code, message, argumentIndex)
                throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.PROCEDURE,
                    invocation.fullText,
                    "${diagnostic.code}: ${diagnostic.message}",
                    dev.panini.execution.ExecutionDiagnostic.from(diagnostic),
                )
            }
            val signature = invocation.kriya.signature
            val resolution = PrakriyaInvocationArgumentResolver.resolve(invocation,
                registry.all().mapTo(mutableSetOf()) { it.nameStem })
            val resolvedArguments = when (resolution) {
                is PrakriyaArgumentResolution.Success -> resolution.arguments
                is PrakriyaArgumentResolution.Failure -> throw CompilerUnsupportedException(
                    CompilerUnsupportedKind.PROCEDURE,
                    invocation.fullText,
                    "${resolution.diagnostic.code}: ${resolution.message}",
                    dev.panini.execution.ExecutionDiagnostic.from(resolution.diagnostic),
                )
            }
            val arguments = resolvedArguments.map { it.argument.term }
            val acceptsCollection = signature.parameters.singleOrNull()?.type == PrakriyaValueType.SUCHI
            if (signature.parameters.size != arguments.size && signature.parameters.isNotEmpty()) {
                semanticFailure(
                    "PROCEDURE_ARITY_MISMATCH",
                    dev.panini.execution.PrakriyaDiagnostics.arity(
                    invocation.kriya.nameStem,
                    signature.parameters.size,
                    arguments.size,
                    ),
                )
            }
            if (signature.hasTypedParticipants) {
                val actual = resolvedArguments.map { resolved ->
                    resolved.argument.value?.let(PrakriyaValueClassifier::classifyValue)
                        ?: resolved.argument.pada?.let(PrakriyaValueClassifier::classifyStaticPada)
                        ?: PrakriyaValueType.UNCONSTRAINED
                }
                if (!dev.panini.execution.TypedParticipantBinding.acceptsTypes(signature.parameters.map { it.type }, actual)) {
                    semanticFailure(
                        "PROCEDURE_PARTICIPANT_TYPE_MISMATCH",
                        "Procedure typed participant mismatch: expected ${signature.parameters.map { it.type }}, received $actual.",
                    )
                }
            }
            signature.parameters.zip(arguments).takeUnless { acceptsCollection || signature.hasTypedParticipants }.orEmpty()
                .forEachIndexed { index, (parameter, _) ->
                val resolved = resolvedArguments[index]
                val suppliedValue = resolved.argument.value
                // An unresolved nominal can name a runtime collection or record;
                // its spelling is not evidence that the argument has word type.
                // ResolveArgument and EnterFrame enforce the actual value type.
                val actual = suppliedValue?.let(PrakriyaValueClassifier::classifyValue)
                    ?: resolved.argument.pada?.let(PrakriyaValueClassifier::classifyStaticPada)
                        ?.takeUnless { it == PrakriyaValueType.UNCONSTRAINED }
                if (parameter.type != PrakriyaValueType.UNCONSTRAINED &&
                    !resolvedArguments[index].isPriorResult && actual != null && actual != parameter.type) {
                    semanticFailure(
                        "PROCEDURE_ARGUMENT_TYPE_MISMATCH",
                        dev.panini.execution.PrakriyaDiagnostics.parameterType(parameter),
                        index,
                    )
                }
            }
            invocation.kriya.nishedhaGuards.forEach { guard ->
                if (NishedhaGuardEvaluator.numericProhibition(guard, signature.parameters,
                    resolvedArguments.size) == null &&
                    dev.panini.execution.PrakriyaSignatureCompiler.inferGuardType(guard) == null) {
                    semanticFailure(
                        "UNSUPPORTED_PROCEDURE_PROHIBITION",
                        "Unsupported procedure prohibition: '${guard.text.trim()}'",
                    )
                }
                val prohibited = NishedhaGuardEvaluator.isProhibitedResolved(
                    guard,
                    signature.parameters,
                    resolvedArguments,
                    resolvedArguments.map { it.argument.value },
                )
                val requiredType = signature.argumentType
                val typeViolated = requiredType != null &&
                    resolvedArguments.withIndex().any { (index, resolved) ->
                        val actual = resolved.argument.value?.let(PrakriyaValueClassifier::classifyValue)
                            ?: resolved.argument.pada?.let(PrakriyaValueClassifier::classifyStaticPada)
                            ?: PrakriyaValueType.UNCONSTRAINED
                        if (actual == PrakriyaValueType.UNCONSTRAINED) {
                            // Unknown referents are safe only when frame entry checks this guard's type.
                            signature.parameters.getOrNull(index)?.type != requiredType
                        } else actual != requiredType
                    }
                if (prohibited || typeViolated) {
                    semanticFailure(
                        "PROCEDURE_PROHIBITION_TRIGGERED",
                        "निषेध-प्रतिषेधः: Prohibition triggered by '${guard.text.trim()}'",
                    )
                }
            }
            return resolvedArguments
        }
    }

    private fun render(node: ProgramNode): String = when (node) {
        is Invocation -> node.vakya.padas.joinToString(" ") { it.sourceText }
        is Sequence -> node.statements.mapIndexed { index, statement ->
            val connector = if (index == 0) "" else "${node.connectors.getOrNull(index - 1) ?: "।"} "
            connector + render(statement)
        }.joinToString(" ")
        else -> node.sourceText
    }

    private fun normalized(source: String): String =
        source.trim().trimEnd('।', '॥').trim() + " ।"

}

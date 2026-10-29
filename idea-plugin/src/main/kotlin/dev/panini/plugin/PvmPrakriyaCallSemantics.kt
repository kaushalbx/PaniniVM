package dev.panini.plugin

import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import dev.panini.execution.PrakriyaArgumentResolution
import dev.panini.execution.PrakriyaArgumentBindingKind
import dev.panini.execution.PrakriyaAstArgumentBinder
import dev.panini.execution.PrakriyaInvocationArgumentResolver
import dev.panini.execution.PrakriyaParameter
import dev.panini.execution.PrakriyaParameterDeclarations
import dev.panini.execution.PrakriyaSignatureCompiler
import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.execution.ResolvedPrakriyaArgument
import dev.panini.vyakaranam.ast.SourceLocatedNode
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.DocumentSourceSpan

/** Bridges structural PSI to canonical runtime call resolution without a plugin-side call model. */
internal object PvmPrakriyaCallSemantics {
    private val BINDINGS_KEY = Key.create<CachedValue<List<ArgumentBinding>>>(
        "dev.panini.plugin.prakriyaArgumentBindings",
    )
    private val BODY_REFERENCES_KEY = Key.create<CachedValue<List<BodyParameterReference>>>(
        "dev.panini.plugin.prakriyaBodyParameterReferences",
    )

    private data class ArgumentBinding(
        val span: DocumentSourceSpan,
        val resolved: ResolvedPrakriyaArgument,
        val declaration: Pair<PvmFile, SubantaPada>?,
    )

    private data class BodyParameterReference(
        val span: DocumentSourceSpan,
        val parameter: PrakriyaParameter,
        val declaration: SubantaPada,
    )

    fun resolvedArgumentAt(file: PvmFile, offset: Int): ResolvedPrakriyaArgument? =
        bindings(file).firstOrNull { offset >= it.span.start && offset < it.span.endExclusive }?.resolved

    fun resolvedParameterReferenceAt(file: PvmFile, offset: Int): ResolvedPrakriyaArgument? =
        bindings(file).firstOrNull { binding ->
            val referenceSpan = if (binding.resolved.bindingKind == PrakriyaArgumentBindingKind.NAMED) {
                (binding.resolved.parameterPada as? SourceLocatedNode)?.sourceSpan
            } else {
                binding.span
            } ?: return@firstOrNull false
            offset >= referenceSpan.start && offset < referenceSpan.endExclusive
        }?.resolved

    fun namedParameterReferenceSpans(file: PvmFile): List<DocumentSourceSpan> = bindings(file).mapNotNull {
        if (it.resolved.bindingKind != PrakriyaArgumentBindingKind.NAMED) return@mapNotNull null
        (it.resolved.parameterPada as? SourceLocatedNode)?.sourceSpan
    }

    fun bodyParameterReferenceSpans(file: PvmFile): List<DocumentSourceSpan> =
        bodyReferences(file).map { it.span }

    fun bodyParameterAt(file: PvmFile, offset: Int): PrakriyaParameter? =
        bodyReferences(file).firstOrNull { offset in it.span.start until it.span.endExclusive }?.parameter

    /** The canonical declaration pada and its owning source file for an argument under the caret. */
    fun parameterDeclarationAt(file: PvmFile, offset: Int): Pair<PvmFile, SubantaPada>? =
        bindings(file).firstOrNull { binding ->
            val referenceSpan = if (binding.resolved.bindingKind == PrakriyaArgumentBindingKind.NAMED) {
                (binding.resolved.parameterPada as? SourceLocatedNode)?.sourceSpan
            } else {
                binding.span
            } ?: return@firstOrNull false
            offset >= referenceSpan.start && offset < referenceSpan.endExclusive
        }?.declaration ?: bodyReferences(file).firstOrNull {
            offset in it.span.start until it.span.endExclusive
        }?.let { file to it.declaration }

    /** Canonical parameter declaration at a declaration-side editor offset. */
    fun declaredParameterAt(file: PvmFile, offset: Int): PrakriyaParameter? {
        val document = file.canonicalDocument() ?: return null
        for (definition in PvmScript.fromDocument(document).filterIsInstance<PvmScriptStatement.PrakriyaDefinition>()) {
            val signature = runCatching {
                PrakriyaSignatureCompiler.compile(
                    definition.body,
                    definition.prakriya.declaredResult,
                    definition.prakriya.resultPredicate,
                )
            }.getOrNull() ?: continue
            val declarations = PrakriyaParameterDeclarations.resolve(signature.parameters, definition.body)
            for (parameter in signature.parameters) {
                val pada = declarations[parameter.nameStem] ?: continue
                val span = (pada as SourceLocatedNode).sourceSpan ?: continue
                if (offset >= span.start && offset < span.endExclusive) return parameter
            }
        }
        return null
    }

    private fun bindings(file: PvmFile): List<ArgumentBinding> {
        val cached = synchronized(file) {
            file.getUserData(BINDINGS_KEY) ?: CachedValuesManager.getManager(file.project)
                .createCachedValue({
                    CachedValueProvider.Result.create(
                        computeBindings(file),
                        PsiModificationTracker.getInstance(file.project).forLanguage(PvmLanguage.INSTANCE),
                        ProjectRootModificationTracker.getInstance(file.project),
                    )
                }, false)
                .also { file.putUserData(BINDINGS_KEY, it) }
        }
        return cached.value
    }

    private fun bodyReferences(file: PvmFile): List<BodyParameterReference> {
        val cached = synchronized(file) {
            file.getUserData(BODY_REFERENCES_KEY) ?: CachedValuesManager.getManager(file.project)
                .createCachedValue({
                    CachedValueProvider.Result.create(
                        computeBodyReferences(file),
                        PsiModificationTracker.getInstance(file.project).forLanguage(PvmLanguage.INSTANCE),
                        ProjectRootModificationTracker.getInstance(file.project),
                    )
                }, false)
                .also { file.putUserData(BODY_REFERENCES_KEY, it) }
        }
        return cached.value
    }

    private fun computeBodyReferences(file: PvmFile): List<BodyParameterReference> {
        val document = file.canonicalDocument() ?: return emptyList()
        val knownOperations = PvmProjectFileIndex.knownOperationStems(file)
        return PvmScript.fromDocument(document)
            .filterIsInstance<PvmScriptStatement.PrakriyaDefinition>()
            .flatMap { definition ->
                val signature = runCatching {
                    PrakriyaSignatureCompiler.compile(
                        definition.body,
                        definition.prakriya.declaredResult,
                        definition.prakriya.resultPredicate,
                    )
                }.getOrNull() ?: return@flatMap emptyList()
                val declarations = PrakriyaParameterDeclarations.resolve(signature.parameters, definition.body)
                PrakriyaAstArgumentBinder.parameterReferences(
                    definition.body,
                    signature.parameters,
                    knownOperations,
                ).mapNotNull { reference ->
                    val span = (reference.pada as SourceLocatedNode).sourceSpan ?: return@mapNotNull null
                    val declaration = declarations[reference.parameter.nameStem] ?: return@mapNotNull null
                    BodyParameterReference(span, reference.parameter, declaration)
                }
            }
    }

    private fun computeBindings(file: PvmFile): List<ArgumentBinding> {
        val document = file.canonicalDocument() ?: return emptyList()
        val registry = PvmProjectFileIndex.registry(file)
        val knownOperations = registry.all().mapTo(linkedSetOf()) { it.nameStem }
        val projectFiles = PvmProjectFileIndex.documents(file).associateBy { it.first.virtualFile?.url }
        val sentences = PvmScript.fromDocument(document).flatMap { statement ->
            when (statement) {
                is PvmScriptStatement.Sentence -> listOf(statement)
                is PvmScriptStatement.PrakriyaDefinition -> statement.body
                else -> emptyList()
            }
        }
        return buildList {
          for (sentence in sentences) {
            val ukti = sentence.ukti ?: continue
            val invocation = runCatching {
                registry.detectInvocation(ukti, file.virtualFile?.url, staticAnalysis = true)
            }.getOrNull() ?: continue
            val resolution = PrakriyaInvocationArgumentResolver.resolve(invocation, knownOperations)
                as? PrakriyaArgumentResolution.Success ?: continue
            val declarations = PrakriyaParameterDeclarations.resolve(
                invocation.kriya.signature.parameters,
                invocation.kriya.body,
            )
            val owner = projectFiles[invocation.kriya.sourceFile]?.first
            resolution.arguments.forEach { resolved ->
                val span = (resolved.argument.pada as? SourceLocatedNode)?.sourceSpan
                    ?: return@forEach
                val declaration = resolved.parameter?.nameStem?.let(declarations::get)
                add(ArgumentBinding(span, resolved, if (owner != null && declaration != null) {
                    owner to declaration
                } else null))
            }
          }
        }
    }
}

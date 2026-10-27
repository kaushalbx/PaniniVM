package dev.panini.plugin

import com.intellij.lang.parameterInfo.CreateParameterInfoContext
import com.intellij.lang.parameterInfo.ParameterInfoHandler
import com.intellij.lang.parameterInfo.ParameterInfoUIContext
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext
import com.intellij.psi.PsiElement
import dev.panini.execution.PrakriyaSignature
import dev.panini.execution.PrakriyaSignatureCompiler
import dev.panini.execution.PvmScript
import dev.panini.execution.PvmScriptStatement
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.PrakriyaVisibility

/** Native Ctrl+P support backed by runtime [PrakriyaSignature] values. */
class PvmParameterInfoHandler : ParameterInfoHandler<PsiElement, PrakriyaSignature> {
    override fun findElementForParameterInfo(context: CreateParameterInfoContext): PsiElement? {
        val file = context.file as? PvmFile ?: return null
        val owner = ownerAt(file, context.offset) ?: return null
        val signatures = signaturesAt(file, context.offset)
        if (signatures.isEmpty()) return null
        context.itemsToShow = signatures.toTypedArray()
        return owner
    }

    override fun showParameterInfo(element: PsiElement, context: CreateParameterInfoContext) {
        context.showHint(element, element.textRange.startOffset, this)
    }

    override fun findElementForUpdatingParameterInfo(context: UpdateParameterInfoContext): PsiElement? {
        val file = context.file as? PvmFile ?: return null
        val owner = ownerAt(file, context.offset) ?: return null
        return owner.takeIf { signaturesAt(file, context.offset).isNotEmpty() }
    }

    override fun updateParameterInfo(parameterOwner: PsiElement, context: UpdateParameterInfoContext) {
        val file = parameterOwner.containingFile as? PvmFile ?: return
        context.setCurrentParameter(callContextAt(file, context.offset)?.second ?: -1)
    }

    override fun updateUI(signature: PrakriyaSignature, context: ParameterInfoUIContext) {
        val presentation = renderSignature(signature)
        val highlight = parameterHighlight(signature, context.currentParameterIndex)
        context.setupUIComponentPresentation(
            presentation,
            highlight?.first ?: -1,
            highlight?.last?.plus(1) ?: -1,
            false,
            false,
            false,
            context.defaultParameterColor,
        )
    }

    internal fun signaturesAt(file: PvmFile, offset: Int): List<PrakriyaSignature> {
        val current = file.canonicalDocument() ?: return emptyList()
        val knownNames = PvmProjectFileIndex.knownOperationStems(file)
        val identity = PvmSymbolResolver.prakriyaCallContextAt(current, offset, knownNames)?.first
            ?: return emptyList()
        val documents = PvmProjectFileIndex.documentsForIdentity(file, identity)

        return buildList {
            documents.forEach { (sourceFile, document) ->
                var activeDomain: String? = null
                PvmScript.fromDocument(document).forEach statementLoop@{ statement ->
                    if (statement is PvmScriptStatement.AdhikaraDefinition) {
                        activeDomain = statement.scope.domainIdentity
                        return@statementLoop
                    }
                    val definition = statement as? PvmScriptStatement.PrakriyaDefinition
                        ?: return@statementLoop
                    val prakriya = definition.prakriya
                    val domain = prakriya.domainIdentity ?: activeDomain
                    if (prakriya.nameIdentity != identity.first || domain != identity.second) {
                        return@statementLoop
                    }
                    if (prakriya.modifiers.visibility == PrakriyaVisibility.INTERNAL && sourceFile != file) {
                        return@statementLoop
                    }
                    runCatching {
                        PrakriyaSignatureCompiler.compile(
                            definition.body,
                            prakriya.declaredResult,
                            prakriya.resultPredicate,
                        )
                    }.getOrNull()?.let(::add)
                }
            }
        }.distinct()
    }

    internal fun renderSignature(signature: PrakriyaSignature): String {
        val parameters = signature.parameters.joinToString(", ") {
            "${it.nameStem}: ${it.type.sanskritName}"
        }.ifBlank {
            signature.argumentType?.let { type -> "मानम्: ${type.sanskritName}" } ?: "मानरहितम्"
        }
        val result = signature.resultType?.sanskritName ?: signature.resultSchema ?: "अनिर्दिष्टफलम्"
        return "$parameters → $result"
    }

    internal fun activeParameterAt(file: PvmFile, offset: Int): Int? = callContextAt(file, offset)?.second

    private fun callContextAt(file: PvmFile, offset: Int): Pair<Pair<String, String?>, Int?>? {
        val document = file.canonicalDocument() ?: return null
        val knownNames = PvmProjectFileIndex.knownOperationStems(file)
        return PvmSymbolResolver.prakriyaCallContextAt(document, offset, knownNames)
    }

    private fun parameterHighlight(signature: PrakriyaSignature, index: Int): IntRange? {
        if (index < 0) return null
        val rendered = signature.parameters.map { "${it.nameStem}: ${it.type.sanskritName}" }
        if (rendered.isEmpty()) {
            val guard = signature.argumentType ?: return null
            return 0 until "मानम्: ${guard.sanskritName}".length
        }
        val selected = rendered.getOrNull(index) ?: return null
        val start = rendered.take(index).sumOf(String::length) + (index * 2)
        return start until start + selected.length
    }

    private fun ownerAt(file: PvmFile, offset: Int): PsiElement? {
        if (file.textLength == 0) return null
        val leaf = file.findElementAt(offset.coerceIn(0, file.textLength - 1))
            ?: file.findElementAt((offset - 1).coerceAtLeast(0))
            ?: return null
        return generateSequence(leaf) { it.parent }
            .filterIsInstance<PvmPsiElement>()
            .firstOrNull { it.node.elementType in PARAMETER_OWNER_TYPES }
            ?: leaf
    }

    private companion object {
        val PARAMETER_OWNER_TYPES = setOf(
            PvmTokenTypes.PRAKRIYA_HEADER,
            PvmTokenTypes.PRAKRIYA_STATEMENT,
            PvmTokenTypes.UKTI,
            PvmTokenTypes.PROGRAM_ITEM,
            PvmTokenTypes.UTTERANCE,
        )
    }
}

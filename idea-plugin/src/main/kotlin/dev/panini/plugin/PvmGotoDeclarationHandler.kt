package dev.panini.plugin

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

class PvmGotoDeclarationHandler : GotoDeclarationHandler {
    override fun getGotoDeclarationTargets(
        sourceElement: PsiElement?,
        offset: Int,
        editor: Editor,
    ): Array<PsiElement>? {
        val file = sourceElement?.containingFile as? PvmFile ?: return null
        val targets = resolvePrakriyaTargets(file, offset)
        return targets.takeIf(List<PsiElement>::isNotEmpty)?.toTypedArray()
    }

    override fun getActionText(context: com.intellij.openapi.actionSystem.DataContext): String? =
        "Go to PaniniVM prakriyā declaration"
}

internal fun resolvePrakriyaTargets(file: PvmFile, offset: Int): List<PsiElement> {
    val document = file.canonicalDocument() ?: return emptyList()
    val knownOperationStems = PvmProjectFileIndex.knownOperationStems(file)
    val identity = PvmSymbolResolver.prakriyaReferenceIdentityAt(
        document, offset, knownOperationStems,
    ) ?: return emptyList()
    return resolvePrakriyaTargets(file, identity, PvmProjectFileIndex.documentsForIdentity(file, identity))
}

internal fun resolvePrakriyaTargets(
    file: PvmFile,
    identity: Pair<String, String?>,
    documents: List<Pair<PvmFile, dev.panini.vyakaranam.ast.ProgramDocument>> =
        PvmProjectFileIndex.documentsForIdentity(file, identity),
): List<PsiElement> {
    return documents.flatMap { (candidate, candidateDocument) ->
        PvmSymbolResolver.prakriyaDeclarationOffsets(
            candidateDocument,
            identity.first,
            identity.second,
            includeInternal = candidate == file,
        ).mapNotNull(candidate::findElementAt)
    }.distinctBy { target -> target.containingFile.virtualFile?.url to target.textOffset }
}

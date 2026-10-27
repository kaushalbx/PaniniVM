package dev.panini.plugin

import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.IndexNotReadyException
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.util.Key
import com.intellij.psi.util.CachedValue
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.indexing.FileBasedIndex
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.ProgramDocument

/** Project PVM sources backed by IntelliJ's file index and each file's cached canonical AST. */
internal object PvmProjectFileIndex {
    private val DOCUMENTS_KEY = Key.create<CachedValue<List<Pair<PvmFile, ProgramDocument>>>>(
        "dev.panini.plugin.projectDocuments",
    )

    fun documents(anchor: PvmFile): List<Pair<PvmFile, ProgramDocument>> {
        val project = anchor.project
        val indexed = CachedValuesManager.getManager(project).getCachedValue(
            project,
            DOCUMENTS_KEY,
            {
                CachedValueProvider.Result.create(
                    indexedDocuments(project),
                    PsiModificationTracker.MODIFICATION_COUNT,
                    ProjectRootModificationTracker.getInstance(project),
                )
            },
            false,
        )
        val anchorUrl = anchor.virtualFile?.url
        if (indexed.any { (file, _) -> file == anchor || file.virtualFile?.url == anchorUrl }) return indexed
        return anchor.canonicalDocument()?.let { listOf(anchor to it) + indexed } ?: indexed
    }

    fun knownOperationStems(anchor: PvmFile): Set<String> {
        if (!DumbService.isDumb(anchor.project)) {
            try {
                val names = linkedSetOf<String>()
                FileBasedIndex.getInstance().processAllKeys(
                    PvmPrakriyaIndex.NAME,
                    Processor { key -> PvmPrakriyaIndex.operation(key)?.let(names::add); true },
                    anchor.project,
                )
                if (names.isNotEmpty()) return names
            } catch (_: IndexNotReadyException) {
                // Fall through to the cached canonical scan.
            }
        }
        return documents(anchor).flatMapTo(linkedSetOf()) { (_, document) ->
            document.items.filterIsInstance<Prakriya>().map(Prakriya::nameIdentity)
        }
    }

    fun documentsForIdentity(
        anchor: PvmFile,
        identity: Pair<String, String?>,
    ): List<Pair<PvmFile, ProgramDocument>> {
        if (!DumbService.isDumb(anchor.project)) {
            try {
                val files = FileBasedIndex.getInstance().getContainingFiles(
                    PvmPrakriyaIndex.NAME,
                    PvmPrakriyaIndex.key(identity.first, identity.second),
                    GlobalSearchScope.projectScope(anchor.project),
                )
                val psiManager = PsiManager.getInstance(anchor.project)
                val indexed = files.mapNotNull { virtualFile ->
                    ProgressManager.checkCanceled()
                    val file = psiManager.findFile(virtualFile) as? PvmFile ?: return@mapNotNull null
                    file.canonicalDocument()?.let { file to it }
                }
                val anchorDocument = anchor.canonicalDocument()
                if (anchorDocument != null && containsIdentity(anchorDocument, identity) &&
                    indexed.none { it.first == anchor }
                ) {
                    return listOf(anchor to anchorDocument) + indexed
                }
                if (indexed.isNotEmpty()) return indexed
            } catch (_: IndexNotReadyException) {
                // Fall through to the cached canonical scan.
            }
        }
        return documents(anchor)
    }

    fun declarationDocuments(anchor: PvmFile): List<Pair<PvmFile, ProgramDocument>> {
        if (!DumbService.isDumb(anchor.project)) {
            try {
                val files = FileBasedIndex.getInstance().getContainingFiles(
                    PvmPrakriyaIndex.NAME,
                    PvmPrakriyaIndex.ALL_DECLARATIONS,
                    GlobalSearchScope.projectScope(anchor.project),
                )
                val psiManager = PsiManager.getInstance(anchor.project)
                val indexed = files.mapNotNull { virtualFile ->
                    ProgressManager.checkCanceled()
                    val file = psiManager.findFile(virtualFile) as? PvmFile ?: return@mapNotNull null
                    file.canonicalDocument()?.let { file to it }
                }
                val anchorDocument = anchor.canonicalDocument()
                if (anchorDocument != null && anchorDocument.items.any { it is Prakriya } &&
                    indexed.none { it.first == anchor }
                ) {
                    return listOf(anchor to anchorDocument) + indexed
                }
                if (indexed.isNotEmpty()) return indexed
            } catch (_: IndexNotReadyException) {
                // Fall through to the cached canonical scan.
            }
        }
        return documents(anchor).filter { (_, document) -> document.items.any { it is Prakriya } }
    }

    private fun containsIdentity(document: ProgramDocument, identity: Pair<String, String?>): Boolean {
        var activeDomain: String? = null
        return document.items.any { item ->
            if (item is dev.panini.vyakaranam.ast.Scope) {
                activeDomain = item.domainIdentity
                false
            } else {
                val declaration = item as? Prakriya ?: return@any false
                declaration.nameIdentity == identity.first &&
                    (declaration.domainIdentity ?: activeDomain) == identity.second
            }
        }
    }

    private fun indexedDocuments(project: Project): List<Pair<PvmFile, ProgramDocument>> {
        val psiManager = PsiManager.getInstance(project)
        return try {
            buildList<PvmFile> {
                FileTypeIndex.processFiles(PvmFileType.INSTANCE, { virtualFile ->
                    ProgressManager.checkCanceled()
                    (psiManager.findFile(virtualFile) as? PvmFile)?.let(::add)
                    true
                }, GlobalSearchScope.projectScope(project))
            }.distinctBy { it.virtualFile?.url }
                .mapNotNull { file ->
                    ProgressManager.checkCanceled()
                    file.canonicalDocument()?.let { file to it }
                }
        } catch (_: IndexNotReadyException) {
            emptyList()
        }
    }
}

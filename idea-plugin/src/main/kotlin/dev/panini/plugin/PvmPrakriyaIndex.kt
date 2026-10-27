package dev.panini.plugin

import com.intellij.openapi.fileTypes.FileType
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.DefaultFileTypeSpecificInputFilter
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.indexing.ScalarIndexExtension
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.ast.Scope
import dev.panini.vyakaranam.parser.PaniniParser

/** Persistent declaration keys; canonical documents remain the semantic source of truth. */
class PvmPrakriyaIndex : ScalarIndexExtension<String>() {
    override fun getName(): ID<String, Void> = NAME
    override fun getVersion(): Int = VERSION
    override fun dependsOnFileContent(): Boolean = true
    override fun getKeyDescriptor(): KeyDescriptor<String> = EnumeratorStringDescriptor.INSTANCE
    override fun getInputFilter(): FileBasedIndex.InputFilter =
        DefaultFileTypeSpecificInputFilter(PvmFileType.INSTANCE)

    override fun getIndexer(): DataIndexer<String, Void, FileContent> = DataIndexer { input ->
        val document = runCatching {
            PaniniParser().parseDocument(input.contentAsText.toString())
        }.getOrNull() ?: return@DataIndexer emptyMap()
        var activeDomain: String? = null
        buildMap {
            var hasDeclarations = false
            document.items.forEach itemLoop@{ item ->
                if (item is Scope) {
                    activeDomain = item.domainIdentity
                    return@itemLoop
                }
                val declaration = item as? Prakriya ?: return@itemLoop
                hasDeclarations = true
                put(key(declaration.nameIdentity, declaration.domainIdentity ?: activeDomain), null)
            }
            if (hasDeclarations) put(ALL_DECLARATIONS, null)
        }
    }

    companion object {
        val NAME: ID<String, Void> = ID.create("dev.panini.plugin.prakriya")
        private const val VERSION = 2
        private const val SEPARATOR = '\u001F'
        const val ALL_DECLARATIONS = "\u0000declarations"

        fun key(operation: String, domain: String?): String =
            operation + SEPARATOR + domain.orEmpty()

        fun operation(key: String): String? =
            key.takeUnless { it == ALL_DECLARATIONS }?.substringBefore(SEPARATOR)
    }
}

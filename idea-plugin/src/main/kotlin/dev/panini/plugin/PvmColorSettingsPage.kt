package dev.panini.plugin

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class PvmColorSettingsPage : ColorSettingsPage {

    private val descriptors = arrayOf(
        AttributesDescriptor("Keyword", PvmSyntaxHighlighter.KEYWORD),
        AttributesDescriptor("Affix", PvmSyntaxHighlighter.AFFIX),
        AttributesDescriptor("Number", PvmSyntaxHighlighter.NUMBER),
        AttributesDescriptor("Operator", PvmSyntaxHighlighter.OPERATOR),
        AttributesDescriptor("Delimiter", PvmSyntaxHighlighter.DELIMITER),
        AttributesDescriptor("Comment", PvmSyntaxHighlighter.COMMENT),
        AttributesDescriptor("Bad character", PvmSyntaxHighlighter.BAD_CHARACTER),
    )

    override fun getIcon(): Icon = PvmIcons.FILE
    override fun getHighlighter(): SyntaxHighlighter = PvmSyntaxHighlighter()

    override fun getDemoText(): String {
        return """
            // PaniniVM Script Example
            हे यन्त्र + सुँ, एक + अम् द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
            एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "PaniniVM"
}

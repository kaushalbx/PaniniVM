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
        AttributesDescriptor("Control flow", PvmSyntaxHighlighter.CONTROL_FLOW),
        AttributesDescriptor("Upasarga", PvmSyntaxHighlighter.UPASARGA),
        AttributesDescriptor("Sanadi affix", PvmSyntaxHighlighter.SANADI),
        AttributesDescriptor("Lakara", PvmSyntaxHighlighter.LAKARA),
        AttributesDescriptor("Ting affix", PvmSyntaxHighlighter.TING_AFFIX),
        AttributesDescriptor("Sup affix", PvmSyntaxHighlighter.SUP_AFFIX),
        AttributesDescriptor("Vikarana", PvmSyntaxHighlighter.VIKARANA),
        AttributesDescriptor("Agama", PvmSyntaxHighlighter.AGAMA),
        AttributesDescriptor("Krt affix", PvmSyntaxHighlighter.KRT_AFFIX),
        AttributesDescriptor("Taddhita affix", PvmSyntaxHighlighter.TADDHITA_AFFIX),
        AttributesDescriptor("Stri affix", PvmSyntaxHighlighter.STRI_AFFIX),
        AttributesDescriptor("Lopa", PvmSyntaxHighlighter.LOPA),
        AttributesDescriptor("Derivation marker", PvmSyntaxHighlighter.DERIVATION_MARKER),
        AttributesDescriptor("Abhyasa affix", PvmSyntaxHighlighter.ABHYASA_AFFIX),
        AttributesDescriptor("Interjection", PvmSyntaxHighlighter.INTERJECTION),
        AttributesDescriptor("Semantic//Prakriya declaration", PvmSyntaxHighlighter.PRAKRIYA_DECLARATION),
        AttributesDescriptor("Semantic//Prakriya reference", PvmSyntaxHighlighter.PRAKRIYA_REFERENCE),
        AttributesDescriptor("Semantic//Domain declaration", PvmSyntaxHighlighter.DOMAIN_DECLARATION),
        AttributesDescriptor("Semantic//Domain reference", PvmSyntaxHighlighter.DOMAIN_REFERENCE),
        AttributesDescriptor("Semantic//Nominal stem", PvmSyntaxHighlighter.NOMINAL_STEM),
        AttributesDescriptor("Number", PvmSyntaxHighlighter.NUMBER),
        AttributesDescriptor("Dhatu", PvmSyntaxHighlighter.DHATU),
        AttributesDescriptor("Phala result", PvmSyntaxHighlighter.PHALA),
        AttributesDescriptor("Identifier", PvmSyntaxHighlighter.IDENTIFIER),
        AttributesDescriptor("Operator", PvmSyntaxHighlighter.OPERATOR),
        AttributesDescriptor("Delimiter", PvmSyntaxHighlighter.DELIMITER),
        AttributesDescriptor("Comment", PvmSyntaxHighlighter.COMMENT),
        AttributesDescriptor("Bad character", PvmSyntaxHighlighter.BAD_CHARACTER),
    )

    override fun getIcon(): Icon = PvmIcons.FILE
    override fun getHighlighter(): SyntaxHighlighter = PvmSyntaxHighlighter()

    override fun getDemoText(): String {
        return """
            # PaniniVM Script Example
            अहो राम भू फल सुच् @
            यदि प्र + कृ + सन् + लट् + तिप् तर्हि राम + सुँ फल + अम् च पठ् + शप् + लोट् + सिप् ।
            नदी + ङीप् अण् + तद्धितम् नुम् लुक् उणादि च ।
            <domainDeclaration>गणित</domainDeclaration> + सुँ इति अधि + कृ + घञ् + सुँ ।
            <domainReference>गणित</domainReference> + ङस् <prakriyaDeclaration>गण</prakriyaDeclaration> + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            <nominalStem>राम</nominalStem> + सुँ भू + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च <domainReference>गणित</domainReference> + ङस् <prakriyaReference>गण</prakriyaReference> + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = mapOf(
        "prakriyaDeclaration" to PvmSyntaxHighlighter.PRAKRIYA_DECLARATION,
        "prakriyaReference" to PvmSyntaxHighlighter.PRAKRIYA_REFERENCE,
        "domainDeclaration" to PvmSyntaxHighlighter.DOMAIN_DECLARATION,
        "domainReference" to PvmSyntaxHighlighter.DOMAIN_REFERENCE,
        "nominalStem" to PvmSyntaxHighlighter.NOMINAL_STEM,
    )
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    override fun getDisplayName(): String = "PaniniVM"
}

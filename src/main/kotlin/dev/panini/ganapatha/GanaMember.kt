package dev.panini.ganapatha

import dev.panini.shiksha.Accent
import dev.panini.shiksha.LexicalUse
import dev.panini.shiksha.Recension

/** A machine-readable restriction on when a gaṇa member is eligible. */
sealed interface GanaCondition {
    val sourceText: String

    fun matches(lexicalUses: Set<LexicalUse>): Boolean
}

data object RelativePositionNotProperName : GanaCondition {
    override val sourceText: String = "व्यवस्थायाम् असंज्ञायाम्"

    override fun matches(lexicalUses: Set<LexicalUse>): Boolean =
        LexicalUse.RELATIVE_POSITION in lexicalUses &&
            LexicalUse.PROPER_NAME !in lexicalUses
}

data object NotKinshipWealthOrProperName : GanaCondition {
    override val sourceText: String = "अज्ञातिधनाख्यायाम्"

    override fun matches(lexicalUses: Set<LexicalUse>): Boolean =
        LexicalUse.KINSHIP !in lexicalUses &&
            LexicalUse.WEALTH !in lexicalUses &&
            LexicalUse.PROPER_NAME !in lexicalUses
}

data object ExteriorAssociationOrGarment : GanaCondition {
    override val sourceText: String = "बहिर्योगोपसंव्यानयोः"

    override fun matches(lexicalUses: Set<LexicalUse>): Boolean =
        LexicalUse.EXTERIOR_ASSOCIATION in lexicalUses ||
            LexicalUse.GARMENT in lexicalUses
}

/** A named subgroup whose members belong to one parent gaṇa. */
data class AntarGana(
    val name: String,
    val members: List<String>,
    /** Surface forms licensed by the subgroup's affix entries. */
    val derivedForms: Set<String> = emptySet(),
    val condition: String? = null,
) {
    init {
        require(name.isNotBlank()) { "An antargaṇa name is required." }
        require(members.isNotEmpty()) { "An antargaṇa must contain at least one member." }
        require(members.all { it.isNotBlank() }) { "An antargaṇa member cannot be blank." }
    }

    fun contains(text: String): Boolean {
        val normalized = GanaNormalizer.normalize(text)
        return members.any { GanaNormalizer.normalize(it) == normalized } ||
            derivedForms.any { GanaNormalizer.normalize(it) == normalized }
    }
}

data class GanaMember(
    val text: String,
    val hindiArtha: String = "",
    val englishArtha: String = "",
    val upadesha: String? = null,
    val condition: String? = null,
    val ganaCondition: GanaCondition? = null,
    val accent: Accent? = null,
    val isVartika: Boolean = false,
    val isInstruction: Boolean = false,
    val recensions: Set<Recension> = emptySet(),
    val examples: List<String> = emptyList(),
) {
    val normalized: String = GanaNormalizer.normalize(text)

    init {
        require(condition == null || ganaCondition == null || condition == ganaCondition.sourceText) {
            "The source condition must match the executable gaṇa condition."
        }
    }
}

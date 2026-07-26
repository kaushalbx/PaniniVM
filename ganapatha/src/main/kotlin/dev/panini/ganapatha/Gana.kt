package dev.panini.ganapatha

import dev.panini.core.Linga
import dev.panini.shiksha.LexicalUse
import dev.panini.shiksha.Samjna

abstract class Gana(
    val name: String,
    val sourceIndex: Int,
    val sutra: String,
    val sutraId: String,
    val sutraText: String,
    val sutraTransliteration: String,
    val kind: GanaKind,
    val sanskritMeaning: String = "",
    val hindiMeaning: String = "",
    val englishMeaning: String = "",
    val members: List<GanaMember>,
    val source: GanaSource = GanaSource.GANAPATHA_DATA,
    val sourceUrl: String = GanaPathaSources.DATA_URL,
    val vartika: String = "",
    val rawWords: String = members.joinToString(" । ", postfix = " ॥") { it.text },
    val notes: String? = null,
    val genders: Set<Linga> = emptySet(),
    val resultSamjnas: Set<Samjna> = emptySet(),
    val references: List<String> = emptyList(),
    val adhikara: Adhikara? = null,
    val antarGanas: List<AntarGana> = emptyList(),
) {
    private val membersByNormalized: Map<String, GanaMember> =
        members.associateBy { it.normalized }

    val memberTexts: List<String>
        get() = members.map { it.text }

    val normalizedMemberTexts: Set<String>
        get() = membersByNormalized.keys

    init {
        antarGanas.forEach { antarGana ->
            require(antarGana.members.all { member -> contains(member) }) {
                "Antargaṇa ${antarGana.name} contains a member absent from ${sourceIndex}."
            }
        }
    }

    fun contains(text: String): Boolean =
        contains(GanaInstructionContext(text))

    fun contains(context: GanaInstructionContext): Boolean =
        findMember(context.text) != null || matchesInstruction(context)

    fun findMember(text: String): GanaMember? =
        membersByNormalized[GanaNormalizer.normalize(text)]

    fun eligibleMember(text: String, lexicalUses: Set<LexicalUse> = emptySet()): GanaMember? =
        findMember(text)?.takeIf { member ->
            member.ganaCondition?.matches(lexicalUses) ?: true
        }

    fun isEligible(text: String, lexicalUses: Set<LexicalUse> = emptySet()): Boolean =
        isEligible(GanaInstructionContext(text), lexicalUses)

    fun isEligible(context: GanaInstructionContext, lexicalUses: Set<LexicalUse> = emptySet()): Boolean =
        eligibleMember(context.text, lexicalUses) != null || matchesInstruction(context) || isAntarGanaMember(context.text)

    fun requireMember(text: String): GanaMember =
        findMember(text) ?: error("Member $text is not present in gana ${sourceIndex}.")

    fun antarGanasContaining(text: String): List<AntarGana> =
        antarGanas.filter { it.contains(text) }

    fun isAntarGanaMember(text: String): Boolean =
        antarGanasContaining(text).isNotEmpty()

    fun hasMeaning(): Boolean =
        sanskritMeaning.isNotBlank() || hindiMeaning.isNotBlank() || englishMeaning.isNotBlank()

    private fun matchesInstruction(context: GanaInstructionContext): Boolean =
        members.asSequence()
            .filter { it.isInstruction }
            .any { GanaInstructionMatcher.matches(it.text, context) }
}

/** A governing scope introduced by the gaṇa's associated sūtra. */
data class Adhikara(
    val name: String,
    val endsAtSutra: String,
)

enum class GanaSource {
    GANAPATHA_DATA,
}

enum class GanaKind {
    PATHA,
    AKRTI,
    /** The source lists this gaṇa without a pāṭha/ākṛti classification. */
    UNSPECIFIED,
}

object GanaPathaSources {
    const val PUBLIC_PAGE_URL: String = "https://ashtadhyayi.com/ganapath"
    const val DATA_URL: String =
        "https://cdn.jsdelivr.net/gh/ashtadhyayi-com/data@4a63049/ganapath/data.txt"
}

object GanaNormalizer {
    fun normalize(value: String): String =
        value.trim()
}

package dev.panini.ganapatha

import dev.panini.shiksha.LexicalUse

object GanaPatha {
    val all: List<Gana> = GanaPathaData.all

    private val bySourceIndex: Map<Int, Gana> = all.associateBy { it.sourceIndex }

    init {
        require(bySourceIndex.size == all.size) { "Duplicate gaṇa source indices are not allowed." }
    }

    fun get(sourceIndex: Int): Gana? = bySourceIndex[sourceIndex]

    fun findByName(name: String): List<Gana> = all.filter { it.name == name }

    fun require(sourceIndex: Int): Gana =
        get(sourceIndex) ?: error("Gaṇa at source index $sourceIndex is not present in this Gaṇapāṭha.")

    fun contains(sourceIndex: Int, text: String): Boolean {
        return get(sourceIndex)?.contains(text) == true
    }

    fun isEligibleMember(
        sourceIndex: Int,
        text: String,
        lexicalUses: Set<LexicalUse> = emptySet(),
        suffixUpadeshas: Set<String> = emptySet(),
    ): Boolean = get(sourceIndex)?.isEligible(
        GanaInstructionContext(text, suffixUpadeshas),
        lexicalUses,
    ) == true

    fun ganasContaining(text: String): List<Gana> {
        return all.filter { it.contains(text) }
    }

    /** Finds subgroups that contain [text], paired with their parent gaṇa. */
    fun antarGanasContaining(text: String): List<Pair<Gana, AntarGana>> =
        all.flatMap { gana -> gana.antarGanasContaining(text).map { antarGana -> gana to antarGana } }

    val gotraGanas: List<Gana> by lazy {
        listOf(
            GargadiGana,
            BidadiGana,
            UpakadiGana,
            SharngaravadiGana,
            ShivadiGana,
            AshvadiGana
        )
    }

    private val genericVamshyaNouns = setOf("मुनि", "ऋषि", "वंश्य", "गुरु", "आचार्य")

    fun isVamshya(stem: String, samjnas: Set<dev.panini.shiksha.Samjna> = emptySet()): Boolean {
        if (samjnas.any { it is dev.panini.shiksha.Samjna.Rudhi && (it.word == "VAMSHYA" || it.word == "GOTRA") }) return true
        if (stem in genericVamshyaNouns) return true
        if (stem.endsWith("मुनि") || stem.endsWith("ऋषि")) return true
        return gotraGanas.any { it.contains(stem) }
    }
}

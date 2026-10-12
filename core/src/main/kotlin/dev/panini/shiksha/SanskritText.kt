package dev.panini.shiksha

/** Stable identity of one varṇa occurrence inside a [SanskritText]. */
@JvmInline
value class VarnaTokenId(val value: String)

/** Why a varṇa occurrence does or does not participate in the current form. */
enum class VarnaState {
    PRESENT,
    LUPTA,
    SUPERSEDED,
    NON_OPERATIVE,
}

/** Accent belongs to a vowel occurrence, not to the phonological vowel type. */
enum class VarnaAccent { UDATTA, ANUDATTA, SVARITA }

/** UTF-16 span in the NFC-normalized input, not a phonological position.
 * An inherent vowel has an empty span until a qualifying sign is encountered.
 * This is parse provenance only; transformations must not treat it as a current offset.
 */
data class VarnaSourceSpan(val start: Int, val endExclusive: Int) {
    init {
        require(start >= 0 && endExclusive >= start)
    }
}

data class VarnaToken(
    val id: VarnaTokenId,
    val varna: Varna,
    val accent: VarnaAccent? = null,
    val nasalized: Boolean = false,
    val state: VarnaState = VarnaState.PRESENT,
    val stateAssignedBySutra: String? = null,
    val sourceSpan: VarnaSourceSpan? = null,
) {
    init {
        require(!nasalized || varna is Svara || varna in nasalizableSemivowels) {
            "Anunāsikatva requires a vowel or a nasalizable semivowel: $id=$varna."
        }
        require(accent == null || varna is Svara) { "Only a vowel token can carry accent: $id=$varna." }
        require(state == VarnaState.PRESENT || stateAssignedBySutra != null) {
            "A non-present varṇa token must retain the sūtra that changed its state: $id."
        }
    }

    val isEffective: Boolean
        get() = state == VarnaState.PRESENT

    companion object {
        // The nasal counterparts required by parasavarṇa and tor li. Ra has no such counterpart.
        val nasalizableSemivowels: Set<Varna> = setOf(Vyanjana.YA, Vyanjana.VA, Vyanjana.LA)
    }
}

/** A phonological sequence independent of Devanāgarī mātrā and virāma layout. */
data class SanskritText(
    val varnas: List<VarnaToken>,
    /** Parsed non-phonological signs, retained as source provenance. */
    val sourceOrthographicSigns: List<OrthographicSignPlacement> = emptyList(),
) {
    init {
        require(varnas.map { it.id }.distinct().size == varnas.size) {
            "Varṇa token IDs must be unique within a SanskritText."
        }
    }

    val effectiveVarnas: List<VarnaToken>
        get() = varnas.filter(VarnaToken::isEffective)

    fun first(): VarnaToken? = effectiveVarnas.firstOrNull()

    fun last(): VarnaToken? = effectiveVarnas.lastOrNull()

    fun render(): String = DevanagariRenderer.render(this)

    companion object {
        fun parse(text: String, tokenIdPrefix: String = "varna"): SanskritText =
            DevanagariParser.parse(text, tokenIdPrefix)
    }
}

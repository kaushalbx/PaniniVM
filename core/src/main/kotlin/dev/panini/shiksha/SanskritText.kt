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

data class VarnaToken(
    val id: VarnaTokenId,
    val varna: Varna,
    val accent: VarnaAccent? = null,
    val nasalized: Boolean = false,
    val state: VarnaState = VarnaState.PRESENT,
    val stateAssignedBySutra: String? = null,
) {
    init {
        require(!nasalized || varna is Svara) { "Only a vowel token can carry anunāsikatva: $id=$varna." }
        require(accent == null || varna is Svara) { "Only a vowel token can carry accent: $id=$varna." }
        require(state == VarnaState.PRESENT || stateAssignedBySutra != null) {
            "A non-present varṇa token must retain the sūtra that changed its state: $id."
        }
    }

    val isEffective: Boolean
        get() = state == VarnaState.PRESENT
}

/** A phonological sequence independent of Devanāgarī mātrā and virāma layout. */
data class SanskritText(val varnas: List<VarnaToken>) {
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

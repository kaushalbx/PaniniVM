package dev.sanskrit.derivation

/**
 * The shared state passed through an Ashtadhyayi derivation.
 */
data class DerivationState(
    val terms: List<DerivationTerm>,
    val droppedTerms: List<DerivationTerm> = emptyList(),
    val samjnas: Set<SamjnaAssignment> = emptySet(),
    val stage: DerivationStage = DerivationStage.INITIAL,
    val semanticFeatures: Set<SemanticFeature> = emptySet(),
    val activeAdhikaras: Set<String> = emptySet(),
    val inheritedAnuvrtti: Set<String> = emptySet(),
    val blockedSutras: Map<String, String> = emptyMap(),
    val varnaComparisons: Set<VarnaComparison> = emptySet(),
    val substitutions: List<VarnaSubstitution> = emptyList(),
) {
    init {
        require(terms.isNotEmpty()) { "A derivation requires at least one term." }
    }

    val surface: String
        get() = terms.joinToString(separator = "") { it.surface }

    val allEffectiveTerms: List<DerivationTerm>
        get() = terms + droppedTerms

    fun withSamjnas(additions: Set<SamjnaAssignment>): DerivationState =
        copy(samjnas = samjnas + additions)

    fun replaceTerm(id: String, replacement: DerivationTerm): DerivationState =
        copy(terms = terms.map { if (it.id == id) replacement else it })

    fun removeTerm(id: String): DerivationState {
        val term = terms.find { it.id == id } ?: return this
        return copy(
            terms = terms.filter { it.id != id },
            droppedTerms = droppedTerms + term.copy(surface = "")
        )
    }

    fun addTerm(term: DerivationTerm): DerivationState {
        require(terms.none { it.id == term.id }) { "A derivation term id must be unique: ${term.id}" }
        return copy(terms = terms + term)
    }

    fun activateAdhikara(sutraNumber: String): DerivationState =
        copy(activeAdhikaras = activeAdhikaras + sutraNumber)

    fun carryAnuvrtti(item: String): DerivationState =
        copy(inheritedAnuvrtti = inheritedAnuvrtti + item)

    fun blockSutra(sutraNumber: String, blocker: String): DerivationState =
        copy(blockedSutras = blockedSutras + (sutraNumber to blocker))

    fun addComparison(comparison: VarnaComparison): DerivationState =
        copy(varnaComparisons = varnaComparisons + comparison)

    fun addSubstitution(substitution: VarnaSubstitution): DerivationState =
        copy(substitutions = substitutions + substitution)
}

data class VarnaComparison(
    val leftTermId: String, val rightTermId: String,
    val left: Char, val right: Char,
    val samePlaceAndEffort: Boolean,
    val leftIsVowel: Boolean, val rightIsVowel: Boolean,
    val forbidden: Boolean = false,
)

data class VarnaSubstitution(
    val targetId: String, val source: Char,
    val replacement: String, val sutra: String,
)

data class DerivationTerm(
    val id: String,
    val surface: String,
    val kind: TermKind,
    val itMarkers: Set<ItMarker> = emptySet(),
    val upadesha: String? = null,
    val deletionType: LopaType? = null,
    val sthaniProps: SthaniProperties? = null 
) {
    fun hasEffectiveMarker(marker: ItMarker): Boolean = 
        marker in itMarkers || (sthaniProps?.itMarkers?.contains(marker) == true)

    fun matchesUpadesha(value: String): Boolean =
        upadesha == value || sthaniProps?.upadesha == value
}

data class SthaniProperties(
    val upadesha: String?,
    val itMarkers: Set<ItMarker>
)

enum class LopaType { LOPA, LUK, SHLU, LUP }
enum class TermKind { DHATU, PRATIPADIKA, PRATYAYA, AGAMA, AUGMENT }
enum class ItMarker { U, J, T, P, SH, NG, KIT, NGIT, NIT, SIT }
enum class DerivationStage { INITIAL, PRATYAYA_SELECTED, IT_PROCESSED, ANGAKARYA, PADA_FORMED, FINAL }

data class SamjnaAssignment(val targetId: String, val samjna: Samjna)

enum class Samjna { 
    VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, ANUNASIKA, SAVARNA, 
    DHATU, PRATYAYA, ANGA, PADA, PRAGRHYA, SARVANAMA, BHA, GHI, 
    NADI, APRUKTA, SAMBUDDHI, SARVANAMASTHANA, GHU, PRATIPADIKA
}

enum class SemanticFeature {
    KARTARI, KARMANI, BHAVE, GUNA_REQUEST, VRDDHI_REQUEST, DHATU_LOPA, ARDHADHATUKA, VARTAMANA, LAT, PRATHAMA_PURUSHA, PUMS, STRI, NAPUMSAKA, PRATHAMA, DVITIYA, TRTIYA, CHATURTHI, PANCHAMI, SASTHI, SAPTAMI, SAMBODHANA, EKAVACANA, DVIVACANA, BAHUVACANA
}

package dev.panini.sutra

/** Closed vocabulary for a sūtra's primary grammatical function. */
sealed interface SutraRole {
    data object Samjna : SutraRole
    data object Vidhi : SutraRole
    data object Nishedha : SutraRole
    data object Niyama : SutraRole
    data object Atidesha : SutraRole
    data object Adhikara : SutraRole
    data object Anuvrtti : SutraRole
    data object Paribhasha : SutraRole
    data object Apavada : SutraRole
    data object Vibhasha : SutraRole
}

enum class SutraAction { SAMJNA, VIDHI, ADESHA, NISHEDHA, NIYAMA, ATIDESHA, ADHIKARA, ANUVRTTI, PARIBHASHA, PRATYAYA_SELECTION, LOPA, AGAMA, APAVADA, VIKALPA }
enum class SutraScope { VARNA, PADA_BOUNDARY, DHATU, PRATYAYA, DERIVATION, VAKYA }
enum class SutraInput { VARNA, PRAHYAHARA, DHATU, PRATIPADIKA, PRATYAYA, IT_MARKER, SAMJNA, SEMANTIC_FEATURE, DERIVATION_STAGE, PRATYAHARA, KARAKA, KARAKA_CANDIDATE }
enum class SutraStage { UNSPECIFIED, SAMJNA, PRATYAYA_SELECTION, IT_PROCESSING, ANGAKARYA, PADA_FORMATION, SANDHI, FINAL }
enum class SutraPriority { NORMAL, UTSARGA, ANTARANGA, NITYA, APAVADA }
enum class SutraVisibility { NORMAL, ASIDDHA, ASIDDHAVAT }

/** Triggers for a rule. INTERNAL means causes are inside the stem; EXTERNAL means suffix-driven. */
enum class NimittaScope { INTERNAL, EXTERNAL, BOTH, UNKNOWN }

data class SutraExample(val input: String, val output: String, val note: String? = null)

data class SutraGovernance(
    val optional: Boolean = false,
    val priority: SutraPriority = SutraPriority.NORMAL,
    val blocks: Set<String> = emptySet(),
    val visibility: SutraVisibility = SutraVisibility.NORMAL,
)

interface ScopedSutra {
    val adhikara: Set<String>
    val anuvrtti: Set<String>
}

interface GovernedSutra {
    val dependencies: Set<String>
    val restrictions: Set<String>
    val exceptions: Set<String>
    val governance: SutraGovernance
}

interface TraceableSutra {
    val traceTemplate: String?
    val examples: List<SutraExample>
}

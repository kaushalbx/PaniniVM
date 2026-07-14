package dev.sanskrit.sutra

/**
 * Shared sūtra base. All descriptive information belongs here directly;
 * executable subclasses contribute only their condition and operation.
 */
abstract class Sutra<C, R>(
    val number: String,
    val text: String,
    val hindiExplanation: String,
    val englishExplanation: String? = null,
    val type: SutraType,
    val chapter: Int, val pada: Int,
    val optional: Boolean,
    val kramaValue: Int,
    val avastha: SutraAvastha = SutraAvastha.KRIYAVAT,
    val role: SutraRole,
    val action: SutraAction,
    val scope: SutraScope,
    val nimittaScope: NimittaScope = NimittaScope.UNKNOWN,
    val inputs: Set<SutraInput> = emptySet(),
    override val dependencies: Set<String> = emptySet(),
    override val adhikara: Set<String> = emptySet(),
    override val anuvrtti: Set<String> = emptySet(),
    val stage: SutraStage = SutraStage.UNSPECIFIED,
    override val restrictions: Set<String> = emptySet(),
    override val exceptions: Set<String> = emptySet(),
    val priority: SutraPriority = SutraPriority.NORMAL,
    val visibility: SutraVisibility = SutraVisibility.NORMAL,
    val blocks: Set<String> = emptySet(),
    private val traceTemplateValue: String? = null,
    private val examplesValue: List<SutraExample> = emptyList(),
) : ScopedSutra, GovernedSutra, TraceableSutra {

    /** Every loaded sūtra must state its own eligibility and grammatical change. */
    abstract fun matches(context: C): Boolean

    abstract fun apply(context: C): R

    val sutra: String get() = number
    val sutraText: String get() = text
    val krama: Int get() = kramaValue


    override val governance: SutraGovernance
        get() = SutraGovernance(optional, priority, blocks, visibility)

    override val traceTemplate: String?
        get() = traceTemplateValue

    override val examples: List<SutraExample>
        get() = examplesValue
}

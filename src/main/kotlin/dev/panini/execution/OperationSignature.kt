package dev.panini.execution

data class OperationSignature(
    val requirements: List<KarakaRequirement>,
    val optionalKarakas: Set<Karaka> = emptySet(),
) {
    init {
        require(requirements.map { it.karaka }.distinct().size == requirements.size) {
            "An operation signature cannot repeat a kāraka requirement."
        }
        require(requirements.none { it.karaka in optionalKarakas }) {
            "A kāraka cannot be both required and optional."
        }
    }

    internal val specificity: Int get() = requirements.sumOf { it.specificity }
}

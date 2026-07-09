package dev.sanskrit.sutra

class SutraPatha(
    entries: List<SutraMetadata>,
) {
    private val byNumber = entries.associateBy { it.sutraNumber }
    val entries: List<SutraMetadata> = entries.sortedBy { it.krama }

    init {
        require(byNumber.size == entries.size) { "Duplicate sutra numbers are not allowed." }
    }

    val pathitaCount: Int
        get() = entries.size

    val kriyavatCount: Int
        get() = entries.count { it.avastha == SutraAvastha.KRIYAVAT }

    fun get(sutraNumber: String): SutraMetadata? = byNumber[sutraNumber]

    fun require(sutraNumber: String): SutraMetadata =
        get(sutraNumber) ?: error("Sutra $sutraNumber is not present in this patha.")
}

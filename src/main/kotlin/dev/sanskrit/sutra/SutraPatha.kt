package dev.sanskrit.sutra

class SutraPatha(
    entries: List<Sutra<*, *>>,
) {
    private val byNumber = entries.associateBy { it.sutra }
    val entries: List<Sutra<*, *>> = entries.sortedBy { it.krama }

    init {
        require(byNumber.size == entries.size) { "Duplicate sutra numbers are not allowed." }
    }

    val pathitaCount: Int
        get() = entries.size

    val kriyavatCount: Int
        get() = entries.count()

    fun get(sutraNumber: String): Sutra<*, *>? = byNumber[sutraNumber]

    fun require(sutraNumber: String): Sutra<*, *> =
        get(sutraNumber) ?: error("Sutra $sutraNumber is not present in this patha.")
}

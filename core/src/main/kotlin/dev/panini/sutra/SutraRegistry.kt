package dev.panini.sutra

/**
 * A navigable registry of the loaded Aṣṭādhyāyī graph.
 *
 * It deliberately stores rule objects, not only metadata, so callers can use
 * both a sūtra's grammatical role and its executable behavior.
 */
class SutraRegistry(
    sutras: List<Sutra<*, *>>,
) {
    val sutras: List<Sutra<*, *>> = sutras.sortedBy { it.krama }
    private val byNumber: Map<String, Sutra<*, *>> = this.sutras.associateBy { it.sutra }

    init {
        require(byNumber.size == sutras.size) { "Duplicate sutra numbers are not allowed." }
    }

    fun get(sutraNumber: String): Sutra<*, *>? = byNumber[sutraNumber]

    fun require(sutraNumber: String): Sutra<*, *> =
        get(sutraNumber) ?: error("Sutra $sutraNumber is not present in this registry.")

    /** Canonical view of an adhikāra and the sūtras governed by its textual range. */
    fun governedBy(adhikaraNumber: String): List<Sutra<*, *>> {
        val adhikara = require(adhikaraNumber)
        val role = adhikara.role as? SutraRole.Adhikara
            ?: error("Sutra $adhikaraNumber is not an adhikara.")
        val start = role.customStartKrama ?: adhikara.krama
        return sutras.filter { it == adhikara || it.krama in start..role.endKrama }
    }

}

package dev.panini.unadipatha

import java.util.Base64

/** A single sūtra in the five-adhyāya Uṇādipāṭha source catalog. */
data class UnadiPathaEntry(
    val number: String,
    val text: String,
    val pratyaya: String,
    val siddhantaKaumudi: String,
) {
    val adhyaya: Int get() = number.substringBefore('.').toInt()
    val sutra: Int get() = number.substringAfter('.').toInt()
}

/**
 * Complete Uṇādipāṭha in source order.
 *
 * The bundled data is derived from https://ashtadhyayi.com/unaadi and its
 * public data repository. Fields are Base64-encoded in the resource only so
 * tabs and line breaks in the Sanskrit commentary cannot corrupt a record.
 */
object UnadiPathaCatalog {
    private const val RESOURCE = "/dev/panini/unadipatha/ashtadhyayi-com-unadi.tsv"

    val sutras: List<UnadiPathaEntry> by lazy {
        val stream = checkNotNull(UnadiPathaCatalog::class.java.getResourceAsStream(RESOURCE)) {
            "Missing Uṇādipāṭha catalog resource: $RESOURCE"
        }
        stream.bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.filter(String::isNotBlank).map(::decodeEntry).toList()
        }.also(::validate)
    }

    fun find(number: String): UnadiPathaEntry? = sutras.find { it.number == number }

    fun adhyaya(number: Int): List<UnadiPathaEntry> = sutras.filter { it.adhyaya == number }

    private fun decodeEntry(line: String): UnadiPathaEntry {
        val fields = line.split('\t')
        require(fields.size == 4) { "Malformed Uṇādipāṭha catalog row" }
        return UnadiPathaEntry(
            number = fields[0],
            text = decode(fields[1]),
            pratyaya = decode(fields[2]),
            siddhantaKaumudi = decode(fields[3]),
        )
    }

    private fun decode(value: String): String =
        String(Base64.getDecoder().decode(value), Charsets.UTF_8)

    private fun validate(entries: List<UnadiPathaEntry>) {
        require(entries.size == 748) { "Expected 748 Uṇādipāṭha sūtras, found ${entries.size}" }
        require(entries.map { it.number }.distinct().size == entries.size) {
            "Duplicate Uṇādipāṭha sūtra number"
        }
        require(entries.zipWithNext().all { (left, right) ->
            left.adhyaya < right.adhyaya ||
                (left.adhyaya == right.adhyaya && left.sutra + 1 == right.sutra)
        }) { "Uṇādipāṭha catalog is not in uninterrupted source order" }
    }
}

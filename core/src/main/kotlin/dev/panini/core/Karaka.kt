package dev.panini.core

/** Semantic roles supplied to an executable dhātu. */
enum class Karaka(val pratipadikas: Set<String>) {
    KARTR(setOf("कर्तृ")),
    KARMAN(setOf("कर्मन्", "कर्म")),
    KARANA(setOf("करण", "करणम्")),
    SAMPRADANA(setOf("सम्प्रदान")),
    APADANA(setOf("अपादान")),
    ADHIKARANA(setOf("अधिकरण")),
    SAMBANDHA(setOf("सम्बन्ध")),
    SAMBODHANA(setOf("सम्बोधन")),
    ANIRDHARITA(emptySet()),
    ;

    companion object {
        fun fromPratipadika(text: String): Karaka? =
            entries.firstOrNull { text.trim() in it.pratipadikas }
    }
}

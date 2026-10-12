package dev.panini.core

/** Prādi identities in segmented source notation, not their sandhi surfaces. */
enum class Upasarga(val upadesha: String) {
    PRA("प्र"), PARA("परा"), APA("अप"), SAM("सम्"), ANU("अनु"), AVA("अव"),
    NIS("निस्"), DUS("दुस्"), VI("वि"), ANG("आङ्"), NI("नि"), ADHI("अधि"),
    API("अपि"), ATI("अति"), SU("सु"), UD("उद्"), ABHI("अभि"), PRATI("प्रति"),
    PARI("परि"), UPA("उप");

    companion object {
        fun fromUpadesha(text: String): Upasarga? = entries.singleOrNull { it.upadesha == text.trim() }
    }
}

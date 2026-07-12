package dev.sanskrit.dhatupatha

/** Small DSL used by each gaṇa file to preserve Dhātupāṭha order. */
class DhatuBuilder(private val gana: Gana) {
    private val dhatus = mutableListOf<Dhatu>()

    fun dhatu(
        id: String,
        krama: Int,
        upadesha: String,
        artha: String,
        arthaHindi: String,
        arthaEnglish: String,
        pada: PadaType? = null,
    ) {
        require(id.isNotBlank()) { "A dhātu id is required." }
        require(krama > 0) { "Dhātu krama must be positive." }
        require(upadesha.isNotBlank()) { "Dhātu upadeśa is required." }
        require(dhatus.none { it.id == id }) { "Duplicate dhātu id in $gana: $id" }
        require(dhatus.none { it.krama == krama }) { "Duplicate dhātu krama in $gana: $krama" }
        dhatus += Dhatu(id, krama, upadesha, artha, arthaHindi, arthaEnglish, gana, pada)
    }

    fun build(): List<Dhatu> = dhatus.toList()
}

fun dhatuPatha(gana: Gana, entries: DhatuBuilder.() -> Unit): List<Dhatu> =
    DhatuBuilder(gana).apply(entries).build()

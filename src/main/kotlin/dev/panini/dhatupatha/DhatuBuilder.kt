package dev.panini.dhatupatha

import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Small DSL used by each gaṇa file to preserve Dhātupāṭha order. */
class DhatuBuilder(private val gana: Gana) {
    private val dhatus = mutableListOf<Dhatu>()

    fun dhatu(
        id: String,
        krama: Int,
        upadesha: String,
        surface: String,
        artha: String,
        arthaHindi: String,
        arthaEnglish: String,
        pada: PadaType? = null,
        itStatus: ItStatus? = null,
        karmatva: Karmatva? = null,
        svara: Accent? = null,
    ) {
        require(id.isNotBlank()) { "A dhātu id is required." }
        require(krama > 0) { "Dhātu krama must be positive." }
        require(upadesha.isNotBlank()) { "Dhātu upadeśa is required." }
        require(dhatus.none { it.id == id }) { "Duplicate dhātu id in $gana: $id" }
        require(dhatus.none { it.krama == krama }) { "Duplicate dhātu krama in $gana: $krama" }
        dhatus += Dhatu(
            id = id,
            krama = krama,
            upadesha = upadesha,
            sourceSurface = surface,
            artha = artha,
            arthaHindi = arthaHindi,
            arthaEnglish = arthaEnglish,
            gana = gana,
            pada = pada,
            itStatus = itStatus,
            karmatva = karmatva,
            svara = svara
        )
    }

    fun build(): List<Dhatu> = dhatus.toList()
}

fun dhatuPatha(gana: Gana, entries: DhatuBuilder.() -> Unit): List<Dhatu> =
    DhatuBuilder(gana).apply(entries).build()

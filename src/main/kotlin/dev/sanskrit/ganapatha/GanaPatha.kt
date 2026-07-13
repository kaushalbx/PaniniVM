package dev.sanskrit.ganapatha

object GanaPatha {
    val all: List<Gana> = GanaPathaData.all

    private val byId: Map<GanaIds, Gana> = all.associateBy { it.id }
    private val byKey: Map<String, Gana> = all.associateBy { it.id.key }

    init {
        require(byId.size == all.size) { "Duplicate gana ids are not allowed." }
        require(byKey.size == all.size) { "Duplicate gana id keys are not allowed." }
    }

    fun get(id: GanaIds): Gana? = byId[id]

    fun get(key: String): Gana? = byKey[key]

    fun require(id: GanaIds): Gana =
        get(id) ?: error("Gana $id is not present in this Gaṇapāṭha.")

    fun require(key: String): Gana =
        get(key) ?: error("Gana $key is not present in this Gaṇapāṭha.")

    fun contains(ganaId: GanaIds, text: String): Boolean {
        return get(ganaId)?.contains(text) == true
    }

    fun ganasContaining(text: String): List<Gana> {
        return all.filter { it.contains(text) }
    }
}

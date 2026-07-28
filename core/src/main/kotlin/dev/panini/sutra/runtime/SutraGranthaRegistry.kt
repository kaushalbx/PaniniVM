package dev.panini.sutra.runtime

data class SutraAddress(
    val grantha: GranthaId,
    val sutra: SutraId,
)

/**
 * Immutable registry for loaded granthas. Imported lookup respects each
 * grantha's explicit export boundary; qualified tooling lookup can inspect the
 * complete loaded graph.
 */
class SutraGranthaRegistry(
    granthas: List<SutraGrantha<*>>,
) {
    val granthas: List<SutraGrantha<*>> = granthas.toList()
    private val byId = this.granthas.associateBy { it.id }
    private val sutras = this.granthas.flatMap { grantha ->
        grantha.sutras.map { SutraAddress(grantha.id, it.id) to it }
    }.toMap()

    init {
        require(byId.size == granthas.size) { "Loaded grantha identities must be unique." }
        require(sutras.size == granthas.sumOf { it.sutras.size }) {
            "Sūtra identities must be unique within each loaded grantha."
        }
    }

    fun grantha(id: GranthaId): SutraGrantha<*>? = byId[id]

    fun sutra(address: SutraAddress): RuntimeSutra<*>? = sutras[address]

    fun requireSutra(address: SutraAddress): RuntimeSutra<*> =
        sutra(address) ?: error("Sūtra ${address.sutra} is not loaded from grantha ${address.grantha}.")

    fun exported(granthaId: GranthaId): List<RuntimeSutra<*>> {
        val grantha = byId[granthaId] ?: return emptyList()
        return grantha.exports.mapNotNull { sutras[SutraAddress(granthaId, it)] }
    }

    fun resolve(
        requester: GranthaId,
        alias: String,
        sutraId: SutraId,
    ): RuntimeSutra<*>? {
        val requestingGrantha = byId[requester] ?: return null
        if (alias == requester.value) {
            return sutras[SutraAddress(requester, sutraId)]
        }
        val imported = requestingGrantha.imports.singleOrNull { it.alias == alias } ?: return null
        val target = byId[imported.grantha] ?: return null
        if (sutraId !in target.exports) return null
        return sutras[SutraAddress(target.id, sutraId)]
    }
}

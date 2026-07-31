package dev.panini.execution.binding

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.TingantaPada

/**
 * Centralized dhātu lookup caches and surface/root resolution helpers.
 * Built lazily once from [DhatuPatha]; no hardcoded stem→root table needed.
 */
internal object DhatuCache {

    /** Maps upadesha → Dhatu for fast फल history lookups. */
    internal val upadeshaDhatuCache: Map<String, Dhatu> by lazy {
        DhatuPatha.all.associateBy { it.upadesha }
    }

    /**
     * Multi-keyed index: maps dhātu id, upadesha, sourceSurface, derivationalSurface,
     * all normalised variants, and all surfaceAliases → canonical Dhatu.
     * Only dhatus that carry at least one operation are indexed.
     */
    private val dhatuCacheMap: Map<String, Dhatu> by lazy {
        val map = mutableMapOf<String, Dhatu>()
        DhatuPatha.all.forEach { dhatu ->
            if (dhatu.operations.isNotEmpty()) {
                map[dhatu.id] = dhatu
                map[dhatu.upadesha] = dhatu
                map[dhatu.sourceSurface] = dhatu
                map[dhatu.derivationalSurface] = dhatu
                map[dhatu.upadesha.normalizeDhatuSurface()] = dhatu
                map[dhatu.sourceSurface.normalizeDhatuSurface()] = dhatu
                map[dhatu.derivationalSurface.normalizeDhatuSurface()] = dhatu
                dhatu.surfaceAliases.forEach { alias ->
                    map[alias] = dhatu
                    map[alias.normalizeDhatuSurface()] = dhatu
                }
            }
        }
        map
    }

    /**
     * Data-driven root index: maps every known dhātu surface form, alias, and
     * operation stem to the canonical root string used for फल resolution.
     * Built once from DhatuPatha; no hardcoded stem→root table needed.
     */
    private val stemToRootCache: Map<String, String> by lazy {
        val map = mutableMapOf<String, String>()
        DhatuPatha.all.forEach { dhatu ->
            val root = dhatu.upadesha.trimEnd('ँ', '्', 'ि', 'र', 'ञ')
            // Index all known surface representations
            sequenceOf(
                dhatu.upadesha,
                dhatu.sourceSurface,
                dhatu.derivationalSurface,
            ).plus(dhatu.surfaceAliases).forEach { surface ->
                if (surface.isNotEmpty()) {
                    map[surface] = root
                    map[surface.normalizeDhatuSurface()] = root
                    // Also index with common prefix/suffix stripped (mirrors getActionRoot cleaning)
                    val cleaned = surface
                        .removePrefix("नि").removePrefix("प्र")
                        .trimEnd('न', 'म', '्', 'अ')
                    if (cleaned.isNotEmpty()) map[cleaned] = root
                }
            }
            // Index every operation name so kridanta-derived nouns resolve correctly
            dhatu.operations.forEach { op ->
                val opCleaned = op.name
                    .removePrefix("नि").removePrefix("प्र")
                    .trimEnd('न', 'म', '्', 'अ')
                if (opCleaned.isNotEmpty()) {
                    map[op.name] = root
                    map[opCleaned] = root
                }
            }
        }
        map
    }

    /** Strips trailing halanta and chandrabindu anusvaras that appear in upadesha forms. */
    internal fun String.normalizeDhatuSurface(): String = trimEnd('्', 'ँ')

    /** Returns the [Dhatu] for [key], or null if not found. */
    internal operator fun get(key: String): Dhatu? = dhatuCacheMap[key]

    /**
     * Resolves the dhātu referred to by [tinganta].
     * Tries exact match first, then a normalised surface match.
     */
    internal fun resolve(tinganta: TingantaPada): Dhatu? {
        val text = tinganta.dhatu.mulaDhatu
        val cached = dhatuCacheMap[text]
        if (cached != null) return cached
        return dhatuCacheMap[text.normalizeDhatuSurface()]
    }

    /**
     * Returns the canonical root string for an action [stem] (e.g. an operation name
     * or pratipadika base), suitable for matching against previous dhātu results.
     */
    internal fun getActionRoot(stem: String): String {
        val clean = stem.removePrefix("नि").removePrefix("प्र").trimEnd('न', 'म', '्', 'अ')
        return stemToRootCache[stem]
            ?: stemToRootCache[clean]
            ?: stemToRootCache[clean.normalizeDhatuSurface()]
            ?: clean
    }

    /**
     * Returns the canonical root string for a dhātu [upadesha],
     * stripping anubandhas before falling back to the cache.
     */
    internal fun getDhatuRoot(upadesha: String): String {
        val clean = upadesha.trimEnd('ँ', '्', 'ि', 'र', 'ञ')
        return stemToRootCache[upadesha]
            ?: stemToRootCache[clean]
            ?: clean
    }
}

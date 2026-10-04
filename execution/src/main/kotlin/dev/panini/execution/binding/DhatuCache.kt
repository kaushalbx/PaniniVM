package dev.panini.execution.binding

import dev.panini.core.DhatuGana
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingantaPada

/** Stable lexical identities for dhātus that carry language-level syntax. */
internal enum class CanonicalDhatuIdentity(val dhatupathaId: String) {
    AS("02.0060"),
    BHU("01.9904"),
    CHI("05.0005"),
    VRJ("02.0022"),
    MUDR("10.0510"),
    YUJ("07.0007"),
    GAN("10.0391"),
    BHAJ("01.1153"),
    DA("03.0010"),
    KRU("08.0010"),
    GRAH("09.0071"),
    KSHIP("06.0005"),
    STHA("01.9901"),
    ;

    companion object {
        private val byDhatupathaId = entries.associateBy(CanonicalDhatuIdentity::dhatupathaId)

        internal fun from(dhatu: Dhatu?): CanonicalDhatuIdentity? =
            dhatu?.id?.let(byDhatupathaId::get)
    }
}

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
                    val cleaned = ActionStemNormalizer.normalize(surface)
                    if (cleaned.isNotEmpty()) map[cleaned] = root
                }
            }
            // Index every operation name so kridanta-derived nouns resolve correctly
            dhatu.operations.forEach { op ->
                val opCleaned = ActionStemNormalizer.normalize(op.name)
                if (opCleaned.isNotEmpty()) {
                    map[op.name] = root
                    map[opCleaned] = root
                }
            }
        }
        map
    }

    /** Strips trailing halanta and chandrabindu anusvaras that appear in upadesha forms. */
    private fun String.normalizeDhatuSurface(): String = trimEnd('्', 'ँ')

    /** Returns the [Dhatu] for [key], or null if not found. */
    internal operator fun get(key: String): Dhatu? = dhatuCacheMap[key]

    /**
     * Resolves the dhātu referred to by [tinganta].
     * Tries exact match first, then a normalised surface match.
     */
    internal fun resolve(tinganta: TingantaPada): Dhatu? {
        val text = tinganta.dhatu.mulaDhatu
        val requiredGana = when (tinganta.vikarana) {
            "शप्" -> DhatuGana.BHVADI
            "श्यन्" -> DhatuGana.DIVADI
            "श्नु" -> DhatuGana.SVADI
            "श्नम्" -> DhatuGana.RUDHADI
            "श्ना" -> DhatuGana.KRYADI
            "उ" -> DhatuGana.TANADI
            "श्नाम्" -> DhatuGana.KRYADI
            "श" -> DhatuGana.TUDADI
            else -> null
        }
        if (requiredGana != null) {
            val candidates = DhatuPatha.all.filter { candidate ->
                candidate.gana == requiredGana &&
                    (candidate.upadesha == text || candidate.sourceSurface == text || candidate.derivationalSurface == text)
            }
            // A gaṇapāṭha row and its executable specialization may share one
            // lexical identity. Explicit vikaraṇa resolves the gaṇa; execution
            // then prefers the single operation-bearing entry within that gaṇa.
            return candidates.filter { it.operations.isNotEmpty() }.singleOrNull()
                ?: candidates.singleOrNull()
        }
        val cached = dhatuCacheMap[text]
        if (cached != null) return cached
        return dhatuCacheMap[text.normalizeDhatuSurface()]
    }

    /** Resolves a non-finite derivation through the same canonical lexicon. */
    internal fun resolve(prakriti: DhatuPrakriti): Dhatu? =
        dhatuCacheMap[prakriti.mulaDhatu]
            ?: dhatuCacheMap[prakriti.mulaDhatu.normalizeDhatuSurface()]

    /**
     * Returns the canonical root string for an action [stem] (e.g. an operation name
     * or pratipadika base), suitable for matching against previous dhātu results.
     */
    internal fun getActionRoot(stem: String): String {
        val clean = ActionStemNormalizer.normalize(stem)
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


internal fun TingantaPada.canonicalDhatuIdentity(): CanonicalDhatuIdentity? =
    CanonicalDhatuIdentity.from(DhatuCache.resolve(this))

internal fun DhatuPrakriti.canonicalDhatuIdentity(): CanonicalDhatuIdentity? =
    CanonicalDhatuIdentity.from(DhatuCache.resolve(this))

internal fun Dhatu.canonicalDhatuIdentity(): CanonicalDhatuIdentity? =
    CanonicalDhatuIdentity.from(this)

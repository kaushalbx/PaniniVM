package dev.panini.execution.binding

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.vyakaranam.ast.DhatuPrakriti
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.lexicalGanaConstraint
import dev.panini.vyakaranam.ast.hasContradictoryActiveCausativeVikarana

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
    HR("01.1046"),
    KSHIP("06.0005"),
    STHA("01.9901"),
    RAM("01.0989"),
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

    /** Source derivations accept roots, not inflected or nominal surface aliases. */
    private val sourceRootCache: Map<String, List<Dhatu>> by lazy {
        DhatuPatha.all.filter { it.operations.isNotEmpty() }.flatMap { dhatu ->
            listOf(dhatu.upadesha, dhatu.sourceSurface, dhatu.derivationalSurface)
                .flatMap { root -> listOf(root, root.normalizeDhatuSurface()) }
                .distinct().map { root -> root to dhatu }
        }.groupBy({ it.first }, { it.second })
    }

    /** Exact upadeśas can identify grammatical roots without VM operations. */
    private val lexicalUpadeshas: Map<String, List<Dhatu>> by lazy {
        DhatuPatha.all.groupBy { it.upadesha }.mapValues { (_, entries) -> entries.distinctBy { it.id } }
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
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        if (tinganta.hasContradictoryActiveCausativeVikarana()) return null
        val text = tinganta.dhatu.mulaDhatu
        val requiredGana = tinganta.lexicalGanaConstraint()
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
        return resolveSource(text) ?: defaultVikaranaRoot(tinganta, text)
    }

    /**
     * A homonym written with no vikaraṇa takes the general शप् of 3.1.68, so its bhvādi entry, among those
     * compatible with the written pada. It stays unresolved if that entry is absent or if the homonyms differ in
     * karmatva, because then the choice would change the analysis: दुह् is दोग्धि, not bhvādi दोहति.
     */
    private fun defaultVikaranaRoot(tinganta: TingantaPada, text: String): Dhatu? {
        val pada = tinganta.ting.affix?.pada
        val active = tinganta.vikarana != dev.panini.vyakaranam.ast.Vikarana.YAK && tinganta.dhatu.sanadiAffixes.isEmpty()
        val candidates = DhatuPatha.all.filter { it.upadesha == text || it.sourceSurface == text || it.derivationalSurface == text }
            // A curādi root requires ṇic (3.1.25), so a form without it cannot be curādi.
            .filter { it.gana != dev.panini.core.DhatuGana.CURADI || tinganta.dhatu.sanadiAffixes.isNotEmpty() }
            .filter { !active || pada == null || it.pada == dev.panini.core.PadaType.UBHAYAPADA || it.pada == pada }
        // Repeated listings of one upadeśa in one gaṇa (जि) are one root for this choice.
        val oneRoot = candidates.all { it.gana == dev.panini.core.DhatuGana.BHVADI } && candidates.map { it.upadesha }.distinct().size == 1
        if (!oneRoot && candidates.map { it.karmatva }.distinct().size != 1) return null
        return candidates.filter { it.gana == dev.panini.core.DhatuGana.BHVADI }
            .minWithOrNull(compareBy({ it.karmatva == dev.panini.shiksha.Karmatva.AKARMAKA }, { it.krama }))
    }

    /** Resolves a non-finite derivation through the same canonical lexicon. */
    internal fun resolve(prakriti: DhatuPrakriti): Dhatu? {
        dev.panini.dhatupatha.DhatuPathaRegistration.ensureRegistered()
        return resolveSource(prakriti.mulaDhatu)
    }

    private fun resolveSource(text: String): Dhatu? {
        val executable = sourceCandidates(text)
        // Preserve collisions in the executable index. An exact non-executable
        // upadeśa is a lexical fallback, not an operation-selection tiebreaker.
        if (executable.isNotEmpty()) return executable.singleOrNull()
        lexicalUpadeshas[text]?.singleOrNull()?.let { return it }
        // गम् names गमॢँ when the Dhātupāṭha has one such root; homonyms stay unresolved.
        return DhatuPatha.all.filter { it.sourceSurface == text || it.derivationalSurface == text }
            .distinctBy { it.id }.singleOrNull()
    }

    private fun sourceCandidates(text: String): List<Dhatu> =
        (sourceRootCache[text] ?: sourceRootCache[text.normalizeDhatuSurface()]).orEmpty()
            .distinctBy { it.id }

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

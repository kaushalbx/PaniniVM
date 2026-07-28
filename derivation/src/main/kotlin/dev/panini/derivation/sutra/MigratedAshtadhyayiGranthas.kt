package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdengGunaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.IkoGunaVrddhiSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.HaloAnantarahSamyogahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.MukhanasikavacanoAnunasikahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TulyasyaprayatnamSavarnamSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.VrddhirAdaicSutra
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraGranthaRegistry
import dev.panini.sutra.runtime.SutraId

/**
 * First Aṣṭādhyāyī slice owned by the shared runtime-grantha architecture.
 * The original rule objects remain the grammatical implementation while this
 * grantha makes their identities, meanings, ordering, and exports inspectable.
 */
object OpeningSamjnaRuntimeGrantha {
    val id: GranthaId = GranthaId("ashtadhyayi-1.1.1-1.1.3")

    private val rules: List<DerivationSutra> = listOf(
        VrddhirAdaicSutra,
        AdengGunaSutra,
        IkoGunaVrddhiSutra,
    )
    private val localIds: Set<SutraId> = rules.mapTo(linkedSetOf()) {
        SutraId(it.sutra)
    }

    val grantha: SutraGrantha<DerivationAvastha> = SutraGrantha(
        id = id,
        sutras = rules.map {
            DerivationSutraRuntimeAdapter.adapt(it, localIds)
        },
        exports = localIds,
    )
}

/** Independent phonological saṃjñās 1.1.7–1.1.9. */
object PhonologicalSamjnaRuntimeGrantha {
    val id: GranthaId = GranthaId("ashtadhyayi-1.1.7-1.1.9")

    private val rules: List<DerivationSutra> = listOf(
        HaloAnantarahSamyogahSutra,
        MukhanasikavacanoAnunasikahSutra,
        TulyasyaprayatnamSavarnamSutra,
    )
    private val localIds: Set<SutraId> = rules.mapTo(linkedSetOf()) {
        SutraId(it.sutra)
    }

    val grantha: SutraGrantha<DerivationAvastha> = SutraGrantha(
        id = id,
        sutras = rules.map {
            DerivationSutraRuntimeAdapter.adapt(it, localIds)
        },
        exports = localIds,
    )
}

/** Discoverable registry of Aṣṭādhyāyī slices migrated to runtime granthas. */
object MigratedAshtadhyayiGranthas {
    val granthas: List<SutraGrantha<DerivationAvastha>> = listOf(
        OpeningSamjnaRuntimeGrantha.grantha,
        PhonologicalSamjnaRuntimeGrantha.grantha,
    )

    val registry: SutraGranthaRegistry = SutraGranthaRegistry(granthas)
}

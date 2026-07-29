package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.runtime.AshtadhyayiCompiler
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.toBlueprint

/**
 * Runtime view of the migrated rules in the authoritative Aṣṭādhyāyī registry.
 *
 * The catalog owns rule identity and ordering; this object supplies the common
 * runtime package boundary while legacy rule implementations are being migrated.
 */
object AshtadhyayiRuntimeGrantha {
    private val rules = Ashtadhyayi.runtimeSutras
    private val localIds = rules.mapTo(linkedSetOf()) { SutraId(it.number) }

    val grantha: SutraGrantha<DerivationAvastha> = SutraGrantha(
        id = GranthaId("ashtadhyayi"),
        sutras = rules.map { sutra ->
            sutra.artha?.let { artha ->
                AshtadhyayiCompiler.compile(
                    sutra.toBlueprint(artha.toSutraArtha()),
                )
            } ?: DerivationSutraRuntimeAdapter.adapt(
                sutra as DerivationSutra,
                localIds,
            )
        },
        exports = localIds,
    )
}

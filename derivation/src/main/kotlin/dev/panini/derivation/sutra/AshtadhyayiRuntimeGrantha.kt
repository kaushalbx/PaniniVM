package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.Sutra
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
    private val rules = Ashtadhyayi.executableSutras
    private val localIds = rules.mapTo(linkedSetOf()) { SutraId(it.sutra) }

    val grantha: SutraGrantha<DerivationAvastha> = SutraGrantha(
        id = GranthaId("ashtadhyayi"),
        sutras = rules.map { rule ->
            val sutra = rule as Sutra<*, *>
            sutra.artha?.let { artha ->
                DerivationBlueprintCompiler.compile(
                    sutra.toBlueprint(artha.toSutraArtha()),
                )
            } ?: DerivationSutraRuntimeAdapter.adapt(rule, localIds)
        },
        exports = localIds,
    )
}

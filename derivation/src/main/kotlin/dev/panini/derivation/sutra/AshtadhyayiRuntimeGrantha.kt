package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraGrantha
import dev.panini.sutra.runtime.SutraId

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
        sutras = rules.map { AshtadhyayiSutraLowerer.lower(it, localIds) },
        exports = localIds,
    )
}

package dev.panini.derivation.sutra

import dev.panini.ashtadhyayi.runtime.AshtadhyayiCompiler
import dev.panini.sutra.runtime.RuntimeSutra
import dev.panini.sutra.runtime.SutraBlueprint

/** Temporary source-compatible facade for the Aṣṭādhyāyī-owned compiler. */
object DerivationBlueprintCompiler {
    fun compile(blueprint: SutraBlueprint): RuntimeSutra<DerivationAvastha> =
        AshtadhyayiCompiler.compile(blueprint)
}

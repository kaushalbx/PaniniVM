package dev.panini.analysis

import dev.panini.core.KrtAffix
import dev.panini.vyakaranam.ast.*

/** An explicitly selected action pursued by the enclosing action (3.3.10 domain). */
sealed interface PurposeActionGovernor {
    val id: String
    val dhatu: DhatuPrakriti
    val upasargas: List<String>

    data class Implied(override val id: String, override val dhatu: DhatuPrakriti,
        override val upasargas: List<String> = emptyList()) : PurposeActionGovernor {
        init { require(id.isNotBlank()) }
    }

    data class Written(override val id: String, val pada: AvyayaPada) : PurposeActionGovernor {
        init {
            require(id.isNotBlank())
            require((pada.derivation as? AvyayaKridantaDerivation)?.krtAffix == KrtAffix.TUMUN) {
                "A written purpose action must retain its tumun derivation."
            }
        }
        private val derivation get() = pada.derivation as AvyayaKridantaDerivation
        override val dhatu get() = derivation.dhatu
        override val upasargas get() = derivation.upasargas
    }
}

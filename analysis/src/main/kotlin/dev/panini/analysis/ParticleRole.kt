package dev.panini.analysis

import dev.panini.vyakaranam.ast.AvyayaPada

enum class ParticleRole { EMPHASIS, INCLUSION, PAST, COMPARISON, NAME, CONJUNCTION, DISJUNCTION, VOCATIVE, NEGATION, CONCESSION }

/** What a written particle contributes, by its identity. Its scope is not decided here: एव may fall on any word. */
val KriyaFrame.particleRoles: Map<AvyayaPada, ParticleRole>
    get() = vakya.padas.filterIsInstance<AvyayaPada>().mapNotNull { pada ->
        // यदि + अपि and तथा + अपि: अपि after यदि or तथा marks concession.
        val members = (pada.derivation as? dev.panini.vyakaranam.ast.NipataSamuccayaDerivation)?.members
        if (members != null) return@mapNotNull (if (members.last() == "अपि" && members.first() in setOf("यदि", "तथा"))
            ParticleRole.CONCESSION else null)?.let { pada to it }
        when (pada.form) {
            "एव" -> ParticleRole.EMPHASIS
            "अपि" -> ParticleRole.INCLUSION
            "स्म" -> ParticleRole.PAST
            "इव" -> ParticleRole.COMPARISON
            "नाम" -> ParticleRole.NAME
            "च" -> ParticleRole.CONJUNCTION
            "वा" -> ParticleRole.DISJUNCTION
            "भो", "हे", "अयि", "अरे" -> ParticleRole.VOCATIVE
            "न", "नहि", "मा" -> ParticleRole.NEGATION
            else -> null
        }?.let { pada to it }
    }.toMap()

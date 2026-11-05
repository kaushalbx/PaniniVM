package dev.panini.analysis

import dev.panini.core.KrtAffix
import dev.panini.vyakaranam.ast.AvyayaKridantaDerivation
import dev.panini.vyakaranam.ast.AvyayaPada

enum class InfinitiveRole { OBJECT_OF_DESIRE, COMPLEMENT, PURPOSE }

private val complement = setOf("शक्", "शकँ", "शकॢँ", "धृष्", "ज्ञा", "ग्लै", "ग्ला", "घट्", "रभ्", "लभ्", "क्रम्", "सह्", "अर्ह्", "अस्", "असँ", "भू", "भूँ", "विद्")

/** What a tumun form is to its finite verb: desire (3.3.158), शक् and its class (3.4.65), otherwise purpose (3.3.10). */
val KriyaFrame.infinitiveRoles: Map<AvyayaPada, InfinitiveRole>
    get() {
        val written = kriya?.analysis?.pada?.dhatu?.mulaDhatu
        val names = setOfNotNull(written, kriya?.dhatu?.sourceSurface, kriya?.dhatu?.upadesha)
        val role = when {
            kriya?.dhatu.hasArtha(DhatuArthaClass.DESIRE) == true -> InfinitiveRole.OBJECT_OF_DESIRE
            names.any { it in complement } -> InfinitiveRole.COMPLEMENT
            // An unresolved root cannot be said to be outside those classes.
            kriya?.dhatu == null -> return emptyMap()
            else -> InfinitiveRole.PURPOSE
        }
        return vakya.padas.filterIsInstance<AvyayaPada>().filter {
            (it.derivation as? AvyayaKridantaDerivation)?.krtAffix == KrtAffix.TUMUN
        }.associateWith { role }
    }

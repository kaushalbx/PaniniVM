package dev.panini.dhatupatha.juhotyadi

import dev.panini.actions.io.SanskritEmitAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Juhotyādi dhātu हु दानादनयोः. */
class HuDhatu : Dhatu(
    id = "03.9901",
    krama = 9901,
    upadesha = "हु",
    sourceSurface = "हु",
    artha = "दानादनयोः",
    arthaHindi = "देना, हवन करना, अर्पण करना",
    arthaEnglish = "to offer, to give, to emit, to sacrifice",
    gana = DhatuGana.JUHOTYADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SanskritEmitAction.op {
            requires(Karaka.KARMAN)
            returns(ExecutionSamjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("जुहोति", "हु", "अर्पणम्"),
)

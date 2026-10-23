package dev.panini.dhatupatha.rudhadi

import dev.panini.actions.collection.ListDistinctAction
import dev.panini.actions.numeric.ModuloAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.core.Upasarga
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.numericOp
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.shiksha.Samjna

/** Executable Rudhādi dhātu शिषॢँ विशेषणे (शेषे). */
class ShishDhatu : Dhatu(
    id = "07.0014",
    krama = 14,
    upadesha = "शिषॢँ",
    sourceSurface = "शिष्",
    artha = "विशेषणे",
    arthaHindi = "विशेषता बताना, भिन्नता दिखाना, शेष बचना",
    arthaEnglish = "to distinguish, to characterize, to remain as remainder",
    gana = DhatuGana.RUDHADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        ModuloAction.numericOp(),
        ListDistinctAction.op {
            triggeredBy(requiredUpasargaIdentities = setOf(Upasarga.VI))
            requires(Karaka.KARMAN)
            returns(Samjna.GANA)
        },
    ),
)

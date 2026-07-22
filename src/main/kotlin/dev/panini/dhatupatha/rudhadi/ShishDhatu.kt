package dev.panini.dhatupatha.rudhadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.core.Karaka
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

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
) {

}

package dev.panini.dhatupatha.bhvadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.core.Karaka
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu दृशिँर् प्रेक्षणे. */
class DrshDhatu : Dhatu(
    id = "01.1143",
    krama = 1143,
    upadesha = "दृशिँर्",
    sourceSurface = "दृश्",
    artha = "प्रेक्षणे",
    arthaHindi = "देखना, पश्य करना, निरूपण करना",
    arthaEnglish = "to see, to inspect, to query context variable",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {

}

package dev.panini.dhatupatha.bhvadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.core.Karaka
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu भजँ सेवायाम् (विभागे / त्रैराशिके). */
class BhajDhatu : Dhatu(
    id = "01.1153",
    krama = 1153,
    upadesha = "भजँ",
    sourceSurface = "भज्",
    artha = "सेवायाम्",
    arthaHindi = "भजना, भाग करना, अंश निकालना",
    arthaEnglish = "to serve, to partition, to compute fraction/ratio",
    gana = DhatuGana.BHVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {

}

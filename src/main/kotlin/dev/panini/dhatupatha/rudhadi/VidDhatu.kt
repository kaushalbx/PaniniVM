package dev.panini.dhatupatha.rudhadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.core.Karaka
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Rudhādi dhātu विदँ विचारणे. */
class VidDhatu : Dhatu(
    id = "07.0013",
    krama = 13,
    upadesha = "विदँ",
    sourceSurface = "विद्",
    artha = "विचारणे",
    arthaHindi = "मनन करना, विचार करना, तुलना करना",
    arthaEnglish = "to think, to analyze, to compare",
    gana = DhatuGana.RUDHADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {

}

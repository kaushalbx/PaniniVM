package dev.panini.dhatupatha.bhvadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu गमॢँ गताौ. */
class GamDhatu : Dhatu(
    id = "01.1137",
    krama = 1137,
    upadesha = "गमॢँ",
    sourceSurface = "गम्",
    artha = "गतौ",
    arthaHindi = "जाना, गमन करना",
    arthaEnglish = "to go, to move",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
)

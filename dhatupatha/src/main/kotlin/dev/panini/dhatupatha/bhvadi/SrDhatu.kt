package dev.panini.dhatupatha.bhvadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class SrDhatu : Dhatu(
    id = "01.1085",
    krama = 1085,
    upadesha = "सृ",
    sourceSurface = "सृ",
    artha = "गतौ",
    arthaHindi = "जाना, सरकना",
    arthaEnglish = "to go,to move,to approach,to slip",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA
)

package dev.panini.dhatupatha.bhvadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class ShruDhatu : Dhatu(
    id = "01.1092",
    krama = 1092,
    upadesha = "श्रु",
    sourceSurface = "श्रु",
    artha = "श्रवणे",
    arthaHindi = "सुनना, श्रवण करना",
    arthaEnglish = "to hear, to listen",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA
)

package dev.panini.dhatupatha.bhvadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class SvadDhatu : Dhatu(
    id = "01.0018",
    krama = 18,
    upadesha = "ष्वदँ",
    sourceSurface = "स्वद्",
    artha = "आस्वादने",
    arthaHindi = "स्वाद लेना, चखना",
    arthaEnglish = "to taste, to eat,to please the tongue, to have delight",
    gana = DhatuGana.BHVADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA
)

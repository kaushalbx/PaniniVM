package dev.panini.dhatupatha.svadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class VrDhatu : Dhatu(
    id = "05.0008",
    krama = 8,
    upadesha = "वृञ्",
    sourceSurface = "वृ",
    artha = "वरणे",
    arthaHindi = "पसंद करना, नियोजित करना, नियमित करना",
    arthaEnglish = "to choose,to select,to marry,to finalize",
    gana = DhatuGana.SVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA
)

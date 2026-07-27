package dev.panini.dhatupatha.tudadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class GrDhatu : Dhatu(
    id = "06.0146",
    krama = 146,
    upadesha = "गॄ",
    sourceSurface = "गॄ",
    artha = "निगरणे",
    arthaHindi = "खाना, निगलना",
    arthaEnglish = "to eat, to swallow",
    gana = DhatuGana.TUDADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA
)

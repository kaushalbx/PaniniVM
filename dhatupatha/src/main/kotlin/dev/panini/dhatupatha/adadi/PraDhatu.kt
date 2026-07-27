package dev.panini.dhatupatha.adadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class PraDhatu : Dhatu(
    id = "02.0056",
    krama = 56,
    upadesha = "प्रा",
    sourceSurface = "प्रा",
    artha = "पूरणे",
    arthaHindi = "भरना, तृप्त होना",
    arthaEnglish = "to fill, to be satisfied, to be content",
    gana = DhatuGana.ADADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA
)

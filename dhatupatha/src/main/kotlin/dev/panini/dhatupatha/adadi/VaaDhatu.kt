package dev.panini.dhatupatha.adadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

class VaaDhatu : Dhatu(
    id = "02.0045",
    krama = 45,
    upadesha = "वा",
    sourceSurface = "वा",
    artha = "गतिगन्धनयोः",
    arthaHindi = "जाना, हवा की तरह तीव्र गति में चलना, बहना, गंध लगना",
    arthaEnglish = "to go,to blow, to move rapidly",
    gana = DhatuGana.ADADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA
)

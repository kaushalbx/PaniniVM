package dev.panini.dhatupatha.svadi

import dev.panini.actions.numeric.RandomChoiceAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.shiksha.Samjna

/** Executable Svādi dhātu चिञ् चयने. */
class ChiDhatu : Dhatu(
    id = "05.0005",
    krama = 5,
    upadesha = "चिञ्",
    sourceSurface = "चि",
    artha = "चयने",
    arthaHindi = "चुनना, बटोरना, एकत्र करना",
    arthaEnglish = "to collect, to select, to pick",
    gana = DhatuGana.SVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.DVIKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        RandomChoiceAction.op {
            requires(Karaka.APADANA)
            requires(Karaka.ADHIKARANA)
            requires(Karaka.KARMAN)
            returns(Samjna.SANKHYA)
        },
    ),
)

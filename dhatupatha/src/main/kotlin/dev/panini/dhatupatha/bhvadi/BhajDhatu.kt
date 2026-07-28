package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.collection.ListSliceAction
import dev.panini.actions.numeric.DivisionAction
import dev.panini.actions.numeric.FractionAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.numericOp
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.shiksha.Samjna

/** Executable Bhvādi dhātu भजँ सेवायाम् (विभागे / त्रैराशिके). */
class BhajDhatu : Dhatu(
    id = "01.1153",
    krama = 1153,
    upadesha = "भजँ",
    sourceSurface = "भज्",
    artha = "सेवायाम्",
    arthaHindi = "भजना, भाग करना, अंश निकालना",
    arthaEnglish = "to serve, to partition, to compute fraction/ratio",
    gana = DhatuGana.BHVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        DivisionAction.numericOp {
            triggeredBy(requiredUpasargas = setOf("वि"))
        },
        FractionAction.numericOp {
            triggeredBy(requiredUpasargas = setOf("वि"))
        },
        ListSliceAction.op {
            requires(Karaka.KARMAN) // list
            requires(Karaka.KARANA) // start index
            requires(Karaka.SAMPRADANA) // end index
            returns(Samjna.GANA)
        },
    ),
)

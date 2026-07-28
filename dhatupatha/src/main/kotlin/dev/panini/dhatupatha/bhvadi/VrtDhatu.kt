package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.control.SanskritLoopAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu वृताँ वर्तने. */
class VrtDhatu : Dhatu(
    id = "01.9910",
    krama = 9910,
    upadesha = "वृताँ",
    sourceSurface = "वृत्",
    artha = "वर्तने",
    arthaHindi = "वर्तना, घूमना, पुनरावृत्ति करना",
    arthaEnglish = "to turn, to exist, to repeat/loop",
    gana = DhatuGana.BHVADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SanskritLoopAction.op {
            requires(Karaka.KARMAN) // loop count
            requires(Karaka.KARANA) // target action name
            returns(Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("वर्तते", "वृत्", "अनुवृत्तिः", "पुनरावृत्तिः"),
)

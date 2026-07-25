package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.state.SanskritStateWaitAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu स्थाञँ गतिनिवृत्तौ. */
class SthaDhatu : Dhatu(
    id = "01.9901",
    krama = 9901,
    upadesha = "स्थाञँ",
    sourceSurface = "स्था",
    artha = "गतिनिवृत्तौ",
    arthaHindi = "रुकना, ठहरना, रहना, खड़ा होना",
    arthaEnglish = "to stand, wait, pause, remain, stop motion",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SanskritStateWaitAction.op {
            requires(Karaka.KARMAN)
            returns(ExecutionSamjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("तिष्ठति", "तिष्ठ", "स्थानम्", "स्थितिः"),
)

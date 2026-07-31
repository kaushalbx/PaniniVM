package dev.panini.dhatupatha.curadi

import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExecutionEffect
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/**
 * Executable Curādi extension for external capability dispatch.
 */
class PreshDhatu : Dhatu(
    id = "10.0509",
    krama = 509,
    upadesha = "प्रेषँ",
    sourceSurface = "प्रेष्",
    artha = "प्रेषणे",
    arthaHindi = "भेजना, प्रेषित करना",
    arthaEnglish = "to send, to dispatch, to invoke external capability",
    gana = DhatuGana.CURADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        dev.panini.actions.external.BahyaSendAction.op {
            requires(Karaka.KARMAN)
            effects(ExecutionEffect.NETWORK, ExecutionEffect.EXECUTE_PROCESS, ExecutionEffect.SEND_MESSAGE)
            returns(Samjna.SHABDA)
        },
    ),
)

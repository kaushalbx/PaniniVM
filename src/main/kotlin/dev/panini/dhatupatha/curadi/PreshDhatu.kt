package dev.panini.dhatupatha.curadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.execution.BahyaSendAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionSamjna
import dev.panini.core.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/**
 * 01.1049 प्रेषँ (प्रेषणे / प्र-इष्) - External system capability dispatch.
 */
class PreshDhatu : Dhatu(
    id = "01.1049",
    krama = 1049,
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
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "बाह्यप्रेषणम्",
            description = "Dispatches command or message to external system capability.",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                    )
                )
            ),
            effects = setOf(ExecutionEffect.NETWORK, ExecutionEffect.EXECUTE_PROCESS, ExecutionEffect.SEND_MESSAGE),
            action = BahyaSendAction,
            resultSamjnas = setOf(ExecutionSamjna.SHABDA),
        )
    )
}

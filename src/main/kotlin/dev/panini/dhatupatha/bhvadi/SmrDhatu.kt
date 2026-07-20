package dev.panini.dhatupatha.bhvadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.Gana
import dev.panini.dhatupatha.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionEffect
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SmritiLoadAction
import dev.panini.execution.SmritiSaveAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/**
 * 01.0601 स्मृँ (आध्याने) - Context persistence and state memory retrieval.
 */
class SmrDhatu : Dhatu(
    id = "01.0601",
    krama = 601,
    upadesha = "स्मृँ",
    sourceSurface = "स्मृ",
    artha = "आध्याने",
    arthaHindi = "याद करना, स्मरण रखना",
    arthaEnglish = "to remember, to keep in mind, to retain context",
    gana = Gana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "स्मृतिरक्षणम्",
            description = "Saves active context state to persistent storage.",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                    )
                )
            ),
            effects = setOf(ExecutionEffect.WRITE_RESOURCE),
            action = SmritiSaveAction,
            resultSamjnas = setOf(ExecutionSamjna.SHABDA),
        ),
        DhatuOperation(
            id = "स्मृतिपुनर्प्राप्तिः",
            description = "Restores context state from persistent storage.",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                    )
                )
            ),
            effects = setOf(ExecutionEffect.READ_RESOURCE),
            action = SmritiLoadAction,
            resultSamjnas = setOf(ExecutionSamjna.SHABDA),
        ),
    )
}

package dev.panini.dhatupatha.juhotyadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionSamjna
import dev.panini.core.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SanskritVariableAssignAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Juhotyādi dhātu डुदाञ् दाने. */
class DaDhatu : Dhatu(
    id = "03.0010",
    krama = 10,
    upadesha = "डुदाञ्",
    sourceSurface = "दा",
    artha = "दाने",
    arthaHindi = "देना, सौंपना, मूल्य का संविभाजन करना",
    arthaEnglish = "to give, to assign, to bind variable value",
    gana = DhatuGana.JUHOTYADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "मूल्यदानम्",
            description = "मूल्यस्य दानम् / संविभाजनम् (Variable Assignment)",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                    )
                )
            ),
            action = SanskritVariableAssignAction,
            resultSamjnas = setOf(ExecutionSamjna.SHABDA),
        )
    )
}

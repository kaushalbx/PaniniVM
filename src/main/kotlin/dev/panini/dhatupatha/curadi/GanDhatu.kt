package dev.panini.dhatupatha.curadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.Gana
import dev.panini.dhatupatha.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SanskritAverageAction
import dev.panini.execution.SanskritCountingAction
import dev.panini.execution.SanskritMultiplicationAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Curādi dhātu गण संख्याने. */
class GanDhatu : Dhatu(
    id = "10.0391",
    krama = 391,
    upadesha = "गण",
    sourceSurface = "गण",
    artha = "सङ्ख्याने",
    arthaHindi = "गिनना, गुणा करना",
    arthaEnglish = "to count, to enumerate, to multiply",
    gana = Gana.CURADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "सङ्ख्यागुणनम्",
            description = "सङ्ख्यानां गुणनम्",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 2,
                        shape = ExpressionShape.COORDINATION,
                        memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                    )
                )
            ),
            action = SanskritMultiplicationAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        ),
        DhatuOperation(
            id = "सङ्ख्यागणनम्",
            description = "पदार्थानां / सङ्ख्यानां गणनम् (सङ्ख्यानम्)",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                        shape = ExpressionShape.COORDINATION,
                    )
                )
            ),
            action = SanskritCountingAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        ),
        DhatuOperation(
            id = "सङ्ख्यासाम्यम्",
            description = "सङ्ख्यानां साम्यम् (माध्यमम् / Average)",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                        shape = ExpressionShape.COORDINATION,
                        memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                    )
                )
            ),
            action = SanskritAverageAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        ),
    )
}

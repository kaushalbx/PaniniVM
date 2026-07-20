package dev.panini.dhatupatha.rudhadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.Gana
import dev.panini.dhatupatha.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SanskritComparisonAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Rudhādi dhātu विदँ विचारणे. */
class VidDhatu : Dhatu(
    id = "07.0013",
    krama = 13,
    upadesha = "विदँ",
    sourceSurface = "विद्",
    artha = "विचारणे",
    arthaHindi = "मनन करना, विचार करना, तुलना करना",
    arthaEnglish = "to think, to analyze, to compare",
    gana = Gana.RUDHADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "सङ्ख्यातुलना",
            description = "सङ्ख्यानां विचारः तुलना च (Comparison / Max)",
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
            action = SanskritComparisonAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        )
    )
}

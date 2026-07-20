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
import dev.panini.execution.SanskritModuloAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Rudhādi dhātu शिषॢँ विशेषणे (शेषे). */
class ShishDhatu : Dhatu(
    id = "07.0014",
    krama = 14,
    upadesha = "शिषॢँ",
    sourceSurface = "शिष्",
    artha = "विशेषणे",
    arthaHindi = "विशेषता बताना, भिन्नता दिखाना, शेष बचना",
    arthaEnglish = "to distinguish, to characterize, to remain as remainder",
    gana = Gana.RUDHADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "सङ्ख्याशेषः",
            description = "सङ्ख्याविभाजनात् शेषः (Modulo)",
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
            action = SanskritModuloAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        )
    )
}

package dev.panini.dhatupatha.bhvadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionSamjna
import dev.panini.core.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SanskritVariableInspectAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu दृशिँर् प्रेक्षणे. */
class DrshDhatu : Dhatu(
    id = "01.1143",
    krama = 1143,
    upadesha = "दृशिँर्",
    sourceSurface = "दृश्",
    artha = "प्रेक्षणे",
    arthaHindi = "देखना, पश्य करना, निरूपण करना",
    arthaEnglish = "to see, to inspect, to query context variable",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "मूल्यदर्शनम्",
            description = "मूल्यस्य दर्शनम् / प्रेक्षणम् (Variable Inspection / Query)",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                    )
                )
            ),
            action = SanskritVariableInspectAction,
            resultSamjnas = setOf(ExecutionSamjna.SHABDA),
        )
    )
}

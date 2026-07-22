package dev.panini.dhatupatha.bhvadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.ExpressionShape
import dev.panini.core.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SanskritFractionAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu भजँ सेवायाम् (विभागे / त्रैराशिके). */
class BhajDhatu : Dhatu(
    id = "01.1153",
    krama = 1153,
    upadesha = "भजँ",
    sourceSurface = "भज्",
    artha = "सेवायाम्",
    arthaHindi = "भजना, भाग करना, अंश निकालना",
    arthaEnglish = "to serve, to partition, to compute fraction/ratio",
    gana = DhatuGana.BHVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "सङ्ख्याभागः",
            description = "सङ्ख्यानां भागः त्रैराशिकं वा (Fraction/Proportion)",
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
            action = SanskritFractionAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        )
    )
}

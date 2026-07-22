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
import dev.panini.execution.SanskritExponentiationAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu वृधुँ वृद्धौ. */
class VridhDhatu : Dhatu(
    id = "01.0863",
    krama = 863,
    upadesha = "वृधुँ",
    sourceSurface = "वृध्",
    artha = "वृद्धौ",
    arthaHindi = "बढ़ना, वृद्धि करना, घात करना",
    arthaEnglish = "to grow, to increase, to elevate, to raise to power",
    gana = DhatuGana.BHVADI,
    pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.AKARMAKA,
    svara = Accent.UDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "सङ्ख्याघातः",
            description = "सङ्ख्यायाः घातवर्धनम् (Exponentiation)",
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
            action = SanskritExponentiationAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        )
    )
}

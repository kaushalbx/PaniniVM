package dev.panini.dhatupatha.bhvadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionSamjna
import dev.panini.core.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SanskritSquareRootAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu मूलँ प्रतिष्ठायाम् (वर्गमूले). */
class MulDhatu : Dhatu(
    id = "01.0607",
    krama = 607,
    upadesha = "मूलँ",
    sourceSurface = "मूल्",
    artha = "प्रतिष्ठायाम्",
    arthaHindi = "जड़ जमाना, वर्गमूल निकालना",
    arthaEnglish = "to root, to establish, to compute square root",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.AKARMAKA,
    svara = Accent.UDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "सङ्ख्यामूलम्",
            description = "सङ्ख्यायाः वर्गमूलम् (Square Root)",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                        memberSamjnas = setOf(ExecutionSamjna.SANKHYA),
                    )
                )
            ),
            action = SanskritSquareRootAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        )
    )
}

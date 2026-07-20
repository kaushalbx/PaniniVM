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
import dev.panini.execution.SanskritAdditionAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Rudhādi धātu युजिँर् योगे. */
class YujirDhatu : Dhatu(
    id = "07.0007",
    krama = 7,
    upadesha = "युजिँर्",
    sourceSurface = "युज्",
    artha = "योगे",
    arthaHindi = "जुड़ना, मिलाप करना,एकत्र करना",
    arthaEnglish = "to bind,to restrain,to join,to unite,to apply, to combine",
    gana = Gana.RUDHADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "सङ्ख्यायोजनम्",
            description = "सङ्ख्यानां योगः",
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
            action = SanskritAdditionAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        )
    )
}

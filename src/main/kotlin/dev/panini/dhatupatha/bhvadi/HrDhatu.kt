package dev.panini.dhatupatha.bhvadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.Gana
import dev.panini.dhatupatha.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SanskritDivisionAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Bhvādi dhātu हृञ् हरणे. */
class HrDhatu : Dhatu(
    id = "01.1046",
    krama = 1046,
    upadesha = "हृञ्",
    sourceSurface = "हृ",
    artha = "हरणे",
    arthaHindi = "ले जाना, हरण करना, चोरी करना",
    arthaEnglish = "to take away, to carry, to steal, to acquire, to divide",
    gana = Gana.BHVADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.DVIKARMAKA,
    svara = Accent.ANUDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "सङ्ख्याहरणम्",
            description = "सङ्ख्यानां हरणम् (विभाजनम्)",
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
            action = SanskritDivisionAction,
            resultSamjnas = setOf(ExecutionSamjna.SANKHYA),
        )
    )
}

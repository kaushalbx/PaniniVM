package dev.panini.dhatupatha.tanadi

import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.Gana
import dev.panini.dhatupatha.PadaType
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.Karaka
import dev.panini.execution.KarakaRequirement
import dev.panini.execution.OperationSignature
import dev.panini.execution.SanskritSandhiAction
import dev.panini.execution.SanskritSubantaDerivationAction
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Tanādi dhātu डुकृञ् करणे. */
class KruDhatu : Dhatu(
    id = "08.0010",
    krama = 10,
    upadesha = "डुकृञ्",
    sourceSurface = "कृ",
    artha = "करणे",
    arthaHindi = "करना, रचना करना, रूपनिष्पत्ति करना",
    arthaEnglish = "to do, to act, to make, to transform, to join sandhi",
    gana = Gana.TANADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
) {
    override val operations: List<DhatuOperation> = listOf(
        DhatuOperation(
            id = "संहिताकरणम्",
            description = "पदानां संहिताकरणम् (सन्धियोगः)",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 2,
                        shape = ExpressionShape.COORDINATION,
                    )
                )
            ),
            action = SanskritSandhiAction,
            resultSamjnas = setOf(ExecutionSamjna.SHABDA),
        ),
        DhatuOperation(
            id = "पदनिष्पत्तिः",
            description = "प्रातिपदिकस्य सुबन्तरूपसिद्धिः",
            signature = OperationSignature(
                requirements = listOf(
                    KarakaRequirement(
                        karaka = Karaka.KARMAN,
                        minimumMembers = 1,
                    )
                )
            ),
            action = SanskritSubantaDerivationAction,
            resultSamjnas = setOf(ExecutionSamjna.SHABDA),
        ),
    )
}

package dev.panini.dhatupatha.curadi

import dev.panini.actions.numeric.SanskritAverageAction
import dev.panini.actions.numeric.SanskritCountingAction
import dev.panini.actions.numeric.SanskritMultiplicationAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.numericOp
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.analysis.SemanticRelation

/** Executable Curādi dhātu गण संख्याने. */
class GanDhatu : Dhatu(
    id = "10.0391",
    krama = 391,
    upadesha = "गण",
    sourceSurface = "गण",
    artha = "सङ्ख्याने",
    arthaHindi = "गिनना, गुणा करना",
    arthaEnglish = "to count, to enumerate, to multiply",
    gana = DhatuGana.CURADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        SanskritMultiplicationAction.numericOp {
            triggeredBy(forbiddenUpasargas = setOf("सम्", "सम"))
        },
        SanskritCountingAction.op {
            requires(Karaka.KARMAN, shape = ExpressionShape.COORDINATION)
            returns(ExecutionSamjna.SANKHYA)
        },
        SanskritAverageAction.op {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredUpasargas = setOf("सम्"))
            returns(ExecutionSamjna.SANKHYA)
        },
    ),
    semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
)

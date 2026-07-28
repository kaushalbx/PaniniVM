package dev.panini.dhatupatha.curadi

import dev.panini.actions.numeric.SanskritAverageAction
import dev.panini.actions.numeric.SanskritCountingAction
import dev.panini.actions.numeric.SanskritMultiplicationAction
import dev.panini.actions.collection.SanskritListLengthAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.numericOp
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.analysis.SemanticRelation

/** Executable Curādi dhātu गण संख्याने. */
class GanDhatu : Dhatu(
    id = "10.0391",
    krama = 391,
    upadesha = "गण",
    sourceSurface = "गण",
    artha = "सङ्ख्याने",
    arthaHindi = "गिनना, गुणा करना, सूच्याकारः",
    arthaEnglish = "to count, to enumerate, to multiply, to get list size",
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
            returns(Samjna.SANKHYA)
        },
        SanskritAverageAction.op {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredUpasargas = setOf("सम्"))
            returns(Samjna.SANKHYA)
        },
        SanskritListLengthAction.op {
            requires(Karaka.KARMAN)
            returns(Samjna.SANKHYA)
        },
    ),
    semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
    surfaceAliases = setOf("गणयति", "गणनम्", "सङ्ख्यानम्", "आकारः"),
)

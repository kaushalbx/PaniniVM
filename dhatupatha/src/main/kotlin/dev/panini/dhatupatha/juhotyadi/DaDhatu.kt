package dev.panini.dhatupatha.juhotyadi

import dev.panini.actions.state.SanskritVariableAssignAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.analysis.SemanticRelation

/** Executable Juhotyādi dhātu डुदाञ् दाने. */
class DaDhatu : Dhatu(
    id = "03.0010",
    krama = 10,
    upadesha = "डुदाञ्",
    sourceSurface = "दा",
    artha = "दाने",
    arthaHindi = "देना, सौंपना, मूल्य का संविभाजन करना",
    arthaEnglish = "to give, to assign, to bind variable value",
    gana = DhatuGana.JUHOTYADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        SanskritVariableAssignAction.op {
            requires(Karaka.KARMAN); returns(Samjna.SHABDA)
        },
    ),
    semanticRelations = setOf(SemanticRelation.RECIPIENT, SemanticRelation.DESIRED_OBJECT),
    surfaceAliases = setOf("यच्छति", "ददाति", "दत्त", "देहि"),
)

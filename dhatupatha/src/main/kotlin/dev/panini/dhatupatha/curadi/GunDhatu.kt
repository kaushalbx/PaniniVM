package dev.panini.dhatupatha.curadi

import dev.panini.actions.numeric.MultiplicationAction
import dev.panini.analysis.SemanticRelation
import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.numericOp
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Curādi गुण. गुणयति, from the mathematical sense of गुण, is the ordinary word for multiplying. */
class GunDhatu : Dhatu(
    id = "10.0436",
    krama = 436,
    upadesha = "गुण",
    sourceSurface = "गुण",
    artha = "आमन्त्रणे",
    arthaHindi = "गुणा करना",
    arthaEnglish = "to multiply",
    gana = DhatuGana.CURADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    blocksNicGuna = true,
    operations = listOf(MultiplicationAction.numericOp { }),
    semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
    surfaceAliases = setOf("गुणयति", "गुणनम्"),
)

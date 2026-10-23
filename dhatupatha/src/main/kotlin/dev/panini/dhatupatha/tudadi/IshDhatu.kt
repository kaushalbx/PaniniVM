package dev.panini.dhatupatha.tudadi

import dev.panini.actions.collection.ListPositionAction
import dev.panini.analysis.SemanticRelation
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.core.Upasarga
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.shiksha.Samjna

/** Executable इषुँ इच्छायाम् with अनु licensing collection-position search. */
class IshDhatu : Dhatu(
    id = "06.0078",
    krama = 78,
    upadesha = "इषुँ",
    sourceSurface = "इष्",
    artha = "इच्छायाम्",
    arthaHindi = "इच्छा करना, खोजना",
    arthaEnglish = "to wish; with anu, to seek",
    gana = DhatuGana.TUDADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        ListPositionAction.op {
            requires(Karaka.KARMAN)      // स्थानम् — requested result relation
            requires(Karaka.SAMBANDHA)   // लक्ष्यस्य — sought item
            requires(Karaka.ADHIKARANA)  // सूच्याम् — collection searched
            triggeredBy(requiredUpasargaIdentities = setOf(Upasarga.ANU))
            returns(Samjna.SANKHYA)
        },
    ),
    semanticRelations = setOf(SemanticRelation.DESIRED_OBJECT),
)

package dev.panini.dhatupatha.bhvadi

import dev.panini.actions.collection.ListSortAction
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

/** Executable Bhvādi dhātu क्रमुँ पादविक्षेपे, extended to deterministic ordering. */
class KramDhatu : Dhatu(
    id = "01.0545",
    krama = 545,
    upadesha = "क्रमुँ",
    sourceSurface = "क्रम्",
    artha = "पादविक्षेपे",
    arthaHindi = "क्रम से चलना, आगे बढ़ना",
    arthaEnglish = "to step; with अनु or प्रति and णिच्, to put in sequence or reverse sequence",
    gana = DhatuGana.BHVADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        ListSortAction.Ascending.op {
            triggeredBy(requiredUpasargaIdentities = setOf(Upasarga.ANU), requiredSanadiIdentities = setOf(dev.panini.core.SanadiAffix.NIC))
            requires(Karaka.KARMAN)
            returns(Samjna.GANA)
        },
        ListSortAction.Descending.op {
            triggeredBy(requiredUpasargaIdentities = setOf(Upasarga.PRATI), requiredSanadiIdentities = setOf(dev.panini.core.SanadiAffix.NIC))
            requires(Karaka.KARMAN)
            returns(Samjna.GANA)
        },
    ),
)

package dev.panini.dhatupatha.tudadi

import dev.panini.actions.collection.ListConcatAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Tudādi dhātu सृजँ विसर्गे (Concatenation / Srj). */
class SrjDhatu : Dhatu(
    id = "06.0150",
    krama = 150,
    upadesha = "सृजँ",
    sourceSurface = "सृज्",
    artha = "विसर्गे",
    arthaHindi = "सृष्टि करना, त्यागना, जोड़ना, सूचीसंयोगः",
    arthaEnglish = "to emit, to create, to combine, to concatenate lists",
    gana = DhatuGana.TUDADI,
    pada = PadaType.PARASMAIPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        ListConcatAction.op {
            requires(Karaka.KARMAN)
            requires(Karaka.SAMPRADANA)
            returns(Samjna.GANA)
        },
    ),
    surfaceAliases = setOf("सृजति", "सर्जनम्", "संयोगः", "संयोजनम्"),
)

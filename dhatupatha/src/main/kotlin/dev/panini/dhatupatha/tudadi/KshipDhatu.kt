package dev.panini.dhatupatha.tudadi

import dev.panini.actions.collection.ListFoldAction
import dev.panini.actions.collection.ListPushAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

/** Executable Tudādi dhātu क्षिपँ प्रेरणे. */
class KshipDhatu : Dhatu(
    id = "06.0005",
    krama = 5,
    upadesha = "क्षिपँ",
    sourceSurface = "क्षिप्",
    artha = "प्रेरणे",
    arthaHindi = "भेजना, फेंकना, सूच्याम् अंशस्य निक्षेपणम्",
    arthaEnglish = "to throw, to send, to push/append item to list",
    gana = DhatuGana.TUDADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.ANIT,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.ANUDATTA,
    operations = listOf(
        ListPushAction.op {
            requires(Karaka.KARMAN); returns(Samjna.GANA, Samjna.SHABDA)
        },
        ListFoldAction.op {
            triggeredBy(requiredUpasargas = setOf("सम्"))
            requires(Karaka.KARMAN) // list
            requires(Karaka.KARANA) // binary operation
            requires(Karaka.SAMPRADANA) // initial value
            returns(Samjna.SANKHYA, Samjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("निक्षिप", "निक्षिपति", "निक्षिपतु", "निक्षेपणम्"),
)

package dev.panini.dhatupatha.tudadi

import dev.panini.actions.collection.SanskritListPushAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.execution.ExecutionSamjna
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
        SanskritListPushAction.op {
            requires(Karaka.KARMAN); returns(ExecutionSamjna.GANA, ExecutionSamjna.SHABDA)
        },
    ),
    surfaceAliases = setOf("निक्षिप", "निक्षिपति", "निक्षिपतु", "निक्षेपणम्"),
)

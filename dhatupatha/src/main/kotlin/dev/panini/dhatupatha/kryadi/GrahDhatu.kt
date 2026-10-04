package dev.panini.dhatupatha.kryadi

import dev.panini.actions.io.ReadAction
import dev.panini.actions.collection.ListIndexAction
import dev.panini.actions.collection.ListCollectAction
import dev.panini.actions.collection.ListSliceAction
import dev.panini.core.DhatuGana
import dev.panini.core.Karaka
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Samjna
import dev.panini.execution.op
import dev.panini.execution.ExecutionEffect
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.analysis.SemanticRelation

/** Executable Kryādi dhātu ग्रहँ उपादाने. */
class GrahDhatu : Dhatu(
    id = "09.0071",
    krama = 71,
    upadesha = "ग्रहँ",
    sourceSurface = "ग्रह्",
    artha = "उपादाने",
    arthaHindi = "लेना, स्वीकार करना, निवेशस्य स्वीकारः",
    arthaEnglish = "to take, to accept, to obtain, to read input",
    gana = DhatuGana.KRYADI,
    pada = PadaType.UBHAYAPADA,
    itStatus = ItStatus.SET,
    karmatva = Karmatva.SAKARMAKA,
    svara = Accent.UDATTA,
    operations = listOf(
        ListCollectAction.op {
            triggeredBy(requiredUpasargas = setOf("सम्"))
            requires(Karaka.KARMAN)
            returns(Samjna.GANA, Samjna.SHABDA)
        },
        ListSliceAction.op {
            requires(Karaka.KARMAN)      // अंशम् — the portion to be taken
            requires(Karaka.SAMBANDHA)   // सूच्याः — collection whose portion it is
            requires(Karaka.APADANA)     // द्वितीयात् — inclusive starting position
            requires(Karaka.ADHIKARANA)  // तृतीयपर्यन्तम् — inclusive end position
            triggeredBy(forbiddenUpasargas = setOf("सम्"))
            returns(Samjna.GANA)
        },
        ListIndexAction.op {
            requires(Karaka.KARMAN)      // मूल्यम् — the value to be taken
            requires(Karaka.APADANA)     // the source collection
            requires(Karaka.ADHIKARANA)  // the position in that collection
            triggeredBy(forbiddenUpasargas = setOf("सम्"))
            returns(Samjna.SHABDA, Samjna.SANKHYA)
        },
        ReadAction.op {
            requires(Karaka.KARMAN); returns(Samjna.SHABDA)
            triggeredBy(forbiddenUpasargas = setOf("सम्"))
            optional(Karaka.SAMPRADANA, Karaka.KARANA, Karaka.APADANA, Karaka.ADHIKARANA)
            effects(ExecutionEffect.READ_RESOURCE)
            bindsResultTo(Karaka.KARMAN)
        },
    ),
    semanticRelations = setOf(SemanticRelation.RECIPIENT, SemanticRelation.DESIRED_OBJECT),
    surfaceAliases = setOf("अनुगृ", "प्रतिगृ", "गृह्णाति", "गृह्ण", "गृह्णीहि"),
)

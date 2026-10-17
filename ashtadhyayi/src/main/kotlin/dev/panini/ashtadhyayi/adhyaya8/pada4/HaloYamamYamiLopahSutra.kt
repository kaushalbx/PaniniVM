package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.*
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.*

/** 8.4.64: optional yam deletion after hal before the corresponding yam. */
object HaloYamamYamiLopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.64", text = "हलो यमां यमि लोपः",
    hindiExplanation = "हल् के बाद यम् वर्ण का उसी यम् वर्ण के परे विकल्प से लोप होता है।",
    type = SutraType.VIBHASHA, chapter = 8, pada = 4, optional = true, kramaValue = 840064,
    role = SutraRole.Vidhi, action = SutraAction.LOPA, scope = SutraScope.DERIVATION,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private fun targets(state: DerivationState) = state.findVarnaTriplets { previous, middle, next ->
        val engine = Ashtadhyayi.pratyaharaEngine
        engine.contains(Pratyahara.HAL, previous.varna) &&
            engine.contains(Pratyahara.YAM, middle.varna) && middle.varna == next.varna
    }
    override fun matches(context: DerivationState) = targets(context).isNotEmpty()
    override fun apply(context: DerivationState): DerivationChange {
        return deleteAt(context, requireNotNull(targets(context).firstOrNull()))
    }
    override fun applyAll(state: DerivationState): List<DerivationChange> {
        val occurrences = targets(state)
        require(occurrences.isNotEmpty())
        return occurrences.map { deleteAt(state, it) } + DerivationChange(state, "8.4.64 declined.", applied = false)
    }
    private fun deleteAt(context: DerivationState, occurrence: LocatedVarna): DerivationChange {
        return DerivationChange(context.deleteTermVarnas(occurrence.termId, occurrence.index, 1, sutra),
            "8.4.64 deletes yam at ${occurrence.termId}:${occurrence.index} after hal before its corresponding yam.")
    }
}

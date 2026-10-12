package dev.panini.dhatupatha.divadi

import dev.panini.core.DhatuGana
import dev.panini.core.Lakara
import dev.panini.core.PadaType
import dev.panini.core.SanadiAffix
import dev.panini.core.TingAffix
import dev.panini.dhatupatha.Dhatu
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva
import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.ast.Vikarana

/** Divādi जनीँ प्रादुर्भावे; causative जनयति denotes producing a result. */
class JanDhatu : Dhatu(
    id = "04.0044", krama = 44, upadesha = "जनीँ", sourceSurface = "जन्",
    artha = "प्रादुर्भावे", arthaHindi = "उत्पन्न करना, पैदा होना",
    arthaEnglish = "to be born, to become, to come into existence",
    gana = DhatuGana.DIVADI, pada = PadaType.ATMANEPADA,
    itStatus = ItStatus.SET, karmatva = Karmatva.AKARMAKA, svara = Accent.UDATTA,
    preferredForSourceDerivation = true,
) {
    override fun denotesResultProduction(form: TingantaPada): Boolean =
        form.dhatu.mulaDhatu in setOf(upadesha, sourceSurface) &&
            form.dhatu.sanadiAffixes == listOf(SanadiAffix.NIC) &&
            // The causative stem takes शप् (3.1.68), not the base root's श्यन्.
            (form.vikarana == null || form.vikarana == Vikarana.SHAP) &&
            form.upasargas.isEmpty() && form.lakara == Lakara.LAT && form.ting.affix == TingAffix.TIP
}

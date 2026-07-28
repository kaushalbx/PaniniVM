package dev.panini.dhatupatha.tanadi

import dev.panini.core.DhatuGana
import dev.panini.core.PadaType
import dev.panini.dhatupatha.Dhatu
import dev.panini.dhatupatha.dhatuPatha
import dev.panini.shiksha.Accent
import dev.panini.shiksha.ItStatus
import dev.panini.shiksha.Karmatva

object TanadiDhatus {
    val all: List<Dhatu> = dhatuPatha(DhatuGana.TANADI) {
        dhatu(TanDhatu())
        dhatu(
            "08.0002", 2, "षणुँ", "सन्",
            "दाने", "देना, दान करना", "to give, to donate",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "08.0003", 3, "क्षणुँ", "क्षण्",
            "हिंसायाम्", "मारना, जान से मार देना", "to kill, to destroy, to irritate, to cause pain, to hurt, to break",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "08.0004", 4, "क्षिणुँ", "क्षिण्",
            "हिंसायाम्", "मारना, जान से मारना, दुःख देना, सताना, तोड़ना", "to kill, to destroy, to irritate, to cause pain, to hurt, to break",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "08.0005", 5, "ऋणुँ", "ऋण्",
            "गतौ", "जाना", "to go",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "08.0006", 6, "तृणुँ", "तृण्",
            "अदने", "खाना", "to eat,to graze",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "08.0007", 7, "घृणुँ", "घृण्",
            "दीप्तौ", "चमकना, प्रकाशित होना", "to shine,to glow",
            PadaType.UBHAYAPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "08.0008", 8, "वनुँ", "वन्",
            "याचने", "याचना करना, मांगना", "to act,to beg,to request,to seek",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(
            "08.0009", 9, "मनुँ", "मन्",
            "अवबोधने", "जानना, समझना, विचार करना, मानना", "to understand, to regard, to think, to believe, to assume",
            PadaType.ATMANEPADA, ItStatus.SET, Karmatva.SAKARMAKA, Accent.UDATTA,
        )
        dhatu(KruDhatu())
    }
}

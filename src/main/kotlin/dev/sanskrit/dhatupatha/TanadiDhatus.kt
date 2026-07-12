package dev.sanskrit.dhatupatha

/** Complete Tanadi-gaṇa imported from the MIT-shared ashtadhyayi-com data set. */
object TanadiDhatus {
    val all: List<Dhatu> = dhatuPatha(Gana.TANADI) {
        dhatu("08.0001", 1, "तनुँ", "विस्तारे", "फैलाना, बढ़ाना", "to spread, to stretch, to expand, to increase", PadaType.UBHAYAPADA)
        dhatu("08.0002", 2, "षणुँ", "दाने", "देना, दान करना", "to give, to donate", PadaType.UBHAYAPADA)
        dhatu("08.0003", 3, "क्षणुँ", "हिंसायाम्", "मारना, जान से मार देना", "to kill, to destroy, to irritate, to cause pain, to hurt, to break", PadaType.UBHAYAPADA)
        dhatu("08.0004", 4, "क्षिणुँ", "हिंसायाम्", "मारना, जान से मारना, दुःख देना, सताना, तोड़ना", "to kill, to destroy, to irritate, to cause pain, to hurt, to break", PadaType.UBHAYAPADA)
        dhatu("08.0005", 5, "ऋणुँ", "गतौ", "जाना", "to go", PadaType.UBHAYAPADA)
        dhatu("08.0006", 6, "तृणुँ", "अदने", "खाना", "to eat,to graze", PadaType.UBHAYAPADA)
        dhatu("08.0007", 7, "घृणुँ", "दीप्तौ", "चमकना, प्रकाशित होना", "to shine,to glow", PadaType.UBHAYAPADA)
        dhatu("08.0008", 8, "वनुँ", "याचने", "याचना करना, मांगना", "to act,to beg,to request,to seek", PadaType.ATMANEPADA)
        dhatu("08.0009", 9, "मनुँ", "अवबोधने", "जानना, समझना, विचार करना, मानना", "to understand, to regard, to think, to believe, to assume", PadaType.ATMANEPADA)
        dhatu("08.0010", 10, "डुकृञ्", "करणे", "करना", "to do, to act, to make", PadaType.UBHAYAPADA)
    }
}

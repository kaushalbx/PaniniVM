package dev.panini.analysis

private val ordinal = Regex("^(प्रथमा?|द्वितीया?|तृतीया?|प्रथ\\+अमच्|द्वि\\+तीय|तृ\\+तीय)(\\+|$)")

/** प्रथम, द्वितीय, तृतीय (5.2.54, 5.2.55): the ordinals that can qualify a noun, written with or without their affix. */
fun isOrdinalWord(written: String): Boolean = ordinal.containsMatchIn(written.replace(" ", ""))

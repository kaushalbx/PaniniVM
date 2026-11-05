package dev.panini.analysis

import dev.panini.dhatupatha.Dhatu

/**
 * Meaning classes read from the Dhātupāṭha gloss of a resolved root. A gloss is evidence for a default, not
 * proof: 2.3.12 needs bodily motion, and मनसा पाटलिपुत्रं गच्छति has the same root.
 */
enum class DhatuArthaClass(private vararg val glosses: String) {
    MOTION("गतौ", "गति", "प्रापणे", "सञ्चलने"),
    FALLING("पतने", "अवस्रंसने"),
    BEING("सत्तायाम्", "भुवि", "वर्तने"),
    FEAR("भये"),
    ANGER("क्रोधे", "जिघांसा", "ईर्ष्या", "असूया"),
    DELIGHT("भिप्रीतौ"),
    COGNITION("ज्ञाने", "अवगमने", "अवबोधने", "बोधने"),
    EATING("भक्षणे", "अदने", "पाने", "अभ्यवहार"),
    /** Articulate speech only; plain शब्दे also covers animal noise and अव्यक्ते शब्दे. */
    SPEECH("वाचि", "परिभाषणे", "भाषायाम्"),
    STUDY("अध्ययने"),
    DESIRE("इच्छायाम्", "कान्तौ"),
    ;

    fun matches(dhatu: Dhatu?): Boolean = dhatu != null && glosses.any { it in dhatu.artha }
}

fun Dhatu?.hasArtha(vararg classes: DhatuArthaClass): Boolean = classes.any { it.matches(this) }

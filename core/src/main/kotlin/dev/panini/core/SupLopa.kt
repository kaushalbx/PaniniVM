package dev.panini.core

enum class SupLopa(val upadesha: String) {
    LUK("लुक्"),
    SHLU("श्लु"),
    LUP("लुप्"),
    ALUK("अलुक्");


    companion object{
        private val upadeshaMap = entries.associateBy { it.upadesha }
        fun fromUpadesha(value: String) = upadeshaMap[value] ?: error("अज्ञाता सुपः अवस्था: $value")
    }
}

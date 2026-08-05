package dev.panini.core

/**
 * Fundamental Pāṇinian Nominal Compound Classifications (समास भेद - Aṣṭādhyāyī 2.1 - 2.2).
 */
enum class SamasaType {
    AVYAYIBHAVA,        // अव्ययीभाव (2.1.6)
    TATPURUSA,          // तत्पुरुष (2.1.24 - 2.2.22)
    NAN_TATPURUSA,      // नञ् तत्पुरुष (2.2.6 नञ्, 6.3.73 नलोपो नञः, 6.3.74 तस्मान्नुडचि)
    UPAPADA_TATPURUSA,  // उपपद तत्पुरुष (2.2.19 उपपदमतिङ्)
    ALUK_TATPURUSA,     // अलुक् तत्पुरुष (6.3.1 अलुक उत्तरपदे, 6.3.14, 6.3.21)
    MAYURAVYAMSAKADI,   // मयूरव्यंसकादि तत्पुरुष (2.1.72 मयूरव्यंसकादयश्च)
    KARMADHARAYA,       // कर्मधारय (2.1.57 विशेषणं विशेष्येण बहुलम्, 1.2.42)
    DVIGU,              // द्विगु (2.1.52 संख्यापूर्वो द्विगुः)
    BAHUVRIHI,          // बहुव्रीहि (2.2.24)
    DVANDVA,            // द्वन्द्व (2.2.29)
}

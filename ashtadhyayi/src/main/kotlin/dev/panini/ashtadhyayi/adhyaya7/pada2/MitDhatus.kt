package dev.panini.ashtadhyayi.adhyaya7.pada2

/**
 * मित् roots of the Dhātupāṭha gaṇasūtras: जनीजॄष्क्नसुरञ्जोऽमन्ताश्च, with न कम्यमिचमाम्, and core घटादि roots.
 * The घटादि list here is a partial slice, not the whole group.
 */
internal object MitDhatus {
    private val excludedAmanta = setOf("कम्", "अम्", "चम्")
    private val listed = setOf("जन्", "जॄ", "क्नस्", "रञ्ज्", "घट्", "व्यथ्", "प्रथ्", "प्रस्", "म्रद्", "स्खद्", "क्षञ्", "दक्ष्", "क्रप्", "त्वर्", "ज्वर्", "नट्", "स्वन्", "ध्वन्")

    fun isMit(root: String): Boolean = root in listed || (root.endsWith("म्") && root !in excludedAmanta)
}

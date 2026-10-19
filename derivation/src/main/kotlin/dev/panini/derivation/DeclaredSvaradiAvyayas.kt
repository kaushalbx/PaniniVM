package dev.panini.derivation

import dev.panini.ganapatha.SvaradiGana

/** Selected corpus-relevant identities from the existing स्वरादि gaṇa. */
object DeclaredSvaradiAvyayas {
    val upadeshas: List<String> = listOf(
        "उच्चैस्", "नीचैस्", "शनैस्", "भूयस्",
        "ह्यस्", "श्वस्", "अधस्", "तिरस्", "मिथस्", "प्रायस्", "मुहुस्", "नमस्", "अम्भस्",
        "स्वर्", "अन्तर्", "प्रातर्", "पुनर्", "भूर्", "भुवर्",
    ).also { entries ->
        require(entries.all(SvaradiGana::contains))
    }

    val source: String = SvaradiGana.sourceUrl
}

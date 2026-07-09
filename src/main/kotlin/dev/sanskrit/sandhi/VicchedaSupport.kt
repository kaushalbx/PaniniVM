package dev.sanskrit.sandhi

import dev.sanskrit.shiksha.Svara

fun splitBeforeEach(text: String, marker: String): List<Pair<String, String>> {
    if (marker.isEmpty()) return emptyList()
    return buildList {
        var start = 0
        while (true) {
            val index = text.indexOf(marker, start)
            if (index < 0) break
            if (index > 0 && index < text.length) {
                add(text.substring(0, index) to text.substring(index))
            }
            start = index + marker.length
        }
    }
}

fun replaceEnding(text: String, old: String, new: String): String? =
    if (text.endsWith(old)) text.dropLast(old.length) + new else null

fun withInitialSvara(right: String, svara: Svara): String =
    svara.devanagari + right

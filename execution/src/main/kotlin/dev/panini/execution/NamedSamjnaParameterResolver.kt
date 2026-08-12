package dev.panini.execution

/** Replaces a named parameter pada while preserving the case required by its use site. */
object NamedSamjnaParameterResolver {
    fun replace(text: String, nameStem: String, rawArgument: String): String {
        val argumentStem = rawArgument.substringBefore('+').trim()
        val pattern = Regex("(?<![\\p{L}\\p{M}])${Regex.escape(nameStem)}\\s*\\+\\s*([^\\s।॥]+)")
        return pattern.replace(text) { match -> "$argumentStem + ${match.groupValues[1]}" }
    }
}

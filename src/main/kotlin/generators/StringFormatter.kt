package generators

import resources.Platform

/**
 * generators.StringFormatter is used to replace arguments in your shared strings file to export properly across platforms.
 * For example, you may wish to localize %STORE to "Play Store" and "App Store" for Android and iOS, respectively
 *
 * @param platforms: formatters will only modify strings on the specified platform
 * @param arg: searches for this argument in a string, e.g. %d
 * @param isIndexed: Android string formatting is indexed, e.g. %s becomes %1$s or %2$s
 * @param formatter: When a match is found, this function replaces the arg
 *                   index: index is only incremented if isIndexed == true
 *                   isXml: true if the file being created is in XML format. Always true on Android, false for normal strings in iOS
 *                   returns a String that replaces the arg
 */
//@Serializable
class StringFormatter(
    val arg: String,
    val platforms: List<Platform>? = null,
    val isIndexed: Boolean = false,
    val formatter: (index: Int, isXml: Boolean) -> String
) {
    init {
        if (arg.isEmpty()) throw IllegalArgumentException("generators.StringFormatter arg cannot be empty")
    }

    companion object {
        val defaultFormatters: List<StringFormatter> by lazy {
            listOf(
                StringFormatter("\n") { _, _ -> "\\n" },
                StringFormatter("\'", Platform.AndroidOnly) { _, _ -> "\\'" },
                StringFormatter("%s", Platform.AndroidOnly, isIndexed = true) { index, _ -> "%$index\$s" },
                StringFormatter("%d", Platform.AndroidOnly, isIndexed = true) { index, _ -> "%$index\$d" },
                StringFormatter("%f", Platform.AndroidOnly, isIndexed = true) { index, _ -> "%$index\$f" },
                StringFormatter("%s", Platform.iosOnly) { _, _ -> "%@" },
                StringFormatter("\"", Platform.iosOnly) { _, isXml -> if (isXml) "\\\"" else "\"" })
        }
    }
}

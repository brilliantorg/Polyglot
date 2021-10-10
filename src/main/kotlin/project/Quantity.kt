package project

/**
 * Different languages have different grammatical rules for quantity.
 * e.g. English, 1 book, 2 books
 *
 * Not all languages use all rules, and some languages, like Chinese, only use "other"
 *
 * More information:
 * https://developer.android.com/guide/topics/resources/string-resource#Plurals
 */
enum class Quantity {
    ZERO,
    ONE,
    TWO,
    FEW,
    MANY,
    OTHER;

    val label: String = name.lowercase()
}

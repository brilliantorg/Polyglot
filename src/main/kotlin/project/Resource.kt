package project

import androidx.compose.runtime.Stable
import java.util.*

/**
 * A Resource is a type of localized content: A string, a plural, or a string array.
 * Strings, Plurals, and Arrays each require different formatting on Android and iOS.
 */
@Stable
sealed interface Resource<M : Metadata> {
    val type: ResourceType
}

sealed class Metadata(val type: ResourceType) {
    abstract val group: String
    abstract val platforms: List<Platform>
}

@Stable
@JvmInline
value class Str(val text: String = "") : Resource<Str.Metadata> {
    override val type: ResourceType get() = ResourceType.STRINGS

    data class Metadata(
        override val group: String = "",
        override val platforms: List<Platform> = Platform.ALL
    ) : project.Metadata(ResourceType.STRINGS)
}

@Stable
@JvmInline
value class Plural(val items: Map<Quantity, String> = mapOf(Quantity.ONE to "", Quantity.OTHER to "")) : Resource<Plural.Metadata> {
    constructor(
        zero: String? = null,
        one: String?,
        two: String? = null,
        few: String? = null,
        many: String? = null,
        other: String
    ) : this(buildMap {
        if (zero != null) put(Quantity.ZERO, zero)
        if (one != null) put(Quantity.ONE, one)
        if (two != null) put(Quantity.TWO, two)
        if (few != null) put(Quantity.FEW, few)
        if (many != null) put(Quantity.MANY, many)
        put(Quantity.OTHER, other)
    })

    operator fun get(quantity: Quantity) = items[quantity]

    override val type: ResourceType get() = ResourceType.PLURALS

    data class Metadata(
        override val group: String = "",
        override val platforms: List<Platform> = Platform.ALL,
        val quantities: List<Quantity> = listOf(Quantity.ONE, Quantity.OTHER)
    ) : project.Metadata(ResourceType.PLURALS)
}

@Stable
@JvmInline
value class StringArray(val items: List<String> = listOf()) : Resource<StringArray.Metadata> {
    override val type: ResourceType get() = ResourceType.ARRAYS

    data class Metadata(
        override val group: String = "",
        override val platforms: List<Platform> = Platform.ALL,
        val size: Int = 1
    ) : project.Metadata(ResourceType.ARRAYS)
}

fun <T : Metadata> Map<ResourceId, T>.save(projectName: String, type: ResourceType) {
    val file = Project.resourceMetadataFile(projectName, type)
    val props = Properties()
    forEach { (resId, metadata) ->
        val extraData = when (metadata) {
            is Str.Metadata -> ""
            is Plural.Metadata -> "|${metadata.quantities.joinToString(separator = ",") { it.name }}"
            is StringArray.Metadata -> "|${metadata.size}"
            else -> ""
        }
        props.setProperty(resId.value, "${metadata.group}|${metadata.platforms.sorted().joinToString(separator = ",") { it.name }}$extraData")
    }
    runCatching { props.store(file.outputStream(), "") }.onFailure {
        println("Failed to save ${type.name.lowercase()} resources with $it")
    }
}

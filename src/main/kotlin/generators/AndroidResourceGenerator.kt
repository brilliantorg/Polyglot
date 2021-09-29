package generators

import locales.LocaleIsoCode
import locales.get
import locales.getRequired
import project.Platform
import project.Quantity
import locales.isDefault
import org.w3c.dom.Document
import org.w3c.dom.Element
import sqldelight.ArrayLocalizations
import sqldelight.PluralLocalizations
import sqldelight.StringLocalizations
import javax.xml.transform.Transformer
import java.io.File

/**
 * Generates Android resources for a given language in the specified folder
 */
class AndroidResourceGenerator(androidFolder: File, locale: LocaleIsoCode, formatters: List<StringFormatter>, strings: List<StringLocalizations>, plurals: List<PluralLocalizations>, arrays: List<ArrayLocalizations>) :
    ResourceGenerator(locale, formatters) {
    override val platform: Platform = Platform.ANDROID
    private val valuesFolder = File(androidFolder, "values${if (locale.isDefault) "" else "-$locale"}").also(File::mkdirs)
    private val document: Document = createDocument()
    private val resourceElement: Element = document.createElement("resources").also {
        if (locale.isDefault) it.setAttribute("xmlns:tools", "http://schemas.android.com/tools")
        document.appendChild(it)
    }

    init {
//        addAll(resources)
    }

    override fun generateFiles() {
        transformer.transform(document, valuesFolder, "strings.xml")
    }

    /**
     * <plurals name="numberOfSongsAvailable">
     *     <item quantity="one">Znaleziono %d piosenkę.</item>
     *     <item quantity="few">Znaleziono %d piosenki.</item>
     *     <item quantity="other">Znaleziono %d piosenek.</item>
     * </plurals>
     */
    override fun addPlurals(res: PluralLocalizations) {
        resourceElement.appendChild(document.createElement("plurals").apply {
            setAttribute("name", res.id)
            Quantity.values().forEach { quantity ->
//                val item = res.quantity(quantity) ?: return@forEach
//                val text = item.get(locale, isRequired = quantity.isRequired) ?: return@forEach
//                appendChild(document.createElement("item").apply {
//                    setAttribute("quantity", quantity.label)
//                    appendChild(document.createTextNode(text.sanitized()))
//                })
            }
        })
    }

    override fun addString(res: StringLocalizations) {
//        val txt = res.localizations.getRequired(locale).sanitized()
//        if (txt.isBlank()) return
//        /** <string name="dragon">Trogdor the Burninator</string> */
//        resourceElement.appendChild(document.createElement("string").apply {
//            setAttribute("name", res.id)
//            appendChild(document.createTextNode(txt))
//        })
    }

    /**
     * <string-array name="country_names">
     *      <item>France</item>
     *      <item>Germany</item>
     * </string-array>
     */
    override fun addStringArray(res: ArrayLocalizations) {
        resourceElement.appendChild(document.createElement("string-array").apply {
            setAttribute("name", res.id)
//            for (item in res.items) {
//                appendChild(document, "item", item.getRequired(locale).sanitized())
//            }
        })
    }

    companion object {
        private val transformer: Transformer by lazy { createTransformer() }
    }
}

import android.util.Xml
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import org.xmlpull.v1.XmlPullParser

data class Currency(val code: String, val rate: Double)

fun fetchCurrencyRates(urlString: String): List<Currency> {
    val currencies = mutableListOf<Currency>()

    try {
        // Открываем соединение
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        // Получаем поток данных
        val inputStream: InputStream = connection.inputStream

        // Создаем и настраиваем парсер
        val parser = Xml.newPullParser()
        parser.setInput(inputStream, null)
        var eventType = parser.eventType
        var currentCode = ""
        var currentRate = ""

        // Парсим XML
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    if (parser.name == "Valute") {
                        currentCode = ""
                        currentRate = ""
                    } else if (parser.name == "CharCode") {
                        parser.next()
                        currentCode = parser.text
                    } else if (parser.name == "VunitRate") {
                        parser.next()
                        currentRate = parser.text.replace(',', '.')
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (parser.name == "Valute") {
                        val rate = currentRate.toDoubleOrNull() ?: 0.0
                        if (currentCode.isNotEmpty() && rate > 0) {
                            currencies.add(Currency(currentCode, rate))
                        }
                    }
                }
            }
            eventType = parser.next()
        }

        // Закрываем поток
        inputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return currencies
}

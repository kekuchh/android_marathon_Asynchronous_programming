import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.*

// Функция для проверки доступности веб-сайта
fun checkWebsite(url: String): Boolean {
    return try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "HEAD"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.responseCode == HttpURLConnection.HTTP_OK
    } catch (e: Exception) {
        false
    }
}

// Список веб-сайтов для проверки
val websites = listOf(
    "https://www.google.com",
    "https://www.facebook.com",
    "https://www.github.com",
    "https://www.twitter.com",
    "https://www.instagram.com",
    "https://ya.ru/",
    "https://theverge.com",
    "https://vk.com/",
    "https://yandex.ru/pogoda/ru-RU/saint-petersburg",
    "https://habr.com"
)

fun main() = runBlocking {
    val tasks = websites.map { url ->
        async {
            val result = checkWebsite(url)
            Pair(url, result)
        }
    }
    tasks.forEach { task ->
        val (url, result) = task.await()
        val status = if (result) "доступен" else "недоступен"
        println("Сайт $url $status")
    }
}
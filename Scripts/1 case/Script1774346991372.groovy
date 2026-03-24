import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import groovy.json.JsonSlurper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

def response = WS.sendRequest(findTestObject('get case 1'))

WS.verifyResponseStatusCode(response, 200)

String responseBody = response.getResponseBodyContent()
def json = new JsonSlurper().parseText(responseBody)

if (!responseBody || responseBody.trim().length() == 0) {
    println "Response kosong!"
} else {
    println "Response berhasil diterima"
}

if (!json.containsKey('list') || json.list.size() == 0) {
    println "JSON tidak mengandung 'list' atau list kosong"
} else {
    println "JSON mengandung 'list' dengan ${json.list.size()} item"
}

String firstDtTxt = json.list[0].dt_txt
LocalDate baseDate = LocalDate.parse(firstDtTxt.substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
println "Tanggal acuan: $baseDate"

for (int i = 0; i < 5; i++) {
    LocalDate checkDate = baseDate.plusDays(i)
    println "Perkiraan cuaca untuk tanggal: $checkDate"

    def dailyForecasts = json.list.findAll { ((String) it.dt_txt).startsWith(checkDate.toString()) }

    if (dailyForecasts.size() == 0) {
        println "Tidak ada data perkiraan cuaca untuk $checkDate"
        continue
    }

    dailyForecasts.each { forecast ->
        println "Waktu: ${forecast.dt_txt}"

        if (!forecast.weather || forecast.weather.size() == 0) {
            println "Data weather tidak tersedia"
        } else {
            forecast.weather.each { w ->
                String mainWeather = w.main ?: "N/A"
                String descriptionWeather = w.description ?: "N/A"

                println "Utama: $mainWeather"
                println "Deskripsi: $descriptionWeather"
            }
        }
    }
    println "---------------------------------------"
}
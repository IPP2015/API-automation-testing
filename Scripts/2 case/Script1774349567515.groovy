import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import groovy.json.JsonSlurper as JsonSlurper

def response = WS.sendRequest(findTestObject('get case 2'))

WS.verifyResponseStatusCode(response, 200)

String responseBody = response.getResponseBodyContent()

if (!(responseBody)) {
    println('Response kosong!')
} else {
    println('Response berhasil diterima')
}

def json = new JsonSlurper().parseText(responseBody)

if (!(json.list) || (json.list.size() == 0)) {
    println('Tidak ada data di \'list\'')
} else {
    println("'list' berisi $json.list.size() item")
}

json.list.each({ def item ->
	println "Air Quality Index: ${item.main?.aqi ?: 'N/A'}"
        if (item.components) {
            println("    CO     : $item.components.co")

            println("    NO     : $item.components.no")

            println("    NO2    : $item.components.no2")

            println("    O3     : $item.components.o3")

            println("    SO2    : $item.components.so2")

            println("    PM2.5  : $item.components.pm2_5")

            println("    PM10   : $item.components.pm10")

            println("    NH3    : $item.components.nh3")
        } else {
            println('components\' tidak ditemukan')
        }
        
        println('--------------------------------')
    })


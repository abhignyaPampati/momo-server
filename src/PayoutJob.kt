package com.momo
import org.quartz.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.*
import kotlinx.serialization.json.*


@Serializable
data class BeneficiaryDetails (
    @SerializedName("email_address") val email_address : String,
    @SerializedName("mobile_number") val mobile_number : String,
    @SerializedName("address") val address : String,
    @SerializedName("ifsc_code") val ifsc_code : String,
    @SerializedName("country_code") val country_code : String
) {
    annotation class SerializedName(val value: String)
}

@Serializable
data class InitiateTransaction (
    @SerializedName("reference_id") val reference_id : String,
    @SerializedName("purpose_message") val purpose_message : String,
    @SerializedName("from_customer_id") val from_customer_id : String,
    @SerializedName("to_customer_id") val to_customer_id : String,
    @SerializedName("from_account") val from_account : String,
    @SerializedName("to_account") val to_account : String,
    @SerializedName("mobile_number") val mobile_number : String,
    @SerializedName("email_address") val email_address : String,
    @SerializedName("name") val name : String,
    @SerializedName("transfer_type") val transfer_type : String,
    @SerializedName("transfer_amount") val transfer_amount : String,
    @SerializedName("beneficiary_details") val beneficiary_details : BeneficiaryDetails,
    @SerializedName("currency_code") val currency_code : String
) {
    annotation class SerializedName(val value: String)
}



class PayoutJob: Job{
    private val clientId = "momo_staging2"
    override fun execute(context: JobExecutionContext?) {
        runBlocking {
            launch {
                    initiate()
//                    println("This is a quartz job!")
            }
        }
    }

    private suspend fun initiate(){
        val url = "https://in.staging.decentro.tech/core_banking/money_transfer/initiate"
        val client = HttpClient(CIO)
        val response: HttpResponse = client.post(url) {
            headers{
                append("client_id", clientId)
                append("client_secret", "6u8b4U0vJVWy84VtvfFVCHEqn0Kllo15")
                append("module_secret", "tGs7NjCd59wy9wZMwLRlUx32HLJTaszj")
                append("provider_secret", "qtDM3ipZa1eWgq3A1cp75zdF5QVXthaL")
            }
            contentType(ContentType.Application.Json)
            body = Json.encodeToString(InitiateTransaction(
                "decentro_request_222",
                "This is a nice message",
                "cust_0003",
                "mer98970",
                "462515414661812074",
                "20461064878",
                "9542246006",
                "pampatiabhignya@gmail.com",
                "some_remitter_name",
                "NEFT",
                "5",
                BeneficiaryDetails(
                    "pampatiabhignya@gmail.com",
                    "9542246006",
                    "test address01",
                    "SBIN0014182",
                    "IN"
                ),
                "INR"
            ))
        }
        val stringBody: String = response.receive()
        println(stringBody)
    }
}
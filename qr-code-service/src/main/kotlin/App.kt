import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream


fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }
    install(Routing) {
        get("/ping") {
            call.respondText("pong")
        }
        post("/generate-qr-code") {
            runCatching { generateQRCode(call.receive<QRCodeParameter>()) }
                .onSuccess {
                    println("generateQRCode success")
                    call.respondBytes(it, ContentType.Image.PNG, HttpStatusCode.OK)
                }
                .onFailure {
                    println("generateQRCode failed: ${it.message}")
                    call.respond(HttpStatusCode.InternalServerError)
                }
        }
    }
}

@KtorExperimentalAPI
fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(CIO, port, watchPaths = listOf("build"), module = Application::module).start(true)
}

private fun generateQRCode(p: QRCodeParameter): ByteArray {
    val codeWriter = QRCodeWriter()
    val hints = mapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L)
    val bitMatrix = codeWriter.encode(p.text, BarcodeFormat.QR_CODE, p.width, p.height, hints)
    ByteArrayOutputStream().use {
        MatrixToImageWriter.writeToStream(bitMatrix, "png", it)
        return it.toByteArray()
    }
}

@Serializable
data class QRCodeParameter(
    val text: String,
    val width: Int,
    val height: Int
)
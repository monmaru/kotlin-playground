import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.client.j2se.MatrixToImageConfig
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
import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt


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
            runCatching { generateQRCode(call.receive()) }
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
    val hints = mapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
    val bitMatrix = codeWriter.encode(p.text, BarcodeFormat.QR_CODE, p.size, p.size, hints)
    val config = MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE)
    val qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, config)
    return if (p.useLogo) {
        convertPngByteArray(overlayLogo(qrImage))
    } else {
        convertPngByteArray(qrImage)
    }
 }

private fun overlayLogo(image: BufferedImage): BufferedImage {
    val logoImage = ImageIO.read(File("./test.png"))
    val deltaHeight = image.height - logoImage.height
    val deltaWidth = image.width - logoImage.width
    val combined = BufferedImage(image.height, image.width, BufferedImage.TYPE_INT_ARGB)
    val g = combined.graphics as Graphics2D
    g.drawImage(image, 0, 0, null)
    g.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)
    g.drawImage(logoImage, (deltaWidth / 2).toFloat().roundToInt(), (deltaHeight / 2).toFloat().roundToInt(), null)
    return combined
}

private fun convertPngByteArray(image: BufferedImage): ByteArray {
    ByteArrayOutputStream().use {
        ImageIO.write(image, "png", it)
        return it.toByteArray()
    }
}

@Serializable
data class QRCodeParameter(
    val text: String,
    val size: Int,
    val useLogo: Boolean
)
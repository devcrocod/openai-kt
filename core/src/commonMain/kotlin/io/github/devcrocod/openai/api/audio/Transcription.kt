package io.github.devcrocod.openai.api.audio

import io.github.devcrocod.openai.model.ModelId
import kotlinx.io.Source
import kotlinx.serialization.Serializable


/**
 * Transcribes audio into the input language.
 *
 * @param file (Required) The audio file object (not file name) to transcribe, in one of these formats:
 * flac, mp3, mp4, mpeg, mpga, m4a, ogg, wav, or webm.
 * @param model (Required) ID of the model to use. Only `whisper-1` is currently available.
 * @param language (Optional) The language of the input audio.
 * Supplying the input language in [ISO-639-1](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)
 * format will improve accuracy and latency.
 * @param prompt (Optional) An optional text to guide the model's style or continue a previous audio segment.
 * The [prompt](https://platform.openai.com/docs/guides/speech-to-text/prompting) should match the audio language.
 * @param responseFormat (Optional) Defaults to `json`. The format of the transcript output, in one of these options:
 * `json`, `text`, `srt`, `verbose_json`, or `vtt`.
 * @param temperature (Optional) Defaults to 0. The sampling temperature, between 0 and 1.
 * Higher values like 0.8 will make the output more random,
 * while lower values like 0.2 will make it more focused and deterministic.
 * If set to 0, the model will use [log probability](https://en.wikipedia.org/wiki/Log_probability)
 * to automatically increase the temperature until certain thresholds are hit.
 */
@Serializable
public data class TranscriptionRequest(
    val file: Source,
    val model: ModelId = ModelId("whisper-1"),
    val language: String? = null,
    val prompt: String? = null,
    val responseFormat: ResponseFormat? = null,
    val temperature: Double? = null
)


public class TranscriptionRequestBuilder() {
    public var file: Source? = null // TODO(test file)

    public var language: String? = null

    public var prompt: String? = null

    public var response: ResponseFormat? = null

    public var temperature: Double? = null

    public fun required(model: ModelId, input: String, voice: Voice): SpeechRequestBuilder =
        SpeechRequestBuilder().apply {
            this.model = model
            this.input = input
            this.voice = voice
        }

    private fun validateFile(file: Source?): Source {
        // TODO(check format flac, mp3, mp4, mpeg, mpga, m4a, ogg, wav, or webm.)
        requireNotNull(file) { "Audio file is required" }
        return file
    }


    internal fun build(): TranscriptionRequest = TranscriptionRequest(
        file = validateFile(file),
        language = language,
        prompt = prompt,
        responseFormat = response,
        temperature = temperature
    )
}

public fun transcriptionRequest(block: TranscriptionRequestBuilder.() -> Unit): TranscriptionRequest =
    TranscriptionRequestBuilder().apply(block).build()

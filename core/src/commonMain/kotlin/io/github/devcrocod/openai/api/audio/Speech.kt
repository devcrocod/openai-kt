package io.github.devcrocod.openai.api.audio

import io.github.devcrocod.openai.model.ModelId
import kotlinx.serialization.Serializable

/**
 * Generates audio from the input text.
 *
 * @param model One of the available [TTS models](https://platform.openai.com/docs/models/tts): `tts-1` or `tts-1-hd`
 * @param input The text to generate audio for. The maximum length is 4096 characters.
 * @param voice The voice to use when generating the audio.
 * Supported voices are `alloy`, `echo`, `fable`, `onyx`, `nova`, and `shimmer`.
 * Previews of the voices are available in the [Text to speech guide](/docs/guides/text-to-speech/voice-options).
 * @param responseFormat The format to audio in. Supported formats are `mp3`, `opus`, `aac`, and `flac`.
 * @param speed The speed of the generated audio. Select a value from `0.25` to `4.0`. `1.0` is the default.
 */

@Serializable
public data class SpeechRequest(
    val model: ModelId,
    val input: String,
    val voice: Voice,
    val responseFormat: AudioFormat? = AudioFormat("mp3"),
    val speed: Double? = 1.0
)

public class SpeechRequestBuilder internal constructor() {
    public var model: ModelId? = null
    public var input: String? = null
    public var voice: Voice? = null
    public var response: AudioFormat? = null
    public var speed: Double? = null

    public fun required(model: ModelId, input: String, voice: Voice): SpeechRequestBuilder =
        SpeechRequestBuilder().apply {
            this.model = model
            this.input = input
            this.voice = voice
        }

    private fun validate(modelId: ModelId?): ModelId {
        requireNotNull(modelId) { "Model is required" }
        require(modelId == ModelId.`tts-1` || modelId == ModelId.`tts-1-hd`) {
            "Model should be one of the available TTS models: `tts-1` or `tts-1-hd`"
        }
        return modelId
    }

    private fun validateInput(input: String?): String {
        requireNotNull(input) { "Model is required" }
        require(input.length <= 4096) { "Input length should be less than or equal to 4096 characters" }
        return input
    }


    internal fun build(): SpeechRequest = SpeechRequest(
        model = validate(model),
        input = validateInput(input),
        voice = requireNotNull(voice) { "Voice is required" },
        responseFormat = response,
        speed = speed,
    )
}

public fun speechRequest(block: SpeechRequestBuilder.() -> Unit): SpeechRequest =
    SpeechRequestBuilder().apply(block).build()

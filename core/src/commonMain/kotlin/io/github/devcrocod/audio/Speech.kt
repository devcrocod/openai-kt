package io.github.devcrocod.audio

import io.github.devcrocod.model.ModelId
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

    public val ModelId.Companion.tts1: ModelId // todo(add targets implementation for wasm?)
        get() = ModelId("tts-1")

    public val ModelId.Companion.tts1hd: ModelId // todo(add targets implementation for wasm?)
        get() = ModelId("tts-1-hd")

    public val Voice.Companion.alloy: Voice
        get() = Voice("alloy")

    public val Voice.Companion.echo: Voice
        get() = Voice("echo")

    public val Voice.Companion.fable: Voice
        get() = Voice("fable")

    public val Voice.Companion.onyx: Voice
        get() = Voice("onyx")

    public val Voice.Companion.nova: Voice
        get() = Voice("nova")

    public val Voice.Companion.shimmer: Voice
        get() = Voice("shimmer")

    public val AudioFormat.Companion.mp3: AudioFormat
        get() = AudioFormat("mp3")

    public val AudioFormat.Companion.opus: AudioFormat
        get() = AudioFormat("opus")

    public val AudioFormat.Companion.aac: AudioFormat
        get() = AudioFormat("aac")

    public val AudioFormat.Companion.flac: AudioFormat
        get() = AudioFormat("flac")

    fun required(model: ModelId, input: String, voice: Voice): SpeechRequestBuilder =
        SpeechRequestBuilder().apply {
            this.model = model
            this.input = input
            this.voice = voice
        }

    private fun validate(modelId: ModelId?): ModelId {
        requireNotNull(modelId) { "Model is required" }
        require(modelId == ModelId.tts1 || modelId == ModelId.tts1hd) {
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



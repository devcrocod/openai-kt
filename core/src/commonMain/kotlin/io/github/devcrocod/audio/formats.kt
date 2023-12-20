package io.github.devcrocod.audio

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * The format to audio in. Supported formats are `mp3`, `opus`, `aac`, and `flac`.
 *
 * Values: mp3, opus, aac, flac
 */
@Serializable
@JvmInline
public value class AudioFormat(public val value: String) {
    public companion object {
        context(SpeechRequestBuilder)
        public val mp3: AudioFormat
            get() = AudioFormat("mp3")

        context(SpeechRequestBuilder)
        public val opus: AudioFormat
            get() = AudioFormat("opus")

        context(SpeechRequestBuilder)
        public val aac: AudioFormat
            get() = AudioFormat("aac")

        context(SpeechRequestBuilder)
        public val flac: AudioFormat
            get() = AudioFormat("flac")
    }
}


@Serializable
@JvmInline
public value class ResponseFormat(public val value: String) {
    public companion object {
        context(TranscriptionRequestBuilder)
        public val json: ResponseFormat
            get() = ResponseFormat("json")

        context(TranscriptionRequestBuilder)
        public val text: ResponseFormat
            get() = ResponseFormat("text")

        context(TranscriptionRequestBuilder)
        public val verboseJson: ResponseFormat
            get() = ResponseFormat("verbose_json")

        context(TranscriptionRequestBuilder)
        public val srt: ResponseFormat
            get() = ResponseFormat("srt")

        context(TranscriptionRequestBuilder)
        public val vtt: ResponseFormat
            get() = ResponseFormat("vtt")
    }
}

/**
 * The voice to use when generating the audio.
 * Supported voices are `alloy`, `echo`, `fable`, `onyx`, `nova`, and `shimmer`.
 * Previews of the voices are available in the [Text to speech guide](/docs/guides/text-to-speech/voice-options).
 *
 * Values: alloy,echo,fable,onyx,nova,shimmer
 */
@Serializable
@JvmInline
public value class Voice(public val value: String) {
    public companion object {
        context(SpeechRequestBuilder)
        public val alloy: Voice
            get() = Voice("alloy")

        context(SpeechRequestBuilder)
        public val echo: Voice
            get() = Voice("echo")

        context(SpeechRequestBuilder)
        public val fable: Voice
            get() = Voice("fable")

        context(SpeechRequestBuilder)
        public val onyx: Voice
            get() = Voice("onyx")

        context(SpeechRequestBuilder)
        public val nova: Voice
            get() = Voice("nova")

        context(SpeechRequestBuilder)
        public val shimmer: Voice
            get() = Voice("shimmer")
    }
}
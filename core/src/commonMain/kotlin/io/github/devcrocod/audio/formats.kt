package io.github.devcrocod.audio

import kotlinx.serialization.Serializable

/**
 * The format to audio in. Supported formats are `mp3`, `opus`, `aac`, and `flac`.
 *
 * Values: mp3, opus, aac, flac
 */
@Serializable
public value class AudioFormat(val value: String) {
    companion object
}


@Serializable
public value class ResponseFormat(val value: String) {
    companion object
}

/**
 * The voice to use when generating the audio.
 * Supported voices are `alloy`, `echo`, `fable`, `onyx`, `nova`, and `shimmer`.
 * Previews of the voices are available in the [Text to speech guide](/docs/guides/text-to-speech/voice-options).
 *
 * Values: alloy,echo,fable,onyx,nova,shimmer
 */
@Serializable
public value class Voice(public val value: String) {
    companion object
}
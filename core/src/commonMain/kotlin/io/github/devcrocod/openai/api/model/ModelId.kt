package io.github.devcrocod.openai.model

import io.github.devcrocod.openai.api.audio.SpeechRequestBuilder
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
public value class ModelId(public val id: String) {
    public companion object {
        context(SpeechRequestBuilder)
        public val `tts-1`: ModelId
            get() = ModelId("tts-1")

        context(SpeechRequestBuilder)
        public val `tts-1-hd`: ModelId
            get() = ModelId("tts-1-hd")
    }
}

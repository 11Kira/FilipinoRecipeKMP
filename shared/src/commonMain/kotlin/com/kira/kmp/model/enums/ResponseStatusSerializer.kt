package com.kira.kmp.model.enums

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ResponseStatusSerializer : KSerializer<ResponseStatus> {
    override val descriptor = PrimitiveSerialDescriptor("ResponseStatus", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ResponseStatus {
        val input = decoder.decodeString()
        // If the server returns "401", "403", or anything unexpected,
        // we catch it here and return UNKNOWN instead of crashing.
        return try {
            ResponseStatus.valueOf(input)
        } catch (e: Exception) {
            ResponseStatus.UNKNOWN
        }
    }

    override fun serialize(encoder: Encoder, value: ResponseStatus) {
        encoder.encodeString(value.name)
    }
}
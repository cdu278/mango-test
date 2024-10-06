package cdu278.mangotest.profile

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class BirthdaySerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("birthday", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(LocalDate.Formats.ISO))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), LocalDate.Formats.ISO)
    }
}
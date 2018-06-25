package com.jbb.server.shared.map;

import java.util.Arrays;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;

public class KryoMapper {
    private static final int MIN_OUTPUT_BUFFER_SIZE = 0x400;
    private static final int MAX_OUTPUT_BUFFER_SIZE = 0x100000;

    private final Kryo kryo;
    private final Output output;

    public KryoMapper(Class[] registerClasses, int startId) {
        output = new Output(MIN_OUTPUT_BUFFER_SIZE, MAX_OUTPUT_BUFFER_SIZE);
        kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setDefaultSerializer(VersionFieldSerializer.class);

        for (int i = 0; i < registerClasses.length; i++) {
            Class c = registerClasses[i];
            if (c == null) continue;

            Registration r = kryo.getRegistration(c);
            int registrationId = startId + i;
            if ((r == null) || (r.getId() != registrationId)) {
                kryo.register(c, kryo.getDefaultSerializer(c), registrationId);
            }
        }
    }

    public byte[] writeObject(Object object) {
        output.clear();
        kryo.writeObject(output, object);
        output.flush();
        return Arrays.copyOf(output.getBuffer(), output.position());
    }

    public <T> T readObject(byte[] bytes, Class<T> clazz) {
        try (Input input = new Input(Arrays.copyOf(bytes, bytes.length))) {
            return kryo.readObject(input, clazz);
        }
    }

    Kryo getKryo() {
        return kryo;
    }
}

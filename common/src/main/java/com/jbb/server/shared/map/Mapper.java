package com.jbb.server.shared.map;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.jbb.domain.LimitPolicy;
import com.jbb.domain.LoanPlatformPolicy;
import com.jbb.domain.SourcePolicy;
import com.jbb.server.common.concurrent.SimpleObjectPool;
import com.jbb.server.common.exception.ExecutionException;
import com.jbb.server.common.util.CodecUtil;

/**
 * Object to byte[] and byte[] to object mapping wrapper class. This utility is used in sending and receiving data
 * objects to and from message queue.
 */
public class Mapper {
    private static Logger logger = LoggerFactory.getLogger(Mapper.class);

    // The classes order must not be changed!!!
    // New classes must be added only to the end of this array!!!
    // To remove a class replace it to null.
    private static final Class[] registerClasses = {ArrayList.class, HashMap.class, Timestamp.class,};
    // New classes must be added only to the end of this array!!!

    private static final int START_REGISTRATION_ID = 100;

    private static final SimpleObjectPool<KryoMapper> kryoPool = new SimpleObjectPool<>(10, () -> {
        final KryoMapper kryoMapper = new KryoMapper(registerClasses, START_REGISTRATION_ID);
        final Kryo kryo = kryoMapper.getKryo();

        CollectionSerializer stringListSerializer = new CollectionSerializer();
        stringListSerializer.setElementClass(String.class, kryo.getSerializer(String.class));
        stringListSerializer.setElementsCanBeNull(false);

        return kryoMapper;
    });

    static int poolSize() {
        return kryoPool.poolSize();
    }

    static byte[] writeObject(Object object) {
        return kryoPool.call(k -> k.writeObject(object));
    }

    public static <T> T readObject(byte[] bytes, Class<T> clazz) {
        for (int i = 4; i >= 0; i--) {
            try {
                return kryoPool.call(k -> k.readObject(bytes, clazz));
            } catch (KryoException e) {
                if (i == 0) {
                    logger.error("Exception in reading " + clazz + "\n" + new String(CodecUtil.toHex(bytes)), e);
                    throw new ExecutionException("Cannot read " + clazz, e);
                }

                logger.warn("Exception in reading " + clazz + " - " + e.toString());
            }
        }

        throw new ExecutionException("Cannot read " + clazz);
    }

    
}

/*
 * Copyright 2011-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.output;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import io.lettuce.core.codec.RedisCodec;

/**
 * {@link Map} of keys and values output.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 *
 * @author Will Glozer
 * @author Mark Paluch
 */
public class MapOutput<K, V> extends CommandOutput<K, V, Map<K, V>> {

    private boolean initialized;

    private K key;

    public MapOutput(RedisCodec<K, V> codec) {
        super(codec, Collections.emptyMap());
    }

    @Override
    public void set(ByteBuffer bytes) {

        if (key == null) {
            key = (bytes == null) ? null : codec.decodeKey(bytes);
            return;
        }

        V value = (bytes == null) ? null : codec.decodeValue(bytes);
        output.put(key, value);
        key = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(long integer) {

        if (key == null) {
            key = (K) Long.valueOf(integer);
            return;
        }

        V value = (V) Long.valueOf(integer);
        output.put(key, value);
        key = null;
    }

    @Override
    public void set(double number) {

        if (key == null) {
            key = (K) Double.valueOf(number);
            return;
        }

        V value = (V) Double.valueOf(number);
        output.put(key, value);
        key = null;
    }

    @Override
    public void set(boolean flag) {

        if (key == null) {
            key = (K) Boolean.valueOf(flag);
            return;
        }

        V value = (V) Boolean.valueOf(flag);
        output.put(key, value);
        key = null;
    }

    @Override
    public void multi(int count) {

        if (!initialized) {
            output = new LinkedHashMap<>(count / 2, 1);
            initialized = true;
        }
    }

}

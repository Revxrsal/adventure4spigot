/*
 * This file is part of adventure4spigot, licensed under the MIT License.
 *
 *  Copyright (c) Revxrsal <reflxction.github@gmail.com>
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package adventure4spigot.reflect;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

/**
 * Represents a field that is accessible with reflection. This API
 * uses MethodHandles to get and set fields, and will propagate any
 * exceptions.
 *
 * @param <T> Field type
 */
public interface ReflectField<T> {

    /**
     * Returns the field instance from the given instance
     *
     * @param instance Instance to get from
     * @return The field instance
     */
    T get(Object instance);

    /**
     * Sets the value of the field in the given instance
     *
     * @param instance Instance to set in
     * @param value    New value
     */
    void set(Object instance, T value);

    /**
     * Represents a reflection field that is bound to an instance
     *
     * @param <T> The field type
     */
    interface BoundReflectField<T> {

        /**
         * Returns the field value
         *
         * @return the field value
         */
        T get();

        /**
         * Sets the value of the field
         *
         * @param value New value to set
         */
        void set(T value);

    }

    /**
     * Returns a {@link BoundReflectField} that is bound to the given
     * instance
     *
     * @param instance Instance to bind to
     * @return The bound reflect field
     */
    default BoundReflectField<T> bindTo(@Nullable Object instance) {
        return new BoundReflectField<T>() {
            @Override public T get() {
                return ReflectField.this.get(instance);
            }

            @Override public void set(T value) {
                ReflectField.this.set(instance, value);
            }
        };
    }

    /**
     * Returns a {@link ReflectField} that exposes a getter and setter for
     * the given field
     *
     * @param type      Class to get from
     * @param fieldName Field name
     * @param <T>       The field type
     * @return The {@link  ReflectField}
     */
    @SneakyThrows
    static <T> @NotNull ReflectField<T> getterAndSetter(@NotNull Class<?> type,
                                                        @NotNull String fieldName) {
        Field field = findField(type, fieldName);
        MethodHandle getter = MethodHandles.lookup().unreflectGetter(field);
        MethodHandle setter = MethodHandles.lookup().unreflectSetter(field);
        return new ReflectField<T>() {
            @SneakyThrows
            @Override public T get(Object instance) {
                if (instance == null) return (T) getter.invoke();
                return (T) getter.invoke(instance);
            }

            @SneakyThrows @Override public void set(Object instance, T value) {
                if (instance == null) setter.invoke(value);
                setter.invoke(instance, value);
            }
        };
    }

    /**
     * Returns a {@link ReflectField} that exposes a getter only for
     * the given field
     *
     * @param type      Class to get from
     * @param fieldName Field name
     * @param <T>       The field type
     * @return The {@link  ReflectField}
     */
    @SneakyThrows
    static <T> @NotNull ReflectField<T> getter(Class<?> type, String fieldName) {
        Field field = findField(type, fieldName);
        MethodHandle getter = MethodHandles.lookup().unreflectGetter(field);
        return new ReflectField<T>() {
            @SneakyThrows
            @Override public T get(Object instance) {
                return (T) getter.invoke(instance);
            }

            @SneakyThrows @Override public void set(Object instance, T value) {
                throw new UnsupportedOperationException("cannot use #set with ReflectFields that were created with #getter()");
            }
        };
    }

    /**
     * Returns a {@link ReflectField} that exposes a setter only for
     * the given field
     *
     * @param type      Class to get from
     * @param fieldName Field name
     * @param <T>       The field type
     * @return The {@link  ReflectField}
     */
    @SneakyThrows
    static <T> @NotNull ReflectField<T> setter(Class<?> type, String fieldName) {
        Field field = findField(type, fieldName);
        MethodHandle setter = MethodHandles.lookup().unreflectSetter(field);
        return new ReflectField<T>() {
            @SneakyThrows
            @Override public T get(Object instance) {
                throw new UnsupportedOperationException("cannot use #get with ReflectFields that were created with #setter()");
            }

            @SneakyThrows @Override public void set(Object instance, T value) {
                setter.invoke(instance, value);
            }
        };
    }

    /**
     * Finds the given field in the class
     *
     * @param type      Class to get from
     * @param fieldName Field name
     * @return The field
     */
    static @NotNull Field findField(Class<?> type, String fieldName) {
        Field field;
        try {
            field = type.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                field = type.getField(fieldName);
            } catch (NoSuchFieldException ex) {
                throw new IllegalArgumentException("Cannot find field " + fieldName + " in class " + type.getName());
            }
        }
        field.setAccessible(true);
        return field;
    }
}

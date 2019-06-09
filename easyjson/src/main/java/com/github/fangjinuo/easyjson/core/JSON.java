package com.github.fangjinuo.easyjson.core;

import com.github.fangjinuo.easyjson.core.type.Primitives;

import java.lang.reflect.Type;

public class JSON {

    JsonHandler jsonHandler;

    /**
     * This method serialize the specified object into its equivalent Json representation.
     * This method should be used when the specified object is not a generic type. This method uses
     * {@link Class#getClass()} to get the type for the specified object, but the
     * {@code getClass()} loses the generic type information because of the Type Erasure feature
     * of Java. Note that this method works fine if the any of the object fields are of generic type,
     * just the object itself should not be of a generic type. If the object is of generic type, use
     * {@link #toJson(Object, Type)} instead.
     */
    public String toJson(Object src) {
        if (src == null) {
            return "";
        }
        return toJson(src, src.getClass());
    }

    /**
     * This method serialize the specified object, including those of generic types, into its
     * equivalent Json representation. This method must be used if the specified object is a generic
     * type. For non-generic objects, use {@link #toJson(Object)} instead.
     **/
    public String toJson(Object src, Type typeOfSrc) {
        if (src == null) {
            return "";
        }
        return jsonHandler.serialize(src, typeOfSrc);
    }

    /**
     * This method deserializes the specified Json into an object of the specified class. It is not
     * suitable to use if the specified class is a generic type since it will not have the generic
     * type information because of the Type Erasure feature of Java. Therefore, this method should not
     * be used if the desired type is a generic type. Note that this method works fine if the any of
     * the fields of the specified object are generics, just the object itself should not be a
     * generic type. For the cases when the object is of generic type, invoke
     * {@link #fromJson(String, Type)}.
     *
     * @param <T>      the type of the desired object
     * @param json     the string from which the object is to be deserialized
     * @param classOfT the class of T
     * @return an object of type T from the string. Returns {@code null} if {@code json} is {@code null}
     * or if {@code json} is empty.
     * classOfT d4
     */
    public <T> T fromJson(String json, Class<T> classOfT) throws JsonException {
        Object object = fromJson(json, (Type) classOfT);
        return Primitives.wrap(classOfT).cast(object);
    }

    /**
     * This method deserializes the specified Json into an object of the specified type. This method
     * is useful if the specified object is a generic type. For non-generic objects, use
     * {@link #fromJson(String, Class)} instead.
     *
     * @param <T>     the type of the desired object
     * @param json    the string from which the object is to be deserialized
     * @param typeOfT The specific genericized type of src. You can obtain this type by using the
     *                {@link com.google.gson.reflect.TypeToken} class. For example, to get the type for
     *                {@code Collection<Foo>}, you should use:
     *                <pre>
     *                Type typeOfT = new TypeToken&lt;Collection&lt;Foo&gt;&gt;(){}.getType();
     *                </pre>
     * @return an object of type T from the string. Returns {@code null} if {@code json} is {@code null}.
     * @throws JsonException if json is not a valid representation for an object of type
     */
    @SuppressWarnings("unchecked")
    public <T> T fromJson(String json, Type typeOfT) throws JsonException {
        if (json == null) {
            return null;
        }
        return jsonHandler.deserialize(json, typeOfT);
    }

    public void setJsonHandler(JsonHandler jsonHandler) {
        this.jsonHandler = jsonHandler;
    }
}

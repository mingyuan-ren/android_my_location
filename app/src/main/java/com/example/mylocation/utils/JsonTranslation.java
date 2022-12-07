package com.example.mylocation.utils;

import com.google.gson.Gson;

/**
 * Used to for serializing and deserializing object
 */
public class JsonTranslation {

    private final Gson gson;


    /**
     * Parameterized constructor for initializing jsonTranslation object with Gson object
     * @param gson - gson object for object serialization/deserialization
     */
    public JsonTranslation(Gson gson) {
        this.gson = gson;
    }

    /**
     * Used to serialize object to json string
     * @param obj
     * @return
     */
    public String convertObjToString(Object obj){
        return gson.toJson(obj);
    }

    /**
     * Used to deserialize json string to a given object
     * @param str
     * @param classz
     * @return
     */
    public Object convertStringToObj(String str,Class classz){
        return gson.fromJson(str,classz);
    }
}

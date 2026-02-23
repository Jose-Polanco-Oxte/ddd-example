package com.toast.shared.domain;

import java.util.Map;

/**
 * An interface for attributes that can be converted to a map of string keys and object values.
 * This is used to represent the attributes of a domain event in a way that can be easily serialized and deserialized.
 * Each domain event should have a corresponding attributes class that implements this interface,
 * and that can be used to convert the attributes of the domain event to a map of string keys and object values.
 */
public interface PrimitiveAttributes {
    Map<String, Object> toMap();
}
package com.androidplot.util;

public interface Mapping<Key, Value> {
    Key get(Value value);
}

package com.androidplot.util;

import java.util.HashMap;
import java.util.List;

public class LayerHash<KeyType, ValueType> implements Layerable<KeyType> {
    private HashMap<KeyType, ValueType> hash = new HashMap<>();
    private LinkedLayerList<KeyType> zlist = new LinkedLayerList<>();

    public int size() {
        return this.zlist.size();
    }

    public ValueType get(KeyType key) {
        return this.hash.get(key);
    }

    public List<KeyType> getKeysAsList() {
        return this.zlist;
    }

    public synchronized void addToTop(KeyType key, ValueType value) {
        if (this.hash.containsKey(key)) {
            this.hash.put(key, value);
        } else {
            this.hash.put(key, value);
            this.zlist.addToTop(key);
        }
    }

    public synchronized void addToBottom(KeyType key, ValueType value) {
        if (this.hash.containsKey(key)) {
            this.hash.put(key, value);
        } else {
            this.hash.put(key, value);
            this.zlist.addToBottom(key);
        }
    }

    public synchronized boolean moveToTop(KeyType element) {
        boolean moveToTop;
        if (!this.hash.containsKey(element)) {
            moveToTop = false;
        } else {
            moveToTop = this.zlist.moveToTop(element);
        }
        return moveToTop;
    }

    public synchronized boolean moveAbove(KeyType objectToMove, KeyType reference) {
        boolean z;
        if (objectToMove == reference) {
            throw new IllegalArgumentException("Illegal argument to moveAbove(A, B); A cannot be equal to B.");
        } else if (!this.hash.containsKey(reference) || !this.hash.containsKey(objectToMove)) {
            z = false;
        } else {
            z = this.zlist.moveAbove(objectToMove, reference);
        }
        return z;
    }

    public synchronized boolean moveBeneath(KeyType objectToMove, KeyType reference) {
        boolean z;
        if (objectToMove == reference) {
            throw new IllegalArgumentException("Illegal argument to moveBeaneath(A, B); A cannot be equal to B.");
        } else if (!this.hash.containsKey(reference) || !this.hash.containsKey(objectToMove)) {
            z = false;
        } else {
            z = this.zlist.moveBeneath(objectToMove, reference);
        }
        return z;
    }

    public synchronized boolean moveToBottom(KeyType key) {
        boolean moveToBottom;
        if (!this.hash.containsKey(key)) {
            moveToBottom = false;
        } else {
            moveToBottom = this.zlist.moveToBottom(key);
        }
        return moveToBottom;
    }

    public synchronized boolean moveUp(KeyType key) {
        boolean moveUp;
        if (!this.hash.containsKey(key)) {
            moveUp = false;
        } else {
            moveUp = this.zlist.moveUp(key);
        }
        return moveUp;
    }

    public synchronized boolean moveDown(KeyType key) {
        boolean moveDown;
        if (!this.hash.containsKey(key)) {
            moveDown = false;
        } else {
            moveDown = this.zlist.moveDown(key);
        }
        return moveDown;
    }

    public List<KeyType> elements() {
        return this.zlist;
    }

    public List<KeyType> keys() {
        return elements();
    }

    public synchronized boolean remove(KeyType key) {
        boolean z;
        if (this.hash.containsKey(key)) {
            this.hash.remove(key);
            this.zlist.remove(key);
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public ValueType getTop() {
        return this.hash.get(this.zlist.getLast());
    }

    public ValueType getBottom() {
        return this.hash.get(this.zlist.getFirst());
    }

    public ValueType getAbove(KeyType key) {
        int index = this.zlist.indexOf(key);
        if (index < 0 || index >= size() - 1) {
            return null;
        }
        return this.hash.get(this.zlist.get(index + 1));
    }

    public ValueType getBeneath(KeyType key) {
        int index = this.zlist.indexOf(key);
        if (index > 0) {
            return this.hash.get(this.zlist.get(index - 1));
        }
        return null;
    }
}

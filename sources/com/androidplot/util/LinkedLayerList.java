package com.androidplot.util;

import java.util.LinkedList;
import java.util.List;

public class LinkedLayerList<Type> extends LinkedList<Type> implements Layerable<Type> {
    private LayerListOrganizer<Type> organizer = new LayerListOrganizer<>(this);

    public boolean moveToTop(Type element) {
        return this.organizer.moveToTop(element);
    }

    public boolean moveAbove(Type objectToMove, Type reference) {
        return this.organizer.moveAbove(objectToMove, reference);
    }

    public boolean moveBeneath(Type objectToMove, Type reference) {
        return this.organizer.moveBeneath(objectToMove, reference);
    }

    public boolean moveToBottom(Type key) {
        return this.organizer.moveToBottom(key);
    }

    public boolean moveUp(Type key) {
        return this.organizer.moveUp(key);
    }

    public boolean moveDown(Type key) {
        return this.organizer.moveDown(key);
    }

    public List<Type> elements() {
        return this.organizer.elements();
    }

    public void addToBottom(Type element) {
        this.organizer.addToBottom(element);
    }

    public void addToTop(Type element) {
        this.organizer.addToTop(element);
    }
}

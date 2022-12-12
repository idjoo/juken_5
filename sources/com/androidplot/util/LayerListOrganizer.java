package com.androidplot.util;

import java.util.List;

public class LayerListOrganizer<ElementType> implements Layerable<ElementType> {
    private static final int ONE = 1;
    private static final int ZERO = 0;
    private List<ElementType> list;

    public LayerListOrganizer(List<ElementType> list2) {
        this.list = list2;
    }

    public boolean moveToTop(ElementType element) {
        if (!this.list.remove(element)) {
            return false;
        }
        this.list.add(this.list.size(), element);
        return true;
    }

    public boolean moveAbove(ElementType objectToMove, ElementType reference) {
        if (objectToMove == reference) {
            throw new IllegalArgumentException("Illegal argument to moveAbove(A, B); A cannot be equal to B.");
        }
        this.list.remove(objectToMove);
        this.list.add(this.list.indexOf(reference) + 1, objectToMove);
        return true;
    }

    public boolean moveBeneath(ElementType objectToMove, ElementType reference) {
        if (objectToMove == reference) {
            throw new IllegalArgumentException("Illegal argument to moveBeaneath(A, B); A cannot be equal to B.");
        }
        this.list.remove(objectToMove);
        this.list.add(this.list.indexOf(reference), objectToMove);
        return true;
    }

    public boolean moveToBottom(ElementType key) {
        this.list.remove(key);
        this.list.add(0, key);
        return true;
    }

    public boolean moveUp(ElementType key) {
        int widgetIndex = this.list.indexOf(key);
        if (widgetIndex == -1) {
            return false;
        }
        if (widgetIndex >= this.list.size() - 1) {
            return true;
        }
        return moveAbove(key, this.list.get(widgetIndex + 1));
    }

    public boolean moveDown(ElementType key) {
        int widgetIndex = this.list.indexOf(key);
        if (widgetIndex == -1) {
            return false;
        }
        if (widgetIndex <= 0) {
            return true;
        }
        return moveBeneath(key, this.list.get(widgetIndex - 1));
    }

    public List<ElementType> elements() {
        return this.list;
    }

    public void addToBottom(ElementType element) {
        this.list.add(0, element);
    }

    public void addToTop(ElementType element) {
        this.list.add(this.list.size(), element);
    }
}

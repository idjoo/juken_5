package com.androidplot.ui;

import android.graphics.RectF;
import java.util.Iterator;

public class FixedTableModel extends TableModel {
    private float cellHeight;
    private float cellWidth;

    public FixedTableModel(float cellWidth2, float cellHeight2, TableOrder order) {
        super(order);
        setCellWidth(cellWidth2);
        setCellHeight(cellHeight2);
    }

    public Iterator<RectF> getIterator(RectF tableRect, int totalElements) {
        return new FixedTableModelIterator(this, tableRect, totalElements);
    }

    public float getCellWidth() {
        return this.cellWidth;
    }

    public void setCellWidth(float cellWidth2) {
        this.cellWidth = cellWidth2;
    }

    public float getCellHeight() {
        return this.cellHeight;
    }

    public void setCellHeight(float cellHeight2) {
        this.cellHeight = cellHeight2;
    }

    private class FixedTableModelIterator implements Iterator<RectF> {
        private int lastElement;
        private RectF lastRect;
        private FixedTableModel model;
        private int numElements;
        private RectF tableRect;

        protected FixedTableModelIterator(FixedTableModel model2, RectF tableRect2, int numElements2) {
            this.model = model2;
            this.tableRect = tableRect2;
            this.numElements = numElements2;
            this.lastRect = new RectF(tableRect2.left, tableRect2.top, tableRect2.left + model2.getCellWidth(), tableRect2.top + model2.getCellHeight());
        }

        public boolean hasNext() {
            return this.lastElement < this.numElements && (!isColumnFinished() || !isRowFinished());
        }

        private boolean isColumnFinished() {
            return this.lastRect.bottom + this.model.getCellHeight() > this.tableRect.height();
        }

        private boolean isRowFinished() {
            return this.lastRect.right + this.model.getCellWidth() > this.tableRect.width();
        }

        public RectF next() {
            RectF rectF;
            try {
                if (this.lastElement == 0) {
                    rectF = this.lastRect;
                } else if (this.lastElement >= this.numElements) {
                    throw new IndexOutOfBoundsException();
                } else {
                    switch (this.model.getOrder()) {
                        case ROW_MAJOR:
                            if (!isColumnFinished()) {
                                moveDown();
                                break;
                            } else {
                                moveOverAndUp();
                                break;
                            }
                        case COLUMN_MAJOR:
                            if (!isRowFinished()) {
                                moveOver();
                                break;
                            } else {
                                moveDownAndBack();
                                break;
                            }
                        default:
                            throw new UnsupportedOperationException();
                    }
                    rectF = this.lastRect;
                    this.lastElement++;
                }
                return rectF;
            } finally {
                this.lastElement++;
            }
        }

        private void moveDownAndBack() {
            this.lastRect.offsetTo(this.tableRect.left, this.lastRect.bottom);
        }

        private void moveOverAndUp() {
            this.lastRect.offsetTo(this.lastRect.right, this.tableRect.top);
        }

        private void moveOver() {
            this.lastRect.offsetTo(this.lastRect.right, this.lastRect.top);
        }

        private void moveDown() {
            this.lastRect.offsetTo(this.lastRect.left, this.lastRect.bottom);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

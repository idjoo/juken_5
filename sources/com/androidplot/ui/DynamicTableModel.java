package com.androidplot.ui;

import android.graphics.RectF;
import com.androidplot.ui.TableModel;
import java.util.Iterator;

public class DynamicTableModel extends TableModel {
    private int numColumns;
    private int numRows;

    public DynamicTableModel(int numColumns2, int numRows2) {
        this(numColumns2, numRows2, TableOrder.ROW_MAJOR);
    }

    public DynamicTableModel(int numColumns2, int numRows2, TableOrder order) {
        super(order);
        this.numColumns = numColumns2;
        this.numRows = numRows2;
    }

    public TableModelIterator getIterator(RectF tableRect, int totalElements) {
        return new TableModelIterator(this, tableRect, totalElements);
    }

    public RectF getCellRect(RectF tableRect, int numElements) {
        RectF cellRect = new RectF();
        cellRect.left = tableRect.left;
        cellRect.top = tableRect.top;
        cellRect.bottom = tableRect.top + calculateCellSize(tableRect, TableModel.Axis.ROW, numElements);
        cellRect.right = tableRect.left + calculateCellSize(tableRect, TableModel.Axis.COLUMN, numElements);
        return cellRect;
    }

    private float calculateCellSize(RectF tableRect, TableModel.Axis axis, int numElementsInTable) {
        int axisElements = 0;
        float axisSizePix = 0.0f;
        switch (axis) {
            case ROW:
                axisElements = this.numRows;
                axisSizePix = tableRect.height();
                break;
            case COLUMN:
                axisElements = this.numColumns;
                axisSizePix = tableRect.width();
                break;
        }
        if (axisElements != 0) {
            return axisSizePix / ((float) axisElements);
        }
        return axisSizePix / ((float) numElementsInTable);
    }

    public int getNumRows() {
        return this.numRows;
    }

    public void setNumRows(int numRows2) {
        this.numRows = numRows2;
    }

    public int getNumColumns() {
        return this.numColumns;
    }

    public void setNumColumns(int numColumns2) {
        this.numColumns = numColumns2;
    }

    private class TableModelIterator implements Iterator<RectF> {
        private int calculatedColumns;
        private int calculatedNumElements;
        private int calculatedRows;
        private DynamicTableModel dynamicTableModel;
        private boolean isOk = true;
        int lastColumn = 0;
        int lastElement = 0;
        private RectF lastElementRect;
        int lastRow = 0;
        private TableOrder order;
        private RectF tableRect;
        private int totalElements;

        public TableModelIterator(DynamicTableModel dynamicTableModel2, RectF tableRect2, int totalElements2) {
            this.dynamicTableModel = dynamicTableModel2;
            this.tableRect = tableRect2;
            this.totalElements = totalElements2;
            this.order = dynamicTableModel2.getOrder();
            if (dynamicTableModel2.getNumColumns() == 0 && dynamicTableModel2.getNumRows() >= 1) {
                this.calculatedRows = dynamicTableModel2.getNumRows();
                this.calculatedColumns = Float.valueOf((((float) totalElements2) / ((float) this.calculatedRows)) + 0.5f).intValue();
            } else if (dynamicTableModel2.getNumRows() == 0 && dynamicTableModel2.getNumColumns() >= 1) {
                this.calculatedColumns = dynamicTableModel2.getNumColumns();
                this.calculatedRows = Float.valueOf((((float) totalElements2) / ((float) this.calculatedColumns)) + 0.5f).intValue();
            } else if (dynamicTableModel2.getNumColumns() == 0 && dynamicTableModel2.getNumRows() == 0) {
                this.calculatedRows = 1;
                this.calculatedColumns = totalElements2;
            } else {
                this.calculatedRows = dynamicTableModel2.getNumRows();
                this.calculatedColumns = dynamicTableModel2.getNumColumns();
            }
            this.calculatedNumElements = this.calculatedRows * this.calculatedColumns;
            this.lastElementRect = dynamicTableModel2.getCellRect(tableRect2, totalElements2);
        }

        public boolean hasNext() {
            return this.isOk && this.lastElement < this.calculatedNumElements;
        }

        public RectF next() {
            if (!hasNext()) {
                this.isOk = false;
                throw new IndexOutOfBoundsException();
            } else if (this.lastElement == 0) {
                this.lastElement++;
                return this.lastElementRect;
            } else {
                RectF nextElementRect = new RectF(this.lastElementRect);
                switch (this.order) {
                    case ROW_MAJOR:
                        if (this.dynamicTableModel.getNumColumns() > 0 && this.lastColumn >= this.dynamicTableModel.getNumColumns() - 1) {
                            nextElementRect.offsetTo(this.tableRect.left, this.lastElementRect.bottom);
                            this.lastColumn = 0;
                            this.lastRow++;
                            break;
                        } else {
                            nextElementRect.offsetTo(this.lastElementRect.right, this.lastElementRect.top);
                            this.lastColumn++;
                            break;
                        }
                    case COLUMN_MAJOR:
                        if (this.dynamicTableModel.getNumRows() > 0 && this.lastRow >= this.dynamicTableModel.getNumRows() - 1) {
                            nextElementRect.offsetTo(this.lastElementRect.right, this.tableRect.top);
                            this.lastRow = 0;
                            this.lastColumn++;
                            break;
                        } else {
                            nextElementRect.offsetTo(this.lastElementRect.left, this.lastElementRect.bottom);
                            this.lastRow++;
                            break;
                        }
                        break;
                    default:
                        this.isOk = false;
                        throw new IllegalArgumentException();
                }
                this.lastElement++;
                this.lastElementRect = nextElementRect;
                return nextElementRect;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

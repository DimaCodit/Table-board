package com.dr.test;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;

public class ContextMenuFix extends ContextMenu {
    private TableColumn tableColumn;

    public TableColumn getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(TableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

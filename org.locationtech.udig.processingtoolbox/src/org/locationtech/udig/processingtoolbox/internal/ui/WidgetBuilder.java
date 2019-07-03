/*
 * uDig - User Friendly Desktop Internet GIS client
 * (C) MangoSystem - www.mangosystem.com 
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.processingtoolbox.internal.ui;

import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.geotools.util.logging.Logging;

/**
 * SWT Widget UI Builder
 * 
 * @author Minpa Lee, MangoSystem
 * 
 * @source $URL$
 */
public class WidgetBuilder {
    protected final Logger LOGGER = Logging.getLogger(WidgetBuilder.class);

    public static WidgetBuilder newInstance() {
        return new WidgetBuilder();
    }

    public GridLayout createGridLayout(int numColumns, boolean makeColumnsEqualWidth,
            int marginWidth, int marginHeight) {
        GridLayout layout = new GridLayout(numColumns, makeColumnsEqualWidth);
        layout.marginWidth = marginWidth;
        layout.marginHeight = marginHeight;

        return layout;
    }

    public Label createLabel(Composite parent, String text, String tooltip, int colspan) {
        Label label = new Label(parent, SWT.NONE);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, colspan, 1));
        if (text != null) {
            label.setText(text);
        }
        label.setToolTipText(tooltip);
        return label;
    }

    public CLabel createLabel(Composite parent, String text, String tooltip, Image image,
            int colspan) {
        CLabel label = new CLabel(parent, SWT.NONE);
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, colspan, 1));
        if (text != null) {
            label.setText(text);
        }

        if (image != null) {
            label.setImage(image);
        }

        label.setToolTipText(tooltip);
        return label;
    }

    public Button createButton(Composite parent, String text, String tooltip, int colspan) {
        GridData laytoutData = new GridData(SWT.LEFT, SWT.CENTER, false, false, colspan, 1);
        return createButton(parent, text, tooltip, laytoutData);
    }

    public Button createButton(Composite parent, String text, String tooltip, GridData laytoutData) {
        Button button = new Button(parent, SWT.NONE);
        button.setLayoutData(laytoutData);
        if (text != null) {
            button.setText(text);
        }
        if (tooltip != null) {
            button.setToolTipText(tooltip);
        }
        return button;
    }

    public Button createCheckbox(Composite parent, String text, String tooltip, int colspan) {
        Button button = new Button(parent, SWT.CHECK);
        button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, colspan, 1));
        button.setText(text);
        button.setToolTipText(tooltip);
        return button;
    }

    public Button createRadioButton(Composite parent, String text, String tooltip, int colspan) {
        Button button = new Button(parent, SWT.RADIO);
        button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, colspan, 1));
        button.setText(text);
        button.setToolTipText(tooltip);
        return button;
    }

    public Text createText(Composite parent, String text, int colspan) {
        return createText(parent, text, colspan, false);
    }

    public Text createText(Composite parent, String text, int colspan, boolean alignRight) {
        Text txt = null;
        if (alignRight) {
            txt = new Text(parent, SWT.RIGHT | SWT.BORDER);
        } else {
            txt = new Text(parent, SWT.BORDER);
        }
        txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, colspan, 1));
        if (text != null) {
            txt.setText(text);
        }
        return txt;
    }

    public Spinner createSpinner(Composite parent, int selection, int minimum, int maximum,
            int digits, int increment, int pageIncrement, int colspan) {
        Spinner spinner = new Spinner(parent, SWT.RIGHT_TO_LEFT | SWT.BORDER);
        spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, colspan, 1));
        spinner.setValues(selection, minimum, maximum, digits, increment, pageIncrement);

        return spinner;
    }

    public Slider createSlider(Composite parent, int selection, int minimum, int maximum,
            int digits, int increment, int pageIncrement, int colspan) {
        Slider slider = new Slider(parent, SWT.HORIZONTAL | SWT.BORDER);
        slider.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, colspan, 1));
        slider.setValues(selection, minimum, maximum, digits, increment, pageIncrement);

        return slider;
    }

    public Combo createCombo(Composite parent, int colspan) {
        return createCombo(parent, colspan, true);
    }

    public Combo createCombo(Composite parent, int colspan, boolean readOlny) {
        Combo combo = null;
        if (readOlny) {
            combo = new Combo(parent, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
        } else {
            combo = new Combo(parent, SWT.NONE | SWT.DROP_DOWN);
        }

        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, colspan, 1));
        return combo;
    }

    public CTabFolder createTabFolder(Composite parent, int colspan) {
        CTabFolder tabFolder = new CTabFolder(parent, SWT.NONE);
        tabFolder.setUnselectedCloseVisible(false);
        tabFolder.setLayout(new FillLayout());
        tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        return tabFolder;
    }

    public CTabItem createTabItem(CTabFolder parent, String text) {
        CTabItem tabItem = new CTabItem(parent, SWT.NONE);
        tabItem.setText(text);
        return tabItem;
    }

    public Group createGroup(Composite parent, String text, int colspan) {
        return createGroup(parent, text, true, colspan);
    }

    public Group createGroup(Composite parent, String text, boolean titleBold, int colspan) {
        return createGroup(parent, text, titleBold, colspan, 0, 2);
    }

    public Group createGroup(Composite parent, String text, boolean titleBold, int colspan,
            int heightHint, int numColumns) {
        Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);

        if (titleBold) {
            group.setFont(new Font(parent.getDisplay(), createFontData(group.getFont(), SWT.BOLD)));
        }

        if (text != null) {
            group.setText(text);
        }

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, colspan, 1);
        if (heightHint > 0) {
            gridData.heightHint = heightHint;
        }

        group.setLayoutData(gridData);
        group.setLayout(new GridLayout(numColumns, false));

        return group;
    }

    public FontData[] createFontData(Font originFont, int style) {
        FontData[] fontData = originFont.getFontData();
        for (int i = 0; i < fontData.length; ++i) {
            fontData[i].setStyle(style);
        }
        return fontData;
    }

    public Table createListTable(Composite composite, String[] columns, int colspan) {
        return createListTable(composite, columns, colspan, 200);
    }

    public Table createListTable(Composite composite, String[] columns, int colspan, int heightHint) {
        Table table = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
                | SWT.FULL_SELECTION);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, colspan, 1);
        gridData.heightHint = heightHint == 0 ? 200 : heightHint;

        table.setLayoutData(gridData);
        table.setHeaderVisible(false);
        table.setLinesVisible(false);

        final int tableWidth = 200;
        for (int i = 0; i < columns.length; ++i) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(columns[i]);
            column.setWidth(tableWidth / columns.length);
        }

        return table;
    }

    public Table createTable(Composite composite, String[] columns, int colspan) {
        return createTable(composite, columns, colspan, 200);
    }

    public Table createTable(Composite composite, String[] columns, int colspan, int heightHint) {
        Table table = new Table(composite, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL
                | SWT.MULTI | SWT.FULL_SELECTION);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, colspan, 1);
        gridData.heightHint = heightHint == 0 ? 250 : heightHint;

        table.setLayoutData(gridData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        final int tableWidth = 550;
        for (int i = 0; i < columns.length; ++i) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(columns[i]);
            column.setWidth(tableWidth / columns.length);
        }

        return table;
    }

    public Table createEditableTable(Composite composite, String[] columns, int colspan) {
        final Table table = createTable(composite, columns, colspan);

        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        table.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    final TableItem item = table.getItem(index);
                    for (int i = 0; i < table.getColumnCount() - 1; i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(pt)) {
                            final int column = i;
                            final Text text = new Text(table, SWT.NONE);
                            Listener textListener = new Listener() {
                                @Override
                                public void handleEvent(final Event e) {
                                    switch (e.type) {
                                    case SWT.FocusOut:
                                        item.setText(column, text.getText());
                                        text.dispose();
                                        break;
                                    case SWT.Traverse:
                                        switch (e.detail) {
                                        case SWT.TRAVERSE_RETURN:
                                            item.setText(column, text.getText());
                                            // FALL THROUGH
                                        case SWT.TRAVERSE_ESCAPE:
                                            text.dispose();
                                            e.doit = false;
                                        }
                                        break;
                                    }
                                }
                            };

                            text.addListener(SWT.FocusOut, textListener);
                            text.addListener(SWT.Traverse, textListener);
                            editor.setEditor(text, item, i);
                            text.setText(item.getText(i));
                            text.selectAll();
                            text.setFocus();
                            return;
                        }

                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }

                    if (!visible) {
                        return;
                    }

                    index++;
                }
            }
        });

        return table;
    }

    public Table createEditableTable(Composite composite, String[] columns, int colspan,
            final int editableRange) {
        final Table table = createTable(composite, columns, colspan);

        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        table.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    final TableItem item = table.getItem(index);
                    for (int i = 0; i < editableRange; i++) {
                        Rectangle rect = item.getBounds(i);
                        if (rect.contains(pt)) {
                            final int column = i;
                            final Text text = new Text(table, SWT.NONE);
                            Listener textListener = new Listener() {
                                @Override
                                public void handleEvent(final Event e) {
                                    switch (e.type) {
                                    case SWT.FocusOut:
                                        item.setText(column, text.getText());
                                        text.dispose();
                                        break;
                                    case SWT.Traverse:
                                        switch (e.detail) {
                                        case SWT.TRAVERSE_RETURN:
                                            item.setText(column, text.getText());
                                        case SWT.TRAVERSE_ESCAPE:
                                            text.dispose();
                                            e.doit = false;
                                        }
                                        break;
                                    }
                                }
                            };

                            text.addListener(SWT.FocusOut, textListener);
                            text.addListener(SWT.Traverse, textListener);
                            editor.setEditor(text, item, i);
                            text.setText(item.getText(i));
                            text.selectAll();
                            text.setFocus();
                            return;
                        }

                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }

                    if (!visible) {
                        return;
                    }

                    index++;
                }
            }
        });

        return table;
    }

    public Table createSchemaEditableTable(Composite composite, String[] columns,
            final String[] comboItems, int colspan, int rowspan, boolean itemCheck) {
        int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION;
        if (itemCheck) {
            style = SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI
                    | SWT.FULL_SELECTION;
        }

        final Table table = new Table(composite, style);

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, colspan, rowspan);
        gridData.heightHint = 200;

        table.setLayoutData(gridData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        final int tableWidth = 550;
        for (int i = 0; i < columns.length; ++i) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(columns[i]);
            column.setWidth(tableWidth / columns.length);
        }

        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        table.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int indexItem = table.getTopIndex();
                while (indexItem < table.getItemCount()) {
                    boolean visible = false;
                    final TableItem item = table.getItem(indexItem);
                    for (int index = 0; index < 3; index++) {
                        Rectangle rect = item.getBounds(index);
                        if (rect.contains(pt)) {
                            final int column = index;

                            // 첫번째와 세번째는 텍스트, 두번째는 콤보박스
                            if (column == 1) {
                                final Combo combo = new Combo(table, SWT.NONE | SWT.READ_ONLY);
                                combo.setItems(comboItems);
                                combo.addSelectionListener(new SelectionAdapter() {
                                    @Override
                                    public void widgetSelected(SelectionEvent e) {
                                        item.setText(column, combo.getText());
                                    }
                                });

                                combo.addFocusListener(new FocusAdapter() {
                                    @Override
                                    public void focusLost(FocusEvent e) {
                                        combo.setVisible(false);
                                    }

                                    @Override
                                    public void focusGained(FocusEvent e) {
                                        combo.setVisible(true);
                                    }
                                });

                                combo.setText(item.getText(index));
                                combo.setFocus();
                                editor.setEditor(combo, item, index);
                            } else {
                                final Text text = new Text(table, SWT.NONE);
                                text.setTextLimit(10);
                                text.addModifyListener(new ModifyListener() {
                                    @Override
                                    public void modifyText(ModifyEvent e) {
                                        item.setText(column, text.getText());
                                    }
                                });

                                text.addFocusListener(new FocusAdapter() {
                                    @Override
                                    public void focusLost(FocusEvent e) {
                                        text.setVisible(false);
                                    }

                                    @Override
                                    public void focusGained(FocusEvent e) {
                                        text.setVisible(true);
                                    }
                                });

                                text.setText(item.getText(index));
                                text.selectAll();
                                text.setFocus();
                                editor.setEditor(text, item, index);
                            }

                            return;
                        }

                        if (!visible && rect.intersects(clientArea)) {
                            visible = true;
                        }
                    }

                    if (!visible) {
                        return;
                    }

                    indexItem++;
                }
            }
        });

        return table;
    }

    // Utility for widget
    public int toSpinnerValue(Spinner spinner, double value) {
        return (int) (value * Math.pow(10, spinner.getDigits()));
    }

    public double fromSpinnerValue(Spinner spinner, int value) {
        return value / Math.pow(10, spinner.getDigits());
    }    
}

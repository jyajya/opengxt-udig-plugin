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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.media.jai.PlanarImage;
import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.process.spatialstatistics.core.SSUtils;
import org.geotools.process.spatialstatistics.gridcoverage.RasterHelper;
import org.geotools.util.NullProgressListener;
import org.geotools.util.logging.Logging;
import org.locationtech.udig.processingtoolbox.ToolboxPlugin;
import org.locationtech.udig.processingtoolbox.internal.Messages;
import org.locationtech.udig.processingtoolbox.styler.MapUtils;
import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.IMap;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * Query Builder Dialog
 * 
 * @author Minpa Lee, MangoSystem
 * 
 * @source $URL$
 */
@SuppressWarnings("nls")
public class QueryBuilderDialog extends Dialog {
    protected static final Logger LOGGER = Logging.getLogger(QueryBuilderDialog.class);

    // Custom Function : http://docs.geotools.org/latest/userguide/library/main/filter.html

    private final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    private IMap map = null;

    private String initSQL = null;

    private ILayer layer = null;

    private SimpleFeatureCollection features = null;

    private boolean isFeatureLayer = true;

    private Table fieldTable, valueTable;

    private Button btnSample, btnAll;

    private Combo cboLayer;

    private Text txtFilter;

    public QueryBuilderDialog(Shell parentShell, ILayer layer) {
        super(parentShell);

        this.layer = layer;
        this.features = MapUtils.getFeatures(layer);
        this.isFeatureLayer = MapUtils.isFeatureLayer(layer);
        setShellStyle(SWT.CLOSE | SWT.MIN | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL
                | SWT.RESIZE);
    }

    public QueryBuilderDialog(Shell parentShell, ILayer layer, String initSQL) {
        this(parentShell, layer);
        this.initSQL = initSQL;
    }

    public QueryBuilderDialog(Shell parentShell, SimpleFeatureCollection features) {
        super(parentShell);
        this.features = features;
        this.isFeatureLayer = true;
        setShellStyle(SWT.CLOSE | SWT.MIN | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL
                | SWT.RESIZE);
    }

    public QueryBuilderDialog(Shell parentShell, IMap map) {
        super(parentShell);

        this.map = map;
        setShellStyle(SWT.CLOSE | SWT.MIN | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL
                | SWT.RESIZE);
    }

    public QueryBuilderDialog(Shell parentShell, IMap map, String initSQL) {
        this(parentShell, map);
        this.initSQL = initSQL;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        newShell.setText(Messages.QueryDialog_title);
    }

    public String getFilter() {
        return this.initSQL;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // Clear, Test, Save..., Load..., OK, Cancel
        Button btnClear = createButton(parent, 2000, Messages.QueryDialog_Clear, false);
        Button btnTest = createButton(parent, 2001, Messages.QueryDialog_Test, false);
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);

        btnClear.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                txtFilter.setText("");
            }
        });

        btnTest.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                try {
                    if (isFeatureLayer) {
                        Filter filter = ECQL.toFilter(txtFilter.getText());
                        int count = features.subCollection(filter).size();
                        MessageDialog.openInformation(getParentShell(), Messages.QueryDialog_Test,
                                "Evaluated Count: " + count);
                    } else {
                        MessageDialog.openInformation(getParentShell(), Messages.QueryDialog_Test,
                                "Ratser layer not yet available!");
                    }
                } catch (CQLException e) {
                    MessageDialog.openInformation(getParentShell(), Messages.QueryDialog_Test,
                            e.getLocalizedMessage());
                }
            }
        });
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return ToolboxPlugin.rescaleSize(getShell(), 650, 500);
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        GridLayout layout = new GridLayout(1, false);
        layout.marginWidth = layout.marginHeight = layout.marginRight = layout.marginBottom = 2;
        area.setLayout(layout);

        Composite container = new Composite(area, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        WidgetBuilder widget = WidgetBuilder.newInstance();
        int scale = (int) parent.getShell().getDisplay().getDPI().x / 96;

        // ========================================================
        // 1. layer
        // ========================================================
        Group grpLayer = widget.createGroup(container, Messages.QueryDialog_Layer, false, 1);

        if (map != null) {
            Group grpCombo = new Group(grpLayer, SWT.SHADOW_ETCHED_IN);
            grpCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
            grpCombo.setLayout(new GridLayout(2, false));
            widget.createLabel(grpCombo, Messages.QueryDialog_Layer, Messages.QueryDialog_Layer, 1);
            cboLayer = widget.createCombo(grpCombo, 1, true);
            for (ILayer layer : map.getMapLayers()) {
                if (layer.getName() == null) {
                    continue;
                }
                cboLayer.add(layer.getName());
            }
            cboLayer.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    layer = MapUtils.getLayer(map, cboLayer.getText());
                    isFeatureLayer = MapUtils.isFeatureLayer(layer);
                    if (isFeatureLayer) {
                        features = MapUtils.getFeatures(layer);
                    } else {
                        features = null;
                    }
                    updateFields();
                }
            });
        }

        // ========================================================
        // 1. fields
        // ========================================================
        final int defaultWidth = 270;
        Group grpFields = widget.createGroup(grpLayer, Messages.QueryDialog_Fields, false, 1);
        GridData gridDataField = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
        gridDataField.widthHint = defaultWidth * scale;
        grpFields.setLayoutData(gridDataField);
        grpFields.setLayout(new GridLayout(1, true));

        fieldTable = widget.createListTable(grpFields,
                new String[] { Messages.QueryDialog_Fields }, 1);

        // select event
        fieldTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                btnSample.setEnabled(fieldTable.getSelectionCount() > 0);
                btnAll.setEnabled(fieldTable.getSelectionCount() > 0);
            }
        });

        // double click event
        fieldTable.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String selection = "[" + fieldTable.getSelection()[0].getText() + "]";
                updateExpression(selection);
            }
        });

        // ========================================================
        // 2. values
        // ========================================================
        Group grpValues = widget.createGroup(grpLayer, Messages.QueryDialog_Values, false, 1);
        grpValues.setLayout(new GridLayout(2, true));

        valueTable = widget.createListTable(grpValues,
                new String[] { Messages.QueryDialog_Fields }, 2);
        valueTable.getColumns()[0].setWidth(defaultWidth * scale);
        // double click event
        valueTable.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (valueTable.getSelectionCount() > 0) {
                    String selection = valueTable.getSelection()[0].getText();
                    updateExpression(selection);
                }
            }
        });

        GridData btnLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);

        btnSample = widget.createButton(grpValues, Messages.QueryDialog_Sample, null, btnLayout);
        btnSample.setEnabled(false);
        btnSample.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateUniqueValues(true);
            }
        });

        btnAll = widget.createButton(grpValues, Messages.QueryDialog_All, null, btnLayout);
        btnAll.setEnabled(false);
        btnAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                updateUniqueValues(false);
            }
        });

        // ========================================================
        // 3. Operators
        // ========================================================
        Group grpOpr = new Group(container, SWT.SHADOW_ETCHED_IN);
        grpOpr.setText(Messages.QueryDialog_Operators);
        grpOpr.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        grpOpr.setLayout(new GridLayout(7, true));

        // create button
        btnLayout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        final String[] oprs = new String[] { "=", "<>", "<", ">", "LIKE", "ILIKE", "%", "<=", ">=",
                "AND", "OR", "NOT", "IS", "NULL" };
        Button[] btnOp = new Button[oprs.length];
        for (int idx = 0; idx < oprs.length; idx++) {
            btnOp[idx] = widget.createButton(grpOpr, oprs[idx], oprs[idx], btnLayout);
            btnOp[idx].addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    Button current = (Button) event.widget;
                    if (current.getText().equalsIgnoreCase("%")) {
                        updateExpression(current.getText());
                    } else {
                        updateExpression(" " + current.getText());
                    }
                }
            });
        }

        // ========================================================
        // 4. SQL where clause
        // ========================================================
        Group grpSql = new Group(container, SWT.SHADOW_ETCHED_IN);
        grpSql.setText(Messages.QueryDialog_SQL_where_clause);
        grpSql.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        grpSql.setLayout(new GridLayout(1, true));

        txtFilter = new Text(grpSql, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gridData.heightHint = 50;
        txtFilter.setLayoutData(gridData);

        final Color warningColor = new Color(Display.getCurrent(), 255, 255, 200);
        final Color oldBackColor = txtFilter.getBackground();
        txtFilter.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (txtFilter.getText().length() == 0) {
                    txtFilter.setBackground(oldBackColor);
                } else {
                    try {
                        ECQL.toFilter(txtFilter.getText());
                        initSQL = txtFilter.getText();
                        txtFilter.setBackground(oldBackColor);
                    } catch (CQLException e1) {
                        txtFilter.setBackground(warningColor);
                    }
                }
            }
        });

        if (initSQL != null && initSQL.length() > 0) {
            txtFilter.setText(initSQL);
        }

        if (map != null && layer == null && cboLayer.getItemCount() > 0) {
            cboLayer.select(0);
        }
        area.pack();
        resizeTableColumn();

        if (features != null) {
            updateFields();
        }

        return area;
    }

    private void resizeTableColumn() {
        fieldTable.setRedraw(false);
        for (TableColumn column : fieldTable.getColumns()) {
            column.setWidth(fieldTable.getSize().x + 35);
        }
        fieldTable.setRedraw(true);

        valueTable.setRedraw(false);
        for (TableColumn column : valueTable.getColumns()) {
            column.setWidth(valueTable.getSize().x + 35);
        }
        valueTable.setRedraw(true);
    }

    private void updateFields() {
        fieldTable.removeAll();
        valueTable.removeAll();

        if (isFeatureLayer && features != null) {
            for (AttributeDescriptor descriptor : features.getSchema().getAttributeDescriptors()) {
                if (descriptor instanceof GeometryDescriptor) {
                    continue;
                } else {
                    TableItem item = new TableItem(fieldTable, SWT.NULL);
                    item.setText(descriptor.getLocalName());
                }
            }
        } else {
            TableItem item = new TableItem(fieldTable, SWT.NULL);
            item.setText("Value");
        }
    }

    private void updateUniqueValues(final boolean useSample) {
        valueTable.removeAll();
        Runnable runnable = new Runnable() {
            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public void run() {
                try {
                    final int max_sample = 1000;
                    boolean isNumeric = true;
                    List values = new ArrayList();

                    if (isFeatureLayer) {
                        String attribute = fieldTable.getSelection()[0].getText();

                        UniqueVisitor visitor = new UniqueVisitor(attribute);
                        // UniqueVisitor occurs npe if property has a null value
                        Filter filter = ff.not(ff.isNull(ff.property(attribute)));
                        if (useSample) {
                            visitor.setMaxFeatures(max_sample);
                        }

                        isNumeric = isNumeric(features.getSchema(), attribute);
                        features.subCollection(filter).accepts(visitor, new NullProgressListener());
                        values = new ArrayList(visitor.getUnique());
                    } else {
                        // Raster
                        GridCoverage2D coverage = MapUtils.getGridCoverage(layer);
                        if (coverage == null) {
                            return;
                        }

                        double noData = RasterHelper.getNoDataValue(coverage);
                        PlanarImage image = (PlanarImage) coverage.getRenderedImage();
                        RectIter iter = RectIterFactory.create(image, image.getBounds());

                        int count = 0;
                        iter.startLines();
                        while (!iter.finishedLines()) {
                            iter.startPixels();
                            while (!iter.finishedPixels()) {
                                double sampleValue = iter.getSampleDouble(0);
                                if (!SSUtils.compareDouble(noData, sampleValue)) {
                                    if (!values.contains(sampleValue)) {
                                        values.add(sampleValue);
                                    }
                                    count++;
                                }
                                iter.nextPixel();
                            }

                            if (useSample && count > max_sample) {
                                break;
                            }
                            iter.nextLine();
                        }
                    }

                    Collections.sort(values);
                    for (Object value : values) {
                        TableItem item = new TableItem(valueTable, SWT.NULL);
                        if (isNumeric) {
                            item.setText(value.toString());
                        } else {
                            item.setText("'" + value.toString() + "'");
                        }
                    }
                } catch (IOException e) {
                    ToolboxPlugin.log(e);
                }
            }
        };

        try {
            BusyIndicator.showWhile(Display.getCurrent(), runnable);
        } catch (Exception e) {
            ToolboxPlugin.log(e);
        }
    }

    private void updateExpression(String insert) {
        String val = txtFilter.getText();
        if (val.length() == 0) {
            txtFilter.setText(insert);
            txtFilter.setSelection(txtFilter.getText().length());
        } else {
            if (txtFilter.getSelectionCount() == 0) {
                final int pos = txtFilter.getCaretPosition();
                String sql = val.substring(0, pos) + " " + insert + val.substring(pos);
                txtFilter.setText(sql);
                txtFilter.setSelection(pos + insert.length() + 1);
            } else {
                final Point pos = txtFilter.getSelection();
                String sql = val.substring(0, pos.x) + insert + val.substring(pos.y);
                txtFilter.setText(sql);
                txtFilter.setSelection(pos.x + insert.length());
            }
        }
        txtFilter.setFocus();
    }

    private boolean isNumeric(SimpleFeatureType schema, String propertyName) {
        if (propertyName == null || propertyName.isEmpty()) {
            return false;
        }
        AttributeDescriptor attDesc = schema.getDescriptor(propertyName);
        return Number.class.isAssignableFrom(attDesc.getType().getBinding());
    }
}

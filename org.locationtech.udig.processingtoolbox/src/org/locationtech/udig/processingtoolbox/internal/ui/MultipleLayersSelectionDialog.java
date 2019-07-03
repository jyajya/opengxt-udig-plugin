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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.util.logging.Logging;
import org.locationtech.udig.processingtoolbox.ToolboxPlugin;
import org.locationtech.udig.processingtoolbox.internal.Messages;
import org.locationtech.udig.processingtoolbox.styler.MapUtils;
import org.locationtech.udig.processingtoolbox.styler.MapUtils.VectorLayerType;
import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.IMap;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Multiple Fields Selection Dialog
 * 
 * @author Minpa Lee, MangoSystem
 * 
 * @source $URL$
 */
public class MultipleLayersSelectionDialog extends Dialog {
    protected static final Logger LOGGER = Logging.getLogger(MultipleLayersSelectionDialog.class);

    static final String[] columns = new String[] { Messages.Feature_Name, Messages.Feature_Type,
            Messages.Feature_CRS };

    private IMap map = null;

    private Table schemaTable;

    private boolean isFeatures = true;

    private Button btnVector, btnPoint, btnLine, btnPolygon, btnRaster;

    private List<SimpleFeatureCollection> vectors = new ArrayList<SimpleFeatureCollection>();

    private List<GridCoverage2D> rasters = new ArrayList<GridCoverage2D>();

    public MultipleLayersSelectionDialog(Shell parentShell, IMap map, boolean isFeatures) {
        super(parentShell);

        this.map = map;
        this.isFeatures = isFeatures;
        setShellStyle(SWT.CLOSE | SWT.MIN | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL
                | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);

        newShell.setText(Messages.MultipleLayersSelectionDialog_title);
    }

    public List<SimpleFeatureCollection> getSelectedFeatures() {
        return vectors.size() == 0 ? null : vectors;
    }

    public List<GridCoverage2D> getSelectedRasters() {
        return rasters.size() == 0 ? null : rasters;
    }

    @Override
    protected Point getInitialSize() {
        return ToolboxPlugin.rescaleSize(getShell(), 500, 450);
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

        // 1. FeatureType's Schema
        Group grpLayer = widget.createGroup(container, null, false, 5, 0, 5);

        btnVector = widget.createRadioButton(grpLayer, Messages.Feature_Vector, null, 1);
        btnPoint = widget.createRadioButton(grpLayer, Messages.Feature_Point, null, 1);
        btnLine = widget.createRadioButton(grpLayer, Messages.Feature_LineString, null, 1);
        btnPolygon = widget.createRadioButton(grpLayer, Messages.Feature_Polygon, null, 1);
        btnRaster = widget.createRadioButton(grpLayer, Messages.Raster, null, 1);

        btnVector.addSelectionListener(selectionListener);
        btnPoint.addSelectionListener(selectionListener);
        btnLine.addSelectionListener(selectionListener);
        btnPolygon.addSelectionListener(selectionListener);
        btnRaster.addSelectionListener(selectionListener);

        btnVector.setEnabled(isFeatures);
        btnPoint.setEnabled(isFeatures);
        btnLine.setEnabled(isFeatures);
        btnPolygon.setEnabled(isFeatures);
        btnRaster.setEnabled(!isFeatures);

        schemaTable = widget.createTable(grpLayer, columns, 5);
        schemaTable.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                if (isFeatures) {
                    vectors.clear();
                    for (TableItem item : schemaTable.getItems()) {
                        if (item.getChecked()) {
                            vectors.add(MapUtils.getFeatures((ILayer) item.getData()));
                        }
                    }
                } else {
                    rasters.clear();
                    for (TableItem item : schemaTable.getItems()) {
                        if (item.getChecked()) {
                            rasters.add(MapUtils.getGridCoverage((ILayer) item.getData()));
                        }
                    }
                }
            }
        });

        TableSelectionWidget tblSelection = new TableSelectionWidget(schemaTable);
        tblSelection.create(grpLayer, SWT.NONE, 5, 1);

        area.pack();

        if (isFeatures) {
            btnVector.setSelection(true);
            loadVectorLayers(VectorLayerType.ALL);
        } else {
            btnRaster.setSelection(true);
            loadRasterLayers();
        }

        return area;
    }

    SelectionListener selectionListener = new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
            Widget widget = event.widget;

            if (widget.equals(btnPoint) && btnPoint.getSelection()) {
                loadVectorLayers(VectorLayerType.POINT);
            } else if (widget.equals(btnLine) && btnLine.getSelection()) {
                loadVectorLayers(VectorLayerType.LINESTRING);
            } else if (widget.equals(btnPolygon) && btnPolygon.getSelection()) {
                loadVectorLayers(VectorLayerType.POLYGON);
            } else if (widget.equals(btnVector) && btnVector.getSelection()) {
                loadVectorLayers(VectorLayerType.ALL);
            } else if (widget.equals(btnRaster) && btnRaster.getSelection()) {
                loadRasterLayers();
            }
        }
    };

    private void loadRasterLayers() {
        schemaTable.removeAll();
        for (ILayer layer : map.getMapLayers()) {
            if (layer.getName() != null
                    && (layer.hasResource(GridCoverage2D.class) || layer.getGeoResource()
                            .canResolve(GridCoverageReader.class))) {
                String crs = layer.getCRS().toWKT();
                TableItem item = new TableItem(schemaTable, SWT.NONE);
                item.setText(new String[] { layer.getName(), Messages.Raster, crs });
                item.setData(layer);
            }
        }
    }

    private void loadVectorLayers(VectorLayerType layerType) {
        schemaTable.removeAll();
        for (ILayer layer : map.getMapLayers()) {
            if (layer.getName() != null && layer.hasResource(FeatureSource.class)) {
                GeometryDescriptor descriptor = layer.getSchema().getGeometryDescriptor();
                Class<?> geometryBinding = descriptor.getType().getBinding();
                String crs = descriptor.getCoordinateReferenceSystem().toWKT();

                switch (layerType) {
                case ALL:
                    insertTableItem(layer, geometryBinding.getSimpleName(), crs);
                    break;
                case LINESTRING:
                    if (geometryBinding.isAssignableFrom(LineString.class)
                            || geometryBinding.isAssignableFrom(MultiLineString.class)) {
                        insertTableItem(layer, geometryBinding.getSimpleName(), crs);
                    }
                    break;
                case POINT:
                    if (geometryBinding.isAssignableFrom(com.vividsolutions.jts.geom.Point.class)
                            || geometryBinding.isAssignableFrom(MultiPoint.class)) {
                        insertTableItem(layer, geometryBinding.getSimpleName(), crs);
                    }
                    break;
                case POLYGON:
                    if (geometryBinding.isAssignableFrom(Polygon.class)
                            || geometryBinding.isAssignableFrom(MultiPolygon.class)) {
                        insertTableItem(layer, geometryBinding.getSimpleName(), crs);
                    }
                    break;
                case MULTIPART:
                    if (geometryBinding.isAssignableFrom(MultiPolygon.class)
                            || geometryBinding.isAssignableFrom(MultiLineString.class)
                            || geometryBinding.isAssignableFrom(MultiPoint.class)) {
                        insertTableItem(layer, geometryBinding.getSimpleName(), crs);
                    }
                    break;
                case POLYLINE:
                    if (geometryBinding.isAssignableFrom(Point.class)
                            || geometryBinding.isAssignableFrom(MultiPoint.class)
                            || geometryBinding.isAssignableFrom(Polygon.class)
                            || geometryBinding.isAssignableFrom(MultiPolygon.class)) {
                        insertTableItem(layer, geometryBinding.getSimpleName(), crs);
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    private void insertTableItem(ILayer layer, String layerType, String crs) {
        TableItem item = new TableItem(schemaTable, SWT.NONE);
        item.setText(new String[] { layer.getName(), layerType, crs });
        item.setData(layer);
    }
}

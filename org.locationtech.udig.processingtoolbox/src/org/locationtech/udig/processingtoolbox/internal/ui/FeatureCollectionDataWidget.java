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

import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.geotools.data.FeatureSource;
import org.geotools.data.Parameter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.process.spatialstatistics.core.Params;
import org.geotools.process.spatialstatistics.transformation.ForceCRSFeatureCollection;
import org.geotools.util.logging.Logging;
import org.locationtech.udig.processingtoolbox.styler.MapUtils;
import org.locationtech.udig.processingtoolbox.styler.MapUtils.FieldType;
import org.locationtech.udig.processingtoolbox.styler.MapUtils.VectorLayerType;
import org.locationtech.udig.project.ILayer;
import org.locationtech.udig.project.IMap;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * FeatureCollection Layer Data control
 * 
 * @author Minpa Lee, MangoSystem
 * 
 * @source $URL$
 */
public class FeatureCollectionDataWidget extends AbstractToolboxWidget {
    protected static final Logger LOGGER = Logging.getLogger(FeatureCollectionDataWidget.class);

    private IMap map;

    public FeatureCollectionDataWidget(IMap map) {
        this.map = map;
    }

    public void create(final Composite parent, final int style,
            final Map<String, Object> processParams, final Parameter<?> param,
            final Map<Widget, Map<String, Object>> uiParams) {
        composite = new Composite(parent, style);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

        GridLayout layout = new GridLayout(1, false);
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        composite.setLayout(layout);

        final Combo cboSfLayer = new Combo(composite, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
        cboSfLayer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Map<String, Object> metadata = param.metadata;
        if (metadata == null || metadata.size() == 0) {
            fillLayers(map, cboSfLayer, VectorLayerType.ALL);
        } else {
            if (metadata.containsKey(Params.FEATURES)) {
                String val = metadata.get(Params.FEATURES).toString();
                if (val.equalsIgnoreCase(VectorLayerType.ALL.toString())) {
                    fillLayers(map, cboSfLayer, VectorLayerType.ALL);
                } else if (val.equalsIgnoreCase(VectorLayerType.POINT.toString())) {
                    fillLayers(map, cboSfLayer, VectorLayerType.POINT);
                } else if (val.equalsIgnoreCase(VectorLayerType.LINESTRING.toString())) {
                    fillLayers(map, cboSfLayer, VectorLayerType.LINESTRING);
                } else if (val.equalsIgnoreCase(VectorLayerType.POLYGON.toString())) {
                    fillLayers(map, cboSfLayer, VectorLayerType.POLYGON);
                } else if (val.equalsIgnoreCase(VectorLayerType.POLYLINE.toString())) {
                    fillLayers(map, cboSfLayer, VectorLayerType.POLYLINE);
                } else if (val.equalsIgnoreCase(VectorLayerType.MULTIPART.toString())) {
                    fillLayers(map, cboSfLayer, VectorLayerType.MULTIPART);
                }
            } else {
                fillLayers(map, cboSfLayer, VectorLayerType.ALL);
            }
        }
        cboSfLayer.setData(param.key);

        cboSfLayer.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                SimpleFeatureCollection sfc = MapUtils.getFeatures(map, cboSfLayer.getText());
                if (sfc.getSchema().getCoordinateReferenceSystem() == null) {
                    sfc = new ForceCRSFeatureCollection(sfc, map.getViewportModel().getCRS());
                }
                processParams.put(param.key, sfc);

                // related field selection = Category, "TypeName.FieldType"
                SimpleFeatureType schema = sfc.getSchema();
                for (Entry<Widget, Map<String, Object>> entrySet : uiParams.entrySet()) {
                    for (Entry<String, Object> kvpSet : entrySet.getValue().entrySet()) {
                        String category = kvpSet.getKey();
                        String value = kvpSet.getValue().toString();
                        String typeName = value.split("\\.")[0]; //$NON-NLS-1$
                        String fieldType = value.split("\\.")[1]; //$NON-NLS-1$

                        if (typeName.equals(param.key)) {
                            if (category.endsWith(Params.FIELD)) {
                                // FieldType = ALL, String, Number, Integer, Double
                                Combo cboField = (Combo) entrySet.getKey();
                                fillFields(cboField, schema, fieldType);
                            } else if (category.endsWith(Params.FIELDS)) {
                                // FieldType = ALL, String, Number, Integer, Double
                                Combo cboField = (Combo) entrySet.getKey();
                                fillFields(cboField, schema, fieldType);
                            }
                        }
                    }
                }
            }
        });

        composite.pack();
    }

    private void fillLayers(IMap map, Combo combo, VectorLayerType layerType) {
        combo.removeAll();
        for (ILayer layer : map.getMapLayers()) {
            if (layer.getName() != null && layer.hasResource(FeatureSource.class)) {
                GeometryDescriptor descriptor = layer.getSchema().getGeometryDescriptor();
                Class<?> geometryBinding = descriptor.getType().getBinding();
                switch (layerType) {
                case ALL:
                    combo.add(layer.getName());
                    break;
                case POINT:
                    if (geometryBinding.isAssignableFrom(Point.class)
                            || geometryBinding.isAssignableFrom(MultiPoint.class)) {
                        combo.add(layer.getName());
                    }
                    break;
                case LINESTRING:
                    if (geometryBinding.isAssignableFrom(LineString.class)
                            || geometryBinding.isAssignableFrom(MultiLineString.class)) {
                        combo.add(layer.getName());
                    }
                    break;
                case POLYGON:
                    if (geometryBinding.isAssignableFrom(Polygon.class)
                            || geometryBinding.isAssignableFrom(MultiPolygon.class)) {
                        combo.add(layer.getName());
                    }
                    break;
                case POLYLINE:
                    if (geometryBinding.isAssignableFrom(Polygon.class)
                            || geometryBinding.isAssignableFrom(MultiPolygon.class)
                            || geometryBinding.isAssignableFrom(LineString.class)
                            || geometryBinding.isAssignableFrom(MultiLineString.class)) {
                        combo.add(layer.getName());
                    }
                    break;
                case MULTIPART:
                    if (geometryBinding.isAssignableFrom(MultiPoint.class)
                            || geometryBinding.isAssignableFrom(MultiLineString.class)
                            || geometryBinding.isAssignableFrom(MultiPolygon.class)) {
                        combo.add(layer.getName());
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    private void fillFields(Combo combo, SimpleFeatureType schema, String fieldType) {
        if (fieldType.equalsIgnoreCase(FieldType.ALL.toString())) {
            fillFields(combo, schema, FieldType.ALL);
        } else if (fieldType.equalsIgnoreCase(FieldType.String.toString())) {
            fillFields(combo, schema, FieldType.String);
        } else if (fieldType.equalsIgnoreCase(FieldType.Number.toString())) {
            fillFields(combo, schema, FieldType.Number);
        } else if (fieldType.equalsIgnoreCase(FieldType.Integer.toString())) {
            fillFields(combo, schema, FieldType.Integer);
        } else if (fieldType.equalsIgnoreCase(FieldType.Double.toString())) {
            fillFields(combo, schema, FieldType.Double);
        }
    }

    private void fillFields(Combo combo, SimpleFeatureType schema, FieldType fieldType) {
        String selectedValue = combo.getText() == null ? null : combo.getText();

        combo.removeAll();
        for (AttributeDescriptor descriptor : schema.getAttributeDescriptors()) {
            if (descriptor instanceof GeometryDescriptor) {
                continue;
            }

            Class<?> binding = descriptor.getType().getBinding();
            switch (fieldType) {
            case ALL:
                combo.add(descriptor.getLocalName());
                break;
            case Double:
                if (Double.class.isAssignableFrom(binding) || Float.class.isAssignableFrom(binding)) {
                    combo.add(descriptor.getLocalName());
                }
                break;
            case Integer:
                if (Short.class.isAssignableFrom(binding)
                        || Integer.class.isAssignableFrom(binding)
                        || Long.class.isAssignableFrom(binding)) {
                    combo.add(descriptor.getLocalName());
                }
                break;
            case Number:
                if (Number.class.isAssignableFrom(binding)) {
                    combo.add(descriptor.getLocalName());
                }
                break;
            case String:
                if (String.class.isAssignableFrom(binding)) {
                    combo.add(descriptor.getLocalName());
                }
                break;
            }
        }

        if (selectedValue != null) {
            if (combo.indexOf(selectedValue) == -1) {
                combo.add(selectedValue);
            }
            combo.setText(selectedValue);
        }
    }
}

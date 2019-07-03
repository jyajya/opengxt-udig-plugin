/*
 * uDig - User Friendly Desktop Internet GIS client
 * (C) MangoSystem - www.mangosystem.com 
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.processingtoolbox.styler;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.geotools.brewer.color.BrewerPalette;
import org.geotools.brewer.color.ColorBrewer;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.DataUtilities;
import org.geotools.data.Parameter;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.process.spatialstatistics.GlobalGStatisticsProcess.GStatisticsProcessResult;
import org.geotools.process.spatialstatistics.GlobalGearysCProcess.GearysCProcessResult;
import org.geotools.process.spatialstatistics.GlobalLeesLProcess.LeesLProcessResult;
import org.geotools.process.spatialstatistics.GlobalLeesSProcess.LeesSProcessResult;
import org.geotools.process.spatialstatistics.GlobalMoransIProcess.MoransIProcessResult;
import org.geotools.process.spatialstatistics.GlobalRogersonRProcess.RogersonRProcessResult;
import org.geotools.process.spatialstatistics.JoinCountStatisticsProcess.JoinCountProcessResult;
import org.geotools.process.spatialstatistics.core.FeatureTypes;
import org.geotools.process.spatialstatistics.core.FeatureTypes.SimpleShapeType;
import org.geotools.process.spatialstatistics.core.FormatUtils;
import org.geotools.process.spatialstatistics.core.HistogramProcessResult;
import org.geotools.process.spatialstatistics.core.Params;
import org.geotools.process.spatialstatistics.gridcoverage.RasterDescribeOperation.RasterDescribeResult;
import org.geotools.process.spatialstatistics.gridcoverage.RasterHelper;
import org.geotools.process.spatialstatistics.operations.DataStatisticsOperation.DataStatisticsResult;
import org.geotools.process.spatialstatistics.operations.PearsonOperation.PearsonResult;
import org.geotools.process.spatialstatistics.pattern.NNIOperation.NearestNeighborResult;
import org.geotools.process.spatialstatistics.pattern.QuadratOperation.QuadratResult;
import org.geotools.process.spatialstatistics.relationship.OLSResult;
import org.geotools.process.spatialstatistics.storage.DataStoreFactory;
import org.geotools.process.spatialstatistics.storage.ShapeExportOperation;
import org.geotools.process.spatialstatistics.styler.GraduatedColorStyleBuilder;
import org.geotools.process.spatialstatistics.styler.GraduatedSymbolStyleBuilder;
import org.geotools.process.spatialstatistics.styler.SSStyleBuilder;
import org.geotools.process.spatialstatistics.transformation.ForceCRSFeatureCollection;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.util.logging.Logging;
import org.locationtech.udig.catalog.CatalogPlugin;
import org.locationtech.udig.catalog.ICatalog;
import org.locationtech.udig.catalog.IGeoResource;
import org.locationtech.udig.catalog.IService;
import org.locationtech.udig.catalog.util.GeoToolsAdapters;
import org.locationtech.udig.processingtoolbox.ToolboxPlugin;
import org.locationtech.udig.processingtoolbox.ToolboxView;
import org.locationtech.udig.processingtoolbox.internal.Messages;
import org.locationtech.udig.processingtoolbox.tools.HtmlWriter;
import org.locationtech.udig.project.IMap;
import org.locationtech.udig.project.internal.Layer;
import org.locationtech.udig.project.ui.ApplicationGIS;
import org.locationtech.udig.style.sld.SLDContent;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;

/**
 * ProcessExecutorOperation
 * 
 * @author Minpa Lee, MangoSystem
 * 
 * @source $URL$
 */
public class ProcessExecutorOperation implements IRunnableWithProgress {
    protected static final Logger LOGGER = Logging.getLogger(ProcessExecutorOperation.class);

    final String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$

    private IMap map;

    private org.geotools.process.ProcessFactory factory;

    private org.opengis.feature.type.Name processName;

    private Map<String, Object> inputParams = new HashMap<String, Object>();

    private Map<String, Object> outputParams = new HashMap<String, Object>();

    private String windowTitle;

    private StringBuffer outputBuffer = new StringBuffer();

    public ProcessExecutorOperation(IMap map, org.geotools.process.ProcessFactory factory,
            org.opengis.feature.type.Name processName, Map<String, Object> inputParams,
            Map<String, Object> outputParams) {
        this.map = map;
        this.factory = factory;
        this.processName = processName;
        this.inputParams = inputParams;
        this.outputParams = outputParams;
        this.windowTitle = factory.getTitle(processName).toString();
    }

    public String getOutputText() {
        return outputBuffer.toString();
    }

    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
        int increment = 10;
        monitor.beginTask(Messages.Task_Running, 100);
        monitor.worked(increment);

        try {
            monitor.setTaskName(String.format(Messages.Task_Executing, windowTitle));
            ToolboxPlugin.log(String.format(Messages.Task_Executing, windowTitle));

            ProgressListener subMonitor = GeoToolsAdapters.progress(SubMonitor.convert(monitor,
                    Messages.Task_Internal, 60));

            org.geotools.process.Process process = factory.create(processName);
            final Map<String, Object> result = process.execute(inputParams, subMonitor);
            monitor.worked(increment);

            monitor.setTaskName(Messages.Task_AddingLayer);
            outputBuffer.setLength(0);
            if (result != null) {
                Map<String, Parameter<?>> resultInfo = factory.getResultInfo(processName, null);
                for (Entry<String, Object> entrySet : result.entrySet()) {
                    final Object val = entrySet.getValue();
                    if (val == null) {
                        continue;
                    }

                    if (val instanceof SimpleFeatureCollection) {
                        postProcessing((SimpleFeatureCollection) val,
                                outputParams.get(entrySet.getKey()),
                                resultInfo.get(entrySet.getKey()).metadata, monitor);
                    } else if (val instanceof Geometry) {
                        postProcessing(geometryToFeatures((Geometry) val, processName.toString()),
                                outputParams.get(entrySet.getKey()),
                                resultInfo.get(entrySet.getKey()).metadata, monitor);
                    } else if (val instanceof BoundingBox) {
                        Geometry boundingBox = JTS.toGeometry((BoundingBox) val);
                        boundingBox.setUserData(((BoundingBox) val).getCoordinateReferenceSystem());
                        postProcessing(geometryToFeatures(boundingBox, processName.toString()),
                                outputParams.get(entrySet.getKey()),
                                resultInfo.get(entrySet.getKey()).metadata, monitor);
                    } else if (val instanceof GridCoverage2D) {
                        postProcessing((GridCoverage2D) val, outputParams.get(entrySet.getKey()),
                                resultInfo.get(entrySet.getKey()).metadata, monitor);
                    } else {
                        postProcessing(val);
                    }
                    monitor.worked(increment);
                }
            }
            monitor.worked(increment);
        } catch (Exception e) {
            // always show log
            boolean showLog = ToolboxView.getShowLog();
            ToolboxView.setShowLog(true);
            ToolboxPlugin.log(e.getMessage());
            ToolboxView.setShowLog(showLog);
        } finally {
            ToolboxPlugin.log(String.format(Messages.Task_Completed, windowTitle));
            monitor.done();
        }
    }

    private void postProcessing(GridCoverage2D source, Object outputPath,
            Map<String, Object> outputMeta, IProgressMonitor monitor) {
        try {
            monitor.setTaskName(Messages.Task_WritingResult);
            File outputFile = new File(outputPath.toString());
            GridCoverage2D output = MapUtils.saveAsGeoTiff(source, outputFile);

            if (ToolboxView.getAddLayerAutomatically()) {
                monitor.setTaskName(Messages.Task_AddingLayer);

                // create default style
                Style style = null;

                Object minValue = source.getProperty("Minimum"); //$NON-NLS-1$
                Object maxValue = source.getProperty("Maximum"); //$NON-NLS-1$
                int numBands = source.getNumSampleDimensions();

                if (minValue != null && minValue instanceof Number && maxValue != null
                        && maxValue instanceof Number && numBands == 1) {
                    Double noData = RasterHelper.getNoDataValue(source);
                    style = buildCoverageStyle((Double) minValue, (Double) maxValue, noData);
                } else {
                    SSStyleBuilder builder = new SSStyleBuilder(null);
                    style = builder.getDefaultGridCoverageStyle(source);
                }

                MapUtils.addGridCoverageToMap(map, output, outputFile, style);
            }
        } catch (IllegalArgumentException e) {
            ToolboxPlugin.log(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            ToolboxPlugin.log(e.getMessage());
        } catch (IOException e) {
            ToolboxPlugin.log(e.getMessage());
        }
    }

    private Style buildCoverageStyle(double minValue, double maxValue, Double noData) {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

        @SuppressWarnings("nls")
        String[] palettes = { "RdYlGn", "YlOrRd", "YlOrBr", "Oranges", "YlGnBu", "Spectral" };

        int numClasses = 8;

        double[] breaks = new double[numClasses + 1];
        double interval = (maxValue - minValue) / numClasses;

        ColorBrewer brewer = ColorBrewer.instance();
        Random random = new Random();
        BrewerPalette brewerPalette = brewer.getPalette(palettes[random.nextInt(palettes.length)]);

        Color[] colors = brewerPalette.getColors(breaks.length);
        // Collections.reverse(Arrays.asList(colors)); // reverse

        StyleBuilder builder = new StyleBuilder();

        ColorMapEntry nodataEntry = sf.createColorMapEntry();
        nodataEntry.setQuantity(ff.literal(noData));
        nodataEntry.setColor(builder.colorExpression(new java.awt.Color(255, 255, 255, 0)));
        nodataEntry.setOpacity(ff.literal(0.0f));
        nodataEntry.setLabel("No Data"); //$NON-NLS-1$

        ColorMap colorMap = sf.createColorMap();
        colorMap.setType(ColorMap.TYPE_RAMP);

        if (noData < breaks[0]) {
            colorMap.addColorMapEntry(nodataEntry);
        }

        for (int i = 0; i < breaks.length; i++) {
            breaks[i] = minValue + (i * interval);

            ColorMapEntry entry = sf.createColorMapEntry();
            entry.setQuantity(builder.literalExpression(breaks[i]));
            entry.setColor(builder.colorExpression(colors[i]));
            entry.setOpacity(builder.literalExpression(colors[i].getAlpha() / 255.0));

            colorMap.addColorMapEntry(entry);
        }

        if (noData > breaks[breaks.length - 1]) {
            colorMap.addColorMapEntry(nodataEntry);
        }

        return builder.createStyle(builder.createRasterSymbolizer(colorMap, 1.0d));
    }

    private void postProcessing(Object value) {
        HtmlWriter writer = new HtmlWriter(windowTitle);
        if (value instanceof DataStatisticsResult) {
            writer.writeDataStatistics((DataStatisticsResult) value);
        } else if (value instanceof GStatisticsProcessResult) {
            writer.writeGStatistics((GStatisticsProcessResult) value);
        } else if (value instanceof MoransIProcessResult) {
            writer.writeMoransI((MoransIProcessResult) value);
        } else if (value instanceof GearysCProcessResult) {
            writer.writeGearysC((GearysCProcessResult) value);
        } else if (value instanceof LeesSProcessResult) {
            writer.writeLeesS((LeesSProcessResult) value);
        } else if (value instanceof LeesLProcessResult) {
            writer.writeLeesL((LeesLProcessResult) value);
        } else if (value instanceof RogersonRProcessResult) {
            writer.writeRogersonR((RogersonRProcessResult) value);
        } else if (value instanceof NearestNeighborResult) {
            writer.writeNearestNeighbor((NearestNeighborResult) value);
        } else if (value instanceof PearsonResult) {
            writer.writePearson((PearsonResult) value);
        } else if (value instanceof OLSResult) {
            writer.writeOLSProcess((OLSResult) value);
        } else if (value instanceof JoinCountProcessResult) {
            writer.writeJoinCount((JoinCountProcessResult) value);
        } else if (value instanceof HistogramProcessResult) {
            writer.writeHistogramProcess((HistogramProcessResult) value);
        } else if (value instanceof QuadratResult) {
            writer.writeQuadratProcess((QuadratResult) value);
        } else if (value instanceof RasterDescribeResult) {
            writer.writeRasterDescribeProcess((RasterDescribeResult) value);
        } else if (Number.class.isAssignableFrom(value.getClass())) {
            writer.writeH2(FormatUtils.format(Double.parseDouble(value.toString())));
        } else {
            writer.writePre(value.toString());
        }
        outputBuffer.append(writer.getHTML());
    }

    private void postProcessing(SimpleFeatureCollection source, Object outputPath,
            Map<String, Object> outputMeta, IProgressMonitor monitor) {
        // check crs
        CoordinateReferenceSystem crs = source.getSchema().getCoordinateReferenceSystem();
        if (crs == null) {
            ToolboxPlugin
                    .log("Warning: Output CRS will be used coordinate reference system of the current map"); //$NON-NLS-1$
            source = new ForceCRSFeatureCollection(source, map.getViewportModel().getCRS());
        }

        // write shapefile
        monitor.setTaskName(Messages.Task_WritingResult);
        File filePath = new File(outputPath.toString());
        String typeName = FilenameUtils.getBaseName(filePath.getPath());
        SimpleFeatureSource featureSource = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            File file = new File(filePath.getParent());
            params.put(ShapefileDataStoreFactory.URLP.key, DataUtilities.fileToURL(file));
            params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key, false);
            params.put(ShapefileDataStoreFactory.DBFCHARSET.key, ToolboxPlugin.defaultCharset());

            ShapeExportOperation exportOp = ShapeExportOperation.getDefault();
            exportOp.setOutputDataStore(DataStoreFactory.getDataStore(params));
            exportOp.setOutputTypeName(typeName);
            featureSource = exportOp.execute(source);
            source = featureSource.getFeatures();
        } catch (IOException e) {
            ToolboxPlugin.log(e.getMessage());
        }

        if (featureSource == null) {
            return;
        }

        if (!ToolboxView.getAddLayerAutomatically()) {
            return;
        }

        monitor.setTaskName(Messages.Task_AddingLayer);
        ToolboxPlugin.log(Messages.Task_AddingLayer);

        SimpleFeatureType schema = featureSource.getSchema();
        SSStyleBuilder ssBuilder = new SSStyleBuilder(schema);
        ssBuilder.setOpacity(0.8f);

        Style style = ssBuilder.getDefaultFeatureStyle();
        if (ToolboxView.getUseDefaultStyle()) {
            if (outputMeta.containsKey(Params.STYLES)) {
                // KVP(Params.STYLES, "renderer.fieldname")
                // renderer = LISA, OLS, UniqueValues, ClassBreaks, Density, Distance, Interpolation
                // ClassBreaks = EqualInterval, Quantile, NaturalBreaks, StdDev

                try {
                    String value = outputMeta.get(Params.STYLES).toString();
                    String[] splits = value.split("\\."); //$NON-NLS-1$ 
                    String styleName = splits[0].toUpperCase();

                    String functionName = null;
                    if (styleName.startsWith("LISA")) { //$NON-NLS-1$
                        style = ssBuilder.getLISAStyle("COType"); //$NON-NLS-1$
                    } else if (styleName.startsWith("OLS")) { //$NON-NLS-1$
                        style = ssBuilder.getOLSStyle(splits[1]);
                    } else if (styleName.startsWith("CL") || styleName.startsWith("JE") //$NON-NLS-1$ //$NON-NLS-2$
                            || styleName.startsWith("NA")) { //$NON-NLS-1$
                        functionName = "JenksNaturalBreaksFunction"; //$NON-NLS-1$
                    } else if (styleName.startsWith("E")) { //$NON-NLS-1$
                        functionName = "EqualIntervalFunction"; //$NON-NLS-1$
                    } else if (styleName.startsWith("S")) { //$NON-NLS-1$
                        functionName = "StandardDeviationFunction"; //$NON-NLS-1$
                    } else if (styleName.startsWith("Q")) { //$NON-NLS-1$
                        functionName = "QuantileFunction"; //$NON-NLS-1$
                    }

                    if (functionName != null && splits.length == 2) {
                        String fieldName = splits[1]; // inputParams
                        if (schema.indexOf(fieldName) == -1) {
                            fieldName = inputParams.get(fieldName).toString();
                        }

                        if (schema.indexOf(fieldName) != -1) {
                            Class<?> binding = schema.getDescriptor(fieldName).getType()
                                    .getBinding();
                            if (Number.class.isAssignableFrom(binding)) {
                                SimpleShapeType shapeType = FeatureTypes.getSimpleShapeType(source);
                                if (shapeType == SimpleShapeType.POINT) {
                                    GraduatedSymbolStyleBuilder builder = new GraduatedSymbolStyleBuilder();
                                    builder.setMethodName(functionName);
                                    style = builder.createStyle(source, fieldName);
                                } else {
                                    GraduatedColorStyleBuilder builder = new GraduatedColorStyleBuilder();
                                    style = builder.createStyle(source, fieldName, functionName, 5,
                                            "Blues"); //$NON-NLS-1$
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    ToolboxPlugin.log(e.getMessage());
                }
            } else {
                if (source.getSchema().indexOf("COType") != -1) { //$NON-NLS-1$
                    style = ssBuilder.getLISAStyle("COType"); //$NON-NLS-1$
                } else if (source.getSchema().indexOf("GiZScore") != -1) { //$NON-NLS-1$
                    style = ssBuilder.getZScoreStdDevStyle("GiZScore"); //$NON-NLS-1$
                }
            }
        }

        try {
            CatalogPlugin catalogPlugin = CatalogPlugin.getDefault();
            ICatalog localCatalog = catalogPlugin.getLocalCatalog();

            URL resourceId = DataUtilities.fileToURL(filePath);
            List<IService> services = catalogPlugin.getServiceFactory().createService(resourceId);
            for (IService service : services) {
                localCatalog.add(service);
                for (IGeoResource resource : service.resources(new NullProgressMonitor())) {
                    List<IGeoResource> resourceList = Collections.singletonList(resource);
                    final int pos = map.getMapLayers().size();
                    Layer layer = (Layer) ApplicationGIS.addLayersToMap(map, resourceList, pos)
                            .get(0);
                    layer.setName(typeName);
                    layer.setVisible(true);

                    if (style != null) {
                        // put the style on the blackboard
                        layer.getStyleBlackboard().put(SLDContent.ID, style);
                        layer.getStyleBlackboard().setSelected(new String[] { SLDContent.ID });
                    }

                    // refresh
                    layer.refresh(layer.getBounds(new NullProgressMonitor(), null));
                }
            }
        } catch (MalformedURLException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
    }

    private SimpleFeatureCollection geometryToFeatures(Geometry source, String layerName) {
        CoordinateReferenceSystem crs = map.getViewportModel().getCRS();
        if (source.getUserData() != null
                && CoordinateReferenceSystem.class
                        .isAssignableFrom(source.getUserData().getClass())) {
            crs = (CoordinateReferenceSystem) source.getUserData();
        }

        Geometry geometry = source;
        if (source instanceof LinearRing) {
            geometry = source.getFactory().createPolygon((LinearRing) source, null);
            geometry.setUserData(source.getUserData());
        }

        SimpleFeatureType schema = FeatureTypes.getDefaultType(layerName, geometry.getClass(), crs);

        ListFeatureCollection features = new ListFeatureCollection(schema);
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);

        SimpleFeature feature = builder.buildFeature(null);
        feature.setDefaultGeometry(geometry);
        features.add(feature);

        return features;
    }
}

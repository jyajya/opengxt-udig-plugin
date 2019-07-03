/*
 * uDig - User Friendly Desktop Internet GIS client
 * (C) MangoSystem - www.mangosystem.com 
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package org.locationtech.udig.processingtoolbox.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Localization
 * 
 * @author Minpa Lee, MangoSystem  
 * 
 * @source $URL$
 */
public class Messages extends NLS {
    private static final String BUNDLE_NAME = "org.locationtech.udig.processingtoolbox.internal.messages"; //$NON-NLS-1$

    public static String AmoebaWizardDialog_title;
    public static String AmoebaWizardDialog_description;

    public static String AmoebaWizardFirstPage_title;
    public static String AmoebaWizardFirstPage_description;
    public static String AmoebaWizardFirstPage_Algorithm;
    public static String AmoebaWizardFirstPage_Layer;
    public static String AmoebaWizardFirstPage_Field;
    public static String AmoebaWizardFirstPage_Conceptualization;
    public static String AmoebaWizardFirstPage_DistanceMethod;
    public static String AmoebaWizardFirstPage_Standardization;
    public static String AmoebaWizardFirstPage_DistanceBand;
    public static String AmoebaWizardFirstPage_CalcuateStatistics;
    public static String AmoebaWizardFirstPage_LocalStatistics;
    public static String AmoebaWizardFirstPage_ClusteringCriteria;
    public static String AmoebaWizardFirstPage_ClusteringField;
    public static String AmoebaWizardFirstPage_GroupTitle;
    public static String AmoebaWizardFirstPage_Maximization;
    public static String AmoebaWizardFirstPage_Minimization;
    public static String AmoebaWizardFirstPage_CustomCriteria;

    public static String AmoebaWizardSecondPage_title;
    public static String AmoebaWizardSecondPage_description;
    public static String AmoebaWizardSecondPage_GeneralOptions;
    public static String AmoebaWizardSecondPage_SeedOptions;
    public static String AmoebaWizardSecondPage_OverlapClusterOptions;
    public static String AmoebaWizardSecondPage_ExclusionOptions;
    public static String AmoebaWizardSecondPage_UseOnlyMax;
    public static String AmoebaWizardSecondPage_SortDescending;
    public static String AmoebaWizardSecondPage_AllCells;
    public static String AmoebaWizardSecondPage_SelectedCells;
    public static String AmoebaWizardSecondPage_CustomCells;
    public static String AmoebaWizardSecondPage_OverlapRemove;
    public static String AmoebaWizardSecondPage_OverlapAvoid;
    public static String AmoebaWizardSecondPage_ExclusionFilter;
    public static String AmoebaWizardSecondPage_ExclusionClusters;
    public static String AmoebaWizardSecondPage_EliminateExclusion;
    public static String AmoebaWizardSecondPage_EliminateSingleCluster;
    
    public static String Feature_Name;
    public static String Feature_Type;
    public static String Feature_CRS;
    public static String Feature_Vector;
    public static String Feature_Point;
    public static String Feature_LineString;
    public static String Feature_Polygon;
    public static String Feature_FeatureType;
    public static String Raster;
    
    public static String BatchClipFeaturesDialog_title;
    public static String BatchClipFeaturesDialog_description;
    public static String BatchClipFeaturesDialog_ClipLayer;
    public static String BatchClipFeaturesDialog_Warning;
    
    public static String BatchClipRastersDialog_title;
    public static String BatchClipRastersDialog_description;
    
    public static String BatchReprojectFeaturesDialog_title;
    public static String BatchReprojectFeaturesDialog_description;
    public static String BatchReprojectFeaturesDialog_SelectLayers;
    public static String BatchReprojectFeaturesDialog_Name;
    public static String BatchReprojectFeaturesDialog_Type;
    public static String BatchReprojectFeaturesDialog_CRS;
    public static String BatchReprojectFeaturesDialog_OutputCRS;
    public static String BatchReprojectFeaturesDialog_Warning;

    public static String BatchReprojectRastersDialog_title;
    public static String BatchReprojectRastersDialog_description;
    
    public static String BoundingBoxViewer_CurrentMapExtent;
    public static String BoundingBoxViewer_CurrentMapFullExtent;
    public static String BoundingBoxViewer_LayerExtent;
    
    public static String BoxPlotDialog_title;
    public static String BoxPlotDialog_description;
    public static String BoxPlotDialog_Fields;
    
    public static String BubbleChartDialog_title;
    public static String BubbleChartDialog_description;
    public static String BubbleChartDialog_XField;
    public static String BubbleChartDialog_YField;
    public static String BubbleChartDialog_SizeField;

    public static String CrsViewer_CRSDialog;
    public static String CrsViewer_LayerCRS;
    public static String CrsViewer_MapCRS;
    
    public static String ExcelToPointDialog_title;
    public static String ExcelToPointDialog_description;
    public static String ExcelToPointDialog_Excelfile;
    public static String ExcelToPointDialog_Sheet;
    public static String ExcelToPointDialog_SourceCRS;
    public static String ExcelToPointDialog_TargetCRS;
    
    public static String ExportStyleDialog_title;
    public static String ExportStyleDialog_description;
    public static String ExportStyleDialog_overwrite;
    
    public static String ExpressionBuilderDialog_title;
    public static String ExpressionBuilderDialog_Clear;
    public static String ExpressionBuilderDialog_Test;
    public static String ExpressionBuilderDialog_Layer_Functions;
    public static String ExpressionBuilderDialog_Layer;
    public static String ExpressionBuilderDialog_Fields;
    public static String ExpressionBuilderDialog_Functions;
    public static String ExpressionBuilderDialog_Operators;
    public static String ExpressionBuilderDialog_Expression;

    public static String ExtentSelection_currentextent;
    public static String ExtentSelection_fullextent;
    public static String ExtentSelection_layer;
    public static String ExtentSelection_layerextent;
    public static String ExtentSelection_message;
    public static String ExtentSelection_title;
    
    public static String FeatureCollectionsDataWidget_Selected;
    
    public static String FieldCalculatorDialog_title;
    public static String FieldCalculatorDialog_description;
    public static String FieldCalculatorDialog_Layer_Group;
    public static String FieldCalculatorDialog_Layer;
    public static String FieldCalculatorDialog_Selected;
    public static String FieldCalculatorDialog_Field;
    public static String FieldCalculatorDialog_Fields;
    public static String FieldCalculatorDialog_Functions;
    public static String FieldCalculatorDialog_Operators;
    public static String FieldCalculatorDialog_Warning;
    public static String FieldCalculatorDialog_FieldType;
    public static String FieldCalculatorDialog_FieldLen;
    public static String FieldCalculatorDialog_Failed;
    
    public static String FormatConversionDialog_title;
    public static String FormatConversionDialog_description;
    public static String FormatConversionDialog_SelectLayers;
    public static String FormatConversionDialog_Name;
    public static String FormatConversionDialog_Type;
    public static String FormatConversionDialog_CRS;
    public static String FormatConversionDialog_Format;
    public static String FormatConversionDialog_Warning;

    public static String General_Cancelled;
    public static String General_Error;
    public static String General_ErrorOccurred;
    public static String General_Completed;
    public static String General_Name;
    public static String General_OverwriteLayer;
    public static String General_OverwriteError;
    
    public static String GeometryPickerDialog_title;
    public static String GeometryPickerDialog_Layer;
    public static String GeometryPickerDialog_Feature;
    public static String GeometryPickerDialog_WKT;
    public static String GeometryPickerDialog_Clipboard;
    public static String GeometryPickerDialog_AddLayer;
    
    public static String GeometryToFeaturesDialog_title;
    public static String GeometryToFeaturesDialog_description;
    public static String GeometryToFeaturesDialog_WKT;
    public static String GeometryToFeaturesDialog_CRS;
    
    public static String GeometryViewer_Point;
    public static String GeometryViewer_LineString;
    public static String GeometryViewer_Polygon;
    public static String GeometryViewer_PointLayer;
    public static String GeometryViewer_LineStringLayer;
    public static String GeometryViewer_PolygonLayer;
    public static String GeometryViewer_MapCenter;
    public static String GeometryViewer_MapExtent;
    public static String GeometryViewer_MapBoundary;
    public static String GeometryViewer_GeometryFromFeatures;
    
    public static String GridCoveragesDataWidget_Selected;
    
    public static String HistogramDialog_title;
    public static String HistogramDialog_description;
    public static String HistogramDialog_InputLayer;
    public static String HistogramDialog_InputField;
    public static String HistogramDialog_BinCount;
    public static String HistogramDialog_YAxisType;
    public static String HistogramDialog_Frequency;
    public static String HistogramDialog_Ratio;
    public static String HistogramDialog_Value;
    
    public static String IntegerDataViewer_FeatureCount;
    
    public static String LiteralDataViewer_ExpressionBuilder;
    public static String LiteralDataViewer_MultipleFieldsSelection;
    public static String LiteralDataViewer_StatisticsFieldsSelection;
    
    public static String MergeFeaturesDialog_title;
    public static String MergeFeaturesDialog_description;
    public static String MergeFeaturesDialog_InputFeatures;
    public static String MergeFeaturesDialog_TemplateFeatures;
    public static String MergeFeaturesDialog_Warning;
    
    public static String MoranScatterPlotDialog_title;
    public static String MoranScatterPlotDialog_description;
    public static String MoranScatterPlotDialog_Graph;
    public static String MoranScatterPlotDialog_Summary;
    public static String MoranScatterPlotDialog_InputLayer;
    public static String MoranScatterPlotDialog_InputField;
    public static String MoranScatterPlotDialog_Conceptualization;
    public static String MoranScatterPlotDialog_DistanceMethod;
    public static String MoranScatterPlotDialog_Standardization;
    public static String MoranScatterPlotDialog_DistanceBand;
    
    public static String MultipleFieldsSelectionDialog_title;
    public static String MultipleFieldsSelectionDialog_Layer;
    public static String MultipleFieldsSelectionDialog_Fields;
    public static String MultipleFieldsSelectionDialog_SelectedFields;
    public static String MultipleFieldsSelectionDialog_SelectAll;
    public static String MultipleFieldsSelectionDialog_SwitchSelect;
    public static String MultipleFieldsSelectionDialog_Clear;
    
    public static String MultipleLayersSelectionDialog_title;
    
    public static String NumberDataViewer_GetArea;

    public static String OutputDataViewer_folderdialog;
    public static String OutputDataViewer_outputdata;
    public static String OutputDataViewer_outputlocation;
    public static String OutputDataViewer_selectdata;
    
    public static String ProcessInformation_Others;

    public static String ProcessExecutionDialog_deletefailed;
    public static String ProcessExecutionDialog_optional;
    public static String ProcessExecutionDialog_overwriteconfirm;
    public static String ProcessExecutionDialog_requiredparam;
    public static String ProcessExecutionDialog_tabhelp;
    public static String ProcessExecutionDialog_taboutput;
    public static String ProcessExecutionDialog_tabparameters;
    public static String ProcessExecutionDialog_Yes;
    public static String ProcessExecutionDialog_No;
    
    public static String ProcessDescriptor_Input_Parameters;
    public static String ProcessDescriptor_Output_Parameters;
    public static String ProcessDescriptor_General_Information;
    public static String ProcessDescriptor_Product;
    public static String ProcessDescriptor_Home;
    public static String ProcessDescriptor_Parameter;
    public static String ProcessDescriptor_Explanation;
    public static String ProcessDescriptor_Required;
    public static String ProcessDescriptor_Author;
    public static String ProcessDescriptor_Document;
    public static String ProcessDescriptor_Contact;
    public static String ProcessDescriptor_Online_Help;
    public static String ProcessDescriptor_Team_Blog;
    
    public static String QueryDialog_title;
    public static String QueryDialog_Clear;
    public static String QueryDialog_Test;
    public static String QueryDialog_Layer;
    public static String QueryDialog_Fields;
    public static String QueryDialog_Values;
    public static String QueryDialog_Sample;
    public static String QueryDialog_All;
    public static String QueryDialog_Operators;
    public static String QueryDialog_SQL_where_clause;
    
    public static String RasterCalculatorDialog_title;
    public static String RasterCalculatorDialog_description;
    public static String RasterCalculatorDialog_ExtentandCell;
    public static String RasterCalculatorDialog_Extent;
    public static String RasterCalculatorDialog_Cell;
    public static String RasterCalculatorDialog_Intersection;
    public static String RasterCalculatorDialog_Union;
    public static String RasterCalculatorDialog_Maximum;
    public static String RasterCalculatorDialog_Minimum;
    public static String RasterCalculatorDialog_LayersandFunctions;
    public static String RasterCalculatorDialog_RasterLayers;
    
    public static String ScatterPlotDialog_title;
    public static String ScatterPlotDialog_description;
    public static String ScatterPlotDialog_Graph;
    public static String ScatterPlotDialog_Summary;
    public static String ScatterPlotDialog_InputLayer;
    public static String ScatterPlotDialog_IndependentField;
    public static String ScatterPlotDialog_DependentField;
    public static String ScatterPlotDialog_BasicStatistics;
    public static String ScatterPlotDialog_Pearson;
    
    public static String SettingsDialog_title;
    public static String SettingsDialog_general;
    public static String SettingsDialog_UseLog;
    public static String SettingsDialog_advanced; 
    public static String SettingsDialog_OnlySelectedFeatures;
    public static String SettingsDialog_SetDefaultStyle;
    public static String SettingsDialog_AddLayerAuto;
    public static String SettingsDialog_MandatoryParameter;  
    public static String SettingsDialog_RetainLastLocation;  
    
    public static String SpatialWeightsMatrixDialog_title;
    public static String SpatialWeightsMatrixDialog_description;
    public static String SpatialWeightsMatrixDialog_InputLayer;
    public static String SpatialWeightsMatrixDialog_ValueField;
    public static String SpatialWeightsMatrixDialog_UniqueField;
    public static String SpatialWeightsMatrixDialog_Self;
    public static String SpatialWeightsMatrixDialog_WeightType;
    public static String SpatialWeightsMatrixDialog_WeightTypeDistance;
    public static String SpatialWeightsMatrixDialog_WeightTypeContiguity;
    public static String SpatialWeightsMatrixDialog_DistanceMethod;
    public static String SpatialWeightsMatrixDialog_Euclidean;
    public static String SpatialWeightsMatrixDialog_Manhattan;
    public static String SpatialWeightsMatrixDialog_ThresholdDistance;
    public static String SpatialWeightsMatrixDialog_Calculate;
    public static String SpatialWeightsMatrixDialog_kNearestNeighbors;
    public static String SpatialWeightsMatrixDialog_Numberofneighbors;
    public static String SpatialWeightsMatrixDialog_RowStandardization;
    public static String SpatialWeightsMatrixDialog_Queen;
    public static String SpatialWeightsMatrixDialog_OrderofContiguity;
    public static String SpatialWeightsMatrixDialog_Rook;
    public static String SpatialWeightsMatrixDialog_Bishops;
    
    public static String SplitByAttributesDialog_title;
    public static String SplitByAttributesDialog_description;
    public static String SplitByAttributesDialog_InputFeatures;
    public static String SplitByAttributesDialog_SplitField;
    public static String SplitByAttributesDialog_UsePrefix;
    public static String SplitByAttributesDialog_OutputLayers;
    public static String SplitByAttributesDialog_OutputName;
    public static String SplitByAttributesDialog_FieldValue;
    public static String SplitByAttributesDialog_Count;
    public static String SplitByAttributesDialog_NamePolicy;
    public static String SplitByAttributesDialog_Warning;
    
    public static String SplitByFeaturesDialog_title;
    public static String SplitByFeaturesDialog_description;
    public static String SplitByFeaturesDialog_SplitFeatures;
    
    public static String StatisticsFieldsSelectionDialog_title;
    public static String StatisticsFieldsSelectionDialog_Layer;
    public static String StatisticsFieldsSelectionDialog_Fields;
    public static String StatisticsFieldsSelectionDialog_Add;
    public static String StatisticsFieldsSelectionDialog_Delete;
    public static String StatisticsFieldsSelectionDialog_TargetField;
    public static String StatisticsFieldsSelectionDialog_StatisticsType;
    public static String StatisticsFieldsSelectionDialog_WarningDuplicate;
    
    public static String TableSelectionWidget_SelectAll;
    public static String TableSelectionWidget_ClearSelection;
    public static String TableSelectionWidget_SwitchSelection;

    public static String Task_AddingLayer;
    public static String Task_Canceled;
    public static String Task_Completed;
    public static String Task_ParameterRequired;
    public static String Task_ConfirmErrorFile;
    public static String Task_CheckFile;
    public static String Task_Executing;
    public static String Task_Internal;
    public static String Task_Running;
    public static String Task_WritingResult;
    
    public static String TextfileToPointDialog_title;
    public static String TextfileToPointDialog_description;
    public static String TextfileToPointDialog_Textfile;
    public static String TextfileToPointDialog_CRS;
    public static String TextfileToPointDialog_TargetCRS;
    public static String TextfileToPointDialog_Encoding;
    public static String TextfileToPointDialog_Schema;
    public static String TextfileToPointDialog_FieldNameSetting;
    public static String TextfileToPointDialog_ColumnFirst;
    public static String TextfileToPointDialog_Delimiters;
    public static String TextfileToPointDialog_Tab;
    public static String TextfileToPointDialog_Semicolon;
    public static String TextfileToPointDialog_Colon;
    public static String TextfileToPointDialog_Space;
    public static String TextfileToPointDialog_Custom;
    public static String TextfileToPointDialog_FieldName;
    public static String TextfileToPointDialog_FieldType;
    public static String TextfileToPointDialog_FieldLength;
    public static String TextfileToPointDialog_FieldRow1;
    public static String TextfileToPointDialog_FieldRow2;
    public static String TextfileToPointDialog_FieldRow3;
    public static String TextfileToPointDialog_FieldRow4;
    public static String TextfileToPointDialog_XYRequired;
    
    public static String ThematicMapDialog_title;
    public static String ThematicMapDialog_description;
    public static String ThematicMapDialog_InputLayer;
    public static String ThematicMapDialog_InputField;
    public static String ThematicMapDialog_Normalization;
    public static String ThematicMapDialog_Mode;
    public static String ThematicMapDialog_Classes;
    public static String ThematicMapDialog_ColorRamp;
    public static String ThematicMapDialog_Reverse;
    public static String ThematicMapDialog_Transparency;
    public static String ThematicMapDialog_OutlineColor;
    public static String ThematicMapDialog_OutlineWidth;
    public static String ThematicMapDialog_Symbol;
    public static String ThematicMapDialog_Minimum;
    public static String ThematicMapDialog_Maximum;
    
    public static String ThematicMapRasterDialog_title;
    public static String ThematicMapRasterDialog_description;
    public static String ThematicMapRasterDialog_NoData;
    
    public static String ToolboxView_workspace;
    public static String ToolboxView_GeoTools;    
    public static String ToolboxView_Title;
    public static String ToolboxView_SpatialStatistics;
    public static String ToolboxView_DescriptiveStatistics;
    public static String ToolboxView_PointPattern;
    public static String ToolboxView_Autocorrelation;
    public static String ToolboxView_Relationsips;
    public static String ToolboxView_Cluster;
    public static String ToolboxView_Distribution;
    public static String ToolboxView_GeneralTools;
    public static String ToolboxView_DataCreation;
    public static String ToolboxView_Aggregation;
    public static String ToolboxView_Calculation;
    public static String ToolboxView_Overlays;
    public static String ToolboxView_Extract;
    public static String ToolboxView_Proximity;
    public static String ToolboxView_Graph;
    public static String ToolboxView_RasterTools;
    public static String ToolboxView_Utilities;
    public static String ToolboxView_NoActiveMap;
    public static String ToolboxView_Import;
    public static String ToolboxView_Export;
    public static String ToolboxView_Density;
    public static String ToolboxView_Interpolation;
    public static String ToolboxView_RasterSurface;
    public static String ToolboxView_Conditional;
    public static String ToolboxView_Conversion;
    public static String ToolboxView_Reclass;
    public static String ToolboxView_Editing;
    public static String ToolboxView_RasterZonal;
    public static String ToolboxView_RasterDistance;
    public static String ToolboxView_RasterMath;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}

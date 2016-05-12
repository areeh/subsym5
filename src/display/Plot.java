package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.*;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

import logic.Individual;
import logic.Population;
import logic.Settings;

public class Plot extends ApplicationFrame {
	public XYPlot xyPlot;

    public Plot(String s, XYDataset dataset) {
        super(s);
        JPanel jpanel = createPanel(dataset);
        jpanel.setPreferredSize(new Dimension(640, 480));
        add(jpanel);
    }

    public JPanel createPanel(XYDataset dataset) {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
            "Fitness plot", "Distance Fitness", "Cost Fitness", dataset,
            PlotOrientation.VERTICAL, true, true, false);
        Shape cross = ShapeUtilities.createDiagonalCross(3, 1);
        xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesShape(0, cross);
        renderer.setSeriesPaint(0, Color.blue);
        return new ChartPanel(jfreechart);
    }
    
    public void addBestVals(XYDataset dataset) {
    	this.xyPlot.setDataset(1, dataset);
    	this.xyPlot.setRenderer(1, new XYShapeRenderer());
    	this.xyPlot.getRenderer(1).setPaint(Color.green);
    }
    
    public void addWorstVals(XYDataset dataset) {
    	this.xyPlot.setDataset(2, dataset);
    	this.xyPlot.setRenderer(2, new XYShapeRenderer());
    	this.xyPlot.getRenderer(2).setPaint(Color.red);
    }

    private static XYDataset samplexydataset2() {
        int cols = 20;
        int rows = 20;
        double[][] values = new double[cols][rows];
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("Random");
        Random rand = new Random();
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                double x = rand.nextGaussian();
                double y = rand.nextGaussian();
                series.add(x, y);
            }
        }
        xySeriesCollection.addSeries(series);
        return xySeriesCollection;
    }
    
    public static XYDataset createDataset(List<Individual> inds, String plotName) {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries(plotName);
        for (Individual ind : inds) {
        	series.add(ind.getDistanceFitness(), ind.getCostFitness());
        }
        xySeriesCollection.addSeries(series);
        return xySeriesCollection;
    }
    
    public void renderPlot() {
    	this.pack();
    	RefineryUtilities.centerFrameOnScreen(this);
    	this.setVisible(true);
    }

    public static void main(String args[]) {
    	Settings set = new Settings();
		List<Individual> inds = new ArrayList<Individual>();
		for (int i=0; i<40; i++) {
			inds.add(new Individual(set));
		}
		
		Population pop = new Population(set);
		ArrayList<ArrayList<Individual>> fronts = pop.nonDominatedSorting(inds);
		ArrayList<Individual> rest = new ArrayList<Individual>();
		
		for (int i=1; i<fronts.size(); i++) {
			rest.addAll(fronts.get(i));
		}
		
		XYDataset dataset = Plot.createDataset(rest, "Individual fitnesses");
		XYDataset dataset2 = Plot.createDataset(fronts.get(0), "Best fitnesses");
		
		Plot plot = new Plot("TestPlot", dataset);
		plot.addBestVals(dataset2);
		
		plot.renderPlot();
    }
}
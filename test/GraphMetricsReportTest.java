import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import dataImport.GraphmlLoaderFactory;
import dataImport.IGraphmlLoader;
import dataImport.IParser;
import dataImport.ParserFactory;
import model.GraphMetricsReport;
import model.DiachronicGraph;
import parmenidianEnumerations.Metric_Enums;

/**
 * Testing {@link model.GraphMetricsReport} class using Atlas as dataset.
 * @author MZ-IK
 * @since 2018-03-04
 * @version {2.0 - modified by KD}
 *
 */

public class GraphMetricsReportTest {
	
	private static DiachronicGraph diag;
	private static Metric_Enums metric;
	private static GraphMetricsReport graphArray;
	private static IGraphmlLoader gmlLoader;
	private static GraphmlLoaderFactory gmlFactory;
	private static IParser parser;
	private static ParserFactory pFactory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		diag = new DiachronicGraph();
		gmlFactory = new GraphmlLoaderFactory();
		gmlLoader = gmlFactory.createGraphmlLoader("C:\\Atlas_test\\output\\layout.graphml");
		diag.loadDiachronicGraph(gmlLoader.getNodes(), gmlLoader.getEdges(), "C:\\Users\\PANOS\\Documents\\EvolutionDatasets\\CERN\\Atlas\\processed schemata", "C:\\Atlas_test\\output", 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		pFactory = new ParserFactory();
		parser = pFactory.createHecateParser();
		diag.setVersions(parser.getLifetime("C:\\Users\\PANOS\\Documents\\EvolutionDatasets\\CERN\\Atlas\\processed schemata"));
		metric=Metric_Enums.GRAPH_DIAMETER;
		graphArray = new GraphMetricsReport("C:\\Atlas_test\\output\\tests",metric,diag);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPopulateArray(){
	}

	@Ignore
	public void testArrayPopulationForGraphMetrics() {
		//tested on MetricsReportEngine
	}

	@Test
	public void testGetDiachronicGraphMetricValue() {
		assertNotNull("DiachronicGraphMetricValue not null", graphArray.getDiachronicGraphMetricValue(metric.name()));
	}

	@Test
	public void testGetVersionMetricValue() {
		assertNotNull("VersionMetricValue not null", graphArray.getVersionMetricValue(metric.name(),2));	
	}

}

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import dataImport.GraphmlLoaderFactory;
import dataImport.IGraphmlLoader;
import model.GraphMetricsReport;
import model.VertexMetricsReport;
import model.DiachronicGraph;

import parmenidianEnumerations.Metric_Enums;

/**
 * Testing {@link model.MetricsReportEngine} class using Atlas as dataset.
 * @author MZ-IK
 * @since 2018-03-04
 * @version {2.0 - modified by KD}
 *
 */

public class MetricsReportEngineTest {
	
	private static GraphMetricsReport graphReport;
	private static VertexMetricsReport vertexReport;
	private static DiachronicGraph diag;
	private static Metric_Enums metric;
	private static GraphMetricsReport spyGraph;
	private static VertexMetricsReport spyVertex;
	private static IGraphmlLoader gmlLoader;
	private static GraphmlLoaderFactory gmlFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		
		diag = new DiachronicGraph();
		gmlFactory = new GraphmlLoaderFactory();
		gmlLoader = gmlFactory.createGraphmlLoader("C:\\Atlas_test\\output\\layout.graphml");
		diag.loadDiachronicGraph(gmlLoader.getNodes(), gmlLoader.getEdges(), "C:\\Users\\PANOS\\Documents\\EvolutionDatasets\\CERN\\Atlas\\processed schemata", "C:\\Atlas_test\\output", 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		metric=Metric_Enums.CLUSTERING_COEFFICIENT;
		graphReport = new GraphMetricsReport("C:\\Atlas_test\\output\\tests",metric, diag);
		vertexReport = new VertexMetricsReport("C:\\Atlas_test\\output\\tests",metric, diag);
		spyGraph = Mockito.spy(graphReport);
		spyVertex = Mockito.spy(vertexReport);
		
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
	public void testGenerateMetricsReport() {
		assertNull("graph report array is not filled", spyGraph.getReport());
		assertNull("vertex report array is not filled", spyVertex.getReport());
		spyGraph.generateMetricsReport();
		spyVertex.generateMetricsReport();
		assertNotNull("graph report array filled", spyGraph.getReport());
		assertNotNull("vertex report array filled", spyVertex.getReport());
	}

	@Ignore
	public void testCreateCsvFile() {
		//just opens a file
	}

	@Ignore
	public void testPopulateArray() {
		//abstract
	}

	@Ignore
	public void testPrintArrayIntoFile() {
		//just writes the array content to the file
	}

}

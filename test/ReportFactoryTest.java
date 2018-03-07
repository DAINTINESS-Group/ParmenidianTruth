import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import model.DiachronicGraph;
import model.MetricsReportEngine;
import model.ReportFactory;
import parmenidianEnumerations.Metric_Enums;

/**
 * Testing {@link model.ReportFactory} class.
 * @author MZ-IK
 * @since 2017-05-23
 * @version 1.0
 *
 */

public class ReportFactoryTest {
	
	private static DiachronicGraph diag;
	private static ReportFactory rf;
	private static Metric_Enums metric;
	private static Metric_Enums metric1;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		diag = Mockito.mock(DiachronicGraph.class);
		metric=Metric_Enums.CLUSTERING_COEFFICIENT;
		metric1=null;
		rf= new ReportFactory();
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
	public void testGetMetricsReportEngine() {
			MetricsReportEngine mReport=(MetricsReportEngine) rf.getMetricsReportEngine("C:\\Users\\mzerva\\Desktop\\Parmenidis_Output\\tests", metric, diag);
			assertNotNull("MetricsReportEngine object not null", mReport);
			MetricsReportEngine mReport1=(MetricsReportEngine) rf.getMetricsReportEngine("C:\\Users\\mzerva\\Desktop\\Parmenidis_Output\\tests", metric1, diag);
			assertNull("MetricsReportEngine is null", mReport1);
	}

}

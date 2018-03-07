

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Testing {@link reportFactoryTester} failure.
 * @author MZ-IK
 * @since 2017-05-23
 *
 */

public class TestRunner {

	public static void main(String[] args) {
		
		Result result = JUnitCore.runClasses(ReportFactoryTester.class);
		
	    for (Failure failure : result.getFailures()) {
	    	
	          System.out.println(failure.toString());
	    }
	    	  System.out.print("Successful result:");
	       	  System.out.println(result.wasSuccessful());
		

	}

}
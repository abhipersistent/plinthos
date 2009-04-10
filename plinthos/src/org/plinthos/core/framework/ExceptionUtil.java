package org.plinthos.core.framework;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.hibernate.exception.ExceptionUtils;

public class ExceptionUtil {

	private ExceptionUtil() {
		
	}
	
	public static String getCompactStackTrace(Throwable t) {
	    Writer result = new StringWriter();
		PrintWriter printWriter = new PrintWriter(result);
	    ExceptionUtils.printRootCauseStackTrace(t, printWriter);
	    return result.toString();
	}
	
}

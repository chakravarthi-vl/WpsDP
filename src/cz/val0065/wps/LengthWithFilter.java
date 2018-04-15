package cz.val0065.wps;

import java.io.IOException;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;

@DescribeProcess(title = "LengthWithFilter", description = "Overlay two polygons and measure area")
public class LengthWithFilter implements GeoServerProcess {

    @DescribeResult(name = "result", description = "output result")
    public String execute() throws IOException, Exception {
        Process p = new Process();
        return p.lengthOfLineWithFilter();
    }

}

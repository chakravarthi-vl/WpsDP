package cz.val0065.wps;

import java.io.IOException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geoserver.wps.gs.GeoServerProcess;

@DescribeProcess(title="overlayWPS", description="Creates buffer around point and overlays it with polygon layer. Returns areas of overlay.")
public class OverlayPolygons implements GeoServerProcess {
    
    @DescribeResult(name="result", description="output result")
    public String execute() throws IOException{
        Process p = new Process();
        return p.overlayPolygons();
    }    
}

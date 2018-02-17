package cz.val0065.wps;


import java.io.IOException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geoserver.wps.gs.GeoServerProcess;

@DescribeProcess(title="overlayWPS", description="Creates buffer around point and overlays it with polygon layer. Returns areas of overlay.")
public class OverlayWPS implements GeoServerProcess {

   @DescribeResult(name="result", description="output result")
   public String execute(@DescribeParameter(name="point", description="point") String point, @DescribeParameter(name="distance", description="distance to search") double distance) throws IOException {
       Process e = new Process();
       return e.overlay(point, distance);
   }
}

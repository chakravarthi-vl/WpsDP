/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.val0065.wps;

import java.io.IOException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geoserver.wps.gs.GeoServerProcess;

@DescribeProcess(title="OverlayWithFilter", description="Overlay two polygons and measure area")
public class OverlayWithFilter implements GeoServerProcess {
    
    @DescribeResult(name="result", description="output result")
    public String execute() throws IOException{
        Process p = new Process();
        return p.overlayPolygonsWithFilter();
    }    
}
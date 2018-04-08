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
import org.geotools.data.simple.SimpleFeatureCollection;

@DescribeProcess(title = "OverlayWithOutput", description = " ")
public class OverlayWithCollectionOutput implements GeoServerProcess {
    
    @DescribeResult(name = "result", description = "result")
    public SimpleFeatureCollection execute(@DescribeParameter(name = "point")String point, @DescribeParameter(name = "distance") double distance) throws IOException {
        Process p = new Process();
        return p.overlayWithOutput(point, distance);
    }
    
}

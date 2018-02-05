package cz.val0065.wps;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

/**
 * @author David
 * WPS development testing.
 */

public class Process {

    String overlay(String pointString, double distance) {
        String nazvy = "Objekt:Plocha prekryvu";
        try {

            //SHP Read
            ShapefileDataStore sfds = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));
            SimpleFeatureSource fs = sfds.getFeatureSource("restricted");

            //double distance = 10000.0d;
            GeometryFactory gf = new GeometryFactory();
            String xy[] = pointString.split(" ");
            Point point = gf.createPoint(new Coordinate(Double.parseDouble(xy[0]), Double.parseDouble(xy[1])));
            
            Polygon p1 = (Polygon) point.buffer(distance);
            
            SimpleFeatureIterator sfi = fs.getFeatures().features();
            while (sfi.hasNext()) {
                SimpleFeature sf = sfi.next();
                MultiPolygon mp2 = (MultiPolygon) sf.getDefaultGeometry();
                Polygon p2 = (Polygon) mp2.getGeometryN(0);
                Polygon p3 = (Polygon) p2.intersection(p1);
                nazvy = nazvy + "\n" + sf.getAttribute("cat") + ": " + p3.getArea();
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex2) {
            Logger.getLogger(Process.class.getName()).log(Level.SEVERE, null, ex2);
        }
        return "Nalezene objekty: " + nazvy;
    }
    
             
}

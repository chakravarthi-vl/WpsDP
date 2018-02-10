package cz.val0065.wps;

import com.vividsolutions.jts.geom.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.*;
import java.util.*;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.*;
import org.opengis.feature.simple.*;
import org.geotools.data.simple.*;

/**
 * @author David
 * WPS development testing.
 */

public class Process {

    String overlay(String pointString, double distance) {
        String nazvy = "Objekt:Plocha prekryvu";
        try {

            //SHP Read
            ShapefileDataStore sfds;
            sfds = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));
            SimpleFeatureSource fs;
            fs = sfds.getFeatureSource("restricted");
            
            ShapefileDataStore sfds2;
            sfds2 = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\archsites.shp"));
            SimpleFeatureSource fs2;
            fs2 = sfds.getFeatureSource("archsites");
            
            

            //double distance = 10000.0d;
            GeometryFactory gf = new GeometryFactory();
            String xy[] = pointString.split(" ");
            Point point = gf.createPoint(new Coordinate(Double.parseDouble(xy[0]), Double.parseDouble(xy[1])));
            
            Polygon p1 = (Polygon) point.buffer(distance);
            
            SimpleFeatureIterator sfi = fs.getFeatures().features();
            SimpleFeatureIterator sfi2 = fs.getFeatures().features();
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
    
    SimpleFeatureCollection overlayWithOutput(String pointString, double distance) throws MalformedURLException, IOException{
        
        SimpleFeatureCollection collection = null;
        
            ShapefileDataStore sfds = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));
            SimpleFeatureSource sfs = sfds.getFeatureSource("restricted");
            
            SimpleFeatureType type = sfs.getSchema();
            GeometryFactory gf = new GeometryFactory();
            String xy[] = pointString.split(" ");
            Point point = gf.createPoint(new Coordinate(Double.parseDouble(xy[0]), Double.parseDouble(xy[1])));
            
            Polygon p1 = (Polygon) point.buffer(distance);
            List<SimpleFeature> features = new ArrayList<>(0);
            SimpleFeatureIterator sfi = sfs.getFeatures().features();
            
            while (sfi.hasNext()){
                SimpleFeature sf = sfi.next();
                MultiPolygon mp1 = (MultiPolygon) sf.getDefaultGeometry();
                Polygon p2 = (Polygon) mp1.getGeometryN(0);
                Polygon p3 = (Polygon) p2.intersection(p1);
                
                if (p3.getArea() > 0) {
                    sf.setDefaultGeometry(p3);
                    features.add(sf);
                }
            }
            collection = new ListFeatureCollection(type, features);
            return collection;  
    }
    
             
}


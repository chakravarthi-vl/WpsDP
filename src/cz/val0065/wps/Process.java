package cz.val0065.wps;

import com.vividsolutions.jts.geom.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.*;
import java.util.*;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.*;
import org.opengis.feature.simple.*;
import org.geotools.data.simple.*;
import org.geotools.feature.FeatureCollection;

/**
 * @author David WPS development testing.
 */
public class Process {

    String overlay(String pointString, double distance) throws MalformedURLException, IOException {

        String nazvy = "Objekt:Plocha prekryvu";

        ShapefileDataStore sfds;
        sfds = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));
        SimpleFeatureSource fs;
        fs = sfds.getFeatureSource("restricted");

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
        return "Nalezene objekty: " + nazvy;
    }

    String overlay2(String pointString, double distance) throws MalformedURLException, IOException {
        String nazvy = "Objekt:Plocha prekryvu";

        ShapefileDataStore sfds1;
        sfds1 = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));
        SimpleFeatureSource fs1;
        fs1 = sfds1.getFeatureSource("restricted");

        ShapefileDataStore sfds2;
        sfds2 = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));
        SimpleFeatureSource fs2;
        fs2 = sfds1.getFeatureSource("restricted");

        GeometryFactory gf = new GeometryFactory();
        String xy[] = pointString.split(" ");
        Point point = gf.createPoint(new Coordinate(Double.parseDouble(xy[0]), Double.parseDouble(xy[1])));

        Polygon p1 = (Polygon) point.buffer(distance);

        SimpleFeatureIterator sfi = fs1.getFeatures().features();
        while (sfi.hasNext()) {
            SimpleFeature sf = sfi.next();
            MultiPolygon mp2 = (MultiPolygon) sf.getDefaultGeometry();
            Polygon p2 = (Polygon) mp2.getGeometryN(0);
            Polygon p3 = (Polygon) p2.intersection(p1);
            nazvy = nazvy + "\n" + sf.getAttribute("cat") + ": " + p3.getArea();
        }
        return "Nalezene objekty: " + nazvy;
    }

    public String lengthOfLine() throws Exception {

        ShapefileDataStore sfds1;
        sfds1 = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\streams.shp"));
        SimpleFeatureSource fs1;
        fs1 = sfds1.getFeatureSource("streams");
        
        SimpleFeatureIterator sfi = fs1.getFeatures().features();
        while (sfi.hasNext()) {
            SimpleFeature sf = sfi.next();
        
        }
        return null;

    }

    SimpleFeatureCollection overlayWithOutput(String pointString, double distance) throws MalformedURLException, IOException {

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

        while (sfi.hasNext()) {
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

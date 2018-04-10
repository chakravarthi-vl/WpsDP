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

public class Process {

    String overlay(String pointString, double distance, String shapeFileURL) throws MalformedURLException, IOException {

        String nazvy = "Objekt:Plocha prekryvu";

        ShapefileDataStore sfds;
        sfds = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));

        //ShapefileDataStoreFactory shpf = new ShapefileDataStoreFactory.ShpFileStoreFactory(new ShapefileDataStore);
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
        return "Nalezene: " + nazvy;
    }
    
    //ShapefileDataStoreFactory shpf = new ShapefileDataStoreFactory.ShpFileStoreFactory(new ShapefileDataStore);

    String overlayPolygons() throws IOException {

        String areas = "Object : Area of overlay";

        ShapefileDataStore sfds;
        sfds = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\test_data\\lesy_cr.shp"));

        SimpleFeatureSource fs;
        fs = sfds.getFeatureSource("lesy_cr");

        ShapefileDataStore sfds2;
        sfds2 = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\test_data\\chranene_uzemi_cr.shp"));

        SimpleFeatureSource fs2;
        fs2 = sfds2.getFeatureSource("chranene_uzemi_cr");

        SimpleFeatureIterator sfi = fs.getFeatures().features();
        double sum = 0;
        while (sfi.hasNext()) {
            SimpleFeature sf = sfi.next();
            MultiPolygon mp2 = (MultiPolygon) sf.getDefaultGeometry();
            Polygon p2 = (Polygon) mp2.getGeometryN(0);

            SimpleFeatureIterator sfi2 = fs2.getFeatures().features();
            while (sfi2.hasNext()) {
                SimpleFeature sf2 = sfi2.next();
                MultiPolygon mp3 = (MultiPolygon) sf2.getDefaultGeometry();
                Polygon p3 = (Polygon) mp3.getGeometryN(0);
                Geometry p4 = p2.intersection(p3);
                if (p4.getArea() != 0) {
                    sum += p4.getArea();
                    areas = areas + "\n" + p4.getArea()+ " : " + p2.getArea() + " : " + p3.getArea();
                }
            }
            sfi2.close();
        }
        sfi.close();
        
        sfds.dispose();
        sfds2.dispose();

        return "Objects found: " + areas + "\nTotal sum: " + sum;
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

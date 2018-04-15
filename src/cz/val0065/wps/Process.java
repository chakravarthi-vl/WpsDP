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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Filter;
import org.geotools.data.DataUtilities;
import org.opengis.filter.MultiValuedFilter;

public class Process {

    public String overlayPolygonWithBuffer(String pointString, double distance, String shapeFileURL)
            throws MalformedURLException, IOException {

        String nazvy = "Objekt:Plocha prekryvu";

        ShapefileDataStore sfds;
        sfds = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));

        // ShapefileDataStoreFactory shpf = new
        // ShapefileDataStoreFactory.ShpFileStoreFactory(new ShapefileDataStore);
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

    public String lengthOfLine() throws Exception {

        String lengths = "Delky linii";

        ShapefileDataStore sfds1 = new ShapefileDataStore(
                new URL("file:///C:\\GeoServer 2.8.5\\data_dir\\data\\sf\\restricted.shp"));
        SimpleFeatureSource fs1 = sfds1.getFeatureSource("restricted");

        ShapefileDataStore sfds2 = new ShapefileDataStore(
                new URL("file:///C:\\GeoServer 2.8.5\\data_dir\\data\\sf\\streams.shp"));
        SimpleFeatureSource fs2 = sfds2.getFeatureSource("streams");

        double sum = 0;

        try (SimpleFeatureIterator sfi = fs1.getFeatures().features()) {
            while (sfi.hasNext()) {
                sum = 0;
                SimpleFeature sf = sfi.next();
                MultiPolygon mp1 = (MultiPolygon) sf.getDefaultGeometry();
                Polygon p1 = (Polygon) mp1.getGeometryN(0);

                try (SimpleFeatureIterator sfi2 = fs2.getFeatures().features()) {
                    while (sfi2.hasNext()) {
                        SimpleFeature sf2 = sfi2.next();
                        MultiLineString mls = (MultiLineString) sf2.getDefaultGeometry();
                        LineString ls = (LineString) mls.getGeometryN(0);
                        Geometry result = p1.intersection(ls);
                        if (result.getLength() != 0) {
                            sum += result.getLength();
                            lengths = lengths + "\n" + result.getLength();
                        }
                    }
                }
            }
        }
        return "Lines found: " + lengths + "\nSuma: " + sum;

    }

    // ShapefileDataStoreFactory shpf = new
    // ShapefileDataStoreFactory.ShpFileStoreFactory(new ShapefileDataStore);
    public String overlayPolygons() throws IOException {

        String areas = "Object : Area of overlay";

        ShapefileDataStore sfds = new ShapefileDataStore(
                new URL("file:///F:\\GeoServer285\\data_dir\\data\\test_data\\chranene_uzemi_cr.shp"));
        SimpleFeatureSource fs = sfds.getFeatureSource();

        ShapefileDataStore sfds2 = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\test_data\\lesy_cr.shp"));
        SimpleFeatureSource fs2 = sfds2.getFeatureSource();

        double sum;
        try (SimpleFeatureIterator sfi = fs.getFeatures().features()) {
            sum = 0;
            while (sfi.hasNext()) {
                SimpleFeature sf = sfi.next();
                MultiPolygon mp2 = (MultiPolygon) sf.getDefaultGeometry();
                Polygon p2 = (Polygon) mp2.getGeometryN(0);

                try (SimpleFeatureIterator sfi2 = fs2.getFeatures().features()) {
                    while (sfi2.hasNext()) {
                        SimpleFeature sf2 = sfi2.next();
                        MultiPolygon mp3 = (MultiPolygon) sf2.getDefaultGeometry();
                        Polygon p3 = (Polygon) mp3.getGeometryN(0);
                        Geometry p4 = p2.intersection(p3);
                        if (p4.getArea() != 0) {
                            sum += p4.getArea();
                            areas = areas + "\n" + p4.getArea() + " : " + p2.getArea() + " : " + p3.getArea();
                        }
                    }
                }
            }
        }

        return "Objects found: " + areas + "\nTotal sum: " + sum;
    }

    public String overlayPolygonsWithFilter() throws IOException {

        long startTime = System.currentTimeMillis();

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

        String areas = "Object : Area of overlay";

        ShapefileDataStore sfds = new ShapefileDataStore(
                new URL("file:///F:\\GeoServer285\\data_dir\\data\\test_data\\chranene_uzemi_cr.shp"));
        SimpleFeatureSource fs = sfds.getFeatureSource();

        ShapefileDataStore sfds2 = new ShapefileDataStore(new URL("file:///F:\\GeoServer285\\data_dir\\data\\test_data\\lesy_cr.shp"));
        SimpleFeatureSource fs2 = sfds2.getFeatureSource();

        SimpleFeatureCollection sfc = DataUtilities.collection(fs.getFeatures());
        SimpleFeatureCollection sfc2 = DataUtilities.collection(fs2.getFeatures());

        ListFeatureCollection sfcList = new ListFeatureCollection(sfc);
        ListFeatureCollection sfcList2 = new ListFeatureCollection(sfc);

        double sum;
        try (SimpleFeatureIterator sfi = sfcList2.features()) {
            sum = 0;
            while (sfi.hasNext()) {

                SimpleFeature sf = sfi.next();
                MultiPolygon mp2 = (MultiPolygon) sf.getDefaultGeometry();

                Filter filter = ff.intersects(ff.property("the_geom"), ff.literal(sf.getDefaultGeometry()));

                try (SimpleFeatureIterator sfi2 = sfcList.subCollection(filter).features()) {
                    Polygon p2 = (Polygon) mp2.getGeometryN(0);

                    while (sfi2.hasNext()) {
                        SimpleFeature sf2 = sfi2.next();
                        MultiPolygon mp3 = (MultiPolygon) sf2.getDefaultGeometry();
                        Polygon p3 = (Polygon) mp3.getGeometryN(0);
                        Geometry p4 = p2.intersection(mp2);
                        if (p4.getArea() != 0) {
                            sum += p4.getArea();
                            areas = areas + "\n" + p4.getArea() + " : " + p2.getArea() + " : " + p3.getArea();
                        }
                    }
                }
            }
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println(elapsedTime);

        return "Objects found: " + areas + "\nTotal sum: " + sum;

    }

    SimpleFeatureCollection overlayWithOutput(String pointString, double distance)
            throws MalformedURLException, IOException {

        SimpleFeatureCollection collection = null;

        ShapefileDataStore sfds = new ShapefileDataStore(
                new URL("file:///F:\\GeoServer285\\data_dir\\data\\sf\\restricted.shp"));
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

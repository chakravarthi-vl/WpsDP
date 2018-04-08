package cz.val0065.wps;

import java.io.IOException;

public class Main {
    
        public static void main(String[] args) throws IOException, Exception {

        Process proc = new Process();
        
        String shapeFileURL = "";

        System.out.println(proc.overlayPolygons(shapeFileURL));
    }
    
}
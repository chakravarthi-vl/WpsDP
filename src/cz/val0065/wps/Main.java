package cz.val0065.wps;

import java.io.IOException;

public class Main {
    
        public static void main(String[] args) throws IOException, Exception {

        Process proc = new Process();

        System.out.println(proc.overlay("600000 4920000", 10000.0d));
    }
    
}

package cz.val0065.wps;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.factory.StaticMethodsProcessFactory;
import org.geotools.text.Text;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.OctagonalEnvelope;
import org.opengis.util.InternationalString;
/**
 *
 * @author David
 */
public class ProcessTutorial extends StaticMethodsProcessFactory<ProcessTutorial> {
    
    public ProcessTutorial() {
        super(Text.text("tutorial"), "tutorial", ProcessTutorial.class);
    }
    @DescribeProcess(title="Octagonal envelope", description="make octagonal envelope around geom")
    @DescribeResult(description="octagon of geom")
    static public Geometry octagonalEnvelope ( @DescribeParameter(name="geom")Geometry geom ) {
        return new OctagonalEnvelope(geom).toGeometry(geom.getFactory());   
    }
    
}

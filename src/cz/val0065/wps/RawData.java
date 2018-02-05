/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.val0065.wps;

import java.io.IOException;
import java.io.InputStream;
/**
 *
 * @author David
 */
public interface RawData {
        /**
     * Returns the mime type of the stream contents
     *
     * @return
     */
    public String getMimeType();

    /**
     * Gives access to the raw data contents.
     *
     * @return
     * @throws FileNotFoundException
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Optional field for output raw data, used by
     * WPS to generate a file extension
     *
     * @return
     */}

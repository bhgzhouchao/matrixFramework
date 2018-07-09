/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.cache;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FilterServletOutputStream extends ServletOutputStream {

    private OutputStream stream;
    
    public FilterServletOutputStream(final OutputStream stream) {
        this.stream = stream;
    }

    public void write(final int b) throws IOException {
        stream.write(b);
    }

    public void write(final byte[] b) throws IOException {
        stream.write(b);
    }

    public void write(final byte[] b, final int off, final int len) throws IOException {
        stream.write(b, off, len);
    }

    public OutputStream getStream() {
        return stream;
    }

    public void setStream(OutputStream stream) {
        this.stream = stream;
    }

}
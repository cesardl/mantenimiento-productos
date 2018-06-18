package org.sanmarcux.clases;

/**
 * @author cesar.diaz
 * @see <a href="http://stackoverflow.com/questions/1194656/appending-to-an-objectoutputstream/1195078#1195078">Appending
 * to an ObjectOutputStream</a>
 */
public class AppendingObjectOutputStream extends java.io.ObjectOutputStream {

    public AppendingObjectOutputStream(java.io.OutputStream out) throws java.io.IOException {
        super(out);
    }

    @Override
    protected void writeStreamHeader() throws java.io.IOException {
        // do not write a header, but reset:
        // this line added after another question
        // showed a problem with the original
        reset();
    }

}

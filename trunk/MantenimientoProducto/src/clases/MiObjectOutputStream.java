package clases;

public class MiObjectOutputStream extends java.io.ObjectOutputStream {

    public MiObjectOutputStream(java.io.OutputStream out) throws java.io.IOException {
        super(out);
    }

    protected MiObjectOutputStream() throws java.io.IOException, SecurityException {
        super();
    }

    @Override
    protected void writeStreamHeader() throws java.io.IOException {
    }
}

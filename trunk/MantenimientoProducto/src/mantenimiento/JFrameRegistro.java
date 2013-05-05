package mantenimiento;

import clases.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public final class JFrameRegistro extends javax.swing.JFrame
        implements java.awt.event.ActionListener, java.awt.event.KeyListener {

    private static final int ACTION_NOTHING = 0;
    private static final int ACTION_NEW = 1;
    private static final int ACTION_EDIT = 2;
    protected int iNumFila, iNumColumna, iAccion;
    protected Object objData[][];
    protected String strRuta = "C:\\MantenimientoProducto\\Producto.dat";
    protected String strTitulo[] = {
        "Codigo", "Descripcion", "Cantidad", "Precio", "Exonerado", "Visible"};
    protected ArrayList<Producto> vProductos;

    public JFrameRegistro() {
        initComponents();

        mostrarDatosDeRegistroTabla();
        controlarEstadoBotonesBarraHerramienta(1);
        controlarEstadoPanelIngresoDeDatos(false);
    }

    public void limpiarRegistro() {
        jTextFieldCodigo.setText("");
        jFormattedTextFieldDescripcion.setText("");
        jFormattedTextFieldCantidad.setText("0");
        jFormattedTextFieldPrecio.setText("0");
        jCheckBoxExonerado.setSelected(true);
        jCheckBoxVisible.setSelected(true);
    }

    public void controlarEstadoBotonesBarraHerramienta(int opcion) {
        switch (opcion) {
            case 1:
                jButtonGrabar.setEnabled(false);
                break;
            case 2:
                jButtonGrabar.setEnabled(true);
                break;
        }
    }

    public void controlarEstadoPanelIngresoDeDatos(boolean b) {
        java.awt.Component c[] = jPanelRegistro.getComponents();
        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof javax.swing.JFormattedTextField
                    || c[i] instanceof javax.swing.JCheckBox) {
                c[i].setEnabled(b);
            }
        }
    }

    public void mostrarDatosDeRegistroTabla() {
        iNumColumna = 6;
        vProductos = new ArrayList<Producto>();

        ArchivoProducto.cargarRegistrosArray(vProductos, strRuta);
        iNumFila = vProductos.size();
        objData = new Object[iNumFila][iNumColumna];

        for (int i = 0; i < iNumFila; i++) {
            Producto p = (Producto) vProductos.get(i);
            objData[i][0] = p.getCodigo();
            objData[i][1] = p.getDescripcion();
            objData[i][2] = p.getTotal();
            objData[i][3] = p.getPrecio();
            objData[i][4] = p.isExonerado();
            objData[i][5] = p.isVisible();
        }
        //Rediseniando la Tabla
        DefaultTableModel dtm = (DefaultTableModel) jTableDato.getModel();
        dtm.setDataVector(objData, strTitulo);
        jTableDato.setModel(dtm);
    }

    public void nuevoRegistro() {
        iAccion = ACTION_NEW;

        controlarEstadoBotonesBarraHerramienta(2);
        controlarEstadoPanelIngresoDeDatos(true);
        jFormattedTextFieldDescripcion.requestFocus();
    }

    public void consultarRegistro() {
        if (ArchivoProducto.cantidadRegistros(strRuta) == 0) {
            Base.mensaje("No existen productos", getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            String strCod = JOptionPane.showInputDialog(this,
                    "Ingrese codigo del producto:", getTitle(), JOptionPane.PLAIN_MESSAGE);
            if (strCod != null) {
                String strProducto = ArchivoProducto.consultarRegistro(strCod, strRuta);
                if (strCod != null) {
                    if (strProducto.equals("")) {
                        Base.mensaje("No existe el producto", getTitle(), JOptionPane.ERROR_MESSAGE);
                    } else {
                        Base.mensaje("Producto:\n" + strProducto, getTitle(), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }

    private boolean preGrabarRegistro() {
        boolean bValor = false;
        int iValor = 0;

        if (jFormattedTextFieldDescripcion.getText().trim().length() == 0) {
            iValor = 2;
        } else if (Base.convertirCadenaReal(jFormattedTextFieldCantidad.getText().trim()) == 0) {
            iValor = 3;
        } else if (Base.convertirCadenaReal(jFormattedTextFieldPrecio.getText().trim()) == 0) {
            iValor = 4;
        }

        switch (iValor) {
            case 1:
                Base.mensaje("Ingrese correctamente el Codigo.", getTitle(), JOptionPane.ERROR_MESSAGE);
                jTextFieldCodigo.requestFocus();
                break;
            case 2:
                Base.mensaje("Ingrese correctamente la Descripcion.", getTitle(), JOptionPane.ERROR_MESSAGE);
                jFormattedTextFieldDescripcion.requestFocus();
                break;
            case 3:
                Base.mensaje("Ingrese correctamente la Cantidad.", getTitle(), JOptionPane.ERROR_MESSAGE);
                jFormattedTextFieldCantidad.requestFocus();
                break;
            case 4:
                Base.mensaje("Ingrese correctamente el Precio.", getTitle(), JOptionPane.ERROR_MESSAGE);
                jFormattedTextFieldPrecio.requestFocus();
                break;
            default:
                bValor = true;
        }
        return bValor;
    }

    private boolean grabarRegistroArchivo() {
        boolean bValor = true;
        Producto producto = new Producto(
                ArchivoProducto.obtenerNumeroSecuencia(),
                jFormattedTextFieldDescripcion.getText().trim(),
                Base.convertirCadenaEntero(jFormattedTextFieldCantidad.getText().trim()),
                Base.convertirCadenaReal(jFormattedTextFieldPrecio.getText().trim()),
                jCheckBoxExonerado.isSelected() ? true : false,
                jCheckBoxVisible.isSelected() ? true : false);

        if (ArchivoProducto.cantidadRegistros(strRuta) == 0) {
            ArchivoProducto.crearArchivo(producto, strRuta);
        } else {
            ArchivoProducto.adicionarRegistro(producto, strRuta);
        }
        jTextFieldCodigo.setText(producto.getCodigo());
        return bValor;
    }

    private void postgrabarRegistro() {
        mostrarDatosDeRegistroTabla();
        controlarEstadoBotonesBarraHerramienta(1);
        controlarEstadoPanelIngresoDeDatos(false);
        Base.mensaje("Se grabo el registro satisfactoriamente.",
                getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean editarRegistroArchivo() {
        boolean bValor = true;
        Producto producto = new Producto(
                jTextFieldCodigo.getText(),
                jFormattedTextFieldDescripcion.getText().trim(),
                Base.convertirCadenaEntero(jFormattedTextFieldCantidad.getText().trim()),
                Base.convertirCadenaReal(jFormattedTextFieldPrecio.getText().trim()),
                jCheckBoxExonerado.isSelected() ? true : false,
                jCheckBoxVisible.isSelected() ? true : false);

        ArchivoProducto.modificarRegistro(producto, strRuta);
        jTextFieldCodigo.setText(producto.getCodigo());
        return bValor;
    }

    public void postEditarRegistro() {
        mostrarDatosDeRegistroTabla();
        controlarEstadoBotonesBarraHerramienta(1);
        controlarEstadoPanelIngresoDeDatos(false);
        Base.mensaje("Se actualizo el registro satisfactoriamente.",
                getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }

    public void grabarRegistro() {
        if (preGrabarRegistro()) {
            switch (iAccion) {
                case ACTION_NEW:
                    if (grabarRegistroArchivo()) {
                        postgrabarRegistro();
                    }
                    break;

                case ACTION_EDIT:
                    if (editarRegistroArchivo()) {
                        postgrabarRegistro();
                    }
                    break;
            }
        }
    }

    public void editarRegistro() {
        iAccion = ACTION_EDIT;

        int selectedRow = jTableDato.getSelectedRow();
        if (selectedRow == -1) {
            Base.mensaje("Seleccione un producto", getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            Producto prod = vProductos.get(selectedRow);

            controlarEstadoBotonesBarraHerramienta(2);
            controlarEstadoPanelIngresoDeDatos(true);

            String[] price = String.valueOf(prod.getPrecio()).split("\\.");
            String real = Base.completarIzquierda(price[0], 2, "0");
            String decimal = Base.completarDerecha(price[1], 2, "0");

            jTextFieldCodigo.setText(prod.getCodigo());
            jFormattedTextFieldDescripcion.setText(prod.getDescripcion());
            jFormattedTextFieldCantidad.setText(Base.completarIzquierda(String.valueOf(prod.getTotal()), 5, "0"));
            jFormattedTextFieldPrecio.setText(real.concat(".").concat(decimal));
            jCheckBoxExonerado.setSelected(prod.isExonerado());
            jCheckBoxVisible.setSelected(prod.isVisible());

            jFormattedTextFieldDescripcion.requestFocus();
        }
    }

    public void anularRegistro() {
        if (ArchivoProducto.cantidadRegistros(strRuta) == 0) {
            Base.mensaje("No existen productos", getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            String strCod = JOptionPane.showInputDialog(this,
                    "Ingrese codigo del producto a eliminar:", getTitle(), JOptionPane.PLAIN_MESSAGE);
            if (strCod != null) {
                if (ArchivoProducto.anularRegistro(strCod, strRuta)) {
                    mostrarDatosDeRegistroTabla();
                    Base.mensaje("Registro eliminado", getTitle(), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Base.mensaje("No existe el producto", getTitle(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void salirRegistro() {
        if (JOptionPane.showConfirmDialog(this, "¿Seguro que desea salir?",
                getTitle(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBarRegistro = new javax.swing.JToolBar();
        jButtonNuevo = new javax.swing.JButton();
        jButtonConsultar = new javax.swing.JButton();
        jButtonGrabar = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonAnular = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jPanelCabecera = new javax.swing.JPanel();
        jPanelRegistro = new javax.swing.JPanel();
        jLabelCodigo = new javax.swing.JLabel();
        jLabelDescripcion = new javax.swing.JLabel();
        jLabelCantidad = new javax.swing.JLabel();
        jLabelPrecio = new javax.swing.JLabel();
        jTextFieldCodigo = new javax.swing.JTextField();
        jFormattedTextFieldDescripcion = new javax.swing.JFormattedTextField();
        jFormattedTextFieldCantidad = new javax.swing.JFormattedTextField();
        jFormattedTextFieldPrecio = new javax.swing.JFormattedTextField();
        jCheckBoxExonerado = new javax.swing.JCheckBox();
        jCheckBoxVisible = new javax.swing.JCheckBox();
        jPanelDetalle = new javax.swing.JPanel();
        jScrollPaneDato = new javax.swing.JScrollPane();
        jTableDato = new javax.swing.JTable();
        defaultTableModel = new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            strTitulo
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mantenimiento de Productos");

        jButtonNuevo.setBackground(new java.awt.Color(255, 255, 255));
        jButtonNuevo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.gif")));
        jButtonNuevo.setMnemonic('N');
        jButtonNuevo.setToolTipText("Nuevo Registro");
        jButtonNuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonNuevo.setHideActionText(true);
        jButtonNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNuevo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNuevo.addActionListener(this);
        jToolBarRegistro.add(jButtonNuevo);

        jButtonConsultar.setBackground(new java.awt.Color(255, 255, 255));
        jButtonConsultar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/consultar.gif")));
        jButtonConsultar.setMnemonic('C');
        jButtonConsultar.setToolTipText("Consultar Registro");
        jButtonConsultar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonConsultar.setHideActionText(true);
        jButtonConsultar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonConsultar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonConsultar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonConsultar.addActionListener(this);
        jToolBarRegistro.add(jButtonConsultar);

        jButtonGrabar.setBackground(new java.awt.Color(255, 255, 255));
        jButtonGrabar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/grabar.gif")));
        jButtonGrabar.setMnemonic('G');
        jButtonGrabar.setToolTipText("Grabar Registro");
        jButtonGrabar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonGrabar.setHideActionText(true);
        jButtonGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonGrabar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonGrabar.addActionListener(this);
        jToolBarRegistro.add(jButtonGrabar);

        jButtonEdit.setBackground(new java.awt.Color(255, 255, 255));
        jButtonEdit.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/edit.gif")));
        jButtonEdit.setMnemonic('E');
        jButtonEdit.setToolTipText("Editar Registro");
        jButtonEdit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonEdit.setFocusable(false);
        jButtonEdit.setHideActionText(true);
        jButtonEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEdit.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEdit.addActionListener(this);
        jToolBarRegistro.add(jButtonEdit);

        jButtonAnular.setBackground(new java.awt.Color(255, 255, 255));
        jButtonAnular.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/anular.gif")));
        jButtonAnular.setMnemonic('A');
        jButtonAnular.setToolTipText("Anular Registro");
        jButtonAnular.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAnular.setHideActionText(true);
        jButtonAnular.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAnular.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonAnular.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAnular.addActionListener(this);
        jToolBarRegistro.add(jButtonAnular);

        jButtonSalir.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSalir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButtonSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.gif")));
        jButtonSalir.setMnemonic('S');
        jButtonSalir.setToolTipText("Salir de la Programa");
        jButtonSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSalir.setHideActionText(true);
        jButtonSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSalir.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSalir.addActionListener(this);
        jToolBarRegistro.add(jButtonSalir);

        jPanelCabecera.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCabecera.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanelRegistro.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRegistro.setBorder(javax.swing.BorderFactory.createTitledBorder("Producto"));

        jLabelCodigo.setText("Codigo:");

        jLabelDescripcion.setText("Descripcion:");

        jLabelCantidad.setText("Cantidad:");

        jLabelPrecio.setText("Precio:");

        jTextFieldCodigo.setEditable(false);
        jTextFieldCodigo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextFieldCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jFormattedTextFieldDescripcion.requestFocus();
        jFormattedTextFieldDescripcion.addKeyListener(this);

        jFormattedTextFieldCantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextFieldCantidad.setFormatterFactory(Base.creaFormatoControl(Base.TIPO_NUM_ENTERO, 5, 0, '0'));
        jFormattedTextFieldCantidad.addKeyListener(this);

        jFormattedTextFieldPrecio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextFieldPrecio.setFormatterFactory(Base.creaFormatoControl(Base.TIPO_NUM_REAL, 2, 2, '0'));
        jFormattedTextFieldPrecio.addKeyListener(this);

        jCheckBoxExonerado.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBoxExonerado.setSelected(true);
        jCheckBoxExonerado.setText("Exonerado de IGV");
        jCheckBoxExonerado.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxExonerado.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBoxExonerado.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jCheckBoxVisible.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBoxVisible.setSelected(true);
        jCheckBoxVisible.setText("Visible");
        jCheckBoxVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxVisible.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBoxVisible.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanelRegistroLayout = new javax.swing.GroupLayout(jPanelRegistro);
        jPanelRegistro.setLayout(jPanelRegistroLayout);
        jPanelRegistroLayout.setHorizontalGroup(
            jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldCodigo)
                    .addComponent(jFormattedTextFieldCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRegistroLayout.createSequentialGroup()
                        .addComponent(jFormattedTextFieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addComponent(jCheckBoxExonerado)
                        .addGap(40, 40, 40)
                        .addComponent(jCheckBoxVisible)
                        .addGap(24, 24, 24))
                    .addComponent(jFormattedTextFieldDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelRegistroLayout.setVerticalGroup(
            jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRegistroLayout.createSequentialGroup()
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCodigo)
                    .addComponent(jTextFieldCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDescripcion))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFormattedTextFieldPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxVisible)
                    .addComponent(jLabelCantidad)
                    .addComponent(jFormattedTextFieldCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelPrecio)
                    .addComponent(jCheckBoxExonerado))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelCabeceraLayout = new javax.swing.GroupLayout(jPanelCabecera);
        jPanelCabecera.setLayout(jPanelCabeceraLayout);
        jPanelCabeceraLayout.setHorizontalGroup(
            jPanelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelCabeceraLayout.setVerticalGroup(
            jPanelCabeceraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCabeceraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanelDetalle.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPaneDato.setBackground(new java.awt.Color(255, 255, 255));

        jTableDato.setModel(defaultTableModel);
        jTableDato.getTableHeader().setReorderingAllowed(false);
        jScrollPaneDato.setViewportView(jTableDato);

        javax.swing.GroupLayout jPanelDetalleLayout = new javax.swing.GroupLayout(jPanelDetalle);
        jPanelDetalle.setLayout(jPanelDetalleLayout);
        jPanelDetalleLayout.setHorizontalGroup(
            jPanelDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneDato, javax.swing.GroupLayout.DEFAULT_SIZE, 682, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelDetalleLayout.setVerticalGroup(
            jPanelDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDetalleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneDato, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
            .addComponent(jPanelCabecera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanelDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBarRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelCabecera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDetalle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        if (ae.getSource().equals(jButtonNuevo)) {
            limpiarRegistro();
            nuevoRegistro();
        }
        if (ae.getSource().equals(jButtonConsultar)) {
            consultarRegistro();
        }
        if (ae.getSource().equals(jButtonGrabar)) {
            grabarRegistro();
        }
        if (ae.getSource().equals(jButtonEdit)) {
            editarRegistro();
        }
        if (ae.getSource().equals(jButtonAnular)) {
            anularRegistro();
        }
        if (ae.getSource().equals(jButtonSalir)) {
            salirRegistro();
        }
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent ke) {
        if (ke.getSource() == jFormattedTextFieldDescripcion) {
            if (ke.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                jFormattedTextFieldCantidad.requestFocus();
            }
        }
        if (ke.getSource() == jFormattedTextFieldCantidad) {
            if (ke.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                jFormattedTextFieldPrecio.requestFocus();
            }
        }
        if (ke.getSource() == jFormattedTextFieldPrecio) {
            if (ke.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                if (jButtonGrabar.isEnabled()) {
                    grabarRegistro();
                }
            }
        }
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent ke) {
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent ke) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnular;
    private javax.swing.JButton jButtonConsultar;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonGrabar;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JCheckBox jCheckBoxExonerado;
    private javax.swing.JCheckBox jCheckBoxVisible;
    private javax.swing.JFormattedTextField jFormattedTextFieldCantidad;
    private javax.swing.JFormattedTextField jFormattedTextFieldDescripcion;
    private javax.swing.JFormattedTextField jFormattedTextFieldPrecio;
    private javax.swing.JLabel jLabelCantidad;
    private javax.swing.JLabel jLabelCodigo;
    private javax.swing.JLabel jLabelDescripcion;
    private javax.swing.JLabel jLabelPrecio;
    private javax.swing.JPanel jPanelCabecera;
    private javax.swing.JPanel jPanelDetalle;
    private javax.swing.JPanel jPanelRegistro;
    private javax.swing.JScrollPane jScrollPaneDato;
    private javax.swing.JTable jTableDato;
    private javax.swing.table.DefaultTableModel defaultTableModel;
    private javax.swing.JTextField jTextFieldCodigo;
    private javax.swing.JToolBar jToolBarRegistro;
    // End of variables declaration//GEN-END:variables
}

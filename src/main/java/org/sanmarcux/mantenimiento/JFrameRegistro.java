package org.sanmarcux.mantenimiento;

import org.sanmarcux.clases.ArchivoProducto;
import org.sanmarcux.clases.Base;
import org.sanmarcux.clases.Producto;

import javax.swing.*;

/**
 * @author cesar.diaz
 */
public final class JFrameRegistro extends javax.swing.JFrame
        implements java.awt.event.ActionListener, java.awt.event.KeyListener {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(JFrameRegistro.class);

    private static final String PRODUCTS_NOT_FOUND_MESSAGE = "No existen productos";

    private static final String[] COLUMN_NAMES = {
            "Codigo", "Descripcion", "Cantidad", "Precio", "Exonerado", "Visible"
    };

    private enum ActionType {
        NEW, EDIT, ON_USE
    }

    private final String strRuta;

    private ActionType currentAction;
    private java.util.List<Producto> vProductos;

    public JFrameRegistro(final String path) {
        this.strRuta = path;

        initComponents();

        currentAction = ActionType.ON_USE;

        controlarEstadoPanelIngresoDeDatos(false);
    }

    private void limpiarRegistro() {
        jTextFieldCodigo.setText("");
        jFormattedTextFieldDescripcion.setText("");
        jFormattedTextFieldCantidad.setText("0");
        jFormattedTextFieldPrecio.setText("0");
        jCheckBoxExonerado.setSelected(true);
        jCheckBoxVisible.setSelected(true);
    }

    private void controlarEstadoBotonesBarraHerramienta(ActionType opcion) {
        if (opcion == ActionType.NEW) {
            jButtonGrabar.setEnabled(true);

            jButtonConsultar.setEnabled(false);
            jButtonEdit.setEnabled(false);
            jButtonAnular.setEnabled(false);
            jButtonInfo.setEnabled(false);
            jButtonSalir.setEnabled(false);

        } else if (opcion == ActionType.EDIT) {
            jButtonGrabar.setEnabled(false);

            jButtonConsultar.setEnabled(true);
            jButtonEdit.setEnabled(true);
            jButtonAnular.setEnabled(true);
            jButtonInfo.setEnabled(true);
            jButtonSalir.setEnabled(true);
        }
    }

    private void controlarEstadoPanelIngresoDeDatos(boolean b) {
        java.awt.Component[] components = jPanelRegistro.getComponents();
        for (java.awt.Component c : components) {
            if (c instanceof javax.swing.JFormattedTextField
                    || c instanceof javax.swing.JCheckBox) {
                c.setEnabled(b);
            }
        }
    }

    private void mostrarDatosDeRegistroTabla() {
        //Rediseniando la Tabla
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) jTableDato.getModel();
        dtm.setDataVector(getTableData(), COLUMN_NAMES);
        jTableDato.setModel(dtm);
    }

    private Object[][] getTableData() {
        vProductos = new java.util.ArrayList<>();

        ArchivoProducto.cargarRegistrosArray(vProductos, strRuta);
        int size = vProductos.size();
        Object[][] data = new Object[size][6];

        for (int i = 0; i < size; i++) {
            Producto p = vProductos.get(i);
            data[i][0] = p.getCodigo();
            data[i][1] = p.getDescripcion();
            data[i][2] = p.getTotal();
            data[i][3] = Base.formatearNumeroYDigitos(p.getPrecio());
            data[i][4] = p.isExonerado();
            data[i][5] = p.isVisible();
        }
        return data;
    }

    private void nuevoRegistro() {
        currentAction = ActionType.NEW;

        limpiarRegistro();
        controlarEstadoBotonesBarraHerramienta(ActionType.NEW);
        controlarEstadoPanelIngresoDeDatos(true);
        jFormattedTextFieldDescripcion.requestFocus();
    }

    private void consultarRegistro() {
        if (ArchivoProducto.cantidadRegistros(strRuta) == 0) {
            Base.mensaje(PRODUCTS_NOT_FOUND_MESSAGE, getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            String strCod = JOptionPane.showInputDialog(this,
                    "Ingrese codigo del producto:", getTitle(), JOptionPane.PLAIN_MESSAGE);
            if (strCod != null) {
                String strProducto = ArchivoProducto.consultarRegistro(strCod, strRuta);

                if (strProducto.isEmpty()) {
                    Base.mensaje("No existe el producto", getTitle(), JOptionPane.ERROR_MESSAGE);
                } else {
                    Base.mensaje("Producto:\n" + strProducto, getTitle(), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private boolean validarFormulario() {
        int iValor = 0;

        if (jFormattedTextFieldDescripcion.getText().isEmpty()) {
            iValor = 2;
        } else if (Base.convertirCadenaReal(jFormattedTextFieldCantidad.getText().trim()) == 0) {
            iValor = 3;
        } else if (Base.convertirCadenaReal(jFormattedTextFieldPrecio.getText().trim()) == 0) {
            iValor = 4;
        }

        switch (iValor) {
            case 2:
                Base.mensaje("Ingrese correctamente la Descripcion.", getTitle(), JOptionPane.ERROR_MESSAGE);
                jFormattedTextFieldDescripcion.requestFocus();
                return false;

            case 3:
                Base.mensaje("Ingrese correctamente la Cantidad.", getTitle(), JOptionPane.ERROR_MESSAGE);
                jFormattedTextFieldCantidad.requestFocus();
                return false;

            case 4:
                Base.mensaje("Ingrese correctamente el Precio.", getTitle(), JOptionPane.ERROR_MESSAGE);
                jFormattedTextFieldPrecio.requestFocus();
                return false;

            default:
                return true;
        }
    }

    private boolean grabarRegistroArchivo() {
        Producto producto = new Producto(
                ArchivoProducto.obtenerNumeroSecuencia(),
                jFormattedTextFieldDescripcion.getText().trim(),
                Base.convertirCadenaEntero(jFormattedTextFieldCantidad.getText().trim()),
                Base.convertirCadenaReal(jFormattedTextFieldPrecio.getText().trim()),
                jCheckBoxExonerado.isSelected(),
                jCheckBoxVisible.isSelected());

        if (ArchivoProducto.cantidadRegistros(strRuta) == 0) {
            ArchivoProducto.crearArchivo(producto, strRuta);
        } else {
            ArchivoProducto.adicionarRegistro(producto, strRuta);
        }
        jTextFieldCodigo.setText(producto.getCodigo());
        return true;
    }

    private void postGrabarRegistro() {
        mostrarDatosDeRegistroTabla();
        controlarEstadoBotonesBarraHerramienta(ActionType.EDIT);
        controlarEstadoPanelIngresoDeDatos(false);
        Base.mensaje("Se grabo el registro satisfactoriamente.",
                getTitle(), JOptionPane.INFORMATION_MESSAGE);
        currentAction = ActionType.ON_USE;
    }

    private boolean editarRegistroArchivo() {
        Producto producto = new Producto(
                jTextFieldCodigo.getText(),
                jFormattedTextFieldDescripcion.getText().trim(),
                Base.convertirCadenaEntero(jFormattedTextFieldCantidad.getText().trim()),
                Base.convertirCadenaReal(jFormattedTextFieldPrecio.getText().trim()),
                jCheckBoxExonerado.isSelected(),
                jCheckBoxVisible.isSelected());

        boolean result = ArchivoProducto.modificarRegistro(producto, strRuta);
        jTextFieldCodigo.setText(producto.getCodigo());
        return result;
    }

    private void postEditarRegistro() {
        mostrarDatosDeRegistroTabla();
        controlarEstadoBotonesBarraHerramienta(ActionType.EDIT);
        controlarEstadoPanelIngresoDeDatos(false);
        limpiarRegistro();
        Base.mensaje("Se actualizo el registro satisfactoriamente.",
                getTitle(), JOptionPane.INFORMATION_MESSAGE);
        currentAction = ActionType.ON_USE;
    }

    private void grabarRegistro() {
        if (validarFormulario()) {
            if (currentAction == ActionType.NEW) {
                if (grabarRegistroArchivo()) {
                    postGrabarRegistro();
                }

            } else if (currentAction == ActionType.EDIT) {
                if (editarRegistroArchivo()) {
                    postEditarRegistro();
                }

            }
        }
    }

    private void editarRegistro() {
        if (ArchivoProducto.cantidadRegistros(strRuta) == 0) {
            Base.mensaje(PRODUCTS_NOT_FOUND_MESSAGE, getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            currentAction = ActionType.EDIT;

            int selectedRow = jTableDato.getSelectedRow();
            if (selectedRow == -1) {
                Base.mensaje("Seleccione un producto", getTitle(), JOptionPane.WARNING_MESSAGE);
            } else {
                Producto prod = vProductos.get(selectedRow);

                controlarEstadoBotonesBarraHerramienta(ActionType.NEW);
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
    }

    private void anularRegistro() {
        if (ArchivoProducto.cantidadRegistros(strRuta) == 0) {
            Base.mensaje(PRODUCTS_NOT_FOUND_MESSAGE, getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            int selectedRow = jTableDato.getSelectedRow();
            if (selectedRow == -1) {
                Base.mensaje("Seleccione un producto", getTitle(), JOptionPane.WARNING_MESSAGE);
            } else {
                if (JOptionPane.showConfirmDialog(this, "Seguro que desea eliminar este producto?",
                        getTitle(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                    String strCod = vProductos.get(selectedRow).getCodigo();
                    if (ArchivoProducto.anularRegistro(strCod, strRuta)) {
                        mostrarDatosDeRegistroTabla();
                        Base.mensaje("Producto eliminado", getTitle(), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }

    private void info() {
        String mensaje = String.format("%s@%s\n%s %s", Base.getNombreMaquina(), Base.getDireccionIp(), Base.getFecha(), Base.getHoraMinAmPm2());
        LOG.debug(mensaje);
        Base.mensaje(mensaje, getTitle(), JOptionPane.PLAIN_MESSAGE);
    }

    private void salirRegistro() {
        if (JOptionPane.showConfirmDialog(this, "Seguro que desea salir?",
                getTitle(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JToolBar jToolBarRegistro = new javax.swing.JToolBar();
        jButtonNuevo = new javax.swing.JButton();
        jButtonConsultar = new javax.swing.JButton();
        jButtonGrabar = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonAnular = new javax.swing.JButton();
        jButtonInfo = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        javax.swing.JPanel jPanelCabecera = new javax.swing.JPanel();
        jPanelRegistro = new javax.swing.JPanel();
        javax.swing.JLabel jLabelCodigo = new javax.swing.JLabel();
        javax.swing.JLabel jLabelDescripcion = new javax.swing.JLabel();
        javax.swing.JLabel jLabelCantidad = new javax.swing.JLabel();
        javax.swing.JLabel jLabelPrecio = new javax.swing.JLabel();
        jTextFieldCodigo = new javax.swing.JTextField();
        jFormattedTextFieldDescripcion = new javax.swing.JFormattedTextField();
        jFormattedTextFieldCantidad = new javax.swing.JFormattedTextField();
        jFormattedTextFieldPrecio = new javax.swing.JFormattedTextField();
        jCheckBoxExonerado = new javax.swing.JCheckBox();
        jCheckBoxVisible = new javax.swing.JCheckBox();
        javax.swing.JPanel jPanelDetalle = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPaneDato = new javax.swing.JScrollPane();
        jTableDato = new javax.swing.JTable();
        javax.swing.table.DefaultTableModel defaultTableModel = new javax.swing.table.DefaultTableModel(
                getTableData(),
                COLUMN_NAMES
        ) {
            Class[] types = new Class[]{
                    java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mantenimiento de Productos");
        setMinimumSize(new java.awt.Dimension(850, 320));

        jToolBarRegistro.setFloatable(false);

        jButtonNuevo.setBackground(new java.awt.Color(255, 255, 255));
        jButtonNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.gif")));
        jButtonNuevo.setMnemonic('N');
        jButtonNuevo.setToolTipText("Nuevo Registro");
        jButtonNuevo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonNuevo.setFocusable(false);
        jButtonNuevo.setHideActionText(true);
        jButtonNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNuevo.setName("buttonNew"); // NOI18N
        jButtonNuevo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNuevo.addActionListener(this);
        jToolBarRegistro.add(jButtonNuevo);

        jButtonConsultar.setBackground(new java.awt.Color(255, 255, 255));
        jButtonConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/consultar.gif")));
        jButtonConsultar.setMnemonic('C');
        jButtonConsultar.setToolTipText("Consultar Registro");
        jButtonConsultar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonConsultar.setFocusable(false);
        jButtonConsultar.setHideActionText(true);
        jButtonConsultar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonConsultar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonConsultar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonConsultar.addActionListener(this);
        jToolBarRegistro.add(jButtonConsultar);

        jButtonGrabar.setBackground(new java.awt.Color(255, 255, 255));
        jButtonGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/grabar.gif")));
        jButtonGrabar.setMnemonic('G');
        jButtonGrabar.setToolTipText("Grabar Registro");
        jButtonGrabar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonGrabar.setEnabled(false);
        jButtonGrabar.setFocusable(false);
        jButtonGrabar.setHideActionText(true);
        jButtonGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonGrabar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonGrabar.addActionListener(this);
        jToolBarRegistro.add(jButtonGrabar);

        jButtonEdit.setBackground(new java.awt.Color(255, 255, 255));
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
        jButtonAnular.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/anular.gif")));
        jButtonAnular.setMnemonic('A');
        jButtonAnular.setToolTipText("Anular Registro");
        jButtonAnular.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAnular.setFocusable(false);
        jButtonAnular.setHideActionText(true);
        jButtonAnular.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonAnular.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonAnular.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonAnular.addActionListener(this);
        jToolBarRegistro.add(jButtonAnular);

        jButtonInfo.setBackground(new java.awt.Color(255, 255, 255));
        jButtonInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info.gif")));
        jButtonInfo.setMnemonic('I');
        jButtonInfo.setToolTipText("Información");
        jButtonInfo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonInfo.setFocusable(false);
        jButtonInfo.setHideActionText(true);
        jButtonInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonInfo.addActionListener(this);
        jToolBarRegistro.add(jButtonInfo);

        jButtonSalir.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.gif")));
        jButtonSalir.setMnemonic('S');
        jButtonSalir.setToolTipText("Salir de la Programa");
        jButtonSalir.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSalir.setFocusable(false);
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
        jTextFieldCodigo.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        jFormattedTextFieldDescripcion.requestFocus();
        jFormattedTextFieldDescripcion.addKeyListener(this);

        jFormattedTextFieldCantidad.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jFormattedTextFieldCantidad.setFormatterFactory(Base.creaFormatoControl(5, 0, '0'));
        jFormattedTextFieldCantidad.addKeyListener(this);

        jFormattedTextFieldPrecio.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jFormattedTextFieldPrecio.setFormatterFactory(Base.creaFormatoControl(2, 2, '0'));
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
                                .addGap(12, 12, 12)
                                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabelCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                        .addComponent(jLabelCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldCodigo)
                                        .addGroup(jPanelRegistroLayout.createSequentialGroup()
                                                .addComponent(jFormattedTextFieldCantidad, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                                .addGap(1, 1, 1)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelRegistroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanelRegistroLayout.createSequentialGroup()
                                                .addComponent(jFormattedTextFieldPrecio, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                                .addGap(53, 53, 53)
                                                .addComponent(jCheckBoxExonerado)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jCheckBoxVisible, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jFormattedTextFieldDescripcion))
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
                                .addContainerGap(13, Short.MAX_VALUE))
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
                                .addComponent(jScrollPaneDato, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanelDetalleLayout.setVerticalGroup(
                jPanelDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelDetalleLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPaneDato, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jToolBarRegistro, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
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
        Base.centrarVentana(this);
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void actionPerformed(java.awt.event.ActionEvent ae) {
        LOG.debug("Current action: {}", currentAction);
        if (ae.getSource().equals(jButtonNuevo)) {
            LOG.debug("Nuevo registro");
            nuevoRegistro();
        }
        if (ae.getSource().equals(jButtonConsultar)) {
            LOG.debug("Consultar registro");
            consultarRegistro();
        }
        if (ae.getSource().equals(jButtonGrabar)) {
            LOG.debug("Grabar registro");
            grabarRegistro();
        }
        if (ae.getSource().equals(jButtonEdit)) {
            LOG.debug("Editar registro");
            editarRegistro();
        }
        if (ae.getSource().equals(jButtonAnular)) {
            LOG.debug("Anular registro");
            anularRegistro();
        }
        if (ae.getSource().equals(jButtonInfo)) {
            LOG.debug("Mostrar detalle de la PC");
            info();
        }
        if (ae.getSource().equals(jButtonSalir)) {
            LOG.debug("Salir de la aplicacion");
            salirRegistro();
        }
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent ke) {
        LOG.trace(ke.paramString());
        if (ke.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            if (ke.getSource() == jFormattedTextFieldDescripcion) {
                jFormattedTextFieldCantidad.requestFocus();
            }
            if (ke.getSource() == jFormattedTextFieldCantidad) {
                jFormattedTextFieldPrecio.requestFocus();
            }
            if (ke.getSource() == jFormattedTextFieldPrecio
                    && jButtonGrabar.isEnabled()) {
                grabarRegistro();
            }
        }
    }

    @Override
    public void keyTyped(java.awt.event.KeyEvent ke) {
        LOG.trace(ke.paramString());
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent ke) {
        LOG.trace(ke.paramString());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnular;
    private javax.swing.JButton jButtonConsultar;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonGrabar;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JCheckBox jCheckBoxExonerado;
    private javax.swing.JCheckBox jCheckBoxVisible;
    private javax.swing.JFormattedTextField jFormattedTextFieldCantidad;
    private javax.swing.JFormattedTextField jFormattedTextFieldDescripcion;
    private javax.swing.JFormattedTextField jFormattedTextFieldPrecio;
    private javax.swing.JPanel jPanelRegistro;
    private javax.swing.JTable jTableDato;
    private javax.swing.JTextField jTextFieldCodigo;
    // End of variables declaration//GEN-END:variables
}

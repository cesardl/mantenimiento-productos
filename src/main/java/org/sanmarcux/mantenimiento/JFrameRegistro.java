package org.sanmarcux.mantenimiento;

import org.sanmarcux.clases.ArchivoProducto;
import org.sanmarcux.clases.Base;
import org.sanmarcux.clases.Producto;

import javax.swing.JOptionPane;

/**
 * @author cesar.diaz
 */
public final class JFrameRegistro extends javax.swing.JFrame {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(JFrameRegistro.class);

    private static final java.util.ResourceBundle APPLICATION_RESOURCES = java.util.ResourceBundle.getBundle("view/Bundle");

    private static final String[] COLUMN_NAMES = {
            APPLICATION_RESOURCES.getString("table.columnText.code"),
            APPLICATION_RESOURCES.getString("table.columnText.description"),
            APPLICATION_RESOURCES.getString("table.columnText.quantity"),
            APPLICATION_RESOURCES.getString("table.columnText.price"),
            APPLICATION_RESOURCES.getString("table.columnText.exonerated"),
            APPLICATION_RESOURCES.getString("table.columnText.visible")
    };

    private enum ActionType {
        NEW, EDIT, ON_USE
    }

    private final String path;

    private ActionType currentAction;
    private java.util.List<Producto> vProducts;

    public JFrameRegistro(final String path) {
        this.path = path;

        initComponents();

        currentAction = ActionType.ON_USE;

        controlarEstadoPanelIngresoDeDatos(false);
    }

    private void limpiarRegistro() {
        jTextFieldCode.setText("");
        jTextFieldDescription.setText("");
        jFormattedTextFieldQuantity.setText("0");
        jFormattedTextFieldPrice.setText("0");
        jCheckBoxExonerated.setSelected(true);
        jCheckBoxVisible.setSelected(true);
    }

    private void controlarEstadoBotonesBarraHerramienta(final boolean b) {
        jButtonSave.setEnabled(b);

        if (b) {
            jButtonNew.setEnabled(false);
            jButtonRead.setEnabled(false);
            jButtonEdit.setEnabled(false);
            jButtonDelete.setEnabled(false);

        } else {
            jButtonNew.setEnabled(true);
            jButtonRead.setEnabled(true);
            jButtonEdit.setEnabled(true);
            jButtonDelete.setEnabled(true);
        }
    }

    private void controlarEstadoPanelIngresoDeDatos(boolean b) {
        java.awt.Component[] components = jPanelRecord.getComponents();
        for (java.awt.Component c : components) {
            if (c instanceof javax.swing.JFormattedTextField
                    || c.equals(jTextFieldDescription)
                    || c instanceof javax.swing.JCheckBox) {
                c.setEnabled(b);
            }
        }
    }

    private void mostrarDatosDeRegistroTabla() {
        //Rediseniando la Tabla
        javax.swing.table.DefaultTableModel dtm = (javax.swing.table.DefaultTableModel) jTableData.getModel();
        dtm.setDataVector(getTableData(), COLUMN_NAMES);
        jTableData.setModel(dtm);
    }

    private Object[][] getTableData() {
        vProducts = new java.util.ArrayList<>();

        ArchivoProducto.cargarRegistrosArray(vProducts, path);
        int size = vProducts.size();
        Object[][] data = new Object[size][6];

        for (int i = 0; i < size; i++) {
            Producto p = vProducts.get(i);
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
        controlarEstadoBotonesBarraHerramienta(true);
        controlarEstadoPanelIngresoDeDatos(true);
        jTextFieldDescription.requestFocus();
    }

    private void consultarRegistro() {
        if (ArchivoProducto.cantidadRegistros(path) == 0) {
            JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.productsNotFound"), getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            String productCode = JOptionPane.showInputDialog(this,
                    APPLICATION_RESOURCES.getString("messages.insertProductCode"), getTitle(), JOptionPane.PLAIN_MESSAGE);
            if (productCode != null) {
                Producto product = ArchivoProducto.consultarRegistro(productCode, path);

                if (product == null) {
                    JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.productNotFound"), getTitle(), JOptionPane.ERROR_MESSAGE);
                } else {
                    String detail = APPLICATION_RESOURCES.getString("label.product") + '\n'
                            + APPLICATION_RESOURCES.getString("form.code") + ' ' + product.getCodigo() + '\n'
                            + APPLICATION_RESOURCES.getString("form.description") + ' ' + product.getDescripcion() + '\n'
                            + APPLICATION_RESOURCES.getString("form.quantity") + ' ' + product.getTotal() + '\n'
                            + APPLICATION_RESOURCES.getString("form.price") + ' ' + product.getPrecio() + '\n'
                            + APPLICATION_RESOURCES.getString("form.exonerated") + ' ' + convert(product.isExonerado()) + '\n'
                            + APPLICATION_RESOURCES.getString("form.visible") + ' ' + convert(product.isVisible());
                    JOptionPane.showMessageDialog(this, detail, getTitle(), JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private String convert(boolean b) {
        return b
                ? APPLICATION_RESOURCES.getString("label.true")
                : APPLICATION_RESOURCES.getString("label.false");
    }

    private boolean validarFormulario() {
        if (jTextFieldDescription.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("validation.form.wrongDescription"), getTitle(), JOptionPane.ERROR_MESSAGE);
            jTextFieldDescription.requestFocus();
            return false;

        } else if (Base.convertirCadenaReal(jFormattedTextFieldQuantity.getText().trim()) == 0) {
            JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("validation.form.wrongQuantity"), getTitle(), JOptionPane.ERROR_MESSAGE);
            jFormattedTextFieldQuantity.requestFocus();
            return false;

        } else if (Base.convertirCadenaReal(jFormattedTextFieldPrice.getText().trim()) == 0) {
            JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("validation.form.wrongPrice"), getTitle(), JOptionPane.ERROR_MESSAGE);
            jFormattedTextFieldPrice.requestFocus();
            return false;

        } else {
            return true;
        }
    }

    private boolean grabarRegistroArchivo() {
        Producto producto = new Producto(
                ArchivoProducto.obtenerNumeroSecuencia(),
                jTextFieldDescription.getText().trim(),
                Base.convertirCadenaEntero(jFormattedTextFieldQuantity.getText().trim()),
                Base.convertirCadenaReal(jFormattedTextFieldPrice.getText().trim()),
                jCheckBoxExonerated.isSelected(),
                jCheckBoxVisible.isSelected());

        boolean result;
        if (ArchivoProducto.cantidadRegistros(path) == 0) {
            result = ArchivoProducto.crearArchivo(producto, path);
        } else {
            result = ArchivoProducto.adicionarRegistro(producto, path);
        }
        jTextFieldCode.setText(producto.getCodigo());
        return result;
    }

    private void postGrabarRegistro() {
        mostrarDatosDeRegistroTabla();
        controlarEstadoBotonesBarraHerramienta(false);
        controlarEstadoPanelIngresoDeDatos(false);
        JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.successful.savedRecord"),
                getTitle(), JOptionPane.INFORMATION_MESSAGE);
        currentAction = ActionType.ON_USE;
    }

    private boolean editarRegistroArchivo() {
        Producto producto = new Producto(
                jTextFieldCode.getText(),
                jTextFieldDescription.getText().trim(),
                Base.convertirCadenaEntero(jFormattedTextFieldQuantity.getText().trim()),
                Base.convertirCadenaReal(jFormattedTextFieldPrice.getText().trim()),
                jCheckBoxExonerated.isSelected(),
                jCheckBoxVisible.isSelected());

        boolean result = ArchivoProducto.modificarRegistro(producto, path);
        jTextFieldCode.setText(producto.getCodigo());
        return result;
    }

    private void postEditarRegistro() {
        mostrarDatosDeRegistroTabla();
        controlarEstadoBotonesBarraHerramienta(false);
        controlarEstadoPanelIngresoDeDatos(false);
        limpiarRegistro();
        JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.successful.updatedRecord"),
                getTitle(), JOptionPane.INFORMATION_MESSAGE);
        currentAction = ActionType.ON_USE;
    }

    private void grabarRegistro() {
        if (validarFormulario()) {
            if (currentAction == ActionType.NEW && grabarRegistroArchivo()) {
                postGrabarRegistro();

            } else if (currentAction == ActionType.EDIT && editarRegistroArchivo()) {
                postEditarRegistro();

            }
        }
    }

    private void editarRegistro() {
        if (ArchivoProducto.cantidadRegistros(path) == 0) {
            JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.productsNotFound"), getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            currentAction = ActionType.EDIT;

            int selectedRow = jTableData.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.selectProduct"), getTitle(), JOptionPane.WARNING_MESSAGE);
            } else {
                Producto prod = vProducts.get(selectedRow);

                controlarEstadoBotonesBarraHerramienta(true);
                controlarEstadoPanelIngresoDeDatos(true);

                String[] price = String.valueOf(prod.getPrecio()).split("\\.");
                String real = Base.completarIzquierda(price[0], 2, "0");
                String decimal = Base.completarDerecha(price[1], 2, "0");

                jTextFieldCode.setText(prod.getCodigo());
                jTextFieldDescription.setText(prod.getDescripcion());
                jFormattedTextFieldQuantity.setText(Base.completarIzquierda(String.valueOf(prod.getTotal()), 5, "0"));
                jFormattedTextFieldPrice.setText(real.concat(".").concat(decimal));
                jCheckBoxExonerated.setSelected(prod.isExonerado());
                jCheckBoxVisible.setSelected(prod.isVisible());

                jTextFieldDescription.requestFocus();
            }
        }
    }

    private void anularRegistro() {
        if (ArchivoProducto.cantidadRegistros(path) == 0) {
            JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.productsNotFound"), getTitle(), JOptionPane.WARNING_MESSAGE);
        } else {
            int selectedRow = jTableData.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.selectProduct"), getTitle(), JOptionPane.WARNING_MESSAGE);
            } else {
                if (JOptionPane.showConfirmDialog(this, APPLICATION_RESOURCES.getString("messages.deleteProduct"),
                        getTitle(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                    String code = vProducts.get(selectedRow).getCodigo();
                    if (ArchivoProducto.anularRegistro(code, path)) {
                        mostrarDatosDeRegistroTabla();
                        JOptionPane.showMessageDialog(this, APPLICATION_RESOURCES.getString("messages.successful.deletedRecord"), getTitle(), JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }

    private void salirRegistro() {
        if (JOptionPane.showConfirmDialog(this, APPLICATION_RESOURCES.getString("messages.exitApplication"),
                getTitle(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JToolBar jToolBarRecord = new javax.swing.JToolBar();
        jButtonNew = new javax.swing.JButton();
        jButtonRead = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonInfo = new javax.swing.JButton();
        jButtonExit = new javax.swing.JButton();
        javax.swing.JPanel jPanelHeader = new javax.swing.JPanel();
        jPanelRecord = new javax.swing.JPanel();
        javax.swing.JLabel jLabelCode = new javax.swing.JLabel();
        javax.swing.JLabel jLabelDescription = new javax.swing.JLabel();
        javax.swing.JLabel jLabelQuantity = new javax.swing.JLabel();
        javax.swing.JLabel jLabelPrice = new javax.swing.JLabel();
        jTextFieldCode = new javax.swing.JTextField();
        jTextFieldDescription = new javax.swing.JTextField();
        jFormattedTextFieldQuantity = new javax.swing.JFormattedTextField();
        jFormattedTextFieldPrice = new javax.swing.JFormattedTextField();
        jCheckBoxExonerated = new javax.swing.JCheckBox();
        jCheckBoxVisible = new javax.swing.JCheckBox();
        javax.swing.JPanel jPanelDetail = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPaneData = new javax.swing.JScrollPane();
        jTableData = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("view/Bundle"); // NOI18N
        setTitle(bundle.getString("title.main")); // NOI18N
        setMinimumSize(new java.awt.Dimension(850, 320));
        setName("mainFrame"); // NOI18N

        jToolBarRecord.setFloatable(false);

        jButtonNew.setBackground(new java.awt.Color(255, 255, 255));
        jButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.gif")));
        jButtonNew.setMnemonic('N');
        jButtonNew.setToolTipText(bundle.getString("label.toolTipText.new")); // NOI18N
        jButtonNew.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonNew.setFocusable(false);
        jButtonNew.setHideActionText(true);
        jButtonNew.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonNew.setName("buttonNew"); // NOI18N
        jButtonNew.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonNew.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonNew.addActionListener(this::formActionPerformed);
        jToolBarRecord.add(jButtonNew);

        jButtonRead.setBackground(new java.awt.Color(255, 255, 255));
        jButtonRead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/consultar.gif")));
        jButtonRead.setMnemonic('C');
        jButtonRead.setToolTipText(bundle.getString("label.toolTipText.read")); // NOI18N
        jButtonRead.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonRead.setFocusable(false);
        jButtonRead.setHideActionText(true);
        jButtonRead.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonRead.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonRead.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonRead.addActionListener(this::formActionPerformed);
        jToolBarRecord.add(jButtonRead);

        jButtonSave.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/grabar.gif")));
        jButtonSave.setMnemonic('G');
        jButtonSave.setToolTipText(bundle.getString("label.toolTipText.save")); // NOI18N
        jButtonSave.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonSave.setEnabled(false);
        jButtonSave.setFocusable(false);
        jButtonSave.setHideActionText(true);
        jButtonSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSave.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSave.addActionListener(this::formActionPerformed);
        jToolBarRecord.add(jButtonSave);

        jButtonEdit.setBackground(new java.awt.Color(255, 255, 255));
        jButtonEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/edit.gif")));
        jButtonEdit.setMnemonic('E');
        jButtonEdit.setToolTipText(bundle.getString("label.toolTipText.edit")); // NOI18N
        jButtonEdit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonEdit.setFocusable(false);
        jButtonEdit.setHideActionText(true);
        jButtonEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonEdit.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonEdit.addActionListener(this::formActionPerformed);
        jToolBarRecord.add(jButtonEdit);

        jButtonDelete.setBackground(new java.awt.Color(255, 255, 255));
        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/anular.gif")));
        jButtonDelete.setMnemonic('A');
        jButtonDelete.setToolTipText(bundle.getString("label.toolTipText.delete")); // NOI18N
        jButtonDelete.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonDelete.setFocusable(false);
        jButtonDelete.setHideActionText(true);
        jButtonDelete.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonDelete.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonDelete.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonDelete.addActionListener(this::formActionPerformed);
        jToolBarRecord.add(jButtonDelete);

        jButtonInfo.setBackground(new java.awt.Color(255, 255, 255));
        jButtonInfo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/info.gif")));
        jButtonInfo.setMnemonic('I');
        jButtonInfo.setToolTipText(bundle.getString("label.toolTipText.info")); // NOI18N
        jButtonInfo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonInfo.setFocusable(false);
        jButtonInfo.setHideActionText(true);
        jButtonInfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonInfo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonInfo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonInfo.addActionListener(this::formActionPerformed);
        jToolBarRecord.add(jButtonInfo);

        jButtonExit.setBackground(new java.awt.Color(255, 255, 255));
        jButtonExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.gif")));
        jButtonExit.setMnemonic('S');
        jButtonExit.setToolTipText(bundle.getString("label.toolTipText.exit")); // NOI18N
        jButtonExit.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonExit.setFocusable(false);
        jButtonExit.setHideActionText(true);
        jButtonExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonExit.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jButtonExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonExit.addActionListener(this::formActionPerformed);
        jToolBarRecord.add(jButtonExit);

        jPanelHeader.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHeader.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanelRecord.setBackground(new java.awt.Color(255, 255, 255));
        jPanelRecord.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("label.product"))); // NOI18N

        jLabelCode.setText(bundle.getString("form.code")); // NOI18N

        jLabelDescription.setText(bundle.getString("form.description")); // NOI18N

        jLabelQuantity.setText(bundle.getString("form.quantity")); // NOI18N

        jLabelPrice.setText(bundle.getString("form.price")); // NOI18N

        jTextFieldCode.setEditable(false);
        jTextFieldCode.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 11)); // NOI18N
        jTextFieldCode.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        jTextFieldDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jFormattedTextFieldQuantity.setFormatterFactory(Base.creaFormatoControl(5, 0, '0'));
        jFormattedTextFieldQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jFormattedTextFieldQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jFormattedTextFieldPrice.setFormatterFactory(Base.creaFormatoControl(2, 2, '0'));
        jFormattedTextFieldPrice.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jFormattedTextFieldPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jCheckBoxExonerated.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBoxExonerated.setSelected(true);
        jCheckBoxExonerated.setText(bundle.getString("form.exonerated")); // NOI18N
        jCheckBoxExonerated.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxExonerated.setFocusable(false);
        jCheckBoxExonerated.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBoxExonerated.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jCheckBoxVisible.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBoxVisible.setSelected(true);
        jCheckBoxVisible.setText(bundle.getString("form.visible")); // NOI18N
        jCheckBoxVisible.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBoxVisible.setFocusable(false);
        jCheckBoxVisible.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jCheckBoxVisible.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanelRecordLayout = new javax.swing.GroupLayout(jPanelRecord);
        jPanelRecord.setLayout(jPanelRecordLayout);
        jPanelRecordLayout.setHorizontalGroup(
                jPanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelRecordLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(jPanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabelCode, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                                        .addComponent(jLabelQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextFieldCode)
                                        .addGroup(jPanelRecordLayout.createSequentialGroup()
                                                .addComponent(jFormattedTextFieldQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                                .addGap(1, 1, 1)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabelPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanelRecordLayout.createSequentialGroup()
                                                .addComponent(jFormattedTextFieldPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                                .addGap(53, 53, 53)
                                                .addComponent(jCheckBoxExonerated)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jCheckBoxVisible, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jTextFieldDescription))
                                .addContainerGap())
        );
        jPanelRecordLayout.setVerticalGroup(
                jPanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelRecordLayout.createSequentialGroup()
                                .addGroup(jPanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabelCode)
                                        .addComponent(jTextFieldCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelDescription)
                                        .addComponent(jTextFieldDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelRecordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jFormattedTextFieldPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jCheckBoxVisible)
                                        .addComponent(jLabelQuantity)
                                        .addComponent(jFormattedTextFieldQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabelPrice)
                                        .addComponent(jCheckBoxExonerated))
                                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTextFieldDescription.requestFocus();

        javax.swing.GroupLayout jPanelHeaderLayout = new javax.swing.GroupLayout(jPanelHeader);
        jPanelHeader.setLayout(jPanelHeaderLayout);
        jPanelHeaderLayout.setHorizontalGroup(
                jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelHeaderLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanelRecord, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanelHeaderLayout.setVerticalGroup(
                jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelHeaderLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanelRecord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanelDetail.setBackground(new java.awt.Color(255, 255, 255));
        jPanelDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPaneData.setBackground(new java.awt.Color(255, 255, 255));

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

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTableData.setModel(defaultTableModel);
        jTableData.getTableHeader().setReorderingAllowed(false);
        jTableData.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        jScrollPaneData.setViewportView(jTableData);

        javax.swing.GroupLayout jPanelDetailLayout = new javax.swing.GroupLayout(jPanelDetail);
        jPanelDetail.setLayout(jPanelDetailLayout);
        jPanelDetailLayout.setHorizontalGroup(
                jPanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelDetailLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 822, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanelDetailLayout.setVerticalGroup(
                jPanelDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelDetailLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPaneData, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jToolBarRecord, javax.swing.GroupLayout.DEFAULT_SIZE, 850, Short.MAX_VALUE)
                        .addComponent(jPanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jToolBarRecord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanelDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        Base.centrarVentana(this);
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        LOG.trace(evt.paramString());
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            if (evt.getSource().equals(jTextFieldDescription)) {
                jFormattedTextFieldQuantity.requestFocus();
            } else if (evt.getSource().equals(jFormattedTextFieldQuantity)) {
                jFormattedTextFieldPrice.requestFocus();
            } else if (evt.getSource().equals(jFormattedTextFieldPrice)
                    && jButtonSave.isEnabled()) {
                grabarRegistro();
            }
        }
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE
                && currentAction != ActionType.ON_USE) {

            LOG.debug("Cancelling action over product");

            controlarEstadoBotonesBarraHerramienta(false);
            controlarEstadoPanelIngresoDeDatos(false);
            limpiarRegistro();

            currentAction = ActionType.ON_USE;
        }
    }//GEN-LAST:event_formKeyPressed

    private void formActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formActionPerformed
        LOG.debug("Current action: {}", currentAction);
        if (evt.getSource().equals(jButtonNew)) {
            LOG.info("Nuevo registro");
            nuevoRegistro();
        }
        if (evt.getSource().equals(jButtonRead)) {
            LOG.info("Consultar registro");
            consultarRegistro();
        }
        if (evt.getSource().equals(jButtonSave)) {
            LOG.info("Grabar registro");
            grabarRegistro();
        }
        if (evt.getSource().equals(jButtonEdit)) {
            LOG.info("Editar registro");
            editarRegistro();
        }
        if (evt.getSource().equals(jButtonDelete)) {
            LOG.info("Anular registro");
            anularRegistro();
        }
        if (evt.getSource().equals(jButtonInfo)) {
            LOG.info("Mostrar detalle de la PC");
            JOptionPane.showMessageDialog(this,
                    String.format("%s@%s\n%s %s", Base.getNombreMaquina(), Base.getDireccionIp(), Base.getFecha(), Base.getHoraMinAmPm2()),
                    getTitle(), JOptionPane.PLAIN_MESSAGE);
        }
        if (evt.getSource().equals(jButtonExit)) {
            LOG.info("Salir de la aplicacion");
            salirRegistro();
        }
    }//GEN-LAST:event_formActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonExit;
    private javax.swing.JButton jButtonInfo;
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonRead;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxExonerated;
    private javax.swing.JCheckBox jCheckBoxVisible;
    private javax.swing.JFormattedTextField jFormattedTextFieldPrice;
    private javax.swing.JFormattedTextField jFormattedTextFieldQuantity;
    private javax.swing.JPanel jPanelRecord;
    private javax.swing.JTable jTableData;
    private javax.swing.JTextField jTextFieldCode;
    private javax.swing.JTextField jTextFieldDescription;
    // End of variables declaration//GEN-END:variables
}

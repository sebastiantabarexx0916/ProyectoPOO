package tiendita;

import tiendita.controlador.ControladorHabitacion;
import tiendita.controlador.ControladorHuesped;
import tiendita.controlador.ControladorReserva;
import tiendita.modelo.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Vista principal del Sistema de Gestion Hotelera.
 * Reutiliza la estructura de la tienda OXXO con JTabbedPane de 3 pestanas.
 * Arquitectura MVC:
 *   - Modelo:      tiendita.modelo.*
 *   - Vista:       esta clase (tienditaOxxo)
 *   - Controlador: tiendita.controlador.*
 */
public class tienditaOxxo extends javax.swing.JFrame {

    // ── Controladores (Capa Controlador) ─────────────────────────────────────
    private ControladorHabitacion ctrlHab   = new ControladorHabitacion();
    private ControladorHuesped    ctrlHues  = new ControladorHuesped();
    private ControladorReserva    ctrlRes;

    // ── Modelos de tabla ──────────────────────────────────────────────────────
    private DefaultTableModel modeloHab   = new DefaultTableModel();
    private DefaultTableModel modeloHues  = new DefaultTableModel();
    private DefaultTableModel modeloRes   = new DefaultTableModel();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // =========================================================================
    public tienditaOxxo() {
        ctrlRes = new ControladorReserva(ctrlHab, ctrlHues);
        initComponents();
        configurarVentana();
        configurarTablas();
        actualizarTodo();
    }

    // ── Configuracion inicial ─────────────────────────────────────────────────
    private void configurarVentana() {
        this.setTitle("Hotel Tiendita OXXO");
        try {
            Image icono = Toolkit.getDefaultToolkit()
                .getImage(getClass().getResource("/tiendita/oxxo.png"));
            this.setIconImage(icono);
            lblLogo.setIcon(new ImageIcon(
                icono.getScaledInstance(lblLogo.getWidth(), lblLogo.getHeight(), Image.SCALE_SMOOTH)));
        } catch (Exception ignored) {}
        this.setLocationRelativeTo(null);
        this.setSize(780, 680);
    }

    private void configurarTablas() {
        // Habitaciones
        for (String col : ctrlHab.getColumnas()) modeloHab.addColumn(col);
        tblHabitaciones.setModel(modeloHab);
        tblHabitaciones.setRowHeight(22);
        tblHabitaciones.getSelectionModel().addListSelectionListener(e -> cargarHabitacionSeleccionada());

        // Huespedes
        for (String col : ctrlHues.getColumnas()) modeloHues.addColumn(col);
        tblHuespedes.setModel(modeloHues);
        tblHuespedes.setRowHeight(20);
        tblHuespedes.getSelectionModel().addListSelectionListener(e -> cargarHuespedSeleccionado());

        // Reservas
        for (String col : ctrlRes.getColumnas()) modeloRes.addColumn(col);
        tblReservas.setModel(modeloRes);
        tblReservas.setRowHeight(22);
        tblReservas.getSelectionModel().addListSelectionListener(e -> cargarReservaSeleccionada());
    }

    private void actualizarTodo() {
        actualizarTablaHab();
        actualizarTablaHues();
        actualizarTablaRes();
        actualizarComboHabitaciones();
    }

    // ── Actualizar tablas ─────────────────────────────────────────────────────
    private void actualizarTablaHab() {
        modeloHab.setDataVector(ctrlHab.getMatriz(), ctrlHab.getColumnas());
    }
    private void actualizarTablaHues() {
        modeloHues.setDataVector(ctrlHues.getMatriz(), ctrlHues.getColumnas());
    }
    private void actualizarTablaRes() {
        modeloRes.setDataVector(ctrlRes.getMatriz(), ctrlRes.getColumnas());
    }

    private void actualizarComboHabitaciones() {
        cboTipoHab.setModel(new DefaultComboBoxModel<>(new String[]{"Simple", "Doble", "Suite"}));
    }

    // ── Seleccion en tablas ───────────────────────────────────────────────────
    private void cargarHabitacionSeleccionada() {
        int f = tblHabitaciones.getSelectedRow();
        if (f < 0) return;
        txtNumHab.setText(modeloHab.getValueAt(f, 0).toString());
        txtPisoHab.setText(modeloHab.getValueAt(f, 2).toString());
        txtDescHab.setText(modeloHab.getValueAt(f, 4).toString());
        String tipo = modeloHab.getValueAt(f, 1).toString();
        cboTipoHab.setSelectedItem(tipo.contains("Suite") ? "Suite" : tipo);
    }

    private void cargarHuespedSeleccionado() {
        int f = tblHuespedes.getSelectedRow();
        if (f < 0) return;
        txtIdHues.setText(modeloHues.getValueAt(f, 0).toString());
        txtNombreHues.setText(modeloHues.getValueAt(f, 1).toString());
        txtApellidoHues.setText(modeloHues.getValueAt(f, 2).toString());
        txtTelHues.setText(modeloHues.getValueAt(f, 3).toString());
        txtEmailHues.setText(modeloHues.getValueAt(f, 4).toString());
        txtNacHues.setText(modeloHues.getValueAt(f, 5).toString());
    }

    private void cargarReservaSeleccionada() {
        int f = tblReservas.getSelectedRow();
        if (f < 0) return;
        txtIdRes.setText(modeloRes.getValueAt(f, 0).toString());
    }

    // =========================================================================
    // ACCIONES - HABITACIONES
    // =========================================================================
    private void registrarHabitacion() {
        try {
            if (txtNumHab.getText().trim().isEmpty() || txtPisoHab.getText().trim().isEmpty())
                throw new Exception("Numero de habitacion y piso son obligatorios.");
            int num  = Integer.parseInt(txtNumHab.getText().trim());
            int piso = Integer.parseInt(txtPisoHab.getText().trim());
            String desc = txtDescHab.getText().trim();
            String tipo = (String) cboTipoHab.getSelectedItem();
            Habitacion h;
            switch (tipo) {
                case "Doble": h = new HabitacionDoble(num, piso, desc); break;
                case "Suite": h = new Suite(num, piso, desc, chkDesayuno.isSelected()); break;
                default:      h = new HabitacionSimple(num, piso, desc);
            }
            ctrlHab.agregar(h);
            actualizarTablaHab();
            limpiarHab();
            JOptionPane.showMessageDialog(this, "Habitacion " + num + " registrada.",
                "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Numero y piso deben ser valores numericos.",
                "Dato invalido", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarDescHab() {
        try {
            if (txtNumHab.getText().trim().isEmpty())
                throw new Exception("Seleccione una habitacion de la tabla.");
            int num = Integer.parseInt(txtNumHab.getText().trim());
            ctrlHab.actualizarDescripcion(num, txtDescHab.getText().trim());
            actualizarTablaHab();
            JOptionPane.showMessageDialog(this, "Descripcion actualizada.", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarHabitacion() {
        try {
            if (txtNumHab.getText().trim().isEmpty())
                throw new Exception("Seleccione una habitacion de la tabla.");
            int num = Integer.parseInt(txtNumHab.getText().trim());
            int conf = JOptionPane.showConfirmDialog(this,
                "Confirma eliminar la habitacion " + num + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                ctrlHab.eliminar(num);
                actualizarTablaHab();
                limpiarHab();
                JOptionPane.showMessageDialog(this, "Habitacion eliminada.", "OK", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarHab() {
        txtNumHab.setText(""); txtPisoHab.setText(""); txtDescHab.setText("");
        cboTipoHab.setSelectedIndex(0); chkDesayuno.setSelected(false);
        tblHabitaciones.clearSelection();
    }

    // =========================================================================
    // ACCIONES - HUESPEDES
    // =========================================================================
    private void registrarHuesped() {
        try {
            Huesped h = new Huesped(
                txtNombreHues.getText().trim(), txtApellidoHues.getText().trim(),
                txtIdHues.getText().trim(), txtTelHues.getText().trim(),
                txtEmailHues.getText().trim(), txtNacHues.getText().trim());
            ctrlHues.registrar(h);
            actualizarTablaHues();
            limpiarHues();
            JOptionPane.showMessageDialog(this, "Huesped registrado.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarHuesped() {
        try {
            if (txtIdHues.getText().trim().isEmpty())
                throw new Exception("Seleccione un huesped de la tabla.");
            ctrlHues.actualizar(txtIdHues.getText().trim(), txtNombreHues.getText().trim(),
                txtApellidoHues.getText().trim(), txtTelHues.getText().trim(),
                txtEmailHues.getText().trim(), txtNacHues.getText().trim());
            actualizarTablaHues();
            JOptionPane.showMessageDialog(this, "Huesped actualizado.", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarHuesped() {
        try {
            if (txtIdHues.getText().trim().isEmpty())
                throw new Exception("Seleccione un huesped de la tabla.");
            int conf = JOptionPane.showConfirmDialog(this,
                "Confirma eliminar al huesped " + txtNombreHues.getText() + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                ctrlHues.eliminar(txtIdHues.getText().trim());
                actualizarTablaHues();
                limpiarHues();
                JOptionPane.showMessageDialog(this, "Huesped eliminado.", "OK", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarHuesped() {
        String texto = txtBuscarHues.getText().trim();
        if (texto.isEmpty()) { actualizarTablaHues(); return; }
        ArrayList<Huesped> r = ctrlHues.buscarPorNombre(texto);
        if (r.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron huespedes con: " + texto,
                "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaHues();
        } else {
            Object[][] d = new Object[r.size()][6];
            for (int i = 0; i < r.size(); i++) {
                Huesped h = r.get(i);
                d[i] = new Object[]{h.getIdentificacion(), h.getNombre(), h.getApellido(),
                                    h.getTelefono(), h.getEmail(), h.getNacionalidad()};
            }
            modeloHues.setDataVector(d, ctrlHues.getColumnas());
        }
    }

    private void limpiarHues() {
        txtIdHues.setText(""); txtNombreHues.setText(""); txtApellidoHues.setText("");
        txtTelHues.setText(""); txtEmailHues.setText(""); txtNacHues.setText("");
        txtBuscarHues.setText(""); tblHuespedes.clearSelection();
    }

    // =========================================================================
    // ACCIONES - RESERVAS
    // =========================================================================
    private void crearReserva() {
        try {
            String idH = txtIdHuesRes.getText().trim();
            String numS = txtNumHabRes.getText().trim();
            String ingS = txtIngreso.getText().trim();
            String salS = txtSalida.getText().trim();
            if (idH.isEmpty() || numS.isEmpty() || ingS.isEmpty() || salS.isEmpty())
                throw new Exception("Todos los campos son obligatorios.");
            int num = Integer.parseInt(numS);
            LocalDate ingreso, salida;
            try {
                ingreso = LocalDate.parse(ingS, FMT);
                salida  = LocalDate.parse(salS, FMT);
            } catch (DateTimeParseException ex) {
                throw new Exception("Formato de fecha invalido. Use: yyyy-MM-dd");
            }
            Reserva res = ctrlRes.crear(idH, num, ingreso, salida);
            actualizarTablaRes();
            actualizarTablaHab(); // actualiza disponibilidad
            limpiarRes();
            JOptionPane.showMessageDialog(this, res.generarComprobante(),
                "Reserva N " + res.getId() + " creada", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El numero de habitacion debe ser numerico.",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarReserva() {
        try {
            if (txtIdRes.getText().trim().isEmpty())
                throw new Exception("Seleccione o ingrese el N de reserva.");
            int id = Integer.parseInt(txtIdRes.getText().trim());
            int conf = JOptionPane.showConfirmDialog(this,
                "Confirma cancelar la reserva N " + id + "?\nLa habitacion quedara disponible.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                ctrlRes.cancelar(id);
                actualizarTablaRes();
                actualizarTablaHab();
                JOptionPane.showMessageDialog(this, "Reserva N " + id + " cancelada. Habitacion liberada.",
                    "OK", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El N de reserva debe ser numerico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verComprobante() {
        try {
            if (txtIdRes.getText().trim().isEmpty())
                throw new Exception("Seleccione o ingrese el N de reserva.");
            int id = Integer.parseInt(txtIdRes.getText().trim());
            Reserva r = ctrlRes.buscar(id);
            if (r == null) throw new Exception("Reserva N " + id + " no encontrada.");
            JOptionPane.showMessageDialog(this, r.generarComprobante(),
                "Comprobante N " + id, JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El N de reserva debe ser numerico.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarRes() {
        txtIdHuesRes.setText(""); txtNumHabRes.setText("");
        txtIngreso.setText(""); txtSalida.setText(""); txtIdRes.setText("");
        tblReservas.clearSelection();
    }

    // =========================================================================
    // initComponents — forma manual (sin .form) para maxima compatibilidad
    // =========================================================================
    @SuppressWarnings("unchecked")
    private void initComponents() {
        // ── Logo ──────────────────────────────────────────────────────────────
        lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

        // ── Titulo ───────────────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("Sistema de Gestion Hotelera");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(30, 90, 160));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // ── TabbedPane ────────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("Habitaciones", buildPanelHab());
        tabs.addTab("Huespedes",    buildPanelHues());
        tabs.addTab("Reservas",     buildPanelRes());
        tabs.addChangeListener(e -> actualizarTablaRes());

        // ── Barra de estado ───────────────────────────────────────────────────
        JPanel barraEstado = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        barraEstado.setBackground(new Color(30, 90, 160));
        JLabel lblEstado = new JLabel("  Hotel Caldas | Universidad de Caldas | POO 2026");
        lblEstado.setForeground(Color.WHITE);
        barraEstado.add(lblEstado);

        // ── Layout principal ──────────────────────────────────────────────────
        JPanel top = new JPanel(new java.awt.BorderLayout());
        top.add(lblLogo, java.awt.BorderLayout.WEST);
        top.add(lblTitulo, java.awt.BorderLayout.CENTER);

        setLayout(new java.awt.BorderLayout(5, 5));
        add(top,          java.awt.BorderLayout.NORTH);
        add(tabs,         java.awt.BorderLayout.CENTER);
        add(barraEstado,  java.awt.BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    // ── Panel Habitaciones ────────────────────────────────────────────────────
    private javax.swing.JPanel buildPanelHab() {
        txtNumHab   = new JTextField(6);
        txtPisoHab  = new JTextField(4);
        txtDescHab  = new JTextField(20);
        cboTipoHab  = new JComboBox<>(new String[]{"Simple", "Doble", "Suite"});
        chkDesayuno = new JCheckBox("Inc. Desayuno");
        cboTipoHab.addActionListener(e ->
            chkDesayuno.setEnabled("Suite".equals(cboTipoHab.getSelectedItem())));
        chkDesayuno.setEnabled(false);

        JButton btnReg  = boton("Registrar",  new Color(40, 167, 69));
        JButton btnAct  = boton("Actualizar", new Color(0, 123, 255));
        JButton btnElim = boton("Eliminar",   new Color(220, 53, 69));
        JButton btnLimp = boton("Limpiar",    new Color(108, 117, 125));
        btnReg.addActionListener(e  -> registrarHabitacion());
        btnAct.addActionListener(e  -> actualizarDescHab());
        btnElim.addActionListener(e -> eliminarHabitacion());
        btnLimp.addActionListener(e -> limpiarHab());

        JPanel form = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Datos de la habitacion"));
        form.add(new JLabel("N Hab:")); form.add(txtNumHab);
        form.add(new JLabel("Piso:")); form.add(txtPisoHab);
        form.add(new JLabel("Tipo:")); form.add(cboTipoHab);
        form.add(chkDesayuno);
        form.add(new JLabel("Descripcion:")); form.add(txtDescHab);
        form.add(btnReg); form.add(btnAct); form.add(btnElim); form.add(btnLimp);

        tblHabitaciones = new JTable();
        JPanel p = new JPanel(new java.awt.BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        p.add(form, java.awt.BorderLayout.NORTH);
        p.add(new JScrollPane(tblHabitaciones), java.awt.BorderLayout.CENTER);
        return p;
    }

    // ── Panel Huespedes ───────────────────────────────────────────────────────
    private javax.swing.JPanel buildPanelHues() {
        txtIdHues       = new JTextField(10);
        txtNombreHues   = new JTextField(10);
        txtApellidoHues = new JTextField(10);
        txtTelHues      = new JTextField(10);
        txtEmailHues    = new JTextField(14);
        txtNacHues      = new JTextField(10);
        txtBuscarHues   = new JTextField(14);

        JButton btnReg   = boton("Registrar",    new Color(40, 167, 69));
        JButton btnAct   = boton("Actualizar",   new Color(0, 123, 255));
        JButton btnElim  = boton("Eliminar",     new Color(220, 53, 69));
        JButton btnBusc  = boton("Buscar",       new Color(255, 153, 0));
        JButton btnTodos = boton("Mostrar todos",new Color(108, 117, 125));
        JButton btnLimp  = boton("Limpiar",      new Color(108, 117, 125));
        btnReg.addActionListener(e   -> registrarHuesped());
        btnAct.addActionListener(e   -> actualizarHuesped());
        btnElim.addActionListener(e  -> eliminarHuesped());
        btnBusc.addActionListener(e  -> buscarHuesped());
        btnTodos.addActionListener(e -> actualizarTablaHues());
        btnLimp.addActionListener(e  -> limpiarHues());

        JPanel form = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Datos del huesped"));
        form.add(new JLabel("Cedula:")); form.add(txtIdHues);
        form.add(new JLabel("Nombre:")); form.add(txtNombreHues);
        form.add(new JLabel("Apellido:")); form.add(txtApellidoHues);
        form.add(new JLabel("Telefono:")); form.add(txtTelHues);
        form.add(new JLabel("Email:")); form.add(txtEmailHues);
        form.add(new JLabel("Nacionalidad:")); form.add(txtNacHues);
        form.add(new JLabel("Buscar:")); form.add(txtBuscarHues);
        form.add(btnBusc); form.add(btnTodos);
        form.add(btnReg); form.add(btnAct); form.add(btnElim); form.add(btnLimp);

        tblHuespedes = new JTable();
        JPanel p = new JPanel(new java.awt.BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        p.add(form, java.awt.BorderLayout.NORTH);
        p.add(new JScrollPane(tblHuespedes), java.awt.BorderLayout.CENTER);
        return p;
    }

    // ── Panel Reservas ────────────────────────────────────────────────────────
    private javax.swing.JPanel buildPanelRes() {
        txtIdHuesRes = new JTextField(10);
        txtNumHabRes = new JTextField(6);
        txtIngreso   = new JTextField(10);
        txtSalida    = new JTextField(10);
        txtIdRes     = new JTextField(6);

        JButton btnCrear  = boton("Crear Reserva",    new Color(40, 167, 69));
        JButton btnCancel = boton("Cancelar Reserva", new Color(220, 53, 69));
        JButton btnComp   = boton("Ver Comprobante",  new Color(255, 153, 0));
        JButton btnLimp   = boton("Limpiar",          new Color(108, 117, 125));
        btnCrear.addActionListener(e  -> crearReserva());
        btnCancel.addActionListener(e -> cancelarReserva());
        btnComp.addActionListener(e   -> verComprobante());
        btnLimp.addActionListener(e   -> limpiarRes());

        JPanel form = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 6));
        form.setBorder(BorderFactory.createTitledBorder("Nueva reserva / Gestion"));
        form.add(new JLabel("ID Huesped:")); form.add(txtIdHuesRes);
        form.add(new JLabel("N Hab:"));      form.add(txtNumHabRes);
        form.add(new JLabel("Ingreso (yyyy-MM-dd):")); form.add(txtIngreso);
        form.add(new JLabel("Salida (yyyy-MM-dd):")); form.add(txtSalida);
        form.add(new JLabel("N Reserva:")); form.add(txtIdRes);
        form.add(btnCrear); form.add(btnCancel); form.add(btnComp); form.add(btnLimp);

        tblReservas = new JTable();
        JPanel p = new JPanel(new java.awt.BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        p.add(form, java.awt.BorderLayout.NORTH);
        p.add(new JScrollPane(tblReservas), java.awt.BorderLayout.CENTER);
        return p;
    }

    private JButton boton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }

    // =========================================================================
    // main
    // =========================================================================
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}
        java.awt.EventQueue.invokeLater(() -> new tienditaOxxo().setVisible(true));
    }

    // ── Variables de la Vista ─────────────────────────────────────────────────
    private JLabel          lblLogo;
    // Habitaciones
    private JTextField      txtNumHab, txtPisoHab, txtDescHab;
    private JComboBox<String> cboTipoHab;
    private JCheckBox       chkDesayuno;
    private JTable          tblHabitaciones;
    // Huespedes
    private JTextField      txtIdHues, txtNombreHues, txtApellidoHues,
                            txtTelHues, txtEmailHues, txtNacHues, txtBuscarHues;
    private JTable          tblHuespedes;
    // Reservas
    private JTextField      txtIdHuesRes, txtNumHabRes, txtIngreso, txtSalida, txtIdRes;
    private JTable          tblReservas;
}

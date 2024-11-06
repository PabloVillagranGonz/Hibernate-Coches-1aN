package com.example.practicajsonpeliculas.Controller;

import com.example.practicajsonpeliculas.DAO.MultasDao;
import com.example.practicajsonpeliculas.modelo.Coche;
import com.example.practicajsonpeliculas.modelo.Multas;
import com.example.practicajsonpeliculas.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SegundaParteMultasController implements Initializable {

    @FXML
    private Button btnIActualizar;

    @FXML
    private Button btnIBorrar;

    @FXML
    private Button btnInsertar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private TableView<Multas> idTablaMultas;

    @FXML
    private TableColumn<LocalDate, Multas> colFecha;

    @FXML
    private TableColumn<Integer, Multas> colID;

    @FXML
    private TableColumn<Integer, Multas> colPrecio;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtIdentificador;

    @FXML
    private TextField txtMatricula;

    @FXML
    private TextField txtPrecio;

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    MultasDao multasDao;

    public SegundaParteMultasController() {
        this.multasDao = new MultasDao();
    }

    private void limpiarCampos() {
        txtMatricula.clear();
        txtPrecio.clear();
        txtDate.setValue(null);
        txtIdentificador.clear();
    }

    @FXML
    void onClicActualizar(ActionEvent event) {
        Multas multaSeleccionada = idTablaMultas.getSelectionModel().getSelectedItem();

        if (multaSeleccionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione una multa para actualizar");
            alert.showAndWait();
        } else {
            multaSeleccionada.setFecha(txtDate.getValue());
            multaSeleccionada.setPrecio(Integer.parseInt(txtPrecio.getText()));
            multaSeleccionada.setId_multa(Integer.parseInt(txtIdentificador.getText()));

            try (Session session = sessionFactory.openSession()) {
                String matricula = multaSeleccionada.getCoche().getMatricula();
                multasDao.updateMulta(multaSeleccionada, session);
                cargarMultasPorCoche(matricula);
                limpiarCampos();
            } catch (Exception e) {
                System.out.println("Error al actualizar la multa: " + e.getMessage());
            }
        }
    }

    @FXML
    void onClicBorrar(ActionEvent event) {
        Multas multaSeleccionada = idTablaMultas.getSelectionModel().getSelectedItem();

        if (multaSeleccionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione una multa para eliminar");
            alert.showAndWait();
        } else {
                try (Session session = sessionFactory.openSession()) {
                    String matricula = multaSeleccionada.getCoche().getMatricula();
                    multasDao.eliminarMultas(multaSeleccionada.getId_multa(), session);
                    cargarMultasPorCoche(matricula);
                    limpiarCampos();
                } catch (Exception e) {
                    System.out.println("Error al eliminar la multa: " + e.getMessage());
                }
        }
    }

    @FXML
    void onClicInsertar(ActionEvent event) {
        Multas multaNueva = new Multas();

        multaNueva.setPrecio(Integer.parseInt(txtPrecio.getText()));
        multaNueva.setId_multa(Integer.parseInt(txtIdentificador.getText()));
        multaNueva.setFecha(txtDate.getValue());

        try (Session session = sessionFactory.openSession()){
            String matricula = txtMatricula.getText();

            Coche cocheAsociado = session.createQuery("FROM Coche WHERE matricula = :matricula", Coche.class)
                    .setParameter("matricula", matricula)
                    .uniqueResult();

            if (cocheAsociado != null) {
                multaNueva.setCoche(cocheAsociado);
                multasDao.guardarMulta(multaNueva, session);
                cargarMultasPorCoche(matricula);
                limpiarCampos();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Advertencia");
                alert.setHeaderText(null);
                alert.setContentText("No se encontró un coche con la matrícula proporcionada.");
                alert.showAndWait();
            }
        }  catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al guardar la multa.");
            alert.showAndWait();
        }
    }

    @FXML
    void onClicLimpiar(ActionEvent event) {
        limpiarCampos();
    }

    public void onClickedMouse(javafx.scene.input.MouseEvent mouseEvent) {
        Multas multas = idTablaMultas.getSelectionModel().getSelectedItem();
        if (multas != null) {
            Coche coche = multas.getCoche();
            if (coche != null) {
                txtMatricula.setText(coche.getMatricula());
            }
            txtPrecio.setText(String.valueOf(multas.getPrecio()));
            txtIdentificador.setText(String.valueOf(multas.getId_multa()));
            txtDate.setValue(multas.getFecha());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colID.setCellValueFactory(new PropertyValueFactory<>("id_multa"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
    }

    public void cargarMultasPorCoche(String matricula) {
        try(Session session = sessionFactory.openSession()) {
            List<Multas> listaMultas = session.createQuery("FROM Multas WHERE matricula = :matricula", Multas.class)
                    .setParameter("matricula", matricula)
                    .list();

            idTablaMultas.getItems().setAll(listaMultas);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al cargar las multas: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
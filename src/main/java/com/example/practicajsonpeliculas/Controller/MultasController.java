package com.example.practicajsonpeliculas.Controller;
import com.example.practicajsonpeliculas.App;
import com.example.practicajsonpeliculas.DAO.CochesDao;
import com.example.practicajsonpeliculas.modelo.Coche;

import com.example.practicajsonpeliculas.util.HibernateUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MultasController implements Initializable {

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnBorrar;

    @FXML
    private Button btnInsertar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnMultas;

    @FXML
    private TableView<Coche> idTablaCoches;

    @FXML
    private TableColumn<String, Coche> colMarca;

    @FXML
    private TableColumn<String, Coche> colMatricula;

    @FXML
    private TableColumn<String, Coche> colModelo;

    @FXML
    private TableColumn<String, Coche> colTipo;

    @FXML
    private ComboBox<String> fxTipo;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtMatricula;

    @FXML
    private TextField txtModelo;

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private CochesDao cochesDao;

    public MultasController() {
        this.cochesDao = new CochesDao();
    }

    private void limpiarCampos() {
        txtMarca.clear();
        txtModelo.clear();
        txtMatricula.clear();
        fxTipo.getSelectionModel().clearSelection();
    }

    @FXML
    void onClicActualizar(ActionEvent event) {
        Coche cocheSeleccionado = idTablaCoches.getSelectionModel().getSelectedItem();

        if (cocheSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un coche para modificar");
            alert.showAndWait();
        } else {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de Modificacion");
            confirmacion.setHeaderText("¿Estás seguro de que deseas modificar este coche?");
            confirmacion.setContentText("Coche: " + cocheSeleccionado.getMatricula() + " " + cocheSeleccionado.getModelo());

            cocheSeleccionado.setMatricula(txtMatricula.getText());
            cocheSeleccionado.setMarca(txtMarca.getText());
            cocheSeleccionado.setModelo(txtModelo.getText());
            cocheSeleccionado.setTipo(fxTipo.getValue());

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try (Session session = sessionFactory.openSession()) {
                    cochesDao.updateCoche(cocheSeleccionado, session);
                    cargarCoches();
                    limpiarCampos();
                }
            }
        }
    }

    @FXML
    void onClicBorrar(ActionEvent event) {
        Coche cocheSeleccionado = idTablaCoches.getSelectionModel().getSelectedItem();

        if (cocheSeleccionado == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un coche para eliminar");
            alert.showAndWait();
        } else {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de Eliminación");
            confirmacion.setHeaderText("¿Estás seguro de que deseas eliminar este coche?");
            confirmacion.setContentText("Coche: " + cocheSeleccionado.getMatricula() + " " + cocheSeleccionado.getModelo());

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try (Session session = sessionFactory.openSession()) {
                    cochesDao.eliminarCoche(cocheSeleccionado.getId(), session);
                    cargarCoches();
                    limpiarCampos();
                } catch (Exception e) {
                    System.out.println("Error al eliminar el coche: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    void onClicInsertar(ActionEvent event) {
        Coche cochenuevo = new Coche();
        cochenuevo.setMatricula(txtMatricula.getText());
        cochenuevo.setMarca(txtMarca.getText());
        cochenuevo.setModelo(txtModelo.getText());
        cochenuevo.setTipo(fxTipo.getValue());

        try (Session session = sessionFactory.openSession()) {
            cochesDao.guardarCoche(cochenuevo, session);
            cargarCoches();
            limpiarCampos();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al guardar el coche.");
            alert.showAndWait();
        }
    }

    @FXML
    void onClickLimpiar(ActionEvent event){
        limpiarCampos();
    }

    @FXML
    void onClickMultas(ActionEvent event) {
        Coche cocheSeleccionado = idTablaCoches.getSelectionModel().getSelectedItem();

        if (cocheSeleccionado != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("multas2.fxml"));
                Parent root = fxmlLoader.load();
                SegundaParteMultasController controller = fxmlLoader.getController();
                controller.cargarMultasPorCoche(cocheSeleccionado.getMatricula());

                Scene currentScene = idTablaCoches.getScene();
                currentScene.setRoot(root);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error al abrir la ventana de multas.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Selecciona un coche para ver sus multas.");
            alert.showAndWait();
        }
    }

    public void onClickedMouse(javafx.scene.input.MouseEvent mouseEvent) {
        Coche coche = idTablaCoches.getSelectionModel().getSelectedItem();
        if (coche != null) {
            txtMatricula.setText(coche.getMatricula());
            txtModelo.setText(coche.getModelo());
            txtMarca.setText(coche.getMarca());
            fxTipo.getSelectionModel().select(coche.getTipo());
        } else {
            System.out.println("No se ha seleccionado ningún coche.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fxTipo.getItems().addAll("Sedan", "SUV", "Camioneta","Deportivo");
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        cargarCoches();
    }

    private void cargarCoches() {
        try(Session session = sessionFactory.openSession()) {
            List<Coche> listaCoches = session.createQuery("from Coche", Coche.class).list();
            idTablaCoches.getItems().setAll(listaCoches);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al cargar los coches: " + e.getMessage());
            alert.showAndWait();
        }
    }
}
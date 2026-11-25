package cl.duoc.esports.controller;

import cl.duoc.esports.model.Casual;
import cl.duoc.esports.model.Competitivo;
import cl.duoc.esports.model.Jugador;
import cl.duoc.esports.repository.JugadorRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class JugadorController {
    public Label labelTitulo;
    public Label labelNickname;
    public Label labelVideojuego;
    public Label labelNivel;
    public Label labelTipoJugador;
    public Label labelCantVicPartJug;
    public TextField textFieldNickname;
    public TextField textFieldNivel;
    public TextField textFieldCantVicPartJug;
    public ComboBox<String> comboBoxVideojuego;
    public ComboBox<String> comboBoxTipoJugador;
    public Button buttonRegistrar;
    public Button buttonEliminar;
    public TableView<Jugador> tableViewJugadores;
    public TableColumn<Jugador,String> tableColumnNickname;
    public TableColumn<Jugador,String> tableColumnVideojuego;
    public TableColumn<Jugador,Integer> tableColumnNivel;
    public TableColumn<Jugador,Integer> tableColumnPuntos;
    public Label labelMensaje;
    public Button buttonBuscar;
    public Button buttonModificar;

    @FXML
    public void initialize(){
        cargarTabla();
        comboBoxVideojuego.getItems().addAll("Counter Strike", "Valorant", "Minecraft");
        comboBoxTipoJugador.getItems().addAll("Casual","Competitivo");

        comboBoxTipoJugador.setOnAction(evento -> {
            textFieldCantVicPartJug.setDisable(false);
            String tipoJugador = comboBoxTipoJugador.getValue();
            if("Casual".equals(tipoJugador)){
                labelCantVicPartJug.setText("Partidas jugadas");
                textFieldCantVicPartJug.setPromptText("ingrese partidas jugadas");
            }else{
                labelCantVicPartJug.setText("Cantidad de victorias");
                textFieldCantVicPartJug.setPromptText("ingrese cantidad de victorias");
            }
        });

        // Al seleccionar una fila de la tabla:
        tableViewJugadores.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nuevo) -> {
            if(nuevo != null){
                cargarFormulario(nuevo);
                estadoSeleccionado();
            }else{
                estadoInicial();
            }
        });
    }

    @FXML
    protected void registrarJugador() {
        try {
            if(comboBoxTipoJugador.getValue()==null){
                labelMensaje.setText("Debe seleccionar un tipo de jugador.");
                return;
            }
            if(comboBoxVideojuego.getValue()==null){
                labelMensaje.setText("Debe seleccionar un videojuego.");
                return;
            }
            Jugador j;
            String nickname = textFieldNickname.getText();
            String videojuego = comboBoxVideojuego.getValue();
            int nivel = Integer.parseInt(textFieldNivel.getText());
            int cantVicPartJug = Integer.parseInt(textFieldCantVicPartJug.getText());

            String tipo = comboBoxTipoJugador.getValue();
            if("Casual".equals(tipo)){
                j = new Casual(nickname,videojuego,nivel,cantVicPartJug);
            }else{
                j = new Competitivo(nickname,videojuego,nivel,cantVicPartJug);
            }
            boolean res = new JugadorRepository().agregarJugador(j);
            if(res){
                mostrarMensaje("Jugador registrado");
                cargarTabla();
                limpiar();
            }else{
                mostrarMensaje("Jugador no registrado");
            }
        }catch (NumberFormatException ex){
            labelMensaje.setText("Debe ingresar números para campos numéricos.");
        }catch (Exception ex){
            labelMensaje.setText(ex.getMessage());
        }
    }

    @FXML
    protected void eliminarJugador() {
        try {
            Jugador j = tableViewJugadores.getSelectionModel().getSelectedItem();
            if(j==null){
                labelMensaje.setText("Debe seleccionar un jugador de la tabla.");
                return;
            }else{
                boolean res = new JugadorRepository().eliminarJugador(j);
                if(res){
                    mostrarMensaje("Jugador eliminado");
                    cargarTabla();
                    limpiar();
                }else{
                    mostrarMensaje("Jugador no eliminado");
                }
            }
        }catch (Exception ex){
            labelMensaje.setText( ex.getMessage() );
        }
    }

    @FXML
    public void modificarJugador() {
        try {
            if (comboBoxTipoJugador.getValue() == null) {
                labelMensaje.setText("Debe seleccionar un tipo de jugador.");
                return;
            }
            if (comboBoxVideojuego.getValue() == null) {
                labelMensaje.setText("Debe seleccionar un videojuego.");
                return;
            }
            Jugador j = leerFormulario();
            boolean res = new JugadorRepository().modificarJugador(j);
            if (res) {
                mostrarMensaje("Jugador modificado");
                cargarTabla();
                limpiar();
            } else {
                mostrarMensaje("Jugador no modificado");
            }
        } catch (NumberFormatException ex) {
            labelMensaje.setText("Debe ingresar números para campos numéricos.");
        } catch (Exception ex) {
            labelMensaje.setText(ex.getMessage());
        }
    }

    @FXML
    public void buscarJugador(){
        try{
            String nickname = textFieldNickname.getText();
            if(nickname.isEmpty()){
                labelMensaje.setText("Ingrese el nickname a buscar.");
                return;
            }
            Jugador j = new JugadorRepository().verJugador(nickname);
            if(j == null){
                mostrarMensaje("Jugador no encontrado");
            }else{
                for(Jugador item : tableViewJugadores.getItems()){
                    if(item.getNickname().equals(j.getNickname())){
                        tableViewJugadores.getSelectionModel().select(item);
                    }
                }
            }
        }catch(Exception ex){
            labelMensaje.setText(ex.getMessage());
        }
    }

    //métodos auxiliares:
    private void limpiar(){
        textFieldNickname.clear();
        textFieldNivel.clear();
        comboBoxVideojuego.setValue(null);
        comboBoxTipoJugador.setValue(null);
        labelCantVicPartJug.setText("-");
        textFieldCantVicPartJug.clear();
        textFieldCantVicPartJug.setDisable(true);
        labelMensaje.setText("-");
        tableViewJugadores.getSelectionModel().clearSelection();
        estadoInicial();
    }

    @FXML
    private void cargarTabla(){
        try{
            tableColumnNickname.setCellValueFactory(new PropertyValueFactory<>("nickname"));
            tableColumnVideojuego.setCellValueFactory(new PropertyValueFactory<>("videojuego"));
            tableColumnNivel.setCellValueFactory(new PropertyValueFactory<>("nivel"));
            tableColumnPuntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));

            ArrayList<Jugador> jugadores = new JugadorRepository().verJugadores();
            ObservableList<Jugador> data = FXCollections.observableArrayList(jugadores);
            tableViewJugadores.setItems(data);
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
        estadoInicial();
    }

    private void mostrarMensaje(String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setContentText(mensaje);
        alerta.show();
    }

    private Jugador leerFormulario() {
        Jugador j;
        String nickname = textFieldNickname.getText();
        String videojuego = comboBoxVideojuego.getValue();
        int nivel = Integer.parseInt(textFieldNivel.getText());
        int cantVicPartJug = Integer.parseInt(textFieldCantVicPartJug.getText());
        String tipo = comboBoxTipoJugador.getValue();
        if ("Casual".equals(tipo)) {
            j = new Casual(nickname, videojuego, nivel, cantVicPartJug);
        } else {
            j = new Competitivo(nickname, videojuego, nivel, cantVicPartJug);
        }
        return j;
    }

    // Al iniciar o después de limpiar:
    private void estadoInicial(){
        buttonRegistrar.setDisable(false);
        buttonEliminar.setDisable(true);
        buttonModificar.setDisable(true);
        // Buscar solo se habilita si hay jugadores en la tabla
        buttonBuscar.setDisable(tableViewJugadores.getItems().isEmpty());
    }

    // Cuando hay un jugador seleccionado o encontrado:
    private void estadoSeleccionado(){
        buttonRegistrar.setDisable(true);
        buttonEliminar.setDisable(false);
        buttonModificar.setDisable(false);
        buttonBuscar.setDisable(false);
    }

    // Pasa los datos del jugador seleccionado al formulario:
    private void cargarFormulario(Jugador j){
        textFieldNickname.setText(j.getNickname());
        comboBoxVideojuego.setValue(j.getVideojuego());
        textFieldNivel.setText(String.valueOf(j.getNivel()));
        if(j instanceof Casual){
            comboBoxTipoJugador.setValue("Casual");
        }else{
            comboBoxTipoJugador.setValue("Competitivo");
        }
        // Falta la cantidad (partidas o victorias), ver nota al final
    }
}

package technesis.client.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import technesis.client.backend.ApiCallException;
import technesis.client.backend.BackendService;

import java.util.Random;

public class MainWindow {
    private final ObservableList<Float> data = FXCollections.observableArrayList();
    private final BackendService backendService = new BackendService();
    private final Random random = new Random();
    private final Scene scene;
    private final TextFormatter<Float> temperatureFormatter = new TextFormatter<>(new TemperatureConverter());

    public MainWindow() {
        var listView = new ListView<>(data);

        var getBtn = new Button("Получить данные");
        getBtn.setOnAction(event -> getTemperatures());

        var setValField = new TextField();
        setValField.setTextFormatter(temperatureFormatter);
        var setBtn = new Button("Задать");
        setBtn.setOnAction(event -> setTemperature());
        var setPane = new HBox(setValField, setBtn);

        var rightPane = new VBox(getBtn, setPane);
        rightPane.setPadding(new Insets(5));
        rightPane.setSpacing(5);

        var root = new BorderPane();
        root.setCenter(listView);
        root.setRight(rightPane);
        scene = new Scene(root);

        refreshCurrentTemperature(false);
    }

    public Scene getScene() {
        return scene;
    }

    private void refreshCurrentTemperature(boolean addToList) {
        backendService.getCurrentTemperature().thenAccept(value ->
            Platform.runLater(() -> {
                temperatureFormatter.setValue(value);
                if (addToList) {
                    data.add(value);
                }
            })
        ).exceptionally(this::handleException);
    }

    private void getTemperatures() {
        int count = 3 + random.nextInt(10);
        backendService.getTemperatures(count, 0).thenAccept(
                values -> Platform.runLater(() -> data.addAll(values))
        ).exceptionally(this::handleException);
    }

    private void setTemperature() {
        float value = temperatureFormatter.getValue();
        backendService.setTemperature(value).thenAccept(
                non -> refreshCurrentTemperature(true)
        ).exceptionally(this::handleException);
    }

    private Void handleException(Throwable throwable) {
        String msg = throwable.getMessage();
        if (throwable.getCause() instanceof ApiCallException apiEx) {
            if (apiEx.getErrorCode() == 2 || apiEx.getErrorCode() == 3) {
                try {
                    Thread.sleep(3000);
                    msg = "Значение температуры вне допустимого диапазона";
                } catch (InterruptedException ignored) {
                }
            }
        }
        String warnText = msg;
        Platform.runLater(() -> new Alert(Alert.AlertType.WARNING, warnText).show());
        return null;
    }
}

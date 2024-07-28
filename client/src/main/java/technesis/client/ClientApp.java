package technesis.client;

import javafx.application.Application;
import javafx.stage.Stage;
import technesis.client.ui.MainWindow;

public class ClientApp extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Test task for Technesis");
        var wnd = new MainWindow();
        stage.setScene(wnd.getScene());
        stage.show();
    }
}

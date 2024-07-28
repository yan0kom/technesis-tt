module technesis.client {
    requires javafx.graphics;
    requires javafx.controls;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    exports technesis.client;
    exports technesis.client.backend;
    exports technesis.client.ui;
}

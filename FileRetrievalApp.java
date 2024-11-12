package application;

public class FileRetrievalApp {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FileRetrievalManager manager = new FileRetrievalManager();
            manager.setVisible(true);
        });
    }
}
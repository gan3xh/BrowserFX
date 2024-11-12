package application;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

public class FileRetriever extends Observable implements Runnable {
    private static final int BUFFER_SIZE = 1024;
    
    public static final String[] STATUS_LABELS = {"Downloading", "Paused", "Complete", "Cancelled", "Error"};
    public static final int DOWNLOADING = 0, PAUSED = 1, COMPLETE = 2, CANCELLED = 3, ERROR = 4;
    
    private URL url;
    private long size;
    private long downloaded;
    private int status;
    private long remainingTime = -1;
    private long initTime;
    private float speed = 0;
    private long elapsedTime = 0;
    private float avgSpeed = 0;
    private File saveLocation;
    
    public FileRetriever(URL url) {
        this.url = url;
        size = -1;
        downloaded = 0;
        status = DOWNLOADING;
        promptForSaveLocation();
    }
    
    private void promptForSaveLocation() {
        SwingUtilities.invokeLater(() -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(getFileName(url)));
            int userSelection = fileChooser.showSaveDialog(null);
            
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                saveLocation = fileChooser.getSelectedFile();
                download();
            } else {
                cancel();
            }
        });
    }
    
    public String getUrl() { return url.toString(); }
    public long getSize() { return size; }
    public float getSpeed() { return speed; }
    public float getAvgSpeed() { return avgSpeed; }
    public String getElapsedTime() { return formatTime(elapsedTime / 1000000000); }
    public String getRemainingTime() { return (remainingTime < 0) ? "Unknown" : formatTime(remainingTime); }
    public float getProgress() { return ((float) downloaded / size) * 100; }
    public int getStatus() { return status; }
    
    public void pause() {
        status = PAUSED;
        stateChanged();
    }
    
    public void resume() {
        status = DOWNLOADING;
        stateChanged();
        download();
    }
    
    public void cancel() {
        status = CANCELLED;
        stateChanged();
    }
    
    private void error() {
        status = ERROR;
        stateChanged();
    }
    
    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }
    
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
    
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;
        
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
            connection.connect();
            
            if (connection.getResponseCode() / 100 != 2) {
                error();
            }
            
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                error();
            }
            
            if (size == -1) {
                size = contentLength;
                stateChanged();
            }
            
            file = new RandomAccessFile(saveLocation, "rw");
            file.seek(downloaded);
            
            stream = connection.getInputStream();
            initTime = System.nanoTime();
            long startTime = initTime;
            long readSinceStart = 0;
            
            while (status == DOWNLOADING) {
                byte buffer[];
                if (size - downloaded > BUFFER_SIZE) {
                    buffer = new byte[BUFFER_SIZE];
                } else {
                    buffer = new byte[(int)(size - downloaded)];
                }
                
                int read = stream.read(buffer);
                if (read == -1) break;
                
                file.write(buffer, 0, read);
                downloaded += read;
                readSinceStart += read;
                
                if (System.nanoTime() - startTime > 1000000000) { // Update every second
                    speed = (readSinceStart * 976.562f) / ((System.nanoTime() - startTime) / 1000000);
                    if (speed > 0) remainingTime = (long)((size - downloaded) / (speed * 1024));
                    elapsedTime = System.nanoTime() - initTime;
                    avgSpeed = (downloaded * 976.562f) / (elapsedTime / 1000000);
                    startTime = System.nanoTime();
                    readSinceStart = 0;
                    stateChanged();
                }
            }
            
            if (status == DOWNLOADING) {
                status = COMPLETE;
                stateChanged();
            }
        } catch (Exception e) {
            error();
        } finally {
            if (file != null) {
                try { file.close(); } catch (Exception e) {}
            }
            if (stream != null) {
                try { stream.close(); } catch (Exception e) {}
            }
        }
    }
    
    private String formatTime(long seconds) {
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }
    
    private void stateChanged() {
        setChanged();
        notifyObservers();
    }
}
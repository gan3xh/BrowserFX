package application;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings({ "serial", "deprecation" })
public class FileRetrievalManager extends JFrame implements Observer {
    private JTextField addTextField;
    private FileRetrievalTableModel tableModel;
    private JTable table;
    private JButton pauseButton, resumeButton;
    private JButton cancelButton, clearButton;
    private FileRetriever selectedDownload;
    private boolean clearing;
    
    public FileRetrievalManager() {
        setTitle("Downloader");
        setSize(640, 480);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(e -> actionExit());
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
        JPanel addPanel = new JPanel();
        addTextField = new JTextField(30);
        addPanel.add(addTextField);
        JButton addButton = new JButton("Add Download");
        addButton.addActionListener(e -> actionAdd());
        addPanel.add(addButton);
        
        tableModel = new FileRetrievalTableModel();
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> tableSelectionChanged());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        ProgressRenderer renderer = new ProgressRenderer(0, 100);
        renderer.setStringPainted(true);
        table.setDefaultRenderer(JProgressBar.class, renderer);
        table.setRowHeight((int) renderer.getPreferredSize().getHeight());
        
        JPanel downloadsPanel = new JPanel();
        downloadsPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
        downloadsPanel.setLayout(new BorderLayout());
        downloadsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel buttonsPanel = new JPanel();
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> actionPause());
        pauseButton.setEnabled(false);
        buttonsPanel.add(pauseButton);
        resumeButton = new JButton("Resume");
        resumeButton.addActionListener(e -> actionResume());
        resumeButton.setEnabled(false);
        buttonsPanel.add(resumeButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> actionCancel());
        cancelButton.setEnabled(false);
        buttonsPanel.add(cancelButton);
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> actionClear());
        clearButton.setEnabled(false);
        buttonsPanel.add(clearButton);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(addPanel, BorderLayout.NORTH);
        getContentPane().add(downloadsPanel, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }
    
    private void actionExit() {
        System.exit(0);
    }
    
    private void actionAdd() {
        URL verifiedUrl = verifyUrl(addTextField.getText());
        if (verifiedUrl != null) {
            tableModel.addDownload(new FileRetriever(verifiedUrl));
            addTextField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private URL verifyUrl(String url) {
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://"))
            return null;
        
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
        
        if (verifiedUrl.getFile().length() < 2)
            return null;
        
        return verifiedUrl;
    }
    
    private void tableSelectionChanged() {
        if (selectedDownload != null)
            selectedDownload.deleteObserver(this);
        
        if (!clearing && table.getSelectedRow() > -1) {
            selectedDownload = tableModel.getDownload(table.getSelectedRow());
            selectedDownload.addObserver(this);
            updateButtons();
        }
    }
    
    private void actionPause() {
        selectedDownload.pause();
        updateButtons();
    }
    
    private void actionResume() {
        selectedDownload.resume();
        updateButtons();
    }
    
    private void actionCancel() {
        selectedDownload.cancel();
        updateButtons();
    }
    
    private void actionClear() {
        clearing = true;
        tableModel.clearDownload(table.getSelectedRow());
        clearing = false;
        selectedDownload = null;
        updateButtons();
    }
    
    private void updateButtons() {
        if (selectedDownload != null) {
            int status = selectedDownload.getStatus();
            switch (status) {
                case FileRetriever.DOWNLOADING:
                    pauseButton.setEnabled(true);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case FileRetriever.PAUSED:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(true);
                    clearButton.setEnabled(false);
                    break;
                case FileRetriever.ERROR:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(true);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
                    break;
                default:
                    pauseButton.setEnabled(false);
                    resumeButton.setEnabled(false);
                    cancelButton.setEnabled(false);
                    clearButton.setEnabled(true);
            }
        } else {
            pauseButton.setEnabled(false);
            resumeButton.setEnabled(false);
            cancelButton.setEnabled(false);
            clearButton.setEnabled(false);
        }
    }
    
    public void update(Observable o, Object arg) {
        if (selectedDownload != null && selectedDownload.equals(o))
            updateButtons();
    }
}
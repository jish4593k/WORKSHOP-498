import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileProcessor {
    static boolean isValidPdf(String file) {
        return file.endsWith(".pdf");
    }

    static List<List<String>> readData(String[] files, String[] columnNames, int year) {
        List<List<String>> data = new ArrayList<>();
        if (files == null || files.length == 0 || !allValidPdf(files)) {
            JOptionPane.showMessageDialog(null, "ERROR\nInvalid or no PDF files selected.");
            return data;
        }

        return data;
    }

    static void saveData(List<List<String>> data, String filename, String[] columnNames) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(String.join(",", columnNames) + "\n");
            for (List<String> row : data) {
                writer.write(String.join(",", row) + "\n");
            }
            JOptionPane.showMessageDialog(null, "File " + filename + " Saved");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot open file " + filename);
        }
    }

    private static boolean allValidPdf(String[] files) {
        for (String file : files) {
            if (!isValidPdf(file)) {
                JOptionPane.showMessageDialog(null, "ERROR\nNot all files are PDFs");
                return false;
            }
        }
        return true;
    }
}

class GUIManager {
    private final FileProcessor fileProcessor;
    private List<List<String>> data;
    private String[] columnNames = {""};
    private String[] yearList = {"2021", "2020"};

    public GUIManager() {
        this.fileProcessor = new FileProcessor();
        this.data = new ArrayList<>();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Cook County Data Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton readButton = new JButton("Read Files");
        JButton saveButton = new JButton("Save Table");

        JTextField filesTextField = new JTextField(20);
        JComboBox<String> yearComboBox = new JComboBox<>(yearList);

        JTable table = new JTable();

        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] files = filesTextField.getText().split(";");
                int year = Integer.parseInt((String) yearComboBox.getSelectedItem());
                data = fileProcessor.readData(files, columnNames, year);
                // Update the table with the data
                // table.setModel(...);
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save As");
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

                int userSelection = fileChooser.showSaveDialog(null);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String filename = fileChooser.getSelectedFile().getPath();
                    fileProcessor.saveData(data, filename, columnNames);
                }
            }
        });

        panel.add(new JLabel("Choose Files: "));
        panel.add(filesTextField);
        panel.add(new JButton("Files Browse")); // Add file browse functionality
        panel.add(new JLabel("Select Assessment Year"));
        panel.add(yearComboBox);
        panel.add(readButton);
        panel.add(new JScrollPane(table));
        panel.add(saveButton);

        frame.getContentPane().add(panel);
        frame.setSize(600, 300);
        frame.setVisible(true);
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUIManager();
            }
        });
    }
}

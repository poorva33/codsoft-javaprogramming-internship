import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

public class CurrencyConverter extends JFrame {
    private JTextField amountField;
    private JComboBox<String> fromCurrencyBox;
    private JComboBox<String> toCurrencyBox;
    private JLabel resultLabel;

    public CurrencyConverter() {
        
        setTitle("Currency Converter");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        
        getContentPane().setBackground(Color.WHITE);

        
        ((JComponent) getContentPane()).setBorder(BorderFactory.createLineBorder(new Color(128, 0, 128), 5));

        
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(20, 20, 100, 25);
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        amountLabel.setForeground(new Color(128, 0, 128));
        add(amountLabel);

    
        amountField = new JTextField();
        amountField.setBounds(150, 20, 200, 25);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(amountField);

        
        JLabel fromCurrencyLabel = new JLabel("From:");
        fromCurrencyLabel.setBounds(20, 60, 100, 25);
        fromCurrencyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fromCurrencyLabel.setForeground(new Color(128, 0, 128));
        add(fromCurrencyLabel);

        
        fromCurrencyBox = createStyledComboBox(new String[]{"USD", "EUR", "INR", "GBP", "JPY"});
        fromCurrencyBox.setBounds(150, 60, 200, 25);
        add(fromCurrencyBox);

        
        JLabel toCurrencyLabel = new JLabel("To:");
        toCurrencyLabel.setBounds(20, 100, 100, 25);
        toCurrencyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        toCurrencyLabel.setForeground(new Color(128, 0, 128));
        add(toCurrencyLabel);

        
        toCurrencyBox = createStyledComboBox(new String[]{"USD", "EUR", "INR", "GBP", "JPY"});
        toCurrencyBox.setBounds(150, 100, 200, 25);
        add(toCurrencyBox);

        
        JButton convertButton = new JButton("Convert");
        convertButton.setBounds(150, 140, 100, 30);
        convertButton.setBackground(new Color(128, 0, 128));
        convertButton.setForeground(Color.WHITE);
        convertButton.setFont(new Font("Arial", Font.BOLD, 14));
        add(convertButton);

        
        resultLabel = new JLabel("");
        resultLabel.setBounds(20, 180, 350, 25);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(new Color(34, 139, 34)); // Green for the result
        add(resultLabel);

        
        convertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(new Color(230, 230, 250)); // Light purple background
        comboBox.setForeground(new Color(75, 0, 130));   // Dark purple text
        comboBox.setBorder(BorderFactory.createLineBorder(new Color(128, 0, 128), 2));
        return comboBox;
    }

    private void convertCurrency() {
        String amountText = amountField.getText();
        if (amountText.isEmpty()) {
            resultLabel.setText("Please enter an amount.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            String fromCurrency = (String) fromCurrencyBox.getSelectedItem();
            String toCurrency = (String) toCurrencyBox.getSelectedItem();

            double exchangeRate = fetchExchangeRate(fromCurrency, toCurrency);
            double convertedAmount = amount * exchangeRate;

            DecimalFormat df = new DecimalFormat("#.##");
            resultLabel.setText("Converted Amount: " + df.format(convertedAmount) + " " + toCurrency);
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid amount entered.");
        } catch (Exception ex) {
            resultLabel.setText("Error: " + ex.getMessage());
        }
    }

    private double fetchExchangeRate(String from, String to) throws Exception {
        String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + from;

        // Make the API request
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse JSON manually
        String jsonResponse = response.toString();
        String targetKey = "\"" + to + "\":";
        int startIndex = jsonResponse.indexOf(targetKey) + targetKey.length();
        if (startIndex == -1) {
            throw new Exception("Currency not found.");
        }
        int endIndex = jsonResponse.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = jsonResponse.indexOf("}", startIndex);
        }
        String exchangeRateString = jsonResponse.substring(startIndex, endIndex).trim();
        return Double.parseDouble(exchangeRateString);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CurrencyConverter().setVisible(true);
        });
    }
}

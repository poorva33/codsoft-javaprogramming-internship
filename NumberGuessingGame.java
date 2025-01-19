import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class NumberGuessingGame {

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Number Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        
        Random random = new Random();
        final int[] targetNumber = {random.nextInt(100) + 1};
        final int[] guessesLeft = {10};
        final int[] roundsWon = {0};

        
        JLabel instructionsLabel = new JLabel("I am thinking of a number between 1-100. Can you guess it?", SwingConstants.CENTER);
        instructionsLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel feedbackLabel = new JLabel("You have 10 guesses left", SwingConstants.CENTER);
        feedbackLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel scoreLabel = new JLabel("Rounds Won: 0", SwingConstants.CENTER);

        JTextField guessField = new JTextField(2); 
        guessField.setHorizontalAlignment(JTextField.CENTER);

        JButton guessButton = new JButton("Guess");
        guessButton.setBackground(Color.BLUE);
        guessButton.setForeground(Color.WHITE);
        guessButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

        JButton resetButton = new JButton("Next Round");
        resetButton.setEnabled(false);
        resetButton.setBackground(Color.BLUE);
        resetButton.setForeground(Color.WHITE);
        resetButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(guessField);
        inputPanel.add(guessButton);

        centerPanel.add(inputPanel);
        centerPanel.add(feedbackLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        centerPanel.add(resetButton);

        
        frame.add(instructionsLabel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(scoreLabel, BorderLayout.SOUTH);

        
        guessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int userGuess = Integer.parseInt(guessField.getText());

                    if (userGuess < 1 || userGuess > 100) {
                        feedbackLabel.setText("Enter a number between 1 and 100!");
                        guessField.setText("");
                        return;
                    }

                    guessesLeft[0]--;
                    if (userGuess == targetNumber[0]) {
                        feedbackLabel.setText("Correct! The number was " + targetNumber[0] + ".");
                        roundsWon[0]++;
                        scoreLabel.setText("Rounds Won: " + roundsWon[0]);
                        guessButton.setEnabled(false);
                        resetButton.setEnabled(true);
                    } else if (userGuess < targetNumber[0]) {
                        feedbackLabel.setText("Too Low! You have " + guessesLeft[0] + " guesses left.");
                    } else {
                        feedbackLabel.setText("Too High! You have " + guessesLeft[0] + " guesses left.");
                    }

                    if (guessesLeft[0] <= 0 && userGuess != targetNumber[0]) {
                        feedbackLabel.setText("Game Over! The number was " + targetNumber[0] + ".");
                        guessButton.setEnabled(false);
                        resetButton.setEnabled(true);
                    }

                    guessField.setText("");
                } catch (NumberFormatException ex) {
                    feedbackLabel.setText("Please enter a valid number.");
                    guessField.setText("");
                }
            }
        });


        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                targetNumber[0] = random.nextInt(100) + 1;
                guessesLeft[0] = 10;
                feedbackLabel.setText("You have 10 guesses left");
                guessField.setText("");
                guessButton.setEnabled(true);
                resetButton.setEnabled(false);
            }
        });

    
        frame.setVisible(true);
    }
}

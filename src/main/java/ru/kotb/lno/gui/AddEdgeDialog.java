package ru.kotb.lno.gui;

import lombok.Getter;
import ru.kotb.lno.dto.EdgeDTO;

import javax.swing.*;
import java.awt.*;


/**
 * Dialog window for adding an edge to a graph
 */
@Getter
public class AddEdgeDialog extends JDialog {

    /**
     * Entered information about the edge
     */
    private EdgeDTO inputtedData;

    public AddEdgeDialog(Frame owner) {
        super(owner);
        settings();

        JLabel sourceLabel = new JLabel("Source");
        JTextField sourceTextField = new JTextField(10);

        JLabel targetLabel = new JLabel("Target");
        JTextField targetTextField = new JTextField(10);

        JLabel weights = new JLabel("Weights");
        JTextField weightsTextField1 = new JTextField(5);
        JTextField weightsTextField2 = new JTextField(5);

        this.add(sourceLabel);
        this.add(sourceTextField);
        this.add(targetLabel);
        this.add(targetTextField);
        this.add(weights);
        this.add(weightsTextField1);
        this.add(weightsTextField2);

        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(e -> {
            String source = sourceTextField.getText();
            String target = targetTextField.getText();
            int weight1 = Integer.parseInt(weightsTextField1.getText());
            int weight2 = Integer.parseInt(weightsTextField2.getText());

            inputtedData = new EdgeDTO(
                    source,
                    target,
                    weight1,
                    weight2
            );
            dispose();
        });

        cancelBtn.addActionListener((e) -> this.dispose());

        this.add(okBtn);
        this.add(cancelBtn);

        this.setVisible(true);
    }

    private void settings() {
        this.setTitle("Add edge");
        this.setModal(true);
        this.setSize(200, 150);
        this.setResizable(false);
        this.getContentPane().setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
    }
}

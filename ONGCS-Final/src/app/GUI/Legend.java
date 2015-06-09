package app.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Font;

public class Legend extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	
	public void showLegend(){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Legend frame = new Legend();
					frame.toBack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Legend frame = new Legend();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Legend() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(480, 500, 324, 199);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label1 = new JLabel("");
		label1.setBackground(Color.MAGENTA);
		label1.setOpaque(true);

		label1.setBounds(197, 19, 90, 7);
		contentPane.add(label1);
		
		JLabel label = new JLabel("");
		label.setBackground(Color.YELLOW);
		label.setOpaque(true);
		label.setBounds(197, 39, 90, 7);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("");
		label_1.setBackground(Color.GREEN);
		label_1.setOpaque(true);

		label_1.setBounds(197, 64, 90, 7);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("");
		label_2.setBackground(Color.CYAN);
		label_2.setBounds(197, 89, 90, 7);
		label_2.setOpaque(true);

		contentPane.add(label_2);
		
		JLabel label_3 = new JLabel("");
		label_3.setBackground(Color.BLUE);
		label_3.setBounds(197, 114, 90, 7);
		label_3.setOpaque(true);

		contentPane.add(label_3);
		
		JLabel label_4 = new JLabel("");
		label_4.setBackground(new Color(75, 0, 130));
		label_4.setBounds(197, 139, 90, 7);
		label_4.setOpaque(true);

		contentPane.add(label_4);
		
		JLabel lblNewLabel = new JLabel("HACS ");
		lblNewLabel.setBounds(27, 14, 46, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblSameOrientation = new JLabel("Same orientation - 0.1 loss");
		lblSameOrientation.setBounds(27, 39, 160, 14);
		contentPane.add(lblSameOrientation);
		
		JLabel lblSameOrientation_1 = new JLabel("Same orientation - 0.2 loss");
		lblSameOrientation_1.setBounds(27, 64, 160, 14);
		contentPane.add(lblSameOrientation_1);
		
		JLabel lblSameOrientation_2 = new JLabel("Same orientation - 0.3 loss");
		lblSameOrientation_2.setBounds(27, 89, 174, 14);
		contentPane.add(lblSameOrientation_2);
		
		JLabel lblSameOrientation_3 = new JLabel("Same orientation - 0.4 loss");
		lblSameOrientation_3.setBounds(27, 114, 160, 14);
		contentPane.add(lblSameOrientation_3);
		
		JLabel lblSameOrientation_4 = new JLabel("Same orientation - 0.5 loss");
		lblSameOrientation_4.setBounds(27, 139, 160, 14);
		contentPane.add(lblSameOrientation_4);
		
		JLabel lblColor = new JLabel("          Colour");
		lblColor.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblColor.setBounds(185, 1, 102, 14);
		contentPane.add(lblColor);
		
		JLabel lblCoolingSystem = new JLabel("             Label");
		lblCoolingSystem.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCoolingSystem.setBounds(10, 1, 177, 14);
		contentPane.add(lblCoolingSystem);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 14, 309, 2);
		contentPane.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 32, 309, 2);
		contentPane.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 55, 309, 103);
		contentPane.add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(10, 81, 309, 77);
		contentPane.add(separator_3);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(10, 107, 309, 52);
		contentPane.add(separator_4);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(10, 132, 309, 27);
		contentPane.add(separator_5);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(10, 156, 299, 2);
		contentPane.add(separator_6);
		
		JSeparator separator_7 = new JSeparator();
		separator_7.setOrientation(SwingConstants.VERTICAL);
		separator_7.setBounds(185, 0, 2, 178);
		contentPane.add(separator_7);
		
		JSeparator separator_9 = new JSeparator();
		separator_9.setBounds(10, 0, 299, 173);
		contentPane.add(separator_9);
		
		JSeparator separator_10 = new JSeparator();
		separator_10.setOrientation(SwingConstants.VERTICAL);
		separator_10.setBounds(10, 5, 2, 173);
		contentPane.add(separator_10);
		
		JSeparator separator_8 = new JSeparator();
		separator_8.setOrientation(SwingConstants.VERTICAL);
		separator_8.setBounds(307, 0, 2, 178);
		contentPane.add(separator_8);
		

	
	}
}

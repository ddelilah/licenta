package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.Timer;

import app.access.impl.ServerDAOImpl;
import app.main.Main;
import app.model.Server;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {

	private JComboBox comboBox = new JComboBox();
	private Main main = new Main();
	private JPanel contentPane;
	private JTextField textField;
	private RackUtilizationGUI rackUtilGUI = new RackUtilizationGUI(); 
	static MainGUI frame = new MainGUI();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setBounds(0, 0, 460, 720);
					frame.toFront();
					frame.setTitle("Energy-Aware Data Centers");
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
	public MainGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 451, 387);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
//		ImageIcon image = new ImageIcon("C:\\Users\\Ade\\Desktop\\GitHub\\licenta\\ONGCS-Final\\dc.jpg");
//		JLabel label = new JLabel( image);
////		contentPane.setOpaque(true);
////		contentPane.setBackground(Color.white);
////		contentPane.add( label);
		
		JLabel lblCracInletTemperature = new JLabel("CRAC inlet temperature: ");
		lblCracInletTemperature.setBounds(10, 23, 139, 14);
		contentPane.add(lblCracInletTemperature);
		
		textField = new JTextField();
		textField.setBounds(159, 20, 86, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblAlgorithmToStart = new JLabel("Algorithm to start:");
		lblAlgorithmToStart.setBounds(10, 54, 121, 14);
		contentPane.add(lblAlgorithmToStart);
		
		
		JButton btnInputData = new JButton("Input Data");
		btnInputData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = new File("script.txt");
				    try {
						Desktop.getDesktop().edit(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		
		btnInputData.setForeground(Color.WHITE);
		btnInputData.setBackground(new Color(148, 0, 211));
		btnInputData.setFont(new Font("Comic Sans", Font.BOLD, 15));
		btnInputData.setOpaque(true);

		
		btnInputData.setBounds(10, 94, 121, 23);
		contentPane.add(btnInputData);
		
		
		comboBox.addItem("FFD");
		comboBox.addItem("NUR");
		comboBox.addItem("RBR");
		comboBox.setBounds(159, 51, 86, 20);
		contentPane.add(comboBox);
		
		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Repaint r = new Repaint();
				
					rackUtilGUI.openFrame();
					System.out.println(String.valueOf(comboBox.getSelectedItem()));
					System.out.println(textField.getText());	
					try {
						rackUtilGUI.getTextArea().append("........... Start initialization...............");
						main.startInitialization();
						rackUtilGUI.getTextArea().append("........... Start monitoring ..................");
						main.startMonitoring(String.valueOf(comboBox.getSelectedItem()),textField.getText());
						
						int delay = 1000; //milliseconds

						ActionListener taskPerformer = new ActionListener() {
						  public void actionPerformed(ActionEvent evt) {
						    rackUtilGUI.repaintAllButtons();
						
						    List<Server> serverList = new ArrayList<>();
						    ServerDAOImpl sDAO = new ServerDAOImpl();
						    serverList = sDAO.getAllServers();
							r.repaintFrame(serverList);
							
							rackUtilGUI.getTextArea().append("hello");
							
						  }
						};

						new Timer(delay, taskPerformer).start();
						
						
						frame.toFront();
						
						
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}  	
			}
		});
		btnStart.setBounds(289, 34, 89, 23);
		contentPane.add(btnStart);
		
		
	}
	
	public JFrame getFrame(){
		return this.frame;
	}
	
	
}

package app.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import java.awt.GridLayout;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import app.access.ServerDAO;
import app.access.impl.ServerDAOImpl;
import app.energy.PowerConsumption;
import app.energy.Utilization;
import app.model.Server;
import javax.swing.JInternalFrame;

public class RackUtilizationGUI extends JFrame {
	private static HashMap componentMap;
	TextArea textArea = new TextArea("", 20, 60, TextArea.SCROLLBARS_BOTH);
	private static JPanel contentPane;
	JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11,
			btn12, btn13, btn14, btn15, btn16, btn17, btn18, btn19, btn20,
			btn21, btn22, btn23, btn24, btn25, btn26, btn27, btn28, btn29,
			btn30, btn31, btn32;
	private static RackUtilizationGUI frame;

	public void openFrame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new RackUtilizationGUI();
					frame.setName("frame");
					frame.setBounds(890, 0, 480, 720);
					frame.setTitle("Virtual Machine Placement");
					frame.setVisible(true);

					createComponentMap();

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
					frame = new RackUtilizationGUI();
					frame.setBounds(890, 0, 480, 720);

					frame.setVisible(true);
					frame.setName("frame");
					createComponentMap();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Create the frame.
	 */
	public RackUtilizationGUI() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 536, 387);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		contentPane.setBackground(Color.white);

		btn1 = new JButton("S1");
		btn1.setName("S1");
		btn1.setEnabled(false);

		btn1.setBounds(41, 22, 51, 23);
		contentPane.add(btn1);

		btn3 = new JButton("S3");
		btn3.setName("S3");

		btn3.setEnabled(false);
		btn3.setBounds(41, 48, 51, 23);
		contentPane.add(btn3);

		btn2 = new JButton("S2");
		btn2.setName("S2");

		btn2.setEnabled(false);
		btn2.setBounds(99, 22, 51, 23);
		contentPane.add(btn2);

		btn4 = new JButton("S4");
		btn4.setName("S4");

		btn4.setEnabled(false);
		btn4.setBounds(99, 48, 51, 23);
		contentPane.add(btn4);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(41, 82, 109, 2);
		contentPane.add(separator_1);

		btn9 = new JButton("S9");
		btn9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn9.setName("S9");

		btn9.setEnabled(false);
		btn9.setBounds(327, 22, 51, 23);
		contentPane.add(btn9);

		btn11 = new JButton("S11");
		btn11.setName("S11");

		btn11.setEnabled(false);
		btn11.setBounds(327, 48, 56, 23);
		contentPane.add(btn11);

		btn10 = new JButton("S10");
		btn10.setName("S10");

		btn10.setEnabled(false);
		btn10.setBounds(382, 22, 56, 23);
		contentPane.add(btn10);

		btn12 = new JButton("S12");
		btn12.setName("S12");

		btn12.setEnabled(false);
		btn12.setBounds(382, 48, 56, 23);
		contentPane.add(btn12);

		btn13 = new JButton("S13");
		btn13.setName("S13");

		btn13.setEnabled(false);
		btn13.setBounds(41, 119, 56, 23);
		contentPane.add(btn13);

		btn14 = new JButton("S14");
		btn14.setName("S14");

		btn14.setEnabled(false);
		btn14.setBounds(96, 119, 56, 23);
		contentPane.add(btn14);

		btn15 = new JButton("S15");
		btn15.setName("S15");

		btn15.setEnabled(false);
		btn15.setBounds(41, 145, 56, 23);
		contentPane.add(btn15);

		btn16 = new JButton("S16");
		btn16.setName("S16");

		btn16.setEnabled(false);
		btn16.setBounds(96, 145, 56, 23);
		contentPane.add(btn16);

		btn5 = new JButton("S5");
		btn5.setName("S5");

		btn5.setEnabled(false);
		btn5.setBounds(181, 22, 51, 23);
		contentPane.add(btn5);

		btn6 = new JButton("S6");
		btn6.setName("S6");

		btn6.setEnabled(false);
		btn6.setBounds(233, 22, 51, 23);
		contentPane.add(btn6);

		btn7 = new JButton("S7");
		btn7.setName("S7");

		btn7.setEnabled(false);
		btn7.setBounds(181, 48, 51, 23);
		contentPane.add(btn7);

		btn8 = new JButton("S8");
		btn8.setName("S8");

		btn8.setEnabled(false);
		btn8.setBounds(233, 48, 51, 23);
		contentPane.add(btn8);

		btn17 = new JButton("S17");
		btn17.setName("S17");

		btn17.setEnabled(false);
		btn17.setBounds(181, 123, 56, 23);
		contentPane.add(btn17);

		btn18 = new JButton("S18");
		btn18.setName("S18");

		btn18.setEnabled(false);
		btn18.setBounds(236, 123, 56, 23);
		contentPane.add(btn18);

		btn25 = new JButton("S25");
		btn25.setName("S25");

		btn25.setEnabled(false);
		btn25.setBounds(39, 219, 56, 23);
		contentPane.add(btn25);

		btn26 = new JButton("S26");
		btn26.setName("S26");

		btn26.setEnabled(false);
		btn26.setBounds(94, 219, 56, 23);
		contentPane.add(btn26);

		btn27 = new JButton("S27");
		btn27.setName("S27");

		btn27.setEnabled(false);
		btn27.setBounds(39, 245, 56, 23);
		contentPane.add(btn27);

		btn28 = new JButton("S28");
		btn28.setName("S28");

		btn28.setEnabled(false);
		btn28.setBounds(94, 245, 56, 23);
		contentPane.add(btn28);

		btn29 = new JButton("S29");
		btn29.setName("S29");

		btn29.setEnabled(false);
		btn29.setBounds(181, 219, 56, 23);
		contentPane.add(btn29);

		btn30 = new JButton("S30");
		btn30.setName("S30");

		btn30.setEnabled(false);
		btn30.setBounds(236, 219, 56, 23);
		contentPane.add(btn30);

		btn31 = new JButton("S31");
		btn31.setName("S31");

		btn31.setEnabled(false);
		btn31.setBounds(181, 245, 56, 23);
		contentPane.add(btn31);

		btn32 = new JButton("S32");
		btn32.setEnabled(false);
		btn32.setName("S32");

		btn32.setBounds(236, 245, 56, 23);
		contentPane.add(btn32);

		btn19 = new JButton("S19");
		btn19.setName("S19");

		btn19.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn19.setEnabled(false);
		btn19.setBounds(181, 149, 56, 23);
		contentPane.add(btn19);

		btn20 = new JButton("S20");
		btn20.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn20.setName("S20");
		btn20.setEnabled(false);
		btn20.setBounds(233, 149, 56, 23);
		contentPane.add(btn20);

		btn21 = new JButton("S21");
		btn21.setName("S21");

		btn21.setEnabled(false);
		btn21.setBounds(327, 119, 56, 23);
		contentPane.add(btn21);

		btn22 = new JButton("S22");
		btn22.setName("S22");

		btn22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btn22.setEnabled(false);
		btn22.setBounds(381, 119, 56, 23);
		contentPane.add(btn22);

		btn23 = new JButton("S23");

		btn23.setName("S23");
		btn23.setEnabled(false);
		btn23.setBounds(327, 145, 56, 23);
		contentPane.add(btn23);

		btn24 = new JButton("S24");
		btn24.setEnabled(false);
		btn24.setName("S24");

		btn24.setBounds(382, 145, 56, 23);
		contentPane.add(btn24);

		JSeparator separator = new JSeparator();
		separator.setBounds(184, 82, 96, 2);
		contentPane.add(separator);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(327, 82, 109, 2);
		contentPane.add(separator_2);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(42, 179, 108, 2);
		contentPane.add(separator_3);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(181, 183, 111, 2);
		contentPane.add(separator_4);

		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(327, 179, 111, 2);
		contentPane.add(separator_5);

		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(39, 279, 111, 2);
		contentPane.add(separator_6);

		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(181, 279, 111, 2);
		contentPane.add(separator_7);

		JSeparator separator_8 = new JSeparator();
		separator_8.setOrientation(SwingConstants.VERTICAL);
		separator_8.setBounds(181, 11, 1, 73);
		contentPane.add(separator_8);

		JSeparator separator_10 = new JSeparator();
		separator_10.setOrientation(SwingConstants.VERTICAL);
		separator_10.setBounds(41, 11, 1, 73);
		contentPane.add(separator_10);

		JSeparator separator_11 = new JSeparator();
		separator_11.setOrientation(SwingConstants.VERTICAL);
		separator_11.setBounds(149, 11, 1, 73);
		contentPane.add(separator_11);

		JSeparator separator_12 = new JSeparator();
		separator_12.setOrientation(SwingConstants.VERTICAL);
		separator_12.setBounds(283, 11, 1, 73);
		contentPane.add(separator_12);

		JSeparator separator_13 = new JSeparator();
		separator_13.setOrientation(SwingConstants.VERTICAL);
		separator_13.setBounds(327, 11, 1, 73);
		contentPane.add(separator_13);

		JSeparator separator_14 = new JSeparator();
		separator_14.setOrientation(SwingConstants.VERTICAL);
		separator_14.setBounds(437, 11, 1, 73);
		contentPane.add(separator_14);

		JSeparator separator_15 = new JSeparator();
		separator_15.setOrientation(SwingConstants.VERTICAL);
		separator_15.setBounds(41, 108, 1, 73);
		contentPane.add(separator_15);

		JSeparator separator_16 = new JSeparator();
		separator_16.setOrientation(SwingConstants.VERTICAL);
		separator_16.setBounds(149, 108, 1, 73);
		contentPane.add(separator_16);

		JSeparator separator_17 = new JSeparator();
		separator_17.setOrientation(SwingConstants.VERTICAL);
		separator_17.setBounds(291, 108, 1, 73);
		contentPane.add(separator_17);

		JSeparator separator_18 = new JSeparator();
		separator_18.setOrientation(SwingConstants.VERTICAL);
		separator_18.setBounds(327, 108, 1, 73);
		contentPane.add(separator_18);

		JSeparator separator_19 = new JSeparator();
		separator_19.setOrientation(SwingConstants.VERTICAL);
		separator_19.setBounds(437, 108, 1, 73);
		contentPane.add(separator_19);

		JSeparator separator_20 = new JSeparator();
		separator_20.setOrientation(SwingConstants.VERTICAL);
		separator_20.setBounds(39, 208, 1, 73);
		contentPane.add(separator_20);

		JSeparator separator_21 = new JSeparator();
		separator_21.setOrientation(SwingConstants.VERTICAL);
		separator_21.setBounds(149, 208, 1, 73);
		contentPane.add(separator_21);

		JSeparator separator_22 = new JSeparator();
		separator_22.setOrientation(SwingConstants.VERTICAL);
		separator_22.setBounds(181, 208, 1, 73);
		contentPane.add(separator_22);

		JSeparator separator_23 = new JSeparator();
		separator_23.setOrientation(SwingConstants.VERTICAL);
		separator_23.setBounds(291, 208, 1, 73);
		contentPane.add(separator_23);

		JSeparator separator_24 = new JSeparator();
		separator_24.setOrientation(SwingConstants.VERTICAL);
		separator_24.setBounds(181, 112, 1, 73);
		contentPane.add(separator_24);

		JLabel lblRack = new JLabel("Rack 1");
		lblRack.setBounds(87, 82, 46, 14);
		contentPane.add(lblRack);

		JLabel lblRack_1 = new JLabel("Rack 2");
		lblRack_1.setBounds(212, 82, 46, 14);
		contentPane.add(lblRack_1);

		JLabel lblRack_2 = new JLabel("Rack 3");
		lblRack_2.setBounds(362, 82, 46, 14);
		contentPane.add(lblRack_2);

		JLabel lblRack_3 = new JLabel("Rack 4");
		lblRack_3.setBounds(71, 179, 46, 14);
		contentPane.add(lblRack_3);

		JLabel lblRack_4 = new JLabel("Rack 5");
		lblRack_4.setBounds(216, 183, 46, 14);
		contentPane.add(lblRack_4);

		JLabel lblRack_5 = new JLabel("Rack 6");
		lblRack_5.setBounds(357, 179, 46, 14);
		contentPane.add(lblRack_5);

		JLabel lblRack_6 = new JLabel("Rack 7");
		lblRack_6.setBounds(74, 279, 46, 14);
		contentPane.add(lblRack_6);

		JLabel lblRack_7 = new JLabel("Rack 8");
		lblRack_7.setBounds(211, 279, 46, 14);
		contentPane.add(lblRack_7);

		JSeparator separator_9 = new JSeparator();
		separator_9.setBounds(20, 297, 479, 2);
		contentPane.add(separator_9);

		// textArea = new JTextArea();
		// frame.setBounds(890, 0, 480, 720);

		textArea.setBounds(10, 400, 450, 277);
		textArea.setEditable(false);
		textArea.setForeground(Color.black);
		textArea.setBackground(Color.white);

		// contentPane.add(new JScrollPane(textArea));
		contentPane.add(textArea);

		// JScrollPane scrollBar = new JScrollPane();
		// scrollBar.setBounds(472, 278, 17, 60);
		// scrollBar.setVisible(true);
		// scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// textArea.add(scrollBar);

		JLabel labelColor1 = new JLabel("");
		labelColor1.setBounds(38, 307, 39, 14);
		labelColor1.setBackground(new Color(251, 255, 150));
		labelColor1.setOpaque(true);
		contentPane.add(labelColor1);

		JLabel labelColor2 = new JLabel("");
		labelColor2.setBounds(38, 332, 39, 14);
		labelColor2.setBackground(new Color(255, 255, 0));
		labelColor2.setOpaque(true);
		contentPane.add(labelColor2);

		JLabel labelColor3 = new JLabel("");
		labelColor3.setForeground(new Color(251, 255, 91));
		labelColor3.setBounds(39, 357, 39, 14);
		labelColor3.setBackground(new Color(255, 204, 0));
		labelColor3.setOpaque(true);
		contentPane.add(labelColor3);

		JLabel lblUtilization = new JLabel("[20,30)");
		lblUtilization.setBounds(103, 332, 60, 14);
		contentPane.add(lblUtilization);

		JLabel lblUtilization_1 = new JLabel("[30, 40)");
		lblUtilization_1.setBounds(103, 357, 51, 14);
		contentPane.add(lblUtilization_1);

		JLabel labelColor4 = new JLabel("");
		labelColor4.setOpaque(true);
		labelColor4.setBackground(new Color(255, 161, 21));
		labelColor4.setBounds(176, 310, 39, 14);
		contentPane.add(labelColor4);

		JLabel labelColor5 = new JLabel("");
		labelColor5.setOpaque(true);
		labelColor5.setBackground(new Color(255, 115, 21));
		labelColor5.setBounds(176, 335, 39, 14);
		contentPane.add(labelColor5);

		JLabel labelColor6 = new JLabel("");
		labelColor6.setOpaque(true);
		labelColor6.setBackground(new Color(255, 68, 0));
		labelColor6.setBounds(177, 360, 39, 14);
		contentPane.add(labelColor6);

		JLabel label_3 = new JLabel("[40,50)");
		label_3.setBounds(237, 310, 60, 14);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("[50, 60)");
		label_4.setBounds(237, 335, 51, 14);
		contentPane.add(label_4);

		JLabel label = new JLabel("[60, 70)");
		label.setBounds(237, 359, 51, 14);
		contentPane.add(label);

		JLabel labelColor7 = new JLabel("");
		labelColor7.setOpaque(true);
		labelColor7.setBackground(new Color(255, 0, 0));
		labelColor7.setBounds(306, 310, 39, 14);
		contentPane.add(labelColor7);

		JLabel label_2 = new JLabel("[70,80)");
		label_2.setBounds(367, 310, 60, 14);
		contentPane.add(label_2);

		JLabel label_5 = new JLabel("[0, 20)");
		label_5.setBounds(103, 310, 46, 14);
		contentPane.add(label_5);

		JLabel labelColor8 = new JLabel("");
		labelColor8.setOpaque(true);
		labelColor8.setBackground(new Color(0, 0, 0));
		labelColor8.setBounds(306, 337, 39, 14);
		contentPane.add(labelColor8);

		JLabel label_7 = new JLabel("[80,100)");
		label_7.setBounds(367, 337, 60, 14);
		contentPane.add(label_7);

		JSeparator separator_25 = new JSeparator();
		separator_25.setBounds(10, 390, 479, 2);
		contentPane.add(separator_25);
		redirectSystemStreams();

	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JButton getBtn1() {
		return btn1;
	}

	public void setBtn1(JButton btn1) {
		this.btn1 = btn1;
	}

	public JButton getBtn2() {
		return btn2;
	}

	public void setBtn2(JButton btn2) {
		this.btn2 = btn2;
	}

	public JButton getBtn3() {
		return btn3;
	}

	public void setBtn3(JButton btn3) {
		this.btn3 = btn3;
	}

	public JButton getBtn4() {
		return btn4;
	}

	public void setBtn4(JButton btn4) {
		this.btn4 = btn4;
	}

	public JButton getBtn5() {
		return btn5;
	}

	public void setBtn5(JButton btn5) {
		this.btn5 = btn5;
	}

	public JButton getBtn6() {
		return btn6;
	}

	public void setBtn6(JButton btn6) {
		this.btn6 = btn6;
	}

	public JButton getBtn7() {
		return btn7;
	}

	public void setBtn7(JButton btn7) {
		this.btn7 = btn7;
	}

	public JButton getBtn8() {
		return btn8;
	}

	public void setBtn8(JButton btn8) {
		this.btn8 = btn8;
	}

	public JButton getBtn9() {
		return btn9;
	}

	public void setBtn9(JButton btn9) {
		this.btn9 = btn9;
	}

	public JButton getBtn10() {
		return btn10;
	}

	public void setBtn10(JButton btn10) {
		this.btn10 = btn10;
	}

	public JButton getBtn11() {
		return btn11;
	}

	public void setBtn11(JButton btn11) {
		this.btn11 = btn11;
	}

	public JButton getBtn12() {
		return btn12;
	}

	public void setBtn12(JButton btn12) {
		this.btn12 = btn12;
	}

	public JButton getBtn13() {
		return btn13;
	}

	public void setBtn13(JButton btn13) {
		this.btn13 = btn13;
	}

	public JButton getBtn14() {
		return btn14;
	}

	public void setBtn14(JButton btn14) {
		this.btn14 = btn14;
	}

	public JButton getBtn15() {
		return btn15;
	}

	public void setBtn15(JButton btn15) {
		this.btn15 = btn15;
	}

	public JButton getBtn16() {
		return btn16;
	}

	public void setBtn16(JButton btn16) {
		this.btn16 = btn16;
	}

	public JButton getBtn17() {
		return btn17;
	}

	public void setBtn17(JButton btn17) {
		this.btn17 = btn17;
	}

	public JButton getBtn18() {
		return btn18;
	}

	public void setBtn18(JButton btn18) {
		this.btn18 = btn18;
	}

	public JButton getBtn19() {
		return btn19;
	}

	public void setBtn19(JButton btn19) {
		this.btn19 = btn19;
	}

	public JButton getBtn20() {
		return btn20;
	}

	public void setBtn20(JButton btn20) {
		this.btn20 = btn20;
	}

	public JButton getBtn21() {
		return btn21;
	}

	public void setBtn21(JButton btn21) {
		this.btn21 = btn21;
	}

	public JButton getBtn22() {
		return btn22;
	}

	public void setBtn22(JButton btn22) {
		this.btn22 = btn22;
	}

	public JButton getBtn23() {
		return btn23;
	}

	public void setBtn23(JButton btn23) {
		this.btn23 = btn23;
	}

	public JButton getBtn24() {
		return btn24;
	}

	public void setBtn24(JButton btn24) {
		this.btn24 = btn24;
	}

	public JButton getBtn25() {
		return btn25;
	}

	public void setBtn25(JButton btn25) {
		this.btn25 = btn25;
	}

	public JButton getBtn26() {
		return btn26;
	}

	public void setBtn26(JButton btn26) {
		this.btn26 = btn26;
	}

	public JButton getBtn27() {
		return btn27;
	}

	public void setBtn27(JButton btn27) {
		this.btn27 = btn27;
	}

	public JButton getBtn28() {
		return btn28;
	}

	public void setBtn28(JButton btn28) {
		this.btn28 = btn28;
	}

	public JButton getBtn29() {
		return btn29;
	}

	public void setBtn29(JButton btn29) {
		this.btn29 = btn29;
	}

	public JButton getBtn30() {
		return btn30;
	}

	public void setBtn30(JButton btn30) {
		this.btn30 = btn30;
	}

	public JButton getBtn31() {
		return btn31;
	}

	public void setBtn31(JButton btn31) {
		this.btn31 = btn31;
	}

	public JButton getBtn32() {
		return btn32;
	}

	public void setBtn32(JButton btn32) {
		this.btn32 = btn32;
	}

	public void repaintAllButtons() {
		for (int i = 1; i < 33; i++)
			getComponentByName("S" + i).repaint();

	}

	private static void createComponentMap() {
		componentMap = new HashMap<String, Component>();
		Component[] components = frame.getContentPane().getComponents();
		for (int i = 0; i < components.length; i++) {
			componentMap.put(components[i].getName(), components[i]);
		}
	}

	public Component getComponentByName(String name) {
		if (componentMap.containsKey(name)) {
			return (Component) componentMap.get(name);
		} else
			return null;
	}

	public TextArea getTextArea() {
		return this.textArea;
	}

	public void updateTextArea(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textArea.append(text);
			}
		});
	}

	public void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextArea(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextArea(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}
}

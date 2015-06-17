package app.GUI;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import app.model.Server;

public class Repaint {
	JButton btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10, btn11,
			btn12, btn13, btn14, btn15, btn16, btn17, btn18, btn19, btn20,
			btn21, btn22, btn23, btn24, btn25, btn26, btn27, btn28, btn29,
			btn30, btn31, btn32;
	RackUtilizationGUI rGUI;
	JFrame frame;

	public Repaint() {
		rGUI = new RackUtilizationGUI();

		this.frame = rGUI.getFrame();
		this.btn1 = rGUI.getBtn1();
		this.btn2 = rGUI.getBtn2();
		this.btn3 = rGUI.getBtn3();
		this.btn4 = rGUI.getBtn4();
		this.btn5 = rGUI.getBtn5();
		this.btn6 = rGUI.getBtn6();
		this.btn7 = rGUI.getBtn7();
		this.btn8 = rGUI.getBtn8();
		this.btn9 = rGUI.getBtn9();
		this.btn10 = rGUI.getBtn10();
		this.btn11 = rGUI.getBtn11();
		this.btn12 = rGUI.getBtn12();
		this.btn13 = rGUI.getBtn13();
		this.btn14 = rGUI.getBtn14();
		this.btn15 = rGUI.getBtn15();
		this.btn16 = rGUI.getBtn16();
		this.btn17 = rGUI.getBtn17();
		this.btn18 = rGUI.getBtn18();
		this.btn19 = rGUI.getBtn19();
		this.btn20 = rGUI.getBtn20();
		this.btn21 = rGUI.getBtn21();
		this.btn22 = rGUI.getBtn22();
		this.btn23 = rGUI.getBtn23();
		this.btn24 = rGUI.getBtn24();
		this.btn25 = rGUI.getBtn25();
		this.btn26 = rGUI.getBtn26();
		this.btn27 = rGUI.getBtn27();
		this.btn28 = rGUI.getBtn28();
		this.btn29 = rGUI.getBtn29();
		this.btn30 = rGUI.getBtn30();
		this.btn31 = rGUI.getBtn31();
		this.btn32 = rGUI.getBtn32();
	}

	public void repaintFrame(List<Server> serverList) {

		for (Server server : serverList) {
			if (server.getUtilization() == 0)
				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(255, 255, 255));
			else if (server.getUtilization() >= 0.20
					&& server.getUtilization() <= 0.30) {

				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(255, 255, 0));
			} else if (server.getUtilization() > 0.30
					&& server.getUtilization() <= 0.40) {

				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(255, 204, 0));

			} else if (server.getUtilization() >= 0.40
					&& server.getUtilization() <= 0.50) {
				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(255, 161, 21));
			} else if (server.getUtilization() >= 0.50
					&& server.getUtilization() <= 0.60) {
				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(255, 115, 21));

			} else if (server.getUtilization() >= 0.60
					&& server.getUtilization() <= 0.70) {
				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(255, 68, 0));

			} else if (server.getUtilization() >= 0.70
					&& server.getUtilization() <= 0.80) {
				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(255, 0, 0));

			} else if (server.getUtilization() < 0.20
					&& server.getUtilization() > 0) {
				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(251, 255, 150));
			} else if (server.getUtilization() > 0.80) {
				(rGUI.getComponentByName("S" + server.getServerId()))
						.setBackground(new Color(0, 0, 0));
			}
		}
	}

	public void repaintFrameBtn(String btnNb, int value) {

		if (value >= 20 && value <= 30) {

			(rGUI.getComponentByName("S" + btnNb)).setBackground(new Color(255,
					255, 0));
		} else if (value > 30 && value <= 40) {

			(rGUI.getComponentByName("S" + btnNb)).setBackground(new Color(255,
					204, 0));

		} else if (value >= 40 && value <= 50) {
			(rGUI.getComponentByName("S" + btnNb)).setBackground(new Color(255,
					161, 21));
		} else if (value >= 50 && value <= 60) {
			(rGUI.getComponentByName("S" + btnNb)).setBackground(new Color(255,
					115, 21));

		} else if (value >= 60 && value <= 70) {
			(rGUI.getComponentByName("S" + btnNb)).setBackground(new Color(255,
					68, 0));

		} else if (value >= 70 && value <= 80) {
			(rGUI.getComponentByName("S" + btnNb)).setBackground(new Color(255,
					0, 0));

		} else {
			(rGUI.getComponentByName("S" + btnNb)).setBackground(new Color(251,
					255, 91));
		}

	}

}

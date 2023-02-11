package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This is the start-GUI for the BlackClient.
 * Here you have to enter the hostname and port to connect to the BlackServer
 * @author Rudi Wagner
 */
public class StartGUI {

	private JFrame frmStart;
	
	//Colours
		Color foregroundColor = new Color(136, 138, 145);
		Color backgroundColor = new Color(48, 49, 54);
		Color specialColor = new Color(55, 57, 63);
		
		private JTextField fieldHostname;
		private JTextField fieldPort;
		private JLabel errorLabel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartGUI window = new StartGUI(null);
					window.frmStart.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public StartGUI(BlackClient client) {
		initialize();
		frmStart.setVisible(true);
	}

	private void initialize() 
	{
		frmStart = new JFrame();
		frmStart.setIconImage(Toolkit.getDefaultToolkit().getImage(Gui.class.getResource("/resources/card_joker_black.png")));
		frmStart.setTitle("Blackjack Connection");
		frmStart.getContentPane().setLayout(null);
		frmStart.setResizable(false);
		frmStart.setSize(1010, 310);
		frmStart.getContentPane().setBackground(backgroundColor);
		frmStart.getContentPane().setForeground(foregroundColor);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBounds(10, 11, 974, 250);
		mainPanel.setBackground(backgroundColor);
		mainPanel.setForeground(foregroundColor);
		mainPanel.setLayout(null);
		frmStart.getContentPane().add(mainPanel);
		
		// Top Label //
		JLabel connectionLabel = new JLabel("Enter the hostname and port to connect to a server.");
		connectionLabel.setName("connectionlbl");
		connectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		connectionLabel.setBounds(10, 11, 944, 29);
		connectionLabel.setFont(new Font("Tahoma", Font.PLAIN, 32));
		connectionLabel.setForeground(foregroundColor);
		mainPanel.add(connectionLabel);
		
		// Port //
		JLabel portLabel = new JLabel("Port:");
		portLabel.setName("portLabel");
		portLabel.setBounds(524, 51, 200, 50);
		portLabel.setFont(new Font("Sitka Small", Font.PLAIN, 30));
		portLabel.setForeground(foregroundColor);
		mainPanel.add(portLabel);
		
		fieldPort = new JTextField();
		fieldPort.setName("fieldPort");
		fieldPort.setText("6868");
		fieldPort.setBounds(524, 89, 250, 50);
		fieldPort.setFont(new Font("Sitka Small", Font.PLAIN, 30));
		fieldPort.setForeground(foregroundColor);
		fieldPort.setBackground(specialColor);
		fieldPort.setColumns(10);
		mainPanel.add(fieldPort);
		
		// Hostname //
		JLabel hostnameLabel = new JLabel("Hostname:");
		hostnameLabel.setName("hostnameLabel");
		hostnameLabel.setBounds(191, 51, 200, 50);
		hostnameLabel.setFont(new Font("Sitka Small", Font.PLAIN, 30));
		hostnameLabel.setForeground(foregroundColor);
		mainPanel.add(hostnameLabel);
		
		fieldHostname = new JTextField();
		fieldHostname.setName("fieldHostname");
		fieldHostname.setText("127.0.0.1");
		fieldHostname.setBounds(191, 89, 250, 50);
		fieldHostname.setFont(new Font("Sitka Small", Font.PLAIN, 30));
		fieldHostname.setForeground(foregroundColor);
		fieldHostname.setBackground(specialColor);
		fieldHostname.setColumns(10);
		mainPanel.add(fieldHostname);
		
		// Submit-Button //
		
		JButton btnSubmit = new JButton("Connect");
		btnSubmit.setBounds(191, 178, 583, 50);
		btnSubmit.addActionListener(e -> connect());
		btnSubmit.setFont(new Font("Sitka Small", Font.PLAIN, 30));
		btnSubmit.setName("submitButton");
		btnSubmit.setForeground(foregroundColor);
		btnSubmit.setBackground(specialColor);
		mainPanel.add(btnSubmit);
		
		JDesktopPane desktopPane = new JDesktopPane();
		desktopPane.setBounds(521, 100, 1, 1);
		mainPanel.add(desktopPane);
		
		errorLabel = new JLabel("Server not reachable!");
		errorLabel.setName("errorLabel");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setForeground(Color.RED);
		errorLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorLabel.setBounds(10, 151, 944, 29);
		errorLabel.setVisible(false);
		mainPanel.add(errorLabel);
	}

	/**
	 * Connect method establishes a connection with the host specified by the user.
	 * If the hostname or port fields are blank, the method will return without taking any action.
	 * If a connection is successfully established, the start program is initialized with the specified port and hostname.
	 * In case of a NumberFormatException or an IOException, an error message is displayed and the start form remains visible.
	 */
	private void connect() 
	{
		String hostname = fieldHostname.getText();
		String port = fieldPort.getText();
		
		if (hostname.isBlank() || port.isBlank()) 
		{
			return;
		}
		
		try {
			//Test Connection
			Socket socket = new Socket(hostname, Integer.parseInt(port));
			OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println("closeSession");
            socket.close();
			
            //Start Program
			String[] args = {port, hostname};
			
			frmStart.setVisible(false);
			BlackClient.main(args);
			return;
		} catch (NumberFormatException | IOException e) 
		{
			errorLabel.setVisible(true);
			frmStart.setVisible(true);
		}
	}
}

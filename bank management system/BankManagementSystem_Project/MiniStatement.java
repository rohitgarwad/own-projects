package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MiniStatement extends JFrame implements ActionListener {
        
    JButton close, print;
    
    MiniStatement(String pinnumber){
        
        setTitle("Mini Statement");
        
        setLayout(null);
        
        JLabel mini = new JLabel();
        add(mini);
        
        JLabel bank = new JLabel("Indian Bank");
        bank.setBounds(150, 20, 100, 20);
        add(bank);

        JLabel card = new JLabel();
        card.setBounds(20, 80, 300, 20);
        add(card);
        
        JLabel balance = new JLabel();
        balance.setBounds(20, 400, 300, 20);
        add(balance);
        
        try {
            Conn conn = new Conn();
            ResultSet rs = conn.s.executeQuery("select * from login where pin = '"+pinnumber+"'");
            while(rs.next()) {
                card.setText("Card Number: " + rs.getString("cardnumber").substring(0, 4) + "XXXXXXXX" + rs.getString("cardnumber").substring(12));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        try {
            Conn conn = new Conn();
            int bal = 0;
            ResultSet rs = conn.s.executeQuery("select * from bank where pin = '"+pinnumber+"'");
            while(rs.next()) {
                mini.setText(mini.getText() + "<html>" + rs.getString("date") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + rs.getString("type") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + rs.getString("amount") + "<br><br><html>");
                if (rs.getString("type").equals("Deposit")) {
                    bal += Integer.parseInt(rs.getString("amount"));                        
                } else {
                    bal -= Integer.parseInt(rs.getString("amount"));
                }
            }
            balance.setText("Your current account balance is Rs " + bal);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        mini.setBounds(20, 140, 400, 200);
        
        close = new JButton("CLOSE");
        close.setBackground(Color.BLACK);
        close.setForeground(Color.WHITE);
        close.setBounds(200, 450, 100, 30);
        close.addActionListener(this);
        add(close);
        
        print = new JButton("PRINT");
        print.setBackground(Color.BLACK);
        print.setForeground(Color.WHITE);
        print.setBounds(100, 450, 100, 30);
        print.addActionListener(this);
        add(print);
        
        
        setSize(400, 600);
        setLocation(20, 20);
        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }
    
    private void captureComponent(Component component) {
		Rectangle rect = component.getBounds();
		
		try {
			String format = "png";
			String fileName = component.getName() + "." + format;
			BufferedImage captureImage = 
					new BufferedImage(rect.width, rect.height, 
										BufferedImage.TYPE_INT_ARGB);
			component.paint(captureImage.getGraphics());
			
			ImageIO.write(captureImage, format, new File(fileName));
			
                        JOptionPane.showMessageDialog(null, "Print Saved!");
			//System.out.printf("The screenshot of %s was saved!", component.getName());
		} catch (IOException ex) {
			System.err.println(ex);
		}		
	}
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == close) {
            setVisible(false);
        } else if (ae.getSource() == print) {
            //capture the frame
            captureComponent(this);
        }
    }
    
    public static void main(String args[]) {
        new MiniStatement("");
    }
}

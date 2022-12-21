package Client;

import java.awt.Component;
import java.awt.Font;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.awt.event.*;
import java.awt.*;

import javax.net.ssl.HostnameVerifier;
import javax.swing.*;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.ColorChooserComponentFactory;

public class Client {
  public static String Stringhost="";
    public static void main(String[] args) {
        File[] filetoSend = new File[1];

        JFrame jFrame = new JFrame("Client");
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setBounds(400,200, 500,450);
        jFrame.setResizable(false);

        JLabel title = new JLabel("Expediteur de Fichier");
        title.setFont(new Font("Serif",Font.BOLD, 25));
        title.setBounds(130,20, 350, 30);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

      final  JTextField host=new JTextField("localhost");
        host.setBounds(190, 80, 120, 30);
        JLabel labelHost=new JLabel("Host : ");
        labelHost.setFont(new Font("Serif",Font.BOLD, 18));
        labelHost.setBounds(150, 80, 120, 30);

        JButton connect=new JButton();
        connect.setFont(new Font("Serif", Font.BOLD, 18)); 
        connect.setText("<html> <color=white><b>Connect</b></font> </html>");
        connect.setBackground(Color.BLACK);
        connect.setBounds(320, 80, 120, 30);

        JLabel FileName = new JLabel("Choisissez un fichier a envoyer.");
        FileName.setFont(new Font("Arial", Font.BOLD, 20));
        FileName.setBounds(100,340, 350, 30);
        FileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton send = new JButton("Envoyer le fichier");
        send.setFont(new Font("Serif", Font.BOLD, 20));
        send.setBounds(150, 260 , 200, 50);

        JButton ChooseFile = new JButton("Choisir le fichier");
        ChooseFile.setFont(new Font("Serif", Font.BOLD, 20));
        ChooseFile.setBounds(150, 200 , 200, 50);
        
        JPanel panel=new JPanel();
        panel.setBounds(0,0,500,450);
        panel.setBackground(Color.GRAY);
        panel.setLayout(null);
        panel.add(connect);
        panel.add(host);
        panel.add(FileName);
        panel.add(labelHost);
        panel.add(send);
        panel.add(ChooseFile);
        panel.add(title);

        ChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFile = new JFileChooser();
                jFile.setDialogTitle("Choisissez un fichier a envoyer");

                if (jFile.showOpenDialog(null) == jFile.APPROVE_OPTION) {
                    filetoSend[0] = jFile.getSelectedFile();
                    FileName.setText("Le fichier que vous souhaitez envoyer : "+filetoSend[0].getName());
                }
            }
        });
        connect.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Stringhost=host.getText();
                System.out.println(Stringhost);
            }        
        });
        send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               
                if (filetoSend[0] == null) {
                    FileName.setText("Veuillez d'abord choisir un fichier");
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(filetoSend[0].getAbsolutePath());
                        Socket socket = new Socket(Stringhost, 1828);
                        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        String filename = filetoSend[0].getName();
                        byte[] filenameBytes = filename.getBytes();

                        byte[] fileContentBytes = new byte[(int)filetoSend[0].length()];
                        fileInputStream.read(fileContentBytes);

                        dataOutputStream.writeInt(filenameBytes.length);
                        dataOutputStream.write(filenameBytes);

                        dataOutputStream.writeInt(fileContentBytes.length);
                        dataOutputStream.write(fileContentBytes);
                    } catch (Exception error) {
                        error.printStackTrace();
                    }
                }
            }
        });

        jFrame.add(panel);
        jFrame.setVisible(true);
    }

}
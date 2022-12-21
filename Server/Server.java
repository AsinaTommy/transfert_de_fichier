package Server;

import java.awt.Component;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.*;

import javax.swing.*;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

import file.MyFile;

public class Server {
    static ArrayList<MyFile> myFiles = new ArrayList<>();

    public static void main(String[] args) {
        int fileId = 0;

        JFrame Frame = new JFrame("Server");
        Frame.setSize(500, 400);
        Frame.setLayout(new BoxLayout(Frame.getContentPane(), BoxLayout.Y_AXIS));
        Frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);

        JPanel Panel = new JPanel();
        Panel.setBackground(Color.gray);
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.X_AXIS));

        JScrollPane ScrollPane = new JScrollPane(Panel);
        ScrollPane.setVerticalScrollBarPolicy(ScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel Title = new JLabel("Fichier Recepteur");
        Title.setFont(new Font("Arial", Font.BOLD, 20));

        Frame.add(Title);
        Frame.add(ScrollPane);
        Frame.setVisible(true);

        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(1828);
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                int fileNameLength = dataInputStream.readInt();

                if (fileNameLength > 0) {
                    byte[] filenameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(filenameBytes, 0, filenameBytes.length);
                    String fileName = new String(filenameBytes);
                    int fileContentLength = dataInputStream.readInt();
                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentLength);

                        JPanel jpFileRow = new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

                        JLabel jlFileName = new JLabel(fileName);
                        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                        jlFileName.setBorder(new EmptyBorder(10, 0, 10, 0));

                        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                            jpFileRow.setName(String.valueOf(fileId));
                            jpFileRow.addMouseListener(getMyMouseListener());

                            jpFileRow.add(jlFileName);
                            Panel.add(jpFileRow);
                            Frame.validate();
                        } else {
                            jpFileRow.setName(String.valueOf(fileId));
                            jpFileRow.addMouseListener(getMyMouseListener());

                            jpFileRow.add(jlFileName);
                            Panel.add(jpFileRow);

                            Frame.validate();
                        }

                        myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                    }
                }
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {
        JFrame Frame = new JFrame("Telechargement de fichier");
        Frame.setSize(550,400);

        JPanel Panel = new JPanel();
        Panel.setLayout(new BoxLayout(Panel, BoxLayout.Y_AXIS));
      
        JLabel Title = new JLabel("Telechargement de fichier");
        Title.setAlignmentX(Component.CENTER_ALIGNMENT);
        Title.setFont(new Font("Arial", Font.BOLD, 25));
        Title.setBorder(new EmptyBorder(20, 0, 10, 0));
      
        JLabel jlPrompt = new JLabel("Etes vous sure de vouloir telecharger "+fileName);
        jlPrompt.setFont(new Font("Arial", Font.BOLD, 25));
        jlPrompt.setBorder(new EmptyBorder(90, 0, 10, 0));
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
      
        JButton jbYes = new JButton("Oui");
        jbYes.setPreferredSize(new Dimension(150, 75));
        jbYes.setFont(new Font("Arial", Font.BOLD, 25));
      
        JButton jbNo = new JButton("No");
        jbNo.setPreferredSize(new Dimension(150, 75));
        jbNo.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel jlFileContent = new JLabel();
        jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel jpButtons = new JPanel();
        jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));
        jpButtons.add(jbYes);
        jpButtons.add(jbNo);

        if (fileExtension.equalsIgnoreCase("txt")) {
            jlFileContent.setText("<html>" + new String(fileData)+"</html>");
        } else {
            jlFileContent.setIcon(new ImageIcon(fileData));
        }

        jbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileToDownloand = new File(fileName);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownloand);

                    fileOutputStream.write(fileData);
                    fileOutputStream.close();

                    Frame.dispose();
                } catch (Exception erorr) {
                    erorr.printStackTrace();
                }
            }
        });
        jbNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Frame.dispose();
            }
        });
        Panel.add(Title);
        Panel.add(jlPrompt);
        Panel.add(jlFileContent);
        Panel.add(jpButtons);
        Frame.add(Panel);
        return Frame;
    }
    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override 
            public void mouseClicked(MouseEvent e) {

                JPanel Panel = (JPanel) e.getSource();

                int fileId = Integer.parseInt(Panel.getName());

                for (MyFile myFile: myFiles) {
                    if (myFile.getId() == fileId) {
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileextension());
                        jfPreview.setVisible(true);
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        };
    }

    public static String getFileExtension(String filename) {
        int i = filename.lastIndexOf('.');
        
        if (i>0) {
            return filename.substring(i + 1);
        } else {
            return "No extension found";
        }
    }
}
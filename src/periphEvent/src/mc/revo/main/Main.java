package mc.revo.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Main {
    public static void main(String[] args) {
    	//créer un lock sur le fichier
        final File file = new File("/tmp/sysevent.lock");
        try {
            final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            final FileChannel fileChannel = randomAccessFile.getChannel();
            final FileLock fileLock = fileChannel.tryLock();
            if (fileLock != null) {
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        try {
                            fileLock.release();
                            randomAccessFile.close();
                            file.delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }));
            } else {
                System.out.println("Another instance of the app is already running. Exiting.");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Création de la fenêtre
        JFrame frame = new JFrame("new module enabled");
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 200);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        // Création du label pour le texte
        JLabel label = new JLabel("new ms/kb enabled");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // Création du bouton
        JButton button = new JButton("Fermer");
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 20));

        // Ajout du label et du bouton à la fenêtre
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        frame.add(label, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        frame.add(button, constraints);
        
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });

        // Afficher la fenêtre
        frame.setVisible(true);
    }
}
package cz.polankam.darkener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class ScreenDarkener {

   private static class Darkener extends JWindow {

      public Darkener() {
         super();

         JButton button = new JButton("Click to Exit");
         button.setForeground(new Color(30, 30, 30));
         button.setOpaque(false);
         button.setContentAreaFilled(false);
         button.setBorder(BorderFactory.createEmptyBorder());
         button.addActionListener(arg -> System.exit(0));
         add(button, BorderLayout.CENTER);
         setAlwaysOnTop(true);
      }

      public void darken(GraphicsDevice device) {
         Rectangle bounds = device.getDefaultConfiguration().getBounds();
         setLocation(bounds.getLocation());
         setSize(bounds.getSize());
         getContentPane().setBackground(Color.black);
         validate();
         setVisible(true);
      }
   }

   private static class Selector {

      private final JFrame frame;
      private final Set<GraphicsDevice> selectedDevices = new HashSet<>();

      public Selector() {
         frame = new JFrame("Screen Darkener Selection");
         JPanel panel = new JPanel();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.add(panel);
         panel.setBorder(new EmptyBorder(10, 10, 10, 10));
         panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

         for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            JCheckBox check = new JCheckBox(device.getIDstring() + 
               " " + device.getDefaultConfiguration().getBounds());
            check.addItemListener(event -> {
               if (event.getStateChange() == ItemEvent.SELECTED) {
                  selectedDevices.add(device);
               } else if (event.getStateChange() == ItemEvent.DESELECTED) {
                  selectedDevices.remove(device);
               }
            });
            panel.add(check);
         }

         JButton button = new JButton("DARKEN");
         button.addActionListener(event -> darken());
         panel.add(button);
      }

      private void run() {
         frame.pack();
         frame.setLocationRelativeTo(null);
         frame.setVisible(true);
      }

      private void darken() {
         if (selectedDevices.isEmpty()) {
            JOptionPane.showMessageDialog(null, "None of the screens selected!");
            return;
         }

         frame.setVisible(false);
         for (GraphicsDevice device : selectedDevices) {
            new Darkener().darken(device);
         }
      }
   }

   public static void main(String[] args) throws Exception {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      new Selector().run();
   }
}

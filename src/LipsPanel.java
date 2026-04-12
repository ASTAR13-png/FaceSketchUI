import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class LipsPanel extends JPanel {

    public interface LipsSelectListener {
        void onLipsSelected(ImageIcon lips);
    }

    private LipsSelectListener listener;

    public LipsPanel(LipsSelectListener listener) {

        this.listener = listener;

        setLayout(new GridLayout(0, 2, 10, 10));
        setBackground(new Color(10, 53, 88));

        File folder = new File("assets/lips");

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (File file : files) {

                ImageIcon fullIcon = new ImageIcon(file.getAbsolutePath());

                Image scaled = fullIcon.getImage()
                        .getScaledInstance(100, 50, Image.SCALE_SMOOTH);

                JLabel label = new JLabel(new ImageIcon(scaled));
                label.setHorizontalAlignment(SwingConstants.CENTER);

                label.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        listener.onLipsSelected(fullIcon);
                    }
                });

                add(label);
            }
        }
    }
}
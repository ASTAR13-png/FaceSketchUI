import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class NosePanel extends JPanel {

    public interface NoseSelectListener {
        void onNoseSelected(ImageIcon nose);
    }

    private NoseSelectListener listener;

    public NosePanel(NoseSelectListener listener) {

        this.listener = listener;

        setLayout(new GridLayout(0, 2, 10, 10));
        setBackground(new Color(10, 53, 88));

        File folder = new File("assets/nose");

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (File file : files) {

                ImageIcon fullIcon = new ImageIcon(file.getAbsolutePath());

                Image scaled = fullIcon.getImage()
                        .getScaledInstance(80, 100, Image.SCALE_SMOOTH);

                JLabel label = new JLabel(new ImageIcon(scaled));
                label.setHorizontalAlignment(SwingConstants.CENTER);

                label.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        listener.onNoseSelected(fullIcon);
                    }
                });

                add(label);
            }
        }
    }
}
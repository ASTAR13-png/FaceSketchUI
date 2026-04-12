import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class HairPanel extends JPanel {

    public interface HairSelectListener {
        void onHairSelected(ImageIcon hair);
    }

    private HairSelectListener listener;

    public HairPanel(HairSelectListener listener) {

        this.listener = listener;

        setLayout(new GridLayout(0, 2, 10, 10));
        setBackground(new Color(10, 53, 88));

        File folder = new File("assets/hair");

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (File file : files) {

                ImageIcon fullIcon = new ImageIcon(file.getAbsolutePath());

                Image scaled = fullIcon.getImage()
                        .getScaledInstance(120, 120, Image.SCALE_SMOOTH);

                JLabel label = new JLabel(new ImageIcon(scaled));
                label.setHorizontalAlignment(SwingConstants.CENTER);

                label.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        listener.onHairSelected(fullIcon);
                    }
                });

                add(label);
            }
        }
    }
}
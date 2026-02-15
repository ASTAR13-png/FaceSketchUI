import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class HeadPanel extends JPanel {

    public interface HeadSelectListener {
        void onHeadSelected(ImageIcon head);
    }

    private HeadSelectListener listener;

    public HeadPanel(HeadSelectListener listener) {

        this.listener = listener;

        setLayout(new GridLayout(0, 2, 10, 10));
        setBackground(new Color(10, 53, 88));

        File folder = new File("assets");
        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".png")
        );

        if (files != null) {
            for (File file : files) {

                ImageIcon fullIcon = new ImageIcon(file.getAbsolutePath());
                Image scaled = fullIcon.getImage()
                        .getScaledInstance(100, 120, Image.SCALE_SMOOTH);

                ImageIcon thumb = new ImageIcon(scaled);

                JLabel label = new JLabel(thumb);
                label.setHorizontalAlignment(SwingConstants.CENTER);

                label.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        listener.onHeadSelected(fullIcon);
                    }
                });

                add(label);
            }
        }
    }
}

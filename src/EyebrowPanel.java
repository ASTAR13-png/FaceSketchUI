import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class EyebrowPanel extends JPanel {

    // Interface to send selected eyebrow back to main UI
    public interface EyebrowSelectListener {
        void onEyebrowSelected(ImageIcon eyebrow);
    }

    private EyebrowSelectListener listener;

    public EyebrowPanel(EyebrowSelectListener listener) {

        this.listener = listener;

        setLayout(new BorderLayout());
        setBackground(new Color(10, 53, 88));

        // TITLE
        JLabel title = new JLabel("EYEBROWS", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        // GRID PANEL
        JPanel grid = new JPanel(new GridLayout(4, 2, 10, 10));
        grid.setBackground(new Color(10, 53, 88));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        loadEyebrowImages(grid);

        add(grid, BorderLayout.CENTER);
    }

    // LOAD IMAGES FROM assets/eyebrows
    private void loadEyebrowImages(JPanel grid) {

        File folder = new File("assets/eyebrows");

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (File file : files) {

                ImageIcon fullIcon = new ImageIcon(file.getAbsolutePath());

                // Resize thumbnail
                Image scaled = fullIcon.getImage()
                        .getScaledInstance(100, 40, Image.SCALE_SMOOTH);

                ImageIcon thumbIcon = new ImageIcon(scaled);

                JLabel thumb = new JLabel(thumbIcon);
                thumb.setHorizontalAlignment(SwingConstants.CENTER);
                thumb.setBorder(BorderFactory.createLineBorder(Color.WHITE));

                // CLICK EVENT
                thumb.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        listener.onEyebrowSelected(fullIcon);
                    }
                });

                grid.add(thumb);
            }
        } else {
            System.out.println("Eyebrows folder not found!");
        }
    }
}
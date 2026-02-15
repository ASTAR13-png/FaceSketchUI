import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class FaceSketchUI implements
        HeadPanel.HeadSelectListener,
        EyePanel.EyeSelectListener {

    private JLabel canvasImage;
    private JLabel eyeImage;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FaceSketchUI().createUI());
    }

    public void createUI() {

        JFrame frame = new JFrame("Face Sketch Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ================= LEFT PANEL =================
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(120, 0));
        leftPanel.setBackground(new Color(10, 53, 88));
        leftPanel.setLayout(new GridLayout(9, 1, 5, 5));

        String[] tools = {"Head", "Hair", "Eyes", "Eyebrows", "Nose", "Lips", "Mustache", "More"};

        // ================= RIGHT PANEL CENTER (Switching Panels) =================
        JPanel rightPanelCenter = new JPanel(new BorderLayout());

        HeadPanel headPanel = new HeadPanel(this);
        EyePanel eyePanel = new EyePanel(this);

        // Default panel
        rightPanelCenter.add(headPanel, BorderLayout.CENTER);

        for (String tool : tools) {
            JButton btn = new JButton(tool);

            btn.addActionListener(e -> {
                rightPanelCenter.removeAll();

                if (tool.equals("Head")) {
                    rightPanelCenter.add(headPanel, BorderLayout.CENTER);
                } else if (tool.equals("Eyes")) {
                    rightPanelCenter.add(eyePanel, BorderLayout.CENTER);
                }

                rightPanelCenter.revalidate();
                rightPanelCenter.repaint();
            });

            leftPanel.add(btn);
        }

        // ================= CENTER CANVAS =================
       JLayeredPane canvas = new JLayeredPane();
canvas.setPreferredSize(new Dimension(800, 700));
canvas.setBackground(Color.WHITE);
canvas.setOpaque(true);


        canvasImage = new JLabel();
canvasImage.setBounds(400, 150, 300, 350);
canvas.add(canvasImage, Integer.valueOf(1)); // Layer 1

eyeImage = new JLabel();
canvas.add(eyeImage, Integer.valueOf(2));    // Layer 2 (above head)


        // ================= RIGHT PANEL =================
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(350, 0));
        rightPanel.setBackground(new Color(10, 53, 88));

        JPanel topButtons = new JPanel();
        topButtons.setBackground(new Color(10, 53, 88));
        topButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        JButton saveBtn = new JButton("SAVE");
        JButton resetBtn = new JButton("RESET");
        JButton compareBtn = new JButton("COMPARE");
        JButton deleteBtn = new JButton("DELETE");

        topButtons.add(saveBtn);
        topButtons.add(resetBtn);
        topButtons.add(deleteBtn);
        topButtons.add(compareBtn);

        // Reset button
        resetBtn.addActionListener(e -> {
            canvasImage.setIcon(null);
            eyeImage.setIcon(null);
        });

        // Delete button
        deleteBtn.addActionListener(e -> {
            canvasImage.setIcon(null);
            eyeImage.setIcon(null);
        });

        // Save button
        saveBtn.addActionListener(e -> saveCanvas(canvas));

        // Compare placeholder
        compareBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(null, "Compare function coming soon!")
        );

        rightPanel.add(topButtons, BorderLayout.NORTH);
        rightPanel.add(rightPanelCenter, BorderLayout.CENTER);

        // ================= ADD TO FRAME =================
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.setVisible(true);
    }

    // ================= HEAD SELECTION =================
    @Override
public void onHeadSelected(ImageIcon headIcon) {

    Image resized = headIcon.getImage()
            .getScaledInstance(300, 350, Image.SCALE_SMOOTH);

    canvasImage.setIcon(new ImageIcon(resized));
    canvasImage.setBounds(400, 150, 300, 350);

    // If eyes already selected, reposition them
    if (eyeImage.getIcon() != null) {
        Rectangle headBounds = canvasImage.getBounds();

        int eyeX = headBounds.x + 60;
        int eyeY = headBounds.y + 100;

        eyeImage.setBounds(eyeX, eyeY, 180, 60);
    }
}


    // ================= EYE SELECTION =================
@Override
public void onEyeSelected(ImageIcon eyeIcon) {

    if (canvasImage.getIcon() == null) {
        JOptionPane.showMessageDialog(null, "Select a head first!");
        return;
    }

    Image resized = eyeIcon.getImage()
            .getScaledInstance(240, 90, Image.SCALE_SMOOTH);

    eyeImage.setIcon(new ImageIcon(resized));

    Rectangle headBounds = canvasImage.getBounds();

    int eyeX = headBounds.x + 35;
    int eyeY = headBounds.y + 90;

    eyeImage.setBounds(eyeX, eyeY, 240, 90);
}

    // ================= SAVE CANVAS =================
    private void saveCanvas(JComponent canvas) {

    BufferedImage img = new BufferedImage(
            canvas.getWidth(),
            canvas.getHeight(),
            BufferedImage.TYPE_INT_RGB
    );

    Graphics2D g2 = img.createGraphics();
    canvas.paint(g2);
    g2.dispose();

    try {
        File dir = new File("saved");
        if (!dir.exists()) dir.mkdir();

        File out = new File(dir,
                "sketch_" + System.currentTimeMillis() + ".png");

        ImageIO.write(img, "png", out);
        JOptionPane.showMessageDialog(null, "Saved Successfully!");
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

        }

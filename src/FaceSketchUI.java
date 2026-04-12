import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class FaceSketchUI implements
        HeadPanel.HeadSelectListener,
        EyePanel.EyeSelectListener,
        EyebrowPanel.EyebrowSelectListener,
        NosePanel.NoseSelectListener,
        LipsPanel.LipsSelectListener {

    private JLabel headLabel, eyeLabel, browLabel, noseLabel, lipsLabel;

    // Default sizes
    private int headW = 300, headH = 350;
    private int eyeW = 180, eyeH = 60;
    private int browW = 200, browH = 50;
    private int noseW = 80, noseH = 120;
    private int lipsW = 120, lipsH = 50;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FaceSketchUI().createUI());
    }

    public void createUI() {

        JFrame frame = new JFrame("Face Sketch Builder");
        frame.setSize(1400, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // LEFT PANEL
        JPanel left = new JPanel(new GridLayout(10, 1));
        left.setPreferredSize(new Dimension(120, 0));

        String[] tools = { "Head", "Eyes", "Eyebrows", "Nose", "Lips" };

        JPanel rightCenter = new JPanel(new BorderLayout());

        HeadPanel headPanel = new HeadPanel(this);
        EyePanel eyePanel = new EyePanel(this);
        EyebrowPanel browPanel = new EyebrowPanel(this);
        NosePanel nosePanel = new NosePanel(this);
        LipsPanel lipsPanel = new LipsPanel(this);

        rightCenter.add(headPanel);

        for (String t : tools) {
            JButton btn = new JButton(t);
            btn.addActionListener(e -> {
                rightCenter.removeAll();

                switch (t) {
                    case "Head":
                        rightCenter.add(headPanel);
                        break;
                    case "Eyes":
                        rightCenter.add(eyePanel);
                        break;
                    case "Eyebrows":
                        rightCenter.add(browPanel);
                        break;
                    case "Nose":
                        rightCenter.add(nosePanel);
                        break;
                    case "Lips":
                        rightCenter.add(lipsPanel);
                        break;
                }

                rightCenter.revalidate();
                rightCenter.repaint();
            });
            left.add(btn);
        }

        // ================= CANVAS =================
        JLayeredPane canvas = new JLayeredPane();
        canvas.setPreferredSize(new Dimension(800, 700));

        // LAYERS (IMPORTANT)
        headLabel = new JLabel(); // bottom
        lipsLabel = new JLabel();
        noseLabel = new JLabel();
        eyeLabel = new JLabel();
        browLabel = new JLabel(); // top

        // ADD IN CORRECT ORDER
        canvas.add(headLabel, Integer.valueOf(1));
        canvas.add(lipsLabel, Integer.valueOf(2));
        canvas.add(noseLabel, Integer.valueOf(3));
        canvas.add(eyeLabel, Integer.valueOf(4));
        canvas.add(browLabel, Integer.valueOf(5));

        headLabel.setBounds(400, 150, headW, headH);

        // DRAG + RESIZE
        enableInteraction(eyeLabel);
        enableInteraction(browLabel);
        enableInteraction(noseLabel);
        enableInteraction(lipsLabel);

        // RIGHT PANEL
        JPanel right = new JPanel(new BorderLayout());

        JButton save = new JButton("SAVE");
        JButton reset = new JButton("RESET");

        JPanel top = new JPanel();
        top.add(save);
        top.add(reset);

        reset.addActionListener(e -> {
            headLabel.setIcon(null);
            eyeLabel.setIcon(null);
            browLabel.setIcon(null);
            noseLabel.setIcon(null);
            lipsLabel.setIcon(null);
        });

        save.addActionListener(e -> saveCanvas(canvas));

        right.add(top, BorderLayout.NORTH);
        right.add(rightCenter, BorderLayout.CENTER);

        frame.add(left, BorderLayout.WEST);
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(right, BorderLayout.EAST);

        frame.setVisible(true);
    }

    // ================= DRAG + RESIZE =================
    private void enableInteraction(JLabel label) {

        final Point[] offset = new Point[1];

        label.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                offset[0] = e.getPoint();
            }
        });

        label.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = label.getX() + e.getX() - offset[0].x;
                int y = label.getY() + e.getY() - offset[0].y;
                label.setLocation(x, y);
            }
        });

        // Scroll to resize
        label.addMouseWheelListener(e -> {
            if (label.getIcon() == null)
                return;

            int w = label.getWidth();
            int h = label.getHeight();

            if (e.getWheelRotation() < 0) {
                w += 10;
                h += 5;
            } else {
                w -= 10;
                h -= 5;
            }

            if (w < 30 || h < 20)
                return;

            ImageIcon icon = (ImageIcon) label.getIcon();
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
            label.setSize(w, h);
        });
    }

    // ================= FEATURE METHODS =================
    public void onHeadSelected(ImageIcon icon) {
        Image img = icon.getImage().getScaledInstance(headW, headH, Image.SCALE_SMOOTH);
        headLabel.setIcon(new ImageIcon(img));
        updatePositions();
    }

    public void onEyeSelected(ImageIcon icon) {
        eyeLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(eyeW, eyeH, Image.SCALE_SMOOTH)));
        updatePositions();
    }

    public void onEyebrowSelected(ImageIcon icon) {
        browLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(browW, browH, Image.SCALE_SMOOTH)));
        updatePositions();
    }

    public void onNoseSelected(ImageIcon icon) {
        noseLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(noseW, noseH, Image.SCALE_SMOOTH)));
        updatePositions();
    }

    public void onLipsSelected(ImageIcon icon) {
        lipsLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(lipsW, lipsH, Image.SCALE_SMOOTH)));
        updatePositions();
    }

    // ================= AUTO ALIGN =================
    private void updatePositions() {

        Rectangle head = headLabel.getBounds();
        int cx = head.x + head.width / 2;

        eyeLabel.setBounds(cx - eyeW / 2, head.y + (int) (head.height * 0.35), eyeW, eyeH);
        browLabel.setBounds(cx - browW / 2, head.y + (int) (head.height * 0.25), browW, browH);
        noseLabel.setBounds(cx - noseW / 2, head.y + (int) (head.height * 0.45), noseW, noseH);
        lipsLabel.setBounds(cx - lipsW / 2, head.y + (int) (head.height * 0.65), lipsW, lipsH);
    }

    // ================= SAVE =================
    private void saveCanvas(JComponent canvas) {

        BufferedImage img = new BufferedImage(
                canvas.getWidth(),
                canvas.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = img.createGraphics();
        canvas.paint(g2);
        g2.dispose();

        try {
            File out = new File("sketch.png");
            ImageIO.write(img, "png", out);
            JOptionPane.showMessageDialog(null, "Saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
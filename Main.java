import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 在事件调度线程中创建和显示GUI
        SwingUtilities.invokeLater(() -> {
            RaceFrame frame = new RaceFrame();
            frame.setVisible(true);
        });
    }
}
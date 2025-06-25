import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RaceFrame extends JFrame {
    private JTextField horseCountField = new JTextField("5", 5);
    private JTextField raceTimeField = new JTextField("15", 5);
    private JButton startBtn = new JButton("开始比赛");
    private RacePanel racePanel = new RacePanel();

    public RaceFrame() {
        setTitle("赛马模拟游戏");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800, 500);
        setLocationRelativeTo(null);

        // 顶部控制面板
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.add(new JLabel("马匹数量：", SwingConstants.RIGHT));
        horseCountField.setFont(new Font("SimSun", Font.PLAIN, 14));
        topPanel.add(horseCountField);
        topPanel.add(new JLabel("比赛时间（秒）：", SwingConstants.RIGHT));
        raceTimeField.setFont(new Font("SimSun", Font.PLAIN, 14));
        topPanel.add(raceTimeField);

        // 按钮样式（绿色 + 中文字体）
        startBtn.setFont(new Font("SimSun", Font.BOLD, 14));
        startBtn.setBackground(new Color(50, 205, 50));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        topPanel.add(startBtn);

        // 绑定事件
        startBtn.addActionListener(this::startRace);

        // 添加组件
        add(topPanel, BorderLayout.NORTH);
        add(racePanel, BorderLayout.CENTER);
    }

    // 开始比赛逻辑（输入验证）
    private void startRace(ActionEvent e) {
        try {
            int horseCount = Integer.parseInt(horseCountField.getText());
            int raceTime = Integer.parseInt(raceTimeField.getText());

            // 简单验证
            if (horseCount < 1 || horseCount > 10) {
                JOptionPane.showMessageDialog(this, "马匹数量应在 1~10 之间！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (raceTime < 5 || raceTime > 30) {
                JOptionPane.showMessageDialog(this, "比赛时间应在 5~30 秒之间！", "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 提示比赛开始
            JOptionPane.showMessageDialog(this, "比赛即将开始！准备观赏精彩赛事～", "赛前提示", JOptionPane.INFORMATION_MESSAGE);
            racePanel.startRace(horseCount, raceTime);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "请输入有效数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RacePanel extends JPanel {
    private List<Horse> horses = new ArrayList<>();
    private int finishLine = 700; // 初始终点线，会随窗口缩放调整
    private boolean running = false;
    private Timer spectatorTimer; // 观众动画定时器

    // 初始化观众位置（简化版，用数组存储）
    private List<Point> spectators = new ArrayList<>();
    private Random rand = new Random();

    public RacePanel() {
        // 预生成观众位置（底部区域）
        for (int i = 0; i < 30; i++) {
            spectators.add(new Point(rand.nextInt(800), 350 + rand.nextInt(50)));
        }
    }

    // 开始比赛
    public void startRace(int horseCount, int raceTime) {
        horses.clear();
        running = true;

        // 动态计算终点线（窗口宽度的 80%）
        finishLine = (int) (this.getWidth() * 0.8);

        // 创建马匹（按赛道垂直分布）
        for (int i = 0; i < horseCount; i++) {
            horses.add(new Horse("马" + (i + 1), 50 + i * 40, finishLine, raceTime, this));
        }

        repaint(); // 重绘初始状态

        // 启动马匹线程
        for (Horse horse : horses) {
            new Thread(horse).start();
        }

        // 启动观众动画（降低速率：500ms/次）
        spectatorTimer = new Timer();
        spectatorTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateSpectators();
                repaint();
            }
        }, 0, 500);

        // 比赛计时
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                running = false;
                spectatorTimer.cancel(); // 停止观众动画
                repaint();
                showResult();
            }
        }, raceTime * 1000L);
    }

    // 控制比赛状态
    public boolean isRunning() {
        return running;
    }

    // 重绘逻辑（赛道、终点线、马匹、观众）
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. 背景（渐变天空）
        GradientPaint skyGrad = new GradientPaint(0, 0, new Color(135, 206, 250), 0, getHeight(), new Color(255, 240, 200));
        g2.setPaint(skyGrad);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // 2. 赛道（灰色跑道 + 黄色虚线）
        g2.setColor(new Color(100, 100, 100));
        g2.fillRect(0, 40, getWidth(), getHeight() - 80);

        // 赛道虚线（横向）
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{10, 10}, 0));
        for (int y = 60; y < getHeight() - 60; y += 40) {
            g2.drawLine(0, y, getWidth(), y);
        }

        // 3. 终点线（红色实线 + 文字）
        g2.setColor(Color.RED);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(finishLine, 40, finishLine, getHeight() - 80);

        // 终点线文字（中文字体）
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SimSun", Font.BOLD, 16));
        g2.drawString("终点线", finishLine - 40, 30);

        // 4. 绘制马匹
        for (Horse horse : horses) {
            horse.draw(g2);
        }

        // 5. 绘制观众（简笔小人 + 动态偏移）
        g2.setColor(Color.BLACK);
        for (Point p : spectators) {
            // 头部
            g2.fillOval(p.x, p.y, 8, 8);
            // 身体
            g2.drawLine(p.x + 4, p.y + 8, p.x + 4, p.y + 15);
            // 手臂
            g2.drawLine(p.x + 4, p.y + 10, p.x, p.y + 14);
            g2.drawLine(p.x + 4, p.y + 10, p.x + 8, p.y + 14);
            // 腿
            g2.drawLine(p.x + 4, p.y + 15, p.x, p.y + 20);
            g2.drawLine(p.x + 4, p.y + 15, p.x + 8, p.y + 20);
        }
    }

    // 观众动画：随机偏移（降低速率）
    private void updateSpectators() {
        for (Point p : spectators) {
            p.x += rand.nextInt(3) - 1; // 左右小幅度移动
            p.y += rand.nextInt(2) - 0; // 上下小幅度移动
            // 边界检测（限制在底部区域）
            p.x = Math.max(10, Math.min(getWidth() - 10, p.x));
            p.y = Math.max(350, Math.min(getHeight() - 30, p.y));
        }
    }

    // 比赛结果弹窗
    private void showResult() {
        horses.sort(Comparator.comparingInt(h -> -h.getDistance()));
        StringBuilder sb = new StringBuilder("比赛结束！排行榜如下：\n\n");
        String[] medals = {"🥇", "🥈", "🥉", "🏃", "🏃"}; // 前3名用奖牌

        for (int i = 0; i < horses.size(); i++) {
            sb.append(medals[i]).append(" ")
                    .append(i + 1).append("、")
                    .append(horses.get(i).getName())
                    .append(" 跑了 ").append(horses.get(i).getDistance()).append(" 像素\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "比赛结果", JOptionPane.INFORMATION_MESSAGE);
    }
}
import java.awt.*;
import java.awt.geom.*;
import java.util.Random;

public class Horse implements Runnable {
    private String name;        // 马匹名称
    private int y;              // 垂直位置（赛道 lane）
    private int x = 0;          // 水平位置（已跑距离）
    private int finishLine;     // 终点线 X 坐标
    private int raceTime;       // 比赛时长（秒）
    private RacePanel panel;    // 关联面板，用于刷新
    private Random rand = new Random();
    private int step = 0;       // 动画步数（控制腿部运动）

    // 颜色配置（让每匹马颜色不同）
    private Color bodyColor;
    private Color maneColor;

    public Horse(String name, int y, int finishLine, int raceTime, RacePanel panel) {
        this.name = name;
        this.y = y;
        this.finishLine = finishLine;
        this.raceTime = raceTime;
        this.panel = panel;

        // 随机生成马匹颜色（棕色系）
        bodyColor = new Color(139 + rand.nextInt(100), 69 + rand.nextInt(50), 19 + rand.nextInt(30));
        maneColor = new Color(bodyColor.getRed() - 20, bodyColor.getGreen() - 10, bodyColor.getBlue());
    }

    // Getter
    public String getName() { return name; }
    public int getDistance() { return x; }

    // 绘制马匹（细化图形：身体、头部、四肢、鬃毛）
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. 身体（渐变椭圆）
        GradientPaint bodyGrad = new GradientPaint(x, y, bodyColor.brighter(), x + 40, y + 20, bodyColor);
        g2.setPaint(bodyGrad);
        g2.fill(new Ellipse2D.Double(x, y, 40, 20));

        // 2. 头部（椭圆 + 细节）
        g2.setColor(bodyColor);
        g2.fillOval(x + 40, y + 5, 25, 15); // 头部椭圆

        // 眼睛
        g2.setColor(Color.WHITE);
        g2.fillOval(x + 52, y + 8, 4, 4);
        g2.setColor(Color.BLACK);
        g2.fillOval(x + 54, y + 9, 2, 2);

        // 3. 鬃毛（多边形）
        g2.setColor(maneColor);
        int[] maneX = {x + 40, x + 45, x + 50, x + 55};
        int[] maneY = {y + 3, y - 2, y - 4, y + 1};
        g2.fillPolygon(maneX, maneY, 4);

        // 4. 动态腿部（交替运动）
        g2.setColor(new Color(80, 60, 40)); // 腿部颜色
        if (step % 4 < 2) {
            // 前腿抬起，后腿落地
            g2.draw(new Line2D.Double(x + 15, y + 20, x + 15, y + 35)); // 左前腿
            g2.draw(new Line2D.Double(x + 25, y + 20, x + 25, y + 30)); // 右前腿
            g2.draw(new Line2D.Double(x + 5,  y + 20, x + 5,  y + 30)); // 左后腿
            g2.draw(new Line2D.Double(x + 35, y + 20, x + 35, y + 35)); // 右后腿
        } else {
            // 前腿落地，后腿抬起
            g2.draw(new Line2D.Double(x + 15, y + 20, x + 15, y + 30)); // 左前腿
            g2.draw(new Line2D.Double(x + 25, y + 20, x + 25, y + 35)); // 右前腿
            g2.draw(new Line2D.Double(x + 5,  y + 20, x + 5,  y + 35)); // 左后腿
            g2.draw(new Line2D.Double(x + 35, y + 20, x + 35, y + 30)); // 右后腿
        }

        // 5. 尾巴（曲线 + 随机飘动）
        g2.setColor(maneColor);
        g2.setStroke(new BasicStroke(2));
        int tailOffset = rand.nextInt(6) - 3; // 随机偏移
        g2.draw(new QuadCurve2D.Double(x, y + 10, x - 10 + tailOffset, y + 5 + tailOffset, x - 20 + tailOffset, y + 10));

        // 6. 名称（中文字体）
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("SimSun", Font.BOLD, 12));
        g2.drawString(name, x + 70, y + 15);

        step++; // 步数更新
    }

    // 线程逻辑：控制移动
    @Override
    public void run() {
        long endTime = System.currentTimeMillis() + raceTime * 1000L;
        while (System.currentTimeMillis() < endTime && panel.isRunning() && x < finishLine) {
            x += rand.nextInt(10) + 1; // 速度：1~10像素/次
            panel.repaint(); // 触发重绘
            try {
                Thread.sleep(100 + rand.nextInt(200)); // 控制动画频率
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
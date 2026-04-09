import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class GraphGUI extends JFrame {

    private Graph graph;
    private GraphPanel graphPanel;
    private JComboBox<String> sourceCombo, destCombo, algoCombo;
    private JTextArea resultArea;
    private List<String> highlightPath = new ArrayList<>();
    private String selectedAlgo = "Dijkstra";

    // Color palette
    static final Color BG_DARK      = new Color(15, 20, 40);
    static final Color BG_PANEL     = new Color(22, 30, 55);
    static final Color ACCENT_BLUE  = new Color(64, 156, 255);
    static final Color ACCENT_GREEN = new Color(50, 220, 130);
    static final Color ACCENT_GOLD  = new Color(255, 200, 50);
    static final Color ACCENT_RED   = new Color(255, 90, 90);
    static final Color TEXT_MAIN    = new Color(220, 230, 255);
    static final Color TEXT_DIM     = new Color(130, 150, 190);
    static final Color NODE_DEFAULT = new Color(60, 80, 140);
    static final Color NODE_PATH    = new Color(255, 200, 50);
    static final Color NODE_START   = new Color(50, 220, 130);
    static final Color NODE_END     = new Color(255, 90, 90);
    static final Color EDGE_DEFAULT = new Color(50, 65, 110);
    static final Color EDGE_PATH    = new Color(255, 200, 50);

    public GraphGUI() {
        graph = new Graph();
        setTitle("CU Campus Route Finder — DAA Project");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout(10, 10));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildGraphPanel(), BorderLayout.CENTER);
        add(buildControlPanel(), BorderLayout.EAST);

        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_PANEL);
        header.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("🎓 Chandigarh University — Campus Route Finder");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ACCENT_BLUE);

        JLabel subtitle = new JLabel("Algorithms: Dijkstra  |  BFS  |  DFS  |  A*  — DAA Project");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_DIM);

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2));
        titles.setBackground(BG_PANEL);
        titles.add(title);
        titles.add(subtitle);
        header.add(titles, BorderLayout.WEST);

        JLabel legend = new JLabel(
            "<html><font color='#32DC82'>■ Start</font>  " +
            "<font color='#FF5A5A'>■ End</font>  " +
            "<font color='#FFC832'>■ Path Node</font>  " +
            "<font color='#3C5080'>■ Default</font></html>"
        );
        legend.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        header.add(legend, BorderLayout.EAST);

        return header;
    }

    private GraphPanel buildGraphPanel() {
        graphPanel = new GraphPanel();
        return graphPanel;
    }

    private JPanel buildControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_PANEL);
        panel.setBorder(new CompoundBorder(
            new EmptyBorder(10, 5, 10, 10),
            new CompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 70, 120), 1),
                new EmptyBorder(15, 15, 15, 15)
            )
        ));
        panel.setPreferredSize(new Dimension(280, 0));

        List<String> locations = new ArrayList<>(graph.getLocations());
        String[] locArray = locations.toArray(new String[0]);

        panel.add(sectionLabel("SOURCE LOCATION"));
        panel.add(Box.createVerticalStrut(5));
        sourceCombo = styledCombo(locArray);
        panel.add(sourceCombo);

        panel.add(Box.createVerticalStrut(12));
        panel.add(sectionLabel("DESTINATION"));
        panel.add(Box.createVerticalStrut(5));
        destCombo = styledCombo(locArray);
        destCombo.setSelectedIndex(Math.min(5, locArray.length - 1));
        panel.add(destCombo);

        panel.add(Box.createVerticalStrut(12));
        panel.add(sectionLabel("ALGORITHM"));
        panel.add(Box.createVerticalStrut(5));
        algoCombo = styledCombo(new String[]{
            "Dijkstra (Shortest Path)",
            "BFS (Fewest Hops)",
            "DFS (Depth-First)",
            "A* (Heuristic)"
        });
        panel.add(algoCombo);

        panel.add(Box.createVerticalStrut(18));
        JButton findBtn = styledButton("▶  FIND ROUTE", ACCENT_BLUE);
        findBtn.addActionListener(e -> findRoute());
        panel.add(findBtn);

        panel.add(Box.createVerticalStrut(8));
        JButton compareBtn = styledButton("⚖  COMPARE ALL", new Color(80, 60, 140));
        compareBtn.addActionListener(e -> compareAll());
        panel.add(compareBtn);

        panel.add(Box.createVerticalStrut(8));
        JButton clearBtn = styledButton("✕  CLEAR", new Color(60, 70, 100));
        clearBtn.addActionListener(e -> clearRoute());
        panel.add(clearBtn);

        panel.add(Box.createVerticalStrut(18));
        panel.add(sectionLabel("RESULT"));
        panel.add(Box.createVerticalStrut(5));

        resultArea = new JTextArea(12, 20);
        resultArea.setBackground(new Color(12, 16, 35));
        resultArea.setForeground(TEXT_MAIN);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(resultArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(50, 70, 120)));
        scroll.setBackground(BG_DARK);
        panel.add(scroll);

        return panel;
    }

    private void findRoute() {
        String src = (String) sourceCombo.getSelectedItem();
        String dst = (String) destCombo.getSelectedItem();
        if (src == null || dst == null || src.equals(dst)) {
            resultArea.setText("⚠️ Please select different source and destination.");
            return;
        }

        String algo = (String) algoCombo.getSelectedItem();
        List<String> path;
        int dist;
        int nodesVisited;

        if (algo.startsWith("Dijkstra")) {
            Dijkstra.Result r = Dijkstra.findShortestPath(graph, src, dst);
            path = r.path; dist = r.totalDistance; nodesVisited = r.nodesVisited;
        } else if (algo.startsWith("BFS")) {
            BFSDFSAlgo.Result r = BFSDFSAlgo.bfs(graph, src, dst);
            path = r.path; dist = r.totalDistance; nodesVisited = r.nodesVisited;
        } else if (algo.startsWith("DFS")) {
            BFSDFSAlgo.Result r = BFSDFSAlgo.dfs(graph, src, dst);
            path = r.path; dist = r.totalDistance; nodesVisited = r.nodesVisited;
        } else {
            AStarAlgo.Result r = AStarAlgo.findPath(graph, src, dst);
            path = r.path; dist = r.totalDistance; nodesVisited = r.nodesVisited;
        }

        highlightPath = path;
        graphPanel.setPath(path, src, dst);

        if (path.isEmpty()) {
            resultArea.setText("❌ No path found.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Algorithm: ").append(algo).append("\n\n");
            sb.append("📏 Distance: ").append(dist).append(" meters\n");
            sb.append("🔍 Nodes Explored: ").append(nodesVisited).append("\n");
            sb.append("🛑 Stops: ").append(path.size()).append("\n\n");
            sb.append("📍 Route:\n");
            for (int i = 0; i < path.size(); i++) {
                if (i == 0) sb.append("  🚦 ").append(path.get(i)).append("\n");
                else if (i == path.size() - 1) sb.append("  🏁 ").append(path.get(i)).append("\n");
                else sb.append("  ↓  ").append(path.get(i)).append("\n");
            }
            resultArea.setText(sb.toString());
        }
    }

    private void compareAll() {
        String src = (String) sourceCombo.getSelectedItem();
        String dst = (String) destCombo.getSelectedItem();
        if (src == null || dst == null || src.equals(dst)) {
            resultArea.setText("⚠️ Select different source and destination.");
            return;
        }

        Dijkstra.Result dRes = Dijkstra.findShortestPath(graph, src, dst);
        BFSDFSAlgo.Result bfsRes = BFSDFSAlgo.bfs(graph, src, dst);
        BFSDFSAlgo.Result dfsRes = BFSDFSAlgo.dfs(graph, src, dst);
        AStarAlgo.Result aRes = AStarAlgo.findPath(graph, src, dst);

        // Highlight Dijkstra path (shortest)
        highlightPath = dRes.path;
        graphPanel.setPath(dRes.path, src, dst);

        StringBuilder sb = new StringBuilder();
        sb.append("=== ALGORITHM COMPARISON ===\n");
        sb.append(src).append(" → ").append(dst).append("\n\n");
        appendCompareRow(sb, "Dijkstra", dRes.path, dRes.totalDistance, dRes.nodesVisited);
        appendCompareRow(sb, "BFS", bfsRes.path, bfsRes.totalDistance, bfsRes.nodesVisited);
        appendCompareRow(sb, "DFS", dfsRes.path, dfsRes.totalDistance, dfsRes.nodesVisited);
        appendCompareRow(sb, "A*", aRes.path, aRes.totalDistance, aRes.nodesVisited);
        sb.append("\n🏆 Shortest: Dijkstra / A*\n🔢 Fewest stops: BFS");
        resultArea.setText(sb.toString());
    }

    private void appendCompareRow(StringBuilder sb, String name, List<String> path, int dist, int visited) {
        sb.append("▸ ").append(name).append(":\n");
        if (path.isEmpty()) sb.append("   ❌ No path\n\n");
        else {
            sb.append("   Dist: ").append(dist).append("m | Nodes: ").append(visited)
              .append(" | Stops: ").append(path.size()).append("\n\n");
        }
    }

    private void clearRoute() {
        highlightPath = new ArrayList<>();
        graphPanel.setPath(new ArrayList<>(), null, null);
        resultArea.setText("");
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(TEXT_DIM);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(new Color(30, 40, 75));
        combo.setForeground(TEXT_MAIN);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBorder(BorderFactory.createLineBorder(new Color(60, 80, 140)));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        return combo;
    }

    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> new GraphGUI());
    }

    // ─── Inner graph drawing panel ───────────────────────────────────────────

    class GraphPanel extends JPanel {
        private List<String> pathNodes = new ArrayList<>();
        private String startNode = null, endNode = null;
        private static final int NODE_RADIUS = 18;

        public GraphPanel() {
            setBackground(BG_DARK);
            setPreferredSize(new Dimension(950, 650));
        }

        public void setPath(List<String> path, String start, String end) {
            this.pathNodes = path;
            this.startNode = start;
            this.endNode = end;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            // Subtle grid background
            g2.setColor(new Color(25, 33, 60));
            for (int x = 0; x < w; x += 40) g2.drawLine(x, 0, x, h);
            for (int y = 0; y < h; y += 40) g2.drawLine(0, y, w, y);

            drawEdges(g2);
            drawNodes(g2);
        }

        private int sx(int x) { return (int)(x * (double)getWidth() / 800); }
        private int sy(int y) { return (int)(y * (double)getHeight() / 650); }

        private void drawEdges(Graphics2D g2) {
            Set<String> pathSet = new HashSet<>(pathNodes);
            for (Map.Entry<String, Map<String, Integer>> entry : graph.getAdjList().entrySet()) {
                String from = entry.getKey();
                int[] fc = graph.getCoordinates(from);
                for (Map.Entry<String, Integer> nb : entry.getValue().entrySet()) {
                    String to = nb.getKey();
                    // Draw each edge only once
                    if (from.compareTo(to) > 0) continue;
                    int[] tc = graph.getCoordinates(to);
                    int x1 = sx(fc[0]), y1 = sy(fc[1]);
                    int x2 = sx(tc[0]), y2 = sy(tc[1]);

                    boolean onPath = isEdgeOnPath(from, to);
                    if (onPath) {
                        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        g2.setColor(EDGE_PATH);
                        g2.drawLine(x1, y1, x2, y2);
                        // Draw weight label on path
                        int mx = (x1 + x2) / 2, my = (y1 + y2) / 2;
                        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                        g2.setColor(ACCENT_GOLD);
                        g2.drawString(nb.getValue() + "m", mx + 4, my - 4);
                    } else {
                        g2.setStroke(new BasicStroke(1.5f));
                        g2.setColor(EDGE_DEFAULT);
                        g2.drawLine(x1, y1, x2, y2);
                        int mx = (x1 + x2) / 2, my = (y1 + y2) / 2;
                        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                        g2.setColor(new Color(80, 100, 150));
                        g2.drawString(nb.getValue() + "m", mx + 3, my - 3);
                    }
                }
            }
        }

        private void drawNodes(Graphics2D g2) {
            for (String loc : graph.getLocations()) {
                int[] c = graph.getCoordinates(loc);
                int x = sx(c[0]), y = sy(c[1]);
                int r = NODE_RADIUS;

                Color nodeColor;
                if (loc.equals(startNode)) nodeColor = NODE_START;
                else if (loc.equals(endNode)) nodeColor = NODE_END;
                else if (pathNodes.contains(loc)) nodeColor = NODE_PATH;
                else nodeColor = NODE_DEFAULT;

                // Glow effect for path nodes
                if (pathNodes.contains(loc) || loc.equals(startNode) || loc.equals(endNode)) {
                    for (int glow = 3; glow >= 1; glow--) {
                        g2.setColor(new Color(nodeColor.getRed(), nodeColor.getGreen(), nodeColor.getBlue(), 40 / glow));
                        g2.fillOval(x - r - glow * 4, y - r - glow * 4, (r + glow * 4) * 2, (r + glow * 4) * 2);
                    }
                }

                // Node circle
                g2.setColor(nodeColor);
                g2.fillOval(x - r, y - r, r * 2, r * 2);
                g2.setStroke(new BasicStroke(2f));
                g2.setColor(Color.WHITE);
                g2.drawOval(x - r, y - r, r * 2, r * 2);

                // Icon inside node
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 11));
                g2.setColor(Color.WHITE);
                String icon = loc.equals(startNode) ? "S" : loc.equals(endNode) ? "E" : "";
                if (!icon.isEmpty()) {
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(icon, x - fm.stringWidth(icon) / 2, y + fm.getAscent() / 2 - 2);
                }

                // Label below node
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                FontMetrics fm = g2.getFontMetrics();
                String label = loc;
                int lw = fm.stringWidth(label);

                // Label background
                g2.setColor(new Color(10, 15, 35, 180));
                g2.fillRoundRect(x - lw / 2 - 3, y + r + 2, lw + 6, 15, 4, 4);

                Color labelColor = loc.equals(startNode) ? NODE_START :
                                   loc.equals(endNode) ? NODE_END :
                                   pathNodes.contains(loc) ? ACCENT_GOLD : TEXT_MAIN;
                g2.setColor(labelColor);
                g2.drawString(label, x - lw / 2, y + r + 14);
            }
        }

        private boolean isEdgeOnPath(String a, String b) {
            if (pathNodes.size() < 2) return false;
            for (int i = 0; i < pathNodes.size() - 1; i++) {
                String p = pathNodes.get(i), q = pathNodes.get(i + 1);
                if ((p.equals(a) && q.equals(b)) || (p.equals(b) && q.equals(a))) return true;
            }
            return false;
        }
    }
}

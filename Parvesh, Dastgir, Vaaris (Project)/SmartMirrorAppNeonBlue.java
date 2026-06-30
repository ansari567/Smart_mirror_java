import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

// CHANGED: Renamed class to reflect the new color theme
public class SmartMirrorAppNeonBlue extends JFrame {

    // --- 1. CONSTANTS ---
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;
    private static final Font MONO_FONT = new Font("Monospaced", Font.BOLD, 18);
    private static final Color MIRROR_BG = Color.BLACK;
    private static final Color NEON_BLUE = new Color(20, 200, 255); // CHANGED: New Neon Blue color
    private static final Color MIRROR_FG = NEON_BLUE;                // CHANGED: Set primary color to Neon Blue
    private static final Color HIGHLIGHT_COLOR = NEON_BLUE;          // CHANGED: Highlight is now also Neon Blue
    private static final int TIMER_DELAY_MS = 1000;
    private static final int DATA_UPDATE_DELAY_MS = 10000;
    private static final int NEWS_HEADLINE_COUNT = 5;

    // --- 2. UI COMPONENTS ---
    private JLabel timeLabel;
    private JLabel dateLabel;
    private JLabel weatherLabel;
    private JTextArea quoteArea;
    private JLabel nextEventLabel;
    private JLabel[] newsHeadlineLabels = new JLabel[NEWS_HEADLINE_COUNT];

    // --- 3. MOCK DATA (Unchanged) ---
    private final String[] MOCK_WEATHER = {
        "14°C, Clear Night 🌙",
        "18°C, Light Fog 🌫️",
        "22°C, Sunny Intervals 🌤️",
        "16°C, Showers 🌧️"
    };

    private final List<String[]> MOCK_NEWS_HEADLINES = List.of(
        new String[]{"GLOBAL", "Asian stocks rally after US debt resolution.", "4h ago"},
        new String[]{"POLITICS", "Summit concludes with joint climate goals.", "1d ago"},
        new String[]{"TECH", "New chip architecture promises 50% efficiency.", "3h ago"},
        new String[]{"SPORTS", "Home team wins football championship in overtime.", "1h ago"},
        new String[]{"FINANCE", "Oil prices stabilize amid Middle East peace talks.", "2h ago"},
        new String[]{"HEALTH", "New guidelines issued for summer heat wave.", "5h ago"}
    );

    private final String[] MOCK_QUOTES = {
        "\"The only way to do great work is to love what you do.\" - Steve Jobs",
        "\"Where there is a will, there is a way. This truth holds for all ambitious endeavors.\" - Unknown",
        "\"Strive not to be a success, but rather to be of value. Focus on impact.\" - Albert Einstein"
    };

    private LocalDateTime now = LocalDateTime.now();

    // --- 4. CONSTRUCTOR ---
    // CHANGED: Constructor name to match the new class name
    public SmartMirrorAppNeonBlue() {
        setTitle("Smart Mirror UI - Neon Blue Glow (Unified)"); // CHANGED: Title updated
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        getContentPane().setBackground(MIRROR_BG);
        setLayout(new GridBagLayout());

        initializeComponents();
        startTimers();

        setVisible(true);
    }

    // --- 5. INITIALIZATION & LAYOUT ---

    private void initializeComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);

        // TOP-LEFT: Clock Panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0.4; gbc.weighty = 0.1; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(createClockPanel(), gbc);

        // TOP-RIGHT: Weather Panel
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 0.6; gbc.weighty = 0.1; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(createWeatherPanel(), gbc);

        // MID-LEFT: Calendar/Event Panel
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0.4; gbc.weighty = 0.8; gbc.fill = GridBagConstraints.BOTH;
        add(createFullCalendarPanel(), gbc);

        // MID-RIGHT: News Panel
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 0.6; gbc.weighty = 0.8; gbc.fill = GridBagConstraints.BOTH;
        add(createNewsPanel(), gbc);

        // BOTTOM: Quote Panel
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0; gbc.weighty = 0.1;
        add(createQuotePanel(), gbc);
    }

    private JPanel createClockPanel() {
        JPanel clockPanel = new JPanel();
        clockPanel.setLayout(new BoxLayout(clockPanel, BoxLayout.Y_AXIS));
        clockPanel.setBackground(MIRROR_BG);

        timeLabel = new JLabel("22:47:25");
        timeLabel.setForeground(MIRROR_FG);
        timeLabel.setFont(new Font("Monospaced", Font.PLAIN, 120));

        dateLabel = new JLabel("Thursday, Oct 09");
        dateLabel.setForeground(MIRROR_FG);
        dateLabel.setFont(new Font("Monospaced", Font.PLAIN, 36));

        clockPanel.add(timeLabel);
        clockPanel.add(dateLabel);
        return clockPanel;
    }

    private JPanel createWeatherPanel() {
        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        weatherPanel.setBackground(MIRROR_BG);

        JLabel locationLabel = new JLabel("Ludhiana, Punjab, India", SwingConstants.RIGHT);
        locationLabel.setForeground(MIRROR_FG);
        locationLabel.setFont(MONO_FONT.deriveFont(Font.BOLD, 20f));
        locationLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        weatherLabel = new JLabel(MOCK_WEATHER[0], SwingConstants.RIGHT);
        weatherLabel.setForeground(MIRROR_FG);
        weatherLabel.setFont(new Font("Monospaced", Font.PLAIN, 48));
        weatherLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        weatherPanel.add(locationLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(weatherLabel);
        return weatherPanel;
    }

    private JPanel createFullCalendarPanel() {
        JPanel calendarContainer = new JPanel(new BorderLayout(10, 10));
        calendarContainer.setBackground(MIRROR_BG);

        JLabel monthYearLabel = new JLabel(
            now.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)).toUpperCase(),
            SwingConstants.LEFT
        );
        monthYearLabel.setForeground(MIRROR_FG);
        monthYearLabel.setFont(new Font("Monospaced", Font.BOLD, 36));
        calendarContainer.add(monthYearLabel, BorderLayout.NORTH);

        JPanel calendarGrid = new JPanel(new GridLayout(0, 7));
        calendarGrid.setBackground(MIRROR_BG);

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setForeground(MIRROR_FG);
            dayLabel.setFont(MONO_FONT.deriveFont(Font.BOLD, 22f));
            calendarGrid.add(dayLabel);
        }

        LocalDateTime firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;
        int totalDaysInMonth = firstDayOfMonth.toLocalDate().lengthOfMonth();
        int dayCounter = 1;

        for (int i = 0; i < startDayOfWeek; i++) calendarGrid.add(new JLabel(""));
        for (int i = 0; i < totalDaysInMonth; i++) {
            JLabel dayNumLabel = new JLabel(String.valueOf(dayCounter), SwingConstants.CENTER);
            dayNumLabel.setFont(MONO_FONT.deriveFont(Font.PLAIN, 24f));

            if (dayCounter == now.getDayOfMonth()) {
                dayNumLabel.setForeground(HIGHLIGHT_COLOR); // This will now be Neon Blue
                dayNumLabel.setText("• " + dayCounter + " •");
            } else {
                dayNumLabel.setForeground(MIRROR_FG);
            }
            calendarGrid.add(dayNumLabel);
            dayCounter++;
        }

        calendarContainer.add(calendarGrid, BorderLayout.CENTER);

        nextEventLabel = new JLabel("NEXT EVENT: 4:30 PM Team Meeting ☕", SwingConstants.LEFT);
        nextEventLabel.setForeground(MIRROR_FG);
        nextEventLabel.setFont(MONO_FONT.deriveFont(Font.BOLD, 20f));
        nextEventLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        calendarContainer.add(nextEventLabel, BorderLayout.SOUTH);

        return calendarContainer;
    }

    private JPanel createNewsPanel() {
        JPanel newsContainer = new JPanel();
        newsContainer.setLayout(new GridBagLayout());
        newsContainer.setBackground(MIRROR_BG);

        JLabel header = new JLabel("🌍 WORLD HEADLINES", SwingConstants.LEFT);
        header.setForeground(MIRROR_FG);
        header.setFont(new Font("Monospaced", Font.BOLD, 28));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        GridBagConstraints gbcHeader = new GridBagConstraints();
        gbcHeader.gridx = 0; gbcHeader.gridy = 0; gbcHeader.anchor = GridBagConstraints.NORTHWEST;
        gbcHeader.fill = GridBagConstraints.HORIZONTAL; gbcHeader.weightx = 1.0;
        newsContainer.add(header, gbcHeader);

        for (int i = 0; i < newsHeadlineLabels.length; i++) {
            newsHeadlineLabels[i] = new JLabel("Loading headline " + (i + 1) + "...");
            newsHeadlineLabels[i].setFont(MONO_FONT.deriveFont(16f));
            newsHeadlineLabels[i].setForeground(MIRROR_FG);
            newsHeadlineLabels[i].setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.gridy = i + 1; gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            gbc.weighty = (i == newsHeadlineLabels.length - 1) ? 1.0 : 0.0;

            newsContainer.add(newsHeadlineLabels[i], gbc);
        }

        updateNewsDisplay(0);

        return newsContainer;
    }

    private void updateNewsDisplay(int startingIndex) {
        String hexColor = String.format("#%06X", (MIRROR_FG.getRGB() & 0xFFFFFF));

        for (int i = 0; i < newsHeadlineLabels.length; i++) {
            int dataIndex = (startingIndex + i) % MOCK_NEWS_HEADLINES.size();
            String[] newsItem = MOCK_NEWS_HEADLINES.get(dataIndex);

            String formattedText = String.format(
                "<html><b><font color='%s'>%s:</font></b> <font color='%s'>%s</font> <font color='%s'>(%s)</font></html>",
                hexColor, newsItem[0], // Category
                hexColor, newsItem[1], // Headline
                hexColor, newsItem[2]  // Time
            );

            newsHeadlineLabels[i].setText(formattedText);
        }
    }

    private JPanel createQuotePanel() {
        JPanel quoteContainer = new JPanel(new GridBagLayout());
        quoteContainer.setBackground(MIRROR_BG);

        quoteArea = new JTextArea(MOCK_QUOTES[0]);
        quoteArea.setWrapStyleWord(true);
        quoteArea.setLineWrap(true);
        quoteArea.setEditable(false);
        quoteArea.setOpaque(false);

        quoteArea.setForeground(MIRROR_FG);
        quoteArea.setFont(new Font("Monospaced", Font.ITALIC, 32));
        quoteArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0; gbc.weighty = 1.0;

        quoteContainer.add(quoteArea, gbc);
        return quoteContainer;
    }

    // --- 6. TIMERS & LISTENERS (Unchanged) ---

    private void startTimers() {
        new Timer(TIMER_DELAY_MS, new ClockUpdateListener()).start();
        Timer dataTimer = new Timer(DATA_UPDATE_DELAY_MS, new DataUpdateListener());
        dataTimer.setInitialDelay(1000);
        dataTimer.start();
    }

    private class ClockUpdateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            now = LocalDateTime.now();
            timeLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            dateLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE, MMM dd")));
        }
    }

    private class DataUpdateListener implements ActionListener {
        private int weatherIndex = 0;
        private int quoteIndex = 0;
        private int newsBaseIndex = 0;

        public void actionPerformed(ActionEvent e) {
            weatherIndex = (weatherIndex + 1) % MOCK_WEATHER.length;
            weatherLabel.setText(MOCK_WEATHER[weatherIndex]);

            quoteIndex = (quoteIndex + 1) % MOCK_QUOTES.length;
            quoteArea.setText(MOCK_QUOTES[quoteIndex]);

            newsBaseIndex = (newsBaseIndex + 1) % MOCK_NEWS_HEADLINES.size();
            updateNewsDisplay(newsBaseIndex);
        }
    }

    // --- 7. MAIN METHOD ---
    public static void main(String[] args) {
        // CHANGED: Instantiating the new class name
        SwingUtilities.invokeLater(SmartMirrorAppNeonBlue::new);
    }
}
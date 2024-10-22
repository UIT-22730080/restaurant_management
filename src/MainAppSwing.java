
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MainAppSwing extends JFrame {

    private JTable orderTable;
    private DefaultTableModel tableModel;
    private int selectedTable = -1;
    private HashMap<Integer, DefaultTableModel> tableOrders = new HashMap<>();
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private HashMap<Integer, Boolean> tableStatus = new HashMap<>();
    private JPanel tablePanel; // Lưu trữ tablePanel
    private Statistics statistics = new Statistics();

    public MainAppSwing() {
        setTitle("Quản lý Nhà hàng");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Khởi tạo trạng thái ban đầu cho các bàn
        for (int i = 1; i <= 12; i++) {
            tableStatus.put(i, false);
            tableOrders.put(i, new DefaultTableModel(new String[]{"Tên món", "Giá tiền", "Trạng thái"}, 0)); // Khởi tạo model trống cho mỗi bàn
        }

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(600);
        splitPane.setLeftComponent(createLeftPane());
        splitPane.setRightComponent(createRightPane());

        add(splitPane);
    }

    private void switchToFoodMenu() {
        // Hiển thị món đã chọn của bàn hiện tại
        if (selectedTable != -1) {
            tableModel = tableOrders.get(selectedTable);
            orderTable.setModel(tableModel);
        } else {
            tableModel.setRowCount(0);
        }
        orderTable.revalidate();

        cardLayout.show(cardPanel, "Thực đơn");
    }

    private void addOrder(String name, String price) {
        if (selectedTable == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!");
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String formattedDate = formatter.format(date);

        tableOrders.get(selectedTable).addRow(new Object[]{name, price, formattedDate}); // Thêm món vào model của bàn hiện tại
        updateOrderTable(); // Cập nhật bảng hiển thị

        // Cập nhật trạng thái bàn
        tableStatus.put(selectedTable, true);

        // Cập nhật màu sắc nút bàn tương ứng
        updateTableButtonColor(selectedTable);
    }

    private void updateOrderTable() {
        if (tableOrders.containsKey(selectedTable)) {
            tableModel = tableOrders.get(selectedTable);
            orderTable.setModel(tableModel);
        } else {
            tableModel.setRowCount(0);
        }
        orderTable.revalidate();

        // Cập nhật màu sắc nút bàn khi xóa món
        if (tableOrders.get(selectedTable) != null && tableOrders.get(selectedTable).getRowCount() == 0) {
            tableStatus.put(selectedTable, false);
            updateTableButtonColor(selectedTable);
        }
    }

    private JPanel createLeftPane() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        tablePanel = createTablePanel();
        JPanel menuPanel = createMenuPanel();

        cardPanel.add(tablePanel, "Bàn");
        cardPanel.add(menuPanel, "Thực đơn");

        JPanel buttonPanel = new JPanel();
        JButton tableButton = new JButton("Bàn");
        JButton menuButton = new JButton("Thực đơn");

        tableButton.addActionListener(e -> cardLayout.show(cardPanel, "Bàn"));
        menuButton.addActionListener(e -> cardLayout.show(cardPanel, "Thực đơn"));

        buttonPanel.add(tableButton);
        buttonPanel.add(menuButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createTablePanel() {
        tablePanel = new JPanel(new GridLayout(4, 3, 10, 10));

        for (int i = 1; i <= 12; i++) {
            JButton tableButton = new JButton("Bàn " + i);
            int tableNumber = i;

            // Thêm hành động cho nút bàn
            tableButton.addActionListener(e -> {
                selectedTable = tableNumber;

                // Cập nhật màu sắc của nút bàn TRƯỚC KHI chuyển sang giao diện món ăn
                updateTableButtonColor(tableNumber);

                switchToFoodMenu();
            });

            // Cập nhật màu sắc ban đầu của nút bàn
            updateTableButtonColor(tableNumber);

            tablePanel.add(tableButton);
        }

        return tablePanel;
    }

    // Hàm cập nhật màu sắc của nút bàn
    private void updateTableButtonColor(int tableNumber) {
        for (Component c : tablePanel.getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                if (button.getText().equals("Bàn " + tableNumber)) {
                    if (tableStatus.get(tableNumber)) {
                        button.setBackground(Color.ORANGE);
                    } else {
                        button.setBackground(Color.CYAN);
                    }
                    break;
                }
            }
        }
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(4, 3, 10, 10));

        // Thêm các món ăn mẫu vào menuPanel
        menuPanel.add(createMenuItem("Phở", "40,000 VND", "images/pho.jpg"));
        menuPanel.add(createMenuItem("Bún Bò", "35,000 VND", "images/bun_bo.jpg"));
        menuPanel.add(createMenuItem("Bò Bít Tết", "50,000 VND", "images/bo_bit_tet.jpg"));
        menuPanel.add(createMenuItem("Bò Hầm", "120,000 VND", "images/bo_ham.jpg"));
        menuPanel.add(createMenuItem("Bún Chả", "50,000 VND", "images/bun_cha.jpg"));
        menuPanel.add(createMenuItem("Cơm Tấm", "30,000 VND", "images/com_tam.jpg"));
        menuPanel.add(createMenuItem("Lẩu Hải Sản", "200,000 VND", "images/lau.jpg"));
        menuPanel.add(createMenuItem("Mì Xào Hải Sản", "100,000 VND", "images/mi_hai_san.jpg"));
        menuPanel.add(createMenuItem("Rau Muống Xào Tỏi", "30,000 VND", "images/rau_muong.jpg"));
        menuPanel.add(createMenuItem("Mì Ý", "30,000 VND", "images/mi_y.jpg"));
        menuPanel.add(createMenuItem("Súp", "30,000 VND", "images/sup.jpg"));
        // Thêm các món khác...

        return menuPanel;
    }

    private JPanel createMenuItem(String name, String price, String imagePath) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Thêm hình ảnh đại diện
        ImageIcon icon = new ImageIcon(new ImageIcon(imagePath)
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Thêm sự kiện khi nhấp vào ảnh
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addOrder(name, price);
            }
        });

        // Tên và giá món ăn
        JLabel nameLabel = new JLabel(name);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel priceLabel = new JLabel(price);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Thêm các thành phần vào panel
        itemPanel.add(imageLabel);
        itemPanel.add(nameLabel);
        itemPanel.add(priceLabel);

        return itemPanel;
    }

    private JPanel createRightPane() {
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BorderLayout(10, 10));

        // Tạo bảng với 2 cột: Tên món và Giá tiền
        String[] columnNames = {"Tên món", "Giá tiền", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0);
        orderTable = new JTable(tableModel);
        orderTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Tạo panel chứa nút dưới cùng
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        // Tạo các nút chức năng
        JButton removeButton = new JButton("Xóa món");
        JButton payButton = new JButton("Thanh toán");
        payButton.addActionListener(e -> {
            if (selectedTable != -1) {
                showPaymentDialog(selectedTable);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn trước!");
            }
        });
        bottomPanel.add(payButton); // Thêm nút vào panel
        JButton statisticsButton = new JButton("Thống kê");
        statisticsButton.addActionListener(e -> showStatisticsDialog());
        bottomPanel.add(statisticsButton); // Thêm nút vào panel
        JButton mergeButton = new JButton("Gộp bàn");

        // Thêm sự kiện cho nút "Xóa món"
        removeButton.addActionListener(e -> {
            int selectedRow = orderTable.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món để xóa!");
            }
        });

        // Thêm các nút vào panel
        bottomPanel.add(removeButton);
        bottomPanel.add(payButton);
        bottomPanel.add(statisticsButton);
        bottomPanel.add(mergeButton);

        rightPane.add(scrollPane, BorderLayout.CENTER);
        rightPane.add(bottomPanel, BorderLayout.SOUTH);

        return rightPane;
    }
    // Hàm hiển thị cửa sổ thanh toán

    private void showPaymentDialog(int tableNumber) {
        // Tạo cửa sổ thanh toán
        JDialog paymentDialog = new JDialog(this, "Thanh toán", true);
        paymentDialog.setSize(400, 300);
        paymentDialog.setLocationRelativeTo(null);

        // Tạo panel chứa thông tin thanh toán
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));

        // Tạo label hiển thị số bàn
        JLabel tableLabel = new JLabel("Bàn " + tableNumber);
        tableLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(tableLabel, BorderLayout.NORTH);

        // Tạo bảng hiển thị danh sách món ăn đã order
        DefaultTableModel orderModel = tableOrders.get(tableNumber);
        JTable orderTable = new JTable(orderModel);
        orderTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Tạo panel chứa nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

// Tạo nút "Cancel" (màu đỏ)
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> paymentDialog.dispose()); // Tắt cửa sổ khi nhấn
        buttonPanel.add(cancelButton);

        // Tạo label hiển thị tổng tiền
        JLabel totalLabel = new JLabel("Tổng tiền: " + getTotalPrice(tableOrders.get(tableNumber)));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(totalLabel, BorderLayout.PAGE_END);
// Tạo nút "PAY" (màu xanh)
        JButton payButton = new JButton("PAY"); // Tạo biến riêng cho nút PAY
        payButton.setBackground(Color.GREEN);
        payButton.setForeground(Color.WHITE);
        payButton.addActionListener(e -> {
            // Cập nhật trạng thái bàn
            tableStatus.put(tableNumber, false);
            updateTableButtonColor(tableNumber);

            // Xóa hết món trong orderModel
            orderModel.setRowCount(0);

            // Tạo Order mới
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            String formattedDate = formatter.format(date);

            // Lấy tổng tiền từ totalLabel
            String totalPriceString = totalLabel.getText().replace("Tổng tiền: ", "").replace(" VND", "");
            int totalPrice = Integer.parseInt(totalPriceString.replace(",", "")); // Sử dụng totalPriceString

            Order newOrder = new Order(tableNumber, formattedDate, totalPrice);

            // Thêm Order vào statistics
            statistics.addOrder(newOrder);

            // Tắt cửa sổ thanh toán
            paymentDialog.dispose();

            // Hiển thị cửa sổ thống kê sau khi thanh toán
            // showStatisticsDialog(); // Thêm dòng code này
        });
        buttonPanel.add(payButton);

        // Thêm panel chứa nút vào contentPanel
        contentPanel.add(buttonPanel, BorderLayout.AFTER_LINE_ENDS);

        // Thêm contentPanel vào cửa sổ thanh toán
        paymentDialog.add(contentPanel);

        // Hiển thị cửa sổ thanh toán
        paymentDialog.setVisible(true);
    }

    // Hàm tính tổng tiền
    private String getTotalPrice(DefaultTableModel orderModel) {
        int totalPrice = 0;
        for (int i = 0; i < orderModel.getRowCount(); i++) {
            totalPrice += Integer.parseInt(orderModel.getValueAt(i, 1).toString().replace(",", "").replace(" VND", ""));
        }
        NumberFormat formatter = NumberFormat.getInstance();
        return formatter.format(totalPrice) + " VND";
    }

    private void showStatisticsDialog() {
        // Tạo cửa sổ thống kê
        JDialog statisticsDialog = new JDialog(this, "Thống kê doanh thu", true);
        statisticsDialog.setSize(400, 300);
        statisticsDialog.setLocationRelativeTo(null);

        // Tạo panel chứa thông tin thống kê
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Tạo label hiển thị tổng doanh thu
        JLabel totalRevenueLabel = new JLabel("Tổng doanh thu: " + statistics.getTotalRevenue() + " VND");
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        contentPanel.add(totalRevenueLabel, BorderLayout.NORTH);

        // Tạo bảng hiển thị chi tiết các hóa đơn
        DefaultTableModel orderTableModel = new DefaultTableModel(
                new String[]{"Số bàn", "Ngày giờ thanh toán", "Tổng tiền"}, 0);
        for (Order order : statistics.getOrders()) {
            orderTableModel.addRow(new Object[]{order.getTableNumber(), order.getOrderDate(), order.getTotalPrice() + " VND"});
        }
        JTable orderTable = new JTable(orderTableModel);
        orderTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Tạo nút "Đóng"
        JButton closeButton = new JButton("Đóng");
        closeButton.addActionListener(e -> statisticsDialog.dispose());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(closeButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Thêm contentPanel vào cửa sổ thống kê
        statisticsDialog.add(contentPanel);

        // Hiển thị cửa sổ thống kê
        statisticsDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainAppSwing app = new MainAppSwing();
            app.setVisible(true);
        });
    }
}

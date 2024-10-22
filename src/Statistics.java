
import java.util.ArrayList;
import java.util.List;

public class Statistics {

    private List<Order> orders = new ArrayList<>();
    private int totalRevenue = 0;

    public void addOrder(Order order) {
        orders.add(order);
        totalRevenue += order.getTotalPrice();
    }

    public int getTotalRevenue() {
        return totalRevenue;
    }

    // Phương thức lấy danh sách hóa đơn (có thể dùng để hiển thị thông tin chi tiết)
    public List<Order> getOrders() {
        return orders;
    }
}

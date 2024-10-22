
public class Order {

    private int tableNumber;
    private String orderDate;
    private int totalPrice;

    public Order(int tableNumber, String orderDate, int totalPrice) {
        this.tableNumber = tableNumber;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
    }

    // Getters
    public int getTableNumber() {
        return tableNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}

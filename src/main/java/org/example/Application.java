package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Customer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Seller {
    private String name;

    public Seller(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class OrderItem {
    private String itemName;
    private double unitPrice;
    private int quantity;

    public OrderItem(String itemName, double unitPrice, int quantity) {
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}

class Order {
    private Customer customer;
    private Seller seller;
    private List<OrderItem> orderItems;

    public Order(Customer customer, Seller seller, List<OrderItem> orderItems) {
        this.customer = customer;
        this.seller = seller;
        this.orderItems = orderItems;
    }

    public void processOrder() {
        System.out.println("Processing order for customer: " + customer.getName());
        System.out.println("Seller: " + seller.getName());

        double totalAmount = 0.0;
        for (OrderItem orderItem : orderItems) {
            System.out.println("Item: " + orderItem.getItemName());
            System.out.println("Quantity: " + orderItem.getQuantity());
            System.out.println("Unit Price: " + orderItem.getUnitPrice());
            double itemTotal = orderItem.getUnitPrice() * orderItem.getQuantity();
            System.out.println("Item Total: " + itemTotal);
            totalAmount += itemTotal;
        }
        System.out.println("Total Amount: " + totalAmount);
        System.out.println("Order processed successfully.");
    }
}

interface OrderBuilder {
    OrderBuilder setCustomer(String customerName);
    OrderBuilder setSeller(String sellerName);
    OrderBuilder addOrderItem(String itemName, double unitPrice, int quantity);
    Order build();
}

class OnlineOrderBuilder implements OrderBuilder {
    private Customer customer;
    private Seller seller;
    private List<OrderItem> orderItems;

    public OnlineOrderBuilder() {
        orderItems = new ArrayList<>();
    }

    @Override
    public OrderBuilder setCustomer(String customerName) {
        this.customer = new Customer(customerName);
        return this;
    }

    @Override
    public OrderBuilder setSeller(String sellerName) {
        this.seller = new Seller(sellerName);
        return this;
    }

    @Override
    public OrderBuilder addOrderItem(String itemName, double unitPrice, int quantity) {
        orderItems.add(new OrderItem(itemName, unitPrice, quantity));
        return this;
    }

    @Override
    public Order build() {
        return new Order(customer, seller, orderItems);
    }
}

class Warehouse {
    List<String> products;
    List<Integer> quantities;

    private Warehouse() {
        products = new ArrayList<>();
        quantities = new ArrayList<>();
    }

    public void updateStock(String productName, int quantity) {
        int index = products.indexOf(productName);
        if (index != -1) {
            quantities.set(index, quantities.get(index) + quantity);
        } else {
            products.add(productName);
            quantities.add(quantity);
        }
    }

    public static class WarehouseBuilder {
        private Warehouse warehouse;

        public WarehouseBuilder() {
            warehouse = new Warehouse();
        }

        public WarehouseBuilder addProduct(String productName, int quantity) {
            warehouse.updateStock(productName, quantity);
            return this;
        }

        public Warehouse build() {
            return warehouse;
        }
    }
}

public class Application {
    private Warehouse warehouse;
    private List<Seller> sellers;
    private Scanner scanner;

    public Application() {
        sellers = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("Select Mode:");
            System.out.println("1. Admin Mode");
            System.out.println("2. User Mode");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    adminModeMenu();
                    break;
                case 2:
                    userModeMenu();
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void adminModeMenu() {
        while (true) {
            System.out.println("\nAdmin Mode:");
            System.out.println("1. Add Seller");
            System.out.println("2. Deliver Products to Warehouse");
            System.out.println("3. Back");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addSeller();
                    break;
                case 2:
                    deliverProductsToWarehouse();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void addSeller() {
        System.out.println("Enter the name of the seller:");
        String sellerName = scanner.nextLine();
        Seller seller = new Seller(sellerName);
        sellers.add(seller);
        System.out.println("Seller added successfully.");
    }

    private void deliverProductsToWarehouse() {
        System.out.println("Enter the name of the product to deliver:");
        String productName = scanner.nextLine();
        System.out.println("Enter the quantity to deliver:");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (warehouse == null) {
            System.out.println("Warehouse not initialized.");
            return;
        }

        warehouse.updateStock(productName, quantity);
        System.out.println("Products delivered to the warehouse successfully.");
    }

    private void userModeMenu() {
        while (true) {
            System.out.println("\nUser Mode:");
            System.out.println("1. Create Order");
            System.out.println("2. List Sellers");
            System.out.println("3. List Products");
            System.out.println("4. Back");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    listSellers();
                    break;
                case 3:
                    listProducts();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void createOrder() {
        if (sellers.isEmpty()) {
            System.out.println("No sellers available.");
            return;
        }

        System.out.println("Enter the name of the customer:");
        String customerName = scanner.nextLine();

        System.out.println("Select a seller:");
        for (int i = 0; i < sellers.size(); i++) {
            System.out.println((i + 1) + ". " + sellers.get(i).getName());
        }
        int sellerChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (sellerChoice < 1 || sellerChoice > sellers.size()) {
            System.out.println("Invalid seller choice.");
            return;
        }

        OrderBuilder orderBuilder = new OnlineOrderBuilder();
        orderBuilder.setCustomer(customerName);
        orderBuilder.setSeller(sellers.get(sellerChoice - 1).getName());

        while (true) {
            System.out.println("Add Order Item:");
            System.out.println("Enter the name of the product:");
            String productName = scanner.nextLine();
            System.out.println("Enter the unit price:");
            double unitPrice = scanner.nextDouble();
            System.out.println("Enter the quantity:");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // consume newline

            orderBuilder.addOrderItem(productName, unitPrice, quantity);

            System.out.println("Do you want to add another item? (Y/N)");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("Y")) {
                break;
            }
        }

        Order order = orderBuilder.build();
        order.processOrder();
    }

    private void listSellers() {
        System.out.println("List of Sellers:");
        for (int i = 0; i < sellers.size(); i++) {
            System.out.println((i + 1) + ". " + sellers.get(i).getName());
        }
    }

    private void listProducts() {
        if (warehouse == null) {
            System.out.println("Warehouse not initialized.");
            return;
        }

        System.out.println("List of Products:");
        for (int i = 0; i < warehouse.products.size(); i++) {
            String productName = warehouse.products.get(i);
            int quantity = warehouse.quantities.get(i);
            System.out.println(productName + " - Quantity: " + quantity);
        }
    }

    public static void main(String[] args) {
        Application application = new Application();

        Warehouse.WarehouseBuilder warehouseBuilder = new Warehouse.WarehouseBuilder();
        warehouseBuilder.addProduct("Smartphone", 50);
        warehouseBuilder.addProduct("Laptop", 30);
        warehouseBuilder.addProduct("Headphones", 100);
        application.warehouse = warehouseBuilder.build();

        application.run();
    }
}

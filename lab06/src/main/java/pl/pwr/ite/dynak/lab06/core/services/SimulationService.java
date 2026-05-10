package pl.pwr.ite.dynak.lab06.core.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pwr.ite.dynak.lab06.persistence.enums.Actions;
import pl.pwr.ite.dynak.lab06.persistence.enums.DietTypes;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;
import pl.pwr.ite.dynak.lab06.persistence.models.Client;
import pl.pwr.ite.dynak.lab06.persistence.models.Course;
import pl.pwr.ite.dynak.lab06.persistence.models.Order;
import pl.pwr.ite.dynak.lab06.persistence.repositories.ClientRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CourseRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CurrentDateRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.OrderRepository;
import pl.pwr.ite.dynak.lab06.core.requests.CreateOrderRequest;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SimulationService {

    private final CurrentDateRepository currentDateRepository;
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;
    private final CourseRepository courseRepository;
    private final CourseService courseService;
    private final OrderService orderService;
    private final CurrentDateService currentDateService;
    private final ActionLogService actionLogService;
    private Long clientId;

    @Autowired
    public SimulationService(
            CurrentDateRepository currentDateRepository,
            ClientRepository clientRepository,
            OrderRepository orderRepository,
            CourseRepository courseRepository,
            CourseService courseService,
            OrderService orderService,
            CurrentDateService currentDateService,
            ActionLogService actionLogService) {
        this.currentDateRepository = currentDateRepository;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.courseRepository = courseRepository;
        this.courseService = courseService;
        this.orderService = orderService;
        this.currentDateService = currentDateService;
        this.actionLogService = actionLogService;
    }

    private boolean adminChoices() {
        boolean isExit = false;

        while (!isExit) {
            IO.println("=== Admin Panel ===");
            IO.println("1. Display all orders");
            IO.println("2. Change course price");
            IO.println("3. Exit");

            int choice = Integer.parseInt(IO.readln());

            switch (choice) {
                case 1 -> displayAllOrdersHandler();
                case 2 -> changeCoursePriceHandler();
                case 3 -> isExit = true;
                default -> IO.println("Invalid choice");
            }
        }

        return !isExit;
    }

    private void displayAllOrdersHandler() {
        IO.println("=== Display All Orders ===");

        List<Order> orders = (List<Order>) orderRepository.findAll();

        if (orders.isEmpty()) {
            IO.println("No orders found");
        } else {
            orders.forEach(order -> {
                String mealTypes = order.getMealTypes().stream()
                        .map(MealTypes::name)
                        .collect(java.util.stream.Collectors.joining(", "));
                IO.println(
                    "Order ID: " + order.getId() +
                            ", Client ID: " + order.getClient().getId() +
                            ", Meal Types: " + mealTypes +
                            ", Diet Type: " + order.getDietType() +
                            ", Price: " + order.getPrice() +
                            ", Payment Date: " + order.getPaymentDate() +
                            ", Is Paid: " + order.isPaid() +
                            ", Start date: " + order.getStartDate() +
                            ", End date: " + order.getEndDate()
                );});
        }
    }

    private void changeCoursePriceHandler() {
        IO.println("=== Change Course Price ===");

        List<Course> courses = courseRepository.findAll();

        if (courses.isEmpty()) {
            IO.println("No courses found");
            return;
        }

        IO.println("=== Available Courses ===");
        courses.forEach(course -> IO.println(
                "Course ID: " + course.getId() +
                        ", Meal Type: " + course.getMealType() +
                        ", Price: " + course.getPrice()
        ));

        Long courseId;
        try {
            courseId = Long.parseLong(IO.readln("Enter course ID to modify: "));
        } catch (NumberFormatException e) {
            IO.println("Invalid course ID");
            return;
        }

        Course course = courses.stream()
                .filter(c -> c.getId().equals(courseId))
                .findFirst()
                .orElse(null);

        if (course == null) {
            IO.println("Course not found");
            return;
        }

        double newPrice;
        try {
            newPrice = Double.parseDouble(IO.readln("Enter new price: "));
        } catch (NumberFormatException e) {
            IO.println("Invalid price format");
            return;
        }

        course.setPrice(newPrice);
        courseService.updateCourse(course);

        IO.println("Course price updated successfully!");
    }

    private boolean userChoices(int date) {
        IO.println("=== Day: " + date + " ===");
        IO.println("=== Available choices ===");
        IO.println("0. Skip day");
        IO.println("1. Create order");
        IO.println("2. Pay for order");
        IO.println("3. Exit");

        int choice = Integer.parseInt(IO.readln());
        boolean isExit = false;

        switch (choice) {
            case 1 -> createOrderHandler();
            case 2 -> payForOrderHandler();
            case 3 -> isExit = true;
            default -> IO.println("Invalid choice");
        }

        return !isExit;
    }

    private void login() {
        IO.println("=== Login ===");
        String clientName = IO.readln("Enter your name: ");

        clientId = clientRepository.findByName(clientName)
                .map(Client::getId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        actionLogService.log("User " + clientId + " logged in", Actions.USER_LOGGED_IN);
    }

    private void createOrderHandler() {

        IO.println("=== Create order ===");
        IO.println("Select meals:");
        IO.println("1. Breakfast");
        IO.println("2. Lunch");
        IO.println("3. Dinner");

        String mealInput = IO.readln("Choose meal types (e.g. 1,3): ");
        Set<MealTypes> mealTypes = parseMealTypes(mealInput.replaceAll("\\s+", ""));

        IO.println("=== Select diet type ===");
        IO.println("1. Vegan");
        IO.println("2. Non Vegan");
        IO.println("3. Lactose Free");
        IO.println("4. Gluten Free");

        int dietChoice = Integer.parseInt(IO.readln());

        DietTypes dietType = switch (dietChoice) {
            case 1 -> DietTypes.VEGAN;
            case 2 -> DietTypes.NON_VEGAN;
            case 3 -> DietTypes.LACTOSE_FREE;
            case 4 -> DietTypes.GLUTEN_FREE;
            default -> throw new RuntimeException("Invalid diet type");
        };

        int startDate = Integer.parseInt(IO.readln("Start date: "));
        int endDate = Integer.parseInt(IO.readln("End date: "));
        int paymentDate = 0;

        CreateOrderRequest request = new CreateOrderRequest(
                clientId,
                mealTypes,
                dietType,
                startDate,
                endDate
        );

        orderService.createOrder(request);

        IO.println("=== Order created successfully! ===");

    }

    private void payForOrderHandler() {
        IO.println("=== Unpaid orders ===");

        List<Order> unpaidOrders = orderRepository.findByClientIdAndIsPaidFalse(clientId);

        if (unpaidOrders.isEmpty()) {
            IO.println("No unpaid orders");
            return;
        }

        unpaidOrders.forEach(o -> IO.println(o.getId() + " " + o.getPrice() + " " + o.getPaymentDate()));

        IO.println("Enter order id to pay for: ");
        Long orderId;
        try {
            orderId = Long.parseLong(IO.readln());
        } catch (NumberFormatException e) {
            IO.println("Invalid order id");
            return;
        }

        boolean orderExists = unpaidOrders.stream()
                        .anyMatch(o -> o.getId().equals(orderId));

        if (!orderExists) {
            IO.println("Order not found");
            return;
        }

        orderService.payForOrder(orderId);

        IO.println("=== Order paid successfully! ===");
    }

    public void run() {
        try {
            login();
        } catch (Exception e) {
            IO.println("Login failed");
            return;
        }

        if (clientId == 1L) {
            while (adminChoices());
        } else {
            while (userChoices(currentDateService.getCurrentDate())) {
                //currentDateService.advanceOneDay();
            }
        }

    }

    private Set<MealTypes> parseMealTypes(String input) {
        return input.chars()
                .filter(c -> c != ',')
                .mapToObj(c -> switch (c) {
                    case '1' -> MealTypes.BREAKFAST;
                    case '2' -> MealTypes.LUNCH;
                    case '3' -> MealTypes.DINNER;
                    default -> throw new RuntimeException("Invalid meal option: " + (char) c);
                })
                .collect(java.util.stream.Collectors.toSet());
    }
}

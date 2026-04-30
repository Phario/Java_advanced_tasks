package pl.pwr.ite.dynak.lab06.core.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.pwr.ite.dynak.lab06.persistence.enums.Actions;
import pl.pwr.ite.dynak.lab06.persistence.models.Client;
import pl.pwr.ite.dynak.lab06.persistence.models.Course;
import pl.pwr.ite.dynak.lab06.persistence.models.Order;
import pl.pwr.ite.dynak.lab06.persistence.repositories.ClientRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CourseRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CurrentDateRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.OrderRepository;
import pl.pwr.ite.dynak.lab06.core.requests.CreateOrderRequest;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CurrentDateRepository currentDateRepository;
    private final ClientRepository clientRepository;
    private final CourseRepository courseRepository;
    private final ActionLogService actionLogService;

    public OrderService(OrderRepository orderRepository, CurrentDateRepository currentDateRepository, ClientRepository clientRepository, CourseRepository courseRepository, ActionLogService actionLogService) {
        this.orderRepository = orderRepository;
        this.currentDateRepository = currentDateRepository;
        this.clientRepository = clientRepository;
        this.courseRepository = courseRepository;
        this.actionLogService = actionLogService;
    }

    @Transactional
    public void payForOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.isPaid()) {
            throw new RuntimeException("Order already paid");
        }

        int currentDate = currentDateRepository.findById(1L).get().getDate();

        order.setPaid(true);
        order.setPaymentDate(currentDate);
        orderRepository.save(order);

        String infoString = String.format("Order paid: %d", orderId);
        log.info(infoString);
        actionLogService.log(infoString, Actions.ORDER_PAID);
    }

    @Transactional
    public void createOrder(CreateOrderRequest request) {

        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        int duration = request.endDate() - request.startDate();

        List<Course> courses = new ArrayList<>();
        courseRepository.findAll().forEach(courses::add);

        Map<MealTypes, Double> priceMap = courses.stream()
                .collect(Collectors.toMap(
                        Course::getMealType,
                        Course::getPrice));

        double dailyPrice = request.mealTypes().stream()
                .mapToDouble(meal -> {
                    Double price = priceMap.get(meal);
                    return price;
                }).sum();

        double price = duration * dailyPrice;

        Order order = new Order();

        order.setMealTypes(request.mealTypes());
        order.setDietType(request.dietType());
        order.setPrice(price);
        order.setStartDate(request.startDate());
        order.setEndDate(request.endDate());
        order.setPaymentDate(request.paymentDate());
        order.setClient(client);
        order.setPaid(false);

        orderRepository.save(order);
        String infoString = String.format("Order created: %d", order.getId());
        log.info(infoString);
        actionLogService.log(infoString, Actions.ORDER_CREATED);
    }

    public void processOrders(int date) {
        List<Order> activeOrders =
                orderRepository.findByIsPaidTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);

        activeOrders.forEach(order -> {
            String infoString = String.format("Processing order: %d", order.getId());
            log.info(infoString);
            actionLogService.log(infoString, Actions.ORDER_DELIVERED);
        });

    }

    public void sendReminders(int date){
        int targetDate = date + 3;

        List<Order> unpaidOrders =
                orderRepository.findByIsPaidFalseAndStartDateLessThanEqual(targetDate);

        unpaidOrders.forEach(order -> {
            String infoString = String.format(
                    "REMINDER: orderId=%d, clientId=%d, startDate=%d, price=%.2f",
                    order.getId(),
                    order.getClient().getId(),
                    order.getStartDate(),
                    order.getPrice()
            );
            log.info(infoString);
            actionLogService.log(infoString, Actions.REMINDER_SENT);
        });
    }
}

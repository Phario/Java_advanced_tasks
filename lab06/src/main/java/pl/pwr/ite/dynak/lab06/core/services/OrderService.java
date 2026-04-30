package pl.pwr.ite.dynak.lab06.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.pwr.ite.dynak.lab06.persistence.models.Client;
import pl.pwr.ite.dynak.lab06.persistence.models.Course;
import pl.pwr.ite.dynak.lab06.persistence.models.Order;
import pl.pwr.ite.dynak.lab06.persistence.repositories.ClientRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CourseRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.CurrentDateRepository;
import pl.pwr.ite.dynak.lab06.persistence.repositories.OrderRepository;
import pl.pwr.ite.dynak.lab06.services.requests.CreateOrderRequest;
import pl.pwr.ite.dynak.lab06.persistence.enums.MealTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CurrentDateRepository currentDateRepository;
    private final ClientRepository clientRepository;
    private final CourseRepository courseRepository;

    public OrderService(OrderRepository orderRepository, CurrentDateRepository currentDateRepository, ClientRepository clientRepository, CourseRepository courseRepository) {
        this.orderRepository = orderRepository;
        this.currentDateRepository = currentDateRepository;
        this.clientRepository = clientRepository;
        this.courseRepository = courseRepository;
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
    }
}

package pl.pwr.ite.dynak.lab06.api;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.pwr.ite.dynak.lab06.core.dtos.OrderDTO;
import pl.pwr.ite.dynak.lab06.core.requests.CreateOrderRequest;
import pl.pwr.ite.dynak.lab06.core.services.OrderService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        List<OrderDTO> orders = orderService.getAllOrders().stream().map(
                order -> new OrderDTO(
                        order.getId(),
                        order.getClient().getId(),
                        order.getMealTypes(),
                        order.getDietType(),
                        order.getPrice(),
                        order.isPaid(),
                        order.getPaymentDate(),
                        order.getStartDate(),
                        order.getEndDate()
                )).toList();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/client")
    public ResponseEntity<List<OrderDTO>> getOrdersByClientId(@RequestParam Long clientId) {
        List<OrderDTO> orders = orderService.getOrdersByClientId(clientId).stream().map(
                order -> new OrderDTO(
                        order.getId(),
                        order.getClient().getId(),
                        order.getMealTypes(),
                        order.getDietType(),
                        order.getPrice(),
                        order.isPaid(),
                        order.getPaymentDate(),
                        order.getStartDate(),
                        order.getEndDate()
                )).toList();

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequest request) {
        orderService.createOrder(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/payment")
    public ResponseEntity<Void> payForOrder(@RequestParam Long orderId) {
        orderService.payForOrder(orderId);

        return ResponseEntity.ok().build();
    }
}

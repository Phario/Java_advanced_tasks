package pl.pwr.ite.dynak.lab06.persistence.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.pwr.ite.dynak.lab06.persistence.models.Order;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByIsPaidFalseAndStartDateLessThanEqual(int date);
    List<Order> findByIsPaidTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            int date1, int date2
    );

    @EntityGraph(attributePaths = {"mealTypes"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"mealTypes"})
    List<Order> findByClientIdAndIsPaidFalse(Long clientId);
}

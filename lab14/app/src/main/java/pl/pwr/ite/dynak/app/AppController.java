package pl.pwr.ite.dynak.app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.pwr.ite.dynak.Booth;
import pl.pwr.ite.dynak.services.NotificationService;
import pl.pwr.ite.dynak.services.TicketingService;
import pl.pwr.ite.dynak.utils.Ticket;
import pl.pwr.ite.dynak.utils.TicketComparator;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.*;

public class AppController {
    @FXML
    public TableColumn<Ticket, Integer> ticketNumberTableColumn;
    @FXML
    public TableColumn<Ticket, String> categoryTableColumn;
    @FXML
    public TableColumn<Ticket, Integer> priorityTableColumn;
    @FXML
    public TextField categoryTextField;
    @FXML
    public Button createTicketButton;
    @FXML
    public Button booth1GetTicketButton;
    @FXML
    public Label booth1Label;
    @FXML
    public TextField booth1NewCatField;
    @FXML
    public Button booth1AddCatButton;
    @FXML
    public Button booth2GetTicketButton;
    @FXML
    public Label booth2Label;
    @FXML
    public TextField booth2NewCatField;
    @FXML
    public Button booth2AddCatButton;
    @FXML
    public Button booth3GetTicketButton;
    @FXML
    public Label booth3Label;
    @FXML
    public TextField booth3NewCatField;
    @FXML
    public Button booth3AddCatButton;
    @FXML
    public TableView<Ticket> ticketTableView;
    @FXML
    public Label booth3SupCatsLabel;
    @FXML
    public Label booth2SupCatsLabel;
    @FXML
    public Label booth1SupCatsLabel;
    @FXML
    public Button createNewCategoryButton;
    @FXML
    public Button removeCategoryButton;
    @FXML
    public Button booth1RemCatButton;
    @FXML
    public Button booth2RemCatButton;
    @FXML
    public Button booth3RemCatButton;
    private Map<String, Integer> categories = new HashMap<>();
    private final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    private final TicketingService ticketingService = new TicketingService();
    private ObservableList<Ticket> observableTicketQueue = FXCollections.observableArrayList();
    List<Booth> boothList = null;

    @FXML
    public void initialize() {
        createTicketButton.setOnAction(e -> handleCreateTicket());
        createNewCategoryButton.setOnAction(e -> handleCreateNewCategory());
        removeCategoryButton.setOnAction(e -> handleRemoveCategoryButton());
        initPriorities();
        initTable();
        initTicketingService();

        initBooths();
    }

    private void handleRemoveCategoryButton() {
        String enteredCategory = categoryTextField.getText().trim().toUpperCase();

        if (enteredCategory.isEmpty()) return;

        boolean isCategoryInTickets = ticketingService.getQueue().stream()
                .anyMatch(ticket -> ticket.getCategory().equalsIgnoreCase(enteredCategory));

        if (ticketingService.getPriorities().containsKey(enteredCategory) && !isCategoryInTickets) {
            categories.remove(enteredCategory);

            ticketingService.setPrioritiesFromUI(categories);
            categoryTextField.clear();

            for (Booth booth : boothList) {
                if (booth.getSupportedCategories().contains(enteredCategory)) {
                    booth.getSupportedCategories().remove(enteredCategory);
                }
            }

            if (boothList != null && boothList.size() >= 3) {
                updateCategoriesLabel(boothList.get(0), booth1SupCatsLabel);
                updateCategoriesLabel(boothList.get(1), booth2SupCatsLabel);
                updateCategoriesLabel(boothList.get(2), booth3SupCatsLabel);
            }
        }
    }

    private void handleCreateNewCategory() {
        String[] enteredCategory = categoryTextField.getText().split(";");
        if (enteredCategory.length != 2) {
            return;
        }

        String category = enteredCategory[0].trim().toUpperCase();
        int priority = Integer.parseInt(enteredCategory[1].trim());

        categories.put(category, priority);
        ticketingService.setPriorities(categories);
    }

    private void initTable() {
        ticketNumberTableColumn.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priorityTableColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        ticketTableView.setItems(observableTicketQueue);
    }

    private void refreshTable() {
        List<Ticket> sortedTickets = ticketingService.getQueue().stream()
                .sorted(new TicketComparator())
                .toList();

        observableTicketQueue.setAll(sortedTickets);
    }

    private void initTicketingService() {
        try {
            ObjectName name = new ObjectName("pl.pwr.ite.dynak:type=TicketingService");
            mbs.registerMBean(ticketingService, name);

            System.out.println("Registered TicketingService MBean");
        } catch (MalformedObjectNameException
                 | NotCompliantMBeanException
                 | InstanceAlreadyExistsException
                 | MBeanRegistrationException e) {
            throw new RuntimeException(e);
        }
    }

    private void initBooths() {
        boothList = new ArrayList<>();
        NotificationService notificationService = new NotificationService();

        Booth booth1 = new Booth(1, "Booth #1", ticketingService, notificationService);
        Booth booth2 = new Booth(2, "Booth #2", ticketingService, notificationService);
        Booth booth3 = new Booth(3, "Booth #3", ticketingService, notificationService);

        boothList.add(booth1);
        boothList.add(booth2);
        boothList.add(booth3);

        for (Booth booth : boothList) {
            try {
                ObjectName name = new ObjectName("pl.pwr.ite.dynak:type=Booth,id=" + booth.getId());
                mbs.registerMBean(booth, name);
            } catch (Exception e) {
                throw new RuntimeException("Error registering booth", e);
            }
        }

        ticketingService.setBooths(boothList);

        booth1.addSupportedCategory("A");
        booth2.addSupportedCategory("B");
        booth3.addSupportedCategory("A");
        booth3.addSupportedCategory("B");

        setupBoothUI(booth1, booth1GetTicketButton, booth1Label, booth1NewCatField, booth1AddCatButton, booth1SupCatsLabel, booth1RemCatButton);
        setupBoothUI(booth2, booth2GetTicketButton, booth2Label, booth2NewCatField, booth2AddCatButton, booth2SupCatsLabel, booth2RemCatButton);
        setupBoothUI(booth3, booth3GetTicketButton, booth3Label, booth3NewCatField, booth3AddCatButton, booth3SupCatsLabel, booth3RemCatButton);
    }

    private void setupBoothUI(Booth booth, Button getTicketBtn, Label infoLabel, TextField newCatField, Button addCatBtn, Label supCatsLabel, Button remCatBtn) {
        infoLabel.setText("No ticket");

        updateCategoriesLabel(booth, supCatsLabel);

        getTicketBtn.setOnAction(event -> {
            booth.serveTicket();

            int currentTicketId = booth.getCurrentTicket();
            if (currentTicketId > 0) {
                infoLabel.setText("Serving ticket no. " + currentTicketId);
            } else {
                infoLabel.setText("No tickets");
            }

            updateCategoriesLabel(booth, supCatsLabel);
            refreshTable();
        });

        addCatBtn.setOnAction(event -> {
            String cat = newCatField.getText().trim().toUpperCase();
            if (!cat.isEmpty() && ticketingService.getPriorities().containsKey(cat)) {
                booth.addSupportedCategory(cat);
                newCatField.clear();

                updateCategoriesLabel(booth, supCatsLabel);
            }
        });

        remCatBtn.setOnAction(event -> {
            String cat  = newCatField.getText().trim().toUpperCase();
            if (!cat.isEmpty() && ticketingService.getPriorities().containsKey(cat)) {
                booth.removeSupportedCategory(cat);
                newCatField.clear();
                updateCategoriesLabel(booth, supCatsLabel);
            }
        });
    }

    private void updateCategoriesLabel(Booth booth, Label label) {
        if (booth.getSupportedCategories() == null || booth.getSupportedCategories().isEmpty()) {
            label.setText("Supported: none");
        } else {
            String categoriesText = String.join(", ", booth.getSupportedCategories());
            label.setText("Supported: [" + categoriesText + "]");
        }
    }

    private void initPriorities() {
        categories.put("A", 1);
        categories.put("B", 2);

        ticketingService.setPriorities(categories);
    }

    public void handleCreateTicket() {
        String enteredCategory = categoryTextField.getText().trim().toUpperCase();

        if (enteredCategory.isEmpty() || !ticketingService.getPriorities().containsKey(enteredCategory)) {
            return;
        }

        Ticket newTicket = ticketingService.generateTicket(enteredCategory);

        if (newTicket != null) {
            categoryTextField.clear();
            refreshTable();
        }
    }
}

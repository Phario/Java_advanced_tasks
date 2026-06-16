var SimpleStringProperty = Java.type("javafx.beans.property.SimpleStringProperty");
var SimpleDoubleProperty = Java.type("javafx.beans.property.SimpleDoubleProperty");
var XYChart = Java.type("javafx.scene.chart.XYChart");
var FXCollections = Java.type("javafx.collections.FXCollections");
var TextFieldTableCell = Java.type("javafx.scene.control.cell.TextFieldTableCell");
var DoubleStringConverter = Java.type("javafx.util.converter.DoubleStringConverter");
var ChangeListener = Java.type("javafx.beans.value.ChangeListener");
var HashMap = Java.type("java.util.HashMap");

var series;

function initUI() {
    series = new XYChart.Series();
    chart.getData().add(series);

    nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    table.setItems(FXCollections.observableArrayList(
        addRow("1", 1.0),
        addRow("2", 2.0),
        addRow("3", 3.0),
        addRow("4", 4.0),
        addRow("5", 5.0)
    ));
}

function addRow(name, value) {
    var map = new HashMap();

    var nameProp = new SimpleStringProperty(name);
    var valueProp = new SimpleDoubleProperty(value);

    var changeListener = new ChangeListener({
        changed: function(observable, oldValue, newValue) {
            updateChart();
        }
    });

    nameProp.addListener(changeListener);
    valueProp.addListener(changeListener);

    map.put("name", nameProp);
    map.put("value", valueProp);
    return map;
}

function updateChart() {
    var items = table.getItems();

    var last = items.get(items.size() - 1);

    if (last && last.get("name").get() !== "") {
        items.add(addRow("", 0.0));
    }

    series.getData().clear();
    for (var i = 0; i < items.size(); i++) {
        var name = items.get(i).get("name").get();
        var value = items.get(i).get("value").get();

        if (name !== "") {
            series.getData().add(new XYChart.Data(name, value));
        }
    }
}

initUI();
updateChart();
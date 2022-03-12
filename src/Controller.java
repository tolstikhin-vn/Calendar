import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Text textOfChosenDate;

    boolean monthIncrease, monthReduce = false;
    LocalDate currentDate;

    LocalDate getCurrentDate(int year, int month) {
        StringBuilder stringDate;
        if (month == 0) {
            year--;
            month = 12;
            stringDate = new StringBuilder(year + "-" + month + "-01");
        } else if (month >= 1 && month <= 9) {
            stringDate = new StringBuilder(year + "-0" + month + "-01");
        } else if (month >= 10 && month <= 12){
            stringDate = new StringBuilder(year + "-" + month + "-01");
        } else {
            year++;
            month = 1;
            stringDate = new StringBuilder(year + "-0" + month + "-01");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        currentDate = LocalDate.parse(stringDate, formatter);
        return currentDate;
    }

    int currentDay;
    void highlightToday() {
        int count1 = 1, count2 = 0;
        for (Node element: anchorPane.getChildren()) {
            if (element instanceof Text) {
                if (!((Text) element).getText().equals("")) {
//                    System.out.println(Integer.parseInt(((Text) element).getText()));
//                    if (Integer.parseInt(((Text) element).getText()) == currentDay) {
//                        break;
//                    }
                    count1++;
                }
            }
        }
        for (Node element: gridPane.getChildren()) {
            if (count2 == count1) {
                element.setStyle("-fx-border-width: 3; -fx-border-color: #000000");
                break;
            }
            count2++;
        }
    }

    boolean firstStartCalendar = true;

    @FXML
    void showCalendar() {

        if (monthIncrease) {
            int year = currentDate.getYear();
            int month = currentDate.getMonthValue() + 1;
            currentDate = getCurrentDate(year, month);
        } else if(monthReduce) {
            int year = currentDate.getYear();
            int month = currentDate.getMonthValue() - 1;
            currentDate = getCurrentDate(year, month);
        } else {
            currentDate = LocalDate.now();
        }
        int firstMonthDay = getDayOfWeek(currentDate);

        // Обнуление содержимого ячейки для изменения
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof Text) {
                ((Text) node).setText("");
            }
        }

        ObservableList listOfTexts = anchorPane.getChildren();

        int j = 1;
        currentDay = currentDate.getDayOfMonth();

        for (int i = firstMonthDay - 1; i < currentDate.lengthOfMonth() + firstMonthDay - 1; ++i) {
            Object text = listOfTexts.get(i);
            if (text instanceof Text) {
                if (currentDay == j) {
                    currentDay = i;
                    if (firstStartCalendar) {
                        highlightToday();
                        changeText();
                    }
                    firstStartCalendar = false;
                }
                ((Text) text).setText(Integer.toString(j));
                j++;
            }
        }

        monthReduce = false;
        monthIncrease = false;
    }

    // Получение даты в нужном формате
    int getDayOfWeek(LocalDate date) {
        String correctMonth;

        if (date.getMonthValue() < 10) {
            correctMonth = "0" + date.getMonthValue();
        } else {
            correctMonth = Integer.toString(date.getMonthValue());
        }
        DayOfWeek dow = LocalDate.parse("01-" + correctMonth + "-" + date.getYear(), DateTimeFormatter.ofPattern("dd-MM-yyyy")).getDayOfWeek();
        return dow.getValue();
    }

    // Установка стиля выбранной ячейки по умолчанию
    void resetStyles() {
        for (Node node : gridPane.getChildren()) {
            node.setStyle("-fx-border-width: 0.5; -fx-border-color: #76787a");
        }
    }

    @FXML
    void increaseMonth() {
        monthIncrease = true;
        resetStyles();
        showCalendar();
    }

    @FXML
    void reduceMonth() {
        monthReduce = true;
        resetStyles();
        showCalendar();
    }

    String getRusMonth(int month) {
        String monthTitle = null;
        switch (month) {
            case (1):
                monthTitle = "ЯНВАРЯ";
                break;
            case (2):
                monthTitle = "ФЕВРАЛЯ";
                break;
            case (3):
                monthTitle = "МАРТА";
                break;
            case (4):
                monthTitle = "АПРЕЛЯ";
                break;
            case (5):
                monthTitle = "МАЯ";
                break;
            case (6):
                monthTitle = "ИЮНЯ";
                break;
            case (7):
                monthTitle = "ИЮЛЯ";
                break;
            case (8):
                monthTitle = "АВГУСТА";
                break;
            case (9):
                monthTitle = "СЕНТЯБРЯ";
                break;
            case (10):
                monthTitle = "ОКТЯБРЯ";
                break;
            case (11):
                monthTitle = "НОЯБРЯ";
                break;
            case (12):
                monthTitle = "ДЕКАБРЯ";
                break;
        }
        return monthTitle;
    }

    StringBuilder getChosenDate() {
        int i = 0, numbOfCell = 0;
        int chosenDay = currentDate.getDayOfMonth();
        for (Node node : gridPane.getChildren()) {
            if (!node.getStyle().equals("-fx-border-width: 3; -fx-border-color: #000000")) {
                i++;
            } else {
                numbOfCell = i;
            }
        }
        int j = 0;
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof Text) {
                if (numbOfCell != j || ((Text) node).getText().equals("")) {
                    j++;
                } else {
                    chosenDay = Integer.parseInt(((Text) node).getText());
                    break;
                }
            }
        }

        String monthTitle = getRusMonth(currentDate.getMonthValue());
        return new StringBuilder(chosenDay + " " + monthTitle + " " + currentDate.getYear() + " г.");
    }

    // "Навешивание" обработчиков событий на ячейки
    void setHandlers() {
        for (Node element: gridPane.getChildren()) {
            for (Node text : anchorPane.getChildren()) {
                if (text instanceof Text) {
                    if (!(((Text) text).getText().equals(""))) {
                        element.setOnMouseClicked(e -> {
                            resetStyles();
                            element.setStyle("-fx-border-width: 3; -fx-border-color: #000000");
                            changeText();
                        });

                    };
                };
            };
        }
    }

    // Изменения даты в текстовом представлении в верхней части календаря
    void changeText() {
        textOfChosenDate.setText(getChosenDate().toString());
    }

    // Метод, вызываемый автоматически при запуске программы, для отображения календаря
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCalendar();
        setHandlers();
    }
}
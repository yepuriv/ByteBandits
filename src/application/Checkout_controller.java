package application;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Checkout_controller {
    private Stage stage;
    private Scene scene;

    @FXML
    private Button chout;
    @FXML
    private TextField tot;
    @FXML
    private Button bill;
    @FXML
    private ListView<String> itemList; // ListView for displaying items

    public void checkout(ActionEvent event) {
        displayTotalCost();
    }

    public void switchToAddressForm(ActionEvent e) throws IOException {
        countUpdation();
        Parent root = FXMLLoader.load(getClass().getResource("AddressForm.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void displayTotalCost() {
        Cart shoppingCart = Cart.getInstance();
        tot.setText("$" + String.format("%.2f", shoppingCart.calculateTotal()));
        populateItems();
    }

    private void populateItems() {
        Cart shoppingCart = Cart.getInstance();
        itemList.getItems().clear();
        for (CartEntry entry : shoppingCart.getCartItems()) {
            itemList.getItems().add(entry.getProduct().getName() + " - Quantity: " + entry.getQuantity());
        }
    }

    public void countUpdation() {
        Cart shoppingCart = Cart.getInstance();
        List<Integer> aisles = shoppingCart.getAisles();
        for (int i = 0; i < aisles.size(); i++) {
            int elements = shoppingCart.getCountMap(aisles.get(i)).size();
            for (int j = 0; j < elements; j++) {
                List<String> prodNames = shoppingCart.getProductNames(aisles.get(i));
                int initCount = shoppingCart.getCountMap(aisles.get(i)).get(prodNames.get(j)).getProduct().getCount();
                int countToBeSub = shoppingCart.getCountMap(aisles.get(i)).get(prodNames.get(j)).getQuantity();
                shoppingCart.getCountMap(aisles.get(i)).get(prodNames.get(j)).getProduct()
                        .setCount(initCount - countToBeSub);
                shoppingCart.getCountMap(aisles.get(i)).get(prodNames.get(j)).resetQuantity();
            }
        }
    }
    
    public void switchToScene1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

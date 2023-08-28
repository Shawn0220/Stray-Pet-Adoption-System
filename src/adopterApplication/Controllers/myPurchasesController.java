package adopterApplication.Controllers;

import adopterApplication.dataBean.CacheDataBean;
import adopterApplication.dataBean.PetBean;
import adopterApplication.utils.DialogBox;
import adopterApplication.utils.SQLUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class myPurchasesController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(() -> setGoodsView(SQLUtil.getPetsByAdopterId(CacheDataBean.getNowAdopter().getAno()))).start();
    }

    @FXML
    private Button findGoods;


    @FXML
    private FlowPane goodsView;

    @FXML
    private Button refresh;

    @FXML
    private TextField findText;

    @FXML
    void refreshOnClick(ActionEvent event) {
        new Thread(() -> setGoodsView(SQLUtil.getPetsByAdopterId(CacheDataBean.getNowAdopter().getAno()))).start();
    }

    @FXML
    void findGoodsOnClick(ActionEvent event) {
        if (findText.getText() != null && !findText.getText().equals("")) {
            String[] userKey = findText.getText().split("[,|/]");
            setGoodsView(SQLUtil.getGoodsByKeyWordsAndAdopterId(userKey, CacheDataBean.getNowAdopter().getAno()));
        }
    }

    private void setGoodsView(List<PetBean> goodsList) {
        Platform.runLater(() -> goodsView.getChildren().clear());
        ExecutorService exec = Executors.newFixedThreadPool(4);
        for (PetBean pet : goodsList) {
            exec.execute(new addGoodsToView(pet));
        }
        exec.shutdown();
    }
    class addGoodsToView implements Runnable {
        PetBean pet;
        public addGoodsToView(PetBean pet) {
            this.pet = pet;
        }
        @Override
        public void run() {
            ImageView imageView = new ImageView(new Image("file:" + Objects.requireNonNull(SQLUtil.getPetsPhoto(null).getPath())));
            imageView.setFitWidth(230);imageView.setFitHeight(150);
            AnchorPane.setTopAnchor(imageView, 10.0);AnchorPane.setLeftAnchor(imageView, 5.0);

            Label goodsName = new Label(pet.getPname());
            goodsName.setPrefSize(115, 40);
            AnchorPane.setTopAnchor(goodsName, 160.0);AnchorPane.setLeftAnchor(goodsName, 5.0);

            Label goodsPrice = new Label(pet.getPtype());
            goodsPrice.setPrefSize(115, 40);
            AnchorPane.setTopAnchor(goodsPrice, 160.0);AnchorPane.setLeftAnchor(goodsPrice, 120.0);

            Button introduce = new Button("详情");
            introduce.setPrefSize(110, 40);
            AnchorPane.setTopAnchor(introduce, 205.0);AnchorPane.setLeftAnchor(introduce, 65.0);

            AnchorPane anchorPane = new AnchorPane(imageView, goodsName, goodsPrice, introduce);
            anchorPane.setPrefSize(240, 240);

            ImageView check = new ImageView();
            check.setFitWidth(100);check.setFitHeight(40);
            AnchorPane.setTopAnchor(check, 10.0);AnchorPane.setLeftAnchor(check, 5.0);

            if (pet.getIsapdated().equals("试养期")) {
                check.setImage(new Image(String.valueOf(getClass().getResource("/adopterApplication/resource/images/adopting.png"))));
            } else {
                check.setImage(new Image(String.valueOf(getClass().getResource("/adopterApplication/resource/images/adopted.png"))));
            }
            anchorPane.getChildren().add(check);

            introduce.setOnAction(event -> {
                CacheDataBean.setLookingPet(pet);
                CacheDataBean.getWorkingPane().getChildren().clear();
                FXMLLoader loader = new FXMLLoader();	// 创建对象
                InputStream in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/introduction.fxml");
                AnchorPane page = null; // 对象方法的参数是InputStream，返回值是Object
                try {
                    page = loader.load(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CacheDataBean.getWorkingPane().getChildren().add(page);
            });

            Platform.runLater(() -> goodsView.getChildren().add(anchorPane));
        }
    }
}

package adopterApplication.Controllers;

import adopterApplication.dataBean.CacheDataBean;
import adopterApplication.dataBean.PetBean;
import adopterApplication.dataBean.StatusBean;
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

public class myDistributionController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CacheDataBean.setStatus(StatusBean.OnDistribution);
        new Thread(() -> setGoodsView(SQLUtil.getPetByVolunteerId(CacheDataBean.getNowVolunteer().getVno()))).start();
    }

    @FXML
    void refreshOnClick(ActionEvent event) {
        new Thread(() -> setGoodsView(SQLUtil.getPetByVolunteerId(CacheDataBean.getNowVolunteer().getVno()))).start();
    }

    @FXML
    void findGoodsOnClick(ActionEvent event) {
        if (findText.getText() != null && !findText.getText().equals("")) {
            String[] userKey = findText.getText().split("[,|/]");
            setGoodsView(SQLUtil.getGoodsByKeyWordsAndVounteerId(userKey, CacheDataBean.getNowVolunteer().getVno()));
        }
    }

    private void setGoodsView(List<PetBean> PetsList) {
        Platform.runLater(() -> {
            withoutSale.getChildren().clear();
            saleOut.getChildren().clear();
        });
        ExecutorService exec = Executors.newFixedThreadPool(4);
        for (PetBean petBean : PetsList) {
            exec.execute(new addGoodsToView(petBean));
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
            ImageView imageView = new ImageView(new Image("file:" + Objects.requireNonNull(SQLUtil.getPetsPhoto(pet)).getPath()));
            imageView.setFitWidth(230);imageView.setFitHeight(150);
            AnchorPane.setTopAnchor(imageView, 10.0);AnchorPane.setLeftAnchor(imageView, 5.0);

            Label goodsName = new Label(pet.getPname());
            goodsName.setPrefSize(115, 40);
            AnchorPane.setTopAnchor(goodsName, 160.0);AnchorPane.setLeftAnchor(goodsName, 5.0);

            Label goodsPrice = new Label(pet.getPtype());
            goodsPrice.setPrefSize(115, 40);
            AnchorPane.setTopAnchor(goodsPrice, 160.0);AnchorPane.setLeftAnchor(goodsPrice, 120.0);

            Button introduce = new Button("详情");
            Button delete = new Button("删除");
            if ("待领养".equals(pet.getIsapdated())) {
                introduce.setPrefSize(70, 40);
                AnchorPane.setTopAnchor(introduce, 205.0);AnchorPane.setLeftAnchor(introduce, 10.0);

                delete.setPrefSize(70, 40);
                AnchorPane.setTopAnchor(delete, 205.0);AnchorPane.setLeftAnchor(delete, 85.0);

                Button modify = new Button("修改");
                modify.setPrefSize(70, 40);
                AnchorPane.setTopAnchor(modify, 205.0);AnchorPane.setLeftAnchor(modify, 160.0);

                AnchorPane anchorPane = new AnchorPane(imageView, goodsName, goodsPrice, introduce, delete, modify);
                anchorPane.setPrefSize(240, 240);

                introduce.getProperties().put("petId", pet.getPno());
                delete.getProperties().put("petId", pet.getPno());
                modify.getProperties().put("petId", pet.getPno());
                ImageView check = new ImageView();
                check.setFitWidth(100);check.setFitHeight(40);
                AnchorPane.setTopAnchor(check, 10.0);AnchorPane.setLeftAnchor(check, 5.0);
                check.setImage(new Image(String.valueOf(getClass().getResource("/adopterApplication/resource/images/waiteAdopt.png"))));
                anchorPane.getChildren().add(check);

                Platform.runLater(() -> withoutSale.getChildren().add(anchorPane));

                modify.setOnAction(event -> {
                    CacheDataBean.setLookingPet(pet);
                    CacheDataBean.getWorkingPane().getChildren().clear();
                    FXMLLoader loader = new FXMLLoader();	// 创建对象
                    InputStream in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/modify.fxml");
                    AnchorPane page = null; // 对象方法的参数是InputStream，返回值是Object
                    try {
                        page = loader.load(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    CacheDataBean.getWorkingPane().getChildren().add(page);
                });
            } else {
                introduce.setPrefSize(110, 40);
                AnchorPane.setTopAnchor(introduce, 205.0);AnchorPane.setLeftAnchor(introduce, 65.0);

                delete.setVisible(false);

                AnchorPane anchorPane = new AnchorPane(imageView, goodsName, goodsPrice, introduce, delete);
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

                Platform.runLater(() -> saleOut.getChildren().add(anchorPane));
            }

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

            delete.setOnAction(event -> {
                if (DialogBox.confirm("确定删除该宠物吗?")) {
                    SQLUtil.deletePetById(pet.getPno());
                    setGoodsView(SQLUtil.getPetByVolunteerId(CacheDataBean.getNowVolunteer().getVno()));
                }
            });
        }
    }

    @FXML
    private TextField findText;

    @FXML
    private Button findGoods;

    @FXML
    private Button refresh;

    @FXML
    private FlowPane withoutSale;

    @FXML
    private FlowPane saleOut;
}

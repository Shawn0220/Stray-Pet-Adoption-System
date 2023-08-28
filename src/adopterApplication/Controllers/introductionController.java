package adopterApplication.Controllers;

import adopterApplication.dataBean.*;
import adopterApplication.utils.DialogBox;
import adopterApplication.utils.SQLUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class introductionController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CacheDataBean.setStatus(StatusBean.OnIntroduction);
        new Thread(new setView(CacheDataBean.getLookingPet())).start();
        if (!"volunteer".equals(CacheDataBean.getIdentity())||
                !CacheDataBean.getLookingPet().getVno().equals(CacheDataBean.getNowVolunteer().getVno())) {
            modify.setVisible(false);
            pass.setVisible(false);
        }
        if ("待领养".equals(CacheDataBean.getLookingPet().getIsapdated())) {
            pass.setVisible(false);
        } else {
            modify.setVisible(false);
        }
        if ("已收养".equals(CacheDataBean.getLookingPet().getIsapdated())) {
            pass.setVisible(false);
        }
    }

    @FXML
    void adoptItOnClick(ActionEvent event) {
        if (CacheDataBean.getIdentity().equals("volunteer")) {
            System.out.println("wzx");
            DialogBox.warning("您没有权限！");
        } else {
            CacheDataBean.getWorkingPane().getChildren().clear();
            FXMLLoader loader = new FXMLLoader();    // 创建对象
            InputStream in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/justBuyIt.fxml");
            AnchorPane page = null; // 对象方法的参数是InputStream，返回值是Object
            try {
                page = loader.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CacheDataBean.getWorkingPane().getChildren().add(page);
        }
    }

    @FXML
    void modifyOnClick(ActionEvent event) {
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
    }

    @FXML
    void cancelOnClick(ActionEvent event) {
        CacheDataBean.getWorkingPane().getChildren().clear();
        FXMLLoader loader = new FXMLLoader();	// 创建对象
        InputStream in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/discovery.fxml");
        if (CacheDataBean.getStatus() == StatusBean.OnMyPurchases) {
            in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/myPurchases.fxml");
        } else if (CacheDataBean.getStatus() == StatusBean.OnMyDistribution) {
            in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/myDistribution.fxml");
        }
        AnchorPane page = null; // 对象方法的参数是InputStream，返回值是Object
        try {
            page = loader.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CacheDataBean.getWorkingPane().getChildren().add(page);
    }

    @FXML
    void passOnClick(ActionEvent event) throws SQLException {
        if (DialogBox.confirm("确定通过宠物试养期吗？")) {
            CacheDataBean.getLookingPet().setIsapdated("已收养");
            SQLUtil.updatePet(CacheDataBean.getLookingPet());
            cancelOnClick(null);
        }
    }

    class setView implements Runnable {
        PetBean pet;
        VolunteerBean volunteer;

        public setView(PetBean pet) {
            this.pet = pet;
            assert pet != null;
            this.volunteer = SQLUtil.findVolunteer(pet.getVno());
        }


        @Override
        public void run() {
            Platform.runLater(() -> {
                if (!"待领养".equals(pet.getIsapdated())) {
                    adoptIt.setVisible(false);
                }
                petName.setText(pet.getPname());
                volunteerID.setText(pet.getVno());
                volunteerName.setText(volunteer.getVname());
                perSex.setText(pet.getPsex());
                petWeight.setText(Integer.toString(pet.getPweight()));
                petType.setText(pet.getPtype());
                petApdated.setText(pet.getIsapdated());
                introduction.setText(pet.getDscrb());
            });
            Platform.runLater(() -> {
                goodsImage.setImage(new Image("file:" + Objects.requireNonNull(SQLUtil.getPetsPhoto(pet)).getPath()));
            });
        }
    }

    @FXML
    private ImageView goodsImage;

    @FXML
    private TextArea introduction;

    @FXML
    private Button adoptIt;

    @FXML
    private Button contactVolunteer;

    @FXML
    private TextField petName;

    @FXML
    private TextField volunteerID;

    @FXML
    private TextField perSex;

    @FXML
    private TextField petType;

    @FXML
    private TextField volunteerName;

    @FXML
    private Button cancel;

    @FXML
    private TextField petWeight;

    @FXML
    private Button modify;

    @FXML
    private TextField failReason;

    @FXML
    private TextField petApdated;

    @FXML
    private Button pass;

}

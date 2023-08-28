package adopterApplication.Controllers;

import adopterApplication.dataBean.*;
import adopterApplication.utils.AdpapplyUtil;
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
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class justBuyItController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(new setView(CacheDataBean.getLookingPet(), CacheDataBean.getNowAdopter())).start();
    }

    @FXML
    void cancelOnClick(ActionEvent event) {
        CacheDataBean.getWorkingPane().getChildren().clear();
        FXMLLoader loader = new FXMLLoader();	// 创建对象
        InputStream in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/discovery.fxml");
        if (CacheDataBean.getStatus() == StatusBean.OnIntroduction) {
            in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/introduction.fxml");
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
    void confirmForBuyOnClick(ActionEvent event) throws SQLException {
        PetBean pet = CacheDataBean.getLookingPet();
        assert pet != null;
        if (!"待领养".equals(pet.getIsapdated())) {
            DialogBox.warning("宠物已被领养!");
            cancelOnClick(null);return;
        }
        if (SQLUtil.getNumber(CacheDataBean.getNowAdopter().getAno()) >= 3) {
            DialogBox.warning("您已经领养了三只宠物！");
            cancelOnClick(null);return;
        }
        if (CacheDataBean.getNowAdopter().getMoney() < SQLUtil.getBond(pet.getPtype())) {
            DialogBox.warning("余额不足,请充值!");
            cancelOnClick(null);return;
        }
        if (Address.getText() == null || "".equals(Address.getText())) {
            DialogBox.warning("请填写收货地址!");return;
        }
        if (introduction.getText() == null || "".equals(introduction.getText())) {
            DialogBox.warning("请填写申请理由!");return;
        }
        pet.setIsapdated("试养期");

        AdpapplyBean adpapply = new AdpapplyBean();
        adpapply.setAdp_applyno(AdpapplyUtil.GenerateAdpapplyId());
        adpapply.setAno(CacheDataBean.getNowAdopter().getAno());
        adpapply.setPno(pet.getPno());
        adpapply.setApply_reason(introduction.getText());
        adpapply.setIsapproval("通过");
        adpapply.setRefuse_reason("");
        adpapply.setTrail_time(new Date(System.currentTimeMillis()));
        adpapply.setTraillenght(10);
        adpapply.setBondstate("已付");

        if (!SQLUtil.AdoptPet(adpapply, pet)) {
            DialogBox.warning("购买出现问题, 请重新下单!");
            return;
        }
        cancelOnClick(null);
        DialogBox.warning("领养成功!");
    }

    class setView implements Runnable {
        PetBean pet;
        VolunteerBean volunteer;
        AdopterBean adopter;

        public setView(PetBean pet, AdopterBean adopter) {
            this.pet = pet;
            assert pet != null;
            this.volunteer = SQLUtil.findVolunteer(pet.getVno());
            this.adopter = adopter;
        }
        @Override
        public void run() {
            Platform.runLater(() -> {
                petName.setText(pet.getPname());
                petId.setText(pet.getPno());
                volunteerName.setText(Objects.requireNonNull(SQLUtil.findVolunteer(pet.getVno())).getVname());
                volunteerId.setText(pet.getVno());
                adopterId.setText(CacheDataBean.getNowAdopter().getAno());
                goodsImage.setImage(new Image("file:" + Objects.requireNonNull(SQLUtil.getPetsPhoto(pet)).getPath()));
            });
        }
    }

    @FXML
    private ImageView goodsImage;

    @FXML
    private Button confirmForBuy;

    @FXML
    private Button cancel;

    @FXML
    private TextField petName;

    @FXML
    private TextField petId;

    @FXML
    private TextField volunteerName;

    @FXML
    private TextField volunteerId;

    @FXML
    private TextField adopterId;

    @FXML
    private TextField Address;

    @FXML
    private TextArea introduction;
}

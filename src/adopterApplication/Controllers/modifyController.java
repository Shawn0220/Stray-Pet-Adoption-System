package adopterApplication.Controllers;

import adopterApplication.dataBean.CacheDataBean;
import adopterApplication.dataBean.PetBean;
import adopterApplication.utils.DialogBox;
import adopterApplication.utils.PetUtil;
import adopterApplication.utils.SQLUtil;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class modifyController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(new setView(CacheDataBean.getLookingPet())).start();
    }

    @FXML
    void submitOnClick(ActionEvent event) throws IOException, SQLException {
        if (CacheDataBean.getPetPhoto() == null) {
            DialogBox.warning("请选择宠物照片");return;
        } else if (CacheDataBean.getPetPhoto().length() > 16777216) {
            DialogBox.warning("图片文件过大!");return;
        }
        if (petName.getText() == null || petName.getText().equals("")) {
            DialogBox.warning("请输入宠物名称");return;
        }
        if (petType.getText() == null || petType.getText().equals("")) {
            DialogBox.warning("请输入宠物类别");return;
        }
        if (sexMale.isSelected() == sexFemale.isSelected()) {
            DialogBox.warning("请选择宠物性别");
            return;
        }
        if (!Pattern.matches("^-?([1-9]\\d*|0)$", petWeight.getText())) {
            DialogBox.warning("请正确输入宠物体重");return;
        }
        if (introduction.getText() == null || introduction.getText().equals("")) {
            DialogBox.warning("请输入宠物描述");return;
        }

        PetBean pet = new PetBean(
                CacheDataBean.getLookingPet().getPno(),
                petName.getText(),
                sexMale.isSelected() ? "雄" : "雌",
                CacheDataBean.getNowVolunteer().getVno(),
                petType.getText(),
                Integer.parseInt(petWeight.getText()),
                introduction.getText(),
                "待领养"
        );
        SQLUtil.updatePet(pet);
        cancelOnClick(null);
    }

    @FXML
    void cancelOnClick(ActionEvent event) throws IOException {
        CacheDataBean.getWorkingPane().getChildren().clear();
        FXMLLoader loader = new FXMLLoader();	// 创建对象
        InputStream in = getClass().getResourceAsStream("/adopterApplication/resource/fxml/myDistribution.fxml");
        AnchorPane page = loader.load(in); // 对象方法的参数是InputStream，返回值是Object
        CacheDataBean.getWorkingPane().getChildren().add(page);
    }

    @FXML
    void uploadPhotoOnClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource Photo");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
        );
        File photo = fileChooser.showOpenDialog(CacheDataBean.getMyStage());
        if (photo == null) {
            return;
        } else if (photo.length() <= 16777216) {
            CacheDataBean.setPetPhoto(photo);
            Image image = new Image("file:" + CacheDataBean.getPetPhoto().getAbsolutePath());
            petPhoto.setImage(image);
        } else if (photo.length() > 16777216) {
            DialogBox.warning("图片大于 4M");
        }
    }

    class setView implements Runnable {
        PetBean pet;

        public setView(PetBean pet) {
            this.pet = pet;
        }

        @Override
        public void run() {
            Platform.runLater(()->{
                CacheDataBean.setPetPhoto(SQLUtil.getPetsPhoto(null));
                petName.setText(pet.getPname());
                petType.setText(pet.getPtype());
                introduction.setText(pet.getDscrb());
                if ("雄".equals(pet.getPsex())) {
                    sexMale.setSelected(true);
                } else {
                    sexFemale.setSelected(true);
                }
                petWeight.setText(String.valueOf(pet.getPweight()));
                petPhoto.setImage(new Image("file:" + CacheDataBean.getPetPhoto()));
            });
        }
    }

    @FXML
    private Button uploadPhoto;

    @FXML
    private ImageView petPhoto;

    @FXML
    private Button submit;

    @FXML
    private Button cancel;

    @FXML
    private TextField petName;

    @FXML
    private TextField petType;

    @FXML
    private TextArea introduction;

    @FXML
    private TextField petWeight;

    @FXML
    private CheckBox sexMale;

    @FXML
    private CheckBox sexFemale;

    @FXML
    void sexFemaleOnClick(ActionEvent event) {
        sexMale.setSelected(false);
    }

    @FXML
    void sexMaleOnclick(ActionEvent event) {
        sexFemale.setSelected(false);
    }


}

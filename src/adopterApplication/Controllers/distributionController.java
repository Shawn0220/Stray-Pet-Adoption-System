package adopterApplication.Controllers;

import adopterApplication.dataBean.CacheDataBean;
import adopterApplication.dataBean.PetBean;
import adopterApplication.dataBean.StatusBean;
import adopterApplication.utils.DialogBox;
import adopterApplication.utils.PetUtil;
import adopterApplication.utils.SQLUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class distributionController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CacheDataBean.setStatus(StatusBean.OnDistribution);
        CacheDataBean.setPetPhoto(null);
        goodsPhoto.setImage(new Image(String.valueOf(getClass().getResource("/adopterApplication/resource/images/choiceImage.png"))));
        // 把清单对象转换为JavaFX控件能够识别的数据对象
        ObservableList<String> obList = FXCollections.observableArrayList(SQLUtil.getPetType());
        // 设置下拉框的数据来源
        petType.setItems(obList);
        petType.getSelectionModel().selectFirst();
    }

    @FXML
    void releaseOnClick(ActionEvent event) throws SQLException {
        if (CacheDataBean.getPetPhoto() == null) {
            DialogBox.warning("请选择宠物照片");return;
        } else if (CacheDataBean.getPetPhoto().length() > 16777216) {
            DialogBox.warning("图片文件过大!");return;
        }
        if (petName.getText() == null || petName.getText().equals("")) {
            DialogBox.warning("请输入宠物名称");return;
        }
        if ("_请选择宠物类别_".equals(petType.getValue())) {
            DialogBox.warning("请选择宠物类别！");
            return;
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
                PetUtil.GeneratePetId(),
                petName.getText(),
                sexMale.isSelected() ? "雄" : "雌",
                CacheDataBean.getNowVolunteer().getVno(),
                petType.getValue(),
                Integer.parseInt(petWeight.getText()),
                introduction.getText(),
                "待领养"
        );
        SQLUtil.addPet(pet);
        refreshOnClick(null);
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
        }
        if (photo.length() <= 16777216) {
            CacheDataBean.setPetPhoto(photo);
            Image image = new Image("file:" + CacheDataBean.getPetPhoto().getAbsolutePath());
            goodsPhoto.setImage(image);
        } else if (photo.length() > 16777216) {
            DialogBox.warning("图片大于 16M");
        }
    }

    @FXML
    void refreshOnClick(ActionEvent event) {
        CacheDataBean.setPetPhoto(null);
        goodsPhoto.setImage(new Image(String.valueOf(getClass().getResource("/adopterApplication/resource/images/choiceImage.png"))));
        petName.setText("");
        petType.getSelectionModel().selectFirst();
        sexMale.setSelected(false);
        sexFemale.setSelected(false);
        petWeight.setText("");
        introduction.setText("");
    }

    @FXML   // 使得男女按钮仅一个可被选中
    void sexFemaleOnClick(ActionEvent event) {
        sexMale.setSelected(false);
    }

    @FXML
    void sexMaleOnclick(ActionEvent event) {
        sexFemale.setSelected(false);
    }

    @FXML
    private Button uploadPhoto;

    @FXML
    private ImageView goodsPhoto;

    @FXML
    private TextField petName;

    @FXML
    private  ComboBox<String>  petType;

    @FXML
    private TextArea introduction;

    @FXML
    private Button release;

    @FXML
    private Button refresh;

    @FXML
    private CheckBox sexMale;

    @FXML
    private CheckBox sexFemale;

    @FXML
    private TextField petWeight;

}

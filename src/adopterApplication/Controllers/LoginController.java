package adopterApplication.Controllers;

import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.CheckBox;
import javafx.stage.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import adopterApplication.dataBean.*;
import adopterApplication.utils.*;

/**
 * @author 11412
 */
public class LoginController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 加载 登陆界面图标
        Image icon = new Image(String.valueOf(getClass().getResource("/adopterApplication/resource/images/icon.png")));
        loginIcon.setImage(icon);
    }

    @FXML private ImageView loginIcon;
    @FXML private TextField userId;
    @FXML private PasswordField password;
    @FXML private Button register;
    @FXML private Button loginIn;
    @FXML
    private CheckBox volunteer;

    @FXML
    private CheckBox adopter;

    @FXML   // 使得男女按钮仅一个可被选中
    void volunteerOnClick(ActionEvent event) {
        adopter.setSelected(false);
    }

    @FXML
    void adopterOnClick(ActionEvent event) {
        volunteer.setSelected(false);
    }

    @FXML   // 点击 登录按钮
    void loginInOnClick(ActionEvent event) throws IOException {
        if (volunteer.isSelected() == adopter.isSelected()) {
            DialogBox.warning("请选择登陆身份");
            return;
        }
        if (userId.getText() == null || "".equals(userId.getText())) {
            DialogBox.warning("用户账号不能为空");
            return;
        }
        // 从数据库获取用户信息并判断是否在线
        if (volunteer.isSelected()) {
            CacheDataBean.setIdentity("volunteer");
            VolunteerBean nowVolunteer = SQLUtil.findVolunteerForLogIn(userId.getText(), password.getText());
            if (nowVolunteer == null) {
                DialogBox.warning("用户名或密码错误!");
                return;
            } else {
                CacheDataBean.setNowVolunteer(nowVolunteer);
            }
        } else if (adopter.isSelected()) {
            CacheDataBean.setIdentity("adopter");
            AdopterBean nowUser = SQLUtil.findAdopterForLogIn(userId.getText(), password.getText());
            if (nowUser == null) {
                DialogBox.warning("用户名或密码错误!");
                return;
            } else {
                CacheDataBean.setNowAdopter(nowUser);
            }
        }
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        CacheDataBean.getMyStage().setX((screenRectangle.getWidth() - 1200) / 2.0);
        CacheDataBean.getMyStage().setY((screenRectangle.getHeight()- 700) / 2.0 - 100);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/adopterApplication/resource/fxml/main.fxml"));
        Pane root = loader.load();
        CacheDataBean.getMyStage().setScene(new Scene(root, 1200, 700));
        // 窗口大小不可调整
        CacheDataBean.getMyStage().setResizable(false);
        CacheDataBean.getMyStage().setTitle("浪宠说");
    }

    @FXML   // 点击 注册按钮
    void registerOnClick(ActionEvent event) throws IOException {
//        // 注册窗口
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/adopterApplication/resource/fxml/register.fxml"));
//        Pane root = loader.load();
//
//        CacheDataBean.getMyStage().setScene(new Scene(root, 800, 500));
//        CacheDataBean.getMyStage().setTitle("浪宠说 注册页面");
    }
}

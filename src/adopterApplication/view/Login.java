package adopterApplication.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import adopterApplication.dataBean.CacheDataBean;

public class Login extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Class.forName("adopterApplication.utils.SQLUtil");
        // 保存 程序主窗口 对象
        CacheDataBean.setMyStage(primaryStage);
        // 加载 login 页面
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/adopterApplication/resource/fxml/login.fxml"));
        Pane root = loader.load();
        // 设置标题
        primaryStage.setTitle("浪宠说 登录页面");
        // 加载画布
        primaryStage.setScene(new Scene(root, 800, 500));
        // 获取屏幕大小
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        primaryStage.setX((screenRectangle.getWidth() - 800) / 2.0);
        // 使得左右居中, 上下定位
        primaryStage.setY((screenRectangle.getHeight()- 500) / 2.0 - 100);
        // 窗口大小不可调整
        primaryStage.setResizable(false);
        // 显示窗口
        primaryStage.show();

        // 窗口推出事件 用户登出, 退出子线程
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}

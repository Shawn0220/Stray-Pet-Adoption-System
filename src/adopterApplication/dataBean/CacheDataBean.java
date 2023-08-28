package adopterApplication.dataBean;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;

public class CacheDataBean {

    private static Stage myStage = null;
    private static AdopterBean nowAdopter = null;
    private static PetBean LookingPet = null;
    private static VolunteerBean nowVolunteer = null;
    private static String identity = null;
    private static File PetPhoto = null;
    private static StatusBean status = null;
    private static Pane workingPane = null;


    public static void logOut() {
        nowAdopter = null;
        LookingPet = null;
        nowVolunteer = null;
        identity = null;
        PetPhoto = null;
        status = null;
    }

    public static File getPetPhoto() {
        return PetPhoto;
    }

    public static void setPetPhoto(File petPhoto) {
        PetPhoto = petPhoto;
    }

    public static PetBean getLookingPet() {
        return LookingPet;
    }

    public static void setLookingPet(PetBean lookingPet) {
        LookingPet = lookingPet;
    }

    public static StatusBean getStatus() {
        return status;
    }

    public static void setStatus(StatusBean status) {
        CacheDataBean.status = status;
    }

    public static Pane getWorkingPane() {
        return workingPane;
    }

    public static void setWorkingPane(Pane workingPane) {
        CacheDataBean.workingPane = workingPane;
    }

    private CacheDataBean() {}  // 禁止实例化

    public static Stage getMyStage() {
        return myStage;
    }

    public static void setMyStage(Stage myStage) {
        CacheDataBean.myStage = myStage;
    }

    public static AdopterBean getNowAdopter() {
        return nowAdopter;
    }

    public static void setNowAdopter(AdopterBean nowAdopter) {
        CacheDataBean.nowAdopter = nowAdopter;
    }

    public static VolunteerBean getNowVolunteer() {
        return nowVolunteer;
    }

    public static void setNowVolunteer(VolunteerBean nowVolunteer) {
        CacheDataBean.nowVolunteer = nowVolunteer;
    }

    public static String getIdentity() {
        return identity;
    }

    public static void setIdentity(String identity) {
        CacheDataBean.identity = identity;
    }
}

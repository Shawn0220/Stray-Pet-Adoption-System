package adopterApplication.utils;

import adopterApplication.dataBean.AdpapplyBean;
import adopterApplication.dataBean.PetBean;
import adopterApplication.dataBean.VolunteerBean;
import javafx.application.Platform;
import adopterApplication.dataBean.AdopterBean;
import java.io.File;
import java.sql.*;
import java.util.*;

public class SQLUtil {
    static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static final String URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=PETHOME";
    static final String USER = "pethome";
    static final String PASSWORD = "pethome";


    static {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = getConnection();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            DialogBox.error(e);
        }
    }
    /**
     * 获取数据库连接
     * */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (Exception e) {
            Platform.runLater(() -> DialogBox.error(e));
        }
        return conn;
    }
    /**
     * Adopter登录
     * @param userId 用户账号
     * @param password 用户密码
     * @return 与数据库中数据对比，若账号密码正确则返回Adopter信息，错误则返回null
     * */
    public static AdopterBean findAdopterForLogIn(String userId, String password) {
        String sql = "select * from adopter where ano = ? and apsw = ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,userId);
            st.setString(2,password);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return AdopterUtil.ResultToUser(rs);
                }
            } catch (Exception inEx) {
                DialogBox.error(inEx);
                return null;
            }
        } catch (SQLException e) {
            DialogBox.error(e);
        }
        return null;
    }
    /**
     * Volunteer登录
     * @param userId 用户账号
     * @param password 用户密码
     * @return Volunteer信息
     * */
    public static VolunteerBean findVolunteerForLogIn(String userId, String password) {
        // 获取 用户 如果用户不在线
        String sql = "select * from volunteer where vno = ? and vpsw = ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,userId);
            st.setString(2,password);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return VolunteerUtil.ResultToVolunteer(rs);
                }
            } catch (Exception inEx) {
                DialogBox.error(inEx);
                return null;
            }
        } catch (SQLException e) {
            DialogBox.error(e);
        }
        return null;
    }
    /**
     * 宠物编号是否存在
     * @param pno 宠物编号
     * @return 该宠物是否存在
     * */
    public static boolean hasPet(String pno) {
        String sql = "select pno from pet where pno = ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,pno);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
    // 从数据库随机获取宠物信息
    public static List<PetBean> getRandomPets(int length) {
        List<PetBean> petsList = new LinkedList<>();
        String sql = "select Top 12 * from waitAdopt order by rand()";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    petsList.add(PetUtil.ResultToPet(rs));
                }
            } catch (Exception inEx) {
                Platform.runLater(()->DialogBox.error(inEx));
            }
        } catch (SQLException e) {
            Platform.runLater(()->DialogBox.error(e));
        }
        return petsList;
    }
    // 获取宠物图片
    public static File getPetsPhoto(PetBean pet) {
        Random random = new Random();
        return new File("PetsPhoto/" + random.nextInt(10) + ".jpg");
    }
    /**
     * 管理员发布宠物信息 使用存储过程 pet_insert
     * @param pet 存储宠物信息的类的对象
     * */
    public static void addPet(PetBean pet) {
        String add = "EXEC pet_insert ?, ?, ?, ?, ?, ?, ?, ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(add);
            // 将用户数据输入SQL语句
            PetUtil.PetAddToSQL(st, pet);
            st.executeUpdate();
        } catch (SQLException e ) {
            DialogBox.error(e);
        }
    }
    // 更新宠物信息
    public static boolean updatePet(PetBean pet) throws SQLException {
        String update = "update pet set pno = ?, pname = ?, psex = ?, vno = ?, ptype = ?, pweight = ?, dscrb = ?, isapdated = ? where pno = ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(update);
            // 将用户数据输入SQL语句
            PetUtil.PetAddToSQL(st, pet);
            st.setString(9, pet.getPno());
            st.executeUpdate();
        } catch (SQLException e ) {
            DialogBox.error(e);
            return false;
        }
        return true;
    }
    // 删除宠物信息
    public static void deletePetById(String petId) {
        String sql = "delete from pet where pno = ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, petId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // 从数据库中通过 volunteerId 寻找宠物
    public static List<PetBean> getPetByVolunteerId(String vno) {
        String sql = "select * from pet where vno = ?";
        List<PetBean> goodsList = new LinkedList<PetBean>();
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, vno);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    goodsList.add(PetUtil.ResultToPet(rs));
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goodsList;
    }
    // 获取宠物保证金
    public static float getBond(String ptype) {
        String sql = "select bond from pettype where ptype = ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, ptype);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    return Float.parseFloat(rs.getString("bond"));
                }
            } catch (Exception inEx) {
                Platform.runLater(()->DialogBox.error(inEx));
            }
        } catch (SQLException e) {
            Platform.runLater(()->DialogBox.error(e));
        }
        return 0;
    }
    // 查询志愿者信息 by Id
    public static VolunteerBean findVolunteer(String vno) {
        String sql = "select * from volunteer where vno = ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,vno);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return VolunteerUtil.ResultToVolunteer(rs);
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    // 宠物编号是否存在
    public static boolean hasAdpapply(String adp_applyno) {
        String sql = "select adp_applyno from adpapply where adp_applyno = ?";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,adp_applyno);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
    /**
     * 领养宠物
     * @param adpapply 领养申请信息
     * @param pet 宠物信息
     * @return 领养是否成功
     * */
    public static boolean AdoptPet(AdpapplyBean adpapply, PetBean pet) throws SQLException {
        String sql = "insert into adpapply(adp_applyno, ano, pno, apply_reason, isapproval, refuse_reason, trail_time, traillenght, formal_time, bondstate) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            AdpapplyUtil.AdpapplyAddToSQL(st, adpapply);
            if (st.executeUpdate() <= 0 || !SQLUtil.updatePet(pet)) {
                pet.setIsapdated("待领养");
                SQLUtil.updatePet(pet);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            pet.setIsapdated("待领养");
            SQLUtil.updatePet(pet);
            return false;
        }
        return true;
    }
    // 查询宠物
    public static List<PetBean> getPetsByAdopterId(String adopterId) {
        String sql = "select pet.* from pet,adpapply where pet.pno = adpapply.pno and ano = ?";
        List<PetBean> adopterList = new LinkedList<PetBean>();
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, adopterId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    adopterList.add(PetUtil.ResultToPet(rs));
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adopterList;
    }
    // 查询宠物
    public static List<PetBean> getGoodsByKeyWordsAndVounteerId(String[] userKeys, String vno) {
        String sql = "select * from pet where vno = ?";
        List<PetBean> goodsList = new LinkedList<>();
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, vno);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    PetBean pet = PetUtil.findPetsByKey(rs, userKeys);
                    if (pet != null) {
                        goodsList.add(pet);
                    }
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goodsList;
    }
    // 查询宠物
    public static List<PetBean> getGoodsByKeyWordsAndAdopterId(String[] userKeys, String ano) {
        String sql = "select * from pet,adpapply where pet.pno = adpapply.pno and vno = ?";
        List<PetBean> goodsList = new LinkedList<>();
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, ano);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    PetBean pet = PetUtil.findPetsByKey(rs, userKeys);
                    if (pet != null) {
                        goodsList.add(pet);
                    }
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goodsList;
    }
    /**
     * 查询检索宠物信息
     * @param userKeys 用户输入的检索关键词
     * @return 检索到的宠物列表
     * */
    public static List<PetBean> getGoodsByKeyWords(String[] userKeys) {
        String sql = "select * from pet";
        List<PetBean> goodsList = new LinkedList<>();
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    PetBean pet = PetUtil.findPetsByKey(rs, userKeys);
                    if (pet != null) {
                        goodsList.add(pet);
                    }
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goodsList;
    }
    // 获取宠物类别
    public static List<String> getPetType() {
        List<String> petTypes = new ArrayList<>(Collections.singletonList("_请选择宠物类别_"));
        String sql = "select distinct ptype from pettype";
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    petTypes.add(rs.getString("ptype"));
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return petTypes;
    }
    // 获取领养数量
    public static int getNumber(String ano) {
        String sql = "select count(*) from adpapply where ano = ?";
        int number = 0;
        try (Connection conn = SQLUtil.getConnection()) {
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, ano);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (Exception inEx) {
                inEx.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}

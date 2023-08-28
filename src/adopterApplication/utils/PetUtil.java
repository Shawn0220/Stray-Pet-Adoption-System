package adopterApplication.utils;


import adopterApplication.dataBean.PetBean;
import net.coobird.thumbnailator.Thumbnails;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class PetUtil {

    public static void PetAddToSQL(PreparedStatement st, PetBean pet) throws SQLException {
        st.setString(1,pet.getPno());
        st.setString(2,pet.getPname());
        st.setString(3,pet.getPsex());
        st.setString(4,pet.getVno());
        st.setString(5,pet.getPtype());
        st.setInt(6,pet.getPweight());
        st.setString(7,pet.getDscrb());
        st.setString(8,pet.getIsapdated());
    }

    public static PetBean ResultToPet(ResultSet rs) throws SQLException, IOException {
        PetBean pet = new PetBean();
        pet.setPno(rs.getString("pno"));
        pet.setPname(rs.getString("pname"));
        pet.setPsex(rs.getString("psex"));
        pet.setVno(rs.getString("vno"));
        pet.setPtype(rs.getString("ptype"));
        pet.setPweight(rs.getInt("pweight"));
        pet.setDscrb(rs.getString("dscrb"));
        pet.setIsapdated(rs.getString("isapdated"));
        return pet;
    }

    public static String GeneratePetId() {
        Random random = new Random();
        StringBuilder buffer = new StringBuilder();
        do {
            buffer.setLength(0);
            for(int i = 0; i < 8; i++){
                int number = random.nextInt(3);
                long result=0;
                switch(number){
                    case 0:
                        result=Math.round(Math.random()*25+65);
                        buffer.append((char) result);
                        break;
                    case 1:
                        result=Math.round(Math.random()*25+97);
                        buffer.append((char) result);
                        break;
                    case 2:
                        buffer.append(new Random().nextInt(10));
                        break;
                    default:break;
                }
            }
        } while (SQLUtil.hasPet(buffer.toString()));
        return buffer.toString();
    }
    /**
     * 判断rs中的数据行是否符合检索要求
     * @param rs 数据库检索结果
     * @param userKeys 用户输入的检索关键词
     * @return 检索到的宠物列表
     * */
    public static PetBean findPetsByKey(ResultSet rs, String[] userKeys) throws IOException, SQLException {
        PetBean pet = ResultToPet(rs);
        String petString = pet.toString();
        for (String user : userKeys) {
            if (petString.contains(user)) {
                return pet;
            }
        }
        return null;
    }

//    public static GoodsBean findGoodsByKey(ResultSet rs, String[] userKeys) throws SQLException, IOException {
//        GoodsBean goods = ResultToGoods(rs);
//        String goodsString = goods.toString();
//            for (String user : userKeys) {
//                if (goodsString.contains(user)) {
//                    return goods;
//                }
//            }
//        return null;
//    }
}

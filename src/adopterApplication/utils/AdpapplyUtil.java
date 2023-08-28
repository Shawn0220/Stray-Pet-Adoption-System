package adopterApplication.utils;

import adopterApplication.dataBean.AdopterBean;
import adopterApplication.dataBean.AdpapplyBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;

public class AdpapplyUtil {

    public static AdpapplyBean ResultToAdpapply(ResultSet rs) throws SQLException {
        AdpapplyBean adpapply = new AdpapplyBean();
        adpapply.setAdp_applyno(rs.getString("adp_applyno"));
        adpapply.setAno(rs.getString("ano"));
        adpapply.setPno(rs.getString("pno"));
        adpapply.setApply_reason(rs.getString("apply_reason"));
        adpapply.setIsapproval(rs.getString("isapproval"));
        adpapply.setTrail_time(rs.getDate("trail_time"));
        adpapply.setTraillenght(rs.getInt("traillenght"));
        adpapply.setFormal_time(rs.getDate("formal_time"));
        adpapply.setBondstate(rs.getString("bondstate"));
        return adpapply;
    }

    public static void AdpapplyAddToSQL(PreparedStatement st, AdpapplyBean adpapply) throws SQLException {
        st.setString(1, adpapply.getAdp_applyno());
        st.setString(2,adpapply.getAno());
        st.setString(3,adpapply.getPno());
        st.setString(4,adpapply.getApply_reason());
        st.setString(5,adpapply.getIsapproval());
        st.setString(6,adpapply.getRefuse_reason());
        st.setDate(7,  new java.sql.Date(adpapply.getTrail_time().getTime()));
        st.setInt(8,adpapply.getTraillenght());
        st.setDate(9, adpapply.getFormal_time() == null ? null : new java.sql.Date(adpapply.getFormal_time().getTime()));
        st.setString(10,adpapply.getBondstate());
    }

    public static String GenerateAdpapplyId() {
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
        } while (SQLUtil.hasAdpapply(buffer.toString()));
        return buffer.toString();
    }
}

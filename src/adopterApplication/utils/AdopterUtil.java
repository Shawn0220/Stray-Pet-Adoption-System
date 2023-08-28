package adopterApplication.utils;

import adopterApplication.dataBean.AdopterBean;

import java.sql.*;

public class AdopterUtil {

    public static AdopterBean ResultToUser(ResultSet rs) throws SQLException {
        AdopterBean adopter = new AdopterBean();
        adopter.setAno(rs.getString("ano"));
        adopter.setAsex(rs.getString("asex"));
        adopter.setTel(rs.getString("tel"));
        adopter.setAloc(rs.getString("aloc"));
        adopter.setAname(rs.getString("aname"));
        adopter.setApsw(rs.getString("apsw"));
        adopter.setContractcnt(rs.getInt("contractcnt"));
        adopter.setMoney(rs.getInt("money"));
        return adopter;
    }

    public static void UserAddToSQL(PreparedStatement st, AdopterBean adopter) throws SQLException {
        st.setString(1, adopter.getAno());
        st.setString(2, adopter.getAsex());
        st.setString(3, adopter.getTel());
        st.setString(4, adopter.getAloc());
        st.setString(5, adopter.getAname());
        st.setString(6, adopter.getApsw());
        st.setInt(7, adopter.getContractcnt());
        st.setInt(8, adopter.getMoney());
    }
}

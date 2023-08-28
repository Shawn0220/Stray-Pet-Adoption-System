package adopterApplication.utils;

import adopterApplication.dataBean.AdopterBean;
import adopterApplication.dataBean.VolunteerBean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VolunteerUtil {


    public static VolunteerBean ResultToVolunteer(ResultSet rs) throws SQLException {
        VolunteerBean volunteer = new VolunteerBean();

        volunteer.setVno(rs.getString("vno"));
        volunteer.setVsex(rs.getString("vsex"));
        volunteer.setTel(rs.getString("tel"));
        volunteer.setVloc(rs.getString("vloc"));
        volunteer.setVname(rs.getString("vname"));
        volunteer.setVpsw(rs.getString("vpsw"));

        return volunteer;
    }

    public static void UserAddToSQL(PreparedStatement st, VolunteerBean volunteer) throws SQLException {
        st.setString(1, volunteer.getVno());
        st.setString(2, volunteer.getVsex());
        st.setString(3, volunteer.getTel());
        st.setString(4, volunteer.getVloc());
        st.setString(5, volunteer.getVname());
        st.setString(6, volunteer.getVpsw());
    }
}

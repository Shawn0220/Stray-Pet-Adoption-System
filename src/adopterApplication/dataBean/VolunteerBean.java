package adopterApplication.dataBean;

public class VolunteerBean {
    private String vno;
    private String vsex;
    private String tel;
    private String vloc;
    private String vname;
    private String vpsw;

    public VolunteerBean(String vno, String vsex, String tel, String vloc, String vname, String vpsw) {
        this.vno = vno;
        this.vsex = vsex;
        this.tel = tel;
        this.vloc = vloc;
        this.vname = vname;
        this.vpsw = vpsw;
    }

    public VolunteerBean() {
    }

    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    public String getVsex() {
        return vsex;
    }

    public void setVsex(String vsex) {
        this.vsex = vsex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getVloc() {
        return vloc;
    }

    public void setVloc(String vloc) {
        this.vloc = vloc;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVpsw() {
        return vpsw;
    }

    public void setVpsw(String vpsw) {
        this.vpsw = vpsw;
    }
}

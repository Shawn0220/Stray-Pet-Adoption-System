package adopterApplication.dataBean;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

public class PetBean {

    private String pno;
    private String pname;
    private String psex;
    private String vno;
    private String ptype;
    private int pweight;
    private String dscrb;
    private String isapdated;

    public PetBean(String pno, String pname, String psex, String vno, String ptype, int pweight, String dscrb, String isapdated) {
        this.pno = pno;
        this.pname = pname;
        this.psex = psex;
        this.vno = vno;
        this.ptype = ptype;
        this.pweight = pweight;
        this.dscrb = dscrb;
        this.isapdated = isapdated;
    }

    public PetBean() {
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPsex() {
        return psex;
    }

    public void setPsex(String psex) {
        this.psex = psex;
    }

    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public int getPweight() {
        return pweight;
    }

    public void setPweight(int pweight) {
        this.pweight = pweight;
    }

    public String getDscrb() {
        return dscrb;
    }

    public void setDscrb(String dscrb) {
        this.dscrb = dscrb;
    }

    public String getIsapdated() {
        return isapdated;
    }

    public void setIsapdated(String isapdated) {
        this.isapdated = isapdated;
    }

    @Override
    public String toString() {
        return "PetBean{" +
                "pno='" + pno + '\'' +
                ", pname='" + pname + '\'' +
                ", psex='" + psex + '\'' +
                ", vno='" + vno + '\'' +
                ", ptype='" + ptype + '\'' +
                ", pweight=" + pweight +
                ", dscrb='" + dscrb + '\'' +
                ", isapdated='" + isapdated + '\'' +
                '}';
    }
}

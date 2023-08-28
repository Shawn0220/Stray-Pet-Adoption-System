package adopterApplication.dataBean;

import java.util.Date;

/**
 * @author 11412
 */
public class AdpapplyBean {
    public AdpapplyBean(String adp_applyno, String ano, String pno, String apply_reason, String isapproval, String refuse_reason, Date trail_time, int traillenght, Date formal_time, String bondstate) {
        this.adp_applyno = adp_applyno;
        this.ano = ano;
        this.pno = pno;
        this.apply_reason = apply_reason;
        this.isapproval = isapproval;
        this.refuse_reason = refuse_reason;
        this.trail_time = trail_time;
        this.traillenght = traillenght;
        this.formal_time = formal_time;
        this.bondstate = bondstate;
    }

    private String adp_applyno = null;
    private String ano = null;
    private String pno = null;
    private String apply_reason = null;
    private String isapproval = null;
    private String refuse_reason = null;
    private Date trail_time = null;
    private int traillenght = 0;
    private Date formal_time = null;
    private String bondstate = null;

    public AdpapplyBean() {
    }

    public String getAdp_applyno() {
        return adp_applyno;
    }

    public void setAdp_applyno(String adp_applyno) {
        this.adp_applyno = adp_applyno;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getApply_reason() {
        return apply_reason;
    }

    public void setApply_reason(String apply_reason) {
        this.apply_reason = apply_reason;
    }

    public String getIsapproval() {
        return isapproval;
    }

    public void setIsapproval(String isapproval) {
        this.isapproval = isapproval;
    }

    public String getRefuse_reason() {
        return refuse_reason;
    }

    public void setRefuse_reason(String refuse_reason) {
        this.refuse_reason = refuse_reason;
    }

    public Date getTrail_time() {
        return trail_time;
    }

    public void setTrail_time(Date trail_time) {
        this.trail_time = trail_time;
    }

    public int getTraillenght() {
        return traillenght;
    }

    public void setTraillenght(int traillenght) {
        this.traillenght = traillenght;
    }

    public Date getFormal_time() {
        return formal_time;
    }

    public void setFormal_time(Date formal_time) {
        this.formal_time = formal_time;
    }

    public String getBondstate() {
        return bondstate;
    }

    public void setBondstate(String bondstate) {
        this.bondstate = bondstate;
    }
}

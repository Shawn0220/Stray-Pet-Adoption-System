package adopterApplication.dataBean;

import java.util.Date;

public class AdopterBean {

    private String ano;
    private String asex;
    private String tel;
    private String aloc;
    private String aname;
    private String apsw;
    private int contractcnt;
    private int money;

    public AdopterBean() {}

    public AdopterBean(String ano, String asex, String tel, String aloc, String aname, String apsw, int contractcnt, int money) {
        this.ano = ano;
        this.asex = asex;
        this.tel = tel;
        this.aloc = aloc;
        this.aname = aname;
        this.apsw = apsw;
        this.contractcnt = contractcnt;
        this.money = money;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getAsex() {
        return asex;
    }

    public void setAsex(String asex) {
        this.asex = asex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAloc() {
        return aloc;
    }

    public void setAloc(String aloc) {
        this.aloc = aloc;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getApsw() {
        return apsw;
    }

    public void setApsw(String apsw) {
        this.apsw = apsw;
    }

    public int getContractcnt() {
        return contractcnt;
    }

    public void setContractcnt(int contractcnt) {
        this.contractcnt = contractcnt;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}

package com.xgx.dw.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>
 * 购电记录
 * </p>
 *
 * @author xgx123
 * @since 2018-04-16
 */
@Entity
public class Purchase implements Serializable, Parcelable {
    private static final long serialVersionUID = -1532244169446127925L;
    @Id
    private Integer id;
    /**
     * 创建时间
     */
    @Property
    private String createtime;
    @Property
    private String audiotype;
    @Property
    private String deleted;
    @Property
    /**
     * 营业厅id
     */
    private String countyid;
    /**
     * 营业厅名称
     */
    @Property
    private String countyname;
    /**
     * 终端编号
     */
    @Property
    private String terminalcode;
    /**
     * 终端名称
     */
    @Property
    private String terminalname;
    /**
     * 用户id
     */
    @Property
    private String userid;
    /**
     * 所属用户
     */
    private String username;
    /**
     * 所属台区
     */
    @Property
    private String taiquname;
    /**
     * 操作单号
     */
    @Property
    private String opcode;
    /**
     * 购电金额
     */
    @Property
    private String amt;
    /**
     * 操作员
     */
    @Property
    private String opuser;
    /**
     * 购电结果
     */
    @Property
    private String result;
    @Property
    private String zuidabh;
    @Property
    private String metatype;
    /**
     * 操作类型
     */
    @Property
    private String optype;
    /**
     * 购买前金额
     */
    @Property
    private String amtbefore;
    /**
     * 购买后金额
     */
    @Property
    private String amtafter;
    /**
     * 购电状态
     * 0 表示未上传，1 表示已同步,9表示已同步电表，但未同步服务器
     *
     */
    @Property
    private Integer status;
    /**
     * 电价id
     */
    @Property
    private Integer priceid;
    @Property
    private String dybl;
    /**
     * 是否同步倍率
     */
    @Property
    private Integer isBl;
    /**
     * 是否保电投入
     */
    @Property
    private Integer isBd;
    /**
     * 报警金额
     */
    @Property
    private String amtbj;
    /**
     * 电流倍率
     */
    @Property
    private String dlbl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getAudiotype() {
        return audiotype;
    }

    public void setAudiotype(String audiotype) {
        this.audiotype = audiotype;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCountyid() {
        return countyid;
    }

    public void setCountyid(String countyid) {
        this.countyid = countyid;
    }

    public String getCountyname() {
        return countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }

    public String getTerminalcode() {
        return terminalcode;
    }

    public void setTerminalcode(String terminalcode) {
        this.terminalcode = terminalcode;
    }

    public String getTerminalname() {
        return terminalname;
    }

    public void setTerminalname(String terminalname) {
        this.terminalname = terminalname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTaiquname() {
        return taiquname;
    }

    public void setTaiquname(String taiquname) {
        this.taiquname = taiquname;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getOpuser() {
        return opuser;
    }

    public void setOpuser(String opuser) {
        this.opuser = opuser;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getZuidabh() {
        return zuidabh;
    }

    public void setZuidabh(String zuidabh) {
        this.zuidabh = zuidabh;
    }

    public String getMetatype() {
        return metatype;
    }

    public void setMetatype(String metatype) {
        this.metatype = metatype;
    }

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public String getAmtbefore() {
        return amtbefore;
    }

    public void setAmtbefore(String amtbefore) {
        this.amtbefore = amtbefore;
    }

    public String getAmtafter() {
        return amtafter;
    }

    public void setAmtafter(String amtafter) {
        this.amtafter = amtafter;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPriceid() {
        return priceid;
    }

    public void setPriceid(Integer priceid) {
        this.priceid = priceid;
    }

    public String getDybl() {
        return dybl;
    }

    public void setDybl(String dybl) {
        this.dybl = dybl;
    }

    public Integer getIsBl() {
        return isBl;
    }

    public void setIsBl(Integer isBl) {
        this.isBl = isBl;
    }

    public Integer getIsBd() {
        return isBd;
    }

    public void setIsBd(Integer isBd) {
        this.isBd = isBd;
    }

    public String getAmtbj() {
        return amtbj;
    }

    public void setAmtbj(String amtbj) {
        this.amtbj = amtbj;
    }

    public String getDlbl() {
        return dlbl;
    }

    public void setDlbl(String dlbl) {
        this.dlbl = dlbl;
    }

    protected Purchase(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        createtime = in.readString();
        audiotype = in.readString();
        deleted = in.readString();
        countyid = in.readString();
        countyname = in.readString();
        terminalcode = in.readString();
        terminalname = in.readString();
        userid = in.readString();
        username = in.readString();
        taiquname = in.readString();
        opcode = in.readString();
        amt = in.readString();
        opuser = in.readString();
        result = in.readString();
        zuidabh = in.readString();
        metatype = in.readString();
        optype = in.readString();
        amtbefore = in.readString();
        amtafter = in.readString();
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        if (in.readByte() == 0) {
            priceid = null;
        } else {
            priceid = in.readInt();
        }
        dybl = in.readString();
        if (in.readByte() == 0) {
            isBl = null;
        } else {
            isBl = in.readInt();
        }
        if (in.readByte() == 0) {
            isBd = null;
        } else {
            isBd = in.readInt();
        }
        amtbj = in.readString();
        dlbl = in.readString();
    }

    @Generated(hash = 1181833382)
    public Purchase(Integer id, String createtime, String audiotype, String deleted,
            String countyid, String countyname, String terminalcode,
            String terminalname, String userid, String username, String taiquname,
            String opcode, String amt, String opuser, String result, String zuidabh,
            String metatype, String optype, String amtbefore, String amtafter,
            Integer status, Integer priceid, String dybl, Integer isBl,
            Integer isBd, String amtbj, String dlbl) {
        this.id = id;
        this.createtime = createtime;
        this.audiotype = audiotype;
        this.deleted = deleted;
        this.countyid = countyid;
        this.countyname = countyname;
        this.terminalcode = terminalcode;
        this.terminalname = terminalname;
        this.userid = userid;
        this.username = username;
        this.taiquname = taiquname;
        this.opcode = opcode;
        this.amt = amt;
        this.opuser = opuser;
        this.result = result;
        this.zuidabh = zuidabh;
        this.metatype = metatype;
        this.optype = optype;
        this.amtbefore = amtbefore;
        this.amtafter = amtafter;
        this.status = status;
        this.priceid = priceid;
        this.dybl = dybl;
        this.isBl = isBl;
        this.isBd = isBd;
        this.amtbj = amtbj;
        this.dlbl = dlbl;
    }

    @Generated(hash = 1281646125)
    public Purchase() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(createtime);
        dest.writeString(audiotype);
        dest.writeString(deleted);
        dest.writeString(countyid);
        dest.writeString(countyname);
        dest.writeString(terminalcode);
        dest.writeString(terminalname);
        dest.writeString(userid);
        dest.writeString(username);
        dest.writeString(taiquname);
        dest.writeString(opcode);
        dest.writeString(amt);
        dest.writeString(opuser);
        dest.writeString(result);
        dest.writeString(zuidabh);
        dest.writeString(metatype);
        dest.writeString(optype);
        dest.writeString(amtbefore);
        dest.writeString(amtafter);
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
        if (priceid == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(priceid);
        }
        dest.writeString(dybl);
        if (isBl == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isBl);
        }
        if (isBd == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(isBd);
        }
        dest.writeString(amtbj);
        dest.writeString(dlbl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Purchase> CREATOR = new Creator<Purchase>() {
        @Override
        public Purchase createFromParcel(Parcel in) {
            return new Purchase(in);
        }

        @Override
        public Purchase[] newArray(int size) {
            return new Purchase[size];
        }
    };
}

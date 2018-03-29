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
 * 电度电费价格管理
 * </p>
 *
 * @author xgx123
 * @since 2018-03-27
 */
@Entity
public class Price implements Serializable, Parcelable {


    private static final long serialVersionUID = -7234299413146039430L;
    /**
     * id
     */
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
    /**
     * 营业厅id
     */
    @Property
    private String countyid;
    /**
     * 营业厅名称
     */
    @Property
    private String countyname;
    /**
     * 价格名称
     */
    @Property
    private String pricename;
    /**
     * 价格类型
     */
    @Property
    private String pricetype;
    /**
     * 总电价
     */
    @Property
    private String totalprice;
    /**
     * 尖电价
     */
    @Property
    private String pricea;
    /**
     * 峰电价
     */
    private String priceb;
    /**
     * 平电价
     */
    @Property
    private String pricec;
    /**
     * 谷电价
     */
    @Property

    private String priced;

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

    public String getPricename() {
        return pricename;
    }

    public void setPricename(String pricename) {
        this.pricename = pricename;
    }

    public String getPricetype() {
        return pricetype;
    }

    public void setPricetype(String pricetype) {
        this.pricetype = pricetype;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getPricea() {
        return pricea;
    }

    public void setPricea(String pricea) {
        this.pricea = pricea;
    }

    public String getPriceb() {
        return priceb;
    }

    public void setPriceb(String priceb) {
        this.priceb = priceb;
    }

    public String getPricec() {
        return pricec;
    }

    public void setPricec(String pricec) {
        this.pricec = pricec;
    }

    public String getPriced() {
        return priced;
    }

    public void setPriced(String priced) {
        this.priced = priced;
    }

    protected Price(Parcel in) {
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
        pricename = in.readString();
        pricetype = in.readString();
    }

    @Generated(hash = 1830632768)
    public Price(Integer id, String createtime, String audiotype, String deleted,
            String countyid, String countyname, String pricename, String pricetype,
            String totalprice, String pricea, String priceb, String pricec,
            String priced) {
        this.id = id;
        this.createtime = createtime;
        this.audiotype = audiotype;
        this.deleted = deleted;
        this.countyid = countyid;
        this.countyname = countyname;
        this.pricename = pricename;
        this.pricetype = pricetype;
        this.totalprice = totalprice;
        this.pricea = pricea;
        this.priceb = priceb;
        this.pricec = pricec;
        this.priced = priced;
    }

    @Generated(hash = 812905808)
    public Price() {
    }

    public static final Creator<Price> CREATOR = new Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel in) {
            return new Price(in);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(createtime);
        parcel.writeString(audiotype);
        parcel.writeString(deleted);
        parcel.writeString(countyid);
        parcel.writeString(countyname);
        parcel.writeString(pricename);
        parcel.writeString(pricetype);
    }
}

package com.xgx.dw.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.math.BigDecimal;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>
 * 营业厅管理
 * </p>
 *
 * @author xgx123
 * @since 2018-03-27
 */
@Entity
public class County implements Serializable, Parcelable {


    private static final long serialVersionUID = -3055114502461446866L;
    /**
     * 主键id
     */
    @Id
    private Integer id;
    /**
     * 营业厅编号
     */
    @Property
    private String countyid;
    /**
     * 营业厅名称
     */
    @Property
    private String countyname;
    /**
     * 排序号
     */
    @Property
    private String sortid;
    /**
     * 备注
     */
    @Property
    private String remark;
    /**
     * 地址
     */
    @Property
    private String address;
    /**
     * 联系人
     */
    @Property
    private String contact;
    /**
     * 联系电话
     */
    @Property
    private String tel;
    /**
     * 创建时间
     */
    @Property
    private String createtime;
    @Property
    private String audiotype;
    @Property
    private String deleted;

    protected County(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        countyid = in.readString();
        countyname = in.readString();
        remark = in.readString();
        address = in.readString();
        contact = in.readString();
        tel = in.readString();
        createtime = in.readString();
        audiotype = in.readString();
        deleted = in.readString();
    }

    @Generated(hash = 1435839203)
    public County(Integer id, String countyid, String countyname, String sortid,
            String remark, String address, String contact, String tel,
            String createtime, String audiotype, String deleted) {
        this.id = id;
        this.countyid = countyid;
        this.countyname = countyname;
        this.sortid = sortid;
        this.remark = remark;
        this.address = address;
        this.contact = contact;
        this.tel = tel;
        this.createtime = createtime;
        this.audiotype = audiotype;
        this.deleted = deleted;
    }

    @Generated(hash = 1991272252)
    public County() {
    }

    public static final Creator<County> CREATOR = new Creator<County>() {
        @Override
        public County createFromParcel(Parcel in) {
            return new County(in);
        }

        @Override
        public County[] newArray(int size) {
            return new County[size];
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
        parcel.writeString(countyid);
        parcel.writeString(countyname);
        parcel.writeString(remark);
        parcel.writeString(address);
        parcel.writeString(contact);
        parcel.writeString(tel);
        parcel.writeString(createtime);
        parcel.writeString(audiotype);
        parcel.writeString(deleted);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountyid() {
        return this.countyid;
    }

    public void setCountyid(String countyid) {
        this.countyid = countyid;
    }

    public String getCountyname() {
        return this.countyname;
    }

    public void setCountyname(String countyname) {
        this.countyname = countyname;
    }

    public String getSortid() {
        return this.sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getAudiotype() {
        return this.audiotype;
    }

    public void setAudiotype(String audiotype) {
        this.audiotype = audiotype;
    }

    public String getDeleted() {
        return this.deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}

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
 * 台区
 * </p>
 *
 * @author xgx123
 * @since 2018-03-27
 */
@Entity
public class Taiqu implements Serializable, Parcelable {


    private static final long serialVersionUID = -1441673194315256907L;
    @Id
    private Integer id;
    @Property
    private String recid;
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
     * 编号
     */
    @Property
    private String code;
    /**
     * 名称
     */
    @Property
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Taiqu(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        recid = in.readString();
        createtime = in.readString();
        audiotype = in.readString();
        deleted = in.readString();
        countyid = in.readString();
        countyname = in.readString();
        code = in.readString();
        name = in.readString();
    }

    @Generated(hash = 869300026)
    public Taiqu(Integer id, String recid, String createtime, String audiotype,
            String deleted, String countyid, String countyname, String code,
            String name) {
        this.id = id;
        this.recid = recid;
        this.createtime = createtime;
        this.audiotype = audiotype;
        this.deleted = deleted;
        this.countyid = countyid;
        this.countyname = countyname;
        this.code = code;
        this.name = name;
    }

    @Generated(hash = 1472317231)
    public Taiqu() {
    }

    public static final Creator<Taiqu> CREATOR = new Creator<Taiqu>() {
        @Override
        public Taiqu createFromParcel(Parcel in) {
            return new Taiqu(in);
        }

        @Override
        public Taiqu[] newArray(int size) {
            return new Taiqu[size];
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
        parcel.writeString(recid);
        parcel.writeString(createtime);
        parcel.writeString(audiotype);
        parcel.writeString(deleted);
        parcel.writeString(countyid);
        parcel.writeString(countyname);
        parcel.writeString(code);
        parcel.writeString(name);
    }
}

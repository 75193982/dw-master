package com.xgx.dw.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author xgx123
 * @since 2018-03-19
 */
@Entity
public class SysUser implements Serializable, Parcelable {


    private static final long serialVersionUID = -3629470186624067374L;
    /**
     * 主键id
     */
    @Id
    private Integer id;
    /**
     * 头像
     */
    @Property
    private String avatar;
    /**
     * 账号
     */
    @Property
    private String account;
    /**
     * 密码
     */
    @Property
    private String password;
    /**
     * md5密码盐
     */
    @Property
    private String salt;
    /**
     * 名字
     */
    @Property
    private String name;
    /**
     * 生日
     */
    @Property
    private Date birthday;
    /**
     * 性别（1：男 2：女）
     */
    @Property
    private Integer sex;
    /**
     * 电子邮件
     */
    @Property
    private String email;
    /**
     * 电话
     */
    @Property
    private String phone;
    /**
     * 角色id
     */
    @Property
    private String roleid;
    /**
     * 部门id
     */
    @Property
    private Integer deptid;
    /**
     * 状态(1：启用  2：冻结  3：删除）
     */
    @Property
    private Integer status;
    /**
     * 创建时间
     */
    @Property
    private String createtime;
    /**
     * 保留字段
     */
    @Property
    private Integer version;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public Integer getDeptid() {
        return deptid;
    }

    public void setDeptid(Integer deptid) {
        this.deptid = deptid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    protected SysUser(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        avatar = in.readString();
        account = in.readString();
        password = in.readString();
        salt = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            sex = null;
        } else {
            sex = in.readInt();
        }
        email = in.readString();
        phone = in.readString();
        roleid = in.readString();
        if (in.readByte() == 0) {
            deptid = null;
        } else {
            deptid = in.readInt();
        }
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        createtime = in.readString();
        if (in.readByte() == 0) {
            version = null;
        } else {
            version = in.readInt();
        }
    }

    @Generated(hash = 377029907)
    public SysUser(Integer id, String avatar, String account, String password, String salt, String name, Date birthday, Integer sex, String email, String phone, String roleid, Integer deptid, Integer status, String createtime, Integer version) {
        this.id = id;
        this.avatar = avatar;
        this.account = account;
        this.password = password;
        this.salt = salt;
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
        this.email = email;
        this.phone = phone;
        this.roleid = roleid;
        this.deptid = deptid;
        this.status = status;
        this.createtime = createtime;
        this.version = version;
    }

    @Generated(hash = 1097522457)
    public SysUser() {
    }

    public static final Creator<SysUser> CREATOR = new Creator<SysUser>() {
        @Override
        public SysUser createFromParcel(Parcel in) {
            return new SysUser(in);
        }

        @Override
        public SysUser[] newArray(int size) {
            return new SysUser[size];
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
        parcel.writeString(avatar);
        parcel.writeString(account);
        parcel.writeString(password);
        parcel.writeString(salt);
        parcel.writeString(name);
        if (sex == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(sex);
        }
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(roleid);
        if (deptid == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(deptid);
        }
        if (status == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(status);
        }
        parcel.writeString(createtime);
        if (version == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(version);
        }
    }
}

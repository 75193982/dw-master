package com.xgx.dw.bean;

import java.util.Date;

/**
 * <p>
 * 操作日志
 * </p>
 *
 * @author xgx123
 * @since 2018-04-16
 */
public class Oplog {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String createtime;
    private String audiotype;
    private String deleted;
    /**
     * 营业厅id
     */
    private String countyid;
    /**
     * 营业厅名称
     */
    private String countyname;
    /**
     * 终端编号
     */
    private String terminalcode;
    /**
     * 操作类型
     */
    private String optype;
    /**
     * 操作结果
     */
    private String opresult;
    /**
     * 发送指令
     */
    private String sendcommand;
    /**
     * 接收内容
     */
    private String receivetext;


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

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public String getOpresult() {
        return opresult;
    }

    public void setOpresult(String opresult) {
        this.opresult = opresult;
    }

    public String getSendcommand() {
        return sendcommand;
    }

    public void setSendcommand(String sendcommand) {
        this.sendcommand = sendcommand;
    }

    public String getReceivetext() {
        return receivetext;
    }

    public void setReceivetext(String receivetext) {
        this.receivetext = receivetext;
    }


    @Override
    public String toString() {
        return "MtzOplog{" +
                "id=" + id +
                ", createtime=" + createtime +
                ", audiotype=" + audiotype +
                ", deleted=" + deleted +
                ", countyid=" + countyid +
                ", countyname=" + countyname +
                ", terminalcode=" + terminalcode +
                ", optype=" + optype +
                ", opresult=" + opresult +
                ", sendcommand=" + sendcommand +
                ", receivetext=" + receivetext +
                "}";
    }
}

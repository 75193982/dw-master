package com.xgx.dw.ui.fragment.dummy;

public class DummyContent {
    public String content;
    public String details;
    public int id;
    public int res;

    public DummyContent(int paramString1, String paramString2, String paramString3, int paramInt) {
        this.id = paramString1;
        this.content = paramString2;
        this.details = paramString3;
        this.res = paramInt;
    }

    public String getContent() {
        return this.content;
    }

    public String getDetails() {
        return this.details;
    }

    public int getId() {
        return this.id;
    }

    public int getRes() {
        return this.res;
    }

    public void setContent(String paramString) {
        this.content = paramString;
    }

    public void setDetails(String paramString) {
        this.details = paramString;
    }

    public void setId(int paramString) {
        this.id = paramString;
    }

    public void setRes(int paramInt) {
        this.res = paramInt;
    }
}

package com.xgx.dw.net;

import java.io.Serializable;
import java.util.List;

public class LzyResponse<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public String result;
    public String resultInfo;

    public Object model;
    public String token;
    public String randomKey;
}
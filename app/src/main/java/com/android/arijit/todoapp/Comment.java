package com.android.arijit.todoapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Comment implements Serializable {
    private int cid;
    private String cmtext, cmdate;

    public Comment() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        cmdate = sdf.format(new java.util.Date());
    }

    public Comment(int cid, String cmtext, String cmdate) {
        this.cid = cid;
        this.cmtext = cmtext;
        this.cmdate = cmdate;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCmtext() {
        return cmtext;
    }

    public void setCmtext(String cmtext) {
        this.cmtext = cmtext;
    }

    public String getCmdate() {
        return cmdate;
    }

    public void setCmdate(String cmdate) {
        this.cmdate = cmdate;
    }
}

package me.ningsk.svideo.sdk.internal.common.project;

import java.util.ArrayList;

import me.ningsk.svideo.sdk.external.struct.effect.ActionBase;

/**
 * <p>描述：<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 18 32<br>
 * 版本：v1.0<br>
 */
public class WaterMark {
    private float xCoord;
    private float yCoord;
    private float width;
    private float height;
    private String uri;
    private int mId;
    public ArrayList<ActionBase> mActions = new ArrayList();

    public WaterMark() {
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public float getxCoord() {
        return this.xCoord;
    }

    public void setxCoord(float xCoord) {
        this.xCoord = xCoord;
    }

    public float getyCoord() {
        return this.yCoord;
    }

    public void setyCoord(float yCoord) {
        this.yCoord = yCoord;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}


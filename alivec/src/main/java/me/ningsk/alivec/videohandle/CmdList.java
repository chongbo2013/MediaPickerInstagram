package me.ningsk.alivec.videohandle;

import java.util.ArrayList;

/**
 * <p>描述：命令集合<p>
 * 作者：ningsk<br>
 * 日期：2018/11/19 10 54<br>
 * 版本：v1.0<br>
 */
public class CmdList extends ArrayList<String> {

    public CmdList append(String s) {
        this.add(s);
        return this;
    }

    public CmdList append(int i) {
        this.add(i + "");
        return this;
    }

    public CmdList append(float f) {
        this.add(f + "");
        return this;
    }

    public CmdList append(StringBuilder sb) {
        this.add(sb.toString());
        return this;
    }

    public CmdList append(String[] ss) {
        for (String s:ss) {
            if(!s.replace(" ","").equals("")) {
                this.add(s);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String s : this) {
            sb.append(" ").append(s);
        }
        return sb.toString();
    }
}

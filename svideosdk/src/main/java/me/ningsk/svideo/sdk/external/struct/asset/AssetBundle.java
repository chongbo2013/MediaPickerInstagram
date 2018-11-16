package me.ningsk.svideo.sdk.external.struct.asset;

/**
 * <p>描述：资源包<p>
 * 作者：ningsk<br>
 * 日期：2018/11/15 15 45<br>
 * 版本：v1.0<br>
 */
public interface AssetBundle {

    String getSceneURL();

    String getMediaURIString();

    String getMediaURIString(String uri);
}

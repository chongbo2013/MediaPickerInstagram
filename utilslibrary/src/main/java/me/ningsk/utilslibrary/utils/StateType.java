package me.ningsk.utilslibrary.utils;

/**
 * <p>描述：播放状态<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 17 59<br>
 * 版本：v1.0<br>
 */
public enum  StateType {
    ERROR,      // 出错
    IDLE,       // 空闲
    PREPARING,  // 准备中
    PREPARED,   // 已经准备完成
    PLAYING,    // 播放过程
    PAUSED,     // 暂停
    STOP,       // 停止
    RELEASED,   // 已释放
    COMPLETED,  // 正常播放完毕
}

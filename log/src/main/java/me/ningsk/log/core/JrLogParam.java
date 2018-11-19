package me.ningsk.log.core;


import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;

public class JrLogParam
{
    public static String generatePushParams(Map<String, String> args, String logLevel, String module, String subModule, int eventId, String requestID)
    {
        StringBuilder params = new StringBuilder("&");
        params.append("t").append("=").append(String.valueOf(System.currentTimeMillis())).append("&");
        params.append("ll").append("=").append(logLevel).append("&");
        params.append("lv").append("=").append("1").append("&");
        params.append("pd").append("=").append("svideo").append("&");
        params.append("md").append("=").append(module).append("&");
        params.append("sm").append("=").append(subModule).append("&");
        params.append("hn").append("=").append(getHostIp()).append("&");
        params.append("bi").append("=").append("").append("&");
        params.append("ri").append("=").append(requestID).append("&");
        params.append("e").append("=").append(String.valueOf(eventId)).append("&");
        params.append("args").append("=").append(transcodeArgs(args)).append("&");
        params.append("tt").append("=").append("phone").append("&");
        params.append("dm").append("=").append(JrLogCommon.DEVICE_MODEL).append("&");
        params.append("os").append("=").append("android").append("&");
        params.append("ov").append("=").append(JrLogCommon.OS_VERSION).append("&");
        params.append("av").append("=").append("3.5.0").append("&");
        params.append("uuid").append("=").append(JrLogCommon.UUID).append("&");
        params.append("dn").append("=").append("").append("&");
        params.append("co").append("=").append("").append("&");
        params.append("ua").append("=").append("&");
        params.append("uat").append("=").append("").append("&");
        params.append("app_id").append("=").append(JrLogCommon.APPLICATION_ID).append("&");
        params.append("app_n").append("=").append(JrLogCommon.APPLICATION_NAME).append("&");
        params.append("cdn_ip").append("=").append("").append("&");
        params.append("r").append("=").append("jerei").append("&");
        return params.toString();
    }

    public static String transcodeArgs(Map<String, String> args) {
        if (args != null) {
            StringBuilder argsStr = new StringBuilder();
            for (Map.Entry entry : args.entrySet()) {
                argsStr.append((String)entry.getKey()).append("=").append((String)entry.getValue()).append("&");
            }
            argsStr.deleteCharAt(argsStr.lastIndexOf("&"));
            try {
                return URLEncoder.encode(argsStr.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static String getHostIp()
    {
        Enumeration en;
        try
        {
            for (en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface)en.nextElement();
                Enumeration ipAddr = intf.getInetAddresses();
                while (ipAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)ipAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                        return inetAddress.getHostAddress();
                }
            }
        } catch (SocketException ex) {
        }
        catch (Exception e) {
        }
        return null;
    }
}

package AI_Extensions;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@SimpleObject(external = true)
public class AIXBase extends AndroidNonvisibleComponent implements Component {
    public static String SERVER = "http://192.168.1.4:5000/aix";

    private final Activity activity;

    public AIXBase(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
    }

    public Component getInstance() {
        return this;
    }

    public String getExtensionName() {
        return getInstance().getClass().getName();
    }

    public void raiseError(String functionName, int errorCode, String msg) {
        Log.i("Base", "raiseError");
        Log.i("Base", "extensionName = " + getExtensionName());
        Log.i("Base", "functionName = " + functionName);
        Log.i("Base", "errorCode = " + errorCode);
        Log.i("Base", "msg = " + msg);
        this.form.dispatchErrorOccurredEvent(getInstance(), functionName, 3300, new Object[]{Integer.valueOf(errorCode), getExtensionName(), msg});
    }

    public void post(final String path, final Map<String, Object> params, final AsyncCallbackPair<JSONObject> callback) {
        Log.i("Base", "POST " + SERVER + path);
        AsynchUtil.runAsynchronously(new Runnable() {
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(AIXBase.SERVER + path);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);
                    PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.write(AIXBase.this.toJSONString(params));
                    out.close();
                    if (callback != null) {
                        Log.i("Base", "Server Response Code " + conn.getResponseCode());
                        if (conn.getResponseCode() == 200) {
                            ByteArrayOutputStream buf = new ByteArrayOutputStream();
                            InputStream in = conn.getInputStream();
                            byte[] chunk = new byte[4096];
                            int len = in.read(chunk, 0, 4096);
                            while (len != -1) {
                                buf.write(chunk, 0, len);
                                len = in.read(chunk, 0, 4096);
                            }
                            final JSONObject resp = new JSONObject(new String(buf.toByteArray(), StandardCharsets.UTF_8));
                            if (resp.getInt("code") == 0) {
                                AIXBase.this.activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        callback.onSuccess(resp);
                                    }
                                });
                            } else {
                                final String msg = "Inference error, server message: " + resp.getString("msg");
                                AIXBase.this.raiseError("Base.post", resp.getInt("code"), msg);
                                AIXBase.this.activity.runOnUiThread(new Runnable() {
                                    public void run() {
                                        callback.onFailure(msg);
                                    }
                                });
                            }
                        } else {
                            final String msg = "Server responded with code " + conn.getResponseCode();
                            AIXBase.this.raiseError("Base.post", conn.getResponseCode(), msg);
                            AIXBase.this.activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    callback.onFailure(msg);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    AIXBase.this.raiseError("Base.post", -1, "Error while communicating with server, " + e.toString());
                } finally {
                    if (conn != null)
                        try {
                            conn.disconnect();
                        } catch (Exception exception) {
                        }
                }
            }
        });
    }

    public String toJSONString(Map<String, Object> map) {
        if (map.size() == 0)
            return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append('"').append(entry.getKey()).append('"').append(':');
            Object value = entry.getValue();
            if (value instanceof String) {
                sb.append('"').append((String) value).append('"');
            } else {
                sb.append(value.toString());
            }
            sb.append(',');
        }
        sb.setCharAt(sb.length() - 1, '}');
        return sb.toString();
    }

    public String b64encode(byte[] data) {
        return Base64.encodeToString(data, 2);
    }

    public byte[] b64decode(String b64str) {
        return Base64.decode(b64str, 2);
    }
}

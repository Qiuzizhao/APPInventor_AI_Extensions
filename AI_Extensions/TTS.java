package AI_Extensions;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.util.AsyncCallbackPair;
import com.google.appinventor.components.runtime.util.MediaUtil;

import android.media.MediaPlayer;
import android.util.Base64;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

@DesignerComponent(version = 1,
    description = "Text to Speech component that generates audio via server",
    category = ComponentCategory.EXTENSION,
    nonVisible = true,
    iconName = "images/extension.png")
@UsesPermissions(permissionNames = "android.permission.INTERNET")
@UsesLibraries(libraries = "json.jar")
@SimpleObject(external = true)
public class TTS extends AIXBase implements OnDestroyListener {

    private static final String PATH = "/tts";
    
    private MediaPlayer mediaPlayer;
    private String audioBase64 = "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qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxP8lZAH4AMGG3aqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqpMQU1FMy4xMDCqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq//NkxHwAAANIAAAAAKqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq";

    private boolean isPlaying = false;

    public TTS(ComponentContainer container) {
        super(container);
        mediaPlayer = new MediaPlayer();
        form.registerForOnDestroy(this);
    }

    // Server property
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Server() {
        return SERVER;
    }

    @DesignerProperty(editorType = "string", defaultValue = "http://192.168.1.4:5000/aix")
    @SimpleProperty
    public void Server(String url) {
        SERVER = url;
    }


    // Speak function
    @SimpleFunction(description = "Speaks the given text")
    public void Speak(String text) {
        try {
            BeforeSpeaking();
            
            Map<String, Object> params = new HashMap<>();
            params.put("operation", "tts");
            params.put("text", text);

            post(PATH, params, new AsyncCallbackPair<JSONObject>() {
                public void onSuccess(JSONObject response) {
                    try {
                        audioBase64 = response.getString("data");
                        playAudio();
                    } catch (Exception e) {
                        onFailure(e.getMessage());
                    }
                }

                public void onFailure(String message) {
                    AfterSpeaking(false);
                    raiseError("TTS.Speak", -1, message);
                }
            });
        } catch (Exception e) {
            AfterSpeaking(false);
            raiseError("TTS.Speak", -1, e.toString());
        }
    }

    // Stop function
    @SimpleFunction(description = "Stops the current speech")
    public void Stop() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            isPlaying = false;
            AfterSpeaking(false);
        }
    }

    // Play function
    @SimpleFunction(description = "play the current speech")
    public void playAudio() {
        try {
            // Convert base64 to temp file
            byte[] audioData = Base64.decode(audioBase64, Base64.DEFAULT);
            File tempFile = File.createTempFile("tts_audio", ".mp3", form.getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(audioData);
            fos.close();

            // Play the audio
            mediaPlayer.reset();
            mediaPlayer.setDataSource(tempFile.getPath());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                    AfterSpeaking(true);
                }
            });
            mediaPlayer.start();
            isPlaying = true;

        } catch (Exception e) {
            AfterSpeaking(false);
            raiseError("TTS.playAudio", -1, e.toString());
        }
    }

    @SimpleEvent(description = "Event raised before speaking starts")
    public void BeforeSpeaking() {
        EventDispatcher.dispatchEvent(this, "BeforeSpeaking");
    }

    @SimpleEvent(description = "Event raised after speaking ends")
    public void AfterSpeaking(boolean success) {
        EventDispatcher.dispatchEvent(this, "AfterSpeaking", success);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
} 
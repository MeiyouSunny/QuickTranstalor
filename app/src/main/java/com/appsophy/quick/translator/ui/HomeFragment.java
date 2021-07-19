package com.appsophy.quick.translator.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;
import com.baidu.ocr.sdk.model.WordSimple;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.google.gson.Gson;
import com.appsophy.quick.translator.R;
import com.appsophy.quick.translator.databinding.FragmentHomeBinding;
import com.appsophy.quick.translator.event.Event;
import com.appsophy.quick.translator.event.EventUtil;
import com.appsophy.quick.translator.model.Language;
import com.appsophy.quick.translator.model.Record;
import com.appsophy.quick.translator.model.TranslateResult;
import com.appsophy.quick.translator.records.IRecordManager;
import com.appsophy.quick.translator.records.RecordManager;
import com.appsophy.quick.translator.util.FileUtil;
import com.appsophy.quick.translator.util.FormattingUtil;
import com.appsophy.quick.translator.util.LanguageUtil;
import com.appsophy.quick.translator.util.ResUtil;
import com.appsophy.quick.translator.util.SystemUtil;
import com.appsophy.quick.translator.util.TTSPlayer;
import com.appsophy.quick.translator.util.ToastUtil;
import com.appsophy.quick.translator.util.VoiceRecognizer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import likly.dollar.$;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private final int REQUEST_SELECT_LANGUAGE_FROM = 2;
    private final int REQUEST_SELECT_LANGUAGE_TO = 3;

    private String appId = "20210714000888211";
    private String key = "PYb_o0S1HXwUv5czSq3t";

    FragmentHomeBinding mBindRoot;

    VoiceRecognizer mVoiceRecognizer;

    Language mLanguageFrom, mLanguageTo;
    String mTextFrom, mTextTo;
    IRecordManager mRecordManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        mBindRoot = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, true);
        initViews(mBindRoot.getRoot());
        return mBindRoot.getRoot();
    }

    private void initViews(View rootView) {
        mBindRoot.openOCR.setOnClickListener(this);
        mBindRoot.translate.setOnClickListener(this);
        mBindRoot.voice.setOnClickListener(this);
        mBindRoot.lanSelectFrom.setOnClickListener(this);
        mBindRoot.lanSelectTo.setOnClickListener(this);
        mBindRoot.exchange.setOnClickListener(this);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                mVoiceRecognizer = new VoiceRecognizer(getContext().getApplicationContext());
                List<Language> languages = LanguageUtil.parseRegions(getContext().getApplicationContext());
                mLanguageFrom = languages.get(0);
                mLanguageTo = languages.get(2);

                mBindRoot.setLanFrom(mLanguageFrom);
                mBindRoot.setLanTo(mLanguageTo);
                mBindRoot.setResUtil(new ResUtil(getContext()));

                refreshRecords();
            }
        }, 500);

        mRecordManager = new RecordManager();

        mBindRoot.etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final String input = mBindRoot.etInput.getText().toString().trim();
                int count = 0;
                if (!TextUtils.isEmpty(input))
                    count = input.length();
                mBindRoot.inputCount.setText(count + "/500");
            }
        });
    }

    public void openOCR() {
        requestPermissions();
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent intent = new Intent(getContext(), CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getContext().getApplicationContext()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        //有返回数据的启动
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == 1) {
            // 通用文字识别参数设置
            GeneralParams param = new GeneralParams();
            param.setDetectDirection(true);
            param.setImageFile(FileUtil.getSaveFile(getContext().getApplicationContext()));

            // 调用通用文字识别服务
            OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
                @Override
                public void onResult(GeneralResult result) {
                    StringBuilder sb = new StringBuilder();
                    // 调用成功，返回GeneralResult对象
                    String wordStr;
                    for (WordSimple wordSimple : result.getWordList()) {
                        // Word类包含位置信息
                        Word word = (Word) wordSimple;
                        wordStr = word.getWords();
                        sb.append(wordStr);
                        //调用格式化
                        if (FormattingUtil.getIsPunctuation(wordStr)) {
                            sb.append("\n    ");
                        }
                    }
                    final String data = sb.toString().trim();
                    if (!TextUtils.isEmpty(data)) {
                        mBindRoot.etInput.setText(data);
                        mBindRoot.etInput.setSelection(data.length());
                    }
                }

                @Override
                public void onError(OCRError error) {
                    // 调用失败，返回OCRError对象
                    ToastUtil.showToast(getContext().getApplicationContext(), "识别失败");
                }
            });
        } else if (requestCode == REQUEST_SELECT_LANGUAGE_FROM) {
            mLanguageFrom = (Language) data.getSerializableExtra("language");
            mBindRoot.setLanFrom(mLanguageFrom);
        } else if (requestCode == REQUEST_SELECT_LANGUAGE_TO) {
            mLanguageTo = (Language) data.getSerializableExtra("language");
            mBindRoot.setLanTo(mLanguageTo);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.openOCR) {
            openOCR();
        } else if (v.getId() == R.id.translate) {
            translate();
        } else if (v.getId() == R.id.voice) {
            voice();
        } else if (v.getId() == R.id.lanSelectFrom) {
            selectLanguage(REQUEST_SELECT_LANGUAGE_FROM);
        } else if (v.getId() == R.id.lanSelectTo) {
            selectLanguage(REQUEST_SELECT_LANGUAGE_TO);
        } else if (v.getId() == R.id.exchange) {
            exchangeLanguage();
        }
    }

    private void exchangeLanguage() {
        if (TextUtils.equals(mLanguageFrom.codeBaidu, "auto")) {
            $.toast().text(R.string.auto_not_switch).show();
            return;
        }
        Language lanTemp = mLanguageFrom;
        mLanguageFrom = mLanguageTo;
        mLanguageTo = lanTemp;
        mBindRoot.setLanFrom(mLanguageFrom);
        mBindRoot.setLanTo(mLanguageTo);
    }

    private void selectLanguage(int requestSelectLanguage) {
        Intent intent = new Intent(getContext(), LanguageActivity.class);
        if (requestSelectLanguage == REQUEST_SELECT_LANGUAGE_TO) {
            intent.putExtra("to", true);
        }
        startActivityForResult(intent, requestSelectLanguage);
    }

    private void voice() {
        requestPermissions();

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            mVoiceRecognizer.startRecognize(new VoiceRecognizer.VoiceRecognizeListener() {
                @Override
                public void onVoiceResult(String result) {
                    if (!TextUtils.isEmpty(result)) {
                        mBindRoot.etInput.setText(result);
                        mBindRoot.etInput.setSelection(result.length());
                    }
                }
            });
        }
    }

    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playTTS(String text) {
        TTSPlayer.getInstance(getContext().getApplicationContext()).playTTS(text);
    }

    private void translate() {
        mTextFrom = mBindRoot.etInput.getText().toString().trim();
        if (!mTextFrom.isEmpty() || !"".equals(mTextFrom)) {//不为空
            String salt = num(1);//随机数
            //拼接一个字符串然后加密
            String spliceStr = appId + mTextFrom + salt + key;//根据百度要求 拼接
            String sign = stringToMD5(spliceStr);//将拼接好的字符串进行MD5加密   作为一个标识
            //异步Get请求访问网络
            asyncGet(mTextFrom, mLanguageFrom.codeBaidu, mLanguageTo.codeBaidu, salt, sign);
        }
    }

    /**
     * 异步Get请求
     *
     * @param content  要翻译的内容
     * @param fromType 翻译源语言
     * @param toType   翻译后语言
     * @param salt     随机数
     * @param sign     标识
     */
    private void asyncGet(String content, String fromType, String toType, String salt, String sign) {
        String httpsStr = "https://fanyi-api.baidu.com/api/trans/vip/translate";
        //拼接请求的地址
        String url = httpsStr +
                "?appid=" + appId + "&q=" + content + "&from=" + fromType + "&to=" +
                toType + "&salt=" + salt + "&sign=" + sign;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                //异常返回
                goToUIThread(e.toString(), 0);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //正常返回
                goToUIThread(response.body().string(), 1);

            }
        });
    }

    /**
     * 接收到返回值后，回到UI线程操作页面变化
     *
     * @param object 接收一个返回对象
     * @param key    表示正常还是异常
     */
    private void goToUIThread(final Object object, final int key) {
        //切换到主线程处理数据
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (key == 0) {//异常返回
                    Log.e("MainActivity", object.toString());
                } else {//正常返回
                    final TranslateResult result = new Gson().fromJson(object.toString(), TranslateResult.class);

                    mTextTo = result.getTrans_result().get(0).getDst();

                    autoCopyAndSpeak();

                    saveRecord();
                    refreshRecords();
                }
            }
        });
    }

    private void refreshRecords() {
        List<Record> records = mRecordManager.getRecords();
        if (records != null && records.size() > 0) {
            mBindRoot.repeatView.viewManager().bind(records);
        } else {
            mBindRoot.repeatView.layoutAdapterManager().showEmptyView();
        }
    }

    private void saveRecord() {
        Record record = new Record();
        record.fromLanguage = mLanguageFrom;
        record.toLanguage = mLanguageTo;
        record.fromText = mTextFrom;
        record.toText = mTextTo;
        mRecordManager.insertRecord(record);
    }

    /**
     * 随机数 (根据百度的要求需要一个随机数)
     */
    public static String num(int a) {
        Random r = new Random(a);
        int ran1 = 0;
        for (int i = 0; i < 5; i++) {
            ran1 = r.nextInt(100);
            System.out.println(ran1);
        }
        return String.valueOf(ran1);
    }

    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    private void autoCopyAndSpeak() {
        if (SystemUtil.getAutoCopyFlag()) {
            SystemUtil.copyToClipboard(getActivity().getApplicationContext(), mTextTo);
        }
        if (SystemUtil.getAutoSpeakFlag()) {
            playTTS(mTextTo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEvent(Event event) {
        if (EventUtil.isRefreshRecordsEvent(event)) {
            refreshRecords();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
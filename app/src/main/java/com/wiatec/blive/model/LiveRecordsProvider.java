package com.wiatec.blive.model;

import com.px.common.constant.CommonApplication;
import com.px.common.utils.FileUtil;
import com.px.common.utils.Logger;
import com.px.common.utils.TimeUtil;
import com.wiatec.blive.pojo.LiveRecordsInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by patrick on 30/10/2017.
 * create time : 4:51 PM
 */

public class LiveRecordsProvider {

    private String recordPath = CommonApplication.context.getExternalFilesDir("record").getAbsolutePath();

    public void loadLiveRecords(final LoadListener<List<LiveRecordsInfo>> loadListener){
        try {
            Flowable.just(recordPath)
                    .subscribeOn(Schedulers.io())
                    .map(new Function<String, File[]>() {
                        @Override
                        public File[] apply(String s) throws Exception {
                            return FileUtil.listFiles(s);
                        }
                    })
                    .map(new Function<File[], List<LiveRecordsInfo>>() {
                        @Override
                        public List<LiveRecordsInfo> apply(File[] files) throws Exception {
                            List<LiveRecordsInfo> liveRecordsInfoList = new ArrayList<>();
                            if(files != null && files.length > 0) {
                                for (File file : files) {
                                    if (file.getName().contains(".mp4")) {
                                        LiveRecordsInfo liveRecordsInfo = new LiveRecordsInfo();
                                        liveRecordsInfo.setTitle(file.getName().substring(0, file.getName().length() - 4));
                                        liveRecordsInfo.setPlayUrl(file.getAbsolutePath());
                                        liveRecordsInfo.setModifyTime(TimeUtil.getStringTime(file.lastModified()));
                                        liveRecordsInfoList.add(liveRecordsInfo);
                                    }
                                }
                            }
                            return liveRecordsInfoList;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<LiveRecordsInfo>>() {
                        @Override
                        public void accept(List<LiveRecordsInfo> liveRecordsInfos) throws Exception {
                            if (liveRecordsInfos != null) {
                                loadListener.onSuccess(true, liveRecordsInfos);
                            } else {
                                loadListener.onSuccess(false, null);
                            }
                        }
                    });
        }catch (Exception e){
            loadListener.onSuccess(false, null);
            Logger.d(e.getMessage());
        }
    }
}

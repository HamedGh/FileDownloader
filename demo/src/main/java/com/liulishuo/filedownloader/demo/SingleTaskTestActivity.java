package com.liulishuo.filedownloader.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Jacksgong on 12/21/15.
 */
public class SingleTaskTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        savePath1 = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tmp1";
        savePath2 = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "tmp2";
        savePath3 = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "chunked_data_tmp1";
        savePath4 = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "chunked_data_tmp2";

        assignViews();

        initNormalDatasAction();
        initChunkTransferEncodingDatasAction();
    }


    private void initNormalDatasAction() {
        // task 1
        startBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadId1 = createDownloadTask(1).start();
            }
        });

        pauseBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloader.getImpl().pause(downloadId1);
            }
        });

        deleteBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(savePath1).delete();
            }
        });

        // task 2
        startBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadId2 = createDownloadTask(2).start();
            }
        });

        pauseBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloader.getImpl().pause(downloadId2);
            }
        });

        deleteBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(savePath2).delete();
            }
        });

    }

    private void initChunkTransferEncodingDatasAction() {
        // task 3
        startBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadId3 = createDownloadTask(3).start();
            }
        });

        pauseBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloader.getImpl().pause(downloadId3);
            }
        });

        deleteBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(savePath3).delete();
            }
        });

        // task 4
        startBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadId4 = createDownloadTask(4).start();
            }
        });

        pauseBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDownloader.getImpl().pause(downloadId4);
            }
        });

        deleteBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new File(savePath4).delete();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pause(downloadId1);
        FileDownloader.getImpl().pause(downloadId2);
    }

    private BaseDownloadTask createDownloadTask(final int position) {
        final ViewHolder tag;
        final String url;
        final String path;

        HashMap<String, String> header = new HashMap<String, String>();

        switch (position) {
            case 1:
//                url = Constant.BIG_FILE_URLS[2];
                url = "http://api.irangrammy.com/api/v4/en/music/11238-5e570e44ae82f0f77133aad4f7789078";
                tag = new ViewHolder(new WeakReference<>(this), progressBar1, null, 1);
                header.put("token","12-c5c856982961f5ec59cad075015409f17c2a1b99");
                header.put("uuid","1");
                path = savePath1;
                break;
            case 2:
                url = Constant.BIG_FILE_URLS[3];
                tag = new ViewHolder(new WeakReference<>(this), progressBar2, null, 2);
                path = savePath2;
                break;
            case 3:
                url = Constant.CHUNKED_TRANSFER_ENCODING_DATAS[0];
                tag = new ViewHolder(new WeakReference<>(this), progressBar3, detailTv3, 3);
                path = savePath3;
                break;
            default:
                url = Constant.CHUNKED_TRANSFER_ENCODING_DATAS[1];
                tag = new ViewHolder(new WeakReference<>(this), progressBar4, detailTv4, 4);
                path = savePath4;
                break;

        }

        return FileDownloader.getImpl().create(url)
                .setHeader(header)
                .setPath(path)
                .setCallbackProgressTimes(300)
                .setTag(tag)
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        ((ViewHolder)task.getTag()).updateProgress(soFarBytes, totalBytes);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        ((ViewHolder)task.getTag()).updateError(e);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                        ((ViewHolder)task.getTag()).updatePaused();
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        ((ViewHolder)task.getTag()).updateCompleted(task);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                        ((ViewHolder)task.getTag()).updateWarn();
                    }
                });

    }

    private static class ViewHolder {
        private ProgressBar pb;
        private TextView tv;
        private int position;

        private WeakReference<SingleTaskTestActivity> weakReferenceContext;

        public ViewHolder(WeakReference<SingleTaskTestActivity> weakReferenceContext,
                          final ProgressBar pb, final TextView tv, final int position) {
            this.weakReferenceContext = weakReferenceContext;
            this.pb = pb;
            this.tv = tv;
            this.position = position;
        }

        public void updateProgress(final int sofar, final int total) {
            if (total == -1) {
                // chunked transfer encoding data
                pb.setIndeterminate(true);
            } else {
                pb.setMax(total);
                pb.setProgress(sofar);
            }

            if (tv != null) {
                tv.setText(String.format("sofar: %d total: %d", sofar, total));
            }
        }

        public void updatePaused() {
            toast(String.format("paused %d", position));
            pb.setIndeterminate(false);
        }

        public void updateWarn() {
            toast(String.format("warn %d", position));
            pb.setIndeterminate(false);
        }

        public void updateError(final Throwable ex) {
            toast(String.format("error %d %s", position, ex.getMessage()));
            pb.setIndeterminate(false);
        }

        public void updateCompleted(final BaseDownloadTask task) {


            toast(String.format("completed %d %s", position, task.getPath()));

            if (tv != null) {
                tv.setText(String.format("sofar: %d total: %d",
                        task.getSmallFileSoFarBytes(), task.getSmallFileTotalBytes()));
            }
            pb.setIndeterminate(false);
            pb.setMax(task.getSmallFileTotalBytes());
            pb.setProgress(task.getSmallFileSoFarBytes());
        }

        private void toast(final String msg) {
            if (this.weakReferenceContext != null && this.weakReferenceContext.get() != null) {
                Toast.makeText(this.weakReferenceContext.get(), msg, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private int downloadId1;
    private int downloadId2;
    private int downloadId3;
    private int downloadId4;


    private String savePath1;
    private String savePath2;
    private String savePath3;
    private String savePath4;

    private Button startBtn1;
    private Button pauseBtn1;
    private Button deleteBtn1;
    private ProgressBar progressBar1;
    private Button startBtn2;
    private Button pauseBtn2;
    private Button deleteBtn2;
    private ProgressBar progressBar2;
    private Button startBtn3;
    private Button pauseBtn3;
    private Button deleteBtn3;
    private TextView detailTv3;
    private ProgressBar progressBar3;
    private Button startBtn4;
    private Button pauseBtn4;
    private Button deleteBtn4;
    private TextView detailTv4;
    private ProgressBar progressBar4;

    private void assignViews() {
        startBtn1 = (Button) findViewById(R.id.start_btn_1);
        pauseBtn1 = (Button) findViewById(R.id.pause_btn_1);
        deleteBtn1 = (Button) findViewById(R.id.delete_btn_1);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar_1);
        startBtn2 = (Button) findViewById(R.id.start_btn_2);
        pauseBtn2 = (Button) findViewById(R.id.pause_btn_2);
        deleteBtn2 = (Button) findViewById(R.id.delete_btn_2);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar_2);
        startBtn3 = (Button) findViewById(R.id.start_btn_3);
        pauseBtn3 = (Button) findViewById(R.id.pause_btn_3);
        deleteBtn3 = (Button) findViewById(R.id.delete_btn_3);
        detailTv3 = (TextView) findViewById(R.id.detail_tv_3);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar_3);
        startBtn4 = (Button) findViewById(R.id.start_btn_4);
        pauseBtn4 = (Button) findViewById(R.id.pause_btn_4);
        deleteBtn4 = (Button) findViewById(R.id.delete_btn_4);
        detailTv4 = (TextView) findViewById(R.id.detail_tv_4);
        progressBar4 = (ProgressBar) findViewById(R.id.progressBar_4);
    }


}

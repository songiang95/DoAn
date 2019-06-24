package com.example.songiang.readebookandmanga.ebook.reading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.model.Ebook;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.orhanobut.hawk.Hawk;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReadEbookOfflineActivity extends AppCompatActivity {

    @BindView(R.id.pdf_view)
    PDFView mPdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_read_ebook_offline);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        final Ebook ebook = (Ebook)intent.getSerializableExtra(Constant.EXTRA_EBOOK);
        File pdfFile = new File(Constant.DOWNLOAD_EBOOK_DIR_PATH + File.separator + ebook.getTitle() + ".pdf");
        mPdfView.fromFile(pdfFile)
                .enableSwipe(true)
                .defaultPage(Hawk.get(Constant.DOWNLOAD + ebook.getTitle(), 0))
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        Hawk.put(Constant.DOWNLOAD + ebook.getTitle(), page);
                    }
                })
                .pageSnap(true)
                .pageFling(true)
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Intent intent = new Intent(ReadEbookOfflineActivity.this,ReadEbookActivity.class);
                        intent.putExtra(Constant.EXTRA_READ_ONLINE,ebook.getReadOnlineLink());
                        startActivity(intent);
                    }
                })
                .load();
    }
}

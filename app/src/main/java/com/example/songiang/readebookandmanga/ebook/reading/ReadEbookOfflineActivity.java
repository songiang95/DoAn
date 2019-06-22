package com.example.songiang.readebookandmanga.ebook.reading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.songiang.readebookandmanga.R;
import com.example.songiang.readebookandmanga.utils.Constant;
import com.github.barteksc.pdfviewer.PDFView;
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
        final String fileName = intent.getStringExtra(Constant.EXTRA_PDF);
        File pdfFile = new File(Constant.DOWNLOAD_EBOOK_DIR_PATH + File.separator + fileName + ".pdf");
        mPdfView.fromFile(pdfFile)
                .enableSwipe(true)
                .defaultPage(Hawk.get(Constant.DOWNLOAD + fileName, 0))
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        Hawk.put(Constant.DOWNLOAD + fileName, page);
                    }
                })
                .pageFling(true)
                .load();
    }
}

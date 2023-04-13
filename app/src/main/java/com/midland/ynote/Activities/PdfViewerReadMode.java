package com.midland.ynote.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cloudmersive.client.ConvertDocumentApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midland.ynote.R;
import com.midland.ynote.Utilities.AdMob;
import com.midland.ynote.Utilities.FilingSystem;
import com.midland.ynote.Utilities.NotesPopUp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PdfViewerReadMode extends AppCompatActivity {

    String uri, fileSize;
    private String currentPdf;
    private ImageView publishDocIm;
    private TextView noPreview;
    private PDFView readMode;
    private WebView webViewReadMode;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("yNote");
        setSupportActionBar(toolbar);
        webViewReadMode = findViewById(R.id.webViewReadMode);
        readMode = findViewById(R.id.pdfViewReadMode);
        publishDocIm = findViewById(R.id.publishDocIm);
        noPreview = findViewById(R.id.noPreview);

        if (getIntent().getStringExtra("selectedDoc") != null) {
            uri = getIntent().getStringExtra("selectedDoc");
        } else {
            uri = intent.getAction();
        }

        if (getIntent().getStringExtra("selectedDocSize") != null) {
            fileSize = getIntent().getStringExtra("selectedDocSize");
        }

        currentPdf = getIntent().getStringExtra("selectedDocName");
        if (getRecentDocs("recentDocs") != null){
            if(!getRecentDocs("recentDocs").contains(currentPdf + "_-_" + uri)){
                CountDownTimer count = new CountDownTimer(120000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Snackbar.make(findViewById(R.id.pdfViewConst), "Keep on reading, great progress!", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Dismiss", v -> {

                                }).show();
                    }
                };
                count.start();
            }
        }else{
            CountDownTimer count = new CountDownTimer(120000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    Snackbar.make(findViewById(R.id.pdfViewConst), "Keep on reading, great progress!", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Dismiss", v -> {

                            }).show();
                }
            };
            count.start();
        }

        byte[] results;
        if (currentPdf.endsWith(".pdf") || currentPdf.endsWith(".PDF")){

            readMode.fromUri(Uri.parse(uri))
                    .password(null)// IF PASSWORD PROTECTED
//                    .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .onPageScroll((page, positionOffset) -> {

                    })
                    .onRender((nbPages, pageWidth, pageHeight) -> {

                    })
                    .enableAnnotationRendering(false)
                    .enableAntialiasing(true)
                    .spacing(10)
                    .onDraw((canvas, pageWidth, pageHeight, displayedPage) -> {

                    })
                    .onDrawAll((canvas, pageWidth, pageHeight, displayedPage) -> {

                    })
                    .onPageError((page, t) -> {

                    })
                    .onPageChange((page, pageCount) -> {
                    })
                    .scrollHandle(new DefaultScrollHandle(this))
                    .onTap(e -> true)
                    .onLoad(nbPages -> {

                    })
                    .invalidPageColor(Color.WHITE)
                    .load();
        } else {
            if (currentPdf.endsWith(".ppt") || currentPdf.endsWith(".PPT")) {
                ApiClient defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
                apiKeyAuth.setApiKey("YOUR API KEY");
//            apiKeyAuth.setApiKeyPrefix("Token");
                ConvertDocumentApi apiInstance = new ConvertDocumentApi();
                File inputFile = new File(FilingSystem.Companion.getRealPathFromURI(Uri.parse(uri), getApplicationContext()));
                try {
                    results = apiInstance.convertDocumentPptToPdf(inputFile);
                    readMode.fromBytes(results)
                            .password(null)// IF PASSWORD PROTECTED
//                        .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .onPageScroll((page, positionOffset) -> {

                            })
                            .onRender((nbPages, pageWidth, pageHeight) -> {

                            })
                            .enableAnnotationRendering(false)
                            .enableAntialiasing(true)
                            .spacing(10)
                            .onDraw((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onDrawAll((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onPageError((page, t) -> {

                            })
                            .onPageChange((page, pageCount) -> {
                            })
                            .scrollHandle(new DefaultScrollHandle(this))
                            .onTap(e -> true)
                            .onLoad(nbPages -> {

                            })
                            .invalidPageColor(Color.WHITE)
                            .load();
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            else if (currentPdf.endsWith(".doc") || currentPdf.endsWith(".DOC")) {
                ApiClient defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
                apiKeyAuth.setApiKey("YOUR API KEY");
//            apiKeyAuth.setApiKeyPrefix("Token");
                ConvertDocumentApi apiInstance = new ConvertDocumentApi();
                File inputFile = new File(FilingSystem.Companion.getRealPathFromURI(Uri.parse(uri), getApplicationContext()));
                try {
                    results = apiInstance.convertDocumentDocToPdf(inputFile);
                    readMode.fromBytes(results)
                            .password(null)// IF PASSWORD PROTECTED
//                        .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .onPageScroll((page, positionOffset) -> {

                            })
                            .onRender((nbPages, pageWidth, pageHeight) -> {

                            })
                            .enableAnnotationRendering(false)
                            .enableAntialiasing(true)
                            .spacing(10)
                            .onDraw((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onDrawAll((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onPageError((page, t) -> {

                            })
                            .onPageChange((page, pageCount) -> {
                            })
                            .scrollHandle(new DefaultScrollHandle(this))
                            .onTap(e -> true)
                            .onLoad(nbPages -> {

                            })
                            .invalidPageColor(Color.WHITE)
                            .load();
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            else if (currentPdf.endsWith(".pptx") || currentPdf.endsWith(".PPTX")) {
                ApiClient defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
                apiKeyAuth.setApiKey("YOUR API KEY");
//            apiKeyAuth.setApiKeyPrefix("Token");
                ConvertDocumentApi apiInstance = new ConvertDocumentApi();
                File inputFile = new File(FilingSystem.Companion.getRealPathFromURI(Uri.parse(uri), getApplicationContext()));
                try {
                    results = apiInstance.convertDocumentPptxToPdf(inputFile);
                    readMode.fromBytes(results)
                            .password(null)// IF PASSWORD PROTECTED
//                        .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .onPageScroll((page, positionOffset) -> {

                            })
                            .onRender((nbPages, pageWidth, pageHeight) -> {

                            })
                            .enableAnnotationRendering(false)
                            .enableAntialiasing(true)
                            .spacing(10)
                            .onDraw((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onDrawAll((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onPageError((page, t) -> {

                            })
                            .onPageChange((page, pageCount) -> {
                            })
                            .scrollHandle(new DefaultScrollHandle(this))
                            .onTap(e -> true)
                            .onLoad(nbPages -> {

                            })
                            .invalidPageColor(Color.WHITE)
                            .load();
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            else if (currentPdf.endsWith(".docx") || currentPdf.endsWith(".DOCX")) {
                ApiClient defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
                apiKeyAuth.setApiKey("YOUR API KEY");
//            apiKeyAuth.setApiKeyPrefix("Token");
                ConvertDocumentApi apiInstance = new ConvertDocumentApi();
                File inputFile = new File(FilingSystem.Companion.getRealPathFromURI(Uri.parse(uri), getApplicationContext()));
                try {
                    results = apiInstance.convertDocumentDocxToPdf(inputFile);
                    readMode.fromBytes(results)
                            .password(null)// IF PASSWORD PROTECTED
//                        .defaultPage(savedPage) // THIS VALUE CAN BE STORED TO BE OPEN FROM LAST TIME
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .onPageScroll((page, positionOffset) -> {

                            })
                            .onRender((nbPages, pageWidth, pageHeight) -> {

                            })
                            .enableAnnotationRendering(false)
                            .enableAntialiasing(true)
                            .spacing(10)
                            .onDraw((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onDrawAll((canvas, pageWidth, pageHeight, displayedPage) -> {

                            })
                            .onPageError((page, t) -> {

                            })
                            .onPageChange((page, pageCount) -> {
                            })
                            .scrollHandle(new DefaultScrollHandle(this))
                            .onTap(e -> true)
                            .onLoad(nbPages -> {

                            })
                            .invalidPageColor(Color.WHITE)
                            .load();
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

            noPreview.setVisibility(View.VISIBLE);
            Snackbar.make(findViewById(R.id.pdfViewConst), "Can't open MS Office document from an offline service. Publish it to view.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("PUBLISH", v -> {
                        Intent intent1 = new Intent(getApplicationContext(), DocumentMetaData.class);
                        intent1.putExtra("flag", "mainHome");
                        intent1.putExtra("selectedDocUri", uri);
                        intent1.putExtra("selectedDocName", currentPdf);
                        intent1.putExtra("selectedDocSize", fileSize);
                        startActivity(intent1);
                    }).show();
        }
        if (isExternalStorageReadWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            File savedLecture = new File(FilingSystem.Companion.getDubDocuments(), currentPdf + ".sn");
            if (!savedLecture.exists()) {
                try {
                    FileOutputStream fos = new FileOutputStream(savedLecture);
                    fos.close();
                    Toast.makeText(PdfViewerReadMode.this, "New study note created!", Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //GET PATH FROM MIME TYPE INTENT FILTERS
        AdView mAdView = findViewById(R.id.adView);
        AdMob.Companion.bannerAd(this, mAdView);

        publishDocIm.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), DocumentMetaData.class);
            intent1.putExtra("flag", "mainHome");
            intent1.putExtra("selectedDocUri", uri);
            intent1.putExtra("selectedDocName", currentPdf);
            intent1.putExtra("selectedDocSize", fileSize);
            startActivity(intent1);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void addToRecent(String s) {
        ArrayList<String> recentDocs;
        recentDocs = getRecentDocs("recentDocs");
        recentDocs.add(s);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("recentDocs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String j = gson.toJson(recentDocs);
        editor.putString("recentDocs", j);
        editor.apply();
        Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getRecentDocs(String flag) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(flag, Context.MODE_PRIVATE);
        String json = preferences.getString(flag, "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        return gson.fromJson(json, type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.read_mode_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.personalNotes:
                studyNotesFile();

                break;
            case R.id.mode:
                if (ContextCompat.checkSelfPermission(PdfViewerReadMode.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(PdfViewerReadMode.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(PdfViewerReadMode.this, Manifest.permission.CAMERA)
                            && ActivityCompat.shouldShowRequestPermissionRationale(PdfViewerReadMode.this, Manifest.permission.RECORD_AUDIO)) {
                        Snackbar.make(findViewById(R.id.pdfViewConst), "Permission", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ENABLE", v -> ActivityCompat.requestPermissions(PdfViewerReadMode.this, new String[]{
                                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                                }, 7)).show();
                    } else {
                        ActivityCompat.requestPermissions(PdfViewerReadMode.this, new String[]{
                                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                        }, 7);
                    }
                } else {
                    lectureNameDialog();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7) {
            lectureNameDialog();
        }
    }

//    private void fileNameDialog() {
//        LinearLayout linearLayout = new LinearLayout(PdfViewerReadMode.this);
//        linearLayout.setOrientation(LinearLayout.VERTICAL);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(50, 0, 50, 100);
//        final EditText input = new EditText(PdfViewerReadMode.this);
//        input.setLayoutParams(lp);
//        input.setGravity(Gravity.TOP | Gravity.START);
//        linearLayout.addView(input, lp);
//
//        final AlertDialog.Builder fileNameDialog = new AlertDialog.Builder(PdfViewerReadMode.this);
//        fileNameDialog.setTitle("Notes title");
//        fileNameDialog.setMessage("e.g '13 reasons why...'");
//        fileNameDialog.setView(linearLayout);
//        fileNameDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        fileNameDialog.setPositiveButton("submit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (input.getText().toString().trim().equals("")) {
//                    fileNameDialog();
//                } else {
//                    currentPdf = input.getText().toString().trim();
//                    if (isExternalStorageReadWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        File savedLecture = new File(FilingSystem.Companion.getDubDocuments(), currentPdf + ".sn");
//                        try {
//                            FileOutputStream fos = new FileOutputStream(savedLecture);
//                            fos.write((uri + "_-_").getBytes());
//                            fos.close();
//                            Toast.makeText(getApplicationContext(), "New study note created!", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
//
//
//        fileNameDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//            }
//        });
//        fileNameDialog.show();
//    }

    public void studyNotesFile() {
        if (isExternalStorageReadWritable() && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
            File savedLecture;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                File audioBooks = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                savedLecture = new File(audioBooks, currentPdf + ".sn");
            }else {
                savedLecture = new File(FilingSystem.Companion.getDubDocuments(), currentPdf + ".sn");
            }

            if (savedLecture.exists()) {
                NotesPopUp notesPopUp = new NotesPopUp(PdfViewerReadMode.this,
                        this,
                        PdfViewerReadMode.this,
                        uri,
                        currentPdf,
                        "PdfReadMode");
                notesPopUp.show();
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(savedLecture);
                    fos.write((uri + "_-_").getBytes());
                    fos.close();
                    NotesPopUp notesPopUp = new NotesPopUp(PdfViewerReadMode.this, getParent(), getApplicationContext(), uri, currentPdf, "PdfReadMode");
                    notesPopUp.show();
                    Toast.makeText(getApplicationContext(), "New study note created!", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void lectureNameDialog() {

        LinearLayout linearLayout = new LinearLayout(PdfViewerReadMode.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 0, 50, 100);
        final EditText input = new EditText(PdfViewerReadMode.this);
        input.setLayoutParams(lp);
        input.setGravity(Gravity.TOP | Gravity.START);
        linearLayout.addView(input, lp);

        final AlertDialog.Builder fileNameDialog = new AlertDialog.Builder(PdfViewerReadMode.this);
        fileNameDialog.setTitle("Lecture title");
        fileNameDialog.setMessage("e.g 'Critical & creative reasoning...'");
        fileNameDialog.setView(linearLayout);
        fileNameDialog.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

        fileNameDialog.setPositiveButton("submit", (dialog, which) -> {
            String lectureName;
            if (input.getText().toString().trim().equals("")) {
                lectureNameDialog();
            } else {
                lectureName = input.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "New lecture created!", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getApplicationContext(), LectureStudio2.class);
                intent1.putExtra("selectedDoc", uri);
                intent1.putExtra("fileName", lectureName);
                intent1.putExtra("studio", "3");
                startActivity(intent1);
            }
        });


        fileNameDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        fileNameDialog.show();
    }

    public boolean isExternalStorageReadWritable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.i("State", "writable");
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermission(String permission) {
        int check = ActivityCompat.checkSelfPermission(getApplicationContext(), permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }


//    public static void openFile(Context context, File url) throws IOException {
//        // Create URI
//        File file=url;
//        Uri uri = Uri.fromFile(file);
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        // Check what kind of file you are trying to open, by comparing the url with extensions.
//        // When the if condition is matched, plugin sets the correct intent (mime) type,
//        // so Android knew what application to use to open the file
//        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
//            // Word document
//            intent.setDataAndType(uri, "application/msword");
//        } else if(url.toString().contains(".pdf")) {
//            // PDF file
//            intent.setDataAndType(uri, "application/pdf");
//        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
//            // Powerpoint file
//            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
//        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
//            // Excel file
//            intent.setDataAndType(uri, "application/vnd.ms-excel");
//        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
//            // WAV audio file
//            intent.setDataAndType(uri, "application/x-wav");
//        } else if(url.toString().contains(".rtf")) {
//            // RTF file
//            intent.setDataAndType(uri, "application/rtf");
//        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
//            // WAV audio file
//            intent.setDataAndType(uri, "audio/x-wav");
//        } else if(url.toString().contains(".gif")) {
//            // GIF file
//            intent.setDataAndType(uri, "image/gif");
//        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
//            // JPG file
//            intent.setDataAndType(uri, "image/jpeg");
//        } else if(url.toString().contains(".txt")) {
//            // Text file
//            intent.setDataAndType(uri, "text/plain");
//        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
//            // Video files
//            intent.setDataAndType(uri, "video/*");
//        } else {
//            //if you want you can also define the intent type for any other file
//
//            //additionally use else clause below, to manage other unknown extensions
//            //in this case, Android will show all applications installed on the device
//            //so you can choose which application to use
//            intent.setDataAndType(uri, "*/*");
//        }
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

}
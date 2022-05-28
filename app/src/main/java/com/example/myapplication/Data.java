package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Data extends AppCompatActivity {

    public static final int READ_REQUEST_CODE = 1;
    public static final int CAMERA_REQUEST_CODE = 2;
    private int buttonCalled = 0;
    private byte[] Sendmsg = new byte[0];
    private ByteArrayOutputStream Imagemsg = new ByteArrayOutputStream();
    public String current_folder_name = "";
    public int photocount = 0;
    private String pageContent;

    Button ButtonPageClass1;
    Button ButtonPageClass2;
    Button ButtonPageClass3;
    Button ButtonPageClass4;
    Button ButtonPageClass5;
    Button ButtonPageClass6;
    Button ButtonPageClass7;
    Button to_tcpclientwaiting_page;
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Button ButtonChoosePage = (Button) findViewById(R.id.ButtonChoosePage);
        ButtonPageClass1 = (Button) findViewById(R.id.ButtonPageClass1);
        ButtonPageClass2 = (Button) findViewById(R.id.ButtonPageClass2);
        ButtonPageClass3 = (Button) findViewById(R.id.ButtonPageClass3);
        ButtonPageClass1 = (Button) findViewById(R.id.ButtonPageClass1);
        ButtonPageClass4 = (Button) findViewById(R.id.ButtonPageClass4);
        ButtonPageClass5 = (Button) findViewById(R.id.ButtonPageClass5);
        ButtonPageClass6 = (Button) findViewById(R.id.ButtonPageClass6);
        ButtonPageClass7 = (Button) findViewById(R.id.ButtonPageClass7);
        to_tcpclientwaiting_page = (Button) findViewById(R.id.ButtonSend);
        FloatingActionButton camera = (FloatingActionButton) findViewById(R.id.camera);
        TextView TextPage = (TextView) findViewById(R.id.TextPage);
        EditText EditTextBookPage = (EditText) findViewById(R.id.EditTextBookPage);
        ButtonChoosePage.setEnabled(false);
        ButtonPageClass1.setEnabled(false);
        ButtonPageClass2.setEnabled(false);
        ButtonPageClass3.setEnabled(false);
        ButtonPageClass4.setEnabled(false);
        ButtonPageClass5.setEnabled(false);
        ButtonPageClass6.setEnabled(false);
        ButtonPageClass7.setEnabled(false);
        to_tcpclientwaiting_page.setEnabled(false);

        boolean externalHasGone = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            externalHasGone = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions;
            if (!externalHasGone) {//如果只有存取權限未取得
                permissions = new String[1];
                permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
                requestPermissions(permissions, 100);
            }
        }

        AdapterView.OnItemSelectedListener spnOnItemSelected = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        mEditText = (EditText) findViewById(R.id.EditTextBookMonth);
        mEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                    ButtonChoosePage.setEnabled(true);
            };
        };

        Runnable monitorEdittext = new Runnable(){
            @Override
            public void run(){
                while (true) {
                    if (!EditTextBookPage.getText().toString().equals("")){
                        handler.sendEmptyMessage(0);
                        break;
                    }
                    try {
                        Thread.sleep(250);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        new Thread(monitorEdittext).start();

        ButtonChoosePage.setOnClickListener(view -> {
            int totalPage = Integer.valueOf(EditTextBookPage.getText().toString());
            int[] page = new int[6];
            boolean randomFlag = false;
            if(totalPage >= 6){
                if(randomFlag){
                    for(int i = 0; i < 6; i++){
                        page[i] = new Random().nextInt(totalPage) + 1;
                        for (int j = 0; j < i; j++){
                            if(page[i] == page[j]){
                                page[i] = new Random().nextInt(totalPage) + 1;
                                j = -1;
                            }
                        }
                    }
                }
                else {
                    page[0] = 1;
                    page[1] = 2;
                    page[2] = totalPage / 3;
                    page[3] = totalPage / 2;
                    page[4] = totalPage / 3 * 2;
                    page[5] = totalPage;
                }
                Arrays.sort(page);
                pageContent = Arrays.toString(page).replaceAll("\\[|\\]", "");
                System.out.println(pageContent);
                TextPage.setText("選擇以下 " + pageContent + " 內頁頁數");
                ButtonPageClass1.setEnabled(true);
                ButtonChoosePage.setEnabled(false);
                pageContent = pageContent.replaceAll(" ", "");
            }
            else{
                Toast.makeText(this, "請選擇 6 頁內頁以上的書籍!!", Toast.LENGTH_SHORT).show();
                EditTextBookPage.setText("");
                ButtonChoosePage.setEnabled(true);
                new Thread(monitorEdittext).start();
            }
        });

        ButtonPageClass1.setOnClickListener(v -> { toMediaCabinet(1); });
        ButtonPageClass2.setOnClickListener(v -> { toMediaCabinet(2); });
        ButtonPageClass3.setOnClickListener(v -> { toMediaCabinet(3); });
        ButtonPageClass4.setOnClickListener(v -> { toMediaCabinet(4); });
        ButtonPageClass5.setOnClickListener(v -> { toMediaCabinet(5); });
        ButtonPageClass6.setOnClickListener(v -> { toMediaCabinet(6); });
        ButtonPageClass7.setOnClickListener(v -> { toMediaCabinet(7); });

        to_tcpclientwaiting_page.setOnClickListener(v -> {
            Intent intent = new Intent(Data.this, TCPClient_Waiting.class);

            Book book = getBookInformation();

            File dir = this.getFilesDir();
            File[] files = dir.listFiles();
            int filesCount = files.length;

            //創建一個資料夾，用來存放一本書的圖片和資訊
            String folder_name = dir.getPath() + "/" + String.valueOf(filesCount);
            current_folder_name = folder_name;
            File folder = new File(folder_name);
            if(!folder.exists())
                folder.mkdir();

            //將tmp中的圖片移至上述資料夾中
            String folder_tmp_name = dir.getPath() + "/tmp";
            File folder_tmp = new File(folder_tmp_name);
            File[] photo_files = folder_tmp.listFiles();
            for(File file : photo_files){
                String oldPath = file.getPath();
                String[] oldPathCount = oldPath.split("/");
                oldPathCount[oldPathCount.length - 1] = oldPathCount[oldPathCount.length - 1].substring(5, oldPathCount[oldPathCount.length - 1].indexOf("."));
                String new_path = folder_name + "/photo" + oldPathCount[oldPathCount.length - 1] + ".png";
                try {
                    fileCopy(oldPath, new_path);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            packetSendMsg();//將封包封進SendMsg中
            String fileName = "packet";
            write_byte(folder, fileName, Sendmsg);
            Bundle bundle = new Bundle();
            bundle.putInt("folderName", filesCount);
            intent.putExtras(bundle);

            write_book_information(book);

            startActivity(intent);
        });

        camera.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        StringBuffer word = new StringBuffer();
        switch (permissions.length) {
            case 1:
                if (permissions[0].equals(Manifest.permission.CAMERA)) word.append("相機權限");
                else word.append("儲存權限");
                if (grantResults[0] == 0) word.append("已取得");
                else word.append("未取得");
                word.append("\n");
                if (permissions[0].equals(Manifest.permission.CAMERA)) word.append("儲存權限");
                else word.append("相機權限");
                word.append("已取得");
                System.out.println(word);
                break;
            case 2:
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equals(Manifest.permission.CAMERA)) word.append("相機權限");
                    else word.append("儲存權限");
                    if (grantResults[i] == 0) word.append("已取得");
                    else word.append("未取得");
                    if (i < permissions.length - 1) word.append("\n");
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        switch (buttonCalled){
            case 1:
                if ( resultData.getData() != null){
                    Toast.makeText(this,"請選擇 6 張內頁!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(1);
                }
                else if( resultData.getClipData().getItemCount() != 6){
                    Toast.makeText(this,"請選擇 6 張內頁!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(1);
                }else {
                    processPhoto(requestCode, resultCode, resultData);
                }   break;
            case 2:
                if( resultData.getData() == null){
                    Toast.makeText(this,"請選擇 1 張書頁側面!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(2);
                }else {
                    processPhoto(requestCode, resultCode, resultData);
                }   break;
            case 3:
                if ( resultData.getData() != null){
                    Toast.makeText(this,"請選擇 2 張書背接縫處!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(3);
                }
                else if( resultData.getClipData().getItemCount() != 2){
                    Toast.makeText(this,"請選擇 2 張書背接縫處!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(3);
                }else {
                    processPhoto(requestCode, resultCode, resultData);
                }   break;
            case 4:
                if( resultData.getData() == null){
                    Toast.makeText(this,"請選擇 1 張封面!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(4);
                }else {
                    processPhoto(requestCode, resultCode, resultData);
                }   break;
            case 5:
                if( resultData.getData() == null){
                    Toast.makeText(this,"請選擇 1 張書衣!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(5);
                }else {
                    processPhoto(requestCode, resultCode, resultData);
                }   break;
            case 6:
                if( resultData.getData() == null){
                    Toast.makeText(this,"請選擇 1 張封底!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(6);
                }else {
                    processPhoto(requestCode, resultCode, resultData);
                }   break;
            case 7:
                if ( resultData.getData() != null){
                    Toast.makeText(this,"請選擇 2 張折口!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(7);
                }
                else if( resultData.getClipData().getItemCount() != 2){
                    Toast.makeText(this,"請選擇 2 張折口!",Toast.LENGTH_LONG).show();
                    toMediaCabinet(7);
                }else {
                    processPhoto(requestCode, resultCode, resultData);
                }   break;
        }
    }

    private void processPhoto(int requestCode, int resultCode, Intent resultData){
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (resultData.getData()!=null) {
                Uri selectedImage = resultData.getData();
                String filePath =  getImageAbsolutePath(this, selectedImage);
                Bitmap bm = getResizedBitmap(filePath);
                String photo_path = saveBitmap(bm, photocount);
                photocount++;
                File file = new File(photo_path);//取出512*512圖片的位置
                try{
                    FileInputStream input = new FileInputStream(file.getAbsolutePath());
                    byte[] oneimagedata = new byte[(int)file.length()];//宣告imagedata來存放圖片的內容
                    byte[] oneImageLength = ByteBuffer.allocate(4).putInt((int)file.length()).array();//宣告長度為4的byte array紀錄一張圖片的長度
                    input.read(oneimagedata);//將圖片內容放進oneimagedata
                    Imagemsg.write(oneImageLength);//將一張圖片的長度放進Imagemsg
                    Imagemsg.write(oneimagedata);//將圖片內容放進Imagemsg
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            else{
                if (resultData.getClipData() != null){
                    for (int i = 0; i < resultData.getClipData().getItemCount(); i++){
                        Uri selectedImages = resultData.getClipData().getItemAt(i).getUri();
                        String filePath =  getImageAbsolutePath(this, selectedImages);
                        Bitmap bm = getResizedBitmap(filePath);
                        String photo_path = saveBitmap(bm, photocount);
                        photocount++;
                        File file = new File(photo_path);
                        try {
                            FileInputStream input = new FileInputStream(file.getAbsolutePath());//取得圖片流
                            byte[] oneimagedata = new byte[(int)file.length()];//宣告一個byte array用來存放一張圖片
                            byte[] oneImageLength = ByteBuffer.allocate(4).putInt((int)file.length()).array();//宣告長度為4的byte array紀錄一張圖片的長度
                            input.read(oneimagedata);//將圖片內容放進oneimagedata
                            Imagemsg.write(oneImageLength);//將一張圖片的長度放進Imagemsg
                            Imagemsg.write(oneimagedata);//將圖片內容放進Imagemsg
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            switch (buttonCalled){
                case 1:
                    ButtonPageClass1.setText("Already!");
                    ButtonPageClass1.setEnabled(false);
                    ButtonPageClass2.setEnabled(true);  break;
                case 2:
                    ButtonPageClass2.setText("Already!");
                    ButtonPageClass2.setEnabled(false);
                    ButtonPageClass3.setEnabled(true);  break;
                case 3:
                    ButtonPageClass3.setText("Already!");
                    ButtonPageClass3.setEnabled(false);
                    ButtonPageClass4.setEnabled(true);  break;
                case 4:
                    ButtonPageClass4.setText("Already!");
                    ButtonPageClass4.setEnabled(false);
                    ButtonPageClass5.setEnabled(true);  break;
                case 5:
                    ButtonPageClass5.setText("Already!");
                    ButtonPageClass5.setEnabled(false);
                    ButtonPageClass6.setEnabled(true);  break;
                case 6:
                    ButtonPageClass6.setText("Already!");
                    ButtonPageClass6.setEnabled(false);
                    ButtonPageClass7.setEnabled(true);  break;
                case 7:
                    ButtonPageClass7.setText("Already!");
                    ButtonPageClass7.setEnabled(false);
                    to_tcpclientwaiting_page.setEnabled(true);  break;
            }
        }
    }

    private void toMediaCabinet(int whoCall){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        switch (whoCall){
            case 1:
                buttonCalled = 1;   break;
            case 2:
                buttonCalled = 2;   break;
            case 3:
                buttonCalled = 3;   break;
            case 4:
                buttonCalled = 4;   break;
            case 5:
                buttonCalled = 5;   break;
            case 6:
                buttonCalled = 6;   break;
            case 7:
                buttonCalled = 7;   break;
        }
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
    else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private void packetSendMsg(){
        int ImageMsgSize = Imagemsg.size();
        byte[] ImageMsgtoByte = Imagemsg.toByteArray();
        byte[] allImageLength = ByteBuffer.allocate(4).putInt(ImageMsgSize).array();//宣告長度為4的byte array紀錄整個封包的長度
        Sendmsg = addBytes(allImageLength, ImageMsgtoByte);
    }

    private Bitmap getResizedBitmap(String imagePath) {
        // 取得原始圖檔的bitmap與寬高

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        int rotated = getOrientation(imagePath);
        bitmap = rotateImage(bitmap, rotated);
        return Bitmap.createScaledBitmap(bitmap, 512, 512, true);
    }

    public String saveBitmap(Bitmap bitmap, int count) {
        FileOutputStream fOut;
        String photo_name = "";
        try {
            String folder_name = this.getFilesDir() + "/tmp";
            File folder = new File(folder_name);
            if(!folder.exists())
                folder.mkdir();
            photo_name = folder_name + "/photo" + String.valueOf(count) + ".png";
            fOut = new FileOutputStream(new File(photo_name));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return photo_name;
    }

    public void fileCopy(String oldFilePath,String newFilePath) throws IOException {
        //獲得原檔案流
        FileInputStream inputStream = new FileInputStream(new File(oldFilePath));
        byte[] data = new byte[1024];
        //輸出流
        FileOutputStream outputStream =new FileOutputStream(new File(newFilePath));
        //開始處理流
        while (inputStream.read(data) != -1) {
            outputStream.write(data);
        }
        inputStream.close();
        outputStream.close();
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    public  void  write_byte(File file, String name, byte[] input){
        File outFile = new File(file, name);
        writeToFile(outFile, input);
    }

    private void writeToFile(File fout, byte[] data) {
        FileOutputStream osw = null;
        try {
            osw = new FileOutputStream(fout);
            osw.write(data);
            osw.flush();
        } catch (Exception e) {
            ;
        } finally {
            try {
                osw.close();
            } catch (Exception e) {
                ;
            }
        }
    }

    public void write_string(File file, String name, String input){
        File outFile = new File(file, name);
        System.out.println(input);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(outFile, true);
            outputStream.write(input.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void write_book_information(Book book){
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
        String t = format.format(new Date());
        System.out.println("currrnt date write in txt = " + t);
        t += "_" + book.getBookname();
        String tmp = book.getBookmonth();
        String[] tmpSplit = tmp.split("-");
        t += "_" + tmpSplit[0] + "-" + String.valueOf(Integer.valueOf(tmpSplit[1]) + 1) + "-" +  tmpSplit[2];
        System.out.println("book date write in txt = " + book.getBookmonth());
        t += "_" + book.getBookprize();
        t += "_" + book.getBookkind();
        t += "_" + pageContent;

        File dir = new File(current_folder_name);
        String index = "book_information.txt";
        write_string(dir, index, t);
    }

    public static class Book{
        public static String bookname;
        public static String bookmonth;
        public static String bookprize;
        public static String bookkind;

        Book(String bookname, String bookmonth, String bookprize, String bookkind){
            this.bookname = bookname;
            this.bookmonth = bookmonth;
            this.bookprize = bookprize;
            this.bookkind = bookkind;
        }

        public String getBookname(){
            return bookname;
        }

        public String getBookmonth(){
            return bookmonth;
        }

        public String getBookprize(){
            return bookprize;
        }

        public String getBookkind(){
            return bookkind;
        }
    }

    private Book getBookInformation(){
        EditText BookName = (EditText) findViewById(R.id.EditTextBookName); //取得BookName的參考
        String name = "\"" + BookName.getText().toString() + "\"";

        String month = mEditText.getText().toString(); //取得BookMonth的參考
        System.out.println("month = " + month);

        EditText BookPrize = (EditText) findViewById(R.id.EditTextBookPrize); //取得BookPrize的參考
        String prize = BookPrize.getText().toString(); //取得原始價格(string)

        Spinner spinner = (Spinner) findViewById(R.id.SpinnerBookClass);  //取得spinner的參考
        String kind = String.valueOf(spinner.getSelectedItem()); //取得使用者在spinner選擇的項目

        return new Book(name, month, prize, kind);
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Data.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Data.this.mEditText.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private int getOrientation(String imageSrc){
        int rotated = 0;
        try {
            ExifInterface ei = new ExifInterface(imageSrc);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotated = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotated = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotated = 270;
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotated = 0;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return rotated;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Data extends AppCompatActivity {

    public static final int READ_REQUEST_CODE = 1;
    private byte[] Sendmsg = new byte[0];
    //private ByteArrayOutputStream Imagemsg = ByteArrayOutputStream();
    public String current_folder_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Button ButtonPageClass1 = (Button) findViewById(R.id.ButtonPageClass1);
        Button ButtonPageClass2 = (Button) findViewById(R.id.ButtonPageClass2);
        Button ButtonPageClass3 = (Button) findViewById(R.id.ButtonPageClass3);
        Button ButtonPageClass4 = (Button) findViewById(R.id.ButtonPageClass4);
        Button ButtonPageClass5 = (Button) findViewById(R.id.ButtonPageClass5);
        Button ButtonPageClass6 = (Button) findViewById(R.id.ButtonPageClass6);
        Button ButtonPageClass7 = (Button) findViewById(R.id.ButtonPageClass7);
        Button to_tcpclientwaiting_page = (Button) findViewById(R.id.ButtonSend);
        ButtonPageClass2.setEnabled(false);
        ButtonPageClass3.setEnabled(false);
        ButtonPageClass4.setEnabled(false);
        ButtonPageClass5.setEnabled(false);
        ButtonPageClass6.setEnabled(false);
        ButtonPageClass7.setEnabled(false);
        //to_tcpclientwaiting_page.setEnabled(false);

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

        ButtonPageClass1.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            ButtonPageClass2.setEnabled(true);
            System.out.println("class1");

            startActivityForResult(intent, READ_REQUEST_CODE);
        });

        ButtonPageClass2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            ButtonPageClass3.setEnabled(true);
            System.out.println("class2");

            startActivityForResult(intent, READ_REQUEST_CODE);
        });

        ButtonPageClass3.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            ButtonPageClass4.setEnabled(true);
            System.out.println("class3");

            startActivityForResult(intent, READ_REQUEST_CODE);
        });

        ButtonPageClass4.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            ButtonPageClass5.setEnabled(true);
            System.out.println("class4");

            startActivityForResult(intent, READ_REQUEST_CODE);
        });

        ButtonPageClass5.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            ButtonPageClass6.setEnabled(true);
            System.out.println("class5");

            startActivityForResult(intent, READ_REQUEST_CODE);
        });

        ButtonPageClass6.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            ButtonPageClass7.setEnabled(true);
            System.out.println("class6");

            startActivityForResult(intent, READ_REQUEST_CODE);
        });

        ButtonPageClass7.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            to_tcpclientwaiting_page.setEnabled(true);
            System.out.println("class7");

            startActivityForResult(intent, READ_REQUEST_CODE);
        });

        EditText test = (EditText)findViewById(R.id.EditTextBookName);
        to_tcpclientwaiting_page.setOnClickListener(v -> {
            Intent intent = new Intent(Data.this, TCPClient_Waiting.class);

            Book book = getBookInformation();

            File dir = this.getFilesDir();
            File[] files = dir.listFiles();
            int filesCount = files.length;

            String folder_name = dir.getPath() + "/" + String.valueOf(filesCount);
            current_folder_name = folder_name;
            File folder = new File(folder_name);
            if(!folder.exists())
                folder.mkdir();

            String folder_tmp_name = dir.getPath() + "/tmp";
            File folder_tmp = new File(folder_tmp_name);
            File[] photo_files = folder_tmp.listFiles();
            for (int i = 0; i < photo_files.length; i++){
                String new_path = folder_name + "/photo" + String.valueOf(i) + ".png";
                try {
                    fileCopy(photo_files[i].getPath(), new_path);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }


            String fileName = "test";
            write_byte(folder, fileName, Sendmsg);
            Bundle bundle = new Bundle();
            bundle.putInt("folderName", filesCount);
            //System.out.println("filename = " + fileName);
            intent.putExtras(bundle);

            write_book_information(book);

            startActivity(intent);
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
        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (resultData.getData()!=null) {
                Uri selectedImage = resultData.getData();
                String filePath =  getImageAbsolutePath(this, selectedImage);
                Bitmap bm = getResizedBitmap(filePath);
                String photo_path = saveBitmap(bm, 0);
                File file = new File(photo_path);
                int file_size = 4 + (int)file.length();
                byte[] imagedatas = new byte[4 + file_size];
                byte[] allImageLength = ByteBuffer.allocate(4).putInt(file_size).array();
                System.arraycopy(allImageLength, 0, imagedatas, 0, 4);
                int count = 4;
                try{
                    FileInputStream input = new FileInputStream(file.getAbsolutePath());
                    byte[] imagedata = new byte[(int)file.length()];
                    byte[] oneImageLength = ByteBuffer.allocate(4).putInt((int)file.length()).array();
                    System.arraycopy(oneImageLength, 0, imagedatas, count, 4);
                    count += 4;
                    input.read(imagedata);
                    System.out.println(String.valueOf(0) + "  imagedata : " + imagedata.length);
                    System.arraycopy(imagedata, 0, imagedatas, count, (int)file.length());
                }catch (IOException e){
                    e.printStackTrace();
                }
                Sendmsg = addBytes(Sendmsg, imagedatas);
                System.out.println(Sendmsg.length);
            }
            else{
                if (resultData.getClipData() != null){
                    int file_size = resultData.getClipData().getItemCount() * 4;
                    for (int j = 0; j < resultData.getClipData().getItemCount(); j++){
                        Uri selectedImages = resultData.getClipData().getItemAt(j).getUri();
                        String filePath =  getImageAbsolutePath(this, selectedImages);
                        Bitmap bm = getResizedBitmap(filePath);
                        String photo_path = saveBitmap(bm, j);

                        File file = new File(photo_path);
                        file_size += file.length();
                    }
                    byte[] imagedatas = new byte[file_size + 4];
                    byte[] allImageLength = ByteBuffer.allocate(4).putInt(file_size).array();
                    System.out.println("imagedatas : " + imagedatas.length);
                    System.arraycopy(allImageLength, 0, imagedatas, 0, 4);

                    int count = 4;
                    File folder_tmp = new File(this.getFilesDir().getPath() + "/tmp");
                    File[] photo_files = folder_tmp.listFiles();
                    for(int i = 0; i < photo_files.length; i++){
                        File file = new File(photo_files[i].getPath());
                        try{
                            FileInputStream input = new FileInputStream(file.getAbsolutePath());
                            byte[] imagedata = new byte[(int)file.length()];
                            byte[] oneImageLength = ByteBuffer.allocate(4).putInt((int)file.length()).array();
                            System.out.println(String.valueOf(i) + "  imagedata : " + imagedata.length);
                            System.arraycopy(oneImageLength, 0, imagedatas, count, 4);
                            count += 4;
                            input.read(imagedata);
                            System.arraycopy(imagedata, 0, imagedatas, count, (int)file.length());
                            count += (int)file.length();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    Sendmsg = addBytes(Sendmsg, imagedatas);
                    System.out.println(Sendmsg.length);
                }
            }
        }
        Button to_album = (Button) findViewById(R.id.ButtonPageClass1);
        to_album.setText("already selected");
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

    private Bitmap getResizedBitmap(String imagePath) {
        // 取得原始圖檔的bitmap與寬高

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        int width = options.outWidth, height = options.outHeight;

        // 將圖檔等比例縮小至寬度為512
        final int MAX_SIZE = 512;
        float resize_width = 1, resize_heigth = 1; // 縮小值 resize 可為任意小數
        if(width>MAX_SIZE){
            resize_width = ((float) MAX_SIZE) / width;
        }
        if(height>MAX_SIZE){
            resize_heigth = ((float) MAX_SIZE) / height;
        }

        // 產生縮圖需要的參數 matrix
        Matrix matrix = new Matrix();
        matrix.postScale(resize_width, resize_heigth); // 設定寬高的縮放比例

        // 產生縮小後的圖
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return resizedBitmap;
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

    public void packetSendMsg(){

    }

    public void write_string(File file, String name, String input){
        File outFile = new File(file, name);
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
        String tmp = "_" + book.getBookname();
        t = t + tmp;
        tmp = "_" + book.getBookmonth();
        t = t + tmp;
        tmp = "_" + book.getBookprize();
        t = t + tmp;
        tmp = "_" + book.getBookkind();
        t = t + tmp;
        t = t + "\n";

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
        String name = BookName.getText().toString(); //取得書籍名稱

        EditText BookMonth = (EditText) findViewById(R.id.EditTextBookMonth); //取得BookMonth的參考
        String month = BookMonth.getText().toString(); //取得書籍年齡(string)

        EditText BookPrize = (EditText) findViewById(R.id.EditTextBookPrize); //取得BookPrize的參考
        String prize = BookPrize.getText().toString(); //取得原始價格(string)

        Spinner spinner = (Spinner) findViewById(R.id.SpinnerBookClass);  //取得spinner的參考
        String kind = String.valueOf(spinner.getSelectedItem()); //取得使用者在spinner選擇的項目

        return new Book(name, month, prize, kind);
    }
}
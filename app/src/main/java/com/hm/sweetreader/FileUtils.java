package com.hm.sweetreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * project：MyReader
 * author： FLY
 * date：   2016/5/23
 * time：   14:55
 * purpose：文件操作工具类
 */
public class FileUtils {

    private static String TAG = FileUtils.class.getSimpleName();

    public static void getAllFileName(String path, ArrayList<String> fileName) {
        File file = new File(path);
        File[] files = file.listFiles();
        String[] names = file.list();
        if (names != null)
            fileName.addAll(Arrays.asList(names));
        for (File a : files) {
            if (a.isDirectory()) {
                getAllFileName(a.getAbsolutePath(), fileName);
            }
        }
    }

    public static String[] getFileListName(String path) {
//        List<String> list = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        String[] names = file.list();
        return names;
    }

    public static List<String> getFileList(String path) {
        List<String> list = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        String[] names = file.list();
        for (String name : names) {
            list.add(name);
        }
        return list;
    }

    public static List<String> isSaveFileName(String path, String name_tag) {
        File file = null;
        List<String> folderList = new ArrayList<>();
        if (path == null) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
        } else {
            file = new File(path);
        }

        if (!file.exists()) {
            file.mkdirs();
        }

        File[] array = file.listFiles();
        if (null == array || array.length == 0) {
            return null;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].getName().split("_")[0].equals(name_tag)) {
                // only take file name
//                System.out.println("file name" + array[i].getName());
                folderList.add(array[i].getPath());
            }

        }
        return folderList;

    }

    /**
     * 调用文件选择软件来选择文件
     **/
    public static void showFileChooser(Activity act) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            act.startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                    0X111);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(getActivity(), "请安装文件管理器", Toast.LENGTH_SHORT)
//                    .show();
        }
    }

    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 从路径中获取 文件名
     *
     * @param path
     * @param hasSuffix 是否包括后缀
     * @return
     */
    public static String getFileName(String path, boolean hasSuffix) {
        if (null == path || -1 == path.lastIndexOf("/") || -1 == path.lastIndexOf("."))
            return null;
        if (!hasSuffix)
            return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
        else
            return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * 判断文件是否是相应格式
     *
     * @param path
     * @param formate
     * @return
     */
    public static boolean isFileFormate(String path, String formate) {
        if (null == path || -1 == path.lastIndexOf("/") || -1 == path.lastIndexOf("."))
            return false;
        if (path.substring(path.lastIndexOf(".") + 1) == null) {
            return false;
        }
        if (path.substring(path.lastIndexOf(".") + 1).equals(formate)) {
            return true;
        }
        return false;
    }

    /**
     * 写， 读sdcard目录上的文件，要用FileOutputStream， 不能用openFileOutput
     * 不同点：openFileOutput是在raw里编译过的，FileOutputStream是任何文件都可以
     *
     * @param fileName
     * @param message
     */
    // 写在/mnt/sdcard/目录下面的文件
    public void writeFileSdcard(String fileName, String message) {

        try {

            // FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

            FileOutputStream fout = new FileOutputStream(fileName);

            byte[] bytes = message.getBytes();

            fout.write(bytes);

            fout.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * 读取文件内容
     *
     * @param path
     * @return
     */
    public static String readFileContentByStringBuffer(String path, int num) {
        StringBuffer sb = new StringBuffer(1024);
        File f = new File(path);

        int count = (int) (f.length() / 1024) + (f.length() % 1024 > 0 ? 1 : 0);
        byte[] bytes = new byte[1024];
        if (!isFileExits(path)) {
            return sb.toString();
        }

        InputStream ins = null;
        try {
            ins = new FileInputStream(path);
            while (ins.read(bytes, 0, 1024) > 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                Log.e(TAG, "this text is " + sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public static String readFileContentByBufferReader(String path) throws UnsupportedEncodingException {
        String str = null;
        StringBuffer buffer = new StringBuffer(1024 * 64);
        BufferedReader bre = null;
        String code = null;
        try {
            code = getCharset(path);
            bre = new BufferedReader(new FileReader(path));//此时获取到的bre就是整个文件的缓存流
//            while (bre.ready()) {
//                str = bre.readLine();
            while ((str = bre.readLine()) != null) // 判断最后一行不存在，为空结束循环
            {
//                Log.e("456", "str =" + str);
//                if (str.trim().equals("")) {
//                    buffer.append("\r\n");
//                } else {
                Log.e(TAG, "str is " + str);
                buffer.append(str);
//                }
                buffer.append("\n");
            }
            bre.close();
            Log.e(TAG, "buffer length is " + buffer.length());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Log.e(TAG,"code is "+code);
        return changeCharset(buffer.toString(), code, "UTF-8");
//        return new String(buffer.toString().getBytes(code),"UTF-8");
    }

    /**
     * 读取文件
     *
     * @param path
     * @return
     */
    public static String readFile(String path) {
        File file = new File(path);
        String code = null;
        InputStreamReader read = null;//考虑到编码格式
        StringBuffer buffer = new StringBuffer(1024 * 60);
        try {
//            code = getCharset(path);
            read = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            String line = "";
            while ((lineTxt = bufferedReader.readLine()) != null) {
//                System.out.println(lineTxt);
//                buffer.append(lineTxt);
//                buffer.append("\n");
                String[] person = lineTxt.split(" ");//以空格分隔
                line = "";
                for (int i = 0; i < person.length ; i++) {
                    if (person[i].compareTo("") != 0) {
                        line = line + person[i] + "\n";
                    }
                    buffer.append(line);
                }



            }
            read.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 判断文件编码格式
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String getCharset(String fileName) throws IOException {

        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();

        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param str        待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String changeCharset(String str, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            //用默认字符编码解码字符串。
            byte[] bs = str.getBytes(oldCharset);
            //用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 指定路么下是否存在文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean isFileExits(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists())
                return true;
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 用NIO把20g的文件分割开 生成到temp文件里
     * 然后再用传统的方法去读取每一个小文件
     */
    public static List<String> readFileContentBySection(String path, String name_tag) throws IOException {
        FileInputStream fin = new FileInputStream(path);
        FileChannel fcin = fin.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 64 * 12);
        File file = new File(Contents.cacheFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        int num_name = 0;
        while (true) {
            buffer.clear();
            int flag = fcin.read(buffer);
            if (flag == -1) {
                break;
            }
            buffer.flip();
            FileOutputStream fout = new FileOutputStream(Contents.cacheFilePath + File.separator + name_tag + "_" + num_name);
            FileChannel fcout = fout.getChannel();
            fcout.write(buffer);
            System.out.println(buffer);
            num_name++;
        }
        List<String> fileList = new ArrayList<>();
        File[] array = new File(Contents.cacheFilePath).listFiles();
        if (null == array || array.length == 0) {
            return null;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].getName().split("_")[0].equals(name_tag)) {
                fileList.add(array[i].getPath());
            }

        }
        return fileList;
    }

    /**
     * 用NIO把20g的文件分割开 生成到temp文件里
     * 然后再用传统的方法去读取每一个小文件
     */
    public static List<String> readFileContentByString(String path, String name_tag) throws IOException {

        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        String code = getCharset(path);
        try {

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), code)); // 指定读取文件的编码格式，要和写入的格式一致，以免出现中文乱码,
            String str = null;
            String line = "";
            while ((str = reader.readLine()) != null) {
                String[] person = str.split(" ");//以空格分隔
                line = "";
                for (int i = 0; i < person.length ; i++) {
                    if (person[i].compareTo("") != 0) {
                        line = line + person[i] + "\n";
                    }
                    buffer.append(line);
//                    buffer.append(person[person.length - 1]);
                }
//                line = line + person[person.length - 1];

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String s = buffer.toString();
        String stringCache = null;
        BufferedWriter writer = null;
        if (s.length() <= 0) {
        }
        File file = new File(Contents.cacheFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (s.length() < 1024 * 1024 * 1024 * 2) {
            for (int i = 0; i < 2; i++) {
                stringCache = s.substring(s.length() / 2 * i, s.length() / 2 * (i + 1) - 1);
                try {
                    writer = new BufferedWriter(new FileWriter(new File(Contents.cacheFilePath + File.separator + name_tag + "_" + i)));

                    writer.write(stringCache);


                } catch (Exception e) {

                } finally {
                    writer.close();
                }
            }
        } else if (s.length() < 1024 * 1024 * 1024 * 5) {
            for (int i = 0; i < 5; i++) {
                stringCache = s.substring(s.length() / 5 * i, s.length() / 5 * (i + 1) - 1);
                try {
                    writer = new BufferedWriter(new FileWriter(new File(Contents.cacheFilePath + File.separator + name_tag + "_" + i)));

                    writer.write(stringCache);


                } catch (Exception e) {

                } finally {
                    writer.close();
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                stringCache = s.substring(s.length() / 10 * i, s.length() / 10 * (i + 1) - 1);
                try {
                    writer = new BufferedWriter(new FileWriter(new File(Contents.cacheFilePath + File.separator + name_tag + "_" + i)));

                    writer.write(stringCache);

                } catch (Exception e) {

                } finally {
                    writer.close();
                }
            }
        }
        List<String> fileList = new ArrayList<>();
        File[] array = new File(Contents.cacheFilePath).listFiles();
        if (null == array || array.length == 0) {
            return null;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i].getName().split("_")[0].equals(name_tag)) {
                fileList.add(array[i].getPath());
            }

        }

        return fileList;
    }


    public static long getFileLength(String path) {
        return new File(path).length();
    }

    public static void readFileContentBySectionTwo(String path) throws Exception {
        final int BUFFER_SIZE = 1024;// 缓冲大小为12M

        System.out.println(BUFFER_SIZE);

        File f = new File(path);

        int len = 0;
        Long start = System.currentTimeMillis();
        for (int z = 8; z > 0; z--) {
            MappedByteBuffer inputBuffer = new RandomAccessFile(f, "r")
                    .getChannel().map(FileChannel.MapMode.READ_ONLY,
                            f.length() * (z - 1) / 8, f.length() * 1 / 8);
            byte[] dst = new byte[BUFFER_SIZE];// 每次读出12M的内容
            for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {
                if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {
                    for (int i = 0; i < BUFFER_SIZE; i++)
                        dst[i] = inputBuffer.get(offset + i);
                } else {
                    for (int i = 0; i < inputBuffer.capacity() - offset; i++)
                        dst[i] = inputBuffer.get(offset + i);
                }
                int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
                        : inputBuffer.capacity() % BUFFER_SIZE;

                len += new String(dst, 0, length).length();
                System.out.println(new String(dst, 0, length).length() + "-" + (z - 1) + "-" + (8 - z + 1));
            }
        }
        System.out.println(len);
        long end = System.currentTimeMillis();
        System.out.println("读取文件文件花费：" + (end - start) + "毫秒");
    }


    public static void copyImgFile(String prePath) {
        StringBuffer stringBuffer = new StringBuffer(4 * 1024);
        long segmentLength = 4 * 1024;   //1M大小
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(prePath, "rw");
            File tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                    "books", "564.log");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile, true);

            int currentSegment = 0;
            int count = (int) (randomAccessFile.length() / segmentLength) + (randomAccessFile.length() % segmentLength > 0 ? 1 : 0);
            for (int i = 0; i < count; i++) {
                currentSegment = i + 1;
                System.out.println("分段：" + currentSegment);
                randomAccessFile.seek(i * segmentLength);

                long toal = 0; //当前已读取的长度
                int read = 0; //一次读取的长度
                int bufferLenght = 4 * 1024; //缓存长度
                byte[] buffer = new byte[bufferLenght];
                while ((read = randomAccessFile.read(buffer, 0, bufferLenght)) > 0) {
                    System.out.println("bufferLength:" + bufferLenght);
                    fileOutputStream.write(buffer, 0, read);
////                    InputStream inputStream=new FileInputStream()
////                    BufferedReader reader = new BufferedReader(new FileOutputStream(ins));
////                    String line = null;
////                    while ((line = reader.readLine()) != null) {
////                        stringBuffer.append(line);
////                        stringBuffer.append("\n");
////                    }
//                    for (int j = 0; j < read; j++) {
//                        stringBuffer.append(buffer[j]);
//                    }
                    toal += read;
                    bufferLenght = (int) ((toal + bufferLenght) > segmentLength ? segmentLength - toal : bufferLenght);
//                    Log.e("456","this stringbuffer is "+stringBuffer);
                }

                /*long toal = 0; //当前已读取的长度
                int read = 0; //一次读取的长度
                int bufferLenght = 500 * 1024; //缓存长度
                int bufferLenghtTemp = bufferLenght;
                byte[] buffer = new byte[bufferLenght];
                while ((read = randomAccessFile.read(buffer, 0, bufferLenght)) > 0) {
                    fileOutputStream.write(buffer, 0, read);
                    toal += read;
                    //bufferLenght = (int) ((toal + bufferLenght) > segmentLength ? segmentLength - toal : bufferLenght);
                    if(toal >= segmentLength) break;
                    else
                    {
                        if((toal + bufferLenghtTemp) >= segmentLength) bufferLenght = bufferLenghtTemp;
                        else bufferLenght = (int) (bufferLenghtTemp - toal);
                    }
                }*/


            }
            fileOutputStream.flush();
            fileOutputStream.close();

            System.out.println("源文件大小：" + randomAccessFile.length() + " , 拷贝后文件大小：" + tempFile.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


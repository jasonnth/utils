package com.jasonngo.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by HuuNgo on 10/21/16.
 */

@SuppressLint("NewApi")
public class DocumentUtil {
    public static final int MEGABYTE = 1024 * 1024;

    public static String getPath(final Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
            // Google Driver
            else if (isGoogleDriveDocument(uri)) {
                final Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
                final int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                InputStream inputStream = null;
                try {
                    inputStream = context.getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (inputStream != null) {
                    try {
                        File filePdf = createFileExternalStorage(returnCursor.getString(nameIndex));
                        OutputStream output = new FileOutputStream(filePdf);
                        try {
                            try {
                                byte[] buffer = new byte[MEGABYTE]; // or other buffer size
                                int read;
                                while ((read = inputStream.read(buffer)) != -1) {
                                    output.write(buffer, 0, read);
                                }
                                output.flush();
                            } finally {
                                output.close();
                            }

                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace(); // handle exception, define IOException and others
                        }

                        return filePdf.getPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return null;

            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri) || isPicasaPhotoUri(uri)) {
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        if (inputStream != null) {
                            try {
                                File filePdf = createFileExternalStorage(getFileName(uri.getLastPathSegment()));
                                OutputStream output = new FileOutputStream(filePdf);
                                try {
                                    try {
                                        byte[] buffer = new byte[MEGABYTE]; // or other buffer size
                                        int read;
                                        while ((read = inputStream.read(buffer)) != -1) {
                                            output.write(buffer, 0, read);
                                        }
                                        output.flush();
                                    } finally {
                                        output.close();
                                    }

                                    inputStream.close();
                                } catch (Exception e) {
                                    e.printStackTrace(); // handle exception, define IOException and others
                                }

                                return filePdf.getPath();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            } else if (isGoogleDriveDocument(uri)) {
                final Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
                final int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                InputStream inputStream = null;
                try {
                    inputStream = context.getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (inputStream != null) {
                    try {
                        File filePdf = createFileExternalStorage(returnCursor.getString(nameIndex));
                        OutputStream output = new FileOutputStream(filePdf);
                        try {
                            try {
                                byte[] buffer = new byte[MEGABYTE]; // or other buffer size
                                int read;
                                while ((read = inputStream.read(buffer)) != -1) {
                                    output.write(buffer, 0, read);
                                }
                                output.flush();
                            } finally {
                                output.close();
                            }

                            inputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace(); // handle exception, define IOException and others
                        }

                        return filePdf.getPath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return null;

            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        if ("com.google.android.apps.photos.content".equals(uri.getAuthority())
                || "com.google.android.apps.photos.contentprovider".equals(uri.getAuthority())) {
            return true;
        }
        return false;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGoogleDriveDocument(Uri uri) {
        return (uri.getAuthority() != null && uri.getAuthority().contains("com.google.android.apps.docs.storage"));
    }

    private static boolean isPicasaPhotoUri(Uri uri) {

        return uri != null
                && !TextUtils.isEmpty(uri.getAuthority())
                && (uri.getAuthority().startsWith("com.android.gallery3d")
                || uri.getAuthority().startsWith("com.google.android.gallery3d"));
    }


    public static String getFileName(String path) {
        String filename = "";
        try {
            filename = path.substring(path.lastIndexOf("/") + 1);
            if (!filename.endsWith("jpg") && !filename.endsWith("png")) {
                filename += ".jpg";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    public static File createFileExternalStorage(String fileName) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "pdf");
        folder.mkdir();
        File pdfFile = new File(folder, fileName);
        if (pdfFile.exists()) {
            return pdfFile;
        }

        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdfFile;
    }

}


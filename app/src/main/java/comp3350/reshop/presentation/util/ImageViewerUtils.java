package comp3350.reshop.presentation.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class ImageViewerUtils {
    private ImageViewerUtils() {
        //prevents instantiation
    }

    /**
     * Converts the Uri string into an actual Uri, null check is important as
     * parse() throws a Null Pointer Exception if the parameter is null
     * @param imageUriString string to be converted to Uri
     * @return the Uri of an image
     */
    public static Uri generateUriFromString(String imageUriString) {
        if (imageUriString != null && !imageUriString.isEmpty()) {
            return Uri.parse(imageUriString);
        }

        return null;
    }

    /**
     * Creates a drawable resource out of an image Uri. If any of the parameters are null, the image
     * of the imageView will be set to the default empty picture.
     * @param context the Activity context/instance that this method is being called
     * @param imageView the ImageView widget that will receive the Uri drawable
     * @param imageUri the unique resource identifier of the image
     * @param displayErrors flag to display an error message to the user
     */
    public static void turnUriIntoDrawable(Context context, ImageView imageView, Uri imageUri, boolean displayErrors) {
        try {
            if (context != null && imageView != null && imageUri != null) {
                imageView.setImageDrawable(Drawable.createFromStream(
                        context.getContentResolver().openInputStream(imageUri), null));
            }
        } catch (FileNotFoundException e) {
            if (displayErrors) {
                Toast.makeText(context, "Image can't be shown", Toast.LENGTH_SHORT).show();
            }
            Log.i("ImageViewerUtils", "Loading image failed with exception: " + e.getMessage());
        }
    }

    /**
     * Creates a bitmap of the imageUri, and shows a message if the bitmap can't be created.
     * This is used for images that are from user's device
     * @param context the Activity context/instance that this method is being called
     * @param imageUri the unique resource identifier of the image
     * @return the bitmap created
     */
    public static Bitmap getBitmapFromUri(Context context, Uri imageUri) {
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            Toast.makeText(context, "Image can't be processed", Toast.LENGTH_SHORT).show();
        }

        return bitmap;
    }

    /**
     * Stores the compressed version of the bitmap in the app's private storage
     * and creates a Unique resource identifier (Uri) for that bitmap.
     * This is used for images that are from user's device
     * @param context the Activity context/instance that this method is being called
     * @param bitmap the bitmap of an image
     * @return the the unique resource identifier of the bitmap
     */
    public static Uri getImageUriFromBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "File", null);
        return generateUriFromString(path);
    }
}


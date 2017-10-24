package ru.sergeykamyshov.fivecards.model;

import android.graphics.Bitmap;

/**
 * Класс описывает модель карточки изображения
 */
public class ImageType implements CardType {

    private Bitmap mImage;

    public ImageType(Bitmap image) {
        mImage = image;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }
}

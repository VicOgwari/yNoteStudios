package com.midland.ynote.Utilities;

public interface ItemTouchAdapter {

    void onItemMove(int fromPos, int toPos);

    void onItemSwipe(int pos);
}

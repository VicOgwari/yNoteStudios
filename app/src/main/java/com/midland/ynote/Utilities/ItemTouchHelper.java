package com.midland.ynote.Utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.midland.ynote.Activities.PhotoDoc;

public class ItemTouchHelper extends androidx.recyclerview.widget.ItemTouchHelper.Callback {

    private final ItemTouchAdapter mAdapter;
    private PhotoDoc con;

    public ItemTouchHelper(ItemTouchAdapter mAdapter, PhotoDoc con) {
        this.mAdapter = mAdapter;
        this.con = con;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = androidx.recyclerview.widget.ItemTouchHelper.UP | androidx.recyclerview.widget.ItemTouchHelper.DOWN;
        final int swipeFlags = androidx.recyclerview.widget.ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
       mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemSwipe(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(ContextCompat
                .getColor(viewHolder.itemView.getContext(), android.R.color.holo_orange_light));

    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG){
            assert viewHolder != null;
            viewHolder.itemView.setBackgroundColor(ContextCompat
                    .getColor(viewHolder.itemView.getContext(), android.R.color.holo_green_light));
        }
    }
}

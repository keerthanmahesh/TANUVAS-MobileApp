package com.mahesh.keerthan.tanvasfarmerapp;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SimpleItem extends DrawerItem<SimpleItem.ViewHolder> {

    private int selectedItemIconTint;
    private int selectedItemTextTint;

    private int normalItemIconTint;
    private int normalItemTextTint;

    private int selectedBackgroundColor;
    private int normalBackgroundColor;

    private Drawable icon;
    private String title;
    private RelativeLayout layout;

    public SimpleItem(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;

    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {
        holder.title.setText(title);
        holder.icon.setImageDrawable(icon);
        holder.title.setTextColor(isChecked ? Color.parseColor("#FFFFFF"): Color.parseColor("#FFFFFF"));
        //holder.icon.setColorFilter(isChecked ? Color.parseColor("#FFFFFF"): Color.parseColor("#000000"));
       // holder.icon.setColorFilter(isChecked ? Color.parseColor("#FFFFFF"): Color.parseColor("#FFFFFF"), PorterDuff.Mode.DST_ATOP);
        holder.layout.setBackgroundResource(isChecked? R.drawable.rounded_layout_selected:R.drawable.rounded_layout_not_selected );
    }

    public SimpleItem withSelectedIconTint(int selectedItemIconTint) {
        this.selectedItemIconTint = selectedItemIconTint;
        return this;
    }

    public SimpleItem withSelectedTextTint(int selectedItemTextTint) {
        this.selectedItemTextTint = selectedItemTextTint;
        return this;
    }

    public SimpleItem withIconTint(int normalItemIconTint) {
        this.normalItemIconTint = normalItemIconTint;
        return this;
    }

    public SimpleItem withTextTint(int normalItemTextTint) {
        this.normalItemTextTint = normalItemTextTint;
        return this;
    }



    static class ViewHolder extends drawerAdapter.ViewHolder {

        private ImageView icon;
        private TextView title;
        private RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout);

        }
    }
}
